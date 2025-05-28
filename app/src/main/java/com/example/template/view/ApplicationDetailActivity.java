package com.example.template.view;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.template.Firebase.FirebaseUseCase;
import com.example.template.R;
import com.example.template.model.Application;
import com.example.template.model.Job;
import com.example.template.status.AcceptStatus;
import com.example.template.status.ConcreteStatus;
import com.example.template.status.DeclineStatus;
import com.example.template.status.Status;

public class ApplicationDetailActivity extends AppCompatActivity implements FirebaseUseCase.OnUpdateJobAppStatus{
    Application clickedApp;
    Job clickedJob;
    ImageView backButton;
    Button acceptButton, declineButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getAppIntent();
        getJobIntent();

        setContentView(R.layout.activity_app_details);

        setContents();
        setEventListeners();

        FirebaseUseCase.set(this);
    }

    private void getJobIntent() {
        clickedJob = (Job) getIntent().getSerializableExtra("job");
    }

    private void setEventListeners() {
        backButton.setOnClickListener(v -> move2ManageApp());
        acceptButton.setOnClickListener(v -> updateJobStatus(new AcceptStatus()));
        declineButton.setOnClickListener(v -> updateJobStatus(new DeclineStatus()));
    }

    private void updateJobStatus(Status s) {
        FirebaseUseCase.updateJobStatusAndApplication(s, this, clickedApp);
    }

    private void move2ManageApp() {
        Intent intent = new Intent(this, ManageApplicationActivity.class);
        startActivity(intent);
    }

    private void setContents() {
        backButton = findViewById(R.id.backButtonImageViewManageAppDetails);
        acceptButton = findViewById(R.id.acceptButtonAppDetails);
        declineButton = findViewById(R.id.declineButtonAppDetails);
    }

    private void getAppIntent() {
        clickedApp = (Application) getIntent().getSerializableExtra("appClicked");
    }

    @Override
    public void onComplete() {
        Intent intent = new Intent(this, ManageJobListingActivity.class);
        intent.putExtra("jobClicked", clickedJob);
        startActivity(intent);
    }
}
