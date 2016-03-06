package com.kevin.focustime;

import android.app.Service;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Binder;
import android.os.IBinder;
import android.view.View;

import java.util.Timer;
import java.util.TimerTask;

public class TotalUsageService extends Service {
    private Callback callback;
    private ScreenStateReceiver screenStateReceiver;

    @Override
    public void onCreate() {
        super.onCreate();
        IntentFilter filter = new IntentFilter();
        filter.addAction(Intent.ACTION_SCREEN_ON);
        filter.addAction(Intent.ACTION_SCREEN_OFF);
        screenStateReceiver=new ScreenStateReceiver();
        registerReceiver(screenStateReceiver, filter);
    }


    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    public static interface Callback{
        void onDataChange(long time);
    }
}
