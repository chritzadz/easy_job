package com.example.template.util;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import com.google.android.gms.maps.model.LatLng;

import java.io.IOException;
import java.util.List;


/**
 * @file LocationHelper.java
 * @author: -
 * @description: The LocationHelper utility class manage the system location.
 */
public class LocationHelper {
    private final Context context;
    private final LocationManager locationManager;
    private LocationListener locationListener;
    private String city;

    /**
     * Callback interface for receiving city updates.
     */
    public interface LocationCallback {
        void onCityUpdated(String cityName, double latitude, double longitude);
    }

    /**
     * Constructs a LocationHelper instance with a callback for city updates.
     * @param context The context to access system services
     * @param callback The callback class to notify city updates
     */
    public LocationHelper(Context context, LocationCallback callback) {
        this.context = context;
        this.locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(@NonNull Location location) {
                double latitude = location.getLatitude();
                double longitude = location.getLongitude();

                Geocoder geocoder = new Geocoder(context.getApplicationContext());
                try {
                    List<Address> addresses = geocoder.getFromLocation(latitude, longitude,1);
                    if (!addresses.isEmpty()) {
                        city = addresses.get(0).getLocality();
                        if (callback != null) {
                            callback.onCityUpdated(city, latitude, longitude);
                        }
                    }
                }catch (IOException e){
                    e.printStackTrace();
                }
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {
            }
            @Override
            public void onProviderEnabled(@NonNull String provider) {
            }
            @Override
            public void onProviderDisabled(@NonNull String provider) {
            }
        };
        requestLocationUpdates();
    }

    /**
     * Check location permission and requests location updates from the LocationManager
     */
    private void requestLocationUpdates() {
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 5000, 10, locationListener);
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 10, locationListener);
        } else {
            Log.e("LocationHelper", "location unauthorized permission");
        }
    }

    /**
     * Returns the current city name.
     * @return The name of the city of the system
     */
    public String getCity(){
        return city;
    }
}
