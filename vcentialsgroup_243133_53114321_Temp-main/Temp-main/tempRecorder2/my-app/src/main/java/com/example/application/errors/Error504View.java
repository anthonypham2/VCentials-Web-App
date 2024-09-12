package com.example.application.errors;

import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import jakarta.annotation.security.PermitAll;

/**
 * This view is displayed when a 504 error (Gateway Timeout) occurs.
 * It includes an error image, title, and message to inform the user about the error.
 */
@Route("error-504")
@PermitAll

public class Error504View extends VerticalLayout {

    public Error504View() {
        // Error title
        H1 errorTitle = new H1("504 - Gateway Timeout");
        errorTitle.addClassName("error-title");

        // Error image
        Image errorImage = new Image("frontend/images/504-error.png", "504 Error");
        errorImage.addClassName("error-image");

        // Error message
        Paragraph errorMessage = new Paragraph("The server got stuck in traffic. Patience, young grasshopper.");
        errorMessage.addClassName("error-message");

        // Add components to the layout
        addClassName("error-page");
        add(errorTitle, errorImage, errorMessage);
    }
}
