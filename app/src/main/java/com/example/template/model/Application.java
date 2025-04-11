/**
 *  @file Application.java
 *  @author:
 *  @description: Represent a job application in the system.
 *  The Application class holds information related to job application
 */

package com.example.template.model;

import java.io.Serializable;

public class Application implements Serializable {
    private String employeeEmail;
    private String employerEmail;
    private String coverLetter;
    private String status;
    private String key;
    private String jobKey;
    private boolean isDecided = false; // New field


    public Application() {}

    /**
     * Constructs a new Application
     * @param employeeEmail The email address of the employee.
     * @param employerEmail The email address of the employer.
     * @param coverLetter The cover letter of applicant.
     */
    public Application(String employeeEmail, String employerEmail, String coverLetter, String jobKey){
        this.employerEmail = employerEmail;
        this.employeeEmail = employeeEmail;
        this.coverLetter = coverLetter;
        this.status = "pending";
        this.jobKey = jobKey;
    }

    /**
     * @return The application key
     */
    public String getKey() {
        return key;
    }

    /**
     * @param key The application key
     */
    public void setKey(String key) {
        this.key = key;
    }

    /**
     * @return The application status
     */
    public String getStatus() {
        return status;
    }

    /**
     * @param status The application status
     */
    public void setStatus(String status) {
        this.status = status;
    }

    /**
     * @return The application employee email
     */
    public String getEmployeeEmail(){
        return employeeEmail;
    }

    /**
     * @param employeeEmail The application employee email
     */
    public void setEmployeeEmail(String employeeEmail){
        this.employeeEmail = employeeEmail;
    }

    /**
     * @return The application employer email
     */
    public String getEmployerEmail(){
        return employerEmail;
    }

    /**
     * @param employerEmail The application employer email
     */
    public void setEmployerEmail(String employerEmail){
        this.employerEmail = employerEmail;
    }

    /**
     * @return The applicant cover letter
     */
    public String getCoverLetter(){ return coverLetter; }

    /**
     * @param letter The applicant cover letter
     */
    public void setCoverLetter(String letter){
        this.coverLetter = letter;
    }

    public String getJobKey() {return jobKey;}
    public void setJobKey(String jobKey) {this.jobKey = jobKey;}

    public boolean isDecided() {
        return isDecided;
    }

    public void setDecided(boolean decided) {
        isDecided = decided;
    }


}


