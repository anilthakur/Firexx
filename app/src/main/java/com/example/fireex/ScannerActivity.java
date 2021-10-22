package com.example.fireex;

import static com.example.fireex.util.Constants.USER_DETAIL;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.budiyev.android.codescanner.CodeScanner;
import com.budiyev.android.codescanner.CodeScannerView;
import com.example.fireex.model.User;
import com.example.fireex.util.DataProcessor;

public class ScannerActivity extends AppCompatActivity {
    CodeScanner codeScanner;
    CodeScannerView scannView;
    TextView resultData;
    DBHelper dbHelper;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scanner);
        dbHelper = new DBHelper(this);
        scannView = findViewById(R.id.scannerView);
        codeScanner = new CodeScanner(this, scannView);
        resultData = findViewById(R.id.resultsOfQr);
        codeScanner.startPreview();
        codeScanner.setDecodeCallback(result -> runOnUiThread(() -> {
            Toast.makeText(ScannerActivity.this, result.getText(), Toast.LENGTH_SHORT).show();
            String[] userData = result.getText().split("-");
            User user = dbHelper.getUserByData(userData[0], userData[1]);
            resultData.setText(userData[0] + "Logged in successfully");
            if (user != null && user.getRoleId() == 3 && user.getPhNum().equalsIgnoreCase(userData[3])) {
                DataProcessor.saveUser(USER_DETAIL, user);
                openGeoLocationActivity();
            } else {
                Toast.makeText(ScannerActivity.this, "Login Failed", Toast.LENGTH_SHORT).show();
            }
        }));

        codeScanner.setErrorCallback(error -> Toast.makeText(ScannerActivity.this, error.toString(), Toast.LENGTH_SHORT).show());

    }

    private void openGeoLocationActivity() {
        Intent intent = new Intent(this, LandingPageActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }


    @Override
    protected void onPause() {
        codeScanner.releaseResources();
        super.onPause();
    }
}
