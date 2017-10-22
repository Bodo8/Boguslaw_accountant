"# Boguslaw_accountant"

The project currently has a Spring Boot, Swagger, Hibernate, MySQL.
For correct action of the application, please:
Run MySQL with  database named: accounting_system
The base must have:
username: root
password: admin

Application - accountancy system. Our basic version of the system allowed
to add (delete, modify and list) invoices, calculate income and VAT taxes. System
allowed to generate PDF reports with taxes to be paid, was able to send email
summaries about invoices registered in the system and allowed to interact with
the system with both SOAP and REST APIs. System was based on MySQL, Spring Boot
and Hibernate frameworks. All the time we worked with GIT version control system
using the Git Flow with frequent Pull Request.
Checkstyle was used to keep quality high - all those tools were integrated with Maven.
For testing we used JUnit, Mockito, JUnitParams, MockMvc and RestAssured,
for SOAP - JAXB, for REST - Jackson, to document REST services Swagger was introduced. 