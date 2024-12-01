package com.cthiebaud.passwordvalidator;

/**
 * The {@code PasswordValidator} interface defines a contract for validating
 * potential passwords based on specific criteria. Implementing classes must
 * provide their own validation logic and a description of the criteria used.
 * 
 * <p>
 * This interface facilitates the creation of various password validation
 * strategies, allowing users to easily integrate and manage multiple validation
 * rules in a consistent manner.
 * </p>
 * 
 * @see ValidationResult
 */
public interface PasswordValidator {
    /**
     * Validates a potential password based on specific criteria.
     * 
     * @param potentialPassword the password to validate
     * @return ValidationResult containing the validation status and an optional
     *         message describing the result of the validation
     */
    ValidationResult validate(String potentialPassword);

    /**
     * Provides the prompt message to display to the user before requesting password
     * entry.
     * 
     * <p>
     * The prompt message is intended to inform the user of the password
     * requirements
     * or other relevant details before they input their password. Implementations
     * of this method should return a clear and user-friendly message.
     * </p>
     * 
     * @return a {@code String} containing the prompt message to display to the user
     */
    String prompt();

    /**
     * Default method to provide an ExitHandler. Implementations can override this
     * if needed.
     *
     * @return the default ExitHandler
     */
    default ExitHandler getExitHandler() {
        return new DefaultExitHandler();
    }
}
