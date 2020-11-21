package com.daypay_technologies.fragments.adapters;


import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.daypay_technologies.R;
import com.daypay_technologies.fragments.HomeFragment;
import android.support.v7.app.AppCompatActivity;
import java.io.File;
import java.util.ArrayList;


public class ImageRecyclerAdapter extends
        RecyclerView.Adapter<ImageRecyclerAdapter.ViewHolder> {

    ArrayList<File> files;
    Bitmap imageBitmap;
    String imagePath;
    AppCompatActivity activity;
    HomeFragment fragment;
    ArrayList<Integer> selectedImages = new ArrayList();
   public ImageRecyclerAdapter(ArrayList<File> files, AppCompatActivity activity){
       this.files = files;
       this.activity = activity;
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
public ArrayList getSelectedImagePosition(){
       return selectedImages;
}
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
       File file = files.get(position);
       imagePath = file.getName();
       ImageView imageView = holder.image;
       TextView textView = holder.fileName;
        if (!file.isDirectory()){
            imageBitmap = BitmapFactory.decodeFile(file.getAbsolutePath());
            imageView.setImageBitmap(imageBitmap);
            holder.checkBox.setVisibility(View.VISIBLE);
        }
        else{
            imageView.setImageDrawable(activity.getResources().getDrawable(R.drawable.fp_folder));
            holder.checkBox.setVisibility(View.GONE);
        }
        textView.setText(imagePath);
    }

    @Override
    public int getItemCount() {
        return files.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder  {
        TextView fileName;
        ImageView image;
        CheckBox checkBox;
        LinearLayout fileArea;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            fileName = (TextView) itemView.findViewById(R.id.file_name);
            image = (ImageView) itemView.findViewById(R.id.image_file);
            checkBox = (CheckBox) itemView.findViewById(R.id.checkBox);
            fileArea = (LinearLayout) itemView.findViewById(R.id.fileArea);
            fileArea.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    openFolder(getAdapterPosition(), checkBox);
                }
            });
        }
        public void openFolder(int position, CheckBox checkBox){
            File file = files.get(position);
            if(!file.isDirectory()){
            checkBox.setChecked(!checkBox.isChecked());
            if(checkBox.isChecked())
                selectedImages.add(new Integer(position));
            else
                selectedImages.remove(Integer.valueOf(position));
                return;
            }
            fragment =  HomeFragment.newInstance(file.getAbsolutePath());
            FragmentTransaction ft = activity.getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.frame, fragment, "HomeFragment");
            ft.addToBackStack("HomeFragment");
            ft.commit();
        }
    }
}





