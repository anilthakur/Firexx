package com.example.fireex;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import com.example.fireex.util.GpsTracker;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class GeoLocationPermissionActivity extends AppCompatActivity {


    protected Context context;
    private GpsTracker locationTrack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_geo_location_permission);
        locationTrack = new GpsTracker(this);
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        } else {
            // Already permission is there
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1) {
            if (ContextCompat.checkSelfPermission(this,
                    Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
              // Permission Granted
            } else {
                // // Permission Denied
            }
            return;
        }
        throw new IllegalStateException("Unexpected value: " + requestCode);
    }

    private void openFirePointDataActivity(String address) {
        Intent intent = new Intent(this, FirePointDataActivity.class);
        intent.putExtra("location", address);
        startActivity(intent);
    }

    private void getLocation() {
        if (locationTrack.canGetLocation()) {
            Geocoder geocoder;
            List<Address> addresses = null;
            geocoder = new Geocoder(this, Locale.getDefault());
            try {
                addresses = geocoder.getFromLocation(locationTrack.getLatitude(), locationTrack.getLongitude(), 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (addresses != null) {
                String address = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
                String city = addresses.get(0).getLocality();
                String state = addresses.get(0).getAdminArea();
                String country = addresses.get(0).getCountryName();
                String postalCode = addresses.get(0).getPostalCode();
                String knownName = addresses.get(0).getFeatureName(); // Only if available else return NULL
                openFirePointDataActivity(address + "," + city + "," + state + "," + postalCode + "," + country + "," + knownName);
            }
        } else {
            Toast.makeText(this, "Unable to find location Try after Some time", Toast.LENGTH_SHORT).show();
            finish();
        }
    }

}


