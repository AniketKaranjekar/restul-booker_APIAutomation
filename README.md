# Restful Booker API Automation Framework

## Overview

This project is a Java-based API automation framework built using Rest Assured and TestNG. It covers end-to-end testing of booking APIs including Create, Read, Update, and Delete operations.

The framework demonstrates real-world QA practices such as data-driven testing, authentication handling, logging, reporting, and CI integration.

---

## Tech Stack

* Language: Java
* API Testing: Rest Assured
* Test Framework: TestNG
* Build Tool: Maven
* Reporting: Extent Reports
* Logging: Log4j2
* Test Data: Excel (Apache POI)
* CI/CD: GitHub Actions

---

## Features

* End-to-End API testing (CRUD)
* Data-driven testing using Excel
* Token-based authentication
* Logging with Log4j2
* Extent HTML reports
* CI execution support
* Clean layered architecture

---

## Project Structure

```
src
 ├── test
 │   ├── java
 │   │   ├── api.endpoints
 │   │   ├── api.payload
 │   │   ├── api.test
 │   │   └── api.utilities
 │   └── resources
 │       └── BookingData.xlsx
```

---

## Base URL

```
https://restful-booker.herokuapp.com
```

---

## API Coverage

* POST /auth
* POST /booking
* GET /booking/{id}
* PUT /booking/{id}
* DELETE /booking/{id}

---

## How to Run

Run all tests:

```
mvn clean test
```

Run specific test:

```
mvn test -Dtest=BookingTests
```

---

## Data Driven Testing

* Test data stored in Excel
* Location:

```
src/test/resources/BookingData.xlsx
```

---

## Reporting

Extent Report generated at:

```
target/ExtentReports.html
```

---

## Logging

* Implemented using Log4j2
* Logs include request, response, and execution flow

---

## CI/CD

* GitHub Actions integrated
* Runs tests automatically on push

---

## Future Improvements

* Environment-based execution
* Parallel execution
* JSON schema validation
* Docker support

---

## Author

Aniket
QA Engineer | API Automation
