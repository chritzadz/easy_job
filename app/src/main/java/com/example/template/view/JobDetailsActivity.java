package com.example.template.view;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.template.R;
import com.example.template.model.CurrentUser;
import com.example.template.model.Job;
import com.example.template.model.User;

import androidx.test.espresso.IdlingRegistry;
import androidx.test.espresso.idling.CountingIdlingResource;

/**
 * @file JobDetailsActivity.java
 * @author: -
 * @description: Activity that will handle in showing job details to potential employee.
 * two important method is applied here which is to navigate location and to apply for the job.
 */
public class JobDetailsActivity extends AppCompatActivity {
    private TextView jobTitle;
    private TextView jobCategory;
    private TextView jobHours;
    private TextView jobLocation;
    private TextView jobDescription;
    private Button applyButton;
    private Button backButton;
    private Button locationButton;
    private Job job;

    /**
     * Initializes activity and set user interface
     * @param savedInstanceState The saved instance state bundle
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_job_details);

        initializeViews();
        setEventListeners();
        getJobIntent();
        showDetails();
    }

    /**
     * Handle in showing job details
     */
    private void showDetails() {
        if (job != null) {
            Log.d("JobDetailsActivity", "Job received: " + job.getJobName());
            displayJobInformation();
        } else {
            Log.e("JobDetailsActivity", "Job object is null!");
        }
    }

    /**
     * Retrieve job object passed from intent of previous page
     */
    private void getJobIntent() {
        Intent intent = getIntent();
        job = (Job) intent.getSerializableExtra("jobObj");
    }

    /**
     * Initializes content components of the interface
     */
    private void initializeViews(){
        jobTitle = findViewById(R.id.jobDetailJobTitle);
        jobCategory = findViewById(R.id.jobDetailCategory);
        jobHours = findViewById(R.id.jobDetailHours);
        jobLocation = findViewById(R.id.jobDetailLocation);
        jobDescription = findViewById(R.id.jobDetailDescription);
        applyButton = findViewById(R.id.jobDetailApplyButton);
        backButton = findViewById(R.id.jobDetailBackButton);
        locationButton = findViewById(R.id.jobDetailLocationButton);
    }

    /**
     * Initializes each components listeners
     */
    private void setEventListeners() {
        applyButton.setOnClickListener(this::move2JobApplicationPage);
        backButton.setOnClickListener(this::move2DashboardPage);
        locationButton.setOnClickListener(this::onLocationButtonClick);
    }

    /**
     * Handles the click event for the location button, moving to MapActivity with jobObj as Intent
     * @param view The view that was clicked.
     */
    private void onLocationButtonClick(View view) {
        Intent intent = new Intent(JobDetailsActivity.this, MapActivity.class);
        intent.putExtra("jobObj", job);
        intent.putExtra("from", "jobDetails");
        startActivity(intent);
    }

    /**
     * Navigate to the JobApplicationActivity
     * @param view The view that was clicked.
     */
    private void move2JobApplicationPage(View view){
        Intent intent = new Intent(JobDetailsActivity.this, JobApplicationActivity.class);
        intent.putExtra("jobObj", job);
        startActivity(intent);
    }

    /**
     * Navigate to the DashboardActivity
     * @param view The view that was clicked.
     */
    private void move2DashboardPage(View view){
        User curUser = CurrentUser.getInstance().getUser();

        Intent intent = new Intent(JobDetailsActivity.this, DashboardActivity.class);
        intent.putExtra("userObj", curUser);
        startActivity(intent);
    }

    /**
     * Display job information in the interface
     */
    public void displayJobInformation(){
        jobTitle.setText(job.getJobName());
        jobCategory.setText(String.format("Category: %s", job.getJobCategory()));
        jobHours.setText(String.format("Hours: %s", job.getJobHours()));
        jobLocation.setText(job.getJobLocation());
        jobDescription.setText(job.getJobDescription());
    }
}