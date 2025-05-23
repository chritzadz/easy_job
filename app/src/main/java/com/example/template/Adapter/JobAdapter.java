/**
 *  File: ApplicationAdapter.java
 *  Author:
 *  Description: This class implements an Adapter design pattern that allows
 *  to fetch application data from firebase realtime database to interact and shown
 *  in RecyclerView format.
 */

package com.example.template.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.template.R;
import com.example.template.model.Job;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

public class JobAdapter extends RecyclerView.Adapter<JobAdapter.JobViewHolder> {
    private List<Job> jobs;
    private JobClickListener listener;

    public JobAdapter(List<Job> jobs){
        this.jobs = jobs;
    }

    @NonNull
    @Override
    public JobViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_job_item, parent, false);
        return new JobViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull JobViewHolder holder, int position) {
        Job job = jobs.get(position);
        holder.jobName.setText(job.getJobTitle());
        holder.jobPay.setText("$"+job.getJobPay());
        holder.jobLocation.setText(job.getJobLocation());
        holder.jobTime.setText(job.getJobHours()+" hrs");

        holder.itemView.setOnClickListener(view -> {
            if (listener != null) {
                listener.onJobClick(view, position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return jobs.size();
    }

    public void setJobClickListener(JobClickListener listener) {
        this.listener = listener;
    }

    public interface JobClickListener {
        void onJobClick(View view, int position);
    }

    public static class JobViewHolder extends RecyclerView.ViewHolder {
        TextView jobName;
        TextView jobLocation;
        TextView jobPay;
        TextView jobTime;
        public JobViewHolder(View itemView) {
            super(itemView);
            jobName = itemView.findViewById(R.id.jobNameAdapterJobItem);
            jobLocation = itemView.findViewById(R.id.jobLocationAdapterJobItem);
            jobPay = itemView.findViewById(R.id.priceLabelAdapterJobItem);
            jobTime = itemView.findViewById(R.id.timeLabelAdapterJobItem);
        }
    }
}

