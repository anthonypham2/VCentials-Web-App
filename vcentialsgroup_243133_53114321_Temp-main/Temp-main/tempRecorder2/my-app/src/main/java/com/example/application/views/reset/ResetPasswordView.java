package com.example.application.views.reset;

import com.example.application.data.UserInfo;
import com.example.application.data.UserInfoRepository;
import com.example.application.services.UserInfoService;
import com.example.application.views.login.LoginView;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.EmailField;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.router.*;
import com.vaadin.flow.server.VaadinSession;
import com.vaadin.flow.server.auth.AnonymousAllowed;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.awt.*;
import java.util.Objects;

@AnonymousAllowed
@Route("reset")
@PageTitle("Reset Password")
public class ResetPasswordView extends VerticalLayout implements HasUrlParameter<String>{

    private EmailField emailField = new EmailField("E-Mail");
    private PasswordField newPasswordField = new PasswordField("New Password",event -> changePassword());
    private PasswordField confirmPasswordField = new PasswordField("Confirm Password",event -> isValid());
    private Button submitButton = new Button("Reset Password",event -> submit());
    private UserInfo resetUser;
    private String token;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private UserInfoService userInfoService;
    @Autowired
    private UserInfoRepository userInfoRepository;

    public ResetPasswordView(UserInfoService userInfoService, UserInfoRepository userInfoRepository){
        this.userInfoService = userInfoService;
        this.userInfoRepository = userInfoRepository;

        setSizeFull();
        setJustifyContentMode(JustifyContentMode.CENTER);
        setAlignItems(Alignment.CENTER);

        emailField.setPrefixComponent(VaadinIcon.ENVELOPE.create());
        emailField.setReadOnly(true);

        newPasswordField.setPrefixComponent(VaadinIcon.LOCK.create());
        newPasswordField.setRequired(true);
        newPasswordField.setInvalid(true);

        confirmPasswordField.setPrefixComponent(VaadinIcon.LOCK.create());
        confirmPasswordField.setRequired(true);
        confirmPasswordField.setEnabled(false);
        confirmPasswordField.setErrorMessage("Password must match");
        confirmPasswordField.setInvalid(true);

        submitButton.setEnabled(false);
        submitButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

        add(new H1("Reset Password"),emailField,newPasswordField,confirmPasswordField,submitButton);
    }

    //gets the token from the url and finds the user with the same token
    @Override
    public void setParameter(BeforeEvent event,@OptionalParameter String parameter) {

        Location location = event.getLocation();
        QueryParameters queryParameters = location.getQueryParameters();
        String queryString =  queryParameters.getQueryString();

        //if the token is absent from the url(the user tries to access the page manually) they get sent back to the login view
        if (queryString.isEmpty()){
            event.forwardTo("login");
            System.out.println("going back to login");
        }
        else {
            token = queryString.substring(6);
            System.out.println(token);
            resetUser = userInfoService.getPasswordToken(token);
            emailField.setValue(resetUser.getEmail());
        }
    }

    //called when reset password button is pressed
    public void submit(){
        String newPassword = newPasswordField.getValue();

        //resets the users password
        String encodedPassword = passwordEncoder.encode(newPassword);
        resetUser.setPassword(encodedPassword);
        userInfoRepository.save(resetUser);
        Notification.show("Password reset");
        UI.getCurrent().navigate(LoginView.class);
    }

    private void isValid() {
        submitButton.setEnabled(!newPasswordField.isInvalid() && !confirmPasswordField.isInvalid());
    }
    //called whenever the first password field is edited
    private void changePassword(){
        confirmPasswordField.setValue("");
        validateConfirmPassword();
        confirmPasswordField.setEnabled(!newPasswordField.isEmpty());
    }

    //sets the confirm password field to check if it has the same value as the first one
    private void validateConfirmPassword(){
        confirmPasswordField.setPattern(newPasswordField.getValue());
        isValid();
    }
}
