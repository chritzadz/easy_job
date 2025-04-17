package com.example.template.util;

import static androidx.core.content.ContentProviderCompat.requireContext;

import android.content.Context;
import android.util.Log;
import androidx.annotation.NonNull;

import com.example.template.R;
import com.example.template.factory.UserRoleFactory;
import com.example.template.model.Application;
import com.example.template.model.User;
import com.example.template.model.Job;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskCompletionSource;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @file FirebaseCRUD.java
 * @author: -
 * @description: Utility class for performing CRUD operations with Firebase Realtime Database.
 * The FirebaseCRUD class provides methods to manage users, jobs, and applications
 * from the Firebase database. This utility class implements a Singleton design pattern.
 */
public class FirebaseCRUD {
    // Constants for job categories
    public static final String[] JOB_CATEGORIES = {
            "Gardening",
            "Pet Care",
            "Cleaning",
            "Delivery",
            "Tutoring"
    };

    private final FirebaseDatabase database;
    private DatabaseReference usersRef = null;
    private DatabaseReference jobsRef = null;
    private DatabaseReference appsRef = null;
    private User usersList;
    private Job jobsList;
    private Application appsList;
    private static FirebaseCRUD instance;
    private Context context;

    /**
     * Private constructor to initialize Firebase database references.
     */
    private FirebaseCRUD(Context context) {
        this.context = context;
        this.database = FirebaseDatabase.getInstance(context.getString(R.string.firebase_ref));
        initializeDatabaseRef();
        initializeDatabaseRefListeners();
    }

    /**
     * Returns the singleton instance of FirebaseCRUD.
     * @return The FirebaseCRUD instance
     */
    public static FirebaseCRUD getInstance(Context context) {
        if (instance == null) {
            instance = new FirebaseCRUD(context);
        }
        return instance;
    }

    /**
     * Initializes Firebase database references for users, jobs, and applications
     */
    private void initializeDatabaseRef() {
        this.usersRef = this.database.getReference("users");
        this.jobsRef = this.database.getReference("jobs");
        this.appsRef = this.database.getReference("applications");
    }

    /**
     * Adds a user to the system
     * @param user The User to be added
     */
    public void addUser(User user){
        String userId = usersRef.push().getKey();
        if (userId != null) {
            usersRef.child(userId).setValue(user);
        }
    }

    /**
     * Adds a validated job to the system
     * @param job The Job object containing category, hours, and other details
     * @throws IllegalArgumentException if required fields are missing/invalid
     */
    public void addJob(Job job) throws IllegalArgumentException {
        // Validate category exists
        if (job.getJobCategory() == null || !isValidCategory(job.getJobCategory())) {
            throw new IllegalArgumentException("Invalid job category");
        }

        // Validate hours
        if (job.getJobHours() == null || job.getJobHours().isEmpty()) {
            throw new IllegalArgumentException("Job hours are required");
        }

        String jobId = jobsRef.push().getKey();
        job.setJobKey(jobId);
        if (jobId != null) {
            jobsRef.child(jobId).setValue(job)
                    .addOnSuccessListener(aVoid -> Log.d("FirebaseCRUD", "Job added successfully"))
                    .addOnFailureListener(e -> Log.e("FirebaseCRUD", "Failed to add job", e));
        }
    }

