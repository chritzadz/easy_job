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
import com.example.template.model.CurrentUser;
import com.example.template.model.Employee;
import com.example.template.util.FirebaseCRUD;

/**
 * @file LoginActivity.java
 * @author: -
 * @description: Activity that prompts user for existing email and it's correlated password.
 * If successful, user will be redirected to dashboard
 */

public class LoginActivity extends AppCompatActivity {
    private EditText emailEditText;
    private EditText passwordEditText;
    private Button loginButton;
    private TextView statusMessage;
    private TextView signUpLink;
    FirebaseCRUD crud = FirebaseCRUD.getInstance();

    /**
     * Initializes activity and set user interface
     * @param savedInstanceState The saved instance state bundle
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        setContents();
        setEventListeners();
    }

    /**
     * Initializes each components listeners
     */
    private void setEventListeners() {
        signUpLink.setOnClickListener(this::move2SignUp);
        loginButton.setOnClickListener(this::onLoginButtonClick);
    }

    /**
     * Navigate to the RegisterActivity
     */
    protected void move2SignUp(View view){
        Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
        startActivity(intent);
    }

    /**
     * Handles the click event for login button
     * @param view The view that was clicked
     */
    private void onLoginButtonClick(View view) {
        Log.d("Button", "is CLICKED");
        String email = emailEditText.getText().toString().trim();
        String password = passwordEditText.getText().toString().trim();

        crud.findUserByEmail(email).addOnSuccessListener(findUser -> {
            if (findUser != null) {
                if (findUser.getPassword().equals(password)) {
                    Toast.makeText(LoginActivity.this, "Login Successful", Toast.LENGTH_LONG).show();
                    Log.d("User class Employee", String.valueOf(findUser instanceof Employee));
                    CurrentUser.getInstance().setUser(findUser);
                    move2DashboardActivity();
                } else {
                    Toast.makeText(LoginActivity.this, "Incorrect password", Toast.LENGTH_LONG).show();
                    setStatusMessage(getString(R.string.INCORRECT_PASSWORD));
                }
            } else {
                Toast.makeText(LoginActivity.this, "Email not found", Toast.LENGTH_LONG).show();
                setStatusMessage(getString(R.string.UNKNOWN_EMAIL));
            }
        }).addOnFailureListener(e -> {

        });
    }

    /**
     * Initializes content components of the interface
     */
    private void setContents() {
        emailEditText = findViewById(R.id.loginEditTextEmailAddress);
        passwordEditText = findViewById(R.id.logineditTextPassword);
        loginButton = findViewById(R.id.loginButton);
        statusMessage = findViewById(R.id.statusLabel);
        signUpLink = findViewById(R.id.textViewForSignUpLink);
    }


    protected void setStatusMessage(String message) {
        statusMessage.setText(message.trim());
    }

    /**
     * Navigate to the DashboardActivity
     */
    protected void move2DashboardActivity(){
        Intent intent = new Intent(LoginActivity.this, DashboardActivity.class);
        startActivity(intent);
    }

    /**
     * initialize FirebaseCRUD (mockito purposes)
     */
    public void setCrud(FirebaseCRUD mockFirebase) {
        crud = mockFirebase;
    }
}