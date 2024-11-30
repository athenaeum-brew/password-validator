package com.cthiebaud.passwordvalidator;

/**
 * Represents the result of a password validation.
 *
 * @param isValid Indicates whether the password is valid.
 * @param message A message providing details about the validation result.
 */
public record ValidationResult(boolean isValid, String message) {
    // No additional methods or fields are needed, as the record automatically
    // provides accessors
}
