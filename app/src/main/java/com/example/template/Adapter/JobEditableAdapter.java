/**
 *  File: ApplicationAdapter.java
 *  Author:
 *  Description: This class implements an Adapter design pattern that allows
 *  to fetch application data from firebase realtime database to interact and shown
 *  in RecyclerView format.
 */

package com.example.template.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.template.R;
import com.example.template.model.Job;
import com.example.template.view.JobDetailsActivity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

public class JobEditableAdapter extends RecyclerView.Adapter<JobEditableAdapter.JobEditableViewHolder> {
    private List<Job> jobs;
    private JobEditableClickListener listener;
    private JobEditableClickListener editButtonListener;
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

        holder.editButton.setOnClickListener(v -> {
            if (editButtonListener != null) {
                editButtonListener.onJobEditButtonClick(v, position);
            }
        });

        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onJobEditableClick(v, position);
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

    public void setJobEditButtonClickListener(JobEditableClickListener listener) {
        this.editButtonListener = listener;
    }

    public interface JobEditableClickListener {
        void onJobEditableClick(View view, int position);
        void onJobEditButtonClick(View view, int position);
    }

    public static class JobEditableViewHolder extends RecyclerView.ViewHolder {
        TextView jobName;
        TextView jobLocation;
        TextView jobPay;
        TextView jobTime;
        TextView editButton;

        public JobEditableViewHolder(View itemView) {
            super(itemView);
            jobName = itemView.findViewById(R.id.jobNameAdapterJobEditableItem);
            jobLocation = itemView.findViewById(R.id.jobLocationAdapterJobEditableItem);
            jobPay = itemView.findViewById(R.id.priceLabelAdapterJobEditableItem);
            jobTime = itemView.findViewById(R.id.timeLabelAdapterJobEditableItem);
            editButton = itemView.findViewById(R.id.editTextViewAdapterJobEditableItem);
        }
    }
}

