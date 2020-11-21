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
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;
import com.daypay_technologies.R;

import java.io.File;
import java.lang.reflect.Array;
import java.util.ArrayList;


public class MergeImageAdapter extends
        RecyclerView.Adapter<MergeImageAdapter.ViewHolder> {

    ArrayList<File> imageData;
    Bitmap imageBitmap;
    String imagePath;
    int firstImagePosition = -1;
    int secondImagePosition = -1;
    public MergeImageAdapter(ArrayList<File> imageData){
        this.imageData = imageData;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        final Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View deviceView = inflater.inflate(R.layout.merge_image_list, parent, false);

        ViewHolder viewHolder = new ViewHolder(deviceView);

        return viewHolder;
    }
    public int[] selectedImages(){
        if(firstImagePosition !=-1 && secondImagePosition !=-1){
            int[] positions = new int[2];
            positions[0] = firstImagePosition;
            positions[1] = secondImagePosition;
            return positions;
        }
       return  null;
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
        holder.checkBox.setChecked(firstImagePosition == position);
    }

    @Override
    public int getItemCount() {
        return imageData.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder  {
        TextView fileName;
        ImageView image;
        CheckBox checkBox;
        LinearLayout checkboxLayout;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            fileName = (TextView) itemView.findViewById(R.id.merge_file_name);
            image = (ImageView) itemView.findViewById(R.id.merge_image);
            checkBox = (CheckBox) itemView.findViewById(R.id.radioBtn);
            checkboxLayout = (LinearLayout) itemView.findViewById(R.id.checkbox_layout);
            checkboxLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    checkBox.setChecked(!checkBox.isChecked());
                    if(firstImagePosition == -1 ) {
                        firstImagePosition = getAdapterPosition();

                    } else {
                        secondImagePosition = firstImagePosition;
                        firstImagePosition = getAdapterPosition();
                    }
                }
            });

        }
    }
}





