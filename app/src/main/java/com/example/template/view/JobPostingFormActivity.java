package com.example.template.view;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.template.R;
import com.example.template.controller.CredentialValidator;
import com.example.template.model.CurrentUser;
import com.example.template.model.Job;
import com.example.template.util.AccessTokenListener;
import com.example.template.util.FirebaseCRUD;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.messaging.FirebaseMessaging;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
/**
 * @file JobPostingFormActivity.java
 * @author: -
 * @description: Activity that prompts user for job necessary details be posted.
 * after clicking submit, job will be added in database.
 */
public class JobPostingFormActivity extends AppCompatActivity {
    private static final String PUSH_NOTIFICATION_ENDPOINT = "https://fcm.googleapis.com/v1/projects/group10-quickcash/messages:send";
    private RequestQueue requestQueue;
    private EditText jobName;
    private EditText jobDescription;
    private EditText jobLocation;
    private Button postJobButton;
    private Spinner categorySpinner;
    private EditText jobPay;
    private EditText jobHours;
    FirebaseCRUD crud = FirebaseCRUD.getInstance(this);

    public static final String CREDENTIALS_FILE_PATH = "key.json";

    /**
     * Initializes activity and set user interface
     * @param savedInstanceState The saved instance state bundle
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_job_posting_form);
        requestQueue = Volley.newRequestQueue(this);
        FirebaseMessaging.getInstance().subscribeToTopic("jobs");

        setContents();
        initSpinner();
        initNotifications();
        setEventListeners();
    }

    private void initNotifications() {
        requestQueue = Volley.newRequestQueue(this);
        FirebaseMessaging.getInstance().subscribeToTopic("jobs");
        Log.d("PushNotifications", "request queue initialized");
    }

    /**
     * Initializes content components of the interface
     */
    private void setContents() {
        jobName = findViewById(R.id.jobName);
        jobDescription = findViewById(R.id.jobDescription);
        jobLocation = findViewById(R.id.jobLocation);
        postJobButton = findViewById(R.id.postJobButton);
        categorySpinner = findViewById(R.id.jobCategory);
        jobPay = findViewById(R.id.jobPay);
        jobHours = findViewById(R.id.etJobHours);
    }

    /**
     * Initializes the category spinner with predefined values
     */
    private void initSpinner() {
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.job_categories, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        categorySpinner.setAdapter(adapter);
    }

    /**
     * Initializes each components listeners
     */
    private void setEventListeners() {
        postJobButton.setOnClickListener(view -> {
            getAccessToken(this, new AccessTokenListener() {
                @Override
                public void onAccessTokenReceived(String token) {
                    Log.d("PushNotifications", "access token recieved: " + token);
                    onPostJobButtonClick(view, token);
                }

                @Override
                public void onAccessTokenError(Exception exception) {
                    Toast.makeText(JobPostingFormActivity.this, exception.getMessage(), Toast.LENGTH_LONG).show();
                }
            });
        });
    }

    /**
     * Handles the click event for post job button
     */
    private void onPostJobButtonClick(View view, String authToken) {
        Log.d("PushNotifications", "post job button clicked");
        String name = jobName.getText().toString().trim();
        String description = jobDescription.getText().toString().trim();
        String location = jobLocation.getText().toString().trim();
        String category = categorySpinner.getSelectedItem().toString().trim();
        String payAmount = jobPay.getText().toString().trim();
        String hours = jobHours.getText().toString().trim();

        if (!CredentialValidator.isValidField(name)) {
            Toast.makeText(JobPostingFormActivity.this, "Job name is empty", Toast.LENGTH_LONG).show();
        } else if (!CredentialValidator.isValidField(description)) {
            Toast.makeText(JobPostingFormActivity.this, "Job description is empty", Toast.LENGTH_LONG).show();
        } else if (!CredentialValidator.isValidField(location)) {
            Toast.makeText(JobPostingFormActivity.this, "Job location is empty", Toast.LENGTH_LONG).show();
        } else if (!CredentialValidator.isValidField(payAmount)) {
            Toast.makeText(JobPostingFormActivity.this, "Job pay amount is empty", Toast.LENGTH_LONG).show();
        } else if (!CredentialValidator.isValidField(hours)) {
            Toast.makeText(JobPostingFormActivity.this, "Job hours are required", Toast.LENGTH_LONG).show();
        } else {
            CurrentUser currUser = CurrentUser.getInstance();
            String email = currUser.getUser().getEmail();
            saveJob(name, description, location, category, email, payAmount, hours, authToken);
            //Toast.makeText(JobPostingFormActivity.this, "Job Successfully Posted", Toast.LENGTH_LONG).show();
            move2Dashboard();
        }
    }

    private void sendNotification(String token, Job j) {
        try {
            JSONObject notificationJSONBody = new JSONObject();
            notificationJSONBody.put("title", "New Job Created");
            notificationJSONBody.put("body", "A new job is created in your city.");

            JSONObject dataJSONBody = new JSONObject();
            dataJSONBody.put("jobLocation", j.getJobLocation());
            dataJSONBody.put("jobName", j.getJobName());

            JSONObject messageJSONBody = new JSONObject();
            messageJSONBody.put("topic", "jobs");
            messageJSONBody.put("notification", notificationJSONBody);
            messageJSONBody.put("data", dataJSONBody);

            JSONObject pushNotificationJSONBody = new JSONObject();
            pushNotificationJSONBody.put("message", messageJSONBody);

            // Log the complete JSON payload for debugging
            Log.d("NotificationBody", "JSON Body: " + pushNotificationJSONBody.toString());

            // Create the request
            JsonObjectRequest request = new JsonObjectRequest(
                    Request.Method.POST,
                    PUSH_NOTIFICATION_ENDPOINT,
                    pushNotificationJSONBody,
                    response -> {
                        Log.d("NotificationResponse", "Response: " + response.toString());
                        Toast.makeText(this, "Notification Sent Successfully", Toast.LENGTH_SHORT).show();
                    },
                    error -> {
                        Log.e("NotificationError", "Error Response: " + error.toString());
                        if (error.networkResponse != null) {
                            Log.e("NotificationError", "Status Code: " + error.networkResponse.statusCode);
                            Log.e("NotificationError", "Error Data: " + new String(error.networkResponse.data));
                        }
                        Toast.makeText(this, "Failed to Send Notification", Toast.LENGTH_SHORT).show();
                        error.printStackTrace();
                    }) {

                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    Map<String, String> headers = new HashMap<>();
                    headers.put("Content-Type", "application/json; charset=UTF-8");
                    headers.put("Authorization", "Bearer " + token);
                    Log.d("NotificationHeaders", "Headers: " + headers.toString());
                    return headers;
                }
            };

            // Add the request to the queue
            requestQueue.add(request);
        } catch (JSONException e) {
            Log.e("NotificationJSONException", "Error creating notification JSON: " + e.getMessage());
            Toast.makeText(this, "Error creating notification payload", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    /**
     * Handle in adding job to database, passing to FirebaseCRUD
     * @param name The job title
     * @param desc The job description
     * @param location The job location
     * @param category The job category
     * @param email The job related email
     * @param payAmount The job salary
     * @param hours The estimated job hours
     */
    private void saveJob(String name, String desc, String location, String category,
                         String email, String payAmount, String hours, String authToken) {
        Job curJob = new Job();
        curJob.setJobName(name);
        curJob.setJobDescription(desc);
        curJob.setJobLocation(location);
        curJob.setJobCategory(category);
        curJob.setJobEmail(email);
        curJob.setJobPay(payAmount);
        curJob.setJobHours(hours);

        crud.addJob(curJob);
        String jobID = curJob.getJobKey();
        Log.d("PushNotifications", "job saved");
        sendNotification(authToken, location, jobID);
    }

    /**
     * Navigate to the DashboardActivity
     */
    private void move2Dashboard() {
        Intent intent = new Intent(JobPostingFormActivity.this, DashboardActivity.class);
        startActivity(intent);
    }

    private void getAccessToken(Context context, AccessTokenListener listener) {
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        executorService.execute(() -> {
            try {
                InputStream serviceAccountStream = context.getAssets().open(CREDENTIALS_FILE_PATH);
                GoogleCredentials googleCredentials = GoogleCredentials.fromStream(serviceAccountStream)
                        .createScoped(Collections.singletonList("https://www.googleapis.com/auth/firebase.messaging"));

                googleCredentials.refreshIfExpired();
                String token = googleCredentials.getAccessToken().getTokenValue();
                listener.onAccessTokenReceived(token);
            } catch (IOException e) {
                listener.onAccessTokenError(e);
            }
        });
        executorService.shutdown();
    }

    private void sendNotification(String authToken, String location, String jobID) {
        Log.d("PushNotifications", "sendNotification method accessed");
        try {
            // Build the notification payload
            JSONObject notificationJSONBody = new JSONObject();
            notificationJSONBody.put("title", "New Job Created");
            notificationJSONBody.put("body", "A new job is created in your city.");

            JSONObject dataJSONBody = new JSONObject();
            dataJSONBody.put("jobLocation", location);
            dataJSONBody.put("job_id", jobID);

            JSONObject messageJSONBody = new JSONObject();
            messageJSONBody.put("topic", "jobs");
            messageJSONBody.put("notification", notificationJSONBody);
            messageJSONBody.put("data", dataJSONBody);

            JSONObject pushNotificationJSONBody = new JSONObject();
            pushNotificationJSONBody.put("message", messageJSONBody);

            // Log the complete JSON payload for debugging
            Log.d("NotificationBody", "JSON Body: " + pushNotificationJSONBody.toString());

            // Create the request
            JsonObjectRequest request = new JsonObjectRequest(
                    Request.Method.POST,
                    PUSH_NOTIFICATION_ENDPOINT,
                    pushNotificationJSONBody,
                    response -> {
                        Log.d("NotificationResponse", "Response: " + response.toString());
                        Toast.makeText(this, "Notification Sent Successfully", Toast.LENGTH_SHORT).show();
                    },
                    error -> {
                        Log.e("NotificationError", "Error Response: " + error.toString());
                        if (error.networkResponse != null) {
                            Log.e("NotificationError", "Status Code: " + error.networkResponse.statusCode);
                            Log.e("NotificationError", "Error Data: " + new String(error.networkResponse.data));
                        }
                        Toast.makeText(this, "Failed to Send Notification", Toast.LENGTH_SHORT).show();
                        error.printStackTrace();
                    }) {
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    Map<String, String> headers = new HashMap<>();
                    headers.put("Content-Type", "application/json; charset=UTF-8");
                    headers.put("Authorization", "Bearer " + authToken);
                    Log.d("NotificationHeaders", "Headers: " + headers.toString());
                    return headers;
                }
            };

            // Add the request to the queue
            requestQueue.add(request);
            Log.d("PushNotifications", "request added");
        } catch (JSONException e) {
            Log.e("NotificationJSONException", "Error creating notification JSON: " + e.getMessage());
            Toast.makeText(this, "Error creating notification payload", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }

    }
}