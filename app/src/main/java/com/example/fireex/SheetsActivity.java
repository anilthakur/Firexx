package com.example.fireex;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.webkit.MimeTypeMap;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.FileProvider;

import java.io.File;
import java.util.Objects;

public class SheetsActivity extends AppCompatActivity {
    public static final int REEQUEST_CODE_VIEW_PDF = 120;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_security_check);
        Objects.requireNonNull(getSupportActionBar()).hide();
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Sheets");
        toolbar.setSubtitleTextColor(Color.WHITE);
        toolbar.setTitleTextColor(Color.WHITE);
        toolbar.setNavigationIcon(R.drawable.ic_baseline_arrow_back_24);
        toolbar.setNavigationOnClickListener(v -> finish());
        if (filePath() == null) {
            finish();
            Toast.makeText(this, "Please generate PDF", Toast.LENGTH_SHORT).show();
        }
        if (ActivityCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(SheetsActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 103);
        } else {
            readFile(filePath());
        }
    }

    public void readFile(File file) {
        try {
            Uri uri = FileProvider.getUriForFile(SheetsActivity.this, BuildConfig.APPLICATION_ID +
                    ".provider", file);

            Intent intent = new Intent(Intent.ACTION_VIEW);
            String mimeType = MimeTypeMap.getSingleton().getExtensionFromMimeType(MimeTypeMap.getFileExtensionFromUrl(file.getPath()));
            intent.setDataAndType(uri, mimeType);
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            Intent chooser = Intent.createChooser(intent, "Open with");
            chooser.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            startActivityForResult(chooser, REEQUEST_CODE_VIEW_PDF);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REEQUEST_CODE_VIEW_PDF) {
            Intent intent = new Intent(this, LandingPageActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        }
    }

    public File filePath() {
        String path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/PDF";
        File directory = new File(path);
        if (!directory.exists())
            directory.mkdir();
        File file = new File(directory, "/survey_data.pdf");
        return file;
    }
}

