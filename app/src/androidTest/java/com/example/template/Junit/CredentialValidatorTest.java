package com.example.template.Junit;

import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.*;

import com.example.template.controller.CredentialValidator;

@RunWith(AndroidJUnit4.class)
public class CredentialValidatorTest {

    // Test cases for password validation
    @Test
    public void testIsValidPassword() {
        assertTrue(CredentialValidator.isValidPassword("Valid1!@"));
        assertTrue(CredentialValidator.isValidPassword("StrongPassword1@#"));
    }

    @Test
    public void testIsNotValidPassword() {
        assertFalse(CredentialValidator.isValidPassword(null));
        assertFalse(CredentialValidator.isValidPassword(""));
        assertFalse(CredentialValidator.isValidPassword("short"));
        assertFalse(CredentialValidator.isValidPassword("nouppercase1!"));
        assertFalse(CredentialValidator.isValidPassword("NOLOWERCASE1!"));
        assertFalse(CredentialValidator.isValidPassword("NoNumber!"));
        assertFalse(CredentialValidator.isValidPassword("NoSpecialChar1"));
    }


    @Test
    public void testIsValidEmail() {
        assertTrue(CredentialValidator.isValidEmail("valid.email@example.com"));
        assertTrue(CredentialValidator.isValidEmail("user-name@domain.ca"));
    }

    @Test
    public void testIsNotValidEmail() {
        assertFalse(CredentialValidator.isValidEmail(null));
        assertFalse(CredentialValidator.isValidEmail(""));
        assertFalse(CredentialValidator.isValidEmail("invalid-email"));
    }

    @Test
    public void testIsValidField() {
        assertTrue(CredentialValidator.isValidField("Test"));
    }
    @Test
    public void testIsNotValidField() {
        assertFalse(CredentialValidator.isValidField(""));
    }
}