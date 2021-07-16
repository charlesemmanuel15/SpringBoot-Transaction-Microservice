# SpringBoot-Transaction-Microservice

This is a MicroServices for the Reloadly Code Challenge.

It is responsible for making transactions of VOICE and DATA subscriptions. Customers can purchase voice or data plans of either DAILY, WEEKLY or MONTHLY duration.

Inorder to make a transaction you must be a registered user on the platform, that means you must have created a account on the Account MicroService, you must have signed and generated a JWT Token using the auth endpoint in the Account MicroService.

You must send the JWT generated from the auth endpoint as an Authorization Header in this format "Bearer {token}" else you will not be permitted to perform a transaction.

Upon completing a successful transaction an API call is made to the Notification MicroService to send an email notification containing the transaction details to the customer.

Running the project locally
To run the project locally you need to:

Have mysql installed and running;

Create the db "reloadly" on mysql.

Update the dev.properties file with your mysql credentials

Set spring.profiles.active=dev in the application.properties file

Run the project

Open your browser and go to "http://localhost:8083/swagger-ui.html" to access the swagger doc from where you can test the endpoints.

Executing the Unit Tests
To execute the unit tests simply run "mvn clean package" on the terminal and allow it to execute or you can navigate to the test package and execute the tests from there.

Test Coverage
Jacoco is used for test coverage.

To see the test coverage, run "mvn clean package". When it's done packaging, navigate to the target folder, inside the target folder, navigate to the "site" folder, inside the site folder, open the jacoco folder you will see an index.html file. Open the html page in your browser to see the test coverage.

Deployment
This MicroService is deployed to Heroku.

Use the below url to access the swagger ui on Heroku for your testing pleasure.

https://reloadly-transaction-services.herokuapp.com/swagger-ui.html

Please reach out to me for any question or clarification (Kelechi Emmanuel C).
