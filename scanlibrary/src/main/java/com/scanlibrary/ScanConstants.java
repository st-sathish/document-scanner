package com.scanlibrary;

import android.graphics.Bitmap;
import android.os.Environment;

/**
 * Created by jhansi on 15/03/15.
 */
public class ScanConstants {

    public final static int PICKFILE_REQUEST_CODE = 1;
    public final static int START_CAMERA_REQUEST_CODE = 2;
    public final static String OPEN_INTENT_PREFERENCE = "selectContent";
    public final static String IMAGE_BASE_PATH_EXTRA = "ImageBasePath";
    public final static int OPEN_FULL_CAMERA = 4;
    public final static int OPEN_ID_CAMERA = 3;
    public final static int OPEN_MEDIA = 5;
    public final static int MERGE_IMAGE = 6;
    public static Bitmap image1, image2, resultImage1, resultImage2;
    public static String ROOT_FOLDER = Environment
            .getExternalStorageDirectory().getPath() + "/DocumentScanner";
    public final static String SCANNED_RESULT = "scannedResult";
    public final static String FOLDER_NAME = "folder_name";
    public final static String FILE_NAME = "file_name";
    public final static String FOLDER_LOCATION = "folder_location";
    public final static String MERGE_IMAGE1 = "merge_image1";
    public final static String MERGE_IMAGE2 = "merge_image2";
    public final static String IMAGE_PATH = Environment
            .getExternalStorageDirectory().getPath() + "/scanSample";

    public final static String SELECTED_BITMAP = "selectedBitmap";
}
