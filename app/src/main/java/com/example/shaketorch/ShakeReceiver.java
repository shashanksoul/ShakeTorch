package com.example.shaketorch;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.example.shaketorch.shaker.ShakeDetector;
import com.example.shaketorch.shaker.ShakeOptions;

public class ShakeReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if (null != intent && intent.getAction().equals("shake.detector")) {
            Toast.makeText(context, "Shake detect", Toast.LENGTH_SHORT).show();
            Log.d("sss","Shake");
        }
    }
}
