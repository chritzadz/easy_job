package com.example.template.view;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.template.Adapter.JobAdapter;
import com.example.template.Adapter.JobEditableAdapter;
import com.example.template.Firebase.FirebaseUseCase;
import com.example.template.R;

public class ManageJobListingActivity extends AppCompatActivity implements JobEditableAdapter.JobEditableClickListener {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_manage_job_listing);

        setContents();
        setEventListeners();

        FirebaseUseCase.set(this);
    }

    private void setEventListeners() {
    }

    private void setContents() {

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
