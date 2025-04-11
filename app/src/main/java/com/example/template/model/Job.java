package com.example.template.model;

/**
 *  @file Job.java
 *  @author: -
 *  @description: Represent a job in the system.
 *  The Job class holds information related to job.
 */

import java.io.Serializable;

public class Job implements Serializable {
    private String jobDesc = "";
    private String jobName = "";
    private String jobLocation = "";
    private String jobCategory = "";
    private String jobEmail = "";
    private String jobPay = "";
    private String jobHours = "";
    private String jobKey = "";
    private String jobStatus = "pending";

    public Job() {}

    /**
     * Constructs a new Job
     * @param jobName The job title
     * @param jobDesc The job description
     * @param jobLocation The job location
     * @param category The job category
     * @param jobEmail The job email
     * @param jobPay The job salary
     * @param jobHours The estimated job hours
     * @param jobKey The unique job key
     */
    public Job(String jobName, String jobDesc, String jobLocation, String category,
               String jobEmail, String jobPay, String jobHours, String jobKey, String jobStatus) {
        this.jobDesc = jobDesc;
        this.jobName = jobName;
        this.jobLocation = jobLocation;
        this.jobCategory = category;
        this.jobEmail = jobEmail;
        this.jobPay = jobPay;
        this.jobHours = jobHours;
        this.jobKey = jobKey;
        this.jobStatus = jobStatus;
    }

    /**
     * @return The job key
     */
    public String getJobKey() {return jobKey;}

    /**
     * @param jobKey The unique job key
     */
    public void setJobKey(String jobKey) {this.jobKey = jobKey;}

    /**
     * @return The job name
     */
    public String getJobName() {
        return jobName;
    }

    /**
     * @param jobName The job name
     */
    public void setJobName(String jobName) {
        this.jobName = jobName;
    }

    /**
     * @return The job description
     */
    public String getJobDescription() {
        return jobDesc;
    }

    /**
     * @param jobDesc The job desc
     */
    public void setJobDescription(String jobDesc) {
        this.jobDesc = jobDesc;
    }

    /**
     * @return The job category
     */
    public String getJobCategory() {return jobCategory;}

    /**
     * @param jobCategory Predefined category from list (Gardening, Cleaning, etc.)
     */
    public void setJobCategory(String jobCategory) {this.jobCategory = jobCategory;}

    /**
     * @return The job location
     */
    public String getJobLocation() {return jobLocation;}

    /**
     * @param jobLocation The job location
     */
    public void setJobLocation(String jobLocation) {this.jobLocation = jobLocation;}

    /**
     * @return The job email
     */
    public String getJobEmail() {return jobEmail;}

    /**
     * @param userEmail The job email
     */
    public void setJobEmail(String userEmail) {this.jobEmail = userEmail;}

    /**
     * @return The job salary
     */
    public String getJobPay() {return jobPay;}

    /**
     * @param userPayAmount The job salary
     */
    public void setJobPay(String userPayAmount) {this.jobPay = userPayAmount;}

    /**
     * @return Estimated hours required for job completion
     */
    public String getJobHours() {return jobHours;}

    /**
     * Sets estimated job duration
     * @param jobHours String representation of hours (e.g., "2")
     */
    public void setJobHours(String jobHours) {this.jobHours = jobHours;}

    /**
     * @return current job status
     */
    public String getJobStatus() {return jobStatus;}

    /**
     * Sets estimated job duration
     * @param jobStatus The job status
     */
    public void setJobStatus(String jobStatus) {this.jobStatus = jobStatus;}
}
