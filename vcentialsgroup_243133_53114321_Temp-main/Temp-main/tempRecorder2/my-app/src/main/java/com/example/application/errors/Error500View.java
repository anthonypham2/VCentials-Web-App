package com.example.application.errors;

import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;

/**
 * This view is displayed when a 500 error (Internal Server Error) occurs.
 * It includes an error image, title, and message to inform the user about the error.
 */
@Route("error-500")
public class Error500View extends VerticalLayout {

    public Error500View() {
        // Error title
        H1 errorTitle = new H1("500 - Internal Server Error");
        errorTitle.addClassName("error-title");

        // Error image
        Image errorImage = new Image("frontend/images/500-error.png", "500 Error");
        errorImage.addClassName("error-image");

        // Error message
        Paragraph errorMessage = new Paragraph("Cthulhu broke the server. We're sending in the IT team.");
        errorMessage.addClassName("error-message");

        // Add components to the layout
        addClassName("error-page");
        add(errorTitle, errorImage, errorMessage);
    }
}
