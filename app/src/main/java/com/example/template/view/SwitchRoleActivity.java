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
import com.example.template.model.Employee;
import com.example.template.model.Employer;
import com.example.template.model.User;
import com.example.template.util.FirebaseCRUD;

/**
 * @file SwitchRoleActivity.java
 * @author: -
 * @description: Activity that allows user to switch User roles (e.g. Employee, Employer).
 * SwitchRoleActivity provides a user interface where user can enter their related account
 * email address to change the their role.
 */
public class SwitchRoleActivity extends AppCompatActivity {
    private Button confirmEmail;
    private EditText emailBox;
    private TextView warningText;
    FirebaseCRUD crud = FirebaseCRUD.getInstance(this);

    /**
     * Initializes activity and sets user interface
     * @param savedInstanceState The saved instance state bundle
     */
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_role_switch);

        setContents();
        setEventsListener();
    }

    /**
     * Initializes each components listeners
     */
    private void setEventsListener() {
        confirmEmail.setOnClickListener(this::confirmEmailButtonClick);
    }

    /**
     * Initializes content components of the interface
     */
    private void setContents() {
        confirmEmail = findViewById(R.id.confirmEmailButton);
        emailBox = findViewById(R.id.enterEmailAddress);
        warningText = findViewById(R.id.roleSwitchWarningText);
    }

    /**
     * Handles the click event for the confirm email button and
     * email validation
     *
     * @param view The view that was clicked
     */
    private void confirmEmailButtonClick(View view) {
        String enteredEmail = emailBox.getText().toString().trim();
        User currUser = CurrentUser.getInstance().getUser();

        if(!enteredEmail.isEmpty()) {
            if (CredentialValidator.isValidEmail(enteredEmail)) {
                String storedEmail = currUser.getEmail();
                if (enteredEmail.equals(storedEmail)) {
                    String currRole = currUser.getRole();
                    String newRole = currRole.equals("Employer") ? "Employee" : "Employer";
                    CurrentUser.getInstance().setRole(newRole);


                    if(currRole.equals("Employer")){
                        Toast.makeText(this, "Switching role to Employee", Toast.LENGTH_LONG).show();
                        warningText.setText("");
                        move2Dashboard();
                    } else {
                        Toast.makeText(this, "Switching role to Employer", Toast.LENGTH_LONG).show();
                        warningText.setText("");
                        move2Dashboard();
                    }
                } else{
                    Toast.makeText(this, R.string.UNKNOWN_EMAIL, Toast.LENGTH_SHORT).show();
                    warningText.setText(R.string.UNKNOWN_EMAIL);
                }
            } else {
                Toast.makeText(this, R.string.EMAIL_ERROR, Toast.LENGTH_SHORT).show();
                warningText.setText(R.string.EMAIL_ERROR);
            }
        } else {
            Toast.makeText(this, R.string.NO_EMAIL, Toast.LENGTH_SHORT).show();
            warningText.setText(R.string.NO_EMAIL);
        }
    }

    /**
     * Navigate to the DashboardActivity.
     */
    private void move2Dashboard() {
        Intent intent = new Intent(SwitchRoleActivity.this, DashboardActivity.class);
        startActivity(intent);
    }

}
