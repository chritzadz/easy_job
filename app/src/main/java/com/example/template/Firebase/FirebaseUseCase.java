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

import java.util.ArrayList;
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

    public static void addJob(Job job){
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

    public static List<Job> getJobs(){
        updateEntities();
        return jobList;
    }

    public static List<Job> getJobsFromOthers(String currUserEmail){
        updateEntities();
        ArrayList<Job> tempList = new ArrayList<>();
        for(Job j: jobList){
            if (!j.getJobEmail().equals(currUserEmail)){
                tempList.add(j);
            }
        }
        return tempList;
    }

    public static List<Job> getJobsFromSelf(String currUserEmail){
        updateEntities();
        ArrayList<Job> tempList = new ArrayList<>();
        for(Job j: jobList){
            if (j.getJobEmail().equals(currUserEmail)){
                tempList.add(j);
            }
        }
        return tempList;
    }

    /**
     * @param searchInput is used for the user search input (filtered category and job title)
     * */
    public static List<Job> getJobsByCriteria(String currUserEmail, String searchInput){
        updateEntities();
        ArrayList<Job> tempList = new ArrayList<>();
        if (searchInput.isEmpty() || searchInput == null){
            return getJobsFromOthers(currUserEmail);
        }
        for(Job j: jobList){
            if (!j.getJobEmail().equals(currUserEmail) && j.getJobTitle().contains(searchInput) || j.getJobCategory().contains(searchInput)){
                tempList.add(j);
            }
        }
        return tempList;
    }

    public static void updateJob(String jobKey, String jobTitle, String jobDesc, String jobLocation, String jobCategory, String jobHours, String jobPay, OnJobUpdateComplete callback) {
        final int[] completed = {0};
        OnProfileUpdateComplete internalCallback = () -> {
            completed[0]++;
            if (completed[0] == 6) {
                callback.onComplete();
            }
            updateEntities();
        };

        database.modifyJobTitle(jobKey, jobTitle, internalCallback);
        database.modifyJobDesc(jobKey, jobDesc, internalCallback);
        database.modifyJobLocation(jobKey, jobLocation, internalCallback);
        database.modifyJobCategory(jobKey, jobCategory, internalCallback);
        database.modifyJobHours(jobKey, jobHours, internalCallback);
        database.modifyJobPay(jobKey, jobPay, internalCallback);
    }

    public interface OnRoleSwitchComplete {
        void onComplete();
    }

    public interface OnProfileUpdateComplete {
        void onComplete();
    }

    public interface OnJobUpdateComplete {
        void onComplete();
    }
}