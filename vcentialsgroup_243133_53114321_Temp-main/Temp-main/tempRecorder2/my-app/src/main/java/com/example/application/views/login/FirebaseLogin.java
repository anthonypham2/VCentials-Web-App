package com.example.application.views.login;

import com.example.application.security.FirebaseService;
import com.example.application.views.home.HomeView;
import com.google.firebase.auth.FirebaseAuthException;
import com.vaadin.flow.component.ClientCallable;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.NpmPackage;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.EmailField;
import com.vaadin.flow.component.textfield.PasswordField;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

/**
 * Includes client-side JS code with the Firebase Node SDK
 * and backend code using the Firebase Admin Java library.
 */
@NpmPackage(value = "firebase", version = "9.6.7")
public class FirebaseLogin extends VerticalLayout {

    private final FirebaseService firebase;
    public final EmailField emailField;
    public final PasswordField passwordField;
    public final Button loginButton;
    public final Button googleButton;
    public final Button facebookButton;
    public final Button githubButton;
    public final Button twitterButton;

    public FirebaseLogin(FirebaseService firebaseService) {
        this.firebase = firebaseService;

        // Email and Password Fields
        emailField = new EmailField("Enter Email");
        passwordField = new PasswordField("Enter Password");

        // Email/Password Login Button
        loginButton = new Button("Login with Email/Password", e -> loginWithEmail());

        // Google Login Button
        googleButton = new Button("Login with Google", e -> loginWithProvider("google"));

        // Facebook Login Button
        facebookButton = new Button("Login with Facebook", e -> loginWithProvider("facebook"));

        // GitHub Login Button
        githubButton = new Button("Login with GitHub", e -> loginWithProvider("github"));

        // Twitter Login Button
        twitterButton = new Button("Login with Twitter", e -> loginWithProvider("twitter"));

        // Add components to layout
        add(emailField, passwordField, loginButton, googleButton, facebookButton, githubButton, twitterButton);

        // Initialize Firebase when the view loads
        addAttachListener(e -> {
            if(e.isInitialAttach()) initializeFirebase();
        });
    }

    private void initializeFirebase() {
        // Firebase configuration using a text block
        String firebaseConfig = """
            {
                apiKey: 'AIzaSyB2PRrS6sk-Hgtvxv__FViNCHBh7Esvv6o',
                authDomain: 'fir-vaadinexample.firebaseapp.com',
                projectId: 'fir-vaadinexample',
                storageBucket: 'fir-vaadinexample.appspot.com',
                messagingSenderId: '344504568176',
                appId: '1:344504568176:web:68ee967ce7f9e4707fe7b5'
            }
            """;

        // JavaScript initialization code with embedded Firebase config
        String initScript = """
            const firebaseConfig = %s;
            firebase.initializeApp(firebaseConfig);
            """.replace("%s", firebaseConfig);

        // Execute the JavaScript to initialize Firebase
        UI.getCurrent().getPage().executeJs(initScript);
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

    private void loginWithEmail() {
        String email = emailField.getValue();
        String password = passwordField.getValue();

        // JavaScript code using a text block with placeholders
        String loginScript = """
            const auth = firebase.auth();
            auth.signInWithEmailAndPassword('%s', '%s')
                .then((userCredential) => {
                    const user = userCredential.user;
                    console.log('User signed in with email:', user);
                    $0.$server.login(user.accessToken, user.uid);
                })
                .catch((error) => {
                    console.error('Error during email login:', error.message);
                    alert('Login failed: ' + error.message);
                });
            """;

        // Replace placeholders and execute the JavaScript
        UI.getCurrent().getPage().executeJs(String.format(loginScript, email, password));
    }

    private void loginWithProvider(String providerName) {
        String providerScript = getProviderScript(providerName);

        // Execute JavaScript to handle third-party login (Google, Facebook, etc.)
        UI.getCurrent().getPage().executeJs(providerScript);
    }

    private String getProviderScript(String providerName) {
        String provider = switch (providerName) {
            case "google" -> "new firebase.auth.GoogleAuthProvider()";
            case "facebook" -> "new firebase.auth.FacebookAuthProvider()";
            case "github" -> "new firebase.auth.GithubAuthProvider()";
            case "twitter" -> "new firebase.auth.TwitterAuthProvider()";
            default -> throw new IllegalArgumentException("Unsupported provider: " + providerName);
        };

        // Use Java Text Blocks for the provider login script with placeholder replacement
        String providerLoginScript = """
            const auth = firebase.auth();
            const provider = %s;
            auth.signInWithPopup(provider)
                .then((result) => {
                    const user = result.user;
                    console.log('User signed in with %s:', user);
                    $0.$server.login(user.accessToken, user.uid);
                })
                .catch((error) => {
                    console.error('Error during %s login:', error.message);
                    alert('%s login failed: ' + error.message);
                });
            """;

        // Replace placeholders (provider and providerName)
        return String.format(providerLoginScript, provider, providerName, providerName, providerName);
    }
}