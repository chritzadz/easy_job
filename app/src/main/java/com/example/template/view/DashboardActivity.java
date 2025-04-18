package com.example.template.view;

import android.content.Intent;
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
        bottomNavigationView.setPadding(0,0,0,0);
        bottomNavigationView.setSelectedItemId(R.id.nav_home);
        bottomNavigationView.setOnItemSelectedListener(item -> {
            if (item.getItemId() == R.id.nav_settings) {
                move2ProfilePage();
                return true;
            } else if (item.getItemId() == R.id.nav_home) {
                return true;
            } else if (item.getItemId() == R.id.nav_job) {
                move2JobPage();
                return true;
            }
            return false;
        });
    }

    private void move2JobPage() {
        Intent intent = new Intent(this, JobActivity.class);
        startActivity(intent);
    }

    private void move2ProfilePage() {
        Intent intent = new Intent(this, ProfileActivity.class);
        startActivity(intent);
    }
}
