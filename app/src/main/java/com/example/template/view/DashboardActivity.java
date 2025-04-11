package com.example.template.view;

import static androidx.core.content.ContentProviderCompat.requireContext;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.template.Adapter.JobAdapter;
import com.example.template.model.CurrentUser;
import com.example.template.model.Employee;
import com.example.template.model.Employer;
import com.example.template.model.Job;
import com.example.template.model.User;
import com.example.template.util.FirebaseCRUD;
import com.example.template.util.LocationHelper;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import com.example.template.R;
import com.google.firebase.database.FirebaseDatabase;

import org.junit.Before;

import java.util.ArrayList;
import java.util.Objects;

/**
 * @file DashboardActivity.java
 * @author: -
 * @description: Activity that serves as a template for custom fragment determine by the User role.
 */

public class DashboardActivity extends AppCompatActivity implements LocationHelper.LocationCallback {
    private TextView welcome_message;
    private String city;
    private CurrentUser currUser = CurrentUser.getInstance();
    TextView locationBox;

    private final ActivityResultLauncher<String> requestPermissionLauncher =
            registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
                if (isGranted) {
                    getCity();
                }
            });

    /**
     * Initializes activity and set user interface
     * @param savedInstanceState The saved instance state bundle
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        initializeContent();
        showNavigation();
        showRolePage();
    }

    /**
     * Initializes content components of the interface
     */
    private void initializeContent() {
        welcome_message= findViewById(R.id.welcomeTextView);
        Log.d("username", currUser.getUser().getFirstName());
        Log.d("username", currUser.getUser().getLastName());
        welcome_message.setText(String.format("Welcome, %s %s", currUser.getUser().getFirstName(), currUser.getUser().getLastName()));
        locationBox = findViewById(R.id.locationTextBox);
        checkLocationPermission();
    }

    /**
     * Initializes system navigation bar
     */
    private void showNavigation() {
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setSelectedItemId(R.id.nav_home);
        bottomNavigationView.setOnItemSelectedListener(item -> {
            if (item.getItemId() == R.id.nav_settings) {
                move2SettingsPage();
                return true;
            } else if (item.getItemId() == R.id.nav_home) {
                return true;
            } else if (item.getItemId() == R.id.nav_job){
                String curRole = CurrentUser.getInstance().getUser().getRole();
                if(curRole.equals("Employer")) {
                    move2AllAppsPage();
                } else {
                    move2JobPage();
                }
                return true;
            }
            return false;
        });
    }

    /**
     * Navigate to the SettingsActivity
     */
    private void move2SettingsPage() {
        Intent intent = new Intent(DashboardActivity.this, SettingsActivity.class);
        startActivity(intent);
    }

    private void move2AllAppsPage() {
        Intent intent = new Intent(DashboardActivity.this, EmployerApplicationListActivity.class);
        startActivity(intent);
    }

    /**
     * Navigate to the JobActivity
     */
    private void move2JobPage() {
        Intent intent = new Intent(DashboardActivity.this, JobActivity.class);
        intent.putExtra("initialSearch", "");
        intent.putExtra("categoryFilter", "");
        startActivity(intent);
    }

    /**
     * Initialize UI according to the user role
     */
    private void showRolePage() {
        User user = CurrentUser.getInstance().getUser();

        if(user.getRole().equals("Employer")) {
            showEmployerPage();
        } else if (user.getRole().equals("Employee")) {
            showEmployeePage();
        }
    }

    /**
     * Initialize fragment UI of employer
     */
    private void showEmployerPage() {
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        EmployerFragment employerFragment = new EmployerFragment();
        transaction.replace(R.id.dashboardFragment, employerFragment);
        transaction.commit();
    }

    /**
     * Initialize fragment UI of employee
     */
    private void showEmployeePage() {
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        EmployeeFragment employeeFragment = new EmployeeFragment();
        transaction.replace(R.id.dashboardFragment, employeeFragment);
        transaction.commit();
    }

    /**
     * Retrieves the city based on the user's location.
     */
    private void getCity() {
        LocationHelper locationHelper = new LocationHelper(this, this);
        city = locationHelper.getCity();
        locationBox.setText("Location: ");
    }


    /**
     * Checks if the location permission, request if not permitted
     */
    private void checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            getCity();
        } else {
            requestPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION);
        }
    }

    @Override
    public void onCityUpdated(String cityName, double latitude, double longitude) {
        city = cityName;
        locationBox.setText(String.format("Location: %s", city));
    }
}
