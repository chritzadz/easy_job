package com.example.template.view;

import androidx.fragment.app.FragmentActivity;

import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.example.template.R;
import com.example.template.model.Job;
import com.example.template.util.LocationHelper;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.Dash;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.example.template.databinding.ActivityMapsBinding;

import java.io.IOException;
import java.util.List;

/**
 * @file MapActivity.java
 * @author: -
 * @description: Activity to show location on a specific location (by providing latitutde and longitude)
 */

public class MapActivity extends FragmentActivity implements OnMapReadyCallback {
    private GoogleMap mMap;
    private ActivityMapsBinding binding;
    private Job job = null;
    private Button backButton;
    private String from;

    /**
     * Initializes the activity, retrieves job information from the intent,
     * and set map components.
     * @param savedInstanceState The saved instance state bundle
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getJobIntent();

        binding = ActivityMapsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setContents();
        setContentListeners();

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    /**
     * Initializes each components listeners
     */
    private void setContentListeners() {
        backButton.setOnClickListener(this::move2JobDetailsActivity);
    }

    /**
     * Navigate to the DetailsActivity
     */
    private void move2JobDetailsActivity(View view) {
        if (from.equals("jobDetails")){
            Intent intent = new Intent(MapActivity.this, JobDetailsActivity.class);
            intent.putExtra("jobObj", job);
            startActivity(intent);
        }
        else if(from.equals("JobActivity")){
            Intent intent = new Intent(MapActivity.this, JobActivity.class);
            startActivity(intent);
        }
        else if(from.equals("Dashboard")){
            Intent intent = new Intent(MapActivity.this, DashboardActivity.class);
            startActivity(intent);
        }




    }

    /**
     * Initializes content components of the interface
     */
    private void setContents() {
        backButton = findViewById(R.id.backButtonFromMap);
    }

    /**
     * Retrieve job object passed from intent of previous page
     */
    private void getJobIntent() {
        Intent intent = getIntent();
        job = (Job) intent.getSerializableExtra("jobObj");
        from = intent.getStringExtra("from");
    }

    /**
     * Called when the map is ready to be used and add a marker for the job's location on the map.
     * @param googleMap The GoogleMap instance
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        LatLng location = getLatLngFromCity(job.getJobLocation());
        mMap.addMarker(new MarkerOptions().position(location).title(job.getJobName()));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(location, 10.0f));
    }

    /**
     * Retrieves the latitude and longitude for a given city name using the Geocoder
     * @param cityName The name of the city
     * @return A LatLng object containing the latitude and longitude of the city
     */
    public LatLng getLatLngFromCity(String cityName) {
        Geocoder geocoder = new Geocoder(this);
        try {
            List<Address> addresses = geocoder.getFromLocationName(cityName, 1);
            if (!addresses.isEmpty()) {
                double latitude = addresses.get(0).getLatitude();
                double longitude = addresses.get(0).getLongitude();
                return new LatLng(latitude, longitude);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
