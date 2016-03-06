package com.kevin.focustime;

import android.content.SharedPreferences;
import android.util.Log;
import android.view.View;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.TimeZone;

/**
 * Created by lenovo on 2015-08-21.
 */
public class TotalUsageGraph {
    private View totalUsage;
    private static ArrayList<Entry> timeEntry;
    private static ArrayList<String> xVals;
    private static LineChart mLineChart;
    private static SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
    private static SharedPreferences sharedPreferences;
    private static SharedPreferences.Editor editor;
    private static LineDataSet timeDataSet;
    private static int day;

    public static void add(long startTime,long endTime,long usageTime) {
        if(!sdf.format(startTime).equals(xVals.get(xVals.size()-1)))
        {
            timeDataSet.addEntry(new Entry(0, timeEntry.size()));
            xVals.add(sdf.format(startTime));
        }
        xVals.add("");
        timeDataSet.addEntry(new Entry(usageTime, timeEntry.size()));
        xVals.add(sdf.format(endTime));
        timeDataSet.addEntry(new Entry(0, timeEntry.size()));
        mLineChart.invalidate();
        mLineChart.notifyDataSetChanged();
        saveArray();
    }

    public static void init(View totalUsage,SharedPreferences sharedPreferences) {
        TotalUsageGraph.sharedPreferences=sharedPreferences;
        editor=sharedPreferences.edit();
        timeEntry = new ArrayList<Entry>();
        mLineChart = (LineChart) totalUsage.findViewById(R.id.line_chart);
        timeDataSet = new LineDataSet(timeEntry, "Total Usage");
        ArrayList<LineDataSet> dataSets = new ArrayList<LineDataSet>();
        dataSets.add(timeDataSet);
        xVals = new ArrayList<String>();
        loadArray();
        if(timeEntry.size()==0)
        { timeDataSet.addEntry(new Entry(0, timeEntry.size()));
            xVals.add(sdf.format(System.currentTimeMillis()));}
        LineData data = new LineData(xVals, dataSets);
        mLineChart.setData(data);
        XAxis xAxis = mLineChart.getXAxis();
        xAxis.setDrawGridLines(false);
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
//        xAxis.setLabelsToSkip(10);
        YAxis rightAxis = mLineChart.getAxisRight();
        YAxis leftAxis = mLineChart.getAxisLeft();
        rightAxis.setDrawGridLines(false);
        leftAxis.setDrawGridLines(false);
        rightAxis.setEnabled(false);
        leftAxis.setEnabled(true);
        leftAxis.setAxisMaxValue(30f);
//        mLineChart.setVisibleYRangeMaximum(20f, YAxis.AxisDependency.LEFT);
        data.setValueFormatter(new MyValueFormatter());
        timeDataSet.setValueFormatter(new MyValueFormatter());
        Legend legend = mLineChart.getLegend();
        legend.setEnabled(false);
        timeDataSet.setLineWidth(2f);
        timeDataSet.setCircleSize(5f);
        timeDataSet.setDrawFilled(true);
        mLineChart.setDescription(null);
        mLineChart.invalidate();
        Calendar c = Calendar.getInstance();
        c.setTimeZone(TimeZone.getTimeZone("GMT+8:00"));
        day = c.get(Calendar.DAY_OF_MONTH);
    }
    public static void loadArray(){
        int size = sharedPreferences.getInt("Array_size", 0);
        for(int i=0;i<size;i++) {
            xVals.add(sharedPreferences.getString(day+"timeString" + i, null));
            timeEntry.add(new Entry(sharedPreferences.getFloat(day+"timeFloat"+i,0),timeEntry.size()));
        }
    }
    public static boolean saveArray() {
        editor.putInt("Array_size",timeEntry.size());
        for(int i=0;i<timeEntry.size();i++) {
            editor.putString(day+"timeString" + i, xVals.get(i));
            editor.putFloat(day+"timeFloat" + i, timeEntry.get(i).getVal());
        }

        return editor.commit();
    }
}

