package com.example.template.view;

import static java.lang.Integer.parseInt;

import android.content.Intent;
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
import com.example.template.model.CurrentUser;
import com.example.template.model.Job;
import com.example.template.model.User;
import com.google.firebase.Firebase;

public class AddJobActivity extends AppCompatActivity {
    ImageView backButton;
    Button addJobButton;
    EditText jobTitleInput;
    Spinner jobCategoryInput;
    EditText jobPayInput;
    EditText jobHoursInput;
    EditText jobDescription;
    EditText jobLocation;
    EditText jobTotalPay;
    User currUser = CurrentUser.getInstance().getUser();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_add_job);

        setContents();
        setEventListeners();

        FirebaseUseCase.set(this);
    }

    private void setEventListeners() {
        backButton.setOnClickListener(v -> move2ManageJobListingPage());
        addJobButton.setOnClickListener(v -> addJob2Database());
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

    private void addJob2Database() {
        Job newJob = new Job(String.valueOf(jobTitleInput.getText()),
                String.valueOf(jobDescription.getText()),
                String.valueOf(jobLocation.getText()),
                jobCategoryInput.getSelectedItem().toString(),
                currUser.getEmail(), String.valueOf(jobPayInput.getText()),
                String.valueOf(jobHoursInput.getText()),
                null,
                String.valueOf(R.string.PENDING_STATUS));
        FirebaseUseCase.addJob(newJob);

        //can use callback to handlee aysnchrnously but for now its fine
        move2ManageJobListingPage();
    }

    private void move2ManageJobListingPage() {
        Intent intent = new Intent(this, ManageJobListingActivity.class);
        startActivity(intent);
    }

    private void updateTotalPay() {
        if (!String.valueOf(jobPayInput.getText()).isEmpty() && !String.valueOf(jobHoursInput.getText()).isEmpty()){
            int hourlyRate = parseInt(String.valueOf(jobPayInput.getText()));
            int totalHours = parseInt(String.valueOf(jobHoursInput.getText()));
            jobTotalPay.setText(String.valueOf(hourlyRate*totalHours));
        }
    }

    private void setContents() {
        addJobButton = findViewById(R.id.addButtonAddJob);
        backButton = findViewById(R.id.backButtonImageViewAddJob);
        jobTitleInput = findViewById(R.id.jobTitleEditTextAddJob);
        jobCategoryInput = findViewById(R.id.jobCategorySpinnerAddJob);
        jobPayInput = findViewById(R.id.jobPayEditTextAddJob);
        jobHoursInput = findViewById(R.id.jobHoursEditTextAddJob);
        jobLocation = findViewById(R.id.jobLocationEditTextAddJob);
        jobDescription = findViewById(R.id.jobDescriptionEditTextAddJob);
        jobTotalPay = findViewById(R.id.jobPayTotalEditTextAddJob);

        initSpinner();
    }

    private void initSpinner() {
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.JOB_CATEGORIES, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        jobCategoryInput.setAdapter(adapter);
    }
}
