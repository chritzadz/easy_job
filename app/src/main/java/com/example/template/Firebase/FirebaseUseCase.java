package com.example.template.Firebase;

import android.content.Context;

import com.example.template.model.Application;
import com.example.template.model.Job;
import com.example.template.model.User;
import com.example.template.status.Status;
import com.example.template.status.SuccessStatus;
import com.example.template.status.UserNotExistStatus;
import com.example.template.status.UserPasswordMismatchStatus;

import java.util.List;

public class FirebaseUseCase {
    private static FirebaseCRUD database;
    private static List<Job> jobList = null;
    private static List<Application> appList = null;
    private static List<User> userList = null;

    public FirebaseUseCase() {}

    public static void set(Context context){
        database = FirebaseCRUD.getInstance(context);
        updateEntities();
    }

    private static void updateEntities() {
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
    public static void addUser(User user){
        database.addUser(user);
    }
    public static User findUserByEmail(String targetEmail){
        updateEntities();
        for (User u: userList){
            if (u.getEmail().equals(targetEmail)){
                return u;
            }
        }
        return null;
    }

    public static Status checkUserExist(String email, String password){
        User user = findUserByEmail(email);
        if (user != null){
            if (user.getPassword().equals(password)){
                return new SuccessStatus();
            }
            return new UserPasswordMismatchStatus();
        }
        else{
            return new UserNotExistStatus();
        }
    }
}