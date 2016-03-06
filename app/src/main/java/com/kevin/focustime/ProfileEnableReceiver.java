package com.kevin.focustime;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;


public class ProfileEnableReceiver extends BroadcastReceiver {
    public static final String ACTION="com.kevin.focustime.intent.action.profileEnableReceiver";
    public ProfileEnableReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        context.startService(new Intent(context,ProfileEnableService.class));
    }
}
