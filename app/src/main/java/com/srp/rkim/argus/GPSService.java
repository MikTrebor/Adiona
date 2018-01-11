package com.srp.rkim.argus;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import com.google.android.gms.maps.GoogleMap;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;

public class GPSService extends Service {
    private static final String TAG = "GPSService";
    private static final int MAP_PERMISSIONS = 1;
    Context context;
    GoogleMap map;
    GPSTracker gps;
    private FirebaseAuth mAuth;
    private FirebaseUser user;

    @Override
    public IBinder onBind(Intent arg0) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.e(TAG, "onStartCommand");
        super.onStartCommand(intent, flags, startId);
        return START_STICKY;
    }

    @Override
    public void onCreate() {
        Log.e(TAG, "onCreate");
        context = getApplicationContext();
        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        FirebaseDatabase mDatabase = FirebaseDatabase.getInstance();
        final DatabaseReference myRef = mDatabase.getReference();
        //  String[] perms = {"android.permission.ACCESS_COARSE_LOCATION", "android.permission.ACCESS_FINE_LOCATION"};
        //   requestPermissions(context, perms, MAP_PERMISSIONS);

        gps = new GPSTracker(context);

        // Check if GPS enabled
        if (gps.canGetLocation()) {
            Log.d(TAG, "gps enabled");
            double latitude = gps.getLatitude();
            double longitude = gps.getLongitude();
            myRef.child("users").child(user.getUid()).child("location").child("latitude").setValue(latitude);
            myRef.child("users").child(user.getUid()).child("location").child("longitude").setValue(longitude);
            myRef.child("users").child(user.getUid()).child("time").setValue(Calendar.getInstance().getTime());
            // \n is for new line
        } else {
            Log.d(TAG, "gps disabled");

            // Can't get location.
            // GPS or network is not enabled.
            // Ask user to enable GPS/network in settings.
            gps.showSettingsAlert();
        }

    }

    //    @Override
//    public void onRequestPermissionsResult(int permsRequestCode, String[] permissions, int[] grantResults) {
//        switch (permsRequestCode) {
//            case 1:
//                boolean coarseLocationAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
//                boolean fineLocationAccepted = grantResults[1] == PackageManager.PERMISSION_GRANTED;
//                break;
//        }
//    }
    @Override
    public void onDestroy() {
        Log.e(TAG, "onDestroy");

    }
}