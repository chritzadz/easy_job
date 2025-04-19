package com.example.template.view;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.template.Firebase.FirebaseUseCase;
import com.example.template.R;
import com.example.template.model.CurrentUser;
import com.example.template.model.User;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class DashboardActivity extends AppCompatActivity {
    private TextView welcomeLabel;
    private final User currUser = CurrentUser.getInstance().getUser();

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

        showRolePage();
        updateWelcomeLabel();
        showNavigation();
    }

    private void showRolePage() {
        if(currUser.getRole().equals("Employer")) {
            showEmployerPage();
        } else if (currUser.getRole().equals("Employee")) {
            showEmployeePage();
        }
    }

    private void showEmployerPage() {
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        EmployerDashboardFragment employerFragment = new EmployerDashboardFragment();
        transaction.replace(R.id.fragmentFragmentContainerDashboard, employerFragment);
        transaction.commit();
    }

    private void showEmployeePage() {
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        EmployeeDashboardFragment employeeFragment = new EmployeeDashboardFragment();
        transaction.replace(R.id.fragmentFragmentContainerDashboard, employeeFragment);
        transaction.commit();
    }

    private void updateWelcomeLabel() {
        String message = welcomeLabel.getText() + currUser.getFirstName();
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
