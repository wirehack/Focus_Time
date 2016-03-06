package com.kevin.focustime;

import java.util.Comparator;

/**
 * Created by lenovo on 2015-08-27.
 */
public class UsedTimesComparator implements Comparator<MyPackageInfo> {
    @Override
    public int compare(MyPackageInfo lhs, MyPackageInfo rhs) {
        return Integer.compare(rhs.getUsedTimes(),lhs.getUsedTimes());
    }
}
