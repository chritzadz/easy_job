package com.example.template.view;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.template.Adapter.JobAdapter;
import com.example.template.Adapter.JobEditableAdapter;
import com.example.template.Firebase.FirebaseUseCase;
import com.example.template.R;
import com.example.template.model.CurrentUser;
import com.example.template.model.Job;
import com.example.template.model.User;

import java.util.List;

public class ManageJobListingActivity extends AppCompatActivity implements JobEditableAdapter.JobEditableClickListener {
    Button addJobButton;
    ImageView backButton;
    User currUser = CurrentUser.getInstance().getUser();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_manage_job_listing);

        setContents();
        setEventListeners();

        FirebaseUseCase.set(this);
    }

    private void setEventListeners() {
        addJobButton.setOnClickListener(v -> move2AddJobPage());
        backButton.setOnClickListener(v -> move2ProfilePage());
    }

    private void move2ProfilePage() {
        Log.d("CLICK", "BACK BUTTON");
        Intent intent = new Intent(this, ProfileActivity.class);
        startActivity(intent);
    }

    private void move2AddJobPage() {
        Intent intent = new Intent(this, AddJobActivity.class);
        startActivity(intent);
    }

    private void setContents() {
        addJobButton = findViewById(R.id.addButtonManageJobListing);
        backButton = findViewById(R.id.backButtonImageViewManageJobListing);

        RecyclerView resultView = findViewById(R.id.jobListRecyclerViewManageJobListing);
        LinearLayoutManager manager = new LinearLayoutManager(this);
        resultView.setLayoutManager(manager);

        DividerItemDecoration decoration = new DividerItemDecoration(resultView.getContext(), manager.getOrientation());
        resultView.addItemDecoration(decoration);

        JobEditableAdapter adapter = new JobEditableAdapter(FirebaseUseCase.getJobsFromSelf(currUser.getEmail()));
        adapter.setJobEditableClickListener(this);
        resultView.setAdapter(adapter);
    }

    @Override
    public void onJobEditableClick(View view, int position) {
        Intent intent = new Intent(this, EditJobActivity.class); //have not change this
        List<Job> jobList = FirebaseUseCase.getJobsFromSelf(currUser.getEmail());
        intent.putExtra("jobClicked", jobList.get(position));

        startActivity(intent);
    }
}
