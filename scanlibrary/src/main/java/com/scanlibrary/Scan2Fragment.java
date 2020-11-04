package com.scanlibrary;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.PointF;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import lib.folderpicker.FolderPicker;

/**
 * Created by jhansi on 29/03/15.
 */
@SuppressLint("ValidFragment")
public class Scan2Fragment extends ScanFragment {

    private View view;
    private IScanner scanner;
    private Button scanButton;
    private ImageView sourceImageView1, sourceImageView2;
    private FrameLayout sourceFrame1, sourceFrame2;
    private PolygonView polygonView1, polygonView2;
    private ProgressDialogFragment progressDialogFragment;
    private Bitmap original;
    Bitmap image1,image2;
    public int FOLDERPICKER_CODE = 1988;
    public String _folderLocation;
    private Bitmap bitmap1, bitmap2;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if (!(activity instanceof IScanner)) {
            throw new ClassCastException("Activity must implement IScanner");
        }
        this.scanner = (IScanner) activity;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.scan_fragment2_layout, null);
        init();
        return view;
    }

    @SuppressLint("ValidFragment")
    public Scan2Fragment(Bitmap image1, Bitmap image2) {
this.image1 = image1;
this.image2 = image2;
    }

    private void init() {
        sourceImageView1 = (ImageView) view.findViewById(R.id.sourceImageView1);
        sourceImageView2 = (ImageView) view.findViewById(R.id.sourceImageView2);
        sourceImageView1.setImageBitmap(image1);
        sourceImageView2.setImageBitmap(image2);
        scanButton = (Button) view.findViewById(R.id.scanButton);
      //  scanButton.setOnClickListener(new ScanButtonClickListener());
        sourceFrame1 = (FrameLayout) view.findViewById(R.id.sourceFrame1);
        polygonView1 = (PolygonView) view.findViewById(R.id.polygonView1);
        sourceFrame2 = (FrameLayout) view.findViewById(R.id.sourceFrame2);
        polygonView2 = (PolygonView) view.findViewById(R.id.polygonView2);
       sourceFrame1.post(new Runnable() {
            @Override
            public void run() {
                if (image1 != null) {
                    setBitmap(image1, sourceFrame1, sourceImageView1, polygonView1);
                }
            }
        });
        sourceFrame2.post(new Runnable() {
            @Override
            public void run() {
                if (image2 != null) {
                    setBitmap(image2, sourceFrame2, sourceImageView2, polygonView2);
                }
            }
        });
        scanButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Map<Integer, PointF> points1 = polygonView1.getPoints();
                Map<Integer, PointF> points2 = polygonView2.getPoints();
                if (isScanPointsValid(points1)&&isScanPointsValid(points2)) {
                    // new ScanAsyncTask(points1, points2).execute();
                       cropImage(points1, points2);
                } else {
                 //   super.showErrorDialog();
                }
            }
        });
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
                    finishActivity(bitmap1, bitmap2 , input.getText().toString());
                    //  passImage(_folderLocation, input.getText().toString());
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
public void finishActivity(Bitmap _bitmap1, Bitmap _bitmap2, final String _fileName){
    final Bitmap bitmap1 =  _bitmap1;
    final Bitmap bitmap2 =  _bitmap2;
    final String fileName =_fileName;
    final String folderLocation =_folderLocation;
    AsyncTask.execute(new Runnable() {
        @Override
        public void run() {
            try {
                Intent data = new Intent();
                Bitmap image1 = bitmap1;
                Bitmap image2 = bitmap2;
                Uri uri1 = Utils.getUri(getActivity(), image1);
                Uri uri2 = Utils.getUri(getActivity(), image2);
                data.putExtra(ScanConstants.FOLDER_LOCATION, folderLocation);
                data.putExtra(ScanConstants.FILE_NAME, _fileName);
                data.putExtra(ScanConstants.MERGE_IMAGE1, uri1);
                data.putExtra(ScanConstants.MERGE_IMAGE2, uri2);

                getActivity().setResult(Activity.RESULT_OK, data);
                bitmap1.recycle();
                bitmap2.recycle();
                System.gc();
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        //        dismissDialog();
                        getActivity().finish();
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    });
}
    public void cropImage(Map<Integer, PointF> points1, Map<Integer, PointF> points2){
         bitmap1 =  getScannedBitmap(image1, points1, sourceImageView1);
         bitmap2 =  getScannedBitmap(image2, points2, sourceImageView2);
        Intent intent = new Intent(getActivity(), FolderPicker.class);
        intent.putExtra("title", "Select folder to save");
        startActivityForResult(intent, FOLDERPICKER_CODE);

       // ScanConstants.resultImage1 = bitmap1;
      //  ScanConstants.resultImage2 = bitmap2;
      //  getActivity().finish();
    }

    private boolean isScanPointsValid(Map<Integer, PointF> points) {
        return points.size() == 4;
    }

    private void setBitmap(Bitmap original,FrameLayout sourceFrame, ImageView sourceImageView,PolygonView polygonView) {
        Bitmap scaledBitmap = super.scaledBitmap(original, sourceFrame.getWidth(), sourceFrame.getHeight());
        sourceImageView.setImageBitmap(scaledBitmap);
        Bitmap tempBitmap = ((BitmapDrawable) sourceImageView.getDrawable()).getBitmap();
        Map<Integer, PointF> pointFs = super.getEdgePoints(tempBitmap, polygonView);
        polygonView.setPoints(pointFs);
        polygonView.setVisibility(View.VISIBLE);
        int padding = (int) getResources().getDimension(R.dimen.scanPadding);
        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(tempBitmap.getWidth() + 2 * padding, tempBitmap.getHeight() + 2 * padding);
        layoutParams.gravity = Gravity.CENTER;
        polygonView.setLayoutParams(layoutParams);
    }


    private class ScanAsyncTask extends AsyncTask<Void, Void, Bitmap> {

        private Map<Integer, PointF> points1;
        private Map<Integer, PointF> points2;

        public ScanAsyncTask(Map<Integer, PointF> points1, Map<Integer, PointF> points2) {
            this.points1 = points1;
            this.points2 = points2;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            showProgressDialog(getString(R.string.scanning));
        }

        @Override
        protected Bitmap doInBackground(Void... params) {
            Bitmap bitmap1 =  getScannedBitmap(image1, points1, sourceImageView1);
            Bitmap bitmap2 =  getScannedBitmap(image2, points2, sourceImageView2);
          //  ScanConstants.resultImage1 = bitmap1;
          //  ScanConstants.resultImage2 = bitmap2;




            return bitmap1;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            super.onPostExecute(bitmap);
            bitmap.recycle();

            getActivity().finish();
            dismissDialog();
        }
    }
    private Bitmap getScannedBitmap(Bitmap original, Map<Integer, PointF> points, ImageView sourceImageView) {
        int width = original.getWidth();
        int height = original.getHeight();
        float xRatio = (float) original.getWidth() / sourceImageView.getWidth();
        float yRatio = (float) original.getHeight() / sourceImageView.getHeight();

        float x1 = (points.get(0).x) * xRatio;
        float x2 = (points.get(1).x) * xRatio;
        float x3 = (points.get(2).x) * xRatio;
        float x4 = (points.get(3).x) * xRatio;
        float y1 = (points.get(0).y) * yRatio;
        float y2 = (points.get(1).y) * yRatio;
        float y3 = (points.get(2).y) * yRatio;
        float y4 = (points.get(3).y) * yRatio;
        Log.d("", "POints(" + x1 + "," + y1 + ")(" + x2 + "," + y2 + ")(" + x3 + "," + y3 + ")(" + x4 + "," + y4 + ")");
        Bitmap _bitmap = ((ScanActivity) getActivity()).getScannedBitmap(original, x1, y1, x2, y2, x3, y3, x4, y4);
        return _bitmap;
    }


}