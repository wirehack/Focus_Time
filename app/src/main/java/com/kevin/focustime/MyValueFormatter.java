package com.kevin.focustime;

import com.github.mikephil.charting.utils.ValueFormatter;

/**
 * Created by lenovo on 2015-08-01.
 */
public class MyValueFormatter implements ValueFormatter {
    public MyValueFormatter() {
    }
    @Override
    public String getFormattedValue(float v) {
        if(v==0)
            return "";
        return Math.round(v)+"mintues";
    }
}
