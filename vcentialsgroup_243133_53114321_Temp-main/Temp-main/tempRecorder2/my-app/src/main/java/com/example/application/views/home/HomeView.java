package com.example.application.views.home;

import com.example.application.data.*;
import com.example.application.services.*;
import com.example.application.views.MainLayout;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.confirmdialog.ConfirmDialog;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.*;
import com.vaadin.flow.component.grid.dataview.GridListDataView;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.NativeLabel;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.Notification.Position;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.splitlayout.SplitLayout;
import com.vaadin.flow.component.textfield.IntegerField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.textfield.TextFieldVariant;
import com.vaadin.flow.component.timepicker.TimePicker;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.PropertyId;
import com.vaadin.flow.data.binder.ValidationException;
import com.vaadin.flow.data.converter.StringToIntegerConverter;
import com.vaadin.flow.data.provider.SortDirection;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouteAlias;
import com.vaadin.flow.spring.data.VaadinSpringDataHelpers;

import java.sql.Date;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import com.vaadin.flow.spring.security.AuthenticationContext;
import jakarta.annotation.security.PermitAll;

import org.apache.poi.hssf.record.HeaderRecord;
import org.apache.poi.ss.formula.functions.T;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

@PageTitle("Home")
@Route(value = "", layout = MainLayout.class)
@RouteAlias(value = "", layout = MainLayout.class)
@PermitAll
public class HomeView extends Div {

    private final Grid<RecordDTO> grid = new Grid<>(RecordDTO.class, false);

    private boolean isGridSelected = false;

    private ComboBox<UserInfo> userInfoComboBox = new ComboBox<>("userInfo");
    private ComboBox<Locations> locationsComboBox = new ComboBox<>("locations");
    private ComboBox<Rooms> roomsComboBox = new ComboBox<>("rooms");
    @PropertyId("machinesId")
    private ComboBox<Machines> machinesComboBox = new ComboBox<>("machines");
    @PropertyId("machineTempF")
    private IntegerField machineTempF;
    @PropertyId("roomTempF")
    private IntegerField roomTempF;
    @PropertyId("date")
    private DatePicker date;
    @PropertyId("time")
    private TimePicker time;

    private final ConfirmDialog deleteDialog = new ConfirmDialog();

    private final Button cancel = new Button("Clear");
    private final Button save = new Button("Save");
    private final Button deleteButton = new Button("Delete Data", e -> openDeleteDialog());
    private final Button previewButton = new Button("Export Data", e -> previewData());

    private final BeanValidationBinder<DataEntries> dataEntriesBeanValidationBinder;

    private DataEntries dataEntries;

    private UserInfo selectedUserInfo;
    private Locations selectedLocation;
    private Rooms selectedRoom;
    private Machines selectedMachine;

    private final DataEntriesService dataEntriesService;
    private final LocationsService locationsService;
    private final RoomsService roomsService;
    private final MachinesService machinesService;
    private final UserInfoService userInfoService;
    private final RecordServices recordServices;
    private final SharedGridDataService sharedGridDataService;

    private final transient AuthenticationContext authContext;

