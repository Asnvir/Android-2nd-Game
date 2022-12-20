package com.example.android_hw4_race.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageView;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ProgressBar;

import com.example.android_hw4_race.data.DataManager;
import com.example.android_hw4_race.fragments.Fragment_List;
import com.example.android_hw4_race.fragments.Fragment_Map;
import com.example.android_hw4_race.data.GameManager;
import com.example.android_hw4_race.data.ListOfScoreRecords;
import com.example.android_hw4_race.R;
import com.example.android_hw4_race.data.ScoreRecord;
import com.example.android_hw4_race.init.MyImage;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;

public class Activity_HighestScoreRecords extends AppCompatActivity {

    private ProgressBar highestScoreRecords_IMG_progress;
    private AppCompatImageView highestScoreRecords_IMG_background;
    private ExtendedFloatingActionButton highestScoreRecords_BTN_menu;
    private ExtendedFloatingActionButton highestScoreRecords_BTN_exit;
    private final String url_background = "https://raw.githubusercontent.com/Asnvir/Anroid-Game-Race-V2/main/images/background_img.jpg";
    private Fragment_List fragment_list;
    private Fragment_Map fragment_map;


    private void loadFragments() {
        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.highestScoreRecords_LAY_list, fragment_list)
                .commit();

        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.highestScoreRecords_LAY_map, fragment_map)
                .commit();


    }

    public interface Callback_List {
        ListOfScoreRecords getTopTenScoreRecords();
    }


    Callback_List callback_list = new Callback_List() {
        @Override
        public ListOfScoreRecords getTopTenScoreRecords() {
            return DataManager.getTopTenScoreRecords();
        }
    };


    public interface Callback_Map {
        void setMarkers(GoogleMap map);
    }

    Callback_Map callback_map = new Callback_Map() {
        @Override
        public void setMarkers(GoogleMap map) {
            map.clear();
            ListOfScoreRecords topTenScoreRecords = DataManager.getTopTenScoreRecords();
            if (topTenScoreRecords != null) {
                for (int i = 0; i < topTenScoreRecords.size(); i++) {
                    ScoreRecord result = topTenScoreRecords.get(i);

                    map.addMarker(new MarkerOptions()
                            .position(new LatLng(
                                    result.getLatitude(),
                                    result.getLongitude()))
                            .title("" + i));
                }
            }
        }
    };


    private void initFragments() {
        fragment_map = new Fragment_Map();
        fragment_map.setCallback_map(callback_map);


        fragment_list = new Fragment_List();
        fragment_list.setCallback_list(callback_list);

    }


    private void openActivityWelcome() {
        finish();
        GameManager.updateGameManager();
        Intent intent = new Intent(Activity_HighestScoreRecords.this, Activity_Welcome.class);
        startActivity(intent);
    }

    private void initBtns() {
        highestScoreRecords_BTN_menu.setOnClickListener(v -> openActivityWelcome());
        highestScoreRecords_BTN_exit.setOnClickListener(v -> finish());

    }

    private void initBackground() {
        MyImage.getInstance().loadImage(highestScoreRecords_IMG_background, url_background, highestScoreRecords_IMG_progress);
    }

    private void findViews() {
        highestScoreRecords_IMG_progress = findViewById(R.id.highestScoreRecords_IMG_progress);
        highestScoreRecords_IMG_background = findViewById(R.id.highestScoreRecords_IMG_background);
        highestScoreRecords_BTN_menu = findViewById(R.id.highestScoreRecords_BTN_menu);
        highestScoreRecords_BTN_exit = findViewById(R.id.highestScoreRecords_BTN_exit);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_highest_records);

        findViews();
        initBackground();
        initBtns();
        initFragments();
        loadFragments();
    }


}