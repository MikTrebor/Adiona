package com.srp.rkim.argus;
//https://stackoverflow.com/questions/29712244/using-googleapiclient-in-a-service

//https://github.com/sakurabird/Android-Fused-location-provider-example/blob/master/app/src/main/java/com/sakurafish/exam/location/api/MainActivity.java

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;

/**
 * Created by 2018rkim on 2/13/2018.
 */
public class LocationService extends Service implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener {
    private static final String TAG = "LocationService";
    private static final long INTERVAL = 1000 * 1 * 1;

    Context context;
    FirebaseDatabase mDatabase;
    DatabaseReference myRef;
    LocationRequest mLocationRequest;
    private FirebaseAuth mAuth;
    private FirebaseUser user;
    private GoogleApiClient mLocationClient;
    private Location mCurrentLocation;
    private FusedLocationProviderClient mFusedLocationClient;


    @Override
    public IBinder onBind(Intent arg0) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.e(TAG, "onStartCommand");

        return START_STICKY;
    }

    @Override
    public void onCreate() {
        Log.e(TAG, "onCreate");
        context = getApplicationContext();

        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        mDatabase = FirebaseDatabase.getInstance();
        myRef = mDatabase.getReference();

        mLocationClient = new GoogleApiClient.Builder(LocationService.this)
                .addApi(LocationServices.API).addConnectionCallbacks(LocationService.this)
                .addOnConnectionFailedListener(LocationService.this).build();
        mLocationClient.connect();

        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(INTERVAL);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setFastestInterval(INTERVAL);

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);


        try {
            mFusedLocationClient.getLastLocation()
                    .addOnSuccessListener(new OnSuccessListener<Location>() {
                        @Override
                        public void onSuccess(Location location) {
                            // Got last known location. In some rare situations this can be null.
                            if (location != null) {
                                Log.e(TAG, "getLastLocation");
                                mCurrentLocation = location;
                                double latitude = mCurrentLocation.getLatitude();
                                double longitude = mCurrentLocation.getLongitude();

                                Log.e(TAG, "location: " + latitude + ", " + longitude);
                                myRef.child("users").child(user.getUid()).child("location").child("latitude").setValue(latitude);
                                myRef.child("users").child(user.getUid()).child("location").child("longitude").setValue(longitude);
                                myRef.child("users").child(user.getUid()).child("time").setValue(Calendar.getInstance().getTime());
                            }
                        }
                    });
        } catch (java.lang.SecurityException ex) {
            Log.i(TAG, "fail to request location update, ignore", ex);
        }
    }

    @Override
    public void onLocationChanged(Location location) {
        Log.e(TAG, "onLocationChanged");
        mCurrentLocation = location;
        double latitude = mCurrentLocation.getLatitude();
        double longitude = mCurrentLocation.getLongitude();

        Log.e(TAG, "location: " + latitude + ", " + longitude);
        myRef.child("users").child(user.getUid()).child("location").child("latitude").setValue(latitude);
        myRef.child("users").child(user.getUid()).child("location").child("longitude").setValue(longitude);
        myRef.child("users").child(user.getUid()).child("time").setValue(Calendar.getInstance().getTime());
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        Log.e(TAG, "Connected");
        try {
            LocationServices.FusedLocationApi.requestLocationUpdates(mLocationClient, mLocationRequest, this);
        } catch (java.lang.SecurityException ex) {
            Log.i(TAG, "fail to request location update, ignore", ex);
        }
    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.e(TAG, "Connection suspended.");
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.e(TAG, "Connection Failed");
    }
}
