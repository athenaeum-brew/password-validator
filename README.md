# Password Validator

## Overview

The Password Validator project provides a framework for validating passwords based on specific criteria. It consists of an interface for password validation and a record for representing the result of the validation. This project is designed to help students implement their own password validation logic.

## Components

### PasswordValidator Interface

The `PasswordValidator` interface defines a contract for validating potential passwords based on specific criteria. Implementing classes must provide their own validation logic and describe the criteria used.

### ValidationResult Record

The `ValidationResult` record represents the result of a password validation, containing the validation status and an optional message.

### PasswordValidatorTester Class

The `PasswordValidatorTester` class serves as the main entry point of the application. It dynamically loads and tests password validators implemented by students.


## Student Deliverables

Students are expected to:

1. Implement the `PasswordValidator` interface by creating their own validation class (e.g., `PasswordLengthValidator`) that adheres to specific criteria they can invent.
2. Include the implementation in a JAR file.
3. Provide a description of the validation criteria used in their implementation.
4. Ensure the `PasswordValidatorTester` class can successfully load and test their implementation.

## Usage

To use the Password Validator, run the `PasswordValidatorTester` class and provide the necessary inputs as required.

## License

This project is licensed under the MIT License.

---

Feel free to adjust any section or wording as needed!