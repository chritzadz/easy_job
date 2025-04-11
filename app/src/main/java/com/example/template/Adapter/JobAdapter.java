/**
 *  File: JobAdapter.java
 *  Author:
 *
 *  Description: This class implements an Adapter design pattern that allows
 *  to fetch job data from firebase realtime database to interact and shown
 *  in RecyclerView format.
 */
package com.example.template.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.template.R;
import com.example.template.model.Job;

import java.util.ArrayList;
import java.util.List;

public class JobAdapter extends RecyclerView.Adapter<JobAdapter.JobViewHolder> {
    protected List<Job> jobList; //List object to store Application object from database
    protected JobClickListener listener; //click listener of Application object
    private viewLocationClickListener viewLocButtonListener;

    /**
     * Constructor for the JobAdapter that initializes the application list.
     *
     * @param jobList the list of Application objects to be managed by the adapter
     */
    public JobAdapter(List<Job> jobList) {
        this.jobList = jobList;
    }

    /**
     * Creates a new ViewHolder instance for each Job in the RecyclerView.
     * This method is called when the RecyclerView needs a new ViewHolder to represent an Job item.
     * @param parent The ViewGroup into which the new view will be added after it is bound to an adapter position.
     * @param viewType The view type of the new view. This can be used to distinguish between different layouts.
     * @return A new JobViewHolder that holds the view for the job item.
     */
    @NonNull
    @Override
    public JobViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.job_item, parent, false);
        return new JobViewHolder(view);
    }

    /**
     * Binds data from job list to ViewHolder
     * This method is called by the RecyclerView to display and set listener to the job by applicant email.
     * @param holder The ViewHolder that should be updated to represent the contents of the item
     *               at the given position.
     * @param position The position of the item within the Adapter.
     */
    @Override
    public void onBindViewHolder(@NonNull JobViewHolder holder, int position) {
        Job job = jobList.get(position);
        holder.jobTitleTextView.setText(job.getJobName());
        holder.jobLocationTextView.setText(job.getJobLocation());
        holder.jobPayTextView.setText(String.format("$%s", job.getJobPay()));
        holder.jobCategoryTextView.setText(job.getJobCategory());
        holder.jobHoursTextView.setText(String.format("%s hours", job.getJobHours()));

        holder.itemView.setOnClickListener(view -> {
            if (listener != null) {
                listener.onJobClick(view, position); //for seeing job later
            }
        });

        holder.viewLocationButton.setOnClickListener(view -> {
            if (viewLocButtonListener != null) {
                viewLocButtonListener.onViewLocationClick(view, position);
            }
        });
    }

    /**
     * Returns the number of item retrieved from jobList.
     * @return The number of items from jobList.
     */
    @Override
    public int getItemCount() {
        return jobList.size();
    }

    public void updateJobList(List<Job> newJobList) {
        this.jobList = newJobList;
        notifyDataSetChanged();
    }

    /**
     * Set a listener object to each of the Job item
     * @param listener the listener object
     */
    public void setJobClickListener(JobClickListener listener) {
        this.listener = listener;
    }

    public void setViewLocationButtonListener(viewLocationClickListener listener) {
        this.viewLocButtonListener = listener;
    }

    /**
     * Listener interface for handling job item clicks.
     */
    public interface JobClickListener {
        /**
         * Called when an job item is clicked. Implemented according to the usage.
         *
         * @param view The view that was clicked.
         * @param position The position of the clicked item in the adapter's data set.
         */
        void onJobClick(View view, int position);
    }

    public interface viewLocationClickListener {
        /**
         * Called when an job item is clicked. Implemented according to the usage.
         *
         * @param view The view that was clicked.
         * @param position The position of the clicked item in the adapter's data set.
         */
        void onViewLocationClick(View view, int position);
    }

    public void updateData(ArrayList<Job> newJobList) {
        this.jobList.clear(); // Clear the old data
        this.jobList.addAll(newJobList); // Add the new data
        notifyDataSetChanged(); // Notify the adapter of the data change
    }

    /**
     * ViewHolder for displaying an job item in the RecyclerView.
     * This class holds references to the each item of the job
     */
    public static class JobViewHolder extends RecyclerView.ViewHolder {
        TextView jobTitleTextView;
        TextView jobLocationTextView;
        TextView jobCategoryTextView;
        TextView jobHoursTextView;
        Button viewLocationButton;
        TextView jobPayTextView;
        TextView jobStatusTextView;

        /**
         * Constructs a new ApplicationViewHolder.
         *
         * @param itemView The view representing an individual job item
         */
        public JobViewHolder(View itemView) {
            super(itemView);
            jobTitleTextView = itemView.findViewById(R.id.job_name_text_box);
            jobLocationTextView = itemView.findViewById(R.id.job_location_text_box);
            jobCategoryTextView = itemView.findViewById(R.id.job_category_text_box);
            jobHoursTextView = itemView.findViewById(R.id.job_hours_text_box);
            viewLocationButton = itemView.findViewById(R.id.buttonViewLocation);
            jobPayTextView = itemView.findViewById(R.id.job_pay_text_box);
            jobStatusTextView = itemView.findViewById(R.id.job_status_text_box);
        }
    }
}