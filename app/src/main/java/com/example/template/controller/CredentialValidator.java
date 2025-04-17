/**
 *  File: CredentialValidator.java
 *  Author: -
 *  Description: This is a utility class to help validating users credentials
 */

package com.example.template.controller;
import com.example.template.status.EmptyEmailStatus;
import com.example.template.status.EmptyPasswordStatus;
import com.example.template.status.InvalidPasswordStatus;
import  com.example.template.status.Status;
import com.example.template.status.InvalidEmailStatus;
import com.example.template.status.SuccessStatus;

public class CredentialValidator {
    private CredentialValidator() {}

    public static Status checkPassword(String password) {
        if (password == null || password.isEmpty()) {
            return new EmptyPasswordStatus();
        }
        else if (password.length() < 8 && !password.matches(".*[A-Z].*") && !password.matches(".*[a-z].*") && !password.matches(".*\\d.*") && !password.matches(".*[!@#$%^&+=].*")){
            return new InvalidPasswordStatus();
        }
        return new SuccessStatus();
    }

    public static Status checkEmail(String email) {
        if (email == null || email.isEmpty()){
            return new EmptyEmailStatus();
        }
        else if (!email.matches("[a-zA-Z0-9-._]+@[a-zA-Z]+\\.[a-zA-Z]+")){
            return new InvalidEmailStatus();
        }
        return new SuccessStatus();
    }
}
