package com.example.shaketorch.shaker;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;



public class ShakeBroadCastReceiver extends BroadcastReceiver {
    private ShakeCallback callback;

    public ShakeBroadCastReceiver() {
    }

    public ShakeBroadCastReceiver(ShakeCallback callback) {
        this.callback = callback;
    }

    public void onReceive(Context context, Intent intent) {
        if (null != intent && intent.getAction().equals("private.shake.detector")) {
            this.callback.onShake();
        }

    }
}
