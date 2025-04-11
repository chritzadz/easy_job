package com.example.template.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;

import com.example.template.R;
import com.example.template.model.Job;

import java.util.List;

public class AppJobAdapter extends JobAdapter{


    /**
     * Constructor for the JobAdapter that initializes the application list.
     *
     * @param jobList the list of Application objects to be managed by the adapter
     */
    public AppJobAdapter(List<Job> jobList) {
        super(jobList);
    }

    @NonNull
    @Override
    public JobViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.app_job_item, parent, false);
        return new JobViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull JobViewHolder holder, int position) {
        Job job = jobList.get(position);
        holder.jobTitleTextView.setText(job.getJobName());
        holder.jobLocationTextView.setText(job.getJobLocation());
        holder.jobPayTextView.setText(String.format("$%s", job.getJobPay()));
        holder.jobStatusTextView.setText(job.getJobStatus());

        holder.itemView.setOnClickListener(view -> {
            if (listener != null) {
                listener.onJobClick(view, position);
            }
        });
    }
}
