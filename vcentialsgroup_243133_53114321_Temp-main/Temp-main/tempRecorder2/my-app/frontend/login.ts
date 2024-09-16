import { html, LitElement } from 'lit';
import {customElement, query} from 'lit/decorators.js';
import { LoginI18n, LoginOverlay } from '@vaadin/login/vaadin-login-overlay.js';

// Import the functions you need from the SDKs you need
import { initializeApp } from "firebase/app";
import { getAuth, signInWithEmailAndPassword } from "firebase/auth";

// Your web app's Firebase configuration goes here:
const firebaseConfig = {
    apiKey: "AIzaSyABaLqSrlOk8h2m4uqP6g8xlhupyXP8X3w",
    authDomain: "testing-app-b71bd.firebaseapp.com",
    projectId: "testing-app-b71bd",
    storageBucket: "testing-app-b71bd.appspot.com",
    messagingSenderId: "1048441392268",
    appId: "1:1048441392268:web:d02162b17d8c2edb01ffa9",
    measurementId: "G-X6Z9DLKMZ9"
};

// Initialize Firebase
const app = initializeApp(firebaseConfig);

@customElement('login-view')
class LoginView extends LitElement {

    private i18n: LoginI18n = {
      header: {
        title: 'VCentials Temperature Recorder',
        description: 'Automatically inserted demo credentials: admin@test.com/admin1',
      },
        form: {
        title: 'Sign in',
        username: 'Email',
        password: 'Password',
        submit: 'Login',
        forgotPassword: '',
      },
      errorMessage: {
        title: 'Wrong email/password',
        message: 'Check your credentials and try again..',
      },
    };

    render() {
        return html`
        <vaadin-login-overlay opened .i18n="${this.i18n}" @login="${this._login}"></vaadin-login-overlay>
`;
    }

    @query('vaadin-login-overlay')
    private login?: LoginOverlay;


    private _login(e: CustomEvent) {
        const auth = getAuth();

        signInWithEmailAndPassword(auth, e.detail.username, e.detail.password)
          .then((userCredential: any) => {
            // Signed in
            const user: any = userCredential.user;

             // Pass the token for the server side app for validation & login
             // @ts-ignore
             this.$server.login(user.accessToken, user.uid);
          })
          .catch((error: any) => {
            const errorCode: any = error.code;
            const errorMessage: any = error.message;
            if(this.login) {
              this.login.disabled = false;
              this.login.error = true;
            }
          });
    }
}
