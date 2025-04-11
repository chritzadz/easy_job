/**
 *  File: CredentialValidator.java
 *  Author: -
 *  Description: This is a utility class to help validating users credentials
 */

package com.example.template.controller;

public class CredentialValidator {
    private CredentialValidator() {}

    /**
     * Determine whether the password is in valid format:
     * 1. not empty
     * 2. at least 8 characters long
     * 3. contains at least one lowercase letter
     * 4. contains at least one uppercase letter
     * 5. contains at least one digit
     * 6. contains at least one special character
     * @param password The password input in the form of a string
     * @return A boolean value determining whether the password is valid
     */
    public static boolean isValidPassword(String password) {
        return password != null
                && !password.isEmpty()
                && password.length() >= 8
                && password.matches(".*[A-Z].*")
                && password.matches(".*[a-z].*")
                && password.matches(".*\\d.*")
                && password.matches(".*[!@#$%^&+=].*");
    }

    /**
     * Determine whether the email is in valid format:
     * 1. not empty
     * 2. not null
     * 3. format should contains @ and . in the in order
     * @param email The email input in the form of a string
     * @return A boolean value determining whether the email is valid
     */
    public static boolean isValidEmail(String email) {
        return email != null && !email.isEmpty() && email.matches("[a-zA-Z0-9-._]+@[a-zA-Z]+\\.[a-zA-Z]+");
    }

    /**
     * Determine whether the field is valid, which is only when:
     * 1. not null
     * 2. not empty
     * @param field The field input in the form of a string
     * @return A boolean value determining whether the field is valid
     */
    public static boolean isValidField(String field) {
        return field != null && !field.isEmpty();
    }
}
