package com.daypay_technologies;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.SurfaceHolder;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.daypay_technologies.fragments.CameraFragment;
import com.daypay_technologies.fragments.HomeFragment;
import com.daypay_technologies.helpers.ImageHelper;
import com.scanlibrary.ScanActivity;
import com.scanlibrary.ScanConstants;
import com.daypay_technologies.utils.ScreenUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import static android.Manifest.permission.CAMERA;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;


public class LandingPageActivity extends BaseAppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener{

    private static final int CAMERA_FRAGMENT = 1;

    private static final int GALLERY_FRAGMENT = 2;

    private static final int MERGE_FRAGMENT = 3;

    private static final int HOME_FRAGMENT = 4;

    private static final int OPEN_MEDIA = 2105;

    String rootDirectory;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ac_landing_page);
        BottomNavigationView bottomNavigationView = findViewById(R.id.navigationView);
        bottomNavigationView.setOnNavigationItemSelectedListener(this);
        attachToolbar();
        rootDirectory = Environment.getExternalStorageDirectory().getAbsolutePath() + "/DocumentScanner";
    }

    private void attachToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
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
    }

    public void displayView(int fragmentNo, String aTitle, boolean addToBackstack) {
        Fragment fragment = null;
        switch (fragmentNo) {
            case CAMERA_FRAGMENT:
                fragment = CameraFragment.newInstance("Camera");
                break;
            case HOME_FRAGMENT:
                fragment = HomeFragment.newInstance(rootDirectory);
                break;
            default:
                break;
        }
        switchFragment(fragment, addToBackstack);
    }

    public void switchFragment(Fragment fragment, boolean aAddtoBackstack) {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        String backStateName = ft.getClass().getName();
        //ft.setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left);
        ft.replace(R.id.frame, fragment, fragment.getClass().getSimpleName());
        if (aAddtoBackstack)
            ft.addToBackStack(backStateName);
        ft.commit();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        switch (menuItem.getItemId()){
            case R.id.home:
                displayView(HOME_FRAGMENT, "Home", true);
                break;
            case R.id.camera:
                displayView(CAMERA_FRAGMENT, "Camera", true);
                break;
            case R.id.media:
                fetchImageFromGallery();
                break;
            case R.id.merge:
                break;
        }
        return true;
    }
    public void fetchImageFromGallery(){
        Intent intent = new Intent(this, ScanActivity.class);
        intent.putExtra(ScanConstants.OPEN_INTENT_PREFERENCE, ScanConstants.OPEN_MEDIA);
        startActivityForResult(intent, OPEN_MEDIA);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == OPEN_MEDIA && resultCode == Activity.RESULT_OK) {
            Uri uri = data.getExtras().getParcelable(ScanConstants.SCANNED_RESULT);
           String  mFolderLocation = data.getStringExtra(ScanConstants.FOLDER_LOCATION);
            String mFile = data.getStringExtra(ScanConstants.FILE_NAME);
            Bitmap bitmap = null;
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                getContentResolver().delete(uri, null, null);
                File imgFile = ImageHelper.saveImage(bitmap, mFolderLocation, mFile);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
