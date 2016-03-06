package com.kevin.focustime;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;


public class ProfileStopActivity extends AppCompatActivity {
    private List<MyPackageInfo> piList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_stop);
        stopService(new Intent(this, ProfileEnableService.class));
        piList.addAll(((ApplicationData) getApplicationContext()).getPiList());
        Iterator<MyPackageInfo> iterator = piList.iterator();
        MyPackageInfo i = iterator.next();
        while (iterator.hasNext()) {
            if (i.getUsedTimes() == 0)
                iterator.remove();
            i = iterator.next();
        }
        Collections.sort(piList, new UsedTimesComparator());
        ListView listView = (ListView) findViewById(R.id.listview);
        TextView unlockTimes = (TextView) findViewById(R.id.unlock_times);
        TextView totalTime = (TextView) findViewById(R.id.total_time);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Profile Log");
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ProfileStopActivity.this, FocusTimeActivity.class));
                finish();
            }
        });
        int times = getIntent().getIntExtra("unlock_times", 0);
        long totalProfileTime = Math.round(((System.currentTimeMillis() - getIntent().getLongExtra("start_time", System.currentTimeMillis())) / 60000.0));
        unlockTimes.setText("" + times);
        totalTime.setText("" + totalProfileTime);
        AppOpenTimesAdapter appOpenTimesAdapter = new AppOpenTimesAdapter(this, piList);
        listView.setAdapter(appOpenTimesAdapter);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            startActivity(new Intent(ProfileStopActivity.this, FocusTimeActivity.class));
            finish();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_profile_success, menu);
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
}
