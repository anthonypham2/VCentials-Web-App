How to install & Run on client side

1) Install Maven (https://maven.apache.org/install.html)

2) You'll need to create a Firebase Project and enable Firebase Authentication (refer to their docs). 
The ./serviceAccountKey.json file needs to exist and contain your app credentials (or change the behaviour in FirebaseService) and you need to replace the Web/JS firebaseConfig with your Firebase web config in ./frontend/login.ts file.

<details>
<summary>Firebase Guide</summary>

You can set up **everything via the Firebase Console (GUI)**. This includes generating the **Service Account credentials** for the backend (Firebase Admin SDK) and configuring the **Firebase Web SDK** for the frontend. Here's how you can do each part using the Firebase Console.

### 1. **Generate Firebase Service Account Credentials via the Firebase Console (GUI)**

You can generate the `serviceAccountKey.json` file that contains your Firebase app credentials for the backend (Firebase Admin SDK) via the Firebase Console:

- **Steps**:
    1. Go to the [Firebase Console](https://console.firebase.google.com/).
    2. Select your Firebase project.
    3. In the **Project Settings**, go to the **Service accounts** tab.
    4. Click **Generate new private key**.
    5. This will download a `serviceAccountKey.json` file, which contains your app credentials. You can use this file in your backend to authenticate with Firebase.

- **Direct Link**:
    - [Firebase Console: Service Accounts](https://console.firebase.google.com/u/0/project/_/settings/serviceaccounts/adminsdk)

- **Documentation**:
    - [Generate Firebase Admin SDK Credentials](https://firebase.google.com/docs/admin/setup#initialize-sdk)

### 2. **Find and Configure Firebase Web SDK via the Firebase Console (GUI)**

You can configure your web app frontend (Firebase Web SDK) by getting the `firebaseConfig` from the Firebase Console.

- **Steps**:
    1. Go to the [Firebase Console](https://console.firebase.google.com/).
    2. Select your project.
    3. Click on the **Project settings** (gear icon in the upper left).
    4. Under **Your apps**, find your Web app or create a new one by clicking **Add app** → **Web**.
    5. After creating or selecting your Web app, Firebase will display the **firebaseConfig** object. This is what you need to copy and use in the `./frontend/login.ts` file.

- **Direct Link**:
    - [Firebase Console: Project Settings](https://console.firebase.google.com/u/0/project/_/settings/general)

- **Documentation**:
    - [Set up Firebase in Web Apps](https://firebase.google.com/docs/web/setup)

---

### Everything in the GUI Summary:

- You can **generate the `serviceAccountKey.json`** file for your backend Firebase Admin SDK using the **Firebase Console Service Accounts** tab.
- You can find and configure the **Firebase Web SDK** by creating a web app in the **Firebase Console Project Settings**.
</details>
 
![image](https://github.com/user-attachments/assets/f7c7e8a9-473f-49fc-a57c-7db83094cecc)



*********************************************************************************************************************************************************************************

Deploying as a .jar

1.  Open up a terminal (Powershell or Command) and "cd" to your my-app directory within your code base.

2.  Run mvn clean package -Pproduction in the my-app directory (You need maven    for this)

 ![image](https://github.com/user-attachments/assets/9b918241-d1cf-462f-bc3b-9fc8298b0219)


3. Move the temprecorder-1.0-SNAPSHOT.jar file in tempRecorder2\my-app\target to your desired location
 

4. Open up a terminal in the directory containing the jar file and run the command java -jar temprecorder-1.0-SNAPSHOT.jar (Make sure you don’t have any ports open on whatever port you set up the project to use in the application.properties file.) The app should be running at http://localhost:8080/login

![image](https://github.com/user-attachments/assets/bfab6c79-3b24-4c69-8ccc-7f0456376014)


*********************************************************************************************************************************************************************************

Still need to Accomplish:
 - Get Metrics Page to load
 - Admin page needs to show and print out logging
 - When creating account, need to verify via email (or other)
 - Add a captcha for account creation
 - Apply a server host for the web app (Supports Java, such as an apache variety)
 - Added security for passing information, disabling development tools, etc
 - Color coding elements outside of nominal range for input temps (red=too hot, blue = too cold)
 - Protect Main Branch on Github
 - Change the database to a type that is persistent between runs, remains secure, and has as low of an install requirement as possible for the client and server.
 - Admin view needs to add the ability to change username, and clean up junk code left over from cloning Home page


*********************************************************************************************************************************************************************************
When Ready to Deploy:

https://hostingtutorials.dev/blog/free-spring-boot-host-with-render

or try:


    Heroku

    Render

    DigitalOcean App Platform

    Platform Sh

    Google App Engine

    Azure App Service

    Netlify

    AWS Elastic Beanstalk

    Red Hat OpenShift

    Engine Yard

    Firebase

*********************************************************************************************************************************************************************************
Additional Resources:

https://vaadin.com/docs/latest/components - For database and component use

https://start.spring.io/ - Create a Spring Framework Project

	
