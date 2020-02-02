# Rest Authentication Service #

The service introduces REST calls for simple authentication (register/ login).  


# Steps to Setup: #

1. Clone the repository: 

        $ git clone https://github.com/danitnegbi92/authentication-system.git

2. Configure MySQL database:

    - Install MySQL database.

    - Change in the projects the username and password in src/main/resources/application.properties as per your MySQL installation:

        spring.datasource.username= #YOUR MYSQL USERNAME#

        spring.datasource.password= #YOUR MYSQL PASSWORD#     
   
    - Start MySql

3. Run the app using maven

    In terminal:

        $ cd {YOUR_PROJECT_PATH}/authentication-system/

        $ mvn spring-boot:run

    The app is running on localhost:9000.

4. When the app is running, you can see and test all the rest API calls in Swagger UI:

    http://localhost:9000/swagger-ui.html#
    
5. This project has Lombok dependency.
   
   If you want to avoid seeing errors of Getters and Setters methods in the project, you can add Lombok plugin to your IDE.
   It will still run without the plugin. 



# JSON Entities: #

1. UserRegistration Entity JSON Example: 

{
  "email": "string",
  "password": "string"
}

# Assumptions: #

* The Authorization token is sent in the header request / response.
* I stored the real email address without hashing, because I read that storing the email address has the advantage of contact users.
* I put the secret key file (secretKey.txt) in the project for easy access.
* I used jbcrypt 3rd party library to hash the password (with salt).
* I used jjwt 3rd party library to generate and decode Authorization token.
* I enclosed MySQL file (db_users_user.sql) in: src/main/resources/db_users_user.sql
