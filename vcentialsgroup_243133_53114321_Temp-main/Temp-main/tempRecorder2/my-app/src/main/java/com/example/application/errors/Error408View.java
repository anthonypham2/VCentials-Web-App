package com.example.application.errors;

import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import jakarta.annotation.security.PermitAll;

/**
 * This view is displayed when a 408 error (Request Timeout) occurs.
 * It includes an error image, title, and message to inform the user about the error.
 */
@Route("error-408")
@PermitAll

public class Error408View extends VerticalLayout {

    public Error408View() {
        // Error title
        H1 errorTitle = new H1("408 - Request Timeout");
        errorTitle.addClassName("error-title");

        // Error image
        Image errorImage = new Image("frontend/images/408-error.png", "408 Error");
        errorImage.addClassName("error-image");

        // Error message
        Paragraph errorMessage = new Paragraph("The server is taking a nap. Try refreshing the page later.");
        errorMessage.addClassName("error-message");

        // Add components to the layout
        addClassName("error-page");
        add(errorTitle, errorImage, errorMessage);
    }
}
