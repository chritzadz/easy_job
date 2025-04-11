/**
 *  File: ApplicationAdapter.java
 *  Author:
 *
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
import com.example.template.model.Application;

import java.util.List;

public class ApplicationAdapter extends RecyclerView.Adapter<ApplicationAdapter.ApplicationViewHolder> {
    private List<Application> appList; //List object to store Application object from database
    private ApplicationClickListener listener;  //click listener of Application object

    /**
     * Constructor for the ApplicationAdapter that initializes the application list.
     *
     * @param appList the list of Application objects to be managed by the adapter
     */
    public ApplicationAdapter(List<Application> appList) {
        this.appList = appList;
    }

    /**
     * Creates a new ViewHolder instance for each Application in the RecyclerView.
     * This method is called when the RecyclerView needs a new ViewHolder to represent an Application item.
     * @param parent The ViewGroup into which the new view will be added after it is bound to an adapter position.
     * @param viewType The view type of the new view. This can be used to distinguish between different layouts.
     * @return A new ApplicationViewHolder that holds the view for the application item.
     */
    @NonNull
    @Override
    public ApplicationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.application_item, parent, false);
        return new ApplicationViewHolder(view);
    }

    /**
     * Binds data from application list to ViewHolder
     * This method is called by the RecyclerView to display and set listener to the application by applicant email.
     * @param holder The ViewHolder that should be updated to represent the contents of the item
     *               at the given position.
     * @param position The position of the item within the Adapter.
     */
    @Override
    public void onBindViewHolder(@NonNull ApplicationViewHolder holder, int position) {
        Application application = appList.get(position);

        holder.appEmployeeTextView.setText(application.getEmployeeEmail());
        holder.appStatusTextView.setText("Status: " + application.getStatus()); // Added

        holder.itemView.setOnClickListener(view -> {
            if (listener != null) {
                listener.onAppClick(view, position);
            }
        });
    }

    /**
     * Returns the number of item retrieved from appList.
     * @return The number of items from appList.
     */
    @Override
    public int getItemCount() {
        return appList.size();
    }

    public void updateApplicationList(List<Application> newAppList) {
        this.appList = newAppList;
        notifyDataSetChanged();
    }

    /**
     * Set a listener object to each of the Application item
     * @param listener the listener object
     */
    public void setAppClickListener(ApplicationClickListener listener) {
        this.listener = listener;
    }

    /**
     * Listener interface for handling application item clicks.
     */
    public interface ApplicationClickListener {
        /**
         * Called when an application item is clicked. Implemented according to the usage.
         *
         * @param view The view that was clicked.
         * @param position The position of the clicked item in the adapter's data set.
         */
        void onAppClick(View view, int position);
    }

    /**
     * ViewHolder for displaying an application item in the RecyclerView.
     * This class holds references to the each item of the application
     */
    public static class ApplicationViewHolder extends RecyclerView.ViewHolder {
        TextView appEmployeeTextView;
        TextView appStatusTextView; // Added

        /**
         * Constructs a new ApplicationViewHolder.
         *
         * @param itemView The view representing an individual application item
         */
        public ApplicationViewHolder(View itemView) {
            super(itemView);
            appEmployeeTextView = itemView.findViewById(R.id.application_employee_email);
            appStatusTextView = itemView.findViewById(R.id.application_status); // Added
        }
    }
}