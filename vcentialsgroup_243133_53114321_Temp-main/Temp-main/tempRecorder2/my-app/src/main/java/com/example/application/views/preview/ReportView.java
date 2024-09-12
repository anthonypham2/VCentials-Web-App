package com.example.application.views.preview;

import com.example.application.data.RecordDTO;
import com.example.application.services.SharedGridDataService;
import com.example.application.views.MainLayout;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.combobox.MultiSelectComboBox;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import jakarta.annotation.security.PermitAll;
import org.springframework.beans.factory.annotation.Autowired;

import jakarta.annotation.PostConstruct;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;
import java.util.Base64;

@PageTitle("Generate Report")
@Route(value = "report", layout = MainLayout.class)
@PermitAll
public class ReportView extends VerticalLayout {

    private final SharedGridDataService sharedGridDataService;
    private List<Checkbox> sortingCheckboxes;
    private List<Checkbox> groupingCheckboxes;
    private DatePicker startDatePicker;
    private DatePicker endDatePicker;
    private MultiSelectComboBox<String> locationComboBox;
    private Div reportDiv = new Div();

    @Autowired
    public ReportView(SharedGridDataService sharedGridDataService) {
        this.sharedGridDataService = sharedGridDataService;
    }

    @PostConstruct
    private void init() {
        List<RecordDTO> records = sharedGridDataService.getRecords();

        // Initialize the location filter ComboBox
        locationComboBox = new MultiSelectComboBox<>("Location");
        locationComboBox.setItems(getAllLocations(records));
        locationComboBox.setClearButtonVisible(true);
        locationComboBox.setPlaceholder("Select locations");


        // Initialize and add sorting options
        HorizontalLayout sortingOptions = createSortingOptions();
        add(new com.vaadin.flow.component.html.Label("Sorting Options:"));
        add(sortingOptions);

        // Initialize and add grouping options
        HorizontalLayout groupingOptions = createGroupingOptions();
        add(new com.vaadin.flow.component.html.Label("Grouping Options:"));
        add(groupingOptions);

        // Initialize and add date range filter
        HorizontalLayout dateRangeFilter = createDateRangeFilter();
        add(dateRangeFilter);

        // Initialize and add buttons for generating and sending the report
        HorizontalLayout buttons = createButtons(records);
        add(buttons);

        // Add location filter and report div to the layout
        add(locationComboBox);
        add(reportDiv); // Initially empty, will hold the report content
    }

    // Retrieve all distinct locations from the records
    private List<String> getAllLocations(List<RecordDTO> records) {
        return records.stream()
                .map(RecordDTO::getLocation)
                .distinct()
                .collect(Collectors.toList());
    }

    // Create sorting options checkboxes
    private HorizontalLayout createSortingOptions() {
        sortingCheckboxes = createCheckboxes("Date", "Room", "Machine", "Location", "Username");
        return new HorizontalLayout(sortingCheckboxes.toArray(new Checkbox[0]));
    }

    // Create grouping options checkboxes
    private HorizontalLayout createGroupingOptions() {
        groupingCheckboxes = createCheckboxes("Date", "Room", "Machine", "Location", "Username");
        return new HorizontalLayout(groupingCheckboxes.toArray(new Checkbox[0]));
    }

    // Create date range filter using DatePickers
    private HorizontalLayout createDateRangeFilter() {
        startDatePicker = new DatePicker("Start Date");
        endDatePicker = new DatePicker("End Date");

        return new HorizontalLayout(startDatePicker, endDatePicker);
    }

    // Create buttons for generating HTML report and sending email
    private HorizontalLayout createButtons(List<RecordDTO> records) {
        Button generateHtmlButton = new Button("Generate HTML Report", event -> showHtmlReport(records));
        Button sendEmailButton = new Button("Send Email", event -> {
            storeReportDataInSession(records);
            sendEmailWithAttachment();
        });

        generateHtmlButton.setPrefixComponent(VaadinIcon.PRESENTATION.create());
        sendEmailButton.setPrefixComponent(VaadinIcon.ENVELOPE.create());

        HorizontalLayout buttonLayout = new HorizontalLayout(generateHtmlButton, sendEmailButton);
        buttonLayout.setWidthFull();
        buttonLayout.setJustifyContentMode(JustifyContentMode.START);

        return buttonLayout;
    }

