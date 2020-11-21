package com.daypay_technologies.helpers;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.daypay_technologies.R;
import com.daypay_technologies.fragments.CreatePdfFragment;
import com.itextpdf.text.Document;
import com.itextpdf.text.Image;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

public class PdfHelper {
    public static void createPdf(File imagefile, String folderLocation, String _fileName, AppCompatActivity activity){
        Document document = new Document();
        String timeStamp = new SimpleDateFormat("yyyy-MMdd_HHmmss").format(new Date());
        File directory = new File(folderLocation);
        if (! directory.exists())
            directory.mkdirs();
        String fileName = _fileName+"_"+ timeStamp +".pdf";
        String imageName = imagefile.getName();
        File file = new File (directory, fileName);
        if (file.exists ()) file.delete ();
        try {
            PdfWriter.getInstance(document, new FileOutputStream(directory + "/" + fileName));
            document.open();
            Image image = Image.getInstance(imagefile.getAbsolutePath());

            float scaler = ((document.getPageSize().getWidth() - document.leftMargin()
                    - document.rightMargin() - 0) / image.getWidth()) * 100; // 0 means you have no indentation. If you have any, change it.
            image.scalePercent(scaler);
            image.setAlignment(Image.ALIGN_CENTER | Image.ALIGN_TOP);
            document.add(image);
            document.close();
             Toast.makeText(activity,"Created", Toast.LENGTH_SHORT).show();
             showPdf(file, activity);
        }
        catch(Exception e){
            e.printStackTrace();
            Toast.makeText(activity,"Sorry, Unable convert this image to pdf "+e.toString(), Toast.LENGTH_LONG).show();
        }
    }
    public static void showPdf(File file, AppCompatActivity activity) {
        Uri pdfUri = Uri.fromFile(file);
        CreatePdfFragment createPdfFragment = new CreatePdfFragment(file, pdfUri);
        FragmentTransaction fragmentTransaction = activity.getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.frame, createPdfFragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }
    public static void sharePdf(Uri pdfUri, AppCompatActivity activity){
        Intent share = new Intent(Intent.ACTION_SEND);
        share.setType("application/pdf");
        share.putExtra(Intent.EXTRA_STREAM, pdfUri);
        activity.startActivity(Intent.createChooser(share, "Share PDF"));
    }
}
