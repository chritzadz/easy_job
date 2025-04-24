package com.example.template.view;

import static java.lang.Integer.parseInt;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;

import com.example.template.Firebase.FirebaseUseCase;
import com.example.template.R;
import com.example.template.model.Job;
import com.google.firebase.Firebase;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;

public class EditJobActivity extends AppCompatActivity implements FirebaseUseCase.OnJobUpdateComplete {
    ImageView backButton;
    Button updateJobButton;
    EditText jobTitleInput;
    Spinner jobCategoryInput;
    EditText jobPayInput;
    EditText jobHoursInput;
    EditText jobDescription;
    EditText jobLocation;
    EditText jobTotalPay;
    Job job;
    List<String> categoryOption;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_edit_job);

        retrieveIntent();
        setContents();
        setEventListeners();

        FirebaseUseCase.set(this);
    }

    private void retrieveIntent() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            this.job = getIntent().getSerializableExtra("jobClicked", Job.class);
        }
        else{
            this.job = (Job) getIntent().getSerializableExtra("jobClicked");
        }
    }

    private void setEventListeners() {
        backButton.setOnClickListener(v -> move2ManageJobListingPage());
        updateJobButton.setOnClickListener(v -> updateJob2Database());
        jobPayInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                updateTotalPay();
            }
        });
        jobHoursInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                updateTotalPay();
            }
        });
    }

    private void updateJob2Database() {
        FirebaseUseCase.updateJob(
            job.getJobKey(),
            String.valueOf(jobTitleInput.getText()),
            String.valueOf(jobDescription.getText()),
            String.valueOf(jobLocation.getText()),
            jobCategoryInput.getSelectedItem().toString(),
            String.valueOf(jobHoursInput.getText()),
            String.valueOf(jobPayInput.getText()),
            this::move2ManageJobListingPage
        );
    }

    private void setContents() {
        categoryOption = Arrays.asList(getResources().getStringArray(R.array.JOB_CATEGORIES));
        updateJobButton = findViewById(R.id.updateButtonEditJob);
        backButton = findViewById(R.id.backButtonImageViewEditJob);
        jobTitleInput = findViewById(R.id.jobTitleEditTextEditJob);
        jobCategoryInput = findViewById(R.id.jobCategorySpinnerEditJob);
        jobPayInput = findViewById(R.id.jobPayEditTextEditJob);
        jobHoursInput = findViewById(R.id.jobHoursEditTextEditJob);
        jobLocation = findViewById(R.id.jobLocationEditTextEditJob);
        jobDescription = findViewById(R.id.jobDescriptionEditTextEditJob);
        jobTotalPay = findViewById(R.id.jobPayTotalEditTextEditJob);
        initSpinner();

        jobTitleInput.setText(job.getJobTitle());
        jobCategoryInput.setSelection(categoryOption.indexOf(job.getJobCategory()));
        jobLocation.setText(job.getJobLocation());
        jobPayInput.setText(job.getJobPay());
        jobDescription.setText(job.getJobDescription());
        jobHoursInput.setText(job.getJobHours());
    }

    private void initSpinner() {
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.JOB_CATEGORIES, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        jobCategoryInput.setAdapter(adapter);
    }

    private void updateTotalPay() {
        if (!String.valueOf(jobPayInput.getText()).isEmpty() && !String.valueOf(jobHoursInput.getText()).isEmpty()){
            int hourlyRate = parseInt(String.valueOf(jobPayInput.getText()));
            int totalHours = parseInt(String.valueOf(jobHoursInput.getText()));
            jobTotalPay.setText(String.valueOf(hourlyRate*totalHours));
        }
    }
    private void move2ManageJobListingPage() {
        Intent intent = new Intent(this, ManageJobListingActivity.class);
        startActivity(intent);
    }

    @Override
    public void onComplete() {
        move2ManageJobListingPage();
    }
}
