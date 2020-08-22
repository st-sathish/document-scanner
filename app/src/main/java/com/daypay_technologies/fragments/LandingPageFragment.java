package com.daypay_technologies.fragments;


import android.graphics.Bitmap;
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

import com.daypay_technologies.R;
import com.daypay_technologies.fragments.adapters.ImageRecyclerAdapter;
import java.io.File;
import java.util.ArrayList;
import java.util.zip.Inflater;

public class LandingPageFragment extends Fragment {
    View view;
    RecyclerView recyclerView;
    String root;
    File directory;
    ArrayList<File> imageData;

    public LandingPageFragment(){
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.landing_page,container,false);
        recyclerView = (RecyclerView) view.findViewById(R.id.image_recycler);
        imageData = new ArrayList<>();
        setImageData();
        if(imageData != null) {
            setRecyclerAdapter(imageData);
        }
        return view;
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
        ImageRecyclerAdapter imageRecyclerAdapter = new ImageRecyclerAdapter(imageData);
        recyclerView.setAdapter(imageRecyclerAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

    }
}