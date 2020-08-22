package com.daypay_technologies.fragments.adapters;


import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import com.daypay_technologies.R;

import java.io.File;
import java.util.ArrayList;


public class ImageRecyclerAdapter extends
        RecyclerView.Adapter<ImageRecyclerAdapter.ViewHolder> {

    ArrayList<File> imageData;
    Bitmap imageBitmap;
    String imagePath;

   public ImageRecyclerAdapter(ArrayList<File> imageData){
       this.imageData = imageData;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        final Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View deviceView = inflater.inflate(R.layout.image_list, parent, false);

        ViewHolder viewHolder = new ViewHolder(deviceView);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
       File imageFile = imageData.get(position);
       imageBitmap = BitmapFactory.decodeFile(imageFile.getAbsolutePath());
       imagePath = imageFile.getName();
       ImageView imageView = holder.image;
       TextView textView = holder.fileName;
       imageView.setImageBitmap(imageBitmap);
       textView.setText(imagePath);
    }

    @Override
    public int getItemCount() {
        return imageData.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder  {
        TextView fileName;
        ImageView image;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            fileName = (TextView) itemView.findViewById(R.id.file_name);
            image = (ImageView) itemView.findViewById(R.id.image_file);
        }
    }
}





