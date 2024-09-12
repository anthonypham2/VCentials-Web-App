package com.example.application.views.preview;

import com.example.application.data.RecordDTO;
import com.example.application.services.SharedGridDataService;
import com.example.application.views.MainLayout;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import jakarta.annotation.security.PermitAll;
import org.springframework.beans.factory.annotation.Autowired;

import jakarta.annotation.PostConstruct;
import java.util.List;
import java.util.stream.Collectors;

@PageTitle("Preview Data")
@Route(value = "preview", layout = MainLayout.class)
@PermitAll
public class PrintPreviewView extends VerticalLayout {

    private final SharedGridDataService sharedGridDataService;
    private Grid<RecordDTO> grid = new Grid<>(RecordDTO.class, false);

    @Autowired
    public PrintPreviewView(SharedGridDataService sharedGridDataService) {
        this.sharedGridDataService = sharedGridDataService;
    }

    @PostConstruct
    private void init() {
        // Retrieve records from the service
        List<RecordDTO> records = sharedGridDataService.getRecords();
        grid.setItems(records);

        // Configure Grid columns
        configureGridColumns();

        // Add Grid to the layout
        add(grid);

        // Create and add button layout
        HorizontalLayout buttonLayout = createButtonLayout();
        buttonLayout.setWidthFull();
        buttonLayout.setJustifyContentMode(JustifyContentMode.START);
        add(buttonLayout);
    }

    private void configureGridColumns() {
        grid.addColumn(RecordDTO::getDate).setHeader("Date").setAutoWidth(true);
        grid.addColumn(RecordDTO::getTime).setHeader("Time").setAutoWidth(true);
        grid.addColumn(RecordDTO::getMachine).setHeader("Machine Name").setAutoWidth(true);
        grid.addColumn(RecordDTO::getMachineTempF).setHeader("Machine Temp F").setAutoWidth(true);
        grid.addColumn(RecordDTO::getRoom).setHeader("Room Name").setAutoWidth(true);
        grid.addColumn(RecordDTO::getRoomTempF).setHeader("Room Temp F").setAutoWidth(true);
        grid.addColumn(RecordDTO::getLocation).setHeader("Location Name").setAutoWidth(true);
        grid.addColumn(RecordDTO::getUsername).setHeader("Username").setAutoWidth(true);
    }

    private HorizontalLayout createButtonLayout() {
        // Create buttons for PDF, CSV, Excel generation, Print, and Report
        Button generatePdfButton = new Button("Generate PDF", event -> generatePdf());
        Button generateCsvButton = new Button("Generate CSV", event -> generateCsv());
        Button generateExcelButton = new Button("Generate Excel", event -> generateExcel());
        Button printButton = new Button("Print", event -> printPreviewGrid());
        Button generateReportButton = new Button("Generate Report", event -> getUI().ifPresent(ui -> ui.navigate("report")));

        generatePdfButton.setPrefixComponent(VaadinIcon.CLIPBOARD.create());
        generateCsvButton.setPrefixComponent(VaadinIcon.MODAL_LIST.create());
        generateExcelButton.setPrefixComponent(VaadinIcon.MODAL_LIST.create());
        printButton.setPrefixComponent(VaadinIcon.PRINT.create());
        generateReportButton.setPrefixComponent(VaadinIcon.NOTEBOOK.create());

        // Add buttons to a HorizontalLayout
        return new HorizontalLayout(generatePdfButton, generateCsvButton, generateExcelButton, printButton, generateReportButton);
    }

    // Method to generate and download PDF
    private void generatePdf() {
        List<RecordDTO> records = grid.getListDataView().getItems().collect(Collectors.toList());
        String htmlContent = generateHtmlContent(records, "VCentials Temperature Recorder Log");

        getUI().ifPresent(ui -> ui.getPage().executeJs(
                "generateAndDownloadPDF($0);", htmlContent
        ));
    }

    // Method to generate and download CSV
    private void generateCsv() {
        List<RecordDTO> records = grid.getListDataView().getItems().collect(Collectors.toList());
        String jsonData = generateJsonData(records);

        getUI().ifPresent(ui -> ui.getPage().executeJs(
                "generateAndDownloadCSV($0, $1);", jsonData, "VCentials Temperature Recorder Log"
        ));
    }

