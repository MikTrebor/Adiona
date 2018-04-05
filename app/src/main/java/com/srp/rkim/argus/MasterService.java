package com.srp.rkim.argus;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.maps.android.PolyUtil;

import java.util.ArrayList;

/**
 * Created by 2018rkim on 3/20/2018.
 */
public class MasterService extends Service {
    private static final String TAG = "MasterService";
    Context context;
    FirebaseDatabase mDatabase;
    DatabaseReference myRef;
    private FirebaseAuth mAuth;
    private FirebaseUser user;
    private ArrayList<String> trackeeUIDs;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
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


        /*
        myRef.child("users").child(user.getUid()).child("trackeedata").child(uid).child("type").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot inDataSnapshot) {
}
            @Override
            public void onCancelled(DatabaseError error) {
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });
        */


        myRef.child("users").child(user.getUid()).child("trackees").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                trackeeUIDs = (ArrayList<String>) dataSnapshot.getValue();
                // System.out.println("td" + trackeeUIDs);
                for (final String uid : trackeeUIDs) {
                    //Log.e(TAG, uid);
                    myRef.child("users").child(user.getUid()).child("trackeedata").child(uid).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(final DataSnapshot inDataSnapshot) {
                            if (inDataSnapshot.child("type").getValue().toString().equals("quad")) {
                                myRef.child("users").child(user.getUid()).child("trackeedata").child(uid).child("boundary").child("quad").addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot inInDataSnapshot) {
                                        final ArrayList<LatLng> quadBounds = new ArrayList<LatLng>();
                                        quadBounds.add(new LatLng((double) inInDataSnapshot.child("0").child("latitude").getValue(), (double) inInDataSnapshot.child("0").child("longitude").getValue()));
                                        quadBounds.add(new LatLng((double) inInDataSnapshot.child("1").child("latitude").getValue(), (double) inInDataSnapshot.child("1").child("longitude").getValue()));
                                        quadBounds.add(new LatLng((double) inInDataSnapshot.child("2").child("latitude").getValue(), (double) inInDataSnapshot.child("2").child("longitude").getValue()));
                                        quadBounds.add(new LatLng((double) inInDataSnapshot.child("3").child("latitude").getValue(), (double) inInDataSnapshot.child("3").child("longitude").getValue()));
                                        myRef.child("users").child(uid).child("location").addValueEventListener(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(DataSnapshot inInInDataSnapshot) {
                                                //if(!isPointInPolygon(new LatLng((double) inInInDataSnapshot.child("latitude").getValue(), (double) inInInDataSnapshot.child("longitude").getValue()), quadBounds)) {
                                                if (!PolyUtil.containsLocation(new LatLng((double) inInInDataSnapshot.child("latitude").getValue(), (double) inInInDataSnapshot.child("longitude").getValue()), quadBounds, false)) {
                                                    Log.e(TAG, "OUTSIDE QUAD");
                                                    Log.e(TAG, "point: " + inInInDataSnapshot.getValue());
                                                    Log.e(TAG, "bounds: " + quadBounds.toString());
//                                                    NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context, TAG)
//                                                            .setSmallIcon(R.drawable.ic_notifications_black_24dp)
//                                                            .setContentTitle("Argus")
//                                                            .setContentText(inDataSnapshot.child("name").getValue().toString() + " has left their boundary")
//                                                            .setPriority(NotificationCompat.PRIORITY_DEFAULT);
//
//
//                                                    NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
//                                                    notificationManager.notify(1, mBuilder.build());
                                                    NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

                                                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                                                        int importance = NotificationManager.IMPORTANCE_LOW;
                                                        NotificationChannel notificationChannel = new NotificationChannel(inDataSnapshot.child("name").getValue().toString(), inDataSnapshot.child("name").getValue().toString(), importance);
                                                        notificationChannel.enableLights(true);
                                                        notificationChannel.setLightColor(Color.RED);
                                                        notificationChannel.enableVibration(true);
                                                        notificationChannel.setVibrationPattern(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});
                                                        notificationManager.createNotificationChannel(notificationChannel);

                                                    }

                                                    NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context, inDataSnapshot.child("name").getValue().toString())
                                                            .setSmallIcon(R.drawable.ic_notifications_black_24dp)
                                                            .setContentTitle("Argus")
                                                            .setContentText(inDataSnapshot.child("name").getValue().toString() + " has left their boundary")
                                                            .setPriority(NotificationCompat.PRIORITY_DEFAULT);


                                                    notificationManager.notify((int) (System.currentTimeMillis() / 1000), mBuilder.build());

                                                }
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


                            } else if (inDataSnapshot.getValue().toString().equals("circle")) {
                                myRef.child("users").child(user.getUid()).child("trackeedata").child(uid).child("boundary").child("circle").addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot inInDataSnapshot) {
                                        myRef.child("users").child(uid).child("location").addValueEventListener(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(DataSnapshot inInInDataSnapshot) {

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
                            Log.w(TAG, "Failed to read value.", error.toException());
                        }
                    });
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });
    }

//    private boolean isPointInPolygon(LatLng tap, ArrayList<LatLng> vertices) {//https://stackoverflow.com/questions/26014312/identify-if-point-is-in-the-polygon
//        int intersectCount = 0;
//        for (int j = 0; j < vertices.size() - 1; j++) {
//            if (rayCastIntersect(tap, vertices.get(j), vertices.get(j + 1))) {
//                intersectCount++;
//            }
//        }
//
//        return ((intersectCount % 2) == 1); // odd = inside, even = outside;
//    }
//
//    private boolean rayCastIntersect(LatLng tap, LatLng vertA, LatLng vertB) {
//
//        double aY = vertA.latitude;
//        double bY = vertB.latitude;
//        double aX = vertA.longitude;
//        double bX = vertB.longitude;
//        double pY = tap.latitude;
//        double pX = tap.longitude;
//
//        if ((aY > pY && bY > pY) || (aY < pY && bY < pY)
//                || (aX < pX && bX < pX)) {
//            return false; // a and b can't both be above or below pt.y, and a or
//            // b must be east of pt.x
//        }
//
//        double m = (aY - bY) / (aX - bX); // Rise over run
//        double bee = (-aX) * m + aY; // y = mx + b
//        double x = (pY - bee) / m; // algebra is neat!
//
//        return x > pX;
//    }
}
