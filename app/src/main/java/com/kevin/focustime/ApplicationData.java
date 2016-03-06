package com.kevin.focustime;

import android.app.Application;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lenovo on 2015-08-24.
 */
public class ApplicationData extends Application {
    private List<MyPackageInfo> piList=new ArrayList<>();
    public List<MyPackageInfo> getPiList() {
        return piList;
    }

    public void setPiList(List<MyPackageInfo> piList) {
        this.piList = piList;
    }
}
