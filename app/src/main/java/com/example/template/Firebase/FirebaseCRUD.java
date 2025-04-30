package com.example.template.Firebase;

import android.content.Context;
import android.util.Log;
import androidx.annotation.NonNull;
import com.example.template.R;
import com.example.template.model.Application;
import com.example.template.model.User;
import com.example.template.model.Job;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class FirebaseCRUD {
    private final FirebaseDatabase database;
    private DatabaseReference usersRef;
    private DatabaseReference jobsRef;
    private DatabaseReference appsRef;
    public static List<User> userList = new ArrayList<>();
    public static List<Job> jobList = new ArrayList<>();
    public static List<Application> appList = new ArrayList<>();
    private static FirebaseCRUD instance;

    private FirebaseCRUD(Context context) {
        this.database = FirebaseDatabase.getInstance(context.getString(R.string.firebase_ref));

        Log.e("SET LITENER", "rye");
        initializeDatabaseRef();

    }

    public static FirebaseCRUD getInstance(Context context) {
        Log.e("GET INSTANCE", "");
        if (instance == null) {
            instance = new FirebaseCRUD(context);
        }
        return instance;
    }
    private void initializeDatabaseRef() {
        this.usersRef = this.database.getReference("users");
        this.jobsRef = this.database.getReference("jobs");
        this.appsRef = this.database.getReference("applications");
        Log.e("SET LITENER", "nito function");
        initializeDatabaseRefListeners();
    }
    public void initializeDatabaseRefListeners() {
        Log.e("SET LITENER", "set user listener");
        this.setUserListener();
        this.setJobListener();
        this.setApplicationListener();
    }
    private void setJobListener() {
        this.jobsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(userList != null) {
                    jobList.clear();

                    for (DataSnapshot userSnapshot : snapshot.getChildren()) {
                        Job job = userSnapshot.getValue(Job.class);
                        if (job != null) {
                            jobList.add(job);
                        }
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
                if(appList != null){
                    appList.clear();

                    for (DataSnapshot userSnapshot : snapshot.getChildren()) {
                        Application application = userSnapshot.getValue(Application.class);
                        if (application != null) {
                            appList.add(application);
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {}
        });
    }
    protected void setUserListener() {
        Log.e("here", userList.toString());
        this.usersRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Log.e("in data change", "test");
                if(userList != null) {
                    userList.clear();
                    for (DataSnapshot userSnapshot : snapshot.getChildren()) {
                        User user = userSnapshot.getValue(User.class);
                        if (user != null) {
                            userList.add(user);
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("Error", String.valueOf(error));
            }
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
        application.setAppKey(appsId);
        if (appsId != null) {
            appsRef.child(appsId).setValue(application);
        }
    }

    public void modifyUserRole(String email, FirebaseUseCase.OnRoleSwitchComplete callback){
        usersRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot userSnapshot : snapshot.getChildren()) {
                    String userEmail = userSnapshot.child("email").getValue(String.class);

                    if (userEmail != null && userEmail.equals(email)) {
                        String oldRole = userSnapshot.child("role").getValue(String.class);
                        assert oldRole != null;
                        String newRole = oldRole.equals("Employee")? "Employer": "Employee";
                        userSnapshot.getRef().child("role").setValue(newRole).addOnCompleteListener(task -> {
                            if (task.isSuccessful()) {
                                callback.onComplete();
                            }
                        });
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("Firebase", "Error: " + error.getMessage());
            }
        });
    }

    public void modifyUserFirstName(String email, String newFirstName, FirebaseUseCase.OnProfileUpdateComplete callback){
        Query query = usersRef.orderByChild("email").equalTo(email);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for (DataSnapshot userSnapshot : snapshot.getChildren()) {
                        String userId = userSnapshot.getKey();
                        usersRef.child(userId).child("firstName").setValue(newFirstName).addOnCompleteListener(task -> {
                            if (task.isSuccessful()) {
                                callback.onComplete();
                            }
                        });
                    }
                } else {
                    Log.d("FirebaseUpdate", "No user found with email: " + email);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("FirebaseUpdate", "Error updating first name", error.toException());
            }
        });
    }
    public void modifyUserLastName(String email, String newLastName, FirebaseUseCase.OnProfileUpdateComplete callback){
        Query query = usersRef.orderByChild("email").equalTo(email);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for (DataSnapshot userSnapshot : snapshot.getChildren()) {
                        String userId = userSnapshot.getKey();
                        usersRef.child(userId).child("lastName").setValue(newLastName).addOnCompleteListener(task -> {
                            if (task.isSuccessful()) {
                                callback.onComplete();
                            }
                        });
                    }
                } else {
                    Log.d("FirebaseUpdate", "No user found with email: " + email);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("FirebaseUpdate", "Error updating last name", error.toException());
            }
        });
    }

    public void modifyJobTitle(String jobKey, String jobTitle, FirebaseUseCase.OnProfileUpdateComplete callback) {
        Query query = jobsRef.orderByChild("jobKey").equalTo(jobKey);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for (DataSnapshot userSnapshot : snapshot.getChildren()) {
                        String jobID = userSnapshot.getKey();
                        jobsRef.child(jobID).child("jobTitle").setValue(jobTitle).addOnCompleteListener(task -> {
                            if (task.isSuccessful()) {
                                callback.onComplete();
                            }
                        });
                    }
                } else {
                    Log.d("FirebaseUpdate", "No user found with email: " + jobTitle);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("FirebaseUpdate", "Error updating job title", error.toException());
            }
        });
    }

    public void modifyJobDesc(String jobKey, String jobDesc, FirebaseUseCase.OnProfileUpdateComplete callback) {
        Query query = jobsRef.orderByChild("jobKey").equalTo(jobKey);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for (DataSnapshot userSnapshot : snapshot.getChildren()) {
                        String jobID = userSnapshot.getKey();
                        jobsRef.child(jobID).child("jobDescription").setValue(jobDesc).addOnCompleteListener(task -> {
                            if (task.isSuccessful()) {
                                callback.onComplete();
                            }
                        });
                    }
                } else {
                    Log.d("FirebaseUpdate", "No user found with email: " + jobDesc);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("FirebaseUpdate", "Error updating job title", error.toException());
            }
        });
    }

    public void modifyJobLocation(String jobKey, String jobLocation, FirebaseUseCase.OnProfileUpdateComplete callback) {
        Query query = jobsRef.orderByChild("jobKey").equalTo(jobKey);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for (DataSnapshot userSnapshot : snapshot.getChildren()) {
                        String jobID = userSnapshot.getKey();
                        jobsRef.child(jobID).child("jobLocation").setValue(jobLocation).addOnCompleteListener(task -> {
                            if (task.isSuccessful()) {
                                callback.onComplete();
                            }
                        });
                    }
                } else {
                    Log.d("FirebaseUpdate", "No user found with email: " + jobLocation);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("FirebaseUpdate", "Error updating job title", error.toException());
            }
        });
    }

    public void modifyJobCategory(String jobKey, String jobCategory, FirebaseUseCase.OnProfileUpdateComplete callback) {
        Query query = jobsRef.orderByChild("jobKey").equalTo(jobKey);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for (DataSnapshot userSnapshot : snapshot.getChildren()) {
                        String jobID = userSnapshot.getKey();
                        jobsRef.child(jobID).child("jobCategory").setValue(jobCategory).addOnCompleteListener(task -> {
                            if (task.isSuccessful()) {
                                callback.onComplete();
                            }
                        });
                    }
                } else {
                    Log.d("FirebaseUpdate", "No user found with email: " + jobCategory);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("FirebaseUpdate", "Error updating job title", error.toException());
            }
        });
    }

    public void modifyJobHours(String jobKey, String jobHours, FirebaseUseCase.OnProfileUpdateComplete callback) {
        Query query = jobsRef.orderByChild("jobKey").equalTo(jobKey);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for (DataSnapshot userSnapshot : snapshot.getChildren()) {
                        String jobID = userSnapshot.getKey();
                        jobsRef.child(jobID).child("jobHours").setValue(jobHours).addOnCompleteListener(task -> {
                            if (task.isSuccessful()) {
                                callback.onComplete();
                            }
                        });
                    }
                } else {
                    Log.d("FirebaseUpdate", "No user found with email: " + jobHours);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("FirebaseUpdate", "Error updating job title", error.toException());
            }
        });
    }

    public void modifyJobPay(String jobKey, String jobPay, FirebaseUseCase.OnProfileUpdateComplete callback) {
        Query query = jobsRef.orderByChild("jobKey").equalTo(jobKey);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for (DataSnapshot userSnapshot : snapshot.getChildren()) {
                        String jobID = userSnapshot.getKey();
                        jobsRef.child(jobID).child("jobPay").setValue(jobPay).addOnCompleteListener(task -> {
                            if (task.isSuccessful()) {
                                callback.onComplete();
                            }
                        });
                    }
                } else {
                    Log.d("FirebaseUpdate", "No user found with email: " + jobPay);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("FirebaseUpdate", "Error updating job title", error.toException());
            }
        });
    }
}