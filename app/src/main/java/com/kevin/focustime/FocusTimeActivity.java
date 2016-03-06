package com.kevin.focustime;


import android.app.Activity;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.List;
import java.util.TimeZone;
import java.util.TreeSet;


public class FocusTimeActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mDrawerToggle;
    private ListView mDrawerList;
    private List<MainMenu> mainMenuList = new ArrayList<MainMenu>();
    private Spinner spinner;
    private ArrayAdapter<String> spinnerAdapter;
    private Button startTimePicker;
    private TextView startTimeTextView;
    private Button endTimePicker;
    private TextView endTimeTextView;
    private Toolbar toolbar;
    private Button profileSettings;
    private Button startProfile;
    private List<MyPackageInfo> piList;
    private List<String> packageNameList = new ArrayList<>();
    private TreeSet<String> profileNameSet = new TreeSet<>();
    private List<String> profileNameList = new ArrayList<>();
    private String enabledProfileName;
    private SharedPreferences sharedPreferences;
    private long startTime;
    private long endTime;
    private int year;
    private int month;
    private int day;
    private String timeString;
    public static Activity instance;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_focus_time);
        Calendar c = Calendar.getInstance();
        c.setTimeZone(TimeZone.getTimeZone("GMT+8:00"));
        year = c.get(Calendar.YEAR);
        month = c.get(Calendar.MONTH);
        day = c.get(Calendar.DAY_OF_MONTH);
        initDrawer();
        initMenu();
        piList = getPhonePackageInfo();
        Iterator<MyPackageInfo> iterator = piList.iterator();
        MyPackageInfo myPackageInfo;
        while (iterator.hasNext()) {
            myPackageInfo = iterator.next();
            if (myPackageInfo.getPackageName().equals("com.kevin.focustime")) {
                iterator.remove();
                break;
            }
        }
        ((ApplicationData) getApplicationContext()).setPiList(piList);
        sharedPreferences = getSharedPreferences("profile_name", MODE_PRIVATE);
        profileNameSet.addAll(sharedPreferences.getStringSet("profile_name", new TreeSet<String>()));
        for (MyPackageInfo packageInfo : piList) {
            packageNameList.add(packageInfo.getPackageName());
        }
        initProfileView();
        startService(new Intent(this, TotalUsageService.class));
        instance=this;
    }

    private void initProfileView() {
        spinner = (Spinner) findViewById(R.id.spinner);
        startTimePicker = (Button) findViewById(R.id.start_time_picker);
        startTimeTextView = (TextView) findViewById(R.id.start_time);
        endTimePicker = (Button) findViewById(R.id.end_time_picker);
        endTimeTextView = (TextView) findViewById(R.id.end_time);
        profileSettings = (Button) findViewById(R.id.profile_settings);
        startProfile = (Button) findViewById(R.id.start_profile);

        SimpleDateFormat formatter = new SimpleDateFormat("HH:mm");
        String currentTime = formatter.format(System.currentTimeMillis());
        startTimeTextView.setText(currentTime);
        endTimeTextView.setText(currentTime);
        profileNameList.addAll(profileNameSet);
        Collections.sort(profileNameList);
        spinnerAdapter = new ArrayAdapter<String>(this, R.layout.spinner_item, profileNameList);
        spinner.setAdapter(spinnerAdapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                enabledProfileName = profileNameList.get(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                enabledProfileName = profileNameList.get(0);
            }
        });
        startTimePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new TimePickerDialog(FocusTimeActivity.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        String time = String.format("%d:%02d", hourOfDay, minute);
                        startTimeTextView.setText(time);
                    }
                }, 0, 0, true).show();
            }
        });
        endTimePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new TimePickerDialog(FocusTimeActivity.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        timeString = String.format("%d:%02d", hourOfDay, minute);
                        endTimeTextView.setText(timeString);
                        Calendar c = new GregorianCalendar(year, month, day, hourOfDay, minute, 0);
                        endTime = c.getTimeInMillis();
                    }
                }, 0, 0, true).show();
            }
        });
        profileSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(FocusTimeActivity.this, ProfileSettingActivity.class);
                intent.putExtra("profile_name", profileNameSet);
                startActivityForResult(intent, 0);
            }
        });
        startProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(FocusTimeActivity.this, ProfileEnableActivity.class);
                intent.putExtra("profile_name", enabledProfileName);
                intent.putExtra("profile_end_time", timeString);
                intent.putExtra("end_time", endTime);
                startActivity(intent);
                finish();
            }
        });
    }

    private void initDrawer() {
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerList = (ListView) findViewById(R.id.left_drawer);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mDrawerList.setAdapter(new MenuAdapter(FocusTimeActivity.this, R.layout.drawer_menu, mainMenuList));
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

    private List<PackageInfo> getAllPackageInfoFromThePhone()

    {

        List<PackageInfo> apkList = new ArrayList<PackageInfo>();

        PackageManager packageManager = this.getPackageManager();

        List<PackageInfo> list = packageManager.getInstalledPackages(0);

        for (int i = 0; i < list.size(); i++)

        {

            PackageInfo pi = list.get(i);

            if ((pi.applicationInfo.flags & pi.applicationInfo.FLAG_SYSTEM) == 0)

            {

                apkList.add(pi);

            }

        }

        return apkList;

    }

    private List<MyPackageInfo> getPhonePackageInfo()

    {

        List<MyPackageInfo> myList = new ArrayList<MyPackageInfo>();

        PackageManager packageManager = this.getPackageManager();

        List<PackageInfo> appList = getAllPackageInfoFromThePhone();

        for (int i = 0; i < appList.size(); i++)

        {

            PackageInfo pinfo = appList.get(i);

            MyPackageInfo myPackageInfo = new MyPackageInfo();

            myPackageInfo.setPackageIcon(packageManager

                    .getApplicationIcon(pinfo.applicationInfo));


            myPackageInfo.setPackageLabel(packageManager.getApplicationLabel(

                    pinfo.applicationInfo).toString());

            myPackageInfo.setPackageName(pinfo.applicationInfo.packageName);

            myList.add(myPackageInfo);

        }

        return myList;

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        profileNameSet = (TreeSet<String>) data.getSerializableExtra("profile_name");
        profileNameList = new ArrayList<>(profileNameSet);
        spinnerAdapter = new ArrayAdapter<String>(this, R.layout.spinner_item, profileNameList);
        spinner.setAdapter(spinnerAdapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the drawer_menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_focus_time, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        switch (id) {
            case android.R.id.home:
                finish();
                break;
        }
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
                mDrawerLayout.closeDrawer(Gravity.LEFT);
                break;
            case 1:
                startActivity(new Intent(FocusTimeActivity.this, MyDataActivity.class));
                mDrawerLayout.closeDrawer(Gravity.LEFT);
                break;
            case 2:
                startActivity(new Intent(FocusTimeActivity.this, SettingsActivity.class));
                mDrawerLayout.closeDrawer(Gravity.LEFT);
                break;
        }
    }

}

