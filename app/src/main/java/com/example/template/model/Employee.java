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
        super.role = "Employee";
        this.jobFilter = "none";
        this.jobFilterCriteria = "";
    }

    /**
     * @return The employee job filter
     */
    public String getJobFilter() {
        return this.jobFilter;
    }

    /**
     * @param filterCat The employee job filter
     */
    public void setJobFilter(String filterCat) {
        this.jobFilter = filterCat;
    }

    /**
     * @return The employee job filter criteria
     */
    public String getJobFilterCriteria() {
        return this.jobFilterCriteria;
    }

    /**
     * @param criteria The employee job filter criteria
     */
    public void setJobFilterCriteria(String criteria) {
        this.jobFilterCriteria = criteria;
    }
}
