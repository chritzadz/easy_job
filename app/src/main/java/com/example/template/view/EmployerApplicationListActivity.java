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
import com.example.template.model.User;
import com.example.template.util.FirebaseCRUD;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;

public class EmployerApplicationListActivity extends AppCompatActivity implements ApplicationAdapter.ApplicationClickListener {

    FirebaseCRUD crud = FirebaseCRUD.getInstance();
    private ArrayList<Application> appList;
    private Button backButton;

    @Override
    protected void onCreate(Bundle SavedInstanceState) {
        super.onCreate(SavedInstanceState);
        setContentView(R.layout.activity_employer_application_list);

        backButton = findViewById(R.id.employerAppListBack);
        setContentListeners();

        getApplicationsFromDB();

        showNavigation();
    }

    private void setContentListeners() {backButton.setOnClickListener(v -> move2Dashboard());}

    private void move2Dashboard() {
        Intent intent = new Intent(EmployerApplicationListActivity.this, DashboardActivity.class);
        startActivity(intent);
    }

    /**
     * Displays the list of applications in a RecyclerView.
     * @param applicationListFromDB The list of applications to display.
     */
    private void showEmployeeAppliedList(ArrayList<Application> applicationListFromDB) {
        appList = applicationListFromDB;
        RecyclerView resultView = findViewById(R.id.allAppRecyler);
        LinearLayoutManager manager = new LinearLayoutManager(this);
        resultView.setLayoutManager(manager);

        DividerItemDecoration decoration = new DividerItemDecoration(resultView.getContext(), manager.getOrientation());
        resultView.addItemDecoration(decoration);

        ApplicationAdapter adapter = new ApplicationAdapter(applicationListFromDB);
        adapter.setAppClickListener(this);
        resultView.setAdapter(adapter);
    }

    /**
     * Fetches the list of applications from the database and filters them by the job's email.
     */
    private void getApplicationsFromDB() {
        crud.getApplicationList().addOnSuccessListener(applications -> {
           ArrayList<Application> jobApplications = new ArrayList<>();
           User curUser = CurrentUser.getInstance().getUser();

           for (Application a : applications) {
               if (a.getEmployerEmail().equals(curUser.getEmail())){
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
        crud.getJobByID(clickedApp.getJobKey()).addOnSuccessListener(job -> {
            Intent intent = new Intent(this, ApplicationDetailsActivity.class);
            intent.putExtra("appObj", clickedApp);
            intent.putExtra("appId", clickedApp.getKey());
            intent.putExtra("corrJob", job);
            intent.putExtra("prevPage", "EmployerApplicationListActivity");
            startActivity(intent);
        });
    }

    private void showNavigation() {
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setSelectedItemId(R.id.nav_job);
        bottomNavigationView.setOnItemSelectedListener(item -> {
            if (item.getItemId() == R.id.nav_settings) {
                move2SettingsPage();
                return true;
            } else if (item.getItemId() == R.id.nav_home) {
                move2Dashboard();
                return true;
            } else if (item.getItemId() == R.id.nav_job){
                return true;
            }
            return false;
        });
    }

    private void move2SettingsPage() {
        Intent intent = new Intent(this, SettingsActivity.class);
        startActivity(intent);
    }
}