    /**
     * Validates if a category exists in predefined list
     * @param category The category to validate
     * @return true if valid, false otherwise
     */
    private boolean isValidCategory(String category) {
        for (String validCat : JOB_CATEGORIES) {
            if (validCat.equalsIgnoreCase(category)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Adds a job application to the system
     * @param application The Job to be added
     */
    public void addApplication(Application application){
        String appsId = appsRef.push().getKey();
        if (appsId != null) {
            appsRef.child(appsId).setValue(application);
        }
    }

    /**
     * Initializes users, jobs, and applications listeners from database
     */
    private void initializeDatabaseRefListeners() {
        this.setUserListener();
        this.setJobListener();
        this.setApplicationListener();
    }

    /**
     * Sets a listener for job data changes in the database.
     */
    private void setJobListener() {
        this.jobsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot userSnapshot : snapshot.getChildren()) {
                    jobsList = userSnapshot.getValue(Job.class);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {}
        });
    }

    /**
     * Sets a listener for job application data changes in the database.
     */
    private void setApplicationListener() {
        this.appsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot userSnapshot : snapshot.getChildren()) {
                    appsList = userSnapshot.getValue(Application.class);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {}
        });
    }

    /**
     * Sets a listener for user data changes in the database.
     */
    protected void setUserListener() {
        this.usersRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot userSnapshot : snapshot.getChildren()) {
                    usersList = userSnapshot.getValue(User.class);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {}
        });
    }

    /**
     * Find a User object by email address
     * @param email The email address corresponding to the user
     * @return A Task that resolves to the User object if found, or null otherwise.
     */
    public Task<User> findUserByEmail(String email) {
        TaskCompletionSource<User> taskCompletionSource = new TaskCompletionSource<>();
        usersRef.orderByChild("email").equalTo(email).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for (DataSnapshot userSnapshot : snapshot.getChildren()) {
                        String first = userSnapshot.child("firstName").getValue(String.class);
                        String last = userSnapshot.child("lastName").getValue(String.class);
                        String foundEmail = userSnapshot.child("email").getValue(String.class);
                        String password = userSnapshot.child("password").getValue(String.class);
                        String role = userSnapshot.child("role").getValue(String.class);

                        User user = UserRoleFactory.createUser(first,last, foundEmail, password, role);
                        taskCompletionSource.setResult(user);
                        return;
                    }
                }
                taskCompletionSource.setResult(null);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                taskCompletionSource.setException(error.toException());
            }
        });
        return taskCompletionSource.getTask();
    }

    /**
     * Find a User object by email address, and change password
     * @param email The email address corresponding to the user
     * @param newPass The new password to be change
     */
    public void changePassword(String email, String newPass){
        findUserByEmail(email).addOnSuccessListener(findUser -> {
            usersRef.orderByChild("email").equalTo(email)
                    .addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            for (DataSnapshot userSnapshot : snapshot.getChildren()) {
                                userSnapshot.getRef().child("password").setValue(newPass);
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            Log.e("FirebaseError", "Database error: " + error.getMessage());
                        }
                    });
        });
    }

    /**
     * return all of the job posted in the database.
     * @return A Task that resolves to a list of job from the database, or null otherwise.
     */
    public Task<ArrayList<Job>> getJobList() {
        TaskCompletionSource<ArrayList<Job>> taskCompletionSource = new TaskCompletionSource<>();

        jobsRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                ArrayList<Job> jobList = new ArrayList<>();
                DataSnapshot dataSnapshot = task.getResult();

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Job job = snapshot.getValue(Job.class);
                    if (job != null) {
                        jobList.add(job);
                    }
                }
                taskCompletionSource.setResult(jobList);
            } else {
                taskCompletionSource.setException(task.getException());
            }
        });
        return taskCompletionSource.getTask();
    }

    /**
     * update the user's job filter and criteria
     * @param user the user object to be updated
     * @param filterType the job filter type
     * @param filterCriteria the job filter criteria
     */
    public void updateJobFilter(User user, String filterType, String filterCriteria) {
        findUserByEmail(user.getEmail()).addOnSuccessListener(findUser -> {
            usersRef.orderByChild("email").equalTo(user.getEmail())
                    .addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            for (DataSnapshot userSnapshot : snapshot.getChildren()) {
                                userSnapshot.getRef().child("jobFilter").setValue(filterType);
                                userSnapshot.getRef().child("jobFilterCriteria").setValue(filterCriteria);
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            Log.e("FirebaseError", "Database error: " + error.getMessage());
                        }
                    });
        });
    }

    /**
     * return all of the job application posted in the database.
     * @return A Task that resolves to a list of job application from the database, or null otherwise.
     */
    public Task<ArrayList<Application>> getApplicationList() {
        TaskCompletionSource<ArrayList<Application>> taskCompletionSource = new TaskCompletionSource<>();

        appsRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                ArrayList<Application> appList = new ArrayList<>();
                DataSnapshot dataSnapshot = task.getResult();

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Application application = snapshot.getValue(Application.class);
                    if (application != null) {
                        application.setKey(snapshot.getKey()); // Set the key for each application
                        appList.add(application);
                    }
                }
                taskCompletionSource.setResult(appList);
            } else {
                taskCompletionSource.setException(task.getException());
            }
        });
        return taskCompletionSource.getTask();
    }

    /**
     * update the corresponding application status.
     * @param applicationId the applicant key id in the database
     * @param status the status of the application
     */
    public void updateApplicationStatus(String applicationId, String status) {
        DatabaseReference applicationRef = appsRef.child(applicationId);
        Map<String, Object> updates = new HashMap<>();
        updates.put("status", status);
        updates.put("isDecided", true);
        applicationRef.updateChildren(updates)
                .addOnSuccessListener(aVoid -> Log.d("FirebaseCRUD", "Status updated"))
                .addOnFailureListener(e -> Log.e("FirebaseCRUD", "Update failed", e));
    }

    /**
     * update the corresponding job status.
     * @param jobId the job key id in the database
     * @param status the status of the job
     */
    public void updateJobStatus(String jobId, String status) {
        DatabaseReference applicationRef = jobsRef.child(jobId);
        Map<String, Object> updates = new HashMap<>();
        updates.put("jobStatus", status);
        applicationRef.updateChildren(updates)
                .addOnSuccessListener(aVoid -> Log.d("FirebaseCRUD", "Status updated"))
                .addOnFailureListener(e -> Log.e("FirebaseCRUD", "Update failed", e));
    }

    /**
     * Find a Job object by its unique key
     * @param id The email address corresponding to the user
     * @return A Task that resolves to the Job object if found, or null otherwise.
     */
    public Task<Job> getJobByID(String id){
        TaskCompletionSource<Job> taskCompletionSource = new TaskCompletionSource<>();
        jobsRef.child(id).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    Job job = snapshot.getValue(Job.class);
                    taskCompletionSource.setResult(job);
                } else {
                    taskCompletionSource.setResult(null);
                }
            }
            @Override
            public void onCancelled(DatabaseError error) {}
        });
        return taskCompletionSource.getTask();
    }

    /**
     * Retrieves the application status for a specific job.
     *
     * This method queries the database to find the application associated with the given
     * job key and retrieves the status of the application.
     *
     * @param jobKey The unique key identifying the job.
     * @return A Task containing the application status as a String, or null if no application is found.
     */
    public Task<String> getStatusByJob(String jobKey) {
        TaskCompletionSource<String> taskCompletionSource = new TaskCompletionSource<>();

        jobsRef.orderByChild("jobKey").equalTo(jobKey).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (!snapshot.exists()) {
                    taskCompletionSource.setResult(null);
                    return;
                }

                DataSnapshot appSnapshot = snapshot.getChildren().iterator().next();
                String appStatus = appSnapshot.child("jobStatus").getValue(String.class);
                taskCompletionSource.setResult(appStatus);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                taskCompletionSource.setException(error.toException());
            }
        });

        return taskCompletionSource.getTask();
    }


}