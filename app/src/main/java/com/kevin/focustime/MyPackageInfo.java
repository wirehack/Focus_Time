package com.kevin.focustime;

/**
 * Created by lenovo on 2015-08-17.
 */
import android.graphics.drawable.Drawable;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.TimeZone;

public class MyPackageInfo implements Comparable<MyPackageInfo>{
    private Drawable packageIcon;
    private String packageLabel = "";
    private String packageName = "";
    private long packageUsageTime;
    private String packageTimeString;
    private static SimpleDateFormat sdf = new SimpleDateFormat("HH ±mm∑÷ss√Î");

    public int getUsedTimes() {
        return usedTimes;
    }

    public void setUsedTimes(int usedTimes) {
        this.usedTimes = usedTimes;
    }

    private int usedTimes=0;
    public String getPackageUsageTimeString() {
        return packageTimeString;
    }

    public void setPackageUsageTime(long packageUsageTime) {
        this.packageUsageTime = packageUsageTime;
        long time=packageUsageTime - TimeZone.getDefault().getRawOffset();
        packageTimeString=sdf.format(time);
    }

    public long getPackageUsageTime() {
        return packageUsageTime;
    }

    public Drawable getPackageIcon()

    {

        return packageIcon;

    }


    public void setPackageIcon(Drawable drawable)

    {

        this.packageIcon = drawable;

    }


    public String getPackageLabel()

    {

        return packageLabel;

    }


    public void setPackageLabel(String packageLabel)

    {

        this.packageLabel = packageLabel;

    }

    public String getPackageName()

    {

        return packageName;

    }


    public void setPackageName(String packageName)

    {

        this.packageName = packageName;

    }


    @Override
    public int compareTo(MyPackageInfo another) {
        return Long.compare(another.packageUsageTime,packageUsageTime);
    }


}
