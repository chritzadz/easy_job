package com.example.template.view;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.template.Firebase.FirebaseUseCase;
import com.example.template.R;
import com.example.template.controller.CredentialCheckUseCase;
import com.example.template.status.Status;
import com.example.template.status.SuccessStatus;

public class LoginActivity extends AppCompatActivity {
    private EditText emailEditText;
    private EditText passwordEditText;
    private Button loginButton;
    private TextView statusText;
    private TextView signUpLink;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        FirebaseUseCase.set(this);

        setContents();
        setEventListeners();
    }

    private void setEventListeners() {
        signUpLink.setOnClickListener( v -> move2SignUp());
        loginButton.setOnClickListener(v -> checkCredentials());
    }

    protected void move2SignUp(){
        Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
        startActivity(intent);
    }

    private void checkCredentials() {
        String email = emailEditText.getText().toString().trim(); //get entered email
        String password = passwordEditText.getText().toString().trim(); //get entered password

        Status status = CredentialCheckUseCase.validateSignUp(email, password);
        if(status.equals(new SuccessStatus())){
            status = FirebaseUseCase.checkUserExist(email, password);
            if (status.equals(new SuccessStatus())){
                move2DashboardActivity();
            }
            else{
                displayErrorMessage(status);
            }
        }
        else{
            displayErrorMessage(status);
        }
    }

    private void setContents() {
        emailEditText = findViewById(R.id.loginEditTextEmailAddress);
        passwordEditText = findViewById(R.id.logineditTextPassword);
        loginButton = findViewById(R.id.loginButton);
        statusText = findViewById(R.id.statusLabel);
        signUpLink = findViewById(R.id.textViewForSignUpLink);
    }

    protected void move2DashboardActivity(){
        Intent intent = new Intent(LoginActivity.this, DashboardActivity.class);
        startActivity(intent);
    }

    private void displayErrorMessage(Status status) {
        statusText.setText(status.message());
    }
}