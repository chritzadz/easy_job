package com.example.template.Firebase;

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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import java.util.Map;

public class FirebaseCRUD {
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
    public List<User> userList;
    public List<Job> jobList;
    public List<Application> appList;
    private static FirebaseCRUD instance;

    private FirebaseCRUD(Context context) {
        this.database = FirebaseDatabase.getInstance(context.getString(R.string.firebase_ref));
        initializeDatabaseRef();
        initializeDatabaseRefListeners();
    }

    public static FirebaseCRUD getInstance(Context context) {
        if (instance == null) {
            instance = new FirebaseCRUD(context);
        }
        return instance;
    }
    private void initializeDatabaseRef() {
        this.usersRef = this.database.getReference("users");
        this.jobsRef = this.database.getReference("jobs");
        this.appsRef = this.database.getReference("applications");
    }
    private void initializeDatabaseRefListeners() {
        this.setUserListener();
        this.setJobListener();
        this.setApplicationListener();
    }
    private void setJobListener() {
        this.jobsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                jobList.clear();
                for (DataSnapshot userSnapshot : snapshot.getChildren()) {
                    Job job = userSnapshot.getValue(Job.class);
                    if (job != null) {
                        jobList.add(job);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {}
        });
    }
    private void setApplicationListener() {
        this.appsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                appList.clear();
                for (DataSnapshot userSnapshot : snapshot.getChildren()) {
                    Application application = userSnapshot.getValue(Application.class);
                    if (application != null) {
                        appList.add(application);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {}
        });
    }
    protected void setUserListener() {
        this.usersRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                userList.clear();
                for (DataSnapshot userSnapshot : snapshot.getChildren()) {
                    User user = userSnapshot.getValue(User.class);
                    if (user != null) {
                        userList.add(user);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {}
        });
    }
    public void addUser(User user){
        String userId = usersRef.push().getKey();
        if (userId != null) {
            usersRef.child(userId).setValue(user);
        }
    }
    public void addJob(Job job) {
        String jobId = jobsRef.push().getKey();
        job.setJobKey(jobId);
        if (jobId != null) {
            jobsRef.child(jobId).setValue(job);
        }
    }
    public void addApplication(Application application){
        String appsId = appsRef.push().getKey();
        if (appsId != null) {
            appsRef.child(appsId).setValue(application);
        }
    }
}