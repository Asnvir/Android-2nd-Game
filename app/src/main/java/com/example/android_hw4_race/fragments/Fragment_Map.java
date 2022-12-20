package com.example.android_hw4_race.fragments;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import com.example.android_hw4_race.R;
import com.example.android_hw4_race.activities.Activity_HighestScoreRecords;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;

public class Fragment_Map extends Fragment {

    private SupportMapFragment supportMapFragment;
    private Activity_HighestScoreRecords.Callback_Map callback_map;


    public Fragment_Map (){}

    public void setCallback_map(Activity_HighestScoreRecords.Callback_Map callback_map){
        this.callback_map = callback_map;
    }

    OnMapReadyCallback onMapReadyCallback = googleMap -> callback_map.setMarkers(googleMap);

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_map, container, false);

        supportMapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.fragment_map_MAP);
        if (supportMapFragment != null) {
            supportMapFragment.getMapAsync(onMapReadyCallback);
        }

        return view;
    }
}