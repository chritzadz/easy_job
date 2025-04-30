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
    private String status;
    private String appKey;
    private String jobKey;

    public Application() {}

    /**
     * Constructs a new Application
     * @param employeeEmail The email address of the employee.
     * @param employerEmail The email address of the employer.
     */
    public Application(String employeeEmail, String employerEmail, String jobKey){
        this.employerEmail = employerEmail;
        this.employeeEmail = employeeEmail;
        this.status = "pending";
        this.jobKey = jobKey;
    }

    public String getAppKey() {
        return appKey;
    }

    public void setAppKey(String appKey) {
        this.appKey = appKey;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getEmployeeEmail(){
        return employeeEmail;
    }

    public void setEmployeeEmail(String employeeEmail){
        this.employeeEmail = employeeEmail;
    }

    public String getEmployerEmail(){
        return employerEmail;
    }

    public void setEmployerEmail(String employerEmail){
        this.employerEmail = employerEmail;
    }

    public String getJobKey() {return jobKey;}
    public void setJobKey(String jobKey) {this.jobKey = jobKey;}

}


