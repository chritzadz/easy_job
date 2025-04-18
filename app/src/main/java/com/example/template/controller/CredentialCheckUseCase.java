package com.example.template.controller;

import com.example.template.status.Status;
import com.example.template.status.SuccessStatus;

public class CredentialCheckUseCase {
    private static Status status = null;
    private CredentialCheckUseCase() {
        throw new IllegalStateException("Utility class");
    }
    public static Status validateSignUp(String email, String password){
        //check email
        status = CredentialValidator.checkEmail(email);
        if (!status.equals(new SuccessStatus())){
            return status;
        }

        status = CredentialValidator.checkPassword(password);
        if (!status.equals(new SuccessStatus())){
            return status;
        }

        return status;
    }
}
