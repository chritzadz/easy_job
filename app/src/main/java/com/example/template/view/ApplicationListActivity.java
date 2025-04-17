package com.example.template.view;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.template.Adapter.ApplicationAdapter;
import com.example.template.R;
import com.example.template.model.Application;
import com.example.template.model.CurrentUser;
import com.example.template.model.Job;
import com.example.template.util.FirebaseCRUD;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;

/**
 * @file ApplicationListActivity.java
 * @author: -
 * @description: Activity that will fetch all application related to the job from database.
 */

public class ApplicationListActivity extends AppCompatActivity implements ApplicationAdapter.ApplicationClickListener {
    FirebaseCRUD crud = FirebaseCRUD.getInstance(this);
    private Job job;

    private Button backButton;
    private ArrayList<Application> appList;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_application_list);

        setContents();
        setContentListeners();

        getJobIntent();
        getApplicationsFromDB();
    }

    @Override
    protected void onResume() {
        super.onResume();
        getApplicationsFromDB(); // to refresh data
    }

    private void setContentListeners() {
        backButton.setOnClickListener(v -> move2Dashboard());
    }

    private void move2Dashboard() {
        Intent intent = new Intent(this, DashboardActivity.class);
        startActivity(intent);
    }

    private void setContents() {
        backButton = findViewById(R.id.backButtonInAppList);
    }

    private void getJobIntent() {
        Intent intent = getIntent();
        job = (Job) intent.getSerializableExtra("jobObj");
    }

    /**
     * Displays the list of applications in a RecyclerView.
     * @param applicationListFromDB The list of applications to display.
     */
    private void showEmployeeAppliedList(ArrayList<Application> applicationListFromDB) {
        appList = applicationListFromDB;
        RecyclerView resultView = findViewById(R.id.applicationRec);
        LinearLayoutManager manager = new LinearLayoutManager(this);
        resultView.setLayoutManager(manager);

        DividerItemDecoration decoration = new DividerItemDecoration(resultView.getContext(), manager.getOrientation());
        resultView.addItemDecoration(decoration);

        ApplicationAdapter adapter = new ApplicationAdapter(applicationListFromDB);
        adapter.setAppClickListener(this);
        resultView.setAdapter(adapter);
    }

    /**
     * Fetches the list of applications from the database and filters them by the job's email
     * and the job's unique key.
     */
    private void getApplicationsFromDB() {
        crud.getApplicationList().addOnSuccessListener(applications -> {
            ArrayList<Application> jobApplications = new ArrayList<>();

            for (Application a : applications) {
                if (a.getEmployerEmail().equals(job.getJobEmail()) && a.getJobKey().equals(job.getJobKey())) {
                    a.setKey(a.getKey());
                    jobApplications.add(a);
                }
            }
            showEmployeeAppliedList(jobApplications);
        });
    }

    /**
     * Handles the click event for an application in the list.
     *
     * @param view The view that was clicked.
     * @param position The position of the clicked item in the list.
     */
    @Override
    public void onAppClick(View view, int position) {
        Application clickedApp = appList.get(position);
        Intent intent = new Intent(this, ApplicationDetailsActivity.class);
        intent.putExtra("appObj", clickedApp);
        intent.putExtra("appId", clickedApp.getKey());
        intent.putExtra("corrJob", job);
        intent.putExtra("prevPage", "ApplicationListActivity");
        startActivity(intent);
    }
}