package com.daypay_technologies.helpers;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.net.Uri;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

import com.daypay_technologies.R;
import com.daypay_technologies.fragments.ShowImageFragment;
import com.scanlibrary.ScanConstants;

import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ImageHelper {


    public static File saveImage(Bitmap bitmap, String folderPath, String fileName, AppCompatActivity activity){
        File folder = new File(folderPath);
        Uri imageUri = null;
        if (! folder.exists()) {
            folder.mkdirs();
        }
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new
                Date());

        String fname = fileName+"_"+timeStamp +".jpg";
        File file = new File (folder, fname);

      if (file.exists ()) file.delete ();
        try {
            FileOutputStream out = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
            out.flush();
            out.close();
           imageUri = Uri.fromFile(file);
        } catch (Exception e) {
            e.printStackTrace();
        }
        showImage(activity, bitmap, file, imageUri);
        return file;
    }
    public static void mergeImage(Bitmap firstImage, Bitmap secondImage, String filePath, String fileName, AppCompatActivity activity){
        int width = firstImage.getWidth()< secondImage.getWidth() ? secondImage.getWidth() : firstImage.getWidth();
        Bitmap result = Bitmap.createBitmap(width, firstImage.getHeight()+secondImage.getHeight(), firstImage.getConfig());
        float firstLeft = firstImage.getWidth()> secondImage.getWidth() ? 0f : (float) (secondImage.getWidth()-firstImage.getWidth())/2;
        float secondLeft = firstImage.getWidth()< secondImage.getWidth() ? 0f : (float) (firstImage.getWidth()-secondImage.getWidth())/2;
        Canvas canvas = new Canvas(result);
        canvas.drawRGB( 255, 255, 255);
        canvas.drawBitmap(firstImage, firstLeft, 0f, null);
        canvas.drawBitmap(secondImage, secondLeft, firstImage.getHeight()+50, null);
        ScanConstants.resultImage1 = null;
        ScanConstants.resultImage2 = null;
        if(result != null) {
            File file = saveImage(result, filePath, fileName, activity);
        }
    }

    public static void showImage(AppCompatActivity activity, Bitmap bitmap, File file, Uri imageUri){
        ShowImageFragment showImageFragment = new ShowImageFragment(bitmap, file, imageUri);
        FragmentTransaction ft = activity.getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.frame, showImageFragment, "showImage");
        ft.addToBackStack("ShowImage");
        ft.commit();
    }
    public static void shareImage(Uri imageUri, AppCompatActivity activity){
        Intent share = new Intent(Intent.ACTION_SEND);
        share.setType("image/jpeg");
        share.putExtra(Intent.EXTRA_STREAM, imageUri);
        activity.startActivity(Intent.createChooser(share, "Share Image"));
    }
}
