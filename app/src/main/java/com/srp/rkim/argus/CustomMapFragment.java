package com.srp.rkim.argus;

import android.app.Fragment;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;


public class CustomMapFragment extends Fragment implements OnMapReadyCallback {
    private static final String TAG = "CustomMapFragment";

    private static final int MAP_PERMISSIONS = 1;
    Context context;
    MapView mapView;
    GoogleMap map;
    GPSTracker gps;
    Button refreshButton;
    boolean networkPerm, gpsPerm;
    private FirebaseAuth mAuth;
    private FirebaseUser master;
    private ArrayList<TrackeeModel> trackees;
    private ArrayList<String> trackeeUIDs;
    private HashMap<String, Marker> mapMarkers;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        context = getActivity();
        View view = inflater.inflate(R.layout.fragment_map, container, false);
        mAuth = FirebaseAuth.getInstance();
        master = mAuth.getCurrentUser();
        FirebaseDatabase mDatabase = FirebaseDatabase.getInstance();
        final DatabaseReference myRef = mDatabase.getReference();
        mapView = view.findViewById(R.id.mapView);
        mapView.onCreate(savedInstanceState);
        mapView.onResume();
        mapView.getMapAsync(this);
        trackees = new ArrayList<TrackeeModel>();
        trackeeUIDs = new ArrayList<String>();
        String[] perms = {"android.permission.ACCESS_COARSE_LOCATION", "android.permission.ACCESS_FINE_LOCATION"};
        requestPermissions(perms, MAP_PERMISSIONS);


        refreshButton = view.findViewById(R.id.refreshLocationButton);
        refreshButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                gps = new GPSTracker(context);

                // Check if GPS enabled
                if (gps.canGetLocation()) {

                    double latitude = gps.getLatitude();
                    double longitude = gps.getLongitude();
                    CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(new LatLng(latitude, longitude), 17);
                    map.animateCamera(cameraUpdate);
                    // \n is for new line
                    Toast.makeText(context, "Your Location is - \nLat: " + latitude + "\nLong: " + longitude, Toast.LENGTH_LONG).show();
                } else {
                    // Can't get location.
                    // GPS or network is not enabled.
                    // Ask user to enable GPS/network in settings.
                    gps.showSettingsAlert();
                }
            }
        });

        myRef.child("users").child(master.getUid()).child("trackees").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                trackeeUIDs = (ArrayList<String>) dataSnapshot.getValue();
                // System.out.println("td" + trackeeUIDs);
                for (String uid : trackeeUIDs) {
                    map.clear();
                    mapMarkers = new HashMap<>();
                    myRef.child("users").child(uid).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot inDataSnapshot) {//listener for each trackee (full acc)
                            for (int x = 0; x < trackees.size(); x++) {
                                if (trackees.get(x).getUID().equals(inDataSnapshot.getRef().toString())) {
                                    Marker markerRemove = mapMarkers.get(trackees.get(x).getUID());
                                    markerRemove.remove();
                                    mapMarkers.remove(trackees.get(x).getUID());
                                    trackees.remove(x);
                                    for (TrackeeModel t : trackees) {
                                        Log.d(TAG, "after" + t.getUID());
                                    }
                                    Log.d(TAG, "trackee length" + trackees.size() + " " + trackeeUIDs.size());
                                    Log.d(TAG, "removed for uid: " + inDataSnapshot.getRef().toString());
                                }
                            }
                            String dsUid = inDataSnapshot.getRef().toString();
                            Object dsName1 = inDataSnapshot.child("name").getValue();
                            Long dsDate1 = (long) inDataSnapshot.child("time").child("time").getValue();
                            String dsName = dsName1.toString();
                            Date dsDate = new Date(dsDate1);
                            Double dsLat = (Double) inDataSnapshot.child("location").child("latitude").getValue();
                            Double dsLong = (Double) inDataSnapshot.child("location").child("longitude").getValue();
                            trackees.add(0, new TrackeeModel(dsUid, dsName, dsLat, dsLong, dsDate));
                            Log.d(TAG, "added for uid: " + inDataSnapshot.getRef().toString());
                            Marker marker = map.addMarker(new MarkerOptions().position(new LatLng(trackees.get(0).getLatitude(), trackees.get(0).getLongitude())).title(trackees.get(0).getTrackeeName()));
                            mapMarkers.put(inDataSnapshot.getRef().toString(), marker);
                        }

                        @Override
                        public void onCancelled(DatabaseError error) {
                            Log.w(TAG, "Failed to read value.", error.toException());
                        }
                    });
                }
                //  Log.d(TAG, "Value is: " + value);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });
        return view;
    }

    @Override
    public void onRequestPermissionsResult(int permsRequestCode, String[] permissions, int[] grantResults) {
        switch (permsRequestCode) {
            case 1:
                boolean coarseLocationAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                boolean fineLocationAccepted = grantResults[1] == PackageManager.PERMISSION_GRANTED;
                break;
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;
        if (map != null) {
            map.getUiSettings().setMyLocationButtonEnabled(true);
            //   map.addMarker(new MarkerOptions().position(new LatLng(43.1, -87.9)).title("Hello world"));
            //map.setMyLocationEnabled(true);

//        try {
//            MapsInitializer.initialize(this.getActivity());
//        } catch (GooglePlayServicesNotAvailableException e) {
//            e.printStackTrace();
//        }
            CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(new LatLng(43.1, -87.9), 10);
            map.animateCamera(cameraUpdate);
            // Add your functions to GoogleMap
        }
    }

    @Override
    public void onResume() {
        mapView.onResume();
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }
}
