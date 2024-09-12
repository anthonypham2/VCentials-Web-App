package com.example.application.views.email;

import com.example.application.services.EmailService;
import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.EmailField;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.QueryParameters;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouteAlias;
import com.vaadin.flow.component.upload.Upload;
import com.vaadin.flow.component.upload.receivers.MultiFileMemoryBuffer;
import com.vaadin.flow.server.auth.AnonymousAllowed;
import org.springframework.beans.factory.annotation.Autowired;

import jakarta.mail.MessagingException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Map;

@PageTitle("Email")
@Route(value = "email")
@RouteAlias(value = "email")
@AnonymousAllowed
public class EmailView extends VerticalLayout {

    @Autowired
    private EmailService emailService;

    private TextField subject = new TextField("Subject");
    private EmailField recipient = new EmailField("Recipient");
    private TextArea body = new TextArea("Body");
    private Button sendButton = new Button("Send");
    private Div reportPreview = new Div();
    private Button removePreviewButton = new Button("Remove Preview");

    // List to store multiple attachments
    private List<EmailService.Attachment> attachments = new ArrayList<>();

    public EmailView() {
        // Set the width and padding for the main layout
        setWidth("800px");
        setPadding(true);
        setSpacing(true);

        // Create a header for the email interface
        H1 header = new H1("Compose Email");

        // Create a form layout for the email fields
        FormLayout formLayout = new FormLayout();
        formLayout.setResponsiveSteps(new FormLayout.ResponsiveStep("0", 1));
        formLayout.addFormItem(recipient, "To");
        formLayout.addFormItem(subject, "Subject");

        // Create a multi-file upload component
        MultiFileMemoryBuffer buffer = new MultiFileMemoryBuffer();
        Upload upload = new Upload(buffer);
        upload.setAcceptedFileTypes("application/pdf", "image/jpeg", "image/png", "text/plain", "application/msword", "application/vnd.openxmlformats-officedocument.wordprocessingml.document");
        upload.setMaxFiles(5); // limit the number of files to 5
        upload.setDropLabel(new Span("Drag and drop files here or click to upload"));

        // Handle file upload finished event
        upload.addFinishedListener(event -> {
            String fileName = event.getFileName();
            InputStream inputStream = buffer.getInputStream(fileName);
            try {
                byte[] fileData = inputStream.readAllBytes();
                attachments.add(new EmailService.Attachment(fileData, fileName));
                Notification.show("Uploaded file: " + fileName);
            } catch (IOException e) {
                Notification.show("Error reading uploaded file: " + e.getMessage());
            }
        });

        // Style the body textarea to take up more vertical space
        body.setWidthFull();
        body.setHeight("300px");

        // Create a horizontal layout for the send button
        HorizontalLayout buttonLayout = new HorizontalLayout(sendButton);
        buttonLayout.setWidthFull();
        buttonLayout.setJustifyContentMode(JustifyContentMode.END);

        // Add click listener to the send button
        sendButton.addClickListener(event -> {
            try {
                sendEmail();
            } catch (MessagingException e) {
                Notification.show("Error sending email: " + e.getMessage());
            }
        });

        // Initialize the report preview div
        reportPreview.getStyle().set("border", "1px solid #ddd");
        reportPreview.getStyle().set("padding", "10px");
        reportPreview.getStyle().set("margin-top", "20px");
        reportPreview.getStyle().set("max-height", "400px");
        reportPreview.getStyle().set("overflow-y", "auto");
        reportPreview.setVisible(false);

        // Initialize the remove preview button
        removePreviewButton.addClickListener(event -> removePreview());
        removePreviewButton.setVisible(false);

        // Add components to the main layout
        add(header, formLayout, upload, body, buttonLayout, reportPreview, removePreviewButton);
    }

    @Override
    protected void onAttach(AttachEvent attachEvent) {
        super.onAttach(attachEvent);
        QueryParameters queryParameters = attachEvent.getUI().getInternals().getActiveViewLocation().getQueryParameters();
        Map<String, List<String>> parametersMap = queryParameters.getParameters();

        String reportTitle = parametersMap.getOrDefault("title", List.of("Report")).get(0);
        String encodedReportContent = parametersMap.getOrDefault("reportContent", List.of("")).get(0);

        if (!encodedReportContent.isEmpty()) {
            byte[] reportContentBytes = Base64.getDecoder().decode(encodedReportContent);
            String reportContent = new String(reportContentBytes, StandardCharsets.UTF_8);

            // Log the report content for debugging
            System.out.println("Retrieved Report Content: " + reportContent);

            // Store report data in session
            UI.getCurrent().getSession().setAttribute("reportContent", reportContent);
            UI.getCurrent().getSession().setAttribute("reportTitle", reportTitle);

            // Display the report content for preview
            reportPreview.getElement().setProperty("innerHTML", reportContent);
            reportPreview.setVisible(true);
            removePreviewButton.setVisible(true);

            // Set a default email body including a reference to the attached report
            String defaultEmailBody = "Please find the attached report titled '" + reportTitle + "'.\n\n" +
                    "Best regards,\n" +
                    "[Your Name]";
            body.setValue(defaultEmailBody);
        } else {
            reportPreview.setVisible(false);
            removePreviewButton.setVisible(false);
        }
    }

    private void removePreview() {
        reportPreview.setVisible(false);
        removePreviewButton.setVisible(false);
        UI.getCurrent().getSession().setAttribute("reportContent", null);
        UI.getCurrent().getSession().setAttribute("reportTitle", null);
        Notification.show("Preview removed");
    }

    /**
     * Sends the email with the provided details and attachments.
     */
    private void sendEmail() throws MessagingException {
        String recipientAddress = recipient.getValue();

        // Retrieve report data from session
        String reportContent = (String) UI.getCurrent().getSession().getAttribute("reportContent");
        String reportTitle = (String) UI.getCurrent().getSession().getAttribute("reportTitle");

        String emailSubject = reportTitle != null ? reportTitle : subject.getValue();
        String emailBody = body.getValue();

        // Log the report content before sending
        System.out.println("Sending Report Content: " + reportContent);

        // Attach the report content
        if (reportContent != null) {
            attachments.add(new EmailService.Attachment(reportContent.getBytes(StandardCharsets.UTF_8), "report.html"));
        }

        try {
            // Send the email with attachments
            emailService.sendEmail(recipientAddress, emailSubject, emailBody, attachments);
            Notification.show("Email sent successfully to " + recipientAddress);
        } catch (MessagingException e) {
            Notification.show("Failed to send email: " + e.getMessage());
            throw e;
        } finally {
            // Clear the attachments list after attempting to send
            attachments.clear();
        }
    }
}
