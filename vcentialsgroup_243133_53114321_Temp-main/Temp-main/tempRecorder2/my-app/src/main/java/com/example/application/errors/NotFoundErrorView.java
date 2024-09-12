package com.example.application.errors;

import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.ErrorParameter;
import com.vaadin.flow.router.HasErrorParameter;
import com.vaadin.flow.router.NotFoundException;
import com.vaadin.flow.router.Route;
import jakarta.annotation.security.PermitAll;

/**
 * This view is displayed when a 404 error (Page Not Found) occurs.
 * It includes an error image, title, and message to inform the user about the error.
 */
@Route("error-404")
@PermitAll

public class NotFoundErrorView extends VerticalLayout implements HasErrorParameter<NotFoundException> {

    public NotFoundErrorView() {
        // Error title
        H1 errorTitle = new H1("404 - Page Not Found");
        errorTitle.addClassName("error-title");

        // Error image
        Image errorImage = new Image("frontend/images/404-error.png", "404 Error");
        errorImage.addClassName("error-image");

        // Error message
        Paragraph errorMessage = new Paragraph("Looks like this page went on a permanent vacation to the bit bucket");
        errorMessage.addClassName("error-message");

        // Add components to the layout
        addClassName("error-page");
        add(errorTitle, errorImage, errorMessage);
    }

    @Override
    public int setErrorParameter(BeforeEnterEvent event, ErrorParameter<NotFoundException> parameter) {
        return 404;
    }
}
