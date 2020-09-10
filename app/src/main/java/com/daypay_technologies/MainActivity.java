package com.daypay_technologies;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.daypay_technologies.fragments.CreatePdfFragment;
import com.daypay_technologies.fragments.LandingPageFragment;
import com.daypay_technologies.fragments.MergeImageFragment;
import com.daypay_technologies.fragments.ShowImageFragment;
import com.itextpdf.text.Document;
import com.itextpdf.text.Image;
import com.itextpdf.text.pdf.PdfWriter;
import com.scanlibrary.ScanActivity;
import com.scanlibrary.ScanConstants;
import com.wildma.idcardcamera.camera.IDCardCamera;


import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;


public class MainActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {

    private static final int REQUEST_CODE = 99;
    private static final int MERGE_REQUEST_CODE = 98;
    BottomNavigationView bottomNavigationView;
    Uri imageUri, pdfUri;
    String root;
    File myDir;
    FragmentManager fragmentManager;
    FragmentTransaction fragmentTransaction;
    Bitmap imageBitmap;
    MenuItem shareIcon, mergeIcon;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        fragmentManager = getSupportFragmentManager();
        if (android.os.Build.VERSION.SDK_INT > 23) {
            if(checkPermission()){
                setLandingPage();
            }
        }
        else{
            setLandingPage();;
        }


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
       // get the reference of Toolbar
        setSupportActionBar(toolbar); // Setting/replace toolbar as the ActionBar
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(ContextCompat.getColor(this, R.color.my_statusbar_color));
        }
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());
       // addBottomBorder();
        root = Environment.getExternalStorageDirectory().getAbsolutePath();
        myDir = new File(root + "/Scanner");
        if (! myDir.exists()) {
           if (myDir.mkdirs()){
               Toast.makeText(this, "created",Toast.LENGTH_LONG).show();
           }
           else{
               Toast.makeText(this, "Not created",Toast.LENGTH_LONG).show();
           }
        } else {
            Toast.makeText(this, "Already exists",Toast.LENGTH_LONG).show();
        }

      init();
    }

    private void setLandingPage() {
        LandingPageFragment landingPageFragment = new LandingPageFragment();
        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.frame, landingPageFragment);
        fragmentTransaction.commit();
    }

    public void showImage(Bitmap bitmap, File file) {
            ShowImageFragment showImageFragment = new ShowImageFragment(bitmap, file);
            fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.frame, showImageFragment);
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();
    }


    @RequiresApi(api = Build.VERSION_CODES.M)
    private boolean checkPermission() {
        if(ContextCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA) !=
                PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(this, android.Manifest.permission.READ_EXTERNAL_STORAGE) !=
                PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE) !=
                PackageManager.PERMISSION_GRANTED ){
            requestPermissions(new String[]{Manifest.permission.CAMERA,Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE }, 500);
            return false;
        }
