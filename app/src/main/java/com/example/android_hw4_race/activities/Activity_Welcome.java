package com.example.android_hw4_race.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageView;

import com.example.android_hw4_race.R;
import com.example.android_hw4_race.init.MyGPS;
import com.example.android_hw4_race.init.MyImage;
import com.example.android_hw4_race.util.Util;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;

public class Activity_Welcome extends AppCompatActivity {

    private ProgressBar welcome_IMG_progress;
    private AppCompatImageView welcome_IMG_background;
    private ExtendedFloatingActionButton welcome_BTN_top_scores;
    private ExtendedFloatingActionButton welcome_BTN_start_game;

    private void initBackground() {
        MyImage.getInstance().loadImage(welcome_IMG_background, Util.BACKGROUND_IMG_URL, welcome_IMG_progress);
    }

    private void initViews() {
        welcome_BTN_start_game.setOnClickListener(view -> callback_buttons(view));
        welcome_BTN_top_scores.setOnClickListener(view -> callback_buttons(view));
    }

    private void callback_buttons(View view) {
        final int BTN_START_GAME = R.id.welcome_BTN_start_game;
        final int BTN_TOP_SCORES = R.id.welcome_BTN_top_scores;
        Intent intent;
        switch (view.getId()) {
            case BTN_START_GAME:
                intent = new Intent(Activity_Welcome.this, Activity_ChooseController.class);
                break;
            case BTN_TOP_SCORES:
                intent = new Intent(Activity_Welcome.this, Activity_HighestScoreRecords.class);
                break;
            default:
                throw new IllegalArgumentException("Invalid view id");

        }
        finish();
        startActivity(intent);

    }


    private void init() {
        initViews();
        initBackground();
        MyGPS.getInstance().checkPermissions(this);
    }

    private void findViews() {
        welcome_IMG_progress = findViewById(R.id.welcome_IMG_progress);
        welcome_IMG_background = findViewById(R.id.welcome_IMG_background);
        welcome_BTN_top_scores = findViewById(R.id.welcome_BTN_top_scores);
        welcome_BTN_start_game = findViewById(R.id.welcome_BTN_start_game);

    }


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        findViews();
        init();

    }
}
