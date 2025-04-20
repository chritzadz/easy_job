package com.example.template.view;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.template.Adapter.JobAdapter;
import com.example.template.Firebase.FirebaseUseCase;
import com.example.template.R;
import com.example.template.model.CurrentUser;
import com.example.template.model.User;
import com.example.template.util.LocationHelper;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class DashboardActivity extends AppCompatActivity implements JobAdapter.JobClickListener{
    private TextView welcomeLabel;
    private TextView locationLabel;
    private User currUser = CurrentUser.getInstance().getUser();
    private String currCity = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_dashboard);


        setContents();
        setEventListeners();

        FirebaseUseCase.set(this);

        requestLocationPermission();
    }

    private void setEventListeners() {

    }

    private void setContents() {
        welcomeLabel = findViewById(R.id.welcomeTextViewDashboard);
        locationLabel = findViewById(R.id.locationTextViewDashboard);

        RecyclerView resultView = findViewById(R.id.jobListRecyclerViewDashboard);
        LinearLayoutManager manager = new LinearLayoutManager(this);
        resultView.setLayoutManager(manager);

        DividerItemDecoration decoration = new DividerItemDecoration(resultView.getContext(), manager.getOrientation());
        resultView.addItemDecoration(decoration);

        JobAdapter adapter = new JobAdapter();
        adapter.setJobClickListener(this);
        resultView.setAdapter(adapter);

        updateWelcomeLabel();
        showNavigation();
    }

//    private void showRolePage() {
//        if(currUser.getRole().equals("Employer")) {
//            showEmployerPage();
//        } else if (currUser.getRole().equals("Employee")) {
//            showEmployeePage();
//        }
//    }

//    private void showEmployerPage() {
//        FragmentManager manager = getSupportFragmentManager();
//        FragmentTransaction transaction = manager.beginTransaction();
//        EmployerDashboardFragment employerFragment = new EmployerDashboardFragment();
//        transaction.replace(R.id.fragmentFragmentContainerDashboard, employerFragment);
//        transaction.commit();
//    }
//
//    private void showEmployeePage() {
//        FragmentManager manager = getSupportFragmentManager();
//        FragmentTransaction transaction = manager.beginTransaction();
//        EmployeeDashboardFragment employeeFragment = new EmployeeDashboardFragment();
//        transaction.replace(R.id.fragmentFragmentContainerDashboard, employeeFragment);
//        transaction.commit();
//    }

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

    public void requestLocationPermission(){
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                    this,
                    new String[]{ Manifest.permission.ACCESS_FINE_LOCATION },
                    1001
            );
        }
        new LocationHelper(this, (city, lat, lng)-> {
            locationLabel.setText(city);
        });
    }

    @Override
    public void onJobClick(View view, int position) {
        Intent intent = new Intent(this, DashboardActivity.class);
        startActivity(intent);
    }
}
