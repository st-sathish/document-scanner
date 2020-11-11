package com.daypay.document.fragments;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.daypay.document.MainActivity;
import com.daypay.document.R;
import com.daypay.document.fragments.adapters.MergeImageAdapter;
import com.scanlibrary.ScanConstants;


import java.io.File;
import java.util.ArrayList;

import lib.folderpicker.FolderPicker;

public class MergeImageFragment extends Fragment {


    View view;
    RecyclerView recyclerView;
    String root;
    File directory;
    ArrayList<File> imageData;
    MergeImageAdapter imageRecyclerAdapter;
    int[] imagePosition;
    public int FOLDERPICKER_CODE = 1988;
    public String _folderLocation;
    private Bitmap _result;

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
            _result = result;
            Intent intent = new Intent(getActivity(), FolderPicker.class);
            intent.putExtra("title", "Select folder to save");
            startActivityForResult(intent, FOLDERPICKER_CODE);

        }
    }
    public void showModal(){
        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        final EditText input = new EditText(getActivity());

        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);
        input.setLayoutParams(lp);



        builder.setView(input);
        builder.setTitle("Select File Name");
        builder.setCancelable(false);

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        builder.setPositiveButton("Continue", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                if(!input.getText().toString().matches("")) {
                    MainActivity mainActivity = (MainActivity)getActivity();
                    mainActivity.mFolderLocation = _folderLocation;
                    mainActivity.mFile = input.getText().toString();
                    File file =  mainActivity.saveImage(_result);
                    mainActivity.showImage(_result, file);
                    //  passImage(_folderLocation, input.getText().toString());
                    dialog.cancel();
                }

                else{
                    Toast.makeText(getActivity(), "Please chooose file name" ,Toast.LENGTH_SHORT).show();
                }
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();

    }
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        if (requestCode == FOLDERPICKER_CODE && resultCode == Activity.RESULT_OK) {

            String folderLocation = intent.getExtras().getString("data");
            Log.i( "folderLocation", folderLocation );
            _folderLocation = folderLocation;
            showModal();
            // passImage(folderLocation);

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
        imageRecyclerAdapter = new MergeImageAdapter(imageData);
        recyclerView.setAdapter(imageRecyclerAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

    }


}