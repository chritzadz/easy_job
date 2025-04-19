package com.example.template.Firebase;

import android.content.Context;
import android.util.Log;

import androidx.room.Query;

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

    public FirebaseUseCase() { /* Nothing */ }

    public static void set(Context context){
        Log.e("set Databse", "HELLO");
        database = FirebaseCRUD.getInstance(context);
        updateEntities();
    }

    private static void updateEntities() {
        jobList = FirebaseCRUD.jobList;
        appList = FirebaseCRUD.appList;
        userList = FirebaseCRUD.userList;
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
    public static void switchRole(String email, OnRoleSwitchComplete callback) {
        database.modifyUserRole(email, callback);
        updateEntities();
    }

    public static void updateProfile(String email, String firstName, String lastName, OnProfileUpdateComplete callback) {
        final int[] completed = {0};
        OnProfileUpdateComplete internalCallback = () -> {
            completed[0]++;
            if (completed[0] == 2) { //here items is according to the number of changes
                callback.onComplete();
            }
            updateEntities();
        };

        database.modifyUserFirstName(email, firstName, internalCallback);
        database.modifyUserLastName(email, lastName, internalCallback);
    }

    public interface OnRoleSwitchComplete {
        void onComplete();
    }

    public interface OnProfileUpdateComplete {
        void onComplete();
    }
}