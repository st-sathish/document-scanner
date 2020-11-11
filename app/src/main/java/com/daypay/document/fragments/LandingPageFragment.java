package com.daypay.document.fragments;

import android.os.Bundle;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.daypay.document.MainActivity;


import com.daypay.document.R;
import com.daypay.document.fragments.adapters.ImageRecyclerAdapter;
import java.io.File;
import java.util.ArrayList;

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



    @Override
    public void onResume() {
        super.onResume();
        if(getActivity() instanceof MainActivity){
            ((MainActivity) getActivity()).setToolbarIcon(this);
        }
    }



    private void setImageData() {
        root = Environment.getExternalStorageDirectory().getAbsolutePath();
        directory = new File(root + "/Scanner/.temp");
        File[] files = directory.listFiles();
        for (int i = 0; i < files.length; i++) {
            if(files[i].isDirectory())
                continue;
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
