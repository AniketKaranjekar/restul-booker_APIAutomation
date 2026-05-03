# OpenMRS API Automation Framework

## Overview

This project is a Java-based API automation framework designed for testing OpenMRS APIs. OpenMRS is a real-world healthcare system, making this framework closer to enterprise-level API testing.

It demonstrates handling of authentication, session management, and complex payload validations.

---

## Tech Stack

* Language: Java
* API Testing: Rest Assured
* Test Framework: TestNG
* Build Tool: Maven
* Reporting: Extent Reports
* Logging: Log4j2
* CI/CD: GitHub Actions

---

## Features

* Real-world API automation (healthcare domain)
* Session-based authentication
* Complex payload handling
* Scalable framework design
* Logging and reporting
* CI-ready setup

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
```

---

## Key Functionalities

* Authentication and session handling
* API validation with real-world scenarios
* Request and response verification
* Reusable API methods

---

## How to Run

Run all tests:

```
mvn clean test
```

Run specific test:

```
mvn test -Dtest=OpenMRSTests
```

---

## Reporting

Extent Report available at:

```
target/ExtentReports.html
```

---

## Logging

* Log4j2 integrated
* Captures full API execution flow

---

## CI/CD

* GitHub Actions configured
* Automated test execution on code push

---

## Why This Project Matters

* Works with real enterprise-level APIs
* Demonstrates handling of complex systems
* Shows understanding beyond basic CRUD APIs

---

## Author

Aniket
QA Engineer | API Automation