    // Create checkboxes for given labels
    private List<Checkbox> createCheckboxes(String... labels) {
        List<Checkbox> checkboxes = new ArrayList<>();
        for (String label : labels) {
            checkboxes.add(new Checkbox(label));
        }
        return checkboxes;
    }

    // Display HTML report based on selected filters, sorting, and grouping options
    private void showHtmlReport(List<RecordDTO> records) {
        List<String> selectedSortingParameters = getSelectedParameters(sortingCheckboxes);
        List<String> selectedGroupingParameters = getSelectedParameters(groupingCheckboxes);

        LocalDate startDate = startDatePicker.getValue();
        LocalDate endDate = endDatePicker.getValue();
        Set<String> selectedLocations = locationComboBox.getValue();

        // Ensure location, room, and machine are prioritized for sorting and grouping
        selectedSortingParameters = prioritizeParameters(selectedSortingParameters);
        selectedGroupingParameters = prioritizeParameters(selectedGroupingParameters);

        List<RecordDTO> filteredRecords = filterRecords(records, startDate, endDate, selectedLocations);
        List<RecordDTO> sortedRecords = sortRecords(filteredRecords, selectedSortingParameters);

        String title = generateDynamicTitle(selectedSortingParameters.toArray(new String[0]), selectedGroupingParameters.toArray(new String[0]));
        String reportContent = generateReportContent(sortedRecords, title, selectedGroupingParameters);

        // Display the report content
        reportDiv.getElement().setProperty("innerHTML", reportContent);
    }

    // Store report data in session for email attachment
    private void storeReportDataInSession(List<RecordDTO> records) {
        List<String> selectedSortingParameters = getSelectedParameters(sortingCheckboxes);
        List<String> selectedGroupingParameters = getSelectedParameters(groupingCheckboxes);

        LocalDate startDate = startDatePicker.getValue();
        LocalDate endDate = endDatePicker.getValue();
        Set<String> selectedLocations = locationComboBox.getValue();

        // Ensure location, room, and machine are prioritized for sorting and grouping
        selectedSortingParameters = prioritizeParameters(selectedSortingParameters);
        selectedGroupingParameters = prioritizeParameters(selectedGroupingParameters);

        List<RecordDTO> filteredRecords = filterRecords(records, startDate, endDate, selectedLocations);
        List<RecordDTO> sortedRecords = sortRecords(filteredRecords, selectedSortingParameters);

        String title = generateDynamicTitle(selectedSortingParameters.toArray(new String[0]), selectedGroupingParameters.toArray(new String[0]));
        String reportContent = generateReportContent(sortedRecords, title, selectedGroupingParameters);

        // Log the report content for debugging
        System.out.println("Storing Report Content: " + reportContent);

        // Store report data in session
        UI.getCurrent().getSession().setAttribute("reportContent", reportContent);
        UI.getCurrent().getSession().setAttribute("reportTitle", title);
    }

    // Navigate to the email view for sending the report
    private void sendEmailWithAttachment() {
        getUI().ifPresent(ui -> ui.navigate("email"));
    }

    // Get selected parameters from checkboxes
    private List<String> getSelectedParameters(List<Checkbox> checkboxes) {
        return checkboxes.stream()
                .filter(Checkbox::getValue)
                .map(Checkbox::getLabel)
                .collect(Collectors.toList());
    }

    // Ensure location, room, and machine are prioritized in the parameter list
    private List<String> prioritizeParameters(List<String> parameters) {
        List<String> prioritizedParameters = new ArrayList<>(List.of("Location", "Room", "Machine"));
        for (String parameter : parameters) {
            if (!prioritizedParameters.contains(parameter)) {
                prioritizedParameters.add(parameter);
            }
        }
        return prioritizedParameters;
    }

