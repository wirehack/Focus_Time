package com.kevin.focustime;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;


public class ProfileEnableActivity extends AppCompatActivity implements ServiceConnection {
    private AlertDialog.Builder appListDialog;
    private AlertDialog.Builder contactListDialog;
    private ListView appListView;
    private List<MyPackageInfo> profileAppList;
    private Intent profileServiceIntent;
    private long endTime;
    private Intent stopServiceIntent;
    private PendingIntent stopServicePendingIntent;
    private AlarmManager duration;
    private AlertDialog.Builder cancelDialog;
    public static Activity instance;
    private SharedPreferences sharedPreferences;
    private Profile profile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_enable);
        profile = new Profile();
        profile.setName(getIntent().getStringExtra("profile_name"));
        sharedPreferences = getSharedPreferences("profile", MODE_PRIVATE);
        instance = this;
        stopServiceIntent = new Intent(ProfileServiceStopReceiver.ACTION);
        stopServiceIntent.putExtra("start_time", System.currentTimeMillis());
        endTime = getIntent().getLongExtra("end_time", 0);
        profileServiceIntent = new Intent(ProfileEnableActivity.this, ProfileEnableService.class);
        bindService(profileServiceIntent, ProfileEnableActivity.this, BIND_AUTO_CREATE);
        initView();

    }

    public void initView() {
        contactListDialog = new AlertDialog.Builder(ProfileEnableActivity.this);
        TextView profileNameTextView = (TextView) findViewById(R.id.profile_name);
        TextView endTime = (TextView) findViewById(R.id.end_time);
        Button stop = (Button) findViewById(R.id.stop);
        profileNameTextView.setText(profile.getName());
        endTime.setText("until " + getIntent().getStringExtra("profile_end_time"));
        Set<String> packageNameSet = sharedPreferences.getStringSet(profile.getName() + "app_list", null);
        profileAppList = new ArrayList<>();
        List<MyPackageInfo> piList = ((ApplicationData) getApplicationContext()).getPiList();
        for (MyPackageInfo packageInfo : piList) {
            if (!packageNameSet.contains(packageInfo.getPackageName()))
                profileAppList.add(packageInfo);
        }
        contactListDialog.setTitle("Contact List");
        contactListDialog.setMessage("contactlist");
        contactListDialog.setPositiveButton("OK", null);
        cancelDialog = new AlertDialog.Builder(this);
        cancelDialog.setMessage("Are you sure you want to exit the profile?");
        cancelDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                stopServiceIntent = new Intent(ProfileServiceStopReceiver.ACTION);
                stopServiceIntent.putExtra("start_time", System.currentTimeMillis());
                duration.cancel(stopServicePendingIntent);
                sendBroadcast(stopServiceIntent);
            }
        });
        cancelDialog.setNegativeButton("No", null);
        Button appList = (Button) findViewById(R.id.app_list);
        Button contactList = (Button) findViewById(R.id.contact_list);
        appList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                appListDialog = new AlertDialog.Builder(ProfileEnableActivity.this);
                appListView = new ListView(ProfileEnableActivity.this);
                appListView.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
                appListView.setAdapter(new AppListInAlertDialogAdapter(ProfileEnableActivity.this, profileAppList));
                appListDialog.setTitle("App List");
                appListDialog.setView(appListView);
                appListDialog.setPositiveButton("OK", null);
                appListDialog.show();
            }
        });
        contactList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                contactListDialog.show();
            }
        });
        stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cancelDialog.show();
            }
        });
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK)
            return true;
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_profile_enable, menu);
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
    public void onServiceConnected(ComponentName name, IBinder service) {
        ProfileEnableService.EnableBinder binder = (ProfileEnableService.EnableBinder) service;
        List<String> packageNameList = new ArrayList<>();
        packageNameList.addAll(sharedPreferences.getStringSet(profile.getName() + "app_list", null));
        binder.setPackageNameList(packageNameList);
        binder.setApps(sharedPreferences.getBoolean(profile.getName() + "isApps", false));
        binder.setDuration(endTime);
        stopServicePendingIntent = binder.getPendingIntent();
        duration = binder.getAlarmManager();
        startService(profileServiceIntent);
        unbindService(this);
    }

    @Override
    public void onServiceDisconnected(ComponentName name) {

    }
}
