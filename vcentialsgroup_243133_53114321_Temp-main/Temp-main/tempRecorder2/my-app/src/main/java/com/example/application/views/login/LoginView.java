package com.example.application.views.login;

import com.example.application.security.FirebaseService;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.auth.AnonymousAllowed;

@Route("login")
@PageTitle("Login")
@AnonymousAllowed
public class LoginView extends FirebaseLogin {

    public LoginView(FirebaseService firebaseService) {
        super(firebaseService);

        // TODO customize login view
    }
}