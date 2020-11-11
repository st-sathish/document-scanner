package com.daypay.document.fragments;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.daypay.document.R;

import java.io.File;

@SuppressLint("ValidFragment")
public class CreatePdfFragment extends Fragment {
    View view;
    File file;
    LinearLayout pdf;
    String fileName;
    TextView file_name;
    @SuppressLint("ValidFragment")
    public CreatePdfFragment(File file){
        this.file = file;
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
}
