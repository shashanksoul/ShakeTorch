package com.example.shaketorch.shaker;

import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.util.Log;



public class ShakeListener implements SensorEventListener {
    private static final int SHAKE_SLOP_TIME_MS = 500;
    private long mShakeTimestamp;
    private int mShakeCount;
    private ShakeOptions shakeOptions;
    private Context context;

    public ShakeListener() {
    }

    public ShakeListener(ShakeOptions shakeOptions) {
        this.shakeOptions = shakeOptions;
    }

    public ShakeListener(ShakeOptions shakeOptions, Context context) {
        this.shakeOptions = shakeOptions;
        this.context = context;
    }

    public ShakeListener(ShakeOptions shakeOptions, Context context, ShakeCallback callback) {
        this.shakeOptions = shakeOptions;
        this.context = context;
    }

    public void resetShakeCount() {
        this.mShakeCount = 0;
    }

    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }

    public void onSensorChanged(SensorEvent event) {
        float x = event.values[0];
        float y = event.values[1];
        float z = event.values[2];
        float gX = x / 9.80665F;
        float gY = y / 9.80665F;
        float gZ = z / 9.80665F;
        float gForce = (float)Math.sqrt((double)(gX * gX + gY * gY + gZ * gZ));
        if (gForce > this.shakeOptions.getSensibility()) {
            Log.d("LISTENER", "force: " + gForce + " count: " + this.mShakeCount);
            long now = System.currentTimeMillis();
            if (this.mShakeTimestamp + 500L > now) {
                return;
            }

            if (this.mShakeTimestamp + (long)this.shakeOptions.getInterval() < now) {
                this.mShakeCount = 0;
            }

            this.mShakeTimestamp = now;
            ++this.mShakeCount;
            if (this.shakeOptions.getShakeCounts() == this.mShakeCount) {
                this.sendToBroadCasts(this.context);
                this.sendToPrivateBroadCasts(this.context);
            }
        }

    }

    private void sendToBroadCasts(Context context) {
        Intent locationIntent = new Intent();
        locationIntent.setAction("shake.detector");
        context.sendBroadcast(locationIntent);
    }

    private void sendToPrivateBroadCasts(Context context) {
        Intent locationIntent = new Intent();
        locationIntent.setAction("private.shake.detector");
        context.sendBroadcast(locationIntent);
    }

    public interface OnShakeListener {
        void onShake(int var1);
    }
}

