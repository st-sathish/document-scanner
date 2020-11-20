package com.daypay_technologies.fragments;

import android.hardware.Camera;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.daypay_technologies.R;
import com.daypay_technologies.widgets.CameraView;

import java.util.ArrayList;

public class CameraFragment extends BaseFragment {

    private CameraView cameraView;
    private Camera camera;

    public static CameraFragment newInstance() {
        CameraFragment cameraFragment = new CameraFragment();
        return cameraFragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fr_camera,container,false);
        cameraView = new CameraView(getActivity(), (SurfaceView) view.findViewById(R.id.surfaceView));
        cameraView.setLayoutParams(new WindowManager.LayoutParams(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT));
        ((FrameLayout) view.findViewById(R.id.layout)).addView(cameraView);
        cameraView.setKeepScreenOn(true);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        int numCams = Camera.getNumberOfCameras();
        if(numCams > 0) {
            try {
                camera = Camera.open(0);
                camera.startPreview();
                cameraView.setCamera(camera);
            } catch (RuntimeException ex) {
                Toast.makeText(getActivity(), getString(R.string.camera_not_found), Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    public void onPause() {
        if(camera != null) {
            camera.stopPreview();
            cameraView.setCamera(null);
            camera.release();
            camera = null;
        }
        super.onPause();
    }

    private void resetCam() {
        camera.startPreview();
        cameraView.setCamera(camera);
    }
}
