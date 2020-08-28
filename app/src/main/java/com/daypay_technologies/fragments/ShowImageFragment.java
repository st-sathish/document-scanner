package com.daypay_technologies.fragments;


import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import com.daypay_technologies.MainActivity;
import com.daypay_technologies.R;

import java.io.File;
import java.util.zip.Inflater;

@SuppressLint("ValidFragment")
public class ShowImageFragment extends Fragment {
    View view;
    Bitmap bitmap;
    File file;
    ImageView imageView;
    Button convert;
  @SuppressLint("ValidFragment")
  public ShowImageFragment(Bitmap bitmap, File file){
      this.bitmap = bitmap;
      this.file = file;

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
              ((MainActivity)getActivity()).convertPdf(file);
          }
      });
        imageView.setImageBitmap(bitmap);
       return view;
    }
    @Override
    public void onResume() {
        super.onResume();
        if(getActivity() instanceof MainActivity){
            ((MainActivity) getActivity()).setToolbarIcon(this);
        }
    }
}

