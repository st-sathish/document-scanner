package com.daypay_technologies.fragments;


import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.daypay_technologies.MainActivity;
import com.daypay_technologies.R;

import java.util.zip.Inflater;

public class ShowImageFragment extends Fragment {
    View view;
    Bitmap bitmap;
    ImageView imageView;
  public ShowImageFragment(Bitmap bitmap){
      this.bitmap = bitmap;

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
       view = inflater.inflate(R.layout.image_shown_fragment,container,false);
      imageView = view.findViewById(R.id.savedImage);
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

