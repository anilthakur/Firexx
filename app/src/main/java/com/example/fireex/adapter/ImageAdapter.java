package com.example.fireex.adapter;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.fireex.R;

import java.util.ArrayList;

public class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.viewHolder> {

    Context context;
    ArrayList<String> arrayList;

    public ImageAdapter(Context context, ArrayList<String> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
    }

    @Override
    public ImageAdapter.viewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.image_view_item, viewGroup, false);
        return new viewHolder(view);
    }

    @Override
    public void onBindViewHolder(ImageAdapter.viewHolder viewHolder, int position) {
        Uri imageUri = Uri.parse(arrayList.get(position));
        viewHolder.image.setImageURI(imageUri);
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class viewHolder extends RecyclerView.ViewHolder {
        ImageView image;

        public viewHolder(View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.item_image);

        }
    }

}