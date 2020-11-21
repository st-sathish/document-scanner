package com.daypay_technologies.helpers;

import android.graphics.Bitmap;
import android.net.Uri;

import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ImageHelper {


    public static File saveImage(Bitmap bitmap, String folderPath, String fileName){
        File folder = new File(folderPath);
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
           Uri imageUri = Uri.fromFile(file);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return file;
    }
}
