package com.example.template.status;

public class EmptyPasswordStatus extends ConcreteStatus{
    @Override
    public String message() {
        return "Please enter a password";
    }
}
