package com.example.template.view;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


import androidx.appcompat.app.AppCompatActivity;

import com.example.template.R;
import com.example.template.controller.CredentialValidator;
import com.example.template.model.CurrentUser;
import com.example.template.model.User;

/**
 * @file ResetPasswordActivity.java
 * @author: -
 * @description: Activity that prompts user for the email.
 * After successful verification, activity will move to reset password form.
 */

public class ResetPasswordActivity extends AppCompatActivity{
    private Button validationButton;
    private EditText emailEditText;
    private TextView warningText;

    /**
     * Initializes activity and set user interface
     * @param savedInstanceState The saved instance state bundle
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);

        setContents();
        validationButton.setOnClickListener(this::validationButtonClick);
    }

    /**
     * Handles the click event for email validation.
     * @param view The view that was clicked
     */
    private void validationButtonClick(View view) {
        String inputEmail = getEmail();
        User currUser = CurrentUser.getInstance().getUser();

        if (!inputEmail.isEmpty()){
            if (CredentialValidator.isValidEmail(inputEmail)){
                if(inputEmail.equals(currUser.getEmail())){
                    Toast.makeText(this, R.string.EMAIL_EXIST, Toast.LENGTH_LONG).show();
                    move2ResetPasswordFormPage();
                }
                else{
                    Toast.makeText(this, R.string.UNKNOWN_EMAIL, Toast.LENGTH_LONG).show();
                    warningText.setText(R.string.UNKNOWN_EMAIL);
                }
            }
            else{
                Toast.makeText(this, R.string.EMAIL_ERROR, Toast.LENGTH_LONG).show();
                warningText.setText(R.string.EMAIL_ERROR);
            }
        }
        else{
            Toast.makeText(this, R.string.NO_EMAIL, Toast.LENGTH_LONG).show();
            warningText.setText(R.string.NO_EMAIL);
        }
    }

    /**
     * Initializes content components of the interface
     */
    private void setContents(){
        validationButton = findViewById(R.id.validationButton);
        emailEditText = findViewById(R.id.editEmailAddressText);
        warningText = findViewById(R.id.warningTextBox);
    }

    /**
     * Navigate to the ResetPasswordFormActivity
     */
    private void move2ResetPasswordFormPage() {
        Intent intent = new Intent(ResetPasswordActivity.this, ResetPasswordFormActivity.class);
        startActivity(intent);
    }

    /**
     * @return email input by user
     */
    private String getEmail() {
        return emailEditText.getText().toString().trim();
    }
}
