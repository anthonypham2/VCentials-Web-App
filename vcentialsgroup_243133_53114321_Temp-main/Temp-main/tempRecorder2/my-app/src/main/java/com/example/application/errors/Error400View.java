package com.example.application.errors;

import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.auth.AnonymousAllowed;
import jakarta.annotation.security.PermitAll;

/**
 * This view is displayed when a 400 error (Bad Request) occurs.
 * It includes an error image, title, and message to inform the user about the error.
 */
@Route("error-400")
@PermitAll

public class Error400View extends VerticalLayout {

    public Error400View() {
        // Error title
        H1 errorTitle = new H1("400 - Bad Request");
        errorTitle.addClassName("error-title");

        // Error image
        Image errorImage = new Image("frontend/images/400-error.png", "400 Error");
        errorImage.addClassName("error-image");

        // Error message
        Paragraph errorMessage = new Paragraph("Seems like your request was written in Klingon. The server needs something more logical.");
        errorMessage.addClassName("error-message");

        // Add components to the layout
        addClassName("error-page");
        add(errorTitle, errorImage, errorMessage);
    }
}
