package com.example.template.view;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.template.Adapter.AppJobAdapter;
import com.example.template.Adapter.JobAdapter;
import com.example.template.R;
import com.example.template.model.Application;
import com.example.template.model.CurrentUser;
import com.example.template.model.Job;
import com.example.template.util.FirebaseCRUD;

import java.util.ArrayList;

public class AppliedJobListActivity extends AppCompatActivity implements JobAdapter.JobClickListener{
    FirebaseCRUD crud = FirebaseCRUD.getInstance(this);
    private ArrayList<Job> jobList;
    private Button backButton;

    private ArrayList<Job> filteredJobList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_applied_job_list);

        initializeViews();
        initializeListener();

        getJobResults();
    }

    private void getJobResults() {
        crud.getJobList().addOnSuccessListener(jobs -> {
            jobList = jobs;
            showJobResults();
        });
    }

    private void showJobResults() {
        ArrayList<Job> jobToBeShown = new ArrayList<>();

        //show jobs that the current applicant have applied
        crud.getApplicationList().addOnSuccessListener(appList -> {
            for(Application a : appList){
                if(a.getEmployeeEmail().equals(CurrentUser.getInstance().getUser().getEmail())){
                    for(Job j :jobList){
                        if(a.getJobKey().equals(j.getJobKey())){
                            jobToBeShown.add(j);
                        }
                    }
                }
            }

            filteredJobList = jobToBeShown;
            RecyclerView resultView = findViewById(R.id.appliedJobsRecyclerView);
            LinearLayoutManager manager = new LinearLayoutManager(this);
            resultView.setLayoutManager(manager);
            DividerItemDecoration decoration = new DividerItemDecoration(resultView.getContext(), manager.getOrientation());
            resultView.addItemDecoration(decoration);

            JobAdapter jobAdapter = new AppJobAdapter(jobToBeShown);
            jobAdapter.setJobClickListener(this);
            resultView.setAdapter(jobAdapter);
            resultView.setVerticalFadingEdgeEnabled(false);
        });
    }

    @Override
    public void onJobClick(View view, int position) {
        Job clickedJob = filteredJobList.get(position);
        if(clickedJob.getJobStatus().equals("To do")){
            Intent intent = new Intent(this, AppliedJobDetailsActivity.class);
            intent.putExtra("jobObj", clickedJob);
            startActivity(intent);
        }
    }

    public void initializeViews(){
        backButton = findViewById(R.id.appliedJobListBackButton);
    }

    private void initializeListener() {
        backButton.setOnClickListener(v -> move2JobActivity());
    }

    public void move2JobActivity(){
        Intent intent = new Intent(this, JobActivity.class);
        startActivity(intent);
    }
}
