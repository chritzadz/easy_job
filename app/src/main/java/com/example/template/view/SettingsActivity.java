package com.example.template.view;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;


import androidx.appcompat.app.AppCompatActivity;

import com.example.template.R;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;

/**
 * @file SettingsActivity.java
 * @author: -
 * @description: Activity that allows user to do system related methods, such as logging out,
 * reset password and switch role.
 */

public class SettingsActivity extends AppCompatActivity {
    private Button switchRoleButton;
    Button resetPassButton;
    Button logoutButton;

    /**
     * Initializes activity and set user interface
     * @param savedInstanceState The saved instance state bundle
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings_page);

        setContents();
        setEventListeners();
        showNavigation();
    }

    /**
     * Sets up the bottom navigation view.
     */
    private void showNavigation() {
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setSelectedItemId(R.id.nav_settings);
        bottomNavigationView.setOnItemSelectedListener(item -> {
            if (item.getItemId() == R.id.nav_settings) {
                //do nothing
                return true;
            } else if (item.getItemId() == R.id.nav_home) {
                move2DashboardPage();
                return true;
            } else if (item.getItemId() == R.id.nav_job){
                move2JobPage();
                return true;
            }
            return false;
        });
    }

    /**
     * Navigate to the JobActivity
     */
    private void move2JobPage() {
        Intent intent = new Intent(SettingsActivity.this, JobActivity.class);
        startActivity(intent);
    }

    /**
     * Navigate to the DashboardActivity
     */
    private void move2DashboardPage() {
        Intent intent = new Intent(SettingsActivity.this, DashboardActivity.class);
        startActivity(intent);
    }

    /**
     * Handles the click event for the switch role button.
     * @param view The view that was clicked.
     */
    private void onSwitchRoleClick(View view) {
        move2RoleSwitchPage();
    }

    /**
     * Handles the click event for the log out button.
     * @param view The view that was clicked.
     */
    private void onLogoutButtonClick(View view) {
        initiateLogout();
    }

    /**
     * Initializes each components listeners
     */
    private void setEventListeners() {
        resetPassButton.setOnClickListener(this::onResetPasswordClick);
        logoutButton.setOnClickListener(this::onLogoutButtonClick);
        switchRoleButton.setOnClickListener(this::onSwitchRoleClick);
    }

    /**
     * Navigate to the SwitchRoleActivity
     */
    protected void move2RoleSwitchPage() {
        Intent intent = new Intent(SettingsActivity.this, SwitchRoleActivity.class);
        startActivity(intent);        
    }

    /**
     * Handles the click event for the reset password button.
     * @param view The view that was clicked.
     */
    private void onResetPasswordClick(View view){
        move2ResetPasswordPage();
    }

    /**
     * Initializes content components of the interface
     */
    private void setContents(){
        resetPassButton = findViewById(R.id.ResetPasswordButton);
        logoutButton = findViewById(R.id.Logout); // ADDED: Retrieve the logout button by its ID
        switchRoleButton = findViewById(R.id.switchRoleButton);
    }

    /**
     * Navigates to the ResetPasswordActivity
     */
    private void move2ResetPasswordPage() {
        Intent intent = new Intent(this, ResetPasswordActivity.class);
        startActivity(intent);
    }

    /**
     * Logout confirmation and handles the logout process.
     */
    private void initiateLogout() {
        new AlertDialog.Builder(this)
                .setTitle("Confirm Logout")
                .setMessage("Are you sure you want to logout?")
                .setPositiveButton("Yes", (dialog, which) -> {
                    FirebaseAuth.getInstance().signOut();

                    Toast.makeText(SettingsActivity.this, "Logout successful", Toast.LENGTH_SHORT).show();

                    Intent intent = new Intent(SettingsActivity.this, LoginActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    finish();
                })
                .setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss())
                .create().show();
    }
}
