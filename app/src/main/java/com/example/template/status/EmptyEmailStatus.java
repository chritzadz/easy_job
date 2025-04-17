package com.example.template.status;

public class EmptyEmailStatus extends ConcreteStatus{
    public String message(){
        return "Please enter an email address";
    }
}
