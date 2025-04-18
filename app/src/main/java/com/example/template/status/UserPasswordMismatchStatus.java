package com.example.template.status;

public class UserPasswordMismatchStatus extends ConcreteStatus{
    @Override
    public String message() {
        return "Wrong Password";
    }
}