    public HomeView(DataEntriesService dataEntriesService, LocationsService locationsService, RoomsService roomsService, MachinesService machinesService, UserInfoService userInfoService, RecordServices recordServices, SharedGridDataService sharedGridDataService, AuthenticationContext authContext) {

        this.authContext = authContext;
        this.dataEntriesService = dataEntriesService;
        this.locationsService = locationsService;
        this.roomsService = roomsService;
        this.machinesService = machinesService;
        this.userInfoService = userInfoService;
        this.recordServices = recordServices;
        this.sharedGridDataService = sharedGridDataService;

        grid.setSelectionMode(Grid.SelectionMode.MULTI);
        GridMultiSelectionModel<RecordDTO> selectionModel = (GridMultiSelectionModel<RecordDTO>)grid.getSelectionModel();
        selectionModel.setDragSelect(true);
        grid.setHeightFull();
        grid.setMultiSort(true, Grid.MultiSortPriority.APPEND);

        itemSelected();

        grid.addSelectionListener(event -> itemSelected());

        //enables the ability to delete records if the current user account is an admin
        authContext.getAuthenticatedUser(UserDetails.class).ifPresent(user -> {
            boolean isAdmin = user.getAuthorities().stream()
                    .anyMatch(grantedAuthority -> "ROLE_ADMIN".equals(grantedAuthority.getAuthority()));
            if (isAdmin) {
                deleteButton.setPrefixComponent(VaadinIcon.TRASH.create());
                deleteButton.addThemeVariants(ButtonVariant.LUMO_ERROR);
                add(deleteButton);
            }
            else {
                deleteButton.setVisible(false);
            }
        });

        previewButton.setPrefixComponent(VaadinIcon.PRINT.create());
        add(previewButton);


        deleteDialog.setHeader("Delete");
        deleteDialog.setText("Are you sure you want to delete these items?");
        deleteDialog.setCancelable(true);
        deleteDialog.addCancelListener(event -> confirmDelete(true));

        deleteDialog.setConfirmText("Delete");
        deleteDialog.setConfirmButtonTheme("error primary");
        deleteDialog.addConfirmListener(event -> confirmDelete(false));


        addClassNames("master-detail-view");
        // Create UI
        SplitLayout splitLayout = new SplitLayout();
        splitLayout.setSplitterPosition(70);

        createGridLayout(splitLayout);
        createEditorLayout(splitLayout);

        add(splitLayout);

        //Checks if the user exists
        //If not, then redirect to login page
        try {
            userInfoComboBox.setValue(userInfoService.getUsername(SecurityContextHolder.getContext().getAuthentication().getName()));
        }
        catch (EmptyResultDataAccessException e){
            UI.getCurrent().getPage().setLocation("login");
        }

        // Configure Grid
        Grid.Column<RecordDTO> dateColumn = grid.addColumn("date").setAutoWidth(true);
        Grid.Column<RecordDTO> timeColumn = grid.addColumn("time").setAutoWidth(true);
        Grid.Column<RecordDTO> machineColumn = grid.addColumn("machine").setAutoWidth(true);
        Grid.Column<RecordDTO> machineTempFColumn = grid.addColumn("machineTempF").setAutoWidth(true);
        Grid.Column<RecordDTO> roomColumn = grid.addColumn("room").setAutoWidth(true);
        Grid.Column<RecordDTO> roomTempFColumn = grid.addColumn("roomTempF").setAutoWidth(true);
        Grid.Column<RecordDTO> locationColumn = grid.addColumn("location").setAutoWidth(true);
        Grid.Column<RecordDTO> usernameColumn = grid.addColumn("username").setAutoWidth(true);
        machineTempFColumn.setHeader("Machine ℉");
        roomTempFColumn.setHeader("Room ℉");


        GridListDataView<RecordDTO> dataView = grid.setItems(recordServices.getRecords());
        dataView.addSortOrder(RecordDTO::getDate,SortDirection.DESCENDING);
        dataView.addSortOrder(RecordDTO::getTime,SortDirection.DESCENDING);
        RecordFilter recordFilter = new RecordFilter(dataView);

        GridSortOrder<RecordDTO> dateSort = new GridSortOrder<RecordDTO>(dateColumn,SortDirection.DESCENDING);
        GridSortOrder<RecordDTO> timeSort = new GridSortOrder<RecordDTO>(timeColumn,SortDirection.DESCENDING);
        List<GridSortOrder<RecordDTO>> gridSortOrders = new ArrayList<GridSortOrder<RecordDTO>>();
        gridSortOrders.add(dateSort);
        gridSortOrders.add(timeSort);
        grid.sort(gridSortOrders);
        grid.getHeaderRows().clear();

        HeaderRow headerRow = grid.appendHeaderRow();

        headerRow.getCell(dateColumn).setComponent(createFilterHeader(recordFilter::setDate));
        headerRow.getCell(timeColumn).setComponent(createFilterHeader(recordFilter::setTime));
        headerRow.getCell(machineColumn).setComponent(createFilterHeader(recordFilter::setMachine));
        headerRow.getCell(machineTempFColumn).setComponent(createFilterHeader(recordFilter::setMachineTempF));
        headerRow.getCell(roomColumn).setComponent(createFilterHeader(recordFilter::setRoom));
        headerRow.getCell(roomTempFColumn).setComponent(createFilterHeader(recordFilter::setRoomTempF));
        headerRow.getCell(locationColumn).setComponent(createFilterHeader(recordFilter::setLocation));
        headerRow.getCell(usernameColumn).setComponent(createFilterHeader(recordFilter::setUsername));

        grid.addThemeVariants(GridVariant.LUMO_NO_BORDER);

        // Configure Form
        dataEntriesBeanValidationBinder = new BeanValidationBinder<>(DataEntries.class);
        dataEntriesBeanValidationBinder.bindInstanceFields(this);

        userInfoComboBox.setReadOnly(true);
        selectedUserInfo = userInfoComboBox.getValue();

        //gets the value currently selected from the machinesComboBox
        //also enables the room combobox component while also setting its items with the rooms currently registered within the location selected
        locationsComboBox.addValueChangeListener(e -> {
            selectedLocation = e.getValue();
            roomsComboBox.setEnabled(true);
            if (selectedLocation != null) {
                roomsComboBox.setItems(roomsService.getRoomsWithLocationID(selectedLocation.getLocationId()));
                roomsComboBox.setItemLabelGenerator(Rooms::getRoomName);
            }
            machinesComboBox.setValue(null);
            machinesComboBox.setEnabled(false);
        });

        //gets the value currently selected from the machinesComboBox
        //also enables the machine combobox component while also setting its items with the machines currently registered within the room selected
        roomsComboBox.addValueChangeListener(e -> {
            selectedRoom = e.getValue();
            machinesComboBox.setEnabled(true);
            if (selectedRoom != null) {
                machinesComboBox.setItems(machinesService.getMachinesWithRoomID(selectedRoom.getRoomId()));
                machinesComboBox.setItemLabelGenerator(Machines::getMachineName);
            }
        });

        //gets the value currently selected from the machinesComboBox
        machinesComboBox.addValueChangeListener(e -> {
            selectedMachine = e.getValue();
            checkIfFormFilledOut();

        });

        //checks if these values change
        machineTempF.addValueChangeListener(event -> checkIfFormFilledOut());
        roomTempF.addValueChangeListener(event -> checkIfFormFilledOut());
        date.addValueChangeListener(event -> checkIfFormFilledOut());
        time.addValueChangeListener(event -> checkIfFormFilledOut());

        //clears the form
        cancel.addClickListener(e -> {
            clearForm();
            refreshGrid();
        });


        //saves the information from the form into the database while checking for conflicts or invalid inputs
        save.addClickListener(e -> {
            try {
                if (this.dataEntries == null) {
                    this.dataEntries = new DataEntries();
                }
                dataEntriesBeanValidationBinder.writeBean(this.dataEntries);
                this.dataEntries.setUserId(selectedUserInfo.getUserId());
                this.dataEntries.setMachineId(selectedMachine.getMachineId());
                dataEntriesService.update(this.dataEntries);
                clearForm();
                refreshGrid();
                Notification.show("Data added");
            } catch (ObjectOptimisticLockingFailureException exception) {
                Notification n = Notification.show(
                        "Error adding the data. Somebody else has updated the record while you were making changes.");
                n.setPosition(Position.MIDDLE);
                n.addThemeVariants(NotificationVariant.LUMO_ERROR);
            } catch (ValidationException validationException) {
                Notification.show("Failed to add the data. Check again that all values are valid");
            }
        });
    }

