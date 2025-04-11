package com.example.template.view;

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

import com.example.template.Adapter.ApplicationAdapter;
import com.example.template.Adapter.JobAdapter;
import com.example.template.R;
import com.example.template.model.Application;
import com.example.template.model.CurrentUser;
import com.example.template.model.Employer;
import com.example.template.model.Job;
import com.example.template.util.FirebaseCRUD;
import com.example.template.util.LocationHelper;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

/**
 * @file EmployerFragment.java
 * @author: -
 * @description: Fragment that represents the employer's view for managing job postings.
 */

public class EmployerFragment extends Fragment implements JobAdapter.JobClickListener, JobAdapter.viewLocationClickListener{
    FirebaseCRUD crud = FirebaseCRUD.getInstance();
    private ArrayList<Job> jobList;
    Button addJobButton;
    View view;

    public EmployerFragment() {
        // Required empty public constructor
    }

    /**
     * Creates a new instance of EmployerFragment.
     * @return A new instance of EmployerFragment.
     */
    public static EmployerFragment newInstance() {
        EmployerFragment fragment = new EmployerFragment();
        Bundle args = new Bundle();

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_employer, container, false);

        getJobsFromUser();

        addJobButton = view.findViewById(R.id.addNewJobButton);
        setEventListener();
        return view;
    }

    /**
     * Displays the job postings of the current user in a RecyclerView
     * @param view The view containing the RecyclerView
     * @param jobListFromDb The list of jobs to display
     */
    private void showUsersJobPostings(View view, ArrayList<Job> jobListFromDb) {
        jobList = jobListFromDb;
        RecyclerView resultView = view.findViewById(R.id.employerRecycler);
        LinearLayoutManager manager = new LinearLayoutManager(getActivity());
        resultView.setLayoutManager(manager);
        DividerItemDecoration decoration = new DividerItemDecoration(resultView.getContext(), manager.getOrientation());
        resultView.addItemDecoration(decoration);
        JobAdapter adapter = new JobAdapter(jobListFromDb);

        adapter.setJobClickListener(this);
        adapter.setViewLocationButtonListener(this);

        resultView.setAdapter(adapter);
        resultView.setVerticalFadingEdgeEnabled(false);
    }

    /**
     * Retrieves the job postings associated with the current user.
     */
    private void getJobsFromUser() {
        crud.getJobList().addOnSuccessListener(jobs -> {
            Employer currentUser = (Employer) CurrentUser.getInstance().getUser();
            String curUserEmail = currentUser.getEmail();
            ArrayList<Job> usersJobs = new ArrayList<>();


            for (Job job : jobs) {
                if (job.getJobEmail().equals(curUserEmail)) {
                    usersJobs.add(job);
                }
            }
            showUsersJobPostings(view, usersJobs);
        });
    }

    /**
     * Handles job item click event in the RecyclerView.
     * @param view The view that was clicked
     * @param position The position of the job item in the RecyclerView
     */
    @Override
    public void onJobClick(View view, int position) {
        Job clickedJob = jobList.get(position);
        Intent intent;
        Log.i("JOB STATUS", clickedJob.getJobStatus());
        if (clickedJob.getJobStatus().equals("pending")){
            intent = new Intent(getActivity(), ApplicationListActivity.class);
        }
        else {
            intent = new Intent(getActivity(), AppliedJobDetailsActivity.class);
        }
        intent.putExtra("jobObj", clickedJob);
        startActivity(intent);
    }

    /**
     * Initializes each components listeners
     */
    public void setEventListener(){ addJobButton.setOnClickListener(this::move2JobPostingPage);}

    /**
     * Navigate to the JobPostingFormActivity
     * @param view The view that was clicked.
     */
    public void move2JobPostingPage(View view) {
        Intent intent = new Intent(this.getContext(), JobPostingFormActivity.class);
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