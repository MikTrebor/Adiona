package com.srp.rkim.argus;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.TextView;

public class TrackeeMainActivity extends AppCompatActivity {

    Fragment smartHomeFragment = new SmartHomeFragment();
    Fragment settingsFragment = new SettingsFragment();
    FragmentManager fragmentManager = getFragmentManager();
    //    GPSTracker gps;
    private TextView mTextMessage;
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_smarthome:
                    fragmentManager.beginTransaction().replace(R.id.content, smartHomeFragment).commit();
                    return true;
                case R.id.navigation_settings:
                    fragmentManager.beginTransaction().replace(R.id.content, settingsFragment).commit();
                    return true;
            }
            return false;
        }

    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trackee_main);

        Intent intent = new Intent(getApplicationContext(), MyService.class);
        startService(intent);

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
    }

}
