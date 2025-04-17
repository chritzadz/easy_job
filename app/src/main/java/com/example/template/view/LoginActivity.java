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

public class LoginActivity extends AppCompatActivity {
    FirebaseCRUD crud = FirebaseCRUD.getInstance(this);
    private EditText emailEditText;
    private EditText passwordEditText;
    private Button loginButton;
    private TextView statusMessage;
    private TextView signUpLink;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

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

    protected void move2DashboardActivity(){
        Intent intent = new Intent(LoginActivity.this, DashboardActivity.class);
        startActivity(intent);
    }

    public void setCrud(FirebaseCRUD mockFirebase) {
        crud = mockFirebase;
    }
}