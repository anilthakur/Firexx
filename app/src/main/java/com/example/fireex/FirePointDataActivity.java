package com.example.fireex;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class FirePointDataActivity extends AppCompatActivity {

    TextView confirmationButton;
    String location;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fire_point_data);
        confirmationButton = findViewById(R.id.btnscan);
        location = getIntent().getStringExtra("location");
        getSupportActionBar().hide();
        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Fire control Panel");
        toolbar.setSubtitleTextColor(Color.WHITE);
        toolbar.setTitleTextColor(Color.WHITE);
        toolbar.setNavigationIcon(R.drawable.ic_baseline_arrow_back_24);
        toolbar.setNavigationOnClickListener(v -> finish());

        confirmationButton.setOnClickListener(v -> {
            Intent intent = new Intent(getApplicationContext(), FirePointScannerActivity.class);
            intent.putExtra("location", location);
            startActivity(intent);
        });
    }
}