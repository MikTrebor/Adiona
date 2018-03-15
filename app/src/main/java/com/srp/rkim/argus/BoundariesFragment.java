package com.srp.rkim.argus;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioGroup;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by 2018rkim on 2/27/2018.
 */
public class BoundariesFragment extends Fragment {
    Context context;

    private FirebaseAuth mAuth;
    private FirebaseUser user;

    private RadioGroup mBoundTypeGroup;

    private LinearLayout mCircleData;
    private LinearLayout mQuadData;

    private EditText mCircleLatitude;
    private EditText mCircleLongitude;
    private EditText mCircleRadius;

    private EditText mQuadOneLatitude;
    private EditText mQuadOneLongitude;
    private EditText mQuadTwoLatitude;
    private EditText mQuadTwoLongitude;
    private EditText mQuadThreeLatitude;
    private EditText mQuadThreeLongitude;
    private EditText mQuadFourLatitude;
    private EditText mQuadFourLongitude;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        context = getActivity();
        View view = inflater.inflate(R.layout.dialog_boundaries, container, false);
        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        FirebaseDatabase mDatabase = FirebaseDatabase.getInstance();
        final DatabaseReference myRef = mDatabase.getReference();

        mCircleData = view.findViewById(R.id.circle_data);
        mQuadData = view.findViewById(R.id.quad_data);
        mCircleData.setVisibility(View.GONE);
        mQuadData.setVisibility(View.GONE);

        mCircleLatitude = view.findViewById(R.id.circle_center_latitude);
        mCircleLongitude = view.findViewById(R.id.circle_center_longitude);
        mCircleRadius = view.findViewById(R.id.circle_radius);

        mQuadOneLatitude = view.findViewById(R.id.quad_1_latitude);
        mQuadOneLongitude = view.findViewById(R.id.quad_1_longitude);

        mQuadTwoLatitude = view.findViewById(R.id.quad_2_latitude);
        mQuadTwoLongitude = view.findViewById(R.id.quad_2_longitude);

        mQuadThreeLatitude = view.findViewById(R.id.quad_3_latitude);
        mQuadThreeLongitude = view.findViewById(R.id.quad_3_longitude);

        mQuadFourLatitude = view.findViewById(R.id.quad_4_latitude);
        mQuadFourLongitude = view.findViewById(R.id.quad_4_longitude);

        RadioGroup mBoundTypeGroup = view.findViewById(R.id.bound_type);
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
        return view;
    }
}
