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

public class JobEditableAdapter extends RecyclerView.Adapter<JobEditableAdapter.JobEditableViewHolder> {
    private List<Job> jobs;
    private JobEditableClickListener listener;

    public JobEditableAdapter(List<Job> jobs){
        this.jobs = jobs;
    }

    @NonNull
    @Override
    public JobEditableViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_job_editable_item, parent, false);
        return new JobEditableViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull JobEditableViewHolder holder, int position) {
        Job job = jobs.get(position);
        holder.jobName.setText(job.getJobTitle());
        holder.jobPay.setText("$"+job.getJobPay());
        holder.jobLocation.setText(job.getJobLocation());
        holder.jobTime.setText(job.getJobHours()+" hrs");

        holder.itemView.setOnClickListener(view -> {
            if (listener != null) {
                listener.onJobEditableClick(view, position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return jobs.size();
    }

    public void setJobEditableClickListener(JobEditableClickListener listener) {
        this.listener = listener;
    }

    public interface JobEditableClickListener {
        void onJobEditableClick(View view, int position);
    }

    public static class JobEditableViewHolder extends RecyclerView.ViewHolder {
        TextView jobName;
        TextView jobLocation;
        TextView jobPay;
        TextView jobTime;
        public JobEditableViewHolder(View itemView) {
            super(itemView);
            jobName = itemView.findViewById(R.id.jobNameAdapterJobEditableItem);
            jobLocation = itemView.findViewById(R.id.jobLocationAdapterJobEditableItem);
            jobPay = itemView.findViewById(R.id.priceLabelAdapterJobEditableItem);
            jobTime = itemView.findViewById(R.id.timeLabelAdapterJobEditableItem);
        }
    }
}

