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

public class FireExtinguisherScannerActivity extends AppCompatActivity {

    CodeScanner codeScanner;
    CodeScannerView scannView;
    TextView resultData;
    DBHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fire_extinguisher_scanner);
        dbHelper = new DBHelper(this);
        scannView = findViewById(R.id.fireExScannerView);
        codeScanner = new CodeScanner(this,scannView);
        resultData = findViewById(R.id.fireExResultsOfQr);
        codeScanner.setDecodeCallback(result -> runOnUiThread(() -> {
            Toast.makeText(FireExtinguisherScannerActivity.this, result.getText(), Toast.LENGTH_SHORT).show();
            resultData.setText(result.getText());
            String[] firePointScannerData = result.getText().split("-");
            boolean isInserted = dbHelper.insertFireExtinguisher(firePointScannerData[0],firePointScannerData[1],firePointScannerData[2],
                    firePointScannerData[3],
                    firePointScannerData[4],firePointScannerData[5]);
            if(isInserted){
                openScannerList();
            }else{
                Log.d("DB_UPDATE_FAIL","Failed to update FireEx");
            }
        }));

        codeScanner.setErrorCallback(error -> Toast.makeText(FireExtinguisherScannerActivity.this, error.toString(), Toast.LENGTH_SHORT).show());


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
                Toast.makeText(FireExtinguisherScannerActivity.this, "Camera Permission is Required.", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) {
                token.continuePermissionRequest();

            }
        }).check();
    }

    private void openScannerList() {
        Intent intent = new Intent(this,ScannerListActivity.class);
        startActivity(intent);
    }
}