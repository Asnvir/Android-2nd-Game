package com.example.android_hw4_race.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.android_hw4_race.util.ScoreRecordAdapter;
import com.example.android_hw4_race.data.ListOfScoreRecords;
import com.example.android_hw4_race.R;
import com.example.android_hw4_race.data.ScoreRecord;
import com.example.android_hw4_race.activities.Activity_HighestScoreRecords;

import java.util.List;

public class Fragment_List extends Fragment{

    private ListView fragmentList_LIST_highestScores;
    private Activity_HighestScoreRecords.Callback_List callback_list;


    public void setCallback_list(Activity_HighestScoreRecords.Callback_List callback_list) {
        this.callback_list = callback_list;
    }


    private void initViews() {
        ListOfScoreRecords data = callback_list.getTopTenScoreRecords();
        if (data != null) {
            List<ScoreRecord> scoreRecords = data.getListOfScoreRecords();
            ScoreRecordAdapter adapter = new ScoreRecordAdapter(getActivity(), android.R.layout.simple_list_item_1, scoreRecords);
            fragmentList_LIST_highestScores.setAdapter(adapter);
        }
    }


    private void findViews(View view) {
        fragmentList_LIST_highestScores = view.findViewById(R.id.fragmentList_LIST_highestScores);
    }

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_list, container, false);
        findViews(view);
        initViews();
        return view;
    }


}
