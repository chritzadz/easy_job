package com.example.template.view;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.template.Adapter.JobAdapter;
import com.example.template.Adapter.JobEditableAdapter;
import com.example.template.Firebase.FirebaseUseCase;
import com.example.template.R;

public class ManageJobListingActivity extends AppCompatActivity implements JobEditableAdapter.JobEditableClickListener {
    Button addJobButton;

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
    }

    private void move2AddJobPage() {
        Intent intent = new Intent(this, AddJobActivity.class);
        startActivity(intent);
    }

    private void setContents() {
        addJobButton = findViewById(R.id.addButtonManageJobListing);

        RecyclerView resultView = findViewById(R.id.jobListRecyclerViewManageJobListing);
        LinearLayoutManager manager = new LinearLayoutManager(this);
        resultView.setLayoutManager(manager);

        DividerItemDecoration decoration = new DividerItemDecoration(resultView.getContext(), manager.getOrientation());
        resultView.addItemDecoration(decoration);

        JobEditableAdapter adapter = new JobEditableAdapter();
        adapter.setJobEditableClickListener(this);
        resultView.setAdapter(adapter);
    }

    @Override
    public void onJobEditableClick(View view, int position) {
        //go to edit job
    }
}
