package com.example.template.model;

/**
 * @file Employer.java
 * @author: -
 * @description: Represent an Employer in the system, extending User.
 * The Employer class holds information related to employer user role.
 */

public class Employer extends User{
    /**
     * Constructs a new Employer
     * @param email The email address of the employer
     * @param password The email address of the employer
     */
    public Employer(String first, String last, String email, String password) {
        super(first, last, email, password);
        super.role = "Employer";
    }
}
