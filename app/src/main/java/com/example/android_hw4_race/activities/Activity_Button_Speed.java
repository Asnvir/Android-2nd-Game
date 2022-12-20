package com.example.android_hw4_race.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;

import com.example.android_hw4_race.data.GameManager;
import com.example.android_hw4_race.R;
import com.example.android_hw4_race.init.MyImage;
import com.example.android_hw4_race.util.Util;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;

public class Activity_Button_Speed extends AppCompatActivity {
    private ExtendedFloatingActionButton button_speed_BTN_FastMode;
    private ExtendedFloatingActionButton button_speed_BTN_SlowMode;
    private AppCompatImageView button_speed_IMG_background;
    private ProgressBar button_speed_IMG_progress;
    private final int BTN_FAST_MODE = R.id.button_speed_BTN_FastMode;
    private final int BTN_SLOW_MODE = R.id.button_speed_BTN_SlowMode;

    private void initBackground() {
        MyImage.getInstance().loadImage(button_speed_IMG_background, Util.BACKGROUND_IMG_URL,button_speed_IMG_progress);
    }


    private void setOnClickListeners() {
        View.OnClickListener listener = v -> {
            int delay;
            switch (v.getId()) {
                case BTN_FAST_MODE:
                    delay = 500;
                    break;
                case BTN_SLOW_MODE:
                    delay = 1500;
                    break;
                default:
                    throw new IllegalArgumentException("Invalid view id");
            }
            GameManager.getInstance().setDELAY(delay);
            finish();
            Intent intent = new Intent(Activity_Button_Speed.this, Activity_Game.class);
            startActivity(intent);
        };

        button_speed_BTN_FastMode.setOnClickListener(listener);
        button_speed_BTN_SlowMode.setOnClickListener(listener);
    }



    private void findViews() {
        button_speed_BTN_FastMode = findViewById(R.id.button_speed_BTN_FastMode);
        button_speed_BTN_SlowMode = findViewById(R.id.button_speed_BTN_SlowMode);
        button_speed_IMG_background = findViewById(R.id.button_speed_IMG_background);
        button_speed_IMG_progress = findViewById(R.id.button_speed_IMG_progress);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_button_speed);

        findViews();
        setOnClickListeners();
        initBackground();
    }
}