package com.daypay_technologies.fragments;


import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.daypay_technologies.MainActivity;
import com.daypay_technologies.R;
import com.daypay_technologies.fragments.adapters.ImageRecyclerAdapter;
import com.daypay_technologies.fragments.adapters.MergeImageAdapter;
import com.scanlibrary.ScanConstants;

import java.io.File;
import java.util.ArrayList;
import java.util.zip.Inflater;

public class MergeImageFragment extends Fragment {
    View view;
    RecyclerView recyclerView;
    String root;
    File directory;
    ArrayList<File> imageData;
    MergeImageAdapter imageRecyclerAdapter;
    int[] imagePosition;

    public MergeImageFragment(){
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.merge_fragment,container,false);
        recyclerView = (RecyclerView) view.findViewById(R.id.merge_image_recycler);
        imageData = new ArrayList<>();
        setImageData();
        if(imageData != null) {
            setRecyclerAdapter(imageData);
        }
        return view;
    }
    public boolean merge(){
        if(imageRecyclerAdapter != null){

          imagePosition = imageRecyclerAdapter.selectedImages();
          if(imagePosition != null) {
             // mergeImage();
              mergeImageFile();
          }
          else {
              return false;
          }
    return true;
        }
        return false;
    }
    public boolean mergeImageFile() {

        Bitmap firstImage = BitmapFactory.decodeFile(imageData.get(imagePosition[0]).getPath());
        Bitmap secondImage = BitmapFactory.decodeFile(imageData.get(imagePosition[1]).getPath());
        ((MainActivity)getActivity()).merge(ScanConstants.MERGE_IMAGE, firstImage, secondImage);
        return true;
    }

    private void mergeImage() {
        Bitmap firstImage = BitmapFactory.decodeFile(imageData.get(imagePosition[0]).getPath());
        Bitmap secondImage = BitmapFactory.decodeFile(imageData.get(imagePosition[1]).getPath());

        int width = firstImage.getWidth()< secondImage.getWidth() ? secondImage.getWidth() : firstImage.getWidth();
            Bitmap result = Bitmap.createBitmap(width, firstImage.getHeight()+secondImage.getHeight(), firstImage.getConfig());
            float firstLeft = firstImage.getWidth()> secondImage.getWidth() ? 0f : (float) (secondImage.getWidth()-firstImage.getWidth())/2;
            float secondLeft = firstImage.getWidth()< secondImage.getWidth() ? 0f : (float) (firstImage.getWidth()-secondImage.getWidth())/2;
            Canvas canvas = new Canvas(result);
            canvas.drawBitmap(firstImage, firstLeft, 0f, null);
            canvas.drawBitmap(secondImage, secondLeft, firstImage.getHeight(), null);
            if(result != null) {
              File file =  ((MainActivity)getActivity()).saveImage(result);
                ((MainActivity)getActivity()).showImage(result, file);
            }
    }

    @Override
    public void onResume() {
        super.onResume();
        if(getActivity() instanceof MainActivity){
            ((MainActivity) getActivity()).setToolbarIcon(this);
        }
    }

    private void setImageData() {
        root = Environment.getExternalStorageDirectory().getAbsolutePath();
        directory = new File(root + "/documentimages");
        File[] files = directory.listFiles();
        for (int i = 0; i < files.length; i++) {
            File imgFile = new File(directory +"/"+files[i].getName());
            imageData.add(imgFile);
        }
    }

    private void setRecyclerAdapter(ArrayList<File> imageData){
       imageRecyclerAdapter = new MergeImageAdapter(imageData);
        recyclerView.setAdapter(imageRecyclerAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

    }


}