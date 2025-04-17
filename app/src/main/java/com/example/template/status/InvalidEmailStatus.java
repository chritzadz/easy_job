package com.example.template.status;

public class InvalidEmailStatus extends ConcreteStatus{
    public String message(){
        return "Please enter a valid email address";
    }
}
