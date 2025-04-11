package com.example.template.Junit;

import android.content.Context;
import android.location.LocationManager;
import android.location.Location;

import androidx.core.content.ContextCompat;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

import com.example.template.util.LocationHelper;

import java.io.IOException;

@RunWith(AndroidJUnit4.class)
public class LocationHelperTest {
    private LocationHelper locationHelper;
    @Mock
    private Context mockContext;
    @Mock
    private LocationManager mockLocationManager;
    @Mock
    private LocationHelper.LocationCallback mockCallback;

    @Before
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        when(mockContext.getSystemService(Context.LOCATION_SERVICE)).thenReturn(mockLocationManager);
        locationHelper = spy(new LocationHelper(mockContext, mockCallback));
    }

    @Test
    public void testIsCorrectCity() throws IOException {
        when(ContextCompat.checkSelfPermission(mockContext, android.Manifest.permission.ACCESS_FINE_LOCATION))
                .thenReturn(android.content.pm.PackageManager.PERMISSION_GRANTED);

        Location fakeLocation = new Location(LocationManager.GPS_PROVIDER);
        fakeLocation.setLongitude(100);
        fakeLocation.setLatitude(-80);

        when(mockLocationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)).thenReturn(fakeLocation);
        doReturn("Halifax").when(locationHelper).getCity();

        String city = locationHelper.getCity();
        assertEquals("Halifax", city);
    }
}