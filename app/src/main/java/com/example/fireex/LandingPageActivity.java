package com.example.fireex;

import static com.example.fireex.util.Constants.CAMERA_IMAGE;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.FileProvider;

import com.example.fireex.util.DataProcessor;
import com.example.fireex.util.GpsTracker;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Objects;


public class LandingPageActivity extends AppCompatActivity {
    private final int MY_PERMISSIONS_REQUEST_CAMERA = 100;
    File mPhotoFile;
    protected Context context;
    private GpsTracker locationTrack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.landingpage);
        Objects.requireNonNull(getSupportActionBar()).hide();
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Home");
        toolbar.setSubtitleTextColor(Color.WHITE);
        toolbar.setTitleTextColor(Color.WHITE);
        locationTrack = new GpsTracker(this);
    }

    public void onCameraClick(View v) {
            dispatchTakePictureIntent();
    }

    public void onQRClick(View view) {
        getLocation();
    }

    public void onFormClick(View view) {
        Intent i = new Intent(this, MyProfileActivity.class);
        startActivity(i);
    }

    public void onSecurityCheckClick(View view) {
        displayPdf();
    }

    private void displayPdf() {
        Intent i = new Intent(this, SheetsActivity.class);
        startActivity(i);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == MY_PERMISSIONS_REQUEST_CAMERA && resultCode == Activity.RESULT_OK) {
            ArrayList<String> imageList = DataProcessor.getImage(CAMERA_IMAGE);
            if (imageList == null) {
                imageList = new ArrayList<>();
            }
            imageList.add(mPhotoFile.toString());
            DataProcessor.saveImage(imageList, CAMERA_IMAGE);
            Log.d("FILEPATH", "Director" + mPhotoFile);
        }
    }


    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                ex.printStackTrace();
                // Error occurred while creating the File
            }
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(this,
                        BuildConfig.APPLICATION_ID + ".provider",
                        photoFile);

                mPhotoFile = photoFile;
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, MY_PERMISSIONS_REQUEST_CAMERA);

            }
        }
    }


    private File createImageFile() throws IOException {
        // Create an image file name
        @SuppressLint("SimpleDateFormat") String timeStamp = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
        String mFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        return File.createTempFile(mFileName, ".jpg", storageDir);
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

    private void openFirePointDataActivity(String address) {
        Intent intent = new Intent(this, FirePointDataActivity.class);
        intent.putExtra("location", address);
        startActivity(intent);
    }


}
