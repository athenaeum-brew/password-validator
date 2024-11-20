# Password Validator

## Overview

The Password Validator project provides a framework for validating passwords based on specific criteria. It consists of an interface for password validation and a record for representing the result of the validation. This project is designed to help students implement their own password validation logic.

## Components

### PasswordValidator Interface

The `PasswordValidator` interface defines a contract for validating potential passwords based on specific criteria. Implementing classes must provide their own validation logic and describe the criteria used.

### ValidationResult Record

The `ValidationResult` record represents the result of a password validation, containing the validation status and an optional message.

### PasswordValidatorTester Class

The `PasswordValidatorTester` dynamically loads and tests password validators implemented by students.

#### Usage

To test your Password Validator Implementation, download un run the jar password-validator from [athenaeum-brew/maven-repo](https://github.com/athenaeum-brew/maven-repo/packages/2291424) and provide the necessary input(s) as required.

```bash
$ java -jar password-validator-x.y.z.jar
$ Argument missing: <path-to-the-jar-containing-your-implementation>
```

## TODO

Students are expected to:

0. Form a team of minimal 2 maximal 3 persons. Every deliverable below must be the result of a group work.
1. Each team will implement the `PasswordValidator` interface by creating their own validation class (e.g., `PasswordLengthValidator`) that adheres to specific criteria they can invent.
2. Include the implementation in a JAR file.
3. Provide a description of the validation criteria used in their implementation.
4. Ensure the `PasswordValidatorTester` class can successfully load and test their implementation.

## Deliverables

1. A JAR file containing the implementation.
2. A pom.xml, that MUST contain 
- a `<scm>` section that points to the source code of the project in Github - cf [scm @ pom reference](https://maven.apache.org/pom.html#SCM)
- a `<developers>` section that lists the Github IDs of the students involved in the project - cf [developers @ pom reference](https://maven.apache.org/pom.html#Developers)
3. Jar and pom.xml files MUST be pushed to [athenaeum-brew maven repo](https://github.com/orgs/athenaeum-brew/packages), such as described in the ["Collaborative Artifact Sharing in Maven using GitHub"](https://athenaeum.cthiebaud.com/index0.html?%2Fexercises%2F11.md) exercice
4. Validation criteria MUST be described in a README file at the root of the Github project
- README format can be any of [plain text, markdown, pdf, latex]. Please do not use exotic formats such as Microsoft Word
5. Source code MUST contain javadoc comments
6. Implementation MUST be unit tested

## Reference Implementation 

See [cthiebaud's password-validator-impl project](https://github.com/athenaeum-brew/password-validator-impl)


## License

This project is licensed under the MIT License.

---

Feel free to adjust any section or wording as needed!