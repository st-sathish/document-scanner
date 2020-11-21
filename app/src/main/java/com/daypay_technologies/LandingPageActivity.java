package com.daypay_technologies;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.daypay_technologies.fragments.CameraFragment;
import com.daypay_technologies.fragments.HomeFragment;
import com.daypay_technologies.helpers.ImageHelper;
import com.daypay_technologies.listeners.MergeImageListener;
import com.daypay_technologies.listeners.ShareItemListener;
import com.scanlibrary.ScanActivity;
import com.scanlibrary.ScanConstants;
import java.io.File;
import java.io.IOException;


public class LandingPageActivity extends BaseAppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener{

    private static final int CAMERA_FRAGMENT = 1;

    private static final int GALLERY_FRAGMENT = 2;

    private static final int MERGE_FRAGMENT = 3;

    private static final int HOME_FRAGMENT = 4;

    private static final int OPEN_MEDIA = 2105;

    String rootDirectory;

    MenuItem shareIcon, mergeIcon;

    MergeImageListener mergeImageListener;

    ShareItemListener shareItemListener;

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
        setSupportActionBar(toolbar);
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
            try {
               Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                getContentResolver().delete(uri, null, null);
                File imgFile = ImageHelper.saveImage(bitmap, mFolderLocation, mFile, this);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        shareIcon = menu.findItem(R.id.share_icon);
        mergeIcon = menu.findItem(R.id.merge_icon);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.share_icon) {
           shareImage();
            return true;
        }
        if (id == R.id.merge_icon) {
           mergeImage();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    public void shareImage(){
        if(shareItemListener != null)
            shareItemListener.onShareClick();
    }
    public void mergeImage(){
        if(mergeImageListener != null)
        mergeImageListener.onMergeClick();
    }
    public void setMergeImageListener(MergeImageListener mergeImageListener){
        this.mergeImageListener = mergeImageListener;
    }
    public void setShareItemListener(ShareItemListener shareItemListener){
        this.shareItemListener = shareItemListener;
    }
    public void setToolbarIcon(boolean shareVisibility, boolean mergeVisibility){
        if(shareIcon == null || mergeIcon == null)
            return;
        shareIcon.setVisible(shareVisibility);
        mergeIcon.setVisible(mergeVisibility);
    }
}
