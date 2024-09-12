package com.example.application.views.admin;

import com.example.application.data.*;
import com.example.application.services.*;
import com.example.application.views.MainLayout;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.confirmdialog.ConfirmDialog;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridMultiSelectionModel;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.grid.HeaderRow;
import com.vaadin.flow.component.grid.dataview.GridListDataView;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.Notification.Position;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.splitlayout.SplitLayout;
import com.vaadin.flow.component.textfield.EmailField;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.textfield.TextFieldVariant;
import com.vaadin.flow.component.timepicker.TimePicker;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.PropertyId;
import com.vaadin.flow.data.binder.ValidationException;
import com.vaadin.flow.data.converter.StringToIntegerConverter;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.*;
import com.vaadin.flow.spring.data.VaadinSpringDataHelpers;
import com.vaadin.flow.spring.security.AuthenticationContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import jakarta.annotation.security.PermitAll;
import jakarta.annotation.security.RolesAllowed;
import org.springframework.data.domain.PageRequest;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.function.Consumer;
import java.util.stream.Collectors;

@PageTitle("Admin")
@Route(value = "admin", layout = MainLayout.class)
@RouteAlias(value = "admin", layout = MainLayout.class)
@RolesAllowed("ROLE_ADMIN")
public class AdminView extends Div implements BeforeEnterObserver {

    private final Grid<UserInfo> grid = new Grid<>(UserInfo.class, false);

    private final TextField usernameField = new TextField("Username"/*,event -> isValid()*/);
    private final EmailField emailField = new EmailField("Email",event -> isValid());
    private final TextField vcidField = new TextField("VCID",event -> isValid());

    private final Checkbox enabledBox = new Checkbox("Account Enabled");

    private final Checkbox adminBox = new Checkbox("Admin Account");


    private final Button cancel = new Button("Cancel");
    private final Button save = new Button("Save");

    private final Button deleteButton = new Button("Delete Data", e -> openDeleteDialog());

    private final Button editButton = new Button(("Edit"),event -> toggleEdit());

    private boolean isEditMode = false;
    private final ConfirmDialog deleteDialog = new ConfirmDialog();
    private final BeanValidationBinder<UserInfo> userInfoBeanValidationBinder;

    private UserInfo userInfo;

    private final UserInfoService userInfoService;

    private final AuthoritiesService authoritiesService;

    @Autowired
    private UserInfoRepository userInfoRepository;

    @Autowired
    private AuthoritiesRepository authoritiesRepository;


    private UserInfo selectedUser;



// @metadata, route=shorthand for MainLayout,  @permitall=security settings

