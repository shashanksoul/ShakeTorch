package com.example.shaketorch.shaker;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.Build;

import java.util.List;




public class ShakeDetector {
    private SensorManager sensorManager;
    private Context context;
    private ShakeCallback shakeCallback;
    private Sensor sensor;
    private boolean isRunning;
    private ShakeOptions shakeOptions;
    private AppPreferences appPreferences;
    private ShakeBroadCastReceiver shakeBroadCastReceiver;
    private ShakeListener shakeListener;

    public ShakeDetector() {
    }

    public ShakeDetector(ShakeOptions shakeOptions) {
        this.shakeOptions = shakeOptions;
    }

    public ShakeDetector start(Context context, ShakeCallback shakeCallback) {
        this.shakeCallback = shakeCallback;
        this.shakeBroadCastReceiver = new ShakeBroadCastReceiver(shakeCallback);
        this.registerPrivateBroadCast(context);
        this.saveOptionsInStorage(context);
        this.startShakeService(context);
        return this;
    }

    public ShakeDetector start(Context context) {
        this.saveOptionsInStorage(context);
        this.startShakeService(context);
        return this;
    }

    public void destroy(Context context) {
        if (this.shakeBroadCastReceiver != null) {
            context.unregisterReceiver(this.shakeBroadCastReceiver);
        }

    }

    public void stopShakeDetector(Context context) {
        context.stopService(new Intent(context, ShakeService.class));
    }

    private void startShakeService(Context context) {
        Intent serviceIntent = new Intent(context, ShakeService.class);
        if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.O){
            context.startForegroundService(serviceIntent);
        }else {
            context.startService(serviceIntent);
        }

    }

    public ShakeDetector startService(Context context) {
        this.shakeListener = new ShakeListener(this.shakeOptions, context);
        this.sensorManager = (SensorManager)context.getSystemService("sensor");
        List<Sensor> sensors = this.sensorManager.getSensorList(1);
        if (sensors.size() > 0) {
            this.sensor = (Sensor)sensors.get(0);
            this.isRunning = this.sensorManager.registerListener(this.shakeListener, this.sensor, 1);
        }

        return this;
    }

    public void saveOptionsInStorage(Context context) {
        this.appPreferences = new AppPreferences(context);
        this.appPreferences.putBoolean("BACKGROUND", this.shakeOptions.isBackground());
        this.appPreferences.putInt("SHAKE_COUNT", this.shakeOptions.getShakeCounts());
        this.appPreferences.putInt("INTERVAL", this.shakeOptions.getInterval());
        this.appPreferences.putFloat("SENSIBILITY", this.shakeOptions.getSensibility());
    }

    private void registerPrivateBroadCast(Context context) {
        IntentFilter filter = new IntentFilter();
        filter.addAction("shake.detector");
        filter.addAction("private.shake.detector");
        context.registerReceiver(this.shakeBroadCastReceiver, filter);
    }

    public Boolean isRunning() {
        return this.isRunning;
    }
}

/*
public class ShakeDetector implements SensorEventListener {

    */
/*
     * The gForce that is necessary to register as shake.
     * Must be greater than 1G (one earth gravity unit).
     * You can install "G-Force", by Blake La Pierre
     * from the Google Play Store and run it to see how
     *  many G's it takes to register a shake
     *//*

    private static final float SHAKE_THRESHOLD_GRAVITY = 2.7F;
    private static final int SHAKE_SLOP_TIME_MS = 200;
    private static final int SHAKE_COUNT_RESET_TIME_MS = 3000;

    private OnShakeListener mListener;
    private long mShakeTimestamp;
    private int mShakeCount;

    public void setOnShakeListener(OnShakeListener listener) {
        this.mListener = listener;
    }

    public interface OnShakeListener {
        public void onShake(int count);
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        // ignore
    }

    @Override
    public void onSensorChanged(SensorEvent event) {

        if (mListener != null) {
            float x = event.values[0];
            float y = event.values[1];
            float z = event.values[2];

            float gX = x / SensorManager.GRAVITY_EARTH;
            float gY = y / SensorManager.GRAVITY_EARTH;
            float gZ = z / SensorManager.GRAVITY_EARTH;

            // gForce will be close to 1 when there is no movement.
            double gForce = sqrt(gX * gX + gY * gY + gZ * gZ);

            if (gForce > SHAKE_THRESHOLD_GRAVITY) {
                final long now = System.currentTimeMillis();
                // ignore shake events too close to each other (500ms)
                if (mShakeTimestamp + SHAKE_SLOP_TIME_MS > now) {
                    return;
                }

                // reset the shake count after 3 seconds of no shakes
                if (mShakeTimestamp + SHAKE_COUNT_RESET_TIME_MS < now) {
                    mShakeCount = 0;
                }

                mShakeTimestamp = now;
                mShakeCount++;

                mListener.onShake(mShakeCount);
            }
        }
    }

}*/