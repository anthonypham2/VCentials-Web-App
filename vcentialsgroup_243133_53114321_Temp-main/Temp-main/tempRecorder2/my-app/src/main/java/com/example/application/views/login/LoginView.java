package com.example.application.views.login;

import com.example.application.security.FirebaseService;
import com.example.application.views.home.HomeView;
import com.google.firebase.auth.FirebaseAuthException;
import com.vaadin.flow.component.ClientCallable;
import com.vaadin.flow.component.Tag;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.dependency.JsModule;
import com.vaadin.flow.component.dependency.NpmPackage;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.littemplate.LitTemplate;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.auth.AnonymousAllowed;
import com.vaadin.flow.spring.security.AuthenticationContext;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

@Route("login")
@PageTitle("Login")
@AnonymousAllowed
@NpmPackage(value = "firebase", version = "10.13.1")
@Tag("login-view")
@JsModule("./login.ts")
public class LoginView extends LitTemplate {

    private final FirebaseService firebase;

    public LoginView(FirebaseService firebaseService) {
        this.firebase = firebaseService;
    }

    @ClientCallable
    private void login(String token, String uid) {

        try {
            // Validate the token at Firebase server  first and create
            // Authentication object based on it
            Authentication authentication = firebase.login(token);
            // Save the authentication to context
            SecurityContextHolder.getContext().setAuthentication(authentication);

            UI.getCurrent().navigate(HomeView.class);

        } catch (FirebaseAuthException e) {
            throw new RuntimeException("Invalid token!");
        }

    }

}