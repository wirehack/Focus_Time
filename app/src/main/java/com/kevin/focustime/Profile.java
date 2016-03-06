package com.kevin.focustime;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by lenovo on 2015-08-20.
 */
public class Profile implements Serializable{
    private boolean notifications = true;
    private boolean callsAndSMS = true;
    private boolean apps = true;
    private String name;
    private List<MyPackageInfo> piList;

    public Profile() {
        piList=new ArrayList<>();
    }

    public List<MyPackageInfo> getPiList() {
        return piList;
    }

    public void setPiList(List<MyPackageInfo> piList) {
        this.piList = piList;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isNotifications() {
        return notifications;
    }

    public void setNotifications(boolean notifications) {
        this.notifications = notifications;
    }

    public boolean isCallsAndSMS() {
        return callsAndSMS;
    }

    public void setCallsAndSMS(boolean callsAndSMS) {
        this.callsAndSMS = callsAndSMS;
    }

    public boolean isApps() {
        return apps;
    }

    public void setApps(boolean apps) {
        this.apps = apps;
    }
}
