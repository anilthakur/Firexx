package com.example.fireex;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.fireex.adapter.MyRecyclerViewAdapter;
import com.example.fireex.model.FireControlDetail;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ScannerListActivity extends AppCompatActivity {
    MyRecyclerViewAdapter adapter;
    private TextView generatePdf;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.scannerlist);
        generatePdf = findViewById(R.id.generatePdf);
        getSupportActionBar().hide();
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Fire control Panel");
        toolbar.setSubtitleTextColor(Color.WHITE);
        toolbar.setTitleTextColor(Color.WHITE);
        toolbar.setNavigationIcon(R.drawable.ic_baseline_arrow_back_24);
        toolbar.setNavigationOnClickListener(v -> goToLandingPage());
        DBHelper db = new DBHelper(this);
        ArrayList<HashMap<String, String>> fireList = db.getAllFire();
        ArrayList<HashMap<String, String>> fireExList = db.getAllFireEx();
        ArrayList<FireControlDetail> listItem = new ArrayList<>();
        FireControlDetail obj = new FireControlDetail();
        for (HashMap<String, String> map : fireList) {
            for (Map.Entry<String, String> entry : map.entrySet()) {
                if (entry.getKey().equalsIgnoreCase("building_name")) {
                    obj.setBuildingName(entry.getValue());
                } else if (entry.getKey().equalsIgnoreCase("fire_extinguisher_num")) {
                    obj.setExtinguisherNum(entry.getValue());
                } else if (entry.getKey().equalsIgnoreCase("fire_extinguisher_type")) {
                    obj.setExtinguisherType(entry.getValue());
                } else if (entry.getKey().equalsIgnoreCase("fire_point_num")) {
                    obj.setFirePointNum(entry.getValue());
                } else if (entry.getKey().equalsIgnoreCase("location")) {
                    obj.setAddress(entry.getValue());
                }
            }

        }
        listItem.add(obj);
        // set up the RecyclerView
        RecyclerView recyclerView = findViewById(R.id.list);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new MyRecyclerViewAdapter(this, listItem);
        recyclerView.setAdapter(adapter);
        generatePdf.setOnClickListener(v -> {
            Intent intent = new Intent(this, FormActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        });

    }

    private void goToLandingPage() {
        Intent intent = new Intent(this, LandingPageActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }
}