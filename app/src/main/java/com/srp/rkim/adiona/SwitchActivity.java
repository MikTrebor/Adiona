package com.srp.rkim.adiona;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class SwitchActivity extends AppCompatActivity {
    private Button mMasterButton;
    private Button mTrackeeButton;
    private FirebaseAuth mAuth;
    private FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_switch);
        final Context context = this;
        mMasterButton = (Button) findViewById(R.id.choose_master_button);
        mTrackeeButton = (Button) findViewById(R.id.choose_trackee_button);
        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        FirebaseDatabase mDatabase = FirebaseDatabase.getInstance();
        DatabaseReference myRef = mDatabase.getReference();
        //String type = myRef.child("users").child(user.getUid()).child("type").toString();

        myRef.child("users").child(user.getUid()).child("type").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                String value = dataSnapshot.getValue(String.class);
                Log.d("here", "Value is: " + value);


                if (value.equals("master")) {
                    Intent myIntent = new Intent(context, MainActivity.class);
                    startActivity(myIntent);
                } else if (value.equals("trackee")) {
                    Intent myIntent = new Intent(context, TrackeeMainActivity.class);
                    startActivity(myIntent);
                } else {
                    mMasterButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            //maybe add a warning prompt, e.g. "are you sure you want to run as a master account?"
                            FirebaseDatabase mDatabase = FirebaseDatabase.getInstance();
                            DatabaseReference myRef = mDatabase.getReference();
                            myRef.child("users").child(user.getUid()).child("type").setValue("master");
                            Intent myIntent = new Intent(context, MainActivity.class);
                            startActivity(myIntent);
                        }
                    });
                    mTrackeeButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            FirebaseDatabase mDatabase = FirebaseDatabase.getInstance();
                            DatabaseReference myRef = mDatabase.getReference();
                            myRef.child("users").child(user.getUid()).child("type").setValue("trackee");
                            Intent myIntent = new Intent(context, TrackeeMainActivity.class);
                            startActivity(myIntent);
                        }
                    });
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w("here", "Failed to read value.", error.toException());
            }
        });

        //   Log.d("type", type);

    }
}
