package com.example.template.view;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.example.template.Firebase.FirebaseUseCase;
import com.example.template.R;

public class JobActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_job);


        setContents();
        setEventListeners();

        FirebaseUseCase.set(this);
    }
}
