package com.example.template.view;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.template.R;
import com.example.template.model.CurrentUser;
import com.example.template.model.Job;
import com.example.template.util.FirebaseCRUD;

public class AppliedJobDetailsActivity extends AppCompatActivity {
    FirebaseCRUD crud = FirebaseCRUD.getInstance(this);
    Job job;

    private TextView jobTitle;
    private TextView jobStatus;
    private TextView jobHours;
    private TextView jobPay;
    private TextView jobLocation;
    private TextView jobDescription;
    private Button backButton;
    private Button completeButton;
    private Button approveButton;

    /**
     * Initializes activity and set user interface
     * @param savedInstanceState The saved instance state bundle
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_applied_job_details);
        initializeViews();
        initializeListener();

        getJobIntent();

        showDetails();

        displayButtonAccCondition();
    }

    private void showDetails() {
        jobTitle.setText(job.getJobName());
        jobStatus.setText(String.format("Status: %s", job.getJobStatus()));
        jobHours.setText(String.format("Hours: %s", job.getJobHours()));
        jobPay.setText(String.format("$%s", job.getJobPay()));
        jobLocation.setText(job.getJobLocation());
        jobDescription.setText(job.getJobDescription());
    }

    private void getJobIntent() {
        Intent intent = getIntent();
        job = (Job) intent.getSerializableExtra("jobObj");
    }

    private void initializeViews() {
        jobTitle = findViewById(R.id.appliedJobDetailJobTitle);
        jobStatus = findViewById(R.id.appliedJobDetailStatus);
        jobHours = findViewById(R.id.appliedJobDetailHours);
        jobPay = findViewById(R.id.appliedJobDetailSalary);
        jobLocation = findViewById(R.id.appliedJobDetailLocation);
        jobDescription = findViewById(R.id.appliedJobDetailDescription);
        backButton = findViewById(R.id.appliedJobDetailBackButton);
        completeButton = findViewById(R.id.appliedJobDetailsCompleteButton);
        approveButton = findViewById(R.id.appliedJobDetailApproveButton);
    }

    private void move2AppJobListActivity() {
        Intent intent = new Intent(this, AppliedJobListActivity.class);
        startActivity(intent);
    }

    /** Initializes the views used in the activity. */
    private void initializeListener() {
        if (CurrentUser.getInstance().getUser().getRole().equals("Employee")){
            backButton.setOnClickListener(v -> move2AppJobListActivity());
        }
        else{
            backButton.setOnClickListener(v -> move2Dashboard());
        }
    }

    private void move2Dashboard() {
        Intent intent = new Intent(this, DashboardActivity.class);
        startActivity(intent);
    }

    private void displayButtonAccCondition() {
        String role;
        CurrentUser currentUser = CurrentUser.getInstance();
        if (currentUser.getUser() != null) {
            role = currentUser.getUser().getRole();
        } else {
            role = "";
        }

        String jobKey = job.getJobKey();

        crud.getStatusByJob(jobKey)
                .addOnSuccessListener(status -> {
                    if (status != null) {

                        if (role.equals("Employee") && status.equals("To do") ) {
                            displayCompleteButton(jobKey);

                        }
                        else if (role.equals("Employer") && status.equals("Completed")){
                            displayApproveButton();
                        }
                    }
                })
                .addOnFailureListener(e -> {
                    System.out.println("Error retrieving status: " + e.getMessage());
                });
    }

    private void displayCompleteButton(String jobKey){
        completeButton.setVisibility(View.VISIBLE);
        completeButton.setEnabled(true);
        completeButton.setOnClickListener(v -> {
            crud.updateJobStatus(jobKey, "Completed");

            move2AppJobListActivity();
        });
    }

    private void displayApproveButton(){
        approveButton.setVisibility(View.VISIBLE);
        approveButton.setEnabled(true);
        approveButton.setOnClickListener(v -> move2PaymentActivity());
    }

    private void move2PaymentActivity(){
        Intent intent = new Intent(this, PaymentEmployerActivity.class);
        intent.putExtra("jobObj", job);
        startActivity(intent);
    }
}
