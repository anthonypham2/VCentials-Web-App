package com.example.application.errors;

import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import jakarta.annotation.security.PermitAll;

/**
 * This view is displayed when a generic error occurs.
 * It includes an error image, title, and message to inform the user about the error.
 */
@Route("error-generic")
@PermitAll

public class ErrorGenericView extends VerticalLayout {

    public ErrorGenericView() {
        // Error title
        H1 errorTitle = new H1("Error");
        errorTitle.addClassName("error-title");

        // Error image
        Image errorImage = new Image("frontend/images/generic-error.png", "Error");
        errorImage.addClassName("error-image");

        // Error message
        Paragraph errorMessage = new Paragraph("The internet has encountered a glitch in the Matrix.");
        errorMessage.addClassName("error-message");

        // Add components to the layout
        addClassName("error-page");
        add(errorTitle, errorImage, errorMessage);
    }
}
