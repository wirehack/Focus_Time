package com.kevin.focustime;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class ScreenStateReceiver extends BroadcastReceiver {
    private String action;
    private long startTime;
    private long endTime;
    private long usageTime;
    public ScreenStateReceiver() {
    }

    public long getUsageTime() {
        return usageTime;
    }

    public void onReceive(Context context, Intent intent) {
        action = intent.getAction();
        if (Intent.ACTION_SCREEN_ON.equals(action)) {
            startTime=System.currentTimeMillis();
        } else if (Intent.ACTION_SCREEN_OFF.equals(action)) {
            endTime=System.currentTimeMillis();
            usageTime=(endTime-startTime)/60000;
            if(usageTime>24000 || usageTime==0)
                return;
            TotalUsageGraph.add(startTime,endTime,usageTime);
        }
    }
}
