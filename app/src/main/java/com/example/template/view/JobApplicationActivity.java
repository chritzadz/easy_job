package com.example.template.view;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.template.R;
import com.example.template.model.Application;
import com.example.template.model.CurrentUser;
import com.example.template.model.Employee;
import com.example.template.model.Job;
import com.example.template.model.User;
import com.example.template.util.FirebaseCRUD;
import com.google.firebase.database.FirebaseDatabase;

/**
 * @file JobApplicationActivity.java
 * @author: -
 * @description: Activity that prompt user a cover letter to be sent for the corresponding job.
 * A submit button click will handle in sending the application to the corresponding employer.
 */

public class JobApplicationActivity extends AppCompatActivity {

    private TextView jobTitle;
    private EditText coverLetter;
    private Button submitButton;
    private Job job;
    private User user;
    FirebaseCRUD crud = FirebaseCRUD.getInstance(this);

    /**
     * Initializes activity and set user interface
     * @param savedInstanceState The saved instance state bundle
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_job_application);
        user = CurrentUser.getInstance().getUser();

        initializeViews();
        setEventListeners();

        Intent intent = getIntent();
        job = (Job) intent.getSerializableExtra("jobObj");
        if (job != null) {
            Log.d("JobApplicationActivity", "Job received: " + job.getJobName());
            jobTitle.setText(job.getJobName());
        } else {
            Log.e("JobApplicationActivity", "Job object is null!");
        }
    }

    /**
     * Initializes content components of the interface
     */
    private void initializeViews(){
        jobTitle = findViewById(R.id.jobApplicationJobTitle);
        coverLetter = findViewById(R.id.jobApplicationCoverLetterText);
        submitButton = findViewById(R.id.jobApplicationSubmitButton);
    }

    /**
     * Initializes each components listeners
     */
    private void setEventListeners() {
        submitButton.setOnClickListener(this::onSubmitButtonClick);
    }

    /**
     * Handles the click event for the submit button
     * @param view The view that was clicked.
     */
    private void onSubmitButtonClick(View view) {
        runOnUiThread(() -> {
            Log.d("JobApplicationActivity", "Submit button clicked");
            String coverLetterText = coverLetter.getText().toString().trim();

            if (coverLetterText.isEmpty()) {
                Toast.makeText(this, "Cover letter is empty", Toast.LENGTH_LONG).show();
            } else {
                saveApplication(user, coverLetterText);
                Toast.makeText(this, "Application Successfully Sent", Toast.LENGTH_LONG).show();
                move2DashboardPage();
            }
        });
    }

    /**
     * Handle in passing data database, pass task to firebaseCRUD to add application
     * @param user The user that applied for the job
     * @param coverLetterText The cover letter of the applicant
     */
    private void saveApplication(User user, String coverLetterText) {
        Application application = new Application(user.getEmail(), job.getJobEmail(), coverLetterText, job.getJobKey());
        crud.addApplication(application);
    }

    /**
     * Navigate to the DashboardActivity
     */
    private void move2DashboardPage(){
        Intent intent = new Intent(JobApplicationActivity.this, DashboardActivity.class);
        CurrentUser currUser = CurrentUser.getInstance();
        User userObj = currUser.getUser();
        intent.putExtra("userObj", userObj);
        startActivity(intent);
    }
}
