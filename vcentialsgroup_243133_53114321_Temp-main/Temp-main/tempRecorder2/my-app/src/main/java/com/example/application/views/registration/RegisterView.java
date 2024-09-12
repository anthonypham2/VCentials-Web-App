package com.example.application.views.registration;

import com.example.application.data.Authorities;
import com.example.application.data.AuthoritiesRepository;
import com.example.application.services.UserInfoService;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.textfield.*;
import org.springframework.beans.factory.annotation.Autowired;
import com.example.application.data.UserInfo;
import com.example.application.data.UserInfoRepository;
import com.example.application.views.login.LoginView;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.EmailField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouterLink;
import com.vaadin.flow.server.auth.AnonymousAllowed;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.regex.Pattern;

@Route("register")
@PageTitle("Registration")
@AnonymousAllowed
public class RegisterView extends VerticalLayout {

    // Define UI components for user input
    private final TextField usernameField = new TextField("Username",event -> isValid());
    private final EmailField emailField = new EmailField("Email",event -> isValid());
    private final TextField vcidField = new TextField("VCID",event -> isValid());
    private final PasswordField passwordField = new PasswordField("Password",event -> changePassword());

    private final PasswordField confirmPasswordField = new PasswordField("Confirm Password",event -> isValid());
    private final Button registerButton = new Button("Sign Up", event -> register());

    @Autowired
    private UserInfoRepository userInfoRepository;
    @Autowired
    private UserInfoService userInfoService;

    @Autowired
    private AuthoritiesRepository authoritiesRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    // Constructor to initialize the view and add components
    public RegisterView(UserInfoRepository userInfoRepository) {

        this.userInfoRepository = userInfoRepository;

        setSizeFull();
        setJustifyContentMode(JustifyContentMode.CENTER);
        setAlignItems(Alignment.CENTER);


        // Add UI components to the layout
        add(new H1("Create an Account"), usernameField, emailField,vcidField ,passwordField,confirmPasswordField ,registerButton);

        usernameField.setRequired(true);
        usernameField.setInvalid(true);

        emailField.setPrefixComponent(VaadinIcon.ENVELOPE.create());
        emailField.setErrorMessage("Enter a valid email address");
        emailField.setRequired(true);
        emailField.setInvalid(true);

        vcidField.setPrefixComponent(VaadinIcon.DIPLOMA.create());
        vcidField.setMaxLength(9);
        vcidField.setMinLength(9);
        vcidField.setAllowedCharPattern("[0-9V]");
        vcidField.setPattern("^[V]+[0-9]{8}");
        vcidField.setErrorMessage("Enter a valid VCID");

        passwordField.setPrefixComponent(VaadinIcon.LOCK.create());
        passwordField.setRequired(true);
        usernameField.setInvalid(true);
        confirmPasswordField.setPrefixComponent(VaadinIcon.LOCK.create());
        confirmPasswordField.setRequired(true);
        confirmPasswordField.setEnabled(false);
        confirmPasswordField.setErrorMessage("Password must match");
        confirmPasswordField.setInvalid(true);

        registerButton.setEnabled(false);

        // Link to the login page
        RouterLink loginLink = new RouterLink("Already have an account? Login here", LoginView.class);
        add(loginLink);

        // Add CSS class to the register button
        registerButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
    }

    // Method to handle user registration
    private void register() {
        String usernameValue = usernameField.getValue();
        String emailValue = emailField.getValue();
        String vcidValue = vcidField.getValue();
        String passwordValue = passwordField.getValue();
        String encodedPassword = passwordEncoder.encode(passwordValue);

        //makes sure a unique username/email is entered
        if (checkForExistingAccount(usernameValue,emailValue,vcidValue)) {
            UserInfo newUser = new UserInfo();
            Authorities newAuthority = new Authorities();

            newUser.setUsername(usernameValue);
            newUser.setEmail(emailValue);  // Set email
            newUser.setPassword(encodedPassword);
            newUser.setEmail(emailValue);
            newUser.setVcid(vcidValue);
            newUser.setEnabled(true);
            newAuthority.setUsername(usernameValue);
            newAuthority.setAuthority("ROLE_USER");

            // Print debug information to console
            System.out.println("Registering user: " + usernameValue + " with email: " + emailValue + " and password: " + passwordValue);
            System.out.println("Encoded password: " + encodedPassword);

            // Save new user and authority to the database
            userInfoRepository.save(newUser);
            authoritiesRepository.save(newAuthority);

            // Show success notification
            Notification.show("New account created successfully!");

            // Clear input fields
            clearFields();

            // Navigate to the login page
            UI.getCurrent().navigate(LoginView.class);
        }
    }

    // Method to validate user input
    private void isValid() {
        registerButton.setEnabled(!usernameField.isInvalid() && !emailField.isInvalid() && !passwordField.isInvalid() && !confirmPasswordField.isInvalid() && !vcidField.isInvalid());
    }

    //checks if the username or email already exists
    private boolean checkForExistingAccount(String username,String email,String vcid){
        if (userInfoRepository.existsByUsername(username)){
            Notification.show("Account with that username already exists!");
            return false;
        }
        if (userInfoRepository.existsByEmail(email)){
            Notification.show("Account with that email already exists!");
            return false;
        }
        if (userInfoRepository.existsByVcid(vcid)){
            Notification.show("Account with that VCID already exists!");
            return false;
        }
        return true;
    }

    // Method to clear input fields after registration
    private void clearFields() {
        usernameField.clear();
        emailField.clear();
        vcidField.clear();
        passwordField.clear();
    }

    //called whenever the first password field is edited
    private void changePassword(){
        confirmPasswordField.setValue("");
        validateConfirmPassword();
        confirmPasswordField.setEnabled(!passwordField.isEmpty());
    }

    //sets the confirm password field to check if it has the same value as the first one
    private void validateConfirmPassword(){
        confirmPasswordField.setPattern(passwordField.getValue());
        isValid();
    }
}