    private void createEditorLayout(SplitLayout splitLayout) {
        Div editorLayoutDiv = new Div();
        editorLayoutDiv.setClassName("editor-layout");

        Div editorDiv = new Div();
        editorDiv.setClassName("editor");
        editorLayoutDiv.add(editorDiv);

        FormLayout formLayout = new FormLayout();
        //creates the components used in the form
        userInfoComboBox = new ComboBox<>("User");
        locationsComboBox = new ComboBox<>("Location");
        roomsComboBox = new ComboBox<>("Room");
        machinesComboBox = new ComboBox<>("Machine");
        machineTempF = new IntegerField("Machine ℉");
        roomTempF = new IntegerField("Room ℉");
        date = new DatePicker("Date");
        time = new TimePicker("Time");

        //disabled the room and machine combobox as a location must be selected beforehand
        roomsComboBox.setEnabled(false);
        roomsComboBox.setPrefixComponent(VaadinIcon.STORAGE.create());
        machinesComboBox.setEnabled(false);
        machinesComboBox.setPrefixComponent(VaadinIcon.COG.create());

        //sets the list of items in the location combobox to all current locations registered.
        locationsComboBox.setItems(query -> locationsService.list(
                PageRequest.of(query.getPage(), query.getPageSize(), VaadinSpringDataHelpers.toSpringDataSort(query))).stream());
        locationsComboBox.setItemLabelGenerator(Locations::getLocationName);
        locationsComboBox.setPrefixComponent(VaadinIcon.BUILDING.create());

        //sets the list of items in the location combobox to all current locations registered.
        userInfoComboBox.setItems(query -> userInfoService.list(
                PageRequest.of(query.getPage(), query.getPageSize(), VaadinSpringDataHelpers.toSpringDataSort(query))).stream());
        userInfoComboBox.setItemLabelGenerator(UserInfo::getUsername);
        userInfoComboBox.setPrefixComponent(VaadinIcon.USER.create());


        formLayout.add(userInfoComboBox,locationsComboBox,roomsComboBox,machinesComboBox, machineTempF,roomTempF, date,time);

        editorDiv.add(formLayout);
        createButtonLayout(editorLayoutDiv);

        splitLayout.addToSecondary(editorLayoutDiv);
    }

    private void createButtonLayout(Div editorLayoutDiv) {
        HorizontalLayout buttonLayout = new HorizontalLayout();
        buttonLayout.setClassName("button-layout");
        cancel.setPrefixComponent(VaadinIcon.CLOSE.create());
        save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        save.setPrefixComponent(VaadinIcon.UPLOAD.create());
        buttonLayout.add(save, cancel);
        save.setEnabled(false);
        editorLayoutDiv.add(buttonLayout);
    }

    private void createGridLayout(SplitLayout splitLayout) {
        Div wrapper = new Div();
        wrapper.setClassName("grid-wrapper");
        splitLayout.addToPrimary(wrapper);
        wrapper.add(grid);
    }

    private void refreshGrid() {
        //grid.select(null);
        grid.setItems(recordServices.getRecords());
        //grid.getDataProvider().refreshAll();
    }

    /**
     * Clears the form
     */
    private void clearForm() {
        populateForm(null);
        machinesComboBox.setValue(null);
        roomsComboBox.setValue(null);
        locationsComboBox.setValue(null);
        roomsComboBox.setEnabled(false);
        machinesComboBox.setEnabled(false);
    }

    private void populateForm(DataEntries value) {
        this.dataEntries = value;
        dataEntriesBeanValidationBinder.readBean(this.dataEntries);

    }

    /**
     * Checks if the form is completed to determine if the save button should be enabled or not.
     */
    private void checkIfFormFilledOut(){
        boolean hasSelectedMachine = false;
        boolean hasMachineTemp = false;
        boolean hasRoomTemp = false;
        boolean hasDate = false;
        boolean hasTime = false;
        if (machinesComboBox.getValue() != machinesComboBox.getEmptyValue()){
            hasSelectedMachine = true;
        }
        if (!Objects.equals(machineTempF.getValue(), machineTempF.getEmptyValue())){
            hasMachineTemp = true;
        }
        if (!Objects.equals(roomTempF.getValue(), roomTempF.getEmptyValue())){
            hasRoomTemp = true;
        }
        if (date.getValue() != date.getEmptyValue()){
            hasDate = true;
        }
        if (time.getValue() != time.getEmptyValue()){
            hasTime = true;
        }

        save.setEnabled(hasSelectedMachine && hasMachineTemp && hasRoomTemp && hasDate && hasTime);

    }

