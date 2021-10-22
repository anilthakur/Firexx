package com.example.fireex.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.fireex.R;
import com.example.fireex.model.FireControlDetail;

import java.util.ArrayList;

public class MyRecyclerViewAdapter extends RecyclerView.Adapter<MyRecyclerViewAdapter.ViewHolder> {

    private ArrayList<FireControlDetail> mData;
    private LayoutInflater mInflater;

    // data is passed into the constructor
    public MyRecyclerViewAdapter(Context context, ArrayList<FireControlDetail> data) {
        this.mInflater = LayoutInflater.from(context);
        this.mData = data;
    }

    // inflates the row layout from xml when needed
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.recyclerview_row, parent, false);
        return new ViewHolder(view);
    }

    // binds the data to the TextView in each row
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        FireControlDetail item = mData.get(position);
        if (item.getBuildingName() != null && !item.getBuildingName().isEmpty()) {
            holder.buildingNameTV.setText(item.getBuildingName());
        }
        if (item.getExtinguisherNum() != null && !item.getExtinguisherNum().isEmpty()) {
            holder.existingNumTV.setText(item.getExtinguisherNum());
        }

        if (item.getExtinguisherType() != null && !item.getExtinguisherType().isEmpty()) {
            holder.existingTypeTV.setText(item.getExtinguisherType());
        }
        if (item.getFirePointNum() != null && !item.getFirePointNum().isEmpty()) {
            holder.firePointTV.setText(item.getFirePointNum());
        }
        if (item.getAddress() != null && !item.getAddress().isEmpty()) {
            holder.addressTV.setText(item.getAddress());
        }
    }

    // total number of rows
    @Override
    public int getItemCount() {
        return mData.size();
    }


    // stores and recycles views as they are scrolled off screen
    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView buildingNameTV;
        TextView existingNumTV;
        TextView existingTypeTV;
        TextView firePointTV;
        TextView addressTV;

        ViewHolder(View itemView) {
            super(itemView);
            buildingNameTV = itemView.findViewById(R.id.tv_buildingName);
            existingNumTV = itemView.findViewById(R.id.tv_existingNum);
            existingTypeTV = itemView.findViewById(R.id.tv_existingType);
            firePointTV = itemView.findViewById(R.id.tv_firePointNum);
            addressTV = itemView.findViewById(R.id.tv_address);
        }

    }


}