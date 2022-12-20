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
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;

public class Activity_ChooseController extends AppCompatActivity {

    private ExtendedFloatingActionButton choose_controller_BTN_SensorMode;
    private ExtendedFloatingActionButton choose_controller_BTN_ButtonMode;
    private AppCompatImageView choose_controller_IMG_background;
    private final String url_background = "https://raw.githubusercontent.com/Asnvir/Anroid-Game-Race-V2/main/images/background_img.jpg";
    private ProgressBar choose_controller_IMG_progress;


    private void initBackground() {
        MyImage.getInstance().loadImage(choose_controller_IMG_background,url_background,choose_controller_IMG_progress);
    }


    private void setOnClickListeners() {
        final int BTN_SENSOR_MODE = R.id.choose_controller_BTN_SensorMode;
        final int BTN_BUTTON_MODE = R.id.choose_controller_BTN_ButtonMode;
        View.OnClickListener listener = v -> {
            Intent intent;
            switch (v.getId()) {
                case BTN_SENSOR_MODE:
                    GameManager.getInstance().setSensorMode(true);
                    GameManager.getInstance().setButtonMode(false);
                    intent = new Intent(Activity_ChooseController.this, Activity_Game.class);
                    break;
                case BTN_BUTTON_MODE:
                    GameManager.getInstance().setSensorMode(false);
                    GameManager.getInstance().setButtonMode(true);
                    intent = new Intent(Activity_ChooseController.this, Activity_Button_Speed.class);
                    break;
                default:
                    throw new IllegalArgumentException("Invalid view id");
            }
            finish();
            startActivity(intent);
        };

        choose_controller_BTN_SensorMode.setOnClickListener(listener);
        choose_controller_BTN_ButtonMode.setOnClickListener(listener);
    }


    private void findViews() {
        choose_controller_BTN_SensorMode = findViewById(R.id.choose_controller_BTN_SensorMode);
        choose_controller_BTN_ButtonMode = findViewById(R.id.choose_controller_BTN_ButtonMode);
        choose_controller_IMG_background = findViewById(R.id.choose_controller_IMG_background);
        choose_controller_IMG_progress = findViewById(R.id.choose_controller_IMG_progress);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_controller);

        findViews();
        setOnClickListeners();
        initBackground();

    }


}