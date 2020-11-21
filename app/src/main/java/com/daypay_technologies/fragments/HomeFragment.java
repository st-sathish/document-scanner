package com.daypay_technologies.fragments;

import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.daypay_technologies.R;
import com.daypay_technologies.fragments.adapters.ImageRecyclerAdapter;

import java.io.File;
import java.util.ArrayList;

public class HomeFragment extends BaseFragment {
    View view;
    RecyclerView recyclerView;
    String root;
    File directory;
    ArrayList<File> imageData;

    public static HomeFragment newInstance(String directory) {
        HomeFragment homeFragment = new HomeFragment();
        Bundle bundle = new Bundle();
        bundle.putString("directory", directory);
        homeFragment.setArguments(bundle);
        return homeFragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fr_home,container,false);
        recyclerView = (RecyclerView) view.findViewById(R.id.image_recycler);
        imageData = new ArrayList<>();
        setImageData();
        if(imageData != null) {
            setRecyclerAdapter(imageData);
        }
        return view;
    }

    private void setImageData() {
        Bundle bundle = this.getArguments();
        if (bundle != null)
            directory = new File(bundle.getString("directory"));
        if(directory == null)
            return;
        File[] files = directory.listFiles();
        if(files == null)
            return;
        for (int i = 0; i < files.length; i++) {
            File imgFile = new File(directory +"/"+files[i].getName());
            imageData.add(imgFile);
        }
    }

    private void setRecyclerAdapter(ArrayList<File> imageData){
        ImageRecyclerAdapter imageRecyclerAdapter = new ImageRecyclerAdapter(imageData, (AppCompatActivity)getActivity());
        recyclerView.setAdapter(imageRecyclerAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

    }
}
