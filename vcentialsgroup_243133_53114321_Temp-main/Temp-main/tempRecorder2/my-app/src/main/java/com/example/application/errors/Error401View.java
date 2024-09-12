package com.example.application.errors;

import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import jakarta.annotation.security.PermitAll;

/**
 * This view is displayed when a 401 error (Unauthorized) occurs.
 * It includes an error image, title, and message to inform the user about the error.
 */
@Route("error-401")
@PermitAll

public class Error401View extends VerticalLayout {

    public Error401View() {
        // Error title
        H1 errorTitle = new H1("401 - Unauthorized");
        errorTitle.addClassName("error-title");

        // Error image
        Image errorImage = new Image("frontend/images/401-error.png", "401 Error");
        errorImage.addClassName("error-image");

        // Error message
        Paragraph errorMessage = new Paragraph("You shall not pass! (This website, that is.)");
        errorMessage.addClassName("error-message");

        // Add components to the layout
        addClassName("error-page");
        add(errorTitle, errorImage, errorMessage);
    }
}
