package com.example.template.Firebase;

import android.content.Context;

import com.example.template.model.Application;
import com.example.template.model.Job;
import com.example.template.model.User;

import java.util.List;

public class FirebaseUseCase {
    private FirebaseCRUD database;
    private List<Job> jobList = null;
    private List<Application> appList = null;
    private List<User> userList = null;

    public FirebaseUseCase(Context context) {
        database = FirebaseCRUD.getInstance(context);
        updateEntities();
    }

    private void updateEntities() {
        jobList = database.jobList;
        appList = database.appList;
        userList = database.userList;
    }

    public void addJob(Job job){
        database.addJob(job);
    }
    public void addApplication(Application application){
        database.addApplication(application);
    }
    public void addUser(User user){
        database.addUser(user);
    }
    public User findUserByEmail(String targetEmail){
        updateEntities();
        for (User u: userList){
            if (u.getEmail().equals(targetEmail)){
                return u;
            }
        }
        return null;
    }
}
