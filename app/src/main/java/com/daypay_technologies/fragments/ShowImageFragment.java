package com.daypay_technologies.fragments;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.daypay_technologies.LandingPageActivity;
import com.daypay_technologies.MainActivity;
import com.daypay_technologies.R;
import com.daypay_technologies.helpers.ImageHelper;
import com.daypay_technologies.helpers.PdfHelper;
import com.daypay_technologies.listeners.ShareItemListener;
import com.scanlibrary.ScanConstants;

import java.io.File;
import java.util.zip.Inflater;

import lib.folderpicker.FolderPicker;

@SuppressLint("ValidFragment")
public class ShowImageFragment extends Fragment implements ShareItemListener {
    View view;
    Bitmap bitmap;
    File file;
    ImageView imageView;
    Button convert;
    public int FOLDERPICKER_CODE = 1988;
    public String _folderLocation;
    boolean shareIconVisibility = true;
    boolean mergeIconVisibility = false;
    Uri imageUri;
  @SuppressLint("ValidFragment")
  public ShowImageFragment(Bitmap bitmap, File file, Uri imageUri){
      this.bitmap = bitmap;
      this.file = file;
      this.imageUri = imageUri;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
       view = inflater.inflate(R.layout.image_shown_fragment,container,false);
      imageView = view.findViewById(R.id.savedImage);
      convert = view.findViewById(R.id.convert);
      convert.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View view) {
              convertToPdf();
          }
      });
        imageView.setImageBitmap(bitmap);
       return view;
    }
    public void convertToPdf(){
        Intent intent = new Intent(getActivity(), FolderPicker.class);
        intent.putExtra("title", "Select folder to save");
        intent.putExtra("location", ScanConstants.ROOT_FOLDER);
        startActivityForResult(intent, FOLDERPICKER_CODE);
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
                   // passImage(_folderLocation, input.getText().toString());
                   PdfHelper.createPdf(file, _folderLocation, input.getText().toString(),(AppCompatActivity) getActivity());
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
        if(getActivity() instanceof LandingPageActivity){
            ((LandingPageActivity) getActivity()).setToolbarIcon(shareIconVisibility, mergeIconVisibility);
            ((LandingPageActivity) getActivity()).setShareItemListener(this);
        }
    }

    @Override
    public void onShareClick() {
        ImageHelper.shareImage(imageUri, (AppCompatActivity) getActivity());
    }
}