    public AdminView(UserInfoService userInfoService, UserInfoRepository userInfoRepository, SharedGridDataService sharedGridDataService, AuthenticationContext authContext, AuthoritiesService authoritiesService) {

        this.userInfoRepository = userInfoRepository;
        this.userInfoService = userInfoService;
        this.authoritiesService = authoritiesService;



        grid.addSelectionListener(selection -> {
            Optional<UserInfo> selectedUser = selection.getFirstSelectedItem();
            selectedUser.ifPresent(this::setFields);
        });

        add(deleteButton);
        add(editButton);

        //enables the ability to delete records if the current user account is an Admin
        authContext.getAuthenticatedUser(UserDetails.class).ifPresent(user -> {
            boolean isAdmin = user.getAuthorities().stream()
                    .anyMatch(grantedAuthority -> "ROLE_Admin".equals(grantedAuthority.getAuthority()));
            if (isAdmin) {
                Button deleteButton = new Button("Delete Data", e -> deleteData());
                add(deleteButton);
            }
        });





        addClassNames("master-detail-view");
        // Create UI
        SplitLayout splitLayout = new SplitLayout();
        splitLayout.setSplitterPosition(70);

        createGridLayout(splitLayout);
        createEditorLayout(splitLayout);

        add(splitLayout);

        //Checks if the user exists
        //If not, then redirect to login page

        // Configure Grid
        Grid.Column<UserInfo> userIdColumn = grid.addColumn("userId").setAutoWidth(true);
        Grid.Column<UserInfo> usernameColumn = grid.addColumn("username").setAutoWidth(true);
        Grid.Column<UserInfo> emailColumn = grid.addColumn("email").setAutoWidth(true);
        Grid.Column<UserInfo> vcidColumn = grid.addColumn("vcid").setAutoWidth(true);
        Grid.Column<UserInfo> passwordTokenColumn = grid.addColumn("passwordToken").setAutoWidth(true);
        Grid.Column<UserInfo> enabledColumn = grid.addColumn("enabled").setAutoWidth(true);
        Grid.Column<UserInfo> isAdminColumn = grid.addColumn("isAdmin").setAutoWidth(true);


        GridListDataView<UserInfo> dataView = grid.setItems(userInfoRepository.findAll());
        //RecordFilter recordFilter = new RecordFilter(dataView);

        grid.getHeaderRows().clear();
        HeaderRow headerRow = grid.appendHeaderRow();

        Anchor anchor = new Anchor("/h2-console", "Access H2 console");
        anchor.getElement().setAttribute("target", "_blank");
        add(anchor);

//        headerRow.getCell(userIdColumn).setComponent(createFilterHeader(recordFilter::setDate));
//        headerRow.getCell(usernameColumn).setComponent(createFilterHeader(recordFilter::setTime));
//        headerRow.getCell(vcidColumn).setComponent(createFilterHeader(recordFilter::setMachine));
//        headerRow.getCell(emailColumn).setComponent(createFilterHeader(recordFilter::setMachineTempF));
//        headerRow.getCell(isAdminColumn).setComponent(createFilterHeader(recordFilter::setRoom));
//        headerRow.getCell(enabledColumn).setComponent(createFilterHeader(recordFilter::setRoomTempF));
//        headerRow.getCell(passwordTokenColumn).setComponent(createFilterHeader(recordFilter::setLocation));


        grid.addThemeVariants(GridVariant.LUMO_NO_BORDER);

        // Configure Form
        userInfoBeanValidationBinder = new BeanValidationBinder<>(UserInfo.class);

        // Bind fields. This is where you'd define e.g. validation rules
       //userInfoBeanValidationBinder.forField(machineTempF).withConverter(new StringToIntegerConverter("Only numbers are allowed"))
//                .bind("machineTempF");
//
//        userInfoBeanValidationBinder.forField(roomTempF).withConverter(new StringToIntegerConverter("Only numbers are allowed"))
//                .bind("roomTempF");

        //userInfoBeanValidationBinder.bindInstanceFields(this);

        //gets the value currently selected from the machinesComboBox
        //also enables the room combobox component while also setting its items with the rooms currently registered within the location selected


        //gets the value currently selected from the machinesComboBox
        //also enables the machine combobox component while also setting its items with the machines currently registered within the room selected




        deleteDialog.setHeader("Delete");
        deleteDialog.setText("Are you sure you want to delete these items?");
        deleteDialog.setCancelable(true);
        deleteDialog.addCancelListener(event -> confirmDelete(true));
        deleteDialog.setConfirmText("Delete");
        deleteDialog.setConfirmButtonTheme("error primary");
        deleteDialog.addConfirmListener(event -> confirmDelete(false));

        //clears the form
        cancel.addClickListener(e -> {
            clearForm();
            refreshGrid();
        });
        save.setPrefixComponent(VaadinIcon.UPLOAD.create());

        save.addClickListener(event -> saveInfo(selectedUser));

    }

    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        String DATAENTRIES_ID = "userInfoID";
        Optional<String> userInfoId = event.getRouteParameters().get(DATAENTRIES_ID);
        if (userInfoId.isPresent()) {
            Optional<UserInfo> userInfoFromBackend = userInfoService.get(userInfoId.get().getBytes());
            if (userInfoFromBackend.isPresent()) {
                populateForm(userInfoFromBackend.get());
            } else {
                Notification.show(String.format("The requested userInfo was not found, ID = %s", userInfoId.get()),
                        3000, Position.BOTTOM_START);
                // when a row is selected but the data is no longer available,
                // refresh grid
                refreshGrid();
                event.forwardTo(AdminView.class);
            }
        }
        System.out.println("before");
    }

    private void createEditorLayout(SplitLayout splitLayout) {
        Div editorLayoutDiv = new Div();
        editorLayoutDiv.setClassName("editor-layout");

        Div editorDiv = new Div();
        editorDiv.setClassName("editor");
        editorLayoutDiv.add(editorDiv);

        FormLayout formLayout = new FormLayout();
        //creates the components used in the form
        usernameField.setPrefixComponent(VaadinIcon.USER.create());
        usernameField.setReadOnly(true);
        emailField.setPrefixComponent(VaadinIcon.ENVELOPE.create());
        emailField.setErrorMessage("Enter a valid email address");

        vcidField.setPrefixComponent(VaadinIcon.DIPLOMA.create());
        vcidField.setMaxLength(9);
        vcidField.setMinLength(9);
        vcidField.setAllowedCharPattern("[0-9V]");
        vcidField.setPattern("^[V]+[0-9]{8}");
        vcidField.setErrorMessage("Enter a valid VCID");


        formLayout.add(usernameField,emailField,vcidField,enabledBox,adminBox);

        editorDiv.add(formLayout);
        createButtonLayout(editorLayoutDiv);

        splitLayout.addToSecondary(editorLayoutDiv);
    }

    private void setFields(UserInfo info){
        selectedUser = info;
        usernameField.setValue(info.getUsername());
        emailField.setValue(info.getEmail());


        enabledBox.setValue(info.getEnabled());
        adminBox.setValue(info.getIsAdmin());
    }

    private void isValid() {
        save.setEnabled( !usernameField.isInvalid() && !emailField.isInvalid() && !vcidField.isInvalid());
    }

    private boolean checkForExistingAccount(String username,String email,String vcid,UserInfo userInfo){
        if (userInfoRepository.existsByEmail(email) && !userInfo.getEmail().equals(email)){
            Notification.show("Account with that email address already exists!");
            return false;
        }

        if (userInfoRepository.existsByUsername(username) && !userInfo.getUsername().equals(username)){
            Notification.show("Account with that username already exists!");
            return false;
        }

        String userVcid = "";
        if (userInfo.getVcid() != null){
            userVcid = userInfo.getVcid();
        }

        if (userInfoRepository.existsByVcid(vcid) && !userVcid.equals(vcid)){
            Notification.show("Account with that VCID already exists!");
            return false;
        }

        return true;
    }

    private void createButtonLayout(Div editorLayoutDiv) {
        HorizontalLayout buttonLayout = new HorizontalLayout();
        buttonLayout.setClassName("button-layout");
        cancel.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        buttonLayout.add(save, cancel);
        //save.setEnabled(false);
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
        grid.setItems(userInfoRepository.findAll());
        //grid.getDataProvider().refreshAll();
    }

    private void openDeleteDialog(){
        deleteDialog.setText("Are you sure you want to delete (" + (long) grid.getSelectedItems().size() + ") users(s)?");
        deleteDialog.open();
    }

    /**
     * Clears the form
     */
    private void clearForm() {
        populateForm(null);

    }

    private void saveInfo(UserInfo selectedUser){

        String usernameValue = usernameField.getValue();
        String emailValue = emailField.getValue();
        String vcidValue = vcidField.getValue();
        boolean enabledValue = enabledBox.getValue();
        boolean adminValue = adminBox.getValue();

        if(checkForExistingAccount(usernameValue,emailValue,vcidValue,selectedUser)) {


            Authorities authorities = authoritiesService.getUsername(userInfoService.get(selectedUser.getUserId()).get().getUsername());

            selectedUser.setIsAdmin(adminValue);
            if (adminValue) {
                authorities.setAuthority("ROLE_ADMIN");
            } else {
                authorities.setAuthority("ROLE_USER");
            }

            selectedUser.setEmail(emailValue);

            if (!vcidValue.isEmpty()) {
                selectedUser.setVcid(vcidValue);
            }
            else {
                selectedUser.setVcid(null);
            }
            selectedUser.setEnabled(enabledValue);


            selectedUser.setUsername(usernameValue);
            authorities.setUsername(usernameValue);

            authoritiesRepository.save(authorities);
            userInfoRepository.save(selectedUser);

            Notification.show(usernameValue + "updated successfully");
            clearForm();
            refreshGrid();
        }
    }

    private void confirmDelete(boolean delete) {
        if (!delete){
            deleteData();
        }
        deleteDialog.close();
    }

    private void populateForm(UserInfo value) {
        emailField.setValue(emailField.getEmptyValue());
        vcidField.setValue(vcidField.getEmptyValue());
        adminBox.setValue(false);
        enabledBox.setValue(false);

    }

    /**
     * Checks if the form is completed to determine if the save button should be enabled or not.
     */
    private void checkIfFormFilledOut(){
        //save.setEnabled( selectedUserInfo != null);
    }

    private void toggleEdit(){
        isEditMode = !isEditMode;

        if (isEditMode){
            grid.setSelectionMode(Grid.SelectionMode.SINGLE);
        }
        else {
            grid.setSelectionMode(Grid.SelectionMode.MULTI);
            GridMultiSelectionModel<UserInfo> selectionModel = (GridMultiSelectionModel<UserInfo>)grid.getSelectionModel();
            selectionModel.setDragSelect(true);
        }

    }

    //called when data is deleted
    private void deleteData() {
        Set<UserInfo> selectedItems = grid.getSelectedItems();

        if (!selectedItems.isEmpty()) {
            for (UserInfo userInfo: selectedItems){
                Authorities authorities = authoritiesService.getUsername(userInfo.getUsername());
                authoritiesService.delete(authorities.getId());
                userInfoService.delete(userInfo.getUserId());
            }
            clearForm();
            refreshGrid();
            Notification.show("Data updated");
            //UI.getCurrent().navigate(AdminView.class);

        } else {
            // Handle case where no items are selected, e.g., show a notification
            Notification.show("No items selected", 3000, Position.MIDDLE);
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
        private final GridListDataView<UserInfo> dataView;

        private String user;
        private String time;
        private String machine;
        private String machineTempF;
        private String room;
        private String roomTempF;
        private String location;
        private String username;

        public RecordFilter(GridListDataView<UserInfo> dataView) {
            this.dataView = dataView;
            //this.dataView.addFilter(this::test);
        }

        public void setDate(String date) {
            //this.date = date;
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



//        public boolean test(UserInfo userInfo){
//            boolean matchesDate = matches(userInfo.getDate().toString(), date);
//            boolean matchesTime = matches(userInfo.getTime().toString(), time);
//            boolean matchesMachine = matches(userInfo.getMachine(), machine);
//            boolean matchesMachineTempF = matches(Integer.toString(userInfo.getMachineTempF()), machineTempF);
//            boolean matchesRoom = matches(userInfo.getRoom(), room);
//            boolean matchesRoomTempF = matches(Integer.toString(userInfo.getRoomTempF()), roomTempF);
//            boolean matchesLocation = matches(userInfo.getLocation(), location);
//            boolean matchesUsername = matches(userInfo.getUsername(), username);
//
//            return matchesDate && matchesTime && matchesMachine && matchesMachineTempF && matchesRoom && matchesRoomTempF && matchesLocation && matchesUsername;
//        }

        private boolean matches(String value, String searchTerm) {
            return searchTerm == null || searchTerm.isEmpty() || value.toLowerCase().contains(searchTerm.toLowerCase());
        }

    }

}

