package com.kevin.focustime;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;


public class ProfileSettingActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private ViewFlipper viewFlipper;
    private LayoutInflater layoutInflater;
    private Button next;
    private Button back;
    private Button chooseApps;
    private View view3;
    private View view4;
    private View view6;
    private RadioButton yes;
    private RadioButton no;
    private EditText profileName;
    private boolean isFromView4 = false;
    private boolean isFromView6 = false;
    private Set<String> profileNameSet;
    private Profile profile;
    private List<MyPackageInfo> piList;
    private TextView chooseAppTextView;
    private Set<String> packageNameSet = new HashSet<>();
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private SharedPreferences.Editor profileListEditor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        profileNameSet = (Set<String>) getIntent().getSerializableExtra("profile_name");
        piList = ((ApplicationData) getApplicationContext()).getPiList();
        sharedPreferences = getSharedPreferences("profile", MODE_PRIVATE);
        SharedPreferences profileList = getSharedPreferences("profile_name", MODE_PRIVATE);
        profileListEditor = profileList.edit();
        editor = sharedPreferences.edit();
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Profile Settings");
        back = (Button) findViewById(R.id.back);
        next = (Button) findViewById(R.id.next);
        layoutInflater = getLayoutInflater();
        View view1 = layoutInflater.inflate(R.layout.fragment_profile1, null);
        View view2 = layoutInflater.inflate(R.layout.fragment_profile2, null);

        view3 = layoutInflater.inflate(R.layout.fragment_profile3, null);
        view4 = layoutInflater.inflate(R.layout.fragment_profile4, null);
        View view5 = layoutInflater.inflate(R.layout.fragment_profile5, null);
        view6 = layoutInflater.inflate(R.layout.fragment_profile6, null);
        View view7 = layoutInflater.inflate(R.layout.fragment_profile7, null);
        View view8 = layoutInflater.inflate(R.layout.fragment_profile8, null);
        chooseApps = (Button) view7.findViewById(R.id.choose_apps);
        viewFlipper = (ViewFlipper) findViewById(R.id.view_flipper);
        viewFlipper.addView(view1);
        viewFlipper.addView(view2);
        viewFlipper.addView(view3);
        viewFlipper.addView(view4);
        viewFlipper.addView(view5);
        viewFlipper.addView(view6);
        viewFlipper.addView(view7);
        viewFlipper.addView(view8);
        profileName = (EditText) view2.findViewById(R.id.profile_name);
        chooseAppTextView = (TextView) view7.findViewById(R.id.choose_apps_textView);
        profile = new Profile();
        chooseApps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent chooseAppsIntent = new Intent(ProfileSettingActivity.this, ChooseAppsActivity.class);
                startActivityForResult(chooseAppsIntent, 0);
            }
        });
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewFlipper.setInAnimation(ProfileSettingActivity.this, R.anim.slide_left_in);
                viewFlipper.setOutAnimation(ProfileSettingActivity.this, R.anim.slide_left_out);
                switch (viewFlipper.getDisplayedChild()) {
                    case 1:
                        if (profileNameSet.contains(profileName.getText().toString()))
                            Toast.makeText(ProfileSettingActivity.this, "This name is taken.Please choose a different one.", Toast.LENGTH_SHORT).show();
                        else {
                            profile.setName(profileName.getText().toString());
                            viewFlipper.showNext();
                        }
                        break;
                    case 2:
                        yes = (RadioButton) view3.findViewById(R.id.yes);
                        no = (RadioButton) view3.findViewById(R.id.no);
                        if (yes.isChecked()) {
                            profile.setNotifications(true);
                        } else {
                            profile.setNotifications(false);
                        }
                        viewFlipper.showNext();
                        break;
                    case 3:
                        yes = (RadioButton) view4.findViewById(R.id.yes);
                        no = (RadioButton) view4.findViewById(R.id.no);
                        if (yes.isChecked()) {
                            profile.setCallsAndSMS(true);
                            isFromView4 = false;
                            viewFlipper.setDisplayedChild(4);
                        } else {
                            profile.setCallsAndSMS(false);
                            isFromView4 = true;
                            viewFlipper.setDisplayedChild(5);
                        }
                        break;
                    case 5:
                        yes = (RadioButton) view6.findViewById(R.id.yes);
                        no = (RadioButton) view6.findViewById(R.id.no);
                        if (yes.isChecked()) {
                            profile.setApps(true);
                            isFromView6 = false;
                            viewFlipper.setDisplayedChild(6);
                        } else {
                            profile.setApps(false);
                            isFromView6 = true;
                            viewFlipper.setDisplayedChild(7);
                            next.setText("Finish");
                        }
                        break;
                    case 6:
                        next.setText("Finish");
                        viewFlipper.showNext();
                        break;
                    case 7:
                        if (!isFromView6 && packageNameSet.size() == 0) {
                            for (MyPackageInfo packageInfo : piList) {
                                packageNameSet.add(packageInfo.getPackageName());
                            }
                        } else {
                            for (MyPackageInfo packageInfo : profile.getPiList()) {
                                packageNameSet.add(packageInfo.getPackageName());
                            }
                        }
                        profileNameSet.add(profile.getName());
                        Intent intent = new Intent();
                        intent.putExtra("profile_name", (Serializable) profileNameSet);
                        setResult(0, intent);
                        profileListEditor.putStringSet("profile_name", profileNameSet);
                        profileListEditor.commit();
                        editor.putBoolean(profile.getName() + "isApps", profile.isApps());
                        editor.putStringSet(profile.getName() + "app_list", packageNameSet);
                        editor.commit();
                        finish();
                        break;
                    default:
                        viewFlipper.showNext();
                        break;
                }
            }
        });
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewFlipper.setInAnimation(ProfileSettingActivity.this, R.anim.slide_right_in);
                viewFlipper.setOutAnimation(ProfileSettingActivity.this, R.anim.slide_right_out);
                switch (viewFlipper.getDisplayedChild()) {
                    case 0:
                        AlertDialog.Builder cancelDialog = new AlertDialog.Builder(ProfileSettingActivity.this);
                        cancelDialog.setTitle("Are you sure?");
                        cancelDialog.setMessage("If you leave now,you will lose your changes.");
                        cancelDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                FocusTimeActivity.instance.finish();
                                startActivity(new Intent(ProfileSettingActivity.this, FocusTimeActivity.class));
                                finish();
                            }
                        });
                        cancelDialog.setNegativeButton("No", null);
                        cancelDialog.show();
                        break;
                    case 5:
                        if (isFromView4)
                            viewFlipper.setDisplayedChild(3);
                        else
                            viewFlipper.showPrevious();
                        break;
                    case 7:
                        if (isFromView6)
                            viewFlipper.setDisplayedChild(5);
                        else
                            viewFlipper.showPrevious();
                        next.setText("Next");
                        break;
                    default:
                        viewFlipper.showPrevious();
                        break;
                }
            }

        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        List<MyPackageInfo> removeList = new ArrayList<>();
        List<MyPackageInfo> removedPiList = new ArrayList<>();
        removedPiList.addAll(piList);
        super.onActivityResult(requestCode, resultCode, data);
        HashMap<Integer, Boolean> isSelected = (HashMap<Integer, Boolean>) data.getSerializableExtra("Profile");
        for (Map.Entry<Integer, Boolean> entry : isSelected.entrySet()) {
            if (entry.getValue())
                removeList.add(removedPiList.get(entry.getKey()));
        }
        removedPiList.removeAll(removeList);
        profile.setPiList(removedPiList);
        int i = piList.size() - removedPiList.size();
        if (i > 0) {
            chooseApps.setText("Reselect Apps");
            chooseAppTextView.setText("You have chosen " + i + " app(s)");
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            AlertDialog.Builder cancelDialog = new AlertDialog.Builder(ProfileSettingActivity.this);
            cancelDialog.setTitle("Are you sure?");
            cancelDialog.setMessage("If you leave now,you will lose your changes.");
            cancelDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    FocusTimeActivity.instance.finish();
                    startActivity(new Intent(ProfileSettingActivity.this, FocusTimeActivity.class));
                    finish();
                }
            });
            cancelDialog.setNegativeButton("No", null);
            cancelDialog.show();
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_profile, menu);
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
