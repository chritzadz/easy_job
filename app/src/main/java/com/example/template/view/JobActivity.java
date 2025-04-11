package com.example.template.view;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Filter;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.template.Adapter.JobAdapter;
import com.example.template.R;
import com.example.template.model.Application;
import com.example.template.model.CurrentUser;
import com.example.template.model.Job;
import com.example.template.model.User;
import com.example.template.util.FirebaseCRUD;
import com.example.template.util.LocationHelper;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.ArrayList;

/**
 * @file JobActivity.java
 * @author: -
 * @description: Activity that will handle in showing job details to potential employee.
 * two important method is applied here which is to navigate location and to apply for the job.
 */

public class JobActivity extends AppCompatActivity implements LocationHelper.LocationCallback, JobAdapter.JobClickListener, JobAdapter.viewLocationClickListener{
    LocationHelper locationHelper;
    FirebaseCRUD crud = FirebaseCRUD.getInstance();
    private ArrayList<Job> jobList;
    private ArrayList<Job> filteredJobs;
    String currCity;
    TextInputEditText searchField;
    Button searchButton;
    Button seeAppliedJobsButton;
    Button filterButton;
    String categoryFilter;
    int payFilter;
    ArrayList<String> appliedJobsID = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_job);


        initializeViews();
        searchField.setText("");

        setContentListeners();

        getFilterAndSearchIntent();
        getJobResults();

        locationHelper = new LocationHelper(this, this);
        showNavigation();
    }

    private void getFilterAndSearchIntent() {
        Intent intent = getIntent();
        searchField.setText(intent.getStringExtra("initialSearch"));
        categoryFilter = intent.getStringExtra("categoryFilter");
        payFilter = intent.getIntExtra("payFilter", 0);
    }

    @Override
    public void onCityUpdated(String cityName, double latitude, double longitude) {
        currCity = cityName;
        Log.i("CITY UPDATED", cityName);
    }

    @Override
    public void onJobClick(View view, int position) {
        Job clickedJob = filteredJobs.get(position);
        Intent intent = new Intent(this, JobDetailsActivity.class);
        intent.putExtra("jobObj", clickedJob);
        startActivity(intent);
    }

    @Override
    public void onViewLocationClick(View view, int position) {
        Job clickedJob = filteredJobs.get(position);
        Intent intent = new Intent(this, MapActivity.class);
        intent.putExtra("jobObj", clickedJob);
        intent.putExtra("from", "JobActivity");
        startActivity(intent);
    }

    private void getJobResults() {
        crud.getJobList().addOnSuccessListener(jobs -> {
            jobList = jobs;
            showJobResults();
        });
    }

    private void setContentListeners() {
        searchButton.setOnClickListener(v -> showJobResults());
        filterButton.setOnClickListener(v -> move2FilterPage());
        seeAppliedJobsButton.setOnClickListener(v -> move2AppliedJobListActivity());
    }

    private void move2AppliedJobListActivity() {
        Intent intent = new Intent(this, AppliedJobListActivity.class);
        startActivity(intent);
    }

    private void move2FilterPage() {
        Intent intent = new Intent(this, FilterActivity.class);
        intent.putExtra("initialSearch", searchField.getText().toString());
        startActivity(intent);
    }

    private void initializeViews() {
        searchButton = findViewById(R.id.searchButton);
        searchField = findViewById(R.id.searchTextField);
        filterButton = findViewById(R.id.buttonFilter);
        seeAppliedJobsButton = findViewById(R.id.buttonSeeAppliedJobs);
    }

    private void showJobResults() {
        String searchKeyword = searchField.getText().toString();
        ArrayList<Job> jobToBeShown;

        if(searchKeyword.isEmpty()){
            getAppliedJob();
            jobToBeShown = getAllJobOutsideAccount();
        }
        else{
            jobToBeShown = selectJobsToBeShown(searchKeyword);
        }

        filteredJobs = jobToBeShown;
        RecyclerView resultView = findViewById(R.id.jobPageJobRecycleView);
        LinearLayoutManager manager = new LinearLayoutManager(this);
        resultView.setLayoutManager(manager);
        DividerItemDecoration decoration = new DividerItemDecoration(resultView.getContext(), manager.getOrientation());
        resultView.addItemDecoration(decoration);

        JobAdapter jobAdapter = new JobAdapter(jobToBeShown);
        jobAdapter.setJobClickListener(this);
        jobAdapter.setViewLocationButtonListener(this);
        resultView.setAdapter(jobAdapter);
        resultView.setVerticalFadingEdgeEnabled(false);
    }

    private void getAppliedJob() {
        CurrentUser currUserObj = CurrentUser.getInstance();
        User currUser = currUserObj.getUser();
        crud.getApplicationList().addOnSuccessListener( appList -> {
            for (Application a : appList){
                if(a.getEmployeeEmail().equals(currUser.getEmail())){
                    appliedJobsID.add(a.getJobKey());
                }
            }
        });
    }

    private ArrayList<Job> getAllJobOutsideAccount() {
        CurrentUser currUserObj = CurrentUser.getInstance();
        User currUser = currUserObj.getUser();
        ArrayList<Job> result = new ArrayList<>();

        for (Job j : jobList) {
            boolean isPending = j.getJobStatus().equals("pending");
            boolean isApplied = !appliedJobsID.contains(j.getJobKey());
            if (!j.getJobEmail().equals(currUser.getEmail()) && isPending && isApplied) {
                result.add(j);
            }
        }

        return result;
    }

    private ArrayList<Job> selectJobsToBeShown(String searchKeyword) {
        CurrentUser currUserObj = CurrentUser.getInstance();
        User currUser = currUserObj.getUser();
        ArrayList<Job> result = new ArrayList<>();

        for (Job j : jobList) {
            boolean matchesSearch = j.getJobName().contains(searchKeyword) || searchKeyword.contains(j.getJobName()) || j.getJobLocation().contains(searchKeyword) || searchKeyword.contains(j.getJobLocation());
            boolean matchesCategory = categoryFilter.isEmpty() || j.getJobCategory().equals(categoryFilter) || categoryFilter.equals("No filter");
            boolean matchesPay = payFilter == 0 || Integer.parseInt(j.getJobPay()) >= payFilter;
            boolean matchesNotFromAccount = !j.getJobEmail().equals(currUser.getEmail());
            boolean isPending = j.getJobStatus().equals("pending");
            boolean isApplied = !appliedJobsID.contains(j.getJobKey());

            if (matchesSearch && matchesCategory && matchesPay && matchesNotFromAccount && isPending && isApplied) {
                result.add(j);
            }
        }

        return result;
    }

    private void showNavigation() {
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setSelectedItemId(R.id.nav_job);
        bottomNavigationView.setOnItemSelectedListener(item -> {
            if (item.getItemId() == R.id.nav_settings) {
                move2SettingsPage();
                return true;
            } else if (item.getItemId() == R.id.nav_home) {
                move2DashboardPage();
                return true;
            } else if (item.getItemId() == R.id.nav_job){
                //do nothing
                return true;
            }
            return false;
        });
    }

    private void move2DashboardPage() {
        Intent intent = new Intent(JobActivity.this, DashboardActivity.class);
        CurrentUser currUser = CurrentUser.getInstance();
        User user = currUser.getUser();
        intent.putExtra("userObj", user);
        startActivity(intent);
    }

    private void move2SettingsPage() {
        Intent intent = new Intent(JobActivity.this, SettingsActivity.class);
        startActivity(intent);
    }
}
