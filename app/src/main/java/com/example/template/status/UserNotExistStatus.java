package com.example.template.status;

public class UserNotExistStatus extends ConcreteStatus{

    @Override
    public String message() {
        return "Email not registered";
    }
}
