package com.example.fireex;

import static com.example.fireex.util.Constants.USER_DETAIL;

import android.graphics.Color;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.fireex.model.User;
import com.example.fireex.util.DataProcessor;

public class MyProfileActivity extends AppCompatActivity {
    private Toolbar toolbar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_profile);
        getSupportActionBar().hide();
        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("My Profile");
        toolbar.setSubtitleTextColor(Color.WHITE);
        toolbar.setTitleTextColor(Color.WHITE);
        toolbar.setNavigationIcon(R.drawable.ic_baseline_arrow_back_24);
        toolbar.setNavigationOnClickListener(v -> finish());
        User user = (User) DataProcessor.getUser(USER_DETAIL);
        if (user != null) {
            TextView nameText = findViewById(R.id.tv_name);
            TextView phoneText = findViewById(R.id.tv_phone);
            nameText.setText(user.getFirstName() + " " + user.getLastName());
            phoneText.setText(user.getPhNum());
        } else {
            finish();
            Toast.makeText(this, "Please Login", Toast.LENGTH_SHORT).show();
        }
    }
}
