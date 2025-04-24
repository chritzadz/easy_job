package com.example.template.view;

import static java.lang.Integer.parseInt;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.template.Firebase.FirebaseUseCase;
import com.example.template.R;
import com.example.template.model.Job;
import com.example.template.util.LocationHelper;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;


public class JobDetailsActivity extends AppCompatActivity implements OnMapReadyCallback {
    TextView jobName;
    TextView jobHours;
    TextView jobPayRate;
    TextView jobTotal;
    TextView jobDesc;
    TextView jobLocation;
    ImageView backButton;
    MapView googleMap;
    Job job = null;
    private GoogleMap mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_job_details);

        FirebaseUseCase.set(this);

        retrieveIntent();

        setContents();
        setEventListeners();

        Bundle mapViewBundle = null;
        if (savedInstanceState != null) {
            mapViewBundle = savedInstanceState.getBundle(getString(R.string.google_location_key));
        }
        googleMap.onCreate(mapViewBundle);
        googleMap.getMapAsync(this);
    }

    private void retrieveIntent() {
        this.job = (Job) getIntent().getSerializableExtra("clickedJob");
    }

    private void setEventListeners() {
        backButton.setOnClickListener(v -> move2Dashboard());
    }

    private void move2Dashboard() {
        Intent intent = new Intent(this, DashboardActivity.class);
        startActivity(intent);
    }

    private void setContents() {
        jobName = findViewById(R.id.jobNameTextViewJobDetails);
        jobHours = findViewById(R.id.totalTimeTextViewJobDetails);
        jobPayRate = findViewById(R.id.hourlyRateTextViewJobDetails);
        jobTotal = findViewById(R.id.totalPayTextViewJobDetails);
        jobDesc = findViewById(R.id.jobDescContentTextViewJobDetails);
        jobLocation = findViewById(R.id.jobLocationTextViewJobDetails);
        backButton = findViewById(R.id.backButtonImageViewManageJobDetails);
        googleMap = findViewById(R.id.jobLocationMapViewJobDetails);

        jobName.setText(job.getJobTitle());
        jobHours.setText( job.getJobHours()+" hrs");
        jobPayRate.setText("$"+job.getJobPay()+"/hr");
        int total = parseInt(job.getJobPay()) * parseInt(job.getJobHours());
        jobTotal.setText("$"+total);
        jobLocation.setText(job.getJobLocation());

    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mMap = googleMap;

        LatLng location = LocationHelper.getLatLngFromCity(job.getJobLocation(), this);
        mMap.addMarker(new MarkerOptions().position(location).title(job.getJobTitle()));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(location, 10.0f));
    }
}
