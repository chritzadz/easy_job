package com.example.template.view;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.template.Firebase.FirebaseUseCase;
import com.example.template.R;
import com.example.template.model.CurrentUser;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class DashboardActivity extends AppCompatActivity {
    private TextView welcomeLabel;
    private final CurrentUser currUser = CurrentUser.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        FirebaseUseCase.set(this);

        setContents();
        setEventListeners();
    }

    private void setEventListeners() {

    }

    private void setContents() {
        welcomeLabel = findViewById(R.id.welcomeTextViewDashboard);


        updateWelcomeLabel();
        showNavigation();
    }

    private void updateWelcomeLabel() {
        String message = welcomeLabel.getText() + currUser.getUser().getFirstName();
        welcomeLabel.setText(message);
    }

    private void showNavigation() {
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setSelectedItemId(R.id.nav_home);
        bottomNavigationView.setOnItemSelectedListener(item -> {
            if (item.getItemId() == R.id.nav_settings) {
                move2SettingsPage();
                return true;
            } else if (item.getItemId() == R.id.nav_home) {
                return true;
            } else if (item.getItemId() == R.id.nav_job) {
                return true;
            }
            return false;
        });
    }

    private void move2JobPage() {
    }

    private void move2AllAppsPage() {
    }

    private void move2SettingsPage() {
    }
}
