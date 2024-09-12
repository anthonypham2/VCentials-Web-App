package com.example.application.views.profile;

import com.example.application.data.UserInfo;
import com.example.application.views.MainLayout;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.EmailField;
import com.vaadin.flow.component.textfield.IntegerField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.textfield.TextFieldBase;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.ValidationException;
import com.vaadin.flow.data.converter.StringToIntegerConverter;
import com.vaadin.flow.data.validator.EmailValidator;
import com.vaadin.flow.data.validator.RegexpValidator;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import jakarta.annotation.security.PermitAll;
import net.bytebuddy.utility.RandomString;
import org.springframework.beans.factory.annotation.Autowired;
import com.example.application.data.UserInfoRepository;
import com.example.application.security.SecurityService;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;

@PermitAll
@Route(value = "profile", layout = MainLayout.class)
@PageTitle("Profile")
public class ProfileView extends VerticalLayout {

    private final UserInfoRepository userInfoRepository;
    private final SecurityService securityService;

    private TextField usernameField = new TextField("Username");
    private EmailField emailField = new EmailField("Email",event -> isValid());
    private TextField vcidField = new TextField("VCID",event -> isValid());
    private final Button passwordButton = new Button("Reset Password");
    private final Button editButton = new Button("Edit");
    private final Button saveButton = new Button("Save");
    private final Button cancelEditButton = new Button("Cancel");

    private Boolean isEditing = true;

    @Autowired
    public ProfileView(UserInfoRepository userInfoRepository, SecurityService securityService) {
        this.userInfoRepository = userInfoRepository;
        this.securityService = securityService;

        createProfileView();
    }

    private void createProfileView() {
        UserDetails userDetails = securityService.getAuthenticatedUser();
        //makes sure that a valid user is authenticated before loading the profile view
        UserInfo userInfo = userInfoRepository.findByUsername(userDetails.getUsername());
        getData(userInfo);
        toggleEditing(userInfo);

        usernameField.setReadOnly(true);
        usernameField.setPrefixComponent(VaadinIcon.USER.create());

        emailField.setPrefixComponent(VaadinIcon.ENVELOPE.create());
        emailField.setErrorMessage("Enter a valid email address");

        vcidField.setPrefixComponent(VaadinIcon.DIPLOMA.create());
        vcidField.setMaxLength(9);
        vcidField.setMinLength(9);
        vcidField.setAllowedCharPattern("[0-9V]");
        vcidField.setPattern("^[V]+[0-9]{8}");
        vcidField.setErrorMessage("Enter a valid VCID");

        passwordButton.setPrefixComponent(VaadinIcon.LOCK.create());
        passwordButton.setTooltipText("An email containing a link to reset your password will be sent");
        passwordButton.addClickListener(event -> submit(userInfo));

        editButton.setPrefixComponent(VaadinIcon.PENCIL.create());
        editButton.addClickListener(event -> toggleEditing(userInfo));

        saveButton.addClickListener(event -> saveUserInfo(userInfo));
        saveButton.setPrefixComponent(VaadinIcon.UPLOAD.create());
        saveButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

        cancelEditButton.setPrefixComponent(VaadinIcon.CLOSE.create());
        cancelEditButton.addClickListener(event -> toggleEditing(userInfo));

        HorizontalLayout layout = new HorizontalLayout(editButton,saveButton,cancelEditButton);
        add(usernameField,passwordButton,emailField,vcidField,layout);
    }


    //toggles the ability to edit the profile
    private void toggleEditing(UserInfo userInfo) {

        isEditing = !isEditing;

        if (!isEditing){
            getData(userInfo);
        }

        editButton.setVisible(!isEditing);
        saveButton.setVisible(isEditing);
        cancelEditButton.setVisible(isEditing);

        emailField.setReadOnly(!isEditing);
        vcidField.setReadOnly(!isEditing);
    }

    private void getData(UserInfo userInfo){

        usernameField.setValue(userInfo.getUsername());
        emailField.setValue(userInfo.getEmail());
        if (userInfo.getVcid() != null){
            vcidField.setValue(userInfo.getVcid());
        }
        else {
            vcidField.setValue("");
        }
    }

    //saves the new user data to the database
    private void saveUserInfo(UserInfo userInfo) {

        String emailValue = emailField.getValue();
        String vcidValue = vcidField.getValue();

        if (checkForExistingAccount(emailValue,vcidValue,userInfo)) {

            userInfo.setEmail(emailValue);
            if (!vcidValue.isEmpty()) {
                userInfo.setVcid(vcidValue);
            }
            else {
                userInfo.setVcid(null);
            }

            userInfoRepository.save(userInfo);
            toggleEditing(userInfo);
            Notification.show("Profile updated successfully");
        }
    }

    private void isValid() {
        saveButton.setEnabled(!emailField.isInvalid() && !vcidField.isInvalid());
    }

    private boolean checkForExistingAccount(String email,String vcid,UserInfo userInfo){
        if (userInfoRepository.existsByEmail(email) && !userInfo.getEmail().equals(email)){
            Notification.show("Account with that email address already exists!");
            return false;
        }

        String userVcid = "";
        if (userInfo.getVcid() != null){
            userVcid = userInfo.getVcid();
        }

        if (userInfoRepository.existsByVcid(vcid) && !userVcid.equals(vcid)){
            Notification.show("Account with that VCID already exists!");
            return false;
        }
        return true;
    }

    private void submit(UserInfo userInfo){

        userInfo.setPasswordToken(RandomString.make(30));
        String emailBody = "Hello " + userInfo.getUsername() + "\nHere is the link to reset your password: \n";
        String resetURL = "http://localhost:8080/reset?token=" + userInfo.getPasswordToken();

        //emailService.sendEmail(email,"Forgot Password?", emailBody + resetURL);
        Notification.show("Reset password email sent to " + userInfo.getEmail(), 3000, Notification.Position.MIDDLE);
        System.out.println(emailBody + resetURL);
        userInfoRepository.save(userInfo);
        }
}
