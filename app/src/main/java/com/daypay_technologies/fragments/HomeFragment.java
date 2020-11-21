package com.daypay_technologies.fragments;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.daypay_technologies.LandingPageActivity;
import com.daypay_technologies.R;
import com.daypay_technologies.fragments.adapters.ImageRecyclerAdapter;
import com.daypay_technologies.helpers.ImageHelper;
import com.daypay_technologies.listeners.MergeImageListener;
import com.scanlibrary.ScanActivity;
import com.scanlibrary.ScanConstants;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class HomeFragment extends BaseFragment implements MergeImageListener {
    private static final int MERGE_REQUEST_CODE = 1811;
    View view;
    RecyclerView recyclerView;
    File directory;
    ArrayList<File> imageData;
    boolean shareIconVisibility = false;
    boolean mergeIconVisibility = true;
    ArrayList<Integer> selectedImagePositions;
    ImageRecyclerAdapter imageRecyclerAdapter;
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
        if(imageData.size()>0)
            setRecyclerAdapter(imageData);
        else{
            view = inflater.inflate(R.layout.message_layout,container,false);
            ((TextView) view.findViewById(R.id.message)).setText(("You have no scanned files"));
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
        imageRecyclerAdapter = new ImageRecyclerAdapter(imageData, (AppCompatActivity)getActivity());
        recyclerView.setAdapter(imageRecyclerAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

    }
    @Override
    public void onResume() {
        super.onResume();
        if(getActivity() instanceof LandingPageActivity){
            ((LandingPageActivity) getActivity()).setToolbarIcon(shareIconVisibility, mergeIconVisibility);
            ((LandingPageActivity) getActivity()).setMergeImageListener(this);
        }
    }

    @Override
    public void onMergeClick() {
        if(recyclerView == null)
            return;
       selectedImagePositions = imageRecyclerAdapter.getSelectedImagePosition();
            if(selectedImagePositions != null && selectedImagePositions.size()>=2) {
                Bitmap firstImage = BitmapFactory.decodeFile(imageData.get(selectedImagePositions.get(0)).getPath());
                Bitmap secondImage = BitmapFactory.decodeFile(imageData.get(selectedImagePositions.get(1)).getPath());
                startMergeActivity(firstImage, secondImage);
            }
            else {
                Toast.makeText(getActivity(),"Select atleast two images to merge",Toast.LENGTH_LONG).show();
            }
        }

    public void startMergeActivity(Bitmap image1, Bitmap image2) {
        Intent intent = new Intent(getActivity(), ScanActivity.class);
        intent.putExtra(ScanConstants.OPEN_INTENT_PREFERENCE, ScanConstants.MERGE_IMAGE);
        ScanConstants.image1 = image1;
        ScanConstants.image2 = image2;
        startActivityForResult(intent, MERGE_REQUEST_CODE);
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == MERGE_REQUEST_CODE && resultCode == Activity.RESULT_OK ) {
            Uri uri1 = data.getExtras().getParcelable(ScanConstants.MERGE_IMAGE1);
            Uri uri2 = data.getExtras().getParcelable(ScanConstants.MERGE_IMAGE2);
            String mFile = data.getStringExtra(ScanConstants.FILE_NAME);
            String mFolderLocation = data.getStringExtra(ScanConstants.FOLDER_LOCATION);
            try {
                Bitmap mBitmap1 = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), uri1);
                getActivity().getContentResolver().delete(uri1, null, null);
                Bitmap mBitmap2 = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), uri2);
                getActivity().getContentResolver().delete(uri1, null, null);
                ImageHelper.mergeImage(mBitmap1, mBitmap2, mFolderLocation, mFile, (AppCompatActivity) getActivity());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
