package com.example.shaketorch.shaker;


import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.Build;
import android.os.IBinder;
import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import com.example.shaketorch.MainActivity;
import com.example.shaketorch.R;

import java.util.List;



public class ShakeService extends Service {
    private String channelId="channelId";
    private AppPreferences appPreferences;
    private ShakeOptions shakeOptions;
    private ShakeListener shakeListener;
    private SensorManager sensorManager;
    private Sensor sensor;

    public ShakeService() {
    }

    public void onCreate() {
        this.appPreferences = new AppPreferences(this.getBaseContext());
        super.onCreate();
    }

    public int onStartCommand(Intent intent, int flags, int startId) {
        createNotificationChannel();


        Intent notificationIntent = new Intent(this, MainActivity.class);
        PendingIntent pendingIntent =
                PendingIntent.getActivity(this, 0, notificationIntent, 0);

        Notification notification = new NotificationCompat.Builder(this, channelId)
                        .setContentTitle("Shake tourch")
                        .setContentText("Shake tourch running")
                        .setSmallIcon(R.drawable.ic_launcher_foreground)
                        .setContentIntent(pendingIntent)
                        .build();

// Notification ID cannot be 0.
        startForeground(1, notification);
        this.shakeOptions = (new ShakeOptions()).background(this.appPreferences.getBoolean("BACKGROUND", true)).sensibility(this.appPreferences.getFloat("SENSIBILITY", 1.2F)).shakeCount(this.appPreferences.getInt("SHAKE_COUNT", 1)).interval(this.appPreferences.getInt("INTERVAL", 2000));
        this.startShakeService(this.getBaseContext());
        return this.shakeOptions.isBackground() ? Service.START_STICKY : Service.START_NOT_STICKY;
    }

    public void startShakeService(Context context) {
        this.shakeListener = new ShakeListener(this.shakeOptions, context);
        this.sensorManager = (SensorManager)context.getSystemService(Context.SENSOR_SERVICE);
        List<Sensor> sensors = this.sensorManager.getSensorList(1);
        if (sensors.size() > 0) {
            this.sensor = (Sensor)sensors.get(0);
            this.sensorManager.registerListener(this.shakeListener, this.sensor, 1);
        }

    }

    public void onDestroy() {
        stopForeground(true);
        stopSelf();
        if (sensorManager!=null)
        this.sensorManager.unregisterListener(this.shakeListener);
        super.onDestroy();
    }

    private void createNotificationChannel(){
        if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.O){
            NotificationChannel notificationChannel=new NotificationChannel(
              channelId,"Foreground", NotificationManager.IMPORTANCE_DEFAULT
            );

            NotificationManager manager= (NotificationManager) getSystemService(Service.NOTIFICATION_SERVICE);
            manager.createNotificationChannel(notificationChannel);

        }
    }

    @Nullable
    public IBinder onBind(Intent intent) {
        return null;
    }
}
