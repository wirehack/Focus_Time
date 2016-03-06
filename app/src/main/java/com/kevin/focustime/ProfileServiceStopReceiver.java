package com.kevin.focustime;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class ProfileServiceStopReceiver extends BroadcastReceiver {
    public final static String ACTION = "com.kevin.focustime.intent.action.profileServiceStopReceiver";
    private int screenUnlockTimes = 0;

    public ProfileServiceStopReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        if (Intent.ACTION_USER_PRESENT.equals(action)) {
            System.out.println("unlock");
            screenUnlockTimes++;
        } else {
            System.out.println("stop");
            Intent activityIntent = new Intent(context, ProfileStopActivity.class);
            activityIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            activityIntent.putExtra("unlock_times", screenUnlockTimes);
            activityIntent.putExtra("start_time", intent.getLongExtra("start_time", 0));
            context.startActivity(activityIntent);
            context.stopService(new Intent(context, ProfileEnableService.class));
            screenUnlockTimes=0;
        }
    }
}