    //navigates to the preview data page
    private void previewData() {
        //List<RecordDTO> sortedRecords = grid.getListDataView().getItems().collect(Collectors.toList());
        List<RecordDTO> sortedRecords = grid.getSelectedItems().stream().toList();
        sharedGridDataService.setRecords(sortedRecords);
        UI.getCurrent().navigate("preview");
    }
    //deletes selected data
    private void deleteData() {
        Set<RecordDTO> selectedItems = grid.getSelectedItems();

        if (!selectedItems.isEmpty()) {
            for (RecordDTO recordDTO: selectedItems){
                dataEntriesService.delete(recordDTO.getDataEntriesId());
            }
            clearForm();
            refreshGrid();
            Notification.show("Data deleted");
            //UI.getCurrent().navigate(HomeView.class);

        }
    }

    private void confirmDelete(boolean delete) {
        if (!delete){
            deleteData();
        }
        deleteDialog.close();
    }

    private void openDeleteDialog(){
        deleteDialog.setText("Are you sure you want to delete (" + grid.getSelectedItems().stream().count() + ") record(s)?");
        deleteDialog.open();
    }

    //toggles the delete and preview button depending on if items are selected or not
    private void itemSelected(){
        if (grid.getSelectedItems().isEmpty()){
            deleteButton.setEnabled(false);
            previewButton.setEnabled(false);
        } else {
            deleteButton.setEnabled(true);
            previewButton.setEnabled(true);
        }
    }

    private static Component createFilterHeader(Consumer<String> filterChangeConsumer) {        
        TextField textField = new TextField();
        textField.setValueChangeMode(ValueChangeMode.EAGER);
        textField.setClearButtonVisible(true);
        textField.addThemeVariants(TextFieldVariant.LUMO_SMALL);
        textField.setWidthFull();
        textField.getStyle().set("max-width", "100%");
        textField.addValueChangeListener(
                e -> filterChangeConsumer.accept(e.getValue()));
        VerticalLayout layout = new VerticalLayout(textField);
        layout.getThemeList().clear();
        layout.getThemeList().add("spacing-xs");

        return layout;
    }

    private static class RecordFilter {
        private final GridListDataView<RecordDTO> dataView;

        private String date;
        private String time;
        private String machine;
        private String machineTempF;
        private String room;
        private String roomTempF;
        private String location;
        private String username;

        public RecordFilter(GridListDataView<RecordDTO> dataView) {
            this.dataView = dataView;
            this.dataView.addFilter(this::test);            
        }

        public void setDate(String date) {
            this.date = date;
            this.dataView.refreshAll();
        }

        public void setTime(String time) {
            this.time = time;
            this.dataView.refreshAll();
        }

        public void setMachine(String machine) {
            this.machine = machine;
            this.dataView.refreshAll();
        }

        public void setMachineTempF(String machineTempF) {
            this.machineTempF = machineTempF;
            this.dataView.refreshAll();
        }

        public void setRoom(String room) {
            this.room = room;
            this.dataView.refreshAll();
        }

        public void setRoomTempF(String roomTempF) {
            this.roomTempF = roomTempF;
            this.dataView.refreshAll();
        }

        public void setLocation(String location) {
            this.location = location;
            this.dataView.refreshAll();
        }

        public void setUsername(String username) {
            this.username = username;
            this.dataView.refreshAll();
        }

        public boolean test(RecordDTO recordDTO){
            boolean matchesDate = matches(recordDTO.getDate().toString(), date);
            boolean matchesTime = matches(recordDTO.getTime().toString(), time);
            boolean matchesMachine = matches(recordDTO.getMachine(), machine);
            boolean matchesMachineTempF = matches(Integer.toString(recordDTO.getMachineTempF()), machineTempF);
            boolean matchesRoom = matches(recordDTO.getRoom(), room);
            boolean matchesRoomTempF = matches(Integer.toString(recordDTO.getRoomTempF()), roomTempF);
            boolean matchesLocation = matches(recordDTO.getLocation().toString(), location);
            boolean matchesUsername = matches(recordDTO.getUsername().toString(), username);

            return matchesDate && matchesTime && matchesMachine && matchesMachineTempF && matchesRoom && matchesRoomTempF && matchesLocation && matchesUsername;
        }

        private boolean matches(String value, String searchTerm) {
            return searchTerm == null || searchTerm.isEmpty() || value.toLowerCase().contains(searchTerm.toLowerCase());
        }
    }
}
