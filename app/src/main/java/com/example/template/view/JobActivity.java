package com.example.template.view;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.template.Adapter.JobAdapter;
import com.example.template.Firebase.FirebaseUseCase;
import com.example.template.R;
import com.example.template.model.CurrentUser;
import com.example.template.model.Job;
import com.example.template.model.User;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.List;

public class JobActivity extends AppCompatActivity implements JobAdapter.JobClickListener{
    User currUser = CurrentUser.getInstance().getUser();
    TextView searchInput;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_job);


        setContents();
        setEventListeners();

        FirebaseUseCase.set(this);
    }

    private void setEventListeners() {
    }

    private void setContents() {
        searchInput = findViewById(R.id.searchTextViewJob);

        RecyclerView resultView = findViewById(R.id.jobListRecyclerViewJob);
        LinearLayoutManager manager = new LinearLayoutManager(this);
        resultView.setLayoutManager(manager);

        DividerItemDecoration decoration = new DividerItemDecoration(resultView.getContext(), manager.getOrientation());
        resultView.addItemDecoration(decoration);

        JobAdapter adapter = new JobAdapter(FirebaseUseCase.getJobsFromOthers(currUser.getEmail()));
        adapter.setJobClickListener(this);
        resultView.setAdapter(adapter);

        showNavigation();
    }

    private void showNavigation() {
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setPadding(0,0,0,0);
        bottomNavigationView.setSelectedItemId(R.id.nav_job);
        bottomNavigationView.setOnItemSelectedListener(item -> {
            if (item.getItemId() == R.id.nav_settings) {
                move2ProfilePage();
                return true;
            } else if (item.getItemId() == R.id.nav_home) {
                move2HomePage();
                return true;
            } else if (item.getItemId() == R.id.nav_job) {
                return true;
            }
            return false;
        });
    }

    @Override
    public void onJobClick(View view, int position) {
        Intent intent = new Intent(this, JobDetailsActivity.class);

        List<Job> jobList = FirebaseUseCase.getJobsFromOthers(currUser.getEmail());

        intent.putExtra("clickedJob", jobList.get(position));
        startActivity(intent);
    }

    private void move2HomePage() {
        Intent intent = new Intent(this, DashboardActivity.class);
        startActivity(intent);
    }

    private void move2ProfilePage() {
        Intent intent = new Intent(this, ProfileActivity.class);
        startActivity(intent);
    }
}
