package com.example.fireex;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class FireExtinguisherActivity extends AppCompatActivity {
    private Toolbar toolbar ;
    TextView fireExButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fire_extinguisher);
        getSupportActionBar().hide();
        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Fire control Panel");
        toolbar.setSubtitleTextColor(Color.WHITE);
        toolbar.setTitleTextColor(Color.WHITE);
        toolbar.setNavigationIcon(R.drawable.ic_baseline_arrow_back_24);
        toolbar.setNavigationOnClickListener(v -> finish());
        fireExButton = findViewById(R.id.fireExBtn);
        fireExButton.setOnClickListener(v -> {
            Intent intent = new Intent(getApplicationContext(), FireExtinguisherScannerActivity.class);
            startActivity(intent);
        });
    }
}