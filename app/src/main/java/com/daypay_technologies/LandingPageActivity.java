package com.daypay_technologies;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
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

import com.daypay_technologies.fragments.CameraFragment;
import com.daypay_technologies.fragments.HomeFragment;
import com.scanlibrary.ScanConstants;
import com.daypay_technologies.utils.ScreenUtils;

import java.util.ArrayList;

import static android.Manifest.permission.CAMERA;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

public class LandingPageActivity extends BaseAppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener{

    private static final int CAMERA_FRAGMENT = 1;

    private static final int GALLERY_FRAGMENT = 2;

    private static final int MERGE_FRAGMENT = 3;

    private static final int HOME_FRAGMENT = 4;

    private int cameraWidth, cameraHeight;
    float screenMinSize,screenMaxSize;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ac_landing_page);
        BottomNavigationView bottomNavigationView = findViewById(R.id.navigationView);
        bottomNavigationView.setOnNavigationItemSelectedListener(this);
        attachToolbar();
    }

    private void attachToolbar() {
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
    }

    public void displayView(int fragmentNo, String aTitle, boolean addToBackstack) {
        Fragment fragment = null;
        switch (fragmentNo) {
            case CAMERA_FRAGMENT:
                fragment = CameraFragment.newInstance(cameraWidth, cameraHeight);
                break;
            case HOME_FRAGMENT:
                fragment = HomeFragment.newInstance("Home");
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
                chooseCameraSize();
               // displayView(CAMERA_FRAGMENT, "Camera", true);
                break;
            case R.id.media:
                break;
            case R.id.merge:
                break;
        }
        return true;
    }
private void startCamera(int width, int height){
    cameraWidth=width;
    cameraHeight=height;
    displayView(CAMERA_FRAGMENT, "Camera", true);
}
    private void chooseCameraSize() {
         screenMinSize = Math.min(ScreenUtils.getScreenWidth(this), ScreenUtils.getScreenHeight(this));
         screenMaxSize = Math.max(ScreenUtils.getScreenWidth(this), ScreenUtils.getScreenHeight(this));
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.document_modal, null);
        final RadioGroup radioGroup = dialogView.findViewById(R.id.radioGroup);
        builder.setView(dialogView);
        builder.setTitle("Select Camera Type");
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
                  int height = (int) (screenMinSize * 0.90);
                   int width = (int) (height * 68.0f / 104.0f);
                    startCamera(width,height);
                } else if(radioGroup .getCheckedRadioButtonId()==R.id.others){
                   int height = (int) (screenMinSize * .99);
                   int width = (int) (height * 68.0f / 47.0f);
                    startCamera(width,height);
                }
                dialog.cancel();
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }
}
