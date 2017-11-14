package com.srp.rkim.adiona;

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

        trackees = new ArrayList<TrackeeModel>();
        //  trackees.add(new TrackeeModel("Bob", 111, 112, Calendar.getInstance().getTime()));

        final TrackeeAdapter adapter = new TrackeeAdapter(context, trackees);
        rTrackees.setAdapter(adapter);
        rTrackees.setLayoutManager(new LinearLayoutManager(context));


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

                            //t.add(0, new Contact("Barney", true));

                            trackees.add(0, new TrackeeModel((String) inDataSnapshot.child("name").getValue(), (Double) inDataSnapshot.child("location").child("latitude").getValue(), (Double) inDataSnapshot.child("location").child("latitude").getValue(), new Date((long) inDataSnapshot.child("time").child("time").getValue())));
                            adapter.notifyItemInserted(0);

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