    // Filter records based on date range and selected locations
    private List<RecordDTO> filterRecords(List<RecordDTO> records, LocalDate startDate, LocalDate endDate, Set<String> selectedLocations) {
        return records.stream()
                .filter(record -> {
                    LocalDate recordDate = record.getDate();
                    boolean isAfterOrEqualStart = (startDate == null || !recordDate.isBefore(startDate));
                    boolean isBeforeOrEqualEnd = (endDate == null || !recordDate.isAfter(endDate));
                    boolean matchesLocation = (selectedLocations == null || selectedLocations.isEmpty() || selectedLocations.contains(record.getLocation()));
                    return isAfterOrEqualStart && isBeforeOrEqualEnd && matchesLocation;
                })
                .distinct()
                .collect(Collectors.toList());
    }

    // Sort records based on selected parameters, with location, room, and machine prioritized
    private List<RecordDTO> sortRecords(List<RecordDTO> records, List<String> selectedParameters) {
        Comparator<RecordDTO> comparator = Comparator.comparing(RecordDTO::getLocation) // Prioritize sorting by Location
                .thenComparing(RecordDTO::getRoom) // Then by Room
                .thenComparing(RecordDTO::getMachine); // Then by Machine
        for (String parameter : selectedParameters) {
            switch (parameter) {
                case "Date":
                    comparator = comparator.thenComparing(RecordDTO::getDate);
                    break;
                case "Username":
                    comparator = comparator.thenComparing(RecordDTO::getUsername);
                    break;
                default:
                    break;
            }
        }
        return records.stream().sorted(comparator).distinct().collect(Collectors.toList());
    }

    // Generate dynamic title for the report based on sorting and grouping parameters
    private String generateDynamicTitle(String[] sortingParameters, String[] groupingParameters) {
        StringBuilder title = new StringBuilder("VCentials Temperature Recorder Report");
        if (sortingParameters.length > 0) {
            title.append(" - Sorted by ");
            for (int i = 0; i < sortingParameters.length; i++) {
                title.append(sortingParameters[i]);
                if (i < sortingParameters.length - 1) {
                    title.append(" and ");
                }
            }
        }
        if (groupingParameters.length > 0) {
            title.append(" - Grouped by ");
            for (int i = 0; i < groupingParameters.length; i++) {
                title.append(groupingParameters[i]);
                if (i < groupingParameters.length - 1) {
                    title.append(" and ");
                }
            }
        }
        return title.toString();
    }

    // Generate HTML report content based on the records, title, and grouping criteria
    private String generateReportContent(List<RecordDTO> records, String title, List<String> groupingCriteria) {
        StringBuilder reportBuilder = new StringBuilder();
        reportBuilder.append("<html><head><title>").append(title).append("</title>");
        reportBuilder.append("<style>")
                .append("body { font-family: Arial, sans-serif; }")
                .append("h1, h2, h3, h4, h5 { color: #AB1A22; text-align: center; margin-bottom: 0; }")
                .append("h1 { font-size: 24px; margin-top: 20px; }")
                .append("h2 { font-size: 20px; margin-top: 15px; }")
                .append("h3 { font-size: 18px; margin-top: 10px; }")
                .append("h4 { font-size: 16px; margin-top: 8px; }")
                .append("h5 { font-size: 14px; margin-top: 6px; }")
                .append("table { width: 100%; border-collapse: collapse; margin-bottom: 20px; margin-top: 10px; }")
                .append("th, td { border: 1px solid #dddddd; text-align: left; padding: 8px; }")
                .append("th { background-color: #AB1A22; color: white; font-weight: bold; }")
                .append("td { max-width: 200px; }") // Adjusted the width of the columns
                .append("tr:nth-child(even) { background-color: #f2f2f2; }")
                .append(".container { padding: 20px; }")
                .append(".header { display: flex; justify-content: space-between; align-items: center; }")
                .append(".header-title { font-size: 24px; font-weight: bold; color: #AB1A22; }")
                .append(".header-date { font-size: 16px; color: #666666; }")
                .append("</style>");
        reportBuilder.append("</head><body>");
        reportBuilder.append("<div class='container'>");
        reportBuilder.append("<div class='header'>");
        reportBuilder.append("<div class='header-title'>").append(title).append("</div>");
        reportBuilder.append("<div class='header-date'></div>");
        reportBuilder.append("</div>");

        // Group records dynamically based on the selected criteria
        Map<String, List<RecordDTO>> groupedRecords = groupRecordsDynamically(records, groupingCriteria, 0);

        buildGroupedHtmlContent(reportBuilder, groupedRecords, groupingCriteria, 0);

        reportBuilder.append("</div></body></html>");
        return reportBuilder.toString();
    }

