package com.example.shaketorch;

import android.content.Context;
import android.content.DialogInterface;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraManager;
import android.os.Build;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.shaketorch.shaker.ShakeDetector;
import com.example.shaketorch.shaker.ShakeOptions;

public class MainActivity extends AppCompatActivity {
    ShakeReceiver receiver=new ShakeReceiver();
    private CameraManager mCameraManager;
    private String mCameraId;
    private boolean torchStatus=false;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        boolean isFlashAvailable = getApplicationContext().getPackageManager()
                .hasSystemFeature(PackageManager.FEATURE_CAMERA);

        /*ShakeOptions options = new ShakeOptions()
                .background(true)
                .interval(1000)
                .shakeCount(2)
                .sensibility(2.0f);

        this.shakeDetector = new ShakeDetector(options).start(this);*/




        if (!isFlashAvailable) {
            showNoFlashError();
        }else {
            IntentFilter filter = new IntentFilter();
            filter.addAction("shake.detector");
            filter.addAction("android.intent.action.BOOT_COMPLETED");
            registerReceiver(receiver, filter);
        }

    }

;



    @RequiresApi(api = Build.VERSION_CODES.M)
    private void  onOffTorch(boolean status){
        mCameraManager = (CameraManager) getSystemService(Context.CAMERA_SERVICE);
        try {
            mCameraId = mCameraManager.getCameraIdList()[0];
            mCameraManager.setTorchMode(mCameraId, status);
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
    }
    public void showNoFlashError() {
        AlertDialog alert = new AlertDialog.Builder(this)
                .create();
        alert.setTitle("Oops!");
        alert.setMessage("Flash not available in this device...");
        alert.setButton(DialogInterface.BUTTON_POSITIVE, "OK", (dialog, which) -> finish());
        alert.show();
    }


}