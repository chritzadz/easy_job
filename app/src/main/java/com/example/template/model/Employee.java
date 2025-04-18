package com.example.template.model;

/**
 * @file Employee.java
 * @author: -
 * @description: Represent an Employee in the system, extending User.
 * The Employee class holds information related to employee user role.
 */

public class Employee extends User{
    private String jobFilter;
    private String jobFilterCriteria;

    /**
     * Constructs a new Employee
     * @param email The email address of the employee
     * @param password The email address of the employee
     */
    public Employee(String firstName, String lastName, String email, String password) {
        super(firstName, lastName, email, password);
        super.role = new EmployeeRole();
    }
}
