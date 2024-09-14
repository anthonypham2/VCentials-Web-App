import { html, LitElement } from 'lit';
import { customElement } from 'lit/decorators.js';

// Import Firebase SDK functions
import { initializeApp } from 'firebase/app';
import { getAuth, signInWithEmailAndPassword, GoogleAuthProvider, FacebookAuthProvider, GithubAuthProvider, TwitterAuthProvider, signInWithPopup } from 'firebase/auth';

// Your web app's Firebase configuration, this is public info and does not grant any special access.
// Steps to get:
// 1. Go to the Firebase Console.
// 2. Create a new project (if you haven't already).
// 3. Add Firebase to your web app and copy the Firebase config object from the setup instructions.
const firebaseConfig = {
  apiKey: "AIzaSyB2PRrS6sk-Hgtvxv__FViNCHBh7Esvv6o",
  authDomain: "fir-vaadinexample.firebaseapp.com",
  projectId: "fir-vaadinexample",
  storageBucket: "fir-vaadinexample.appspot.com",
  messagingSenderId: "344504568176",
  appId: "1:344504568176:web:68ee967ce7f9e4707fe7b5"
};

// Initialize Firebase
const app = initializeApp(firebaseConfig);

@customElement('login-view')
class LoginView extends LitElement {

    // Define properties to hold user credentials for email/password login
    private email = '';
    private password = '';

    render() {
        return html`
            <div>
                <h1>Choose Login Method</h1>

                <!-- Email/Password Login -->
                <div>
                    <input id="email" type="email" placeholder="Enter Email" @input="${this._updateEmail}">
                    <input id="password" type="password" placeholder="Enter Password" @input="${this._updatePassword}">
                    <button @click="${this._loginWithEmail}">Login with Email/Password</button>
                </div>

                <br>

                <!-- Google Login Button -->
                <button @click="${this._loginWithGoogle}">Login with Google</button>

                <br>

                <!-- Facebook Login Button -->
                <button @click="${this._loginWithFacebook}">Login with Facebook</button>

                <br>

                <!-- GitHub Login Button -->
                <button @click="${this._loginWithGithub}">Login with GitHub</button>

                <br>

                <!-- Twitter Login Button -->
                <button @click="${this._loginWithTwitter}">Login with Twitter</button>
            </div>
        `;
    }

    // Method to update email when the input field changes
    private _updateEmail(event: Event) {
        this.email = (event.target as HTMLInputElement).value;
    }

    // Method to update password when the input field changes
    private _updatePassword(event: Event) {
        this.password = (event.target as HTMLInputElement).value;
    }

    // Method for Email/Password login
    private _loginWithEmail() {
        const auth = getAuth();
        signInWithEmailAndPassword(auth, this.email, this.password)
            .then((userCredential) => {
                const user: any = userCredential.user;
                console.log('User signed in with email:', user);
                // Pass the token to the backend for validation and login
                // @ts-ignore
                this.$server.login(user.accessToken, user.uid);
            })
            .catch((error) => {
                console.error('Error during email/password login:', error.message);
                alert('Login failed: ' + error.message);
            });
    }

    // Method for Google login
    private _loginWithGoogle() {
        const auth = getAuth();
        const provider = new GoogleAuthProvider();

        signInWithPopup(auth, provider)
            .then((result) => {
                const user: any = result.user;
                console.log('User signed in with Google:', user);
                // Pass the token to the backend for validation and login
                // @ts-ignore
                this.$server.login(user.accessToken, user.uid);
            })
            .catch((error) => {
                console.error('Error during Google login:', error.message);
                alert('Google login failed: ' + error.message);
            });
    }

    // Method for Facebook login
    private _loginWithFacebook() {
        const auth = getAuth();
        const provider = new FacebookAuthProvider();

        signInWithPopup(auth, provider)
            .then((result) => {
                const user: any = result.user;
                console.log('User signed in with Facebook:', user);
                // Pass the token to the backend for validation and login
                // @ts-ignore
                this.$server.login(user.accessToken, user.uid);
            })
            .catch((error) => {
                console.error('Error during Facebook login:', error.message);
                alert('Facebook login failed: ' + error.message);
            });
    }

    // Method for GitHub login
    private _loginWithGithub() {
        const auth = getAuth();
        const provider = new GithubAuthProvider();

        signInWithPopup(auth, provider)
            .then((result) => {
                const user: any = result.user;
                console.log('User signed in with GitHub:', user);
                // Pass the token to the backend for validation and login
                // @ts-ignore
                this.$server.login(user.accessToken, user.uid);
            })
            .catch((error) => {
                console.error('Error during GitHub login:', error.message);
                alert('GitHub login failed: ' + error.message);
            });
    }

    // Method for Twitter login
    private _loginWithTwitter() {
        const auth = getAuth();
        const provider = new TwitterAuthProvider();

        signInWithPopup(auth, provider)
            .then((result) => {
                const user: any = result.user;
                console.log('User signed in with Twitter:', user);
                // Pass the token to the backend for validation and login
                // @ts-ignore
                this.$server.login(user.accessToken, user.uid);
            })
            .catch((error) => {
                console.error('Error during Twitter login:', error.message);
                alert('Twitter login failed: ' + error.message);
            });
    }
}