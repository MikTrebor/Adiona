package com.srp.rkim.adiona;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.TextView;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends AppCompatActivity implements OnMapReadyCallback {

    Fragment trackeesFragment = new TrackeesFragment();
    Fragment customMapFragment = new CustomMapFragment();
    Fragment mMapFragment = MapFragment.newInstance();
    Fragment smartHomeFragment = new SmartHomeFragment();
    Fragment settingsFragment = new SettingsFragment();
    FragmentManager fragmentManager = getFragmentManager();
    private TextView mTextMessage;
    private TextView mDebugMessage;
    private FirebaseAuth mAuth;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_trackees:
                    fragmentManager.beginTransaction().replace(R.id.content, trackeesFragment).commit();
                    //mTextMessage.setText(R.string.title_trackees);
                    return true;
                case R.id.navigation_map:
                    fragmentManager.beginTransaction().replace(R.id.content, customMapFragment).commit();
                    // fragmentManager.beginTransaction().replace(R.id.content, mMapFragment).commit();
                    //mTextMessage.setText(R.string.title_map);
                    return true;
                case R.id.navigation_smarthome:
                    fragmentManager.beginTransaction().replace(R.id.content, smartHomeFragment).commit();
                    //mTextMessage.setText(R.string.title_smarthome);
                    return true;
                case R.id.navigation_settings:
                    fragmentManager.beginTransaction().replace(R.id.content, settingsFragment).commit();
                    //mTextMessage.setText(R.string.title_settings);
                    return true;
            }
            return false;
        }

    };

    //MapFragment mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.map);
    // mapFragment.getMapAsync(this);
    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        updateUI(currentUser);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mAuth = FirebaseAuth.getInstance();

        Intent intent = new Intent(getApplicationContext(), MyService.class);
        // stopService(intent);


        FirebaseDatabase database = FirebaseDatabase.getInstance();
        // DatabaseReference myRef = database.getReference("users");


//        mTextMessage = (TextView) findViewById(R.id.message);
//        mDebugMessage = (TextView) findViewById(R.id.debug);
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

    }

    @Override
    public void onMapReady(GoogleMap map) {
        map.addMarker(new MarkerOptions()
                .position(new LatLng(0, 0))
                .title("Marker"));
    }
    private void updateUI(FirebaseUser user) {
//        hideProgressDialog();
        if (user != null) {
//            mDebugMessage.setText("User: " + user.getEmail());
//            intent.putExtra("UID", user.getUid());

//            mStatusTextView.setText(getString(R.string.emailpassword_status_fmt,
//                    user.getEmail(), user.isEmailVerified()));
//            mDetailTextView.setText(getString(R.string.firebase_status_fmt, user.getUid()));
//
//            findViewById(R.id.email_password_buttons).setVisibility(View.GONE);
//            findViewById(R.id.email_password_fields).setVisibility(View.GONE);
//            findViewById(R.id.signed_in_buttons).setVisibility(View.VISIBLE);
//
//            findViewById(R.id.verify_email_button).setEnabled(!user.isEmailVerified());
        } else {
            mDebugMessage.setText("Not signed in!");

//            mStatusTextView.setText(R.string.signed_out);
//            mDetailTextView.setText(null);

//            findViewById(R.id.email_password_buttons).setVisibility(View.VISIBLE);
//            findViewById(R.id.email_password_fields).setVisibility(View.VISIBLE);
//            findViewById(R.id.signed_in_buttons).setVisibility(View.GONE);
        }
    }

}
