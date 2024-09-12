How to install & Run on client side

1) Install Maven (https://maven.apache.org/install.html)
 
![image](https://github.com/user-attachments/assets/f7c7e8a9-473f-49fc-a57c-7db83094cecc)


*********************************************************************************************************************************************************************************

Deploying as a .jar

1.  Open up a terminal (Powershell or Command) and "cd" to your my-app directory within your code base.

2.  Run mvn clean package -Pproduction in the my-app directory (You need maven    for this)

 ![image](https://github.com/user-attachments/assets/9b918241-d1cf-462f-bc3b-9fc8298b0219)


3. Move the temprecorder-1.0-SNAPSHOT.jar file in tempRecorder2\my-app\target to your desired location
 

4. Open up a terminal in the directory containing the jar file and run the command java -jar temprecorder-1.0-SNAPSHOT.jar (Make sure you don’t have any ports open on whatever port you set up the project to use in the application.properties file.) The app should be running at http://localhost:8080/login

![image](https://github.com/user-attachments/assets/bfab6c79-3b24-4c69-8ccc-7f0456376014)

