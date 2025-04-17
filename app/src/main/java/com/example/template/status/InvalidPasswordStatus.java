package com.example.template.status;

public class InvalidPasswordStatus extends ConcreteStatus{
    @Override
    public String message() {
        return "Password must be at least 8 characters long, contain uppercase, lowercase, digit, and special character";
    }
}
