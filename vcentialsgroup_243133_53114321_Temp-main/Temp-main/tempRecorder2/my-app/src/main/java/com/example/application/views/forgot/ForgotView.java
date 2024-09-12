package com.example.application.views.forgot;

import com.example.application.data.UserInfo;
import com.example.application.data.UserInfoRepository;
import com.example.application.services.EmailService;
import com.example.application.views.MainLayout;
import com.example.application.views.login.LoginView;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.EmailField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouterLink;
import com.vaadin.flow.server.auth.AnonymousAllowed;
import jakarta.annotation.security.PermitAll;
import net.bytebuddy.utility.RandomString;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;

@AnonymousAllowed
@Route("forgot")
@PageTitle("Forgot Password")
public class ForgotView extends VerticalLayout {

    private final EmailField emailField = new EmailField("E-Mail",event -> isValid());

    private final EmailService emailService;

    @Autowired
    private UserInfoRepository userInfoRepository;

    private final Button submitButton = new Button("Submit", event -> submit());
    public ForgotView(EmailService emailService){
        this.emailService = emailService;

        setSizeFull();
        setJustifyContentMode(JustifyContentMode.CENTER);
        setAlignItems(Alignment.CENTER);

        add(new H1("Forgot password?"),emailField,submitButton);

        //return to login link
        RouterLink loginLink = new RouterLink("Return to login", LoginView.class);
        add(loginLink);

        emailField.setErrorMessage("Enter a valid email address");
        emailField.setPrefixComponent(VaadinIcon.ENVELOPE.create());
        emailField.setInvalid(true);
        submitButton.setEnabled(false);
        submitButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
    }

    //is called whenever the submit button is pressed
    private void submit(){
        String email = emailField.getValue();

        //if the email field is valid, then get the user by email and assign them a special token along with emailing the token to the address
        if (checkForExistingEmail(email)) {
            UserInfo userInfo = userInfoRepository.findByEmail(email);
            userInfo.setPasswordToken(RandomString.make(30));
            String emailBody = "Hello " + userInfo.getUsername() + "\nHere is the link to reset your password: \n";
            String resetURL = "http://localhost:8080/reset?token=" + userInfo.getPasswordToken();

            //emailService.sendEmail(email,"Forgot Password?", emailBody + resetURL);
            Notification.show("Password sent to " + email, 3000, Notification.Position.MIDDLE);
            System.out.println(emailBody + resetURL);
            userInfoRepository.save(userInfo);
        }
        else {
            Notification.show("Account with email doesn't exist", 3000, Notification.Position.MIDDLE);
        }

    }

    //checks if an account with the email address exists
    private boolean checkForExistingEmail(String email){
        return userInfoRepository.existsByEmail(email);
    }

    //checks if the email is valid
    private void isValid(){
        submitButton.setEnabled(!emailField.isInvalid());
    }
}

