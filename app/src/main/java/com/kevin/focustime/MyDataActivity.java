package com.kevin.focustime;

import android.app.Activity;
import android.app.usage.UsageStats;
import android.app.usage.UsageStatsManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;

import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.ogaclejapan.smarttablayout.SmartTabLayout;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.TimeZone;


public class MyDataActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {
    private ViewPager viewPager;
    private UsageStatsManager usageStatsManager;
    private List<UsageStats> queryUsageStats;
    private List<MyPackageInfo> piList;
    private SmartTabLayout viewPagerTab;
    private View totalUsage;
    private View appUsage;
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mDrawerToggle;
    private ListView mDrawerList;
    private List<MainMenu> mainMenuList = new ArrayList<MainMenu>();
    private Intent menuIntent;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_data);
        LayoutInflater inflater = getLayoutInflater();
        totalUsage = inflater.inflate(R.layout.total_usage, null);
        appUsage = inflater.inflate(R.layout.app_usage, null);
        SharedPreferences sharedPreferences=getSharedPreferences("total_usage_data",Activity.MODE_PRIVATE);
        TotalUsageGraph.init(totalUsage,sharedPreferences);
        initAppList();
        initViewPager();
        initDrawer();
        initMenu();
    }

    private void initDrawer() {
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerList = (ListView) findViewById(R.id.left_drawer);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("My Statistics");
        setSupportActionBar(toolbar);

        mDrawerList.setAdapter(new MenuAdapter(MyDataActivity.this, R.layout.drawer_menu, mainMenuList));
        mDrawerList.setOnItemClickListener(this);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, toolbar, R.string.action_settings, R.string.app_name) {
            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
            }
        };
        mDrawerToggle.syncState();
        mDrawerLayout.setDrawerListener(mDrawerToggle);
    }

    private void initViewPager() {
        ListView listView = (ListView) appUsage.findViewById(R.id.listview);
        listView.setAdapter(new AppUsageListAdapter(this, piList));
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            }
        });
        viewPager = (ViewPager) findViewById(R.id.view_pager);
        List<View> views = new ArrayList<View>();
        views.add(totalUsage);
        views.add(appUsage);
        List<String> titles;
        titles = new ArrayList<String>();
        titles.add("Total Usage");
        titles.add("Apps Usage");
        MyDataViewPagerAdapter adapter = new MyDataViewPagerAdapter(this, views, titles);
        viewPager.setAdapter(adapter);

        viewPagerTab = (SmartTabLayout) findViewById(R.id.viewpagertab);
        viewPagerTab.setBackgroundColor(getResources().getColor(R.color.fbutton_color_peter_river));
        viewPagerTab.setViewPager(viewPager);
    }


    private void initAppList() {
        Calendar c = Calendar.getInstance();
        c.setTimeZone(TimeZone.getTimeZone("GMT+8:00"));
        piList = ((ApplicationData)getApplicationContext()).getPiList();
        usageStatsManager = (UsageStatsManager) this.getSystemService("usagestats");
        Calendar beginCal = Calendar.getInstance();
        beginCal.set(Calendar.DATE, c.get(Calendar.DAY_OF_MONTH)-1);
        beginCal.set(Calendar.MONTH,  c.get(Calendar.MONTH));
        beginCal.set(Calendar.YEAR, c.get(Calendar.YEAR));

        Calendar endCal = Calendar.getInstance();
        endCal.set(Calendar.DATE, c.get(Calendar.DAY_OF_MONTH));
        endCal.set(Calendar.MONTH, c.get(Calendar.MONTH));
        endCal.set(Calendar.YEAR, c.get(Calendar.YEAR));
        queryUsageStats = usageStatsManager.queryUsageStats(UsageStatsManager.INTERVAL_BEST, beginCal.getTimeInMillis(), endCal.getTimeInMillis());
        String packageName;
        String packageInfoName;

        for (UsageStats usageStats : queryUsageStats) {
            packageName = usageStats.getPackageName();
            long time = usageStats.getTotalTimeInForeground();
            for (MyPackageInfo myPackageInfo : piList) {
                packageInfoName = myPackageInfo.getPackageName();
                if (packageName.equals(packageInfoName)) {
                    myPackageInfo.setPackageUsageTime(time);
                }
            }
        }
        Iterator<MyPackageInfo> iterator = piList.iterator();
        MyPackageInfo i;
        i = iterator.next();
        while (iterator.hasNext()) {
            if (i.getPackageUsageTimeString() == null || i.getPackageUsageTimeString().equals("00:00:00"))
                iterator.remove();
            i = iterator.next();
        }
        Collections.sort(piList);
    }

    private void initMenu() {
        MainMenu focusTime = new MainMenu("take FocusTime", R.drawable.ic_do_not_disturb_black_36dp);
        MainMenu MyStatistics = new MainMenu("My statistics", R.drawable.statistics);
        MainMenu settings = new MainMenu("settings", R.drawable.app_settings);
        mainMenuList.add(focusTime);
        mainMenuList.add(MyStatistics);
        mainMenuList.add(settings);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the drawer_menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_my_data, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        switch (position) {
            case 0:
                startActivity(new Intent(MyDataActivity.this, FocusTimeActivity.class));
                mDrawerLayout.closeDrawer(Gravity.LEFT);
                break;
            case 1:
                mDrawerLayout.closeDrawer(Gravity.LEFT);
                break;
            case 2:
                startActivity(new Intent(MyDataActivity.this,SettingsActivity.class));
                mDrawerLayout.closeDrawer(Gravity.LEFT);
                break;
        }
    }

}
