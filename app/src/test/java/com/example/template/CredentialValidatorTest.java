package com.example.template;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import com.example.template.controller.CredentialValidator;

import org.junit.Test;

public class CredentialValidatorTest {
    @Test
    public void testValidPassword() {
        // Test with a valid password
        String validPassword = "Password@123";
        assertTrue(CredentialValidator.isValidPassword(validPassword));
    }

    @Test
    public void testPasswordTooShort() {
        // Test with a password that is too short
        String shortPassword = "Pas@1";
        assertFalse(CredentialValidator.isValidPassword(shortPassword));
    }

    @Test
    public void testPasswordNoUppercase() {
        // Test with a password that has no uppercase letter
        String noUppercasePassword = "password@123";
        assertFalse(CredentialValidator.isValidPassword(noUppercasePassword));
    }

    @Test
    public void testPasswordNoLowercase() {
        // Test with a password that has no lowercase letter
        String noLowercasePassword = "PASSWORD@123";
        assertFalse(CredentialValidator.isValidPassword(noLowercasePassword));
    }

    @Test
    public void testPasswordNoDigit() {
        // Test with a password that has no digit
        String noDigitPassword = "Password@";
        assertFalse(CredentialValidator.isValidPassword(noDigitPassword));
    }

    @Test
    public void testPasswordNoSpecialCharacter() {
        // Test with a password that has no special character
        String noSpecialCharPassword = "Password123";
        assertFalse(CredentialValidator.isValidPassword(noSpecialCharPassword));
    }

    @Test
    public void testPasswordIsNull() {
        // Test with a null password
        assertFalse(CredentialValidator.isValidPassword(null));
    }

    @Test
    public void testPasswordIsEmpty() {
        // Test with an empty password
        assertFalse(CredentialValidator.isValidPassword(""));
    }

    @Test
    public void testValidEmail() {
        String validEmail = "User_1.2-3@example.com";
        assertTrue(CredentialValidator.isValidEmail(validEmail));
    }

     @Test
    public void testEmailIsEmpty() {
        String emptyEmail = "";
        assertFalse(CredentialValidator.isValidEmail(emptyEmail));
    }

    @Test
    public void testEmailNoAtSign() {
        String emailNoAtSign = "User_1.2-3example.com";
        assertFalse(CredentialValidator.isValidEmail(emailNoAtSign));
    }

    @Test
    public void testEmailNoDot() {
        String emailNoDot = "User_1.2-3@examplecom";
        assertFalse(CredentialValidator.isValidEmail(emailNoDot));
    }

    @Test
    public void testEmailNoUsername() {
        String emailNoUsername = "@example.com";
        assertFalse(CredentialValidator.isValidEmail(emailNoUsername));
    }

    @Test
    public void testEmailNoServer() {
        String emailNoServer = "User_1.2-33@.com";
        assertFalse(CredentialValidator.isValidEmail(emailNoServer));
    }

    @Test
    public void testEmailNoTopLevelDomain() {
        String emailNoDomain = "User_1.2-3@example.";
        assertFalse(CredentialValidator.isValidEmail(emailNoDomain));
    }
}
