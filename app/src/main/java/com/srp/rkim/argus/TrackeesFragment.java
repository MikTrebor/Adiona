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
    private FirebaseAuth mAuth;
    private FirebaseUser user;
    private ArrayList<TrackeeModel> trackees;
    private ArrayList<String> trackeeUIDs;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_trackees, container, false);
        context = getActivity();
        RecyclerView rTrackees = view.findViewById(R.id.trackee_recycler_view);
        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        FirebaseDatabase mDatabase = FirebaseDatabase.getInstance();
        final DatabaseReference myRef = mDatabase.getReference();
        Log.d(TAG, "onCreateView: " + myRef.toString());

        trackees = new ArrayList<TrackeeModel>();
        trackeeUIDs = new ArrayList<String>();

        final TrackeeAdapter adapter = new TrackeeAdapter(context, trackees);
        rTrackees.setAdapter(adapter);
        rTrackees.setLayoutManager(new LinearLayoutManager(context));

        myRef.child("users").child(user.getUid()).child("trackees").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                trackeeUIDs = (ArrayList<String>) dataSnapshot.getValue();
                System.out.println("td" + trackeeUIDs);
                for (String uid : trackeeUIDs) {
                    final String myUid = uid;
                    myRef.child("users").child(uid).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(final DataSnapshot inDataSnapshot) {
                            myRef.child("users").child(user.getUid()).child("trackeedata").child(myUid).addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot inInDataSnapshot) {
                                    //     Log.e(TAG, "value " + inInDataSnapshot.toString());

                                    for (TrackeeModel t : trackees) {
                                        Log.e(TAG, trackees.size() + "before" + t.getUID());
                                    }
                                    for (int x = 0; x < trackees.size(); x++) {
                                        Log.e(TAG, "first:" + trackees.get(x).getUID());
                                        Log.e(TAG, "second:" + myUid);
                                        if (trackees.get(x).getUID().equals(myUid)) {
                                            trackees.remove(x);
                                            for (TrackeeModel t : trackees) {
                                                Log.e(TAG, "after" + t.getUID());
                                            }
                                            Log.e(TAG, "removed for uid: " + inDataSnapshot.getRef().toString());
                                        }
                                    }

                                    Object dsName1 = inInDataSnapshot.child("name").getValue();
                                    String dsName = dsName1.toString();
                                    Long dsDate1 = (long) inDataSnapshot.child("time").child("time").getValue();

                                    Date dsDate = new Date(dsDate1);

                                    Double dsLat = (Double) inDataSnapshot.child("location").child("latitude").getValue();
                                    Double dsLong = (Double) inDataSnapshot.child("location").child("longitude").getValue();

                                    trackees.add(0, new TrackeeModel(myUid, dsName, dsLat, dsLong, dsDate));
                                    Log.d(TAG, "added for uid: " + inDataSnapshot.getRef().toString());

                                    adapter.notifyDataSetChanged();
                                }

                                @Override
                                public void onCancelled(DatabaseError error) {
                                    Log.w(TAG, "Failed to read value.", error.toException());
                                }
                            });


                        }

                        @Override
                        public void onCancelled(DatabaseError error) {
                            Log.w(TAG, "Failed to read value.", error.toException());
                        }
                    });
                }
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
