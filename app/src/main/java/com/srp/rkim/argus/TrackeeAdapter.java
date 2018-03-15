package com.srp.rkim.argus;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

/**
 * Created by 2018rkim on 11/9/2017.
 */
public class TrackeeAdapter extends
        RecyclerView.Adapter<TrackeeAdapter.ViewHolder> {
    private static final String TAG = "TrackeeAdapter";

    private List<TrackeeModel> mTrackees;
    // Store the context for easy access
    private Context mContext;

    // Pass in the contact array into the constructor
    public TrackeeAdapter(Context context, List<TrackeeModel> trackees) {
        mTrackees = trackees;
        mContext = context;
        //  Log.e("trackeeadapter", context.toString());
    }

    // Easy access to the context object in the recyclerview
    private Context getContext() {
        return mContext;
    }

    @Override
    public TrackeeAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        // Inflate the custom layout
        View trackeeView = inflater.inflate(R.layout.item_trackee, parent, false);

        // Return a new holder instance
        ViewHolder viewHolder = new ViewHolder(trackeeView);
        return viewHolder;
    }

    // Involves populating data into the item through holder
    @Override
    public void onBindViewHolder(final TrackeeAdapter.ViewHolder viewHolder, int position) {
        // Get the data model based on position
        final TrackeeModel trackee = mTrackees.get(position);

        FirebaseDatabase mDatabase = FirebaseDatabase.getInstance();
        final DatabaseReference myRef = mDatabase.getReference();
        final FirebaseUser user;
        FirebaseAuth mAuth;
        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        // Set item views based on your views and data model
        TextView nameView = viewHolder.nameTextView;
        nameView.setText(trackee.getTrackeeName());

        TextView syncView = viewHolder.syncTextView;
        syncView.setText(trackee.getTime().toString());

        Button eButton = viewHolder.editButton;
        eButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LayoutInflater li = LayoutInflater.from(mContext);
                View boundariesView = li.inflate(R.layout.dialog_boundaries, null);
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(mContext);
                alertDialogBuilder.setView(boundariesView);

                final LinearLayout mCircleData = boundariesView.findViewById(R.id.circle_data);
                final LinearLayout mQuadData = boundariesView.findViewById(R.id.quad_data);
                mCircleData.setVisibility(View.GONE);
                mQuadData.setVisibility(View.GONE);

                final EditText mTrackeeName = boundariesView.findViewById(R.id.trackee_name);

                final EditText mCircleLatitude = boundariesView.findViewById(R.id.circle_center_latitude);
                final EditText mCircleLongitude = boundariesView.findViewById(R.id.circle_center_longitude);
                final EditText mCircleRadius = boundariesView.findViewById(R.id.circle_radius);

                final EditText mQuadOneLatitude = boundariesView.findViewById(R.id.quad_1_latitude);
                final EditText mQuadOneLongitude = boundariesView.findViewById(R.id.quad_1_longitude);

                final EditText mQuadTwoLatitude = boundariesView.findViewById(R.id.quad_2_latitude);
                final EditText mQuadTwoLongitude = boundariesView.findViewById(R.id.quad_2_longitude);

                final EditText mQuadThreeLatitude = boundariesView.findViewById(R.id.quad_3_latitude);
                final EditText mQuadThreeLongitude = boundariesView.findViewById(R.id.quad_3_longitude);

                final EditText mQuadFourLatitude = boundariesView.findViewById(R.id.quad_4_latitude);
                final EditText mQuadFourLongitude = boundariesView.findViewById(R.id.quad_4_longitude);


                RadioGroup mBoundTypeGroup = boundariesView.findViewById(R.id.bound_type);
                mBoundTypeGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(RadioGroup group, int checkedId) {
                        // checkedId is the RadioButton selected
                        if (checkedId == R.id.quad_button) {
                            mCircleData.setVisibility(View.GONE);
                            mQuadData.setVisibility(View.VISIBLE);
                        } else if (checkedId == R.id.circle_button) {
                            mQuadData.setVisibility(View.GONE);
                            mCircleData.setVisibility(View.VISIBLE);
                        }
                    }
                });
                Log.e(TAG, "trackee id " + trackee.getUID());
                myRef.child("users").child(user.getUid()).child("trackeedata").child(trackee.getUID()).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        mTrackeeName.setText(dataSnapshot.child("name").getValue().toString());
                        mCircleLatitude.setText(dataSnapshot.child("boundary").child("circle").child("center").child("latitude").getValue().toString());
                        mCircleLongitude.setText(dataSnapshot.child("boundary").child("circle").child("center").child("longitude").getValue().toString());
                        mCircleRadius.setText(dataSnapshot.child("boundary").child("circle").child("radius").getValue().toString());

                        mQuadOneLatitude.setText(dataSnapshot.child("boundary").child("quad").child("0").child("latitude").getValue() + "");
                        mQuadOneLongitude.setText(dataSnapshot.child("boundary").child("quad").child("0").child("longitude").getValue().toString());

                        mQuadTwoLatitude.setText(dataSnapshot.child("boundary").child("quad").child("1").child("latitude").getValue().toString());
                        mQuadTwoLongitude.setText(dataSnapshot.child("boundary").child("quad").child("1").child("longitude").getValue().toString());

                        mQuadThreeLatitude.setText(dataSnapshot.child("boundary").child("quad").child("2").child("latitude").getValue().toString());
                        mQuadThreeLongitude.setText(dataSnapshot.child("boundary").child("quad").child("2").child("longitude").getValue().toString());

                        mQuadFourLatitude.setText(dataSnapshot.child("boundary").child("quad").child("3").child("latitude").getValue().toString());
                        mQuadFourLongitude.setText(dataSnapshot.child("boundary").child("quad").child("3").child("longitude").getValue().toString());
                    }

                    public void onCancelled(DatabaseError error) {
                        Log.w(TAG, "Failed to read value.", error.toException());
                    }
                });
                // set dialog message
                alertDialogBuilder
                        .setCancelable(false)
                        .setPositiveButton("OK",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {

                                        myRef.child("users").child(user.getUid()).child("trackeedata").child(trackee.getUID()).child("name").setValue(mTrackeeName.getText().toString());
                                        myRef.child("users").child(user.getUid()).child("trackeedata").child(trackee.getUID()).child("boundary").child("circle").child("center").child("latitude").setValue(Integer.parseInt(mCircleLatitude.getText().toString()));
                                        myRef.child("users").child(user.getUid()).child("trackeedata").child(trackee.getUID()).child("boundary").child("circle").child("center").child("longitude").setValue(Integer.parseInt(mCircleLongitude.getText().toString()));
                                        myRef.child("users").child(user.getUid()).child("trackeedata").child(trackee.getUID()).child("boundary").child("circle").child("radius").setValue(Integer.parseInt(mCircleRadius.getText().toString()));

                                        myRef.child("users").child(user.getUid()).child("trackeedata").child(trackee.getUID()).child("boundary").child("quad").child("0").child("latitude").setValue(Integer.parseInt(mQuadOneLatitude.getText().toString()));
                                        myRef.child("users").child(user.getUid()).child("trackeedata").child(trackee.getUID()).child("boundary").child("quad").child("0").child("longitude").setValue(Integer.parseInt(mQuadOneLongitude.getText().toString()));

                                        myRef.child("users").child(user.getUid()).child("trackeedata").child(trackee.getUID()).child("boundary").child("quad").child("1").child("latitude").setValue(Integer.parseInt(mQuadTwoLatitude.getText().toString()));
                                        myRef.child("users").child(user.getUid()).child("trackeedata").child(trackee.getUID()).child("boundary").child("quad").child("1").child("longitude").setValue(Integer.parseInt(mQuadTwoLongitude.getText().toString()));

                                        myRef.child("users").child(user.getUid()).child("trackeedata").child(trackee.getUID()).child("boundary").child("quad").child("2").child("latitude").setValue(Integer.parseInt(mQuadThreeLatitude.getText().toString()));
                                        myRef.child("users").child(user.getUid()).child("trackeedata").child(trackee.getUID()).child("boundary").child("quad").child("2").child("longitude").setValue(Integer.parseInt(mQuadThreeLongitude.getText().toString()));

                                        myRef.child("users").child(user.getUid()).child("trackeedata").child(trackee.getUID()).child("boundary").child("quad").child("3").child("latitude").setValue(Integer.parseInt(mQuadFourLatitude.getText().toString()));
                                        myRef.child("users").child(user.getUid()).child("trackeedata").child(trackee.getUID()).child("boundary").child("quad").child("3").child("longitude").setValue(Integer.parseInt(mQuadFourLongitude.getText().toString()));


//                                        // get user input and set it to result
//                                        // edit text
//                                        AuthCredential credential = EmailAuthProvider.getCredential(user.getEmail(), oldpass.getText().toString());
//                                        user.reauthenticate(credential);
//                                        if (newpass1.getText().toString().equals(newpass2.getText().toString())) {
//                                            user.updatePassword(newpass1.getText().toString()).addOnCompleteListener(new OnCompleteListener() {
//                                                @Override
//                                                public void onComplete(@NonNull Task task) {
//                                                    if (task.isSuccessful()) {
//                                                    //    Toast.makeText(getActivity(), "Password is updated!", Toast.LENGTH_SHORT).show();
//                                                    } else {
//                                                      //  Toast.makeText(getActivity(), "Failed to update password!", Toast.LENGTH_SHORT).show();
//                                                    }
//                                                }
//                                            });
//                                        } else {
//                                           // Toast.makeText(getActivity(), "Passwords do not match!", Toast.LENGTH_SHORT).show();
//
//                                        }                                     \\


                                    }
                                })
                        .setNegativeButton("Cancel",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        dialog.cancel();
                                    }
                                });

                // create alert dialog
                AlertDialog alertDialog = alertDialogBuilder.create();
                // show it
                alertDialog.show();
            }
        });
        Button dButton = viewHolder.deleteButton;
        //button.setText(trackee.isOnline() ? "Message" : "Offline");
        //button.setEnabled(trackee.isOnline());
    }

    // Returns the total count of items in the list
    @Override
    public int getItemCount() {
        return mTrackees.size();
    }

    // Provide a direct reference to each of the views within a data item
    // Used to cache the views within the item layout for fast access
    public class ViewHolder extends RecyclerView.ViewHolder {
        // Your holder should contain a member variable
        // for any view that will be set as you render a row
        public TextView nameTextView;
        public TextView syncTextView;
        public Button editButton;
        public Button deleteButton;

        // We also create a constructor that accepts the entire item row
        // and does the view lookups to find each subview
        public ViewHolder(View itemView) {
            // Stores the itemView in a public final member variable that can be used
            // to access the context from any ViewHolder instance.
            super(itemView);

            nameTextView = itemView.findViewById(R.id.trackee_name);
            syncTextView = itemView.findViewById(R.id.trackee_sync);
            editButton = itemView.findViewById(R.id.edit_trackee_button);
            deleteButton = itemView.findViewById(R.id.delete_trackee_button);
        }
    }
}