    // Group records dynamically based on the selected criteria
    private Map<String, List<RecordDTO>> groupRecordsDynamically(List<RecordDTO> records, List<String> groupingCriteria, int level) {
        if (level >= groupingCriteria.size()) {
            return Map.of("Records", records);
        }

        String criteria = groupingCriteria.get(level);
        return records.stream().collect(Collectors.groupingBy(record -> getGroupingKey(record, criteria)));
    }

    // Get the grouping key based on the selected criteria
    private String getGroupingKey(RecordDTO record, String criteria) {
        switch (criteria) {
            case "Date":
                return record.getDate().toString();
            case "Room":
                return record.getRoom();
            case "Machine":
                return record.getMachine();
            case "Location":
                return record.getLocation();
            case "Username":
                return record.getUsername();
            default:
                return "";
        }
    }

    // Build the HTML content for the grouped records
    private void buildGroupedHtmlContent(StringBuilder reportBuilder, Map<String, List<RecordDTO>> groupedRecords, List<String> groupingCriteria, int level) {
        if (level >= groupingCriteria.size()) {
            // Add table for the final grouped records
            reportBuilder.append("<table><tr>")
                    .append("<th>Date</th><th>Time</th><th>Machine Name</th><th>Machine Temp F</th><th>Room Name</th><th>Room Temp F</th><th>Location Name</th><th>Username</th>")
                    .append("<th>Unsafe Temp</th>")
                    .append("</tr>");

            Set<String> uniqueRecords = new HashSet<>();

            for (Map.Entry<String, List<RecordDTO>> entry : groupedRecords.entrySet()) {
                List<RecordDTO> records = entry.getValue();

                for (RecordDTO record : records) {
                    String uniqueIdentifier = record.getDate().toString() + record.getTime().toString() + record.getMachine() + record.getRoom() + record.getLocation() + record.getUsername();
                    if (uniqueRecords.add(uniqueIdentifier)) {
                        int temp = record.getMachineTempF().intValue();
                        boolean unsafeTemp = (record.getMachine().contains("Freezer") && temp > 0) ||
                                (record.getMachine().contains("Fridge") && (temp < 34 || temp > 40));

                        reportBuilder.append("<tr>");
                        reportBuilder.append("<td>").append(record.getDate().toString()).append("</td>");
                        reportBuilder.append("<td>").append(record.getTime().toString()).append("</td>");
                        reportBuilder.append("<td>").append(record.getMachine()).append("</td>");
                        reportBuilder.append("<td>").append(temp).append("</td>");
                        reportBuilder.append("<td>").append(record.getRoom()).append("</td>");
                        reportBuilder.append("<td>").append(record.getRoomTempF().toString()).append("</td>");
                        reportBuilder.append("<td>").append(record.getLocation()).append("</td>");
                        reportBuilder.append("<td>").append(record.getUsername()).append("</td>");
                        reportBuilder.append("<td>").append(unsafeTemp ? String.valueOf(temp) : "No").append("</td>");
                        reportBuilder.append("</tr>");
                    }
                }
            }

            reportBuilder.append("</table>");
            return;
        }

        for (Map.Entry<String, List<RecordDTO>> entry : groupedRecords.entrySet()) {
            String key = entry.getKey();
            List<RecordDTO> grouped = entry.getValue();

            reportBuilder.append("<h").append(level + 2).append(">").append(groupingCriteria.get(level)).append(": ").append(key).append("</h").append(level + 2).append(">");
            Map<String, List<RecordDTO>> nextGroupedRecords = groupRecordsDynamically(grouped, groupingCriteria, level + 1);
            buildGroupedHtmlContent(reportBuilder, nextGroupedRecords, groupingCriteria, level + 1);
        }
    }
}
