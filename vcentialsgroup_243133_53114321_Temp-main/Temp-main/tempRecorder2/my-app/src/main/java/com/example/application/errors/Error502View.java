package com.example.application.errors;

import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import jakarta.annotation.security.PermitAll;

/**
 * This view is displayed when a 502 error (Bad Gateway) occurs.
 * It includes an error image, title, and message to inform the user about the error.
 */
@Route("error-502")
@PermitAll

public class Error502View extends VerticalLayout {

    public Error502View() {
        // Error title
        H1 errorTitle = new H1("502 - Bad Gateway");
        errorTitle.addClassName("error-title");

        // Error image
        Image errorImage = new Image("frontend/images/502-error.png", "502 Error");
        errorImage.addClassName("error-image");

        // Error message
        Paragraph errorMessage = new Paragraph("Uh oh, the server got caught in an infinite loop!");
        errorMessage.addClassName("error-message");

        // Add components to the layout
        addClassName("error-page");
        add(errorTitle, errorImage, errorMessage);
    }
}
