package com.srp.rkim.adiona;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


public class TrackeesFragment extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
//        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss:S");
//        String result = sdf.format();
//        System.out.println(result);

        return inflater.inflate(R.layout.fragment_trackees, container, false);
    }
}