return true;
    }




    private void init() {
        bottomNavigationView = (BottomNavigationView) findViewById(R.id.navigationView);
        bottomNavigationView.setOnNavigationItemSelectedListener(this);
    }

    private void shareImage() {
        Fragment fragment = fragmentManager.findFragmentById(R.id.frame);
        if(fragment instanceof ShowImageFragment) {
            Intent share = new Intent(Intent.ACTION_SEND);
            share.setType("image/jpeg");
            share.putExtra(Intent.EXTRA_STREAM, imageUri);
            startActivity(Intent.createChooser(share, "Share Image"));
        }
        else if(fragment instanceof CreatePdfFragment) {
            Intent share = new Intent(Intent.ACTION_SEND);
            share.setType("application/pdf");
            share.putExtra(Intent.EXTRA_STREAM, pdfUri);
            startActivity(Intent.createChooser(share, "Share PDF"));
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        switch (menuItem.getItemId()){
            case R.id.camera:

                openModal();

             //  IDCardCamera.create(this).openCamera(IDCardCamera.TYPE_IDCARD_FRONT);

                break;
            case R.id.media:
                startScan(ScanConstants.OPEN_MEDIA);

                break;
           /* case R.id.scan:
                startScan(0);

                break; */
            case R.id.merge:
             mergeImageFragment();

                break;
        }
        return true;
    }

    private void openModal() {
            final AlertDialog.Builder builder = new AlertDialog.Builder(this);
            final boolean[] n = new boolean[1];
            EditText input = new EditText(this);
        LayoutInflater inflater = this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.camera_modal, null);
       RadioButton id = dialogView.findViewById(R.id.id);
        RadioButton full = dialogView.findViewById(R.id.full);
        final RadioGroup radioGroup = dialogView.findViewById(R.id.radioGroup);


            builder.setView(dialogView);
            builder.setTitle("Select Type");
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
                if(radioGroup .getCheckedRadioButtonId()==R.id.id){
                    startScan(ScanConstants.OPEN_FULL_CAMERA);
                } else if(radioGroup .getCheckedRadioButtonId()==R.id.full){
                    startScan(ScanConstants.OPEN_ID_CAMERA);
                }
                dialog.cancel();
            }
        });
            AlertDialog alertDialog = builder.create();
            alertDialog.show();
    }

    private void mergeImageFragment() {
        Fragment fragment = fragmentManager.findFragmentById(R.id.frame);
        if(fragment instanceof MergeImageFragment) {
            return;
        } else {
            MergeImageFragment mergeImageFragment = new MergeImageFragment();
            fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.frame, mergeImageFragment);
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();
        }
    }

    private void pickImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"),143);

    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

    }

    protected void startScan(int preference) {
        Intent intent = new Intent(this, ScanActivity.class);
        intent.putExtra(ScanConstants.OPEN_INTENT_PREFERENCE, preference);
        startActivityForResult(intent, REQUEST_CODE);
    }

    public void merge(int preference, Bitmap image1, Bitmap image2) {
        Intent intent = new Intent(this, ScanActivity.class);
        intent.putExtra(ScanConstants.OPEN_INTENT_PREFERENCE, preference);
        ScanConstants.image1 = image1;
        ScanConstants.image2 = image2;

        startActivityForResult(intent, MERGE_REQUEST_CODE);
    }



    public File saveImage(Bitmap finalBitmap) {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new
                Date());

        String fname = "Image-"+ timeStamp +".jpg";
        File file = new File (myDir, fname);
        if (file.exists ()) file.delete ();
        try {
            FileOutputStream out = new FileOutputStream(file);
            finalBitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
            out.flush();
            out.close();
            imageUri = Uri.fromFile(file);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return  file;
    }
    public void setToolbarIcon(Fragment fragment){
        if(fragment instanceof LandingPageFragment){
            if(shareIcon != null && mergeIcon != null) {
                shareIcon.setVisible(false);
                mergeIcon.setVisible(false);
            }
        }
        else if(fragment instanceof ShowImageFragment){
            shareIcon.setVisible(true);
            mergeIcon.setVisible(false);
        }
       else if(fragment instanceof MergeImageFragment){
            shareIcon.setVisible(false);
            mergeIcon.setVisible(true);
        }
        else if(fragment instanceof CreatePdfFragment){
            shareIcon.setVisible(true);
            mergeIcon.setVisible(false);
        }
    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            Uri uri = data.getExtras().getParcelable(ScanConstants.SCANNED_RESULT);
            imageUri = uri;
            Bitmap bitmap = null;
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                getContentResolver().delete(uri, null, null);
               File imgFile = saveImage(bitmap);
                imageBitmap = bitmap;
                showImage(imageBitmap, imgFile);
               // shareIcon.setVisible(true);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (requestCode == MERGE_REQUEST_CODE && resultCode == Activity.RESULT_OK ) {

            Uri uri1 = data.getExtras().getParcelable(ScanConstants.MERGE_IMAGE1);
            Uri uri2 = data.getExtras().getParcelable(ScanConstants.MERGE_IMAGE2);
            Bitmap mBitmap1 = null;
            Bitmap mBitmap2 = null;
            try {
                mBitmap1 = MediaStore.Images.Media.getBitmap(getContentResolver(), uri1);
                getContentResolver().delete(uri1, null, null);
                mBitmap2 = MediaStore.Images.Media.getBitmap(getContentResolver(), uri2);
                getContentResolver().delete(uri1, null, null);
                imageMerge(mBitmap1, mBitmap2);
                // shareIcon.setVisible(true);
            } catch (IOException e) {
                e.printStackTrace();
            }


          /*  if(ScanConstants.resultImage1 != null && ScanConstants.resultImage2 != null){
                Bitmap image1 = ScanConstants.resultImage1;
                Bitmap image2 = ScanConstants.resultImage2;
if (image1.isRecycled()){
    Toast.makeText(this,"Recycled",Toast.LENGTH_LONG).show();
    showImage(image1, null);
}
else{
    Toast.makeText(this,"Not Recycled",Toast.LENGTH_LONG).show();
}
               // imageMerge(image1, image2);
               // showImage(image1, null);
            } */

        }
        if (requestCode == 143) {
           Toast.makeText(this,"Image Selected",Toast.LENGTH_LONG).show();
        }
    }

    private Bitmap convertByteArrayToBitmap(byte[] data) {
        return BitmapFactory.decodeByteArray(data, 0, data.length);
    }

    private void imageMerge(Bitmap firstImage, Bitmap secondImage) {


        int width = firstImage.getWidth()< secondImage.getWidth() ? secondImage.getWidth() : firstImage.getWidth();
        Bitmap result = Bitmap.createBitmap(width, firstImage.getHeight()+secondImage.getHeight(), firstImage.getConfig());
        float firstLeft = firstImage.getWidth()> secondImage.getWidth() ? 0f : (float) (secondImage.getWidth()-firstImage.getWidth())/2;
        float secondLeft = firstImage.getWidth()< secondImage.getWidth() ? 0f : (float) (firstImage.getWidth()-secondImage.getWidth())/2;
        Canvas canvas = new Canvas(result);
       // canvas.drawARGB(256,256,256,256);
        canvas.drawBitmap(firstImage, firstLeft, 0f, null);
        canvas.drawBitmap(secondImage, secondLeft, firstImage.getHeight()+50, null);
        ScanConstants.resultImage1 = null;
        ScanConstants.resultImage2 = null;
        if(result != null) {
            File file = saveImage(result);
           showImage(result, file);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        shareIcon = menu.findItem(R.id.share_icon);
        mergeIcon = menu.findItem(R.id.merge_icon);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.share_icon) {
            shareImage();

            return true;
        }
        if (id == R.id.merge_icon) {
            mergeImage();
              //merge(ScanConstants.MERGE_IMAGE);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    public void convertPdf(File imagefile){
        Document document = new Document();
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new
                Date());
        File directory = new File(root + "/Scanner/Documents");
        if (! directory.exists()) {
            directory.mkdirs();
        }
        String fileName = "Document-"+ timeStamp +".pdf";
        String imageName = imagefile.getName();
        File file = new File (directory, fileName);
        if (file.exists ()) file.delete ();
        try {
            PdfWriter.getInstance(document, new FileOutputStream(directory + "/" + fileName));
            document.open();
            Image image = Image.getInstance(myDir + "/" + imageName);

            float scaler = ((document.getPageSize().getWidth() - document.leftMargin()
                    - document.rightMargin() - 0) / image.getWidth()) * 100; // 0 means you have no indentation. If you have any, change it.
            image.scalePercent(scaler);
            image.setAlignment(Image.ALIGN_CENTER | Image.ALIGN_TOP);
            document.add(image);
            document.close();
           // Toast.makeText(this,"Created", Toast.LENGTH_SHORT).show();
            viewpdf(file);
        }
        catch(Exception e){
            e.printStackTrace();
            Toast.makeText(this,"Sorry, Unable convert this image to pdf "+e.toString(), Toast.LENGTH_LONG).show();
        }
    }

    private void viewpdf(File file) {
        pdfUri = Uri.fromFile(file);
        CreatePdfFragment createPdfFragment = new CreatePdfFragment(file);
        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame, createPdfFragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    private void mergeImage() {
        Fragment fragment = fragmentManager.findFragmentById(R.id.frame);
        if(fragment instanceof MergeImageFragment) {
         if(((MergeImageFragment) fragment).merge()){

         //    Toast.makeText(this,"Merged", Toast.LENGTH_SHORT).show();
          }
          else {
              Toast.makeText(this,"Select Images to Merge", Toast.LENGTH_SHORT).show();
          }
        }
    }

}
