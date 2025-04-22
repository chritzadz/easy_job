package com.example.template.view;

import static java.lang.Integer.parseInt;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;

import com.example.template.Firebase.FirebaseUseCase;
import com.example.template.R;

public class AddJobActivity extends AppCompatActivity {
    ImageView backButton;
    EditText jobTitleInput;
    Spinner jobCategoryInput;
    EditText jobPayInput;
    EditText jobHoursInput;
    EditText jobDescription;
    EditText jobLocation;
    EditText jobTotalPay;


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
        backButton = findViewById(R.id.backButtonImageViewAddJob);
        jobTitleInput = findViewById(R.id.jobTitleEditTextAddJob);
        jobCategoryInput = findViewById(R.id.jobCategorySpinnerAddJob);
        jobPayInput = findViewById(R.id.jobPayEditTextAddJob);
        jobHoursInput = findViewById(R.id.jobHoursEditTextAddJob);
        jobLocation = findViewById(R.id.jobLocationEditTextAddJob);
        jobDescription = findViewById(R.id.jobDescriptionEditTextAddJob);
        jobTotalPay = findViewById(R.id.jobPayTotalEditTextAddJob);
    }
}
