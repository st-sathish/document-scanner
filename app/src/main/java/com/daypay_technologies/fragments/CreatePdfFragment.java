package com.daypay_technologies.fragments;


import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.daypay_technologies.LandingPageActivity;
import com.daypay_technologies.MainActivity;
import com.daypay_technologies.R;
import com.daypay_technologies.helpers.PdfHelper;
import com.daypay_technologies.listeners.ShareItemListener;

import java.io.File;
import java.util.zip.Inflater;

@SuppressLint("ValidFragment")
public class CreatePdfFragment extends Fragment implements ShareItemListener {
    View view;
    File file;
    LinearLayout pdf;
    String fileName;
    TextView file_name;
    Uri pdfUri;
    boolean shareIconVisibility = true;
    boolean mergeIconVisibility = false;
    @SuppressLint("ValidFragment")
    public CreatePdfFragment(File file, Uri pdfUri){
        this.file = file;
        this.pdfUri = pdfUri;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.create_pdf_fragment,container,false);
        pdf = (LinearLayout) view.findViewById(R.id.pdf);
        file_name = (TextView) view.findViewById(R.id.file_name);
        if(file!=null){
            fileName = file.getName();
            file_name.setText(fileName);
        }
        pdf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                viewPdf();
            }
        });
        return view;
    }

    private void viewPdf() {
        if(file != null){
            Intent pdfViewIntent = new Intent(Intent.ACTION_VIEW, Uri.fromFile(file));
           // startActivity(pdfViewIntent);
            startActivity(Intent.createChooser(pdfViewIntent, "View PDF"));
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
        Toast.makeText(getActivity(),"share Click", Toast.LENGTH_LONG).show();
        PdfHelper.sharePdf(pdfUri, (AppCompatActivity)getActivity());
    }
}

