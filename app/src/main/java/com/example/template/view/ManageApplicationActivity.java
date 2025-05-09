package com.example.template.view;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.template.Adapter.ApplicationAdapter;
import com.example.template.Adapter.JobAdapter;
import com.example.template.Firebase.FirebaseUseCase;
import com.example.template.R;
import com.example.template.model.Job;

public class ManageApplicationActivity extends AppCompatActivity implements ApplicationAdapter.ApplicationClickListener{
    Job clickedJob;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getJobIntent();

        setContentView(R.layout.activity_manage_application);

        setContents();
        setEventListeners();

        FirebaseUseCase.set(this);
    }

    private void getJobIntent() {
        clickedJob = (Job) getIntent().getSerializableExtra("clickedJob");
    }

    private void setEventListeners() {

    }

    private void setContents() {
        showApplicationList();
    }

    private void showApplicationList() {
        RecyclerView resultView = findViewById(R.id.applicationListRecyclerViewManageApplication);
        LinearLayoutManager manager = new LinearLayoutManager(this);
        resultView.setLayoutManager(manager);

        DividerItemDecoration decoration = new DividerItemDecoration(resultView.getContext(), manager.getOrientation());
        resultView.addItemDecoration(decoration);

        ApplicationAdapter adapter = new ApplicationAdapter(FirebaseUseCase.getApplicationsByJob(clickedJob));
        adapter.setApplicationClickListener(this);
        resultView.setAdapter(adapter);
    }

    @Override
    public void onApplicationClick(View view, int position) {

    }
}
