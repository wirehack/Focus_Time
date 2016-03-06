package com.kevin.focustime;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.app.usage.UsageStats;
import android.app.usage.UsageStatsManager;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Binder;
import android.os.IBinder;
import android.os.PowerManager;
import android.os.SystemClock;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.TimeZone;

public class ProfileEnableService extends Service {
    private List<UsageStats> queryUsageStats;
    private UsageStats max;
    private Calendar beginCal;
    private Calendar endCal;
    private UsageStatsManager usageStatsManager;
    private boolean apps;
    private List<String> packageNameList = new ArrayList<>();
    private Intent homeIntent;
    private PendingIntent pendingIntent;
    private AlarmManager alarmManager;
    private AlarmManager duration;
    private Intent broadcastIntent;
    private PowerManager.WakeLock mWakeLock;
    private PowerManager pm;
    private long durationTime;
    private PendingIntent stopServicePendingIntent;
    private Intent stopServiceIntent;
    private List<MyPackageInfo> piList;
    private ProfileServiceStopReceiver profileServiceStopReceiver;

    public ProfileEnableService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        return new EnableBinder();
    }

    public class EnableBinder extends Binder {

        public void setPackageNameList(List<String> packageNameList) {
            ProfileEnableService.this.packageNameList = packageNameList;
        }

        public void setApps(boolean apps) {
            ProfileEnableService.this.apps = apps;
        }

        public void setDuration(long time) {
            ProfileEnableService.this.durationTime = time;
            duration.setExact(AlarmManager.RTC_WAKEUP, ProfileEnableService.this.durationTime, stopServicePendingIntent);
        }

        public AlarmManager getAlarmManager() {
            return duration;
        }

        public PendingIntent getPendingIntent() {
            return stopServicePendingIntent;
        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (apps) {
            System.out.println("success");
            queryUsageStats = usageStatsManager.queryUsageStats(UsageStatsManager.INTERVAL_DAILY, beginCal.getTimeInMillis(), endCal.getTimeInMillis());
            max = queryUsageStats.get(0);
            for (UsageStats usageStats : queryUsageStats) {
                if (usageStats.getLastTimeUsed() > max.getLastTimeUsed())
                    max = usageStats;
            }
            if (packageNameList.contains(max.getPackageName())) {
                for (MyPackageInfo myPackageInfo : piList) {
                    if (myPackageInfo.getPackageName().equals(max.getPackageName())) {
                        myPackageInfo.setUsedTimes(myPackageInfo.getUsedTimes() + 1);
                    }
                }
                startActivity(homeIntent);
            }
            alarmManager.setExact(AlarmManager.ELAPSED_REALTIME_WAKEUP, SystemClock.elapsedRealtime() + 1000, pendingIntent);
            System.out.println();
        }
        return super.onStartCommand(intent, flags, startId);

    }

    @Override
    public void onCreate() {
        super.onCreate();
        piList = ((ApplicationData) getApplicationContext()).getPiList();
        pm = (PowerManager) this.getSystemService(Context.POWER_SERVICE);
        mWakeLock = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK | PowerManager.ACQUIRE_CAUSES_WAKEUP, "");
        mWakeLock.acquire();
        usageStatsManager = (UsageStatsManager) this.getSystemService("usagestats");
        Calendar c = Calendar.getInstance();
        c.setTimeZone(TimeZone.getTimeZone("GMT+8:00"));
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);
        beginCal = Calendar.getInstance();
        beginCal.set(Calendar.DATE, day-1);
        beginCal.set(Calendar.MONTH, month);
        beginCal.set(Calendar.YEAR, year);

        endCal = Calendar.getInstance();
        endCal.set(Calendar.DATE, day);
        endCal.set(Calendar.MONTH, month);
        endCal.set(Calendar.YEAR, year);
        homeIntent = new Intent(Intent.ACTION_MAIN);
        homeIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        homeIntent.addCategory(Intent.CATEGORY_HOME);
        broadcastIntent = new Intent(ProfileEnableReceiver.ACTION);
        pendingIntent = PendingIntent.getBroadcast(this, 0, broadcastIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        duration = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        stopServiceIntent = new Intent(ProfileServiceStopReceiver.ACTION);
        stopServiceIntent.putExtra("start_time", System.currentTimeMillis());
        stopServicePendingIntent = PendingIntent.getBroadcast(this, 0, stopServiceIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        IntentFilter filter = new IntentFilter();
        filter.addAction(Intent.ACTION_USER_PRESENT);
        filter.addAction(ProfileServiceStopReceiver.ACTION);
        profileServiceStopReceiver = new ProfileServiceStopReceiver();
        registerReceiver(profileServiceStopReceiver, filter);
    }

    @Override
    public void onDestroy() {
        alarmManager.cancel(pendingIntent);
        if (null != mWakeLock) {
            mWakeLock.release();
            mWakeLock = null;
        }
        unregisterReceiver(profileServiceStopReceiver);
        ProfileEnableActivity.instance.finish();
        super.onDestroy();
    }
}


