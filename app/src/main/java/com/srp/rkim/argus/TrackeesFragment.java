package com.srp.rkim.argus;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Date;


public class TrackeesFragment extends Fragment {
    private static final String TAG = "TrackeesFragment";
    Context context;
    private FirebaseAuth masterAuth;
    private FirebaseAuth trackeeAuth;
    private FirebaseUser master;
    private ArrayList<TrackeeModel> trackees;
    private ArrayList<String> trackeeUIDs;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        View view = inflater.inflate(R.layout.fragment_trackees, container, false);

        RecyclerView rTrackees = view.findViewById(R.id.trackee_recycler_view);
        masterAuth = FirebaseAuth.getInstance();
        master = masterAuth.getCurrentUser();
        FirebaseDatabase mDatabase = FirebaseDatabase.getInstance();
        final DatabaseReference myRef = mDatabase.getReference();
        Log.d(TAG, "onCreateView: " + myRef.toString());

        trackees = new ArrayList<TrackeeModel>();
        trackeeUIDs = new ArrayList<String>();
        //  trackees.add(new TrackeeModel("Bob", 111, 112, Calendar.getInstance().getTime()));

        final TrackeeAdapter adapter = new TrackeeAdapter(context, trackees);
        rTrackees.setAdapter(adapter);
        rTrackees.setLayoutManager(new LinearLayoutManager(context));

//        myRef.child("users").child(master.getUid()).child("trackees").addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                trackeeUIDs = (ArrayList<String>) dataSnapshot.getValue();
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//                Log.w(TAG, "Failed to read value.", databaseError.toException());
//
//            }
//        });


        myRef.child("users").child(master.getUid()).child("trackees").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                trackeeUIDs = (ArrayList<String>) dataSnapshot.getValue();
                System.out.println("td" + trackeeUIDs);

                for (String uid : trackeeUIDs) {
                    myRef.child("users").child(uid).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot inDataSnapshot) {
                            //inDataSnapshot.child("location").getValue();
                            //inDataSnapshot.child("time").getValue();
                            for (TrackeeModel t : trackees) {
                                Log.d(TAG, trackees.size() + "before" + t.getUID());
                            }
                            //t.add(0, new Contact("Barney", true));
                            for (int x = 0; x < trackees.size(); x++) {
                                Log.d(TAG, "first:" + trackees.get(x).getUID());
                                Log.d(TAG, "second:" + inDataSnapshot.getRef().toString());

                                if (trackees.get(x).getUID().equals(inDataSnapshot.getRef().toString())) {
                                    trackees.remove(x);
                                    for (TrackeeModel t : trackees) {
                                        Log.d(TAG, "after" + t.getUID());
                                    }
                                    Log.d(TAG, "removed for uid: " + inDataSnapshot.getRef().toString());
                                }
                            }
                            trackees.add(0, new TrackeeModel(inDataSnapshot.getRef().toString(), (String) inDataSnapshot.child("name").getValue(), (Double) inDataSnapshot.child("location").child("latitude").getValue(), (Double) inDataSnapshot.child("location").child("latitude").getValue(), new Date((long) inDataSnapshot.child("time").child("time").getValue())));
                            Log.d(TAG, "added for uid: " + inDataSnapshot.getRef().toString());

                            adapter.notifyDataSetChanged();
//                            adapter.notifyItemInserted(0);

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


}