    // Method to generate and download Excel
    private void generateExcel() {
        List<RecordDTO> records = grid.getListDataView().getItems().collect(Collectors.toList());
        String jsonData = generateJsonData(records);

        getUI().ifPresent(ui -> ui.getPage().executeJs(
                "generateAndDownloadExcel($0, $1);", jsonData, "VCentials Temperature Recorder Log"
        ));
    }

    // Method to print the preview grid
    private void printPreviewGrid() {
        List<RecordDTO> records = grid.getListDataView().getItems().collect(Collectors.toList());
        String htmlContent = generateHtmlContent(records, "VCentials Temperature Recorder Log");

        getUI().ifPresent(ui -> ui.getPage().executeJs(
                "openPrintWindow($0);", htmlContent
        ));
    }

    // Method to generate JSON data from records
    private String generateJsonData(List<RecordDTO> records) {
        StringBuilder jsonBuilder = new StringBuilder();
        jsonBuilder.append("[");
        for (RecordDTO record : records) {
            jsonBuilder.append("{")
                    .append("\"date\":\"").append(record.getDate()).append("\",")
                    .append("\"time\":\"").append(record.getTime()).append("\",")
                    .append("\"machine\":\"").append(record.getMachine()).append("\",")
                    .append("\"machineTempF\":\"").append(record.getMachineTempF()).append("\",")
                    .append("\"room\":\"").append(record.getRoom()).append("\",")
                    .append("\"roomTempF\":\"").append(record.getRoomTempF()).append("\",")
                    .append("\"location\":\"").append(record.getLocation()).append("\",")
                    .append("\"username\":\"").append(record.getUsername()).append("\"")
                    .append("},");
        }
        jsonBuilder.setLength(jsonBuilder.length() - 1); // Remove last comma
        jsonBuilder.append("]");
        return jsonBuilder.toString();
    }

    // Method to generate HTML content from records with title
    private String generateHtmlContent(List<RecordDTO> records, String title) {
        StringBuilder htmlBuilder = new StringBuilder();
        htmlBuilder.append("<html><head><title>").append(title).append("</title>");
        htmlBuilder.append("<style>")
                .append("table { font-family: Arial, sans-serif; border-collapse: collapse; width: 100%; }")
                .append("th, td { border: 1px solid #dddddd; text-align: left; padding: 1px 4px; }")
                .append("th { background-color: #f2f2f2; font-weight: bold; }")
                .append("h1 { text-align: center; margin-bottom: 20px; }") // Add some margin below the title
                .append("</style>");
        htmlBuilder.append("</head><body>");
        htmlBuilder.append("<h1>").append(title).append("</h1>"); // Ensure the title is a separate block element
        htmlBuilder.append("<table border='1'><tr>");
        htmlBuilder.append("<th>Date</th><th>Time</th><th>Machine Name</th><th>Machine Temp F</th>");
        htmlBuilder.append("<th>Room Name</th><th>Room Temp F</th><th>Location Name</th><th>Username</th>");
        htmlBuilder.append("</tr>");

        for (RecordDTO record : records) {
            htmlBuilder.append("<tr>");
            htmlBuilder.append("<td>").append(record.getDate()).append("</td>");
            htmlBuilder.append("<td>").append(record.getTime()).append("</td>");
            htmlBuilder.append("<td>").append(record.getMachine()).append("</td>");
            htmlBuilder.append("<td>").append(record.getMachineTempF()).append("</td>");
            htmlBuilder.append("<td>").append(record.getRoom()).append("</td>");
            htmlBuilder.append("<td>").append(record.getRoomTempF()).append("</td>");
            htmlBuilder.append("<td>").append(record.getLocation()).append("</td>");
            htmlBuilder.append("<td>").append(record.getUsername()).append("</td>");
            htmlBuilder.append("</tr>");
        }

        htmlBuilder.append("</table></body></html>");
        return htmlBuilder.toString();
    }
}
