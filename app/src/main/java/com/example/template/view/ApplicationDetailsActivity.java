package com.example.template.view;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.template.R;
import com.example.template.model.Application;
import com.example.template.model.Job;
import com.example.template.util.FirebaseCRUD;

/**
 * @file ApplicationDetailsActivity.java
 * @author: -
 * @description: Activity that will handle in showing job details to potential employee.
 * one method include an acceptance and decline button to process the application.
 */

public class ApplicationDetailsActivity extends AppCompatActivity {
    private TextView coverLetter;
    private TextView employeeEmail;
    private Application clickedApp;
    private Job correspondingJob;
    private Button acceptButton;
    private Button declineButton;
    private String applicationId;
    private String prevPage;
    private Button backButton;
    FirebaseCRUD crud = FirebaseCRUD.getInstance();

    /**
     * Initializes activity and set user interface
     * @param savedInstanceState The saved instance state bundle
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_application_details);
        getPrevIntent();
        initializeViews();
        initializeListener();

        if (clickedApp != null) {
            displayAppInformation();
            updateButtonStates(); // Added
        }
    }

    private void updateButtonStates() {
        if (clickedApp.isDecided()) {
            acceptButton.setEnabled(false);
            declineButton.setEnabled(false);
            acceptButton.setAlpha(0.5f);
            declineButton.setAlpha(0.5f);
        }
    }

    private void initializeListener() {
        backButton.setOnClickListener(v -> move2AppListActivity());
        acceptButton.setOnClickListener(v -> acceptApplication());
        declineButton.setOnClickListener(v -> declineApplication());
    }

    private void move2AppListActivity() {
        if (!prevPage.equals(null) && prevPage.equals("EmployerApplicationListActivity")){
            Intent intent = new Intent(this, EmployerApplicationListActivity.class);
            startActivity(intent);
        } else {
            Intent intent = new Intent(this, ApplicationListActivity.class);
            intent.putExtra("jobObj", correspondingJob);
            startActivity(intent);
        }
    }

    /**
     * Retrieve application object passed from intent of previous page
     */
    private void getPrevIntent() {
        Intent intent = getIntent();
        clickedApp = (Application) intent.getSerializableExtra("appObj");
        correspondingJob = (Job) intent.getSerializableExtra("corrJob");
        applicationId = intent.getStringExtra("appId");
        prevPage = intent.getStringExtra("prevPage");
    }

    /**
     * Initializes the views used in the activity.
     */
    private void initializeViews() {
        coverLetter = findViewById(R.id.coverLetterText);
        employeeEmail = findViewById(R.id.employeeEmailText);
        acceptButton = findViewById(R.id.acceptButton);
        declineButton = findViewById(R.id.declineButton);
        backButton = findViewById(R.id.backButtonAppDetails);

        if(!clickedApp.getStatus().equals("pending")){
            // Disable buttons to prevent duplicate actions
            acceptButton.setEnabled(false);
            declineButton.setEnabled(false);
            acceptButton.setAlpha(0.5f);
            declineButton.setAlpha(0.5f);

        }
    }

    /**
     * Handles the logic for accepting an application.
     * Updates status in Firebase and disables buttons to prevent duplicate actions
     */
    private void acceptApplication() {
        if (clickedApp != null && !clickedApp.isDecided()) {
            // Update application status
            crud.updateApplicationStatus(applicationId, "accepted");
            clickedApp.setStatus("accepted");

            // Update corresponding job status
            updateJobStatus(clickedApp, "accepted");

            // Disable buttons to prevent duplicate actions
            acceptButton.setEnabled(false);
            declineButton.setEnabled(false);
            acceptButton.setAlpha(0.5f);
            declineButton.setAlpha(0.5f);

            Toast.makeText(this, "Application Accepted", Toast.LENGTH_SHORT).show();
        }
    }

    private void updateJobStatus(Application clickedApp, String status) {
        if (status.equals("accepted")){
            crud.getJobByID(clickedApp.getJobKey()).addOnSuccessListener(job -> {
                crud.updateJobStatus(job.getJobKey(), "To do");
            });
        }
    }

    /**
     * Handles the logic for declining an application.
     * Updates status in Firebase and disables buttons to prevent duplicate actions
     */
    private void declineApplication() {
        if (clickedApp != null && !clickedApp.isDecided()) {
            // Update application status
            crud.updateApplicationStatus(applicationId, "declined");
            clickedApp.setStatus("declined");

            // Update corresponding job status
            updateJobStatus(clickedApp, "declined");

            // Disable buttons to prevent duplicate actions
            acceptButton.setEnabled(false);
            declineButton.setEnabled(false);
            acceptButton.setAlpha(0.5f);
            declineButton.setAlpha(0.5f);

            Toast.makeText(this, "Application Declined", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Displays the application details (cover letter, email, and status).
     */
    public void displayAppInformation() {
        crud.findUserByEmail(clickedApp.getEmployeeEmail()).addOnSuccessListener(user -> {
            if (user != null) {
                coverLetter.setText(clickedApp.getCoverLetter());
                employeeEmail.setText(user.getEmail() + "\nStatus: " + clickedApp.getStatus());
            } else {
                employeeEmail.setText("User not found");
            }
        }).addOnFailureListener(e -> Log.e("ApplicationDetailsActivity", "Failed to fetch user details", e));
    }
}