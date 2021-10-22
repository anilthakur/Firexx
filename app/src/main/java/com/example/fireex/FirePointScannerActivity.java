package com.example.fireex;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.budiyev.android.codescanner.CodeScanner;
import com.budiyev.android.codescanner.CodeScannerView;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

public class FirePointScannerActivity extends AppCompatActivity {
    CodeScanner codeScanner;
    CodeScannerView scannView;
    TextView resultData;
    DBHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fire_point_scanner);
        Intent intent = getIntent();
        String location = intent.getStringExtra("location");
        dbHelper = new DBHelper(this);
        scannView = findViewById(R.id.firePntScannerView);
        codeScanner = new CodeScanner(this, scannView);
        resultData = findViewById(R.id.firePntResultsOfQr);
        codeScanner.setDecodeCallback(result -> runOnUiThread(() -> {
            Toast.makeText(FirePointScannerActivity.this, result.getText(), Toast.LENGTH_SHORT).show();
            resultData.setText(result.getText());
            String[] firePointScannerData = result.getText().split("-");
            boolean isInserted = dbHelper.insertFire(firePointScannerData[0], firePointScannerData[1], firePointScannerData[2],
                    location,
                    firePointScannerData[3], firePointScannerData[4]);
            if (isInserted) {
                openFireExActivity();
            } else {
                Log.d("DB_UPDATE_FAIL", "Failed to update Fire");
            }
        }));
        codeScanner.setErrorCallback(error -> Toast.makeText(FirePointScannerActivity.this, error.toString(), Toast.LENGTH_SHORT).show());
        scannView.setOnClickListener(v -> codeScanner.startPreview());
    }

    @Override
    protected void onPause() {
        codeScanner.releaseResources();
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        requestForCamera();
    }

    public void requestForCamera() {
        Dexter.withActivity(this).withPermission(Manifest.permission.CAMERA).withListener(new PermissionListener() {
            @Override
            public void onPermissionGranted(PermissionGrantedResponse response) {
                codeScanner.startPreview();
            }

            @Override
            public void onPermissionDenied(PermissionDeniedResponse response) {
                Toast.makeText(FirePointScannerActivity.this, "Camera Permission is Required.", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) {
                token.continuePermissionRequest();

            }
        }).check();
    }

    private void openFireExActivity() {
        Intent intent = new Intent(this, FireExtinguisherActivity.class);
        startActivity(intent);
    }
}
