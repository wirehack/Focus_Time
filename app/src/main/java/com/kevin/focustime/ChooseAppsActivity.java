package com.kevin.focustime;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import java.util.HashMap;
import java.util.List;


public class ChooseAppsActivity extends AppCompatActivity {
    private List<MyPackageInfo> piList;
    private Button selectAll;
    private Button deselectAll;
    private Button inverse;
    private ChooseAppListAdapter chooseAppListAdapter;
    private HashMap<Integer, Boolean> isSelected;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_apps);
        isSelected = new HashMap<>();
        piList = ((ApplicationData)getApplicationContext()).getPiList();
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        selectAll = (Button) findViewById(R.id.select_all);
        deselectAll = (Button) findViewById(R.id.deselect_all);
        inverse = (Button) findViewById(R.id.inverse);
        toolbar.setTitle("Choose your Apps");
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if(item.getItemId()==R.id.done)
                {
                    isSelected=ChooseAppListAdapter.getIsSelected();
                    Intent intent=new Intent();
                    intent.putExtra("Profile",isSelected);
                    setResult(0,intent);
                    finish();
                }
                return true;
            }
        });
        ListView listView = (ListView) findViewById(R.id.app_list);
        chooseAppListAdapter = new ChooseAppListAdapter(this, piList);
        listView.setAdapter(chooseAppListAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ChooseAppListAdapter.ViewHolder viewHolder = (ChooseAppListAdapter.ViewHolder) view.getTag();
                viewHolder.checkBox.toggle();
                ChooseAppListAdapter.getIsSelected().put(position, viewHolder.checkBox.isChecked());
            }
        });
        selectAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (int i = 0; i < piList.size(); i++) {
                    ChooseAppListAdapter.getIsSelected().put(i, true);
                }
                chooseAppListAdapter.notifyDataSetChanged();
            }
        });
        deselectAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (int i = 0; i < piList.size(); i++) {
                    if (ChooseAppListAdapter.getIsSelected().get(i)) {
                        ChooseAppListAdapter.getIsSelected().put(i, false);
                    }
                }
                chooseAppListAdapter.notifyDataSetChanged();
            }
        });
        inverse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (int i = 0; i < piList.size(); i++) {
                    if (ChooseAppListAdapter.getIsSelected().get(i)) {
                        ChooseAppListAdapter.getIsSelected().put(i, false);
                    } else {
                        ChooseAppListAdapter.getIsSelected().put(i, true);
                    }
                }
                chooseAppListAdapter.notifyDataSetChanged();
            }
        });
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_choose_apps, menu);
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
