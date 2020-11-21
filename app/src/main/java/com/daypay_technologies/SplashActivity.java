package com.daypay_technologies;



import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;


import android.os.Environment;
import android.provider.Settings;
import android.support.annotation.RequiresApi;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;
import android.view.WindowManager;


import java.io.File;

import static android.content.Context.MODE_PRIVATE;

public class SplashActivity extends AppCompatActivity {


    CountDownTimer countDownTimer;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_splash);
        setCountDownTimer();
        if (android.os.Build.VERSION.SDK_INT > 23) {
            if(checkPermission()){
                setRootfolder();
                countDownTimer.start();
            }
            else{
                countDownTimer.start();
            }

        }
        else{
            setRootfolder();
            countDownTimer.start();
        }
    }
    private void setCountDownTimer(){
        countDownTimer = new CountDownTimer(2500, 500) {
            public void onTick(long millisUntilFinished) {
            }
            public void onFinish() {
                Intent intent=new Intent(SplashActivity.this, LandingPageActivity.class);
                if(getIntent().getExtras()!=null) {
                    intent.putExtras(getIntent().getExtras());
                    setIntent(null);
                }
                startActivity(intent);
                finish();
            }
        };
    }

    public void setRootfolder(){
        File rootDir = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/DocumentScanner");
        if(!rootDir.exists())
            rootDir.mkdirs();
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
    @Override
    protected void onDestroy() {
        super.onDestroy();
        countDownTimer.cancel();
    }

}
