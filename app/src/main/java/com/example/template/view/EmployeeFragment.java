package com.example.template.view;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.template.Adapter.JobAdapter;
import com.example.template.R;
import com.example.template.model.Application;
import com.example.template.model.CurrentUser;
import com.example.template.model.Employee;
import com.example.template.model.Job;
import com.example.template.model.User;
import com.example.template.util.FirebaseCRUD;
import com.example.template.util.LocationHelper;
import com.google.firebase.messaging.FirebaseMessaging;

import java.util.ArrayList;

/**
 * @file EmployeeFragment.java
 * @author: -
 * @description: Fragment that represents the employee's view for seeing job postings.
 * two important method is applied here which is to navigate location and to apply for the job.
 */

public class EmployeeFragment extends Fragment implements LocationHelper.LocationCallback, JobAdapter.JobClickListener, JobAdapter.viewLocationClickListener {
    Button moreJobsButton;
    private JobAdapter jobAdapter;
    private ArrayList<Job> jobList;
    private String currCity;
    View view;
    FirebaseCRUD crud = FirebaseCRUD.getInstance();
    LocationHelper locationHelper;
    ArrayList<String> appliedJobsID = new ArrayList<>();

    /**
     * Callback method that is called when the city is updated.
     *
     * @param cityName   The name of the updated city.
     * @param latitude   The latitude of the updated location.
     * @param longitude  The longitude of the updated location.
     */
    @Override
    public void onCityUpdated(String cityName, double latitude, double longitude) {
        currCity = cityName;
        Log.i("CITY UPDATED", cityName);
        showJobsByCurrentLocation();
        appliedJobsID = new ArrayList<>();
        getAppliedJob();
    }


    public EmployeeFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_employee, container, false);
        moreJobsButton = view.findViewById(R.id.moreJobsButton);

        locationHelper = new LocationHelper(getContext(), this);

        setEventListeners();
        initNotifications();
        return view;
    }

    private void initNotifications() {
        FirebaseMessaging.getInstance().subscribeToTopic("jobs");
    }

    /**
     * Sets event listeners for UI components.
     */
    private void setEventListeners() {
        moreJobsButton.setOnClickListener(this::moveToJobActivity);
    }

    /**
     * Navigate to the FilterActivity
     * @param view The view that was clicked.
     */
    private void moveToJobActivity(View view) {
        Intent intent = new Intent(this.getContext(), JobActivity.class);
        intent.putExtra("initialSearch", "");
        intent.putExtra("categoryFilter", "");
        startActivity(intent);
    }

    /**
     * Displays the search results in the RecyclerView.
     * @param view             The view containing the RecyclerView
     * @param jobListFromDb    The list of jobs to display
     */
    private void showSearchResults(View view, ArrayList<Job> jobListFromDb) {
        jobList = jobListFromDb;
        RecyclerView resultView = view.findViewById(R.id.jobRecycleView);
        LinearLayoutManager manager = new LinearLayoutManager(getActivity());
        resultView.setLayoutManager(manager);
        DividerItemDecoration decoration = new DividerItemDecoration(resultView.getContext(), manager.getOrientation());
        resultView.addItemDecoration(decoration);
        jobAdapter = new JobAdapter(jobListFromDb);
        jobAdapter.setJobClickListener(this);
        jobAdapter.setViewLocationButtonListener(this);


        resultView.setAdapter(jobAdapter);
        resultView.setVerticalFadingEdgeEnabled(false);
    }

    private void getAppliedJob() {
        CurrentUser currUserObj = CurrentUser.getInstance();
        User currUser = currUserObj.getUser();
        crud.getApplicationList().addOnSuccessListener( appList ->{
            for (Application a : appList){
                if(a.getEmployeeEmail().equals(currUser.getEmail())){
                    appliedJobsID.add(a.getJobKey());
                }
            }
        });
    }

    private void showJobsByCurrentLocation() {
        crud.getJobList().addOnSuccessListener(jobs -> {
            ArrayList<Job> relevantJobs = new ArrayList<>();
            CurrentUser currUserObj = CurrentUser.getInstance();
            User currUser = currUserObj.getUser();
            for (Job job : jobs) {
                String jobCity = job.getJobLocation();
                Log.i("SEE", String.format("%s: %s", appliedJobsID.contains(job.getJobKey()), job.getJobKey()));
                if (!job.getJobEmail().equals(currUser.getEmail()) && jobCity.equals(currCity) && !appliedJobsID.contains(job.getJobKey())) {
                    relevantJobs.add(job);
                }
            }
            getAppliedJob();
            showSearchResults(view, relevantJobs);
        });
    }

    /**
     * Handles job item click event in the RecyclerView.
     * @param view     The view that was clicked
     * @param position The position of the job item
     */
    @Override
    public void onJobClick(View view, int position) {
        Job clickedJob = jobList.get(position);
        Intent intent = new Intent(getActivity(), JobDetailsActivity.class);
        intent.putExtra("jobObj", clickedJob);
        startActivity(intent);
    }

    @Override
    public void onViewLocationClick(View view, int position) {
        Job clickedJob = jobList.get(position);
        Intent intent = new Intent(getActivity(), MapActivity.class);
        intent.putExtra("jobObj", clickedJob);
        intent.putExtra("from", "Dashboard");
        startActivity(intent);
    }
}