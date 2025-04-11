package com.example.template.view;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.template.controller.CredentialValidator;

import androidx.appcompat.app.AppCompatActivity;

import com.example.template.R;
import com.example.template.model.CurrentUser;
import com.example.template.model.User;
import com.example.template.util.FirebaseCRUD;

/**
 * @file ResetPasswordFormActivity.java
 * @author: -
 * @description: Activity that prompts user for a new password, and will handle the changes.
 * After successful changes, activity will move to log in process.
 */

public class ResetPasswordFormActivity extends AppCompatActivity{
    private Button resetButton;
    private EditText passEditText;
    private EditText confirmPassEditText;
    private TextView warningText;
    FirebaseCRUD crud = FirebaseCRUD.getInstance();

    /**
     * Initializes activity and set user interface
     * @param savedInstanceState The saved instance state bundle
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password_form);

        setContents();
        setEventsListeners();

    }

    /**
     * Initializes each components listeners
     */
    private void setEventsListeners() {
        resetButton.setOnClickListener(this::validationButtonClick);
    }

    /**
     * Handles the click event for reset button
     * @param view The view that was clicked
     */
    private void validationButtonClick(View view) {
        User currUser = CurrentUser.getInstance().getUser();
        if (CredentialValidator.isValidPassword(getPass())){
            String confirmPass = getConfirmPass();
            if (getPass().equals(confirmPass)){
                changePassword(currUser.getEmail(), confirmPass);
                Toast.makeText(this, R.string.SUCCESS, Toast.LENGTH_SHORT).show();
                move2LogInPage();
            }
            else{
                Toast.makeText(this, R.string.PASSWORD_NOT_MATCH, Toast.LENGTH_SHORT).show();
                warningText.setText(R.string.PASSWORD_NOT_MATCH);
            }
        }
        else{
            Toast.makeText(this, R.string.PASSWORD_ERROR, Toast.LENGTH_SHORT).show();
            warningText.setText(R.string.PASSWORD_ERROR);
        }
    }

    /**
     * Change password in database-level. Pass to firebase instance.
     * @param email The user email
     * @param newPass The new password of the user
     */
    private void changePassword(String email, String newPass) {
        crud.changePassword(email, newPass);
    }

    /**
     * Initializes content components of the interface
     */
    private void setContents(){
        resetButton = findViewById(R.id.validationButton);
        passEditText = findViewById(R.id.editPasswordText);
        confirmPassEditText = findViewById(R.id.editPasswordText2);
        warningText = findViewById(R.id.warningTextBox);
    }

    /**
     * Navigate to the LoginActivity
     */
    private void move2LogInPage() {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }

    /**
     * @return new password input by user
     */
    private String getPass() {
        return passEditText.getText().toString().trim();
    }

    /**
     * @return confirmation new password input by user
     */
    private String getConfirmPass() {
        return confirmPassEditText.getText().toString().trim();
    }

    /**
     * initialize FirebaseCRUD (mockito purposes)
     */
    public void setCrud(FirebaseCRUD mockFirebase) {
        crud = mockFirebase;
    }

}
