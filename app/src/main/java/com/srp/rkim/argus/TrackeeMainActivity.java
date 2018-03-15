package com.srp.rkim.argus;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.widget.TextView;

public class TrackeeMainActivity extends AppCompatActivity {
    private static final String TAG = "TrackeeMainActivity";

    private static final int MAP_PERMISSIONS = 1;
    Fragment smartHomeFragment = new SmartHomeFragment();
    Fragment settingsFragment = new SettingsFragment();
    FragmentManager fragmentManager = getFragmentManager();
    Context context;

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
        Log.e(TAG, "onCreate");


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trackee_main);

        String[] perms = {"android.permission.ACCESS_COARSE_LOCATION", "android.permission.ACCESS_FINE_LOCATION"};
        requestPermissions(perms, MAP_PERMISSIONS);

        Intent intent = new Intent(getApplicationContext(), LocationService.class);
        startService(intent);

        BottomNavigationView navigation = findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        fragmentManager.beginTransaction().replace(R.id.content, smartHomeFragment).commit();

    }

    @Override
    public void onRequestPermissionsResult(int permsRequestCode, String[] permissions, int[] grantResults) {

        switch (permsRequestCode) {
            case MAP_PERMISSIONS:
                boolean coarseLocationAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                boolean fineLocationAccepted = grantResults[1] == PackageManager.PERMISSION_GRANTED;
                break;
        }
    }
}
