package com.kevin.focustime;

import android.app.ActivityManager;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;


public class SettingsActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {
    private Switch aSwitch;
    private Intent serviceStartIntent;
    private AlertDialog.Builder aboutDialog;
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mDrawerToggle;
    private ListView mDrawerList;
    private List<MainMenu> mainMenuList = new ArrayList<MainMenu>();
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        initDrawer();
        initMenu();
        Button check = (Button) findViewById(R.id.check);
        aSwitch = (Switch) findViewById(R.id.aSwitch);
        TextView about = (TextView) findViewById(R.id.about);
        serviceStartIntent = new Intent(this, TotalUsageService.class);
        aboutDialog = new AlertDialog.Builder(SettingsActivity.this);
        aboutDialog.setTitle("About");
        aboutDialog.setMessage("Developed by Kevin.\n" +
                "All Rights Reserved.");
        aboutDialog.setPositiveButton("OK", null);
        if (isServiceWork("com.kevin.focustime.TotalUsageService"))
            aSwitch.setChecked(true);
        aSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    startService(serviceStartIntent);
                } else {
                    stopService(serviceStartIntent);
                }
            }
        });
        check.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS);
                startActivity(intent);
            }
        });
        about.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                aboutDialog.show();
            }
        });
    }

    private void initDrawer() {
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerList = (ListView) findViewById(R.id.left_drawer);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mDrawerList.setAdapter(new MenuAdapter(SettingsActivity.this, R.layout.drawer_menu, mainMenuList));
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


    private void initMenu() {
        MainMenu focusTime = new MainMenu("take FocusTime", R.drawable.ic_do_not_disturb_black_36dp);
        MainMenu MyStatistics = new MainMenu("My statistics", R.drawable.statistics);
        MainMenu settings = new MainMenu("settings", R.drawable.app_settings);
        mainMenuList.add(focusTime);
        mainMenuList.add(MyStatistics);
        mainMenuList.add(settings);
    }

    public boolean isServiceWork(String serviceName) {
        boolean isWork = false;
        ActivityManager myAM = (ActivityManager) this.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningServiceInfo> myList = myAM.getRunningServices(100);
        if (myList.size() <= 0) {
            return false;
        }
        for (int i = 0; i < myList.size(); i++) {
            String mName = myList.get(i).service.getClassName();
            if (mName.equals(serviceName)) {
                isWork = true;
                break;
            }
        }
        return isWork;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_settings, menu);
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
                startActivity(new Intent(SettingsActivity.this, FocusTimeActivity.class));
                mDrawerLayout.closeDrawer(Gravity.LEFT);
                break;
            case 1:
                startActivity(new Intent(SettingsActivity.this, MyDataActivity.class));
                mDrawerLayout.closeDrawer(Gravity.LEFT);
                break;
            case 2:
                mDrawerLayout.closeDrawer(Gravity.LEFT);
                break;
        }
    }
}
