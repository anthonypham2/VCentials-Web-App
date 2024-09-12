package com.example.application.errors;

import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import jakarta.annotation.security.PermitAll;

/**
 * This view is displayed when a 403 error (Forbidden) occurs.
 * It includes an error image, title, and message to inform the user about the error.
 */
@Route("error-403")
@PermitAll

public class Error403View extends VerticalLayout {

    public Error403View() {
        // Error title
        H1 errorTitle = new H1("403 - Forbidden");
        errorTitle.addClassName("error-title");

        // Error image
        Image errorImage = new Image("frontend/images/403-error.png", "403 Error");
        errorImage.addClassName("error-image");

        // Error message
        Paragraph errorMessage = new Paragraph("Looks like you're trying to access the Pentagon's secret files. Permission denied, my friend.");
        errorMessage.addClassName("error-message");

        // Add components to the layout
        addClassName("error-page");
        add(errorTitle, errorImage, errorMessage);
    }
}
