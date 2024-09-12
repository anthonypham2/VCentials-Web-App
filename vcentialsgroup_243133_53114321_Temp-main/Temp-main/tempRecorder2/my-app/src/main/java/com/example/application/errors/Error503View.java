package com.example.application.errors;

import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import jakarta.annotation.security.PermitAll;

/**
 * This view is displayed when a 503 error (Service Unavailable) occurs.
 * It includes an error image, title, and message to inform the user about the error.
 */
@Route("error-503")
@PermitAll

public class Error503View extends VerticalLayout {

    public Error503View() {
        // Error title
        H1 errorTitle = new H1("503 - Service Unavailable");
        errorTitle.addClassName("error-title");

        // Error image
        Image errorImage = new Image("frontend/images/503-error.png", "503 Error");
        errorImage.addClassName("error-image");

        // Error message
        Paragraph errorMessage = new Paragraph("The server is on vacation, but will be back soon...ish.");
        errorMessage.addClassName("error-message");

        // Add components to the layout
        addClassName("error-page");
        add(errorTitle, errorImage, errorMessage);
    }
}
