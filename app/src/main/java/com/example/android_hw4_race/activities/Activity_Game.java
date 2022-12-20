package com.example.android_hw4_race.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageView;


import android.annotation.SuppressLint;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;


import com.example.android_hw4_race.data.GameManager;
import com.example.android_hw4_race.data.Item;
import com.example.android_hw4_race.R;
import com.example.android_hw4_race.init.MyImage;
import com.example.android_hw4_race.util.SensorDetector;
import com.example.android_hw4_race.util.TypeItem;
import com.example.android_hw4_race.util.TypeVisibility;
import com.example.android_hw4_race.init.MyGPS;
import com.example.android_hw4_race.init.MySignal;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.textview.MaterialTextView;

import java.util.ArrayList;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;


public class Activity_Game extends AppCompatActivity {

    private static final int NUM_OF_HEARTS = 3;
    private static final int NUM_OF_ROWS = 8;
    private static final int NUM_OF_COLUMNS = 5;
    private static final int NUM_OF_CARS = 5;
    private static final int NUM_OF_BUTTONS = 2;

    private ArrayList<AppCompatImageView> game_IMG_Hearts;
    private ArrayList<ArrayList<AppCompatImageView>> game_IMG_Bombs;
    private ArrayList<AppCompatImageView> game_IMG_Cars;
    private ArrayList<ExtendedFloatingActionButton> game_BTNS;
    private boolean isActiveController = true;
    private MaterialTextView game_LBL_score_value;
    private ProgressBar game_IMG_progress;
    private final String url_background = "https://raw.githubusercontent.com/Asnvir/Anroid-Game-Race-V2/main/images/background_img.jpg";
    private AppCompatImageView game_IMG_background;

    private Timer timerUpdateBombs;
    private GameManager gameManager;
    private SensorDetector sensorDetector;
    private int roundsToCoin = 0;

    private void doMove(String direction) {
        if (isActiveController) {
            gameManager.movement(direction);
            renderCars();
        }
    }

    private final SensorDetector.Callback_movement callback_movement = new SensorDetector.Callback_movement() {
        @Override
        public void moveRight() {
            doMove("right");
        }

        @Override
        public void moveLeft() {
            doMove("left");
        }
    };


    public void renderToast(String text) {
        MySignal.getInstance().toast(text);
    }

    private void makeMusic(String type) {
        int musicId = 0;
        switch (type) {
            case "isBoom":
                musicId = R.raw.msc_car_crash;
                break;
            case "isCoin":
                musicId = R.raw.msc_coin_music;
                break;
        }
        if (musicId != 0) {
            MediaPlayer player = MediaPlayer.create(Activity_Game.this, musicId);
            player.start();
        }
    }


    public void makeVibrate() {
        MySignal.getInstance().vibrate();
    }

    public void renderCars() {
        ArrayList<Item> carItems = gameManager.getCarItems();
        for (int index = 0; index < NUM_OF_CARS; index++) {
            AppCompatImageView imgCar = game_IMG_Cars.get(index);
            TypeVisibility typeVisibility = carItems.get(index).getTypeVisibility();
            imgCar.setVisibility(typeVisibility == TypeVisibility.VISIBLE ? View.VISIBLE : View.INVISIBLE);
        }
    }


    public void renderHearts() {
        ArrayList<Item> heartItems = gameManager.getHeartItems();
        for (int index = 0; index < NUM_OF_HEARTS; index++) {
            AppCompatImageView imgHeart = game_IMG_Hearts.get(index);
            TypeVisibility typeVisibility = heartItems.get(index).getTypeVisibility();
            imgHeart.setVisibility(typeVisibility == TypeVisibility.VISIBLE ? View.VISIBLE : View.INVISIBLE);
        }
    }


    public void updateUI() {
        if (gameManager.isBombCollision()) {
            renderCollision("isBoom", gameManager.getSTRING_LOST_1_LIFE(), true);
        } else if (gameManager.isCoinCollision()) {
            renderCollision("isCoin", gameManager.getSTRING_PLUS_10_COINS(), false);
        }
        renderScore();
        renderBombsTable();
        gameManager.setCoinCollision(false);
        gameManager.setBombCollision(false);
    }

    private void renderCollision(String musicType, String toastMessage, boolean doVibrate) {
        renderHearts();
        renderToast(toastMessage);
        if (doVibrate) {
            makeVibrate();
        }
        makeMusic(musicType);
    }


    @SuppressLint("SetTextI18n")
    private void renderScore() {
        game_LBL_score_value.setText(Integer.toString(gameManager.getScore()));
    }


    private void renderGameOver() {
        renderHearts();
        renderScore();
        renderBombsTable();
        showGameOverMessage();
    }

    private void showGameOverMessage() {
        String msg = gameManager.getSTRING_GAME_OVER();
        renderToast(msg);
        makeVibrate();
        makeMusic("isBoom");
    }

    private void openActivityHighestRecords() {
        finish();
        Intent intent = new Intent(Activity_Game.this, Activity_HighestScoreRecords.class);
        startActivity(intent);
    }

    private void updateUIGameOver() {
        renderGameOver();
        isActiveController = false;
        stopTimer();
    }

    private void saveScoreRecord(){
        gameManager.saveNewScoreRecord();
    }

    public void updateUIonTime(boolean insertCoin) {
        gameManager.updateTable(insertCoin);
        if (gameManager.isGameOver()) {
            saveScoreRecord();
            updateUIGameOver();
            openActivityHighestRecords();
        } else {
            updateUI();
        }
    }

    public void renderBombsTable() {
        ArrayList<ArrayList<Item>> matrixBombItems = gameManager.getMatrixItems();

        for (int rowIndex = 0; rowIndex < NUM_OF_ROWS; rowIndex++) {
            for (int columnIndex = 0; columnIndex < NUM_OF_COLUMNS; columnIndex++) {
                Item itemBomb = matrixBombItems.get(rowIndex).get(columnIndex);
                TypeVisibility typeVisibility = itemBomb.getTypeVisibility();
                AppCompatImageView imgBomb = game_IMG_Bombs.get(rowIndex).get(columnIndex);
                if (typeVisibility == TypeVisibility.VISIBLE) {
                    int imageResource = itemBomb.getTypeItem() == TypeItem.Bomb ? R.drawable.img_bomb : R.drawable.img_coin;
                    imgBomb.setImageResource(imageResource);
                    imgBomb.setVisibility(View.VISIBLE);
                } else {
                    imgBomb.setVisibility(View.INVISIBLE);
                }
            }
        }
    }


    private void renderView() {
        renderHearts();
        renderBombsTable();
        renderCars();
    }

    private void stopTimer() {
        timerUpdateBombs.cancel();
    }

    @Override
    protected void onPause() {
        super.onPause();
        stopTimer();
        if (gameManager.isSensorMode()) {
            sensorDetector.stop();
        }
    }

    protected void onStop() {
        super.onStop();
        stopTimer();
        MyGPS.getInstance().stop();
    }

    @Override
    protected void onResume() {
        super.onResume();
        startTimer();
        if (gameManager.isSensorMode()) {
            sensorDetector.start();
        }
        MyGPS.getInstance().start();
    }

    private void startTimer() {
        timerUpdateBombs = new Timer();
        TimerTask callback_updateBombs = new TimerTask() {
            @Override
            public void run() {
                boolean insertCoin = --roundsToCoin == 0;
                roundsToCoin = insertCoin ? roundsToCoin : new Random().nextInt(2);
                runOnUiThread(() -> updateUIonTime(insertCoin));
            }
        };
        timerUpdateBombs.scheduleAtFixedRate(callback_updateBombs, gameManager.getDELAY(), gameManager.getDELAY());
    }

    private void initBTNS() {
        game_BTNS.get(0).setOnClickListener(v -> doMove("left"));
        game_BTNS.get(1).setOnClickListener(v -> doMove("right"));
    }

    private void initSensor() {
        sensorDetector = new SensorDetector(this, callback_movement);
    }

    private void initControls() {
        if (gameManager.isButtonMode()) {
            initBTNS();
        } else {
            initSensor();
        }
    }


    private void initGameManager() {
        gameManager = GameManager.getInstance().setNumOfHearts(NUM_OF_HEARTS).setNumOfRows(NUM_OF_ROWS).setNumOfColumns(NUM_OF_COLUMNS);
        gameManager.initItems(game_IMG_Hearts, "hearts");
        int imageCoin = R.drawable.img_coin;
        gameManager.initBombsMatrix(game_IMG_Bombs, imageCoin);
        gameManager.initItems(game_IMG_Cars, "cars");

    }

    private void init() {
        initGameManager();
        initControls();
        initBackground();
    }

    private void initBackground() {
        MyImage.getInstance().loadImage(game_IMG_background,url_background,game_IMG_progress);

    }

    private void findBackground() {
        game_IMG_progress = findViewById(R.id.game_IMG_progress);
        game_IMG_background = findViewById(R.id.game_IMG_background);
    }

    private void findButtons() {
        game_BTNS = new ArrayList<>();
        for (int buttonIndex = 0; buttonIndex < NUM_OF_BUTTONS; buttonIndex++) {
            int btn_ID = getResources().getIdentifier("game_BTN_" + buttonIndex, "id", getPackageName());
            ExtendedFloatingActionButton current_BTN = findViewById(btn_ID);
            current_BTN.setVisibility(View.VISIBLE);
            game_BTNS.add(current_BTN);
        }
    }


    private void findControl() {
        if (GameManager.getInstance().isButtonMode()) {
            findButtons();
        }

    }

    private void findCars() {
        game_IMG_Cars = new ArrayList<>(NUM_OF_CARS);
        for (int carIndex = 0; carIndex < NUM_OF_CARS; carIndex++) {
            int car_ID = getResources().getIdentifier("game_IMG_car_" + carIndex, "id", getPackageName());
            AppCompatImageView currentCar = findViewById(car_ID);
            game_IMG_Cars.add(currentCar);
        }
    }

    private void findBombs() {
        game_IMG_Bombs = new ArrayList<>(NUM_OF_ROWS);
        for (int rowIndex = 0; rowIndex < NUM_OF_ROWS; rowIndex++) {
            game_IMG_Bombs.add(new ArrayList<>(NUM_OF_COLUMNS));
            for (int columnIndex = 0; columnIndex < NUM_OF_COLUMNS; columnIndex++) {
                int bomb_ID = getResources().getIdentifier("game_IMG_bomb_" + rowIndex + "_" + columnIndex, "id", getPackageName());
                AppCompatImageView current_Bomb = findViewById(bomb_ID);
                game_IMG_Bombs.get(rowIndex).add(current_Bomb);
            }
        }
    }

    private void findScore() {
        game_LBL_score_value = findViewById(R.id.game_LBL_score_value);
    }

    private void findHearts() {
        game_IMG_Hearts = new ArrayList<>(NUM_OF_HEARTS);
        for (int heartIndex = 0; heartIndex < NUM_OF_HEARTS; heartIndex++) {
            int heart_ID = getResources().getIdentifier("game_IMG_heart_" + heartIndex, "id", getPackageName());
            AppCompatImageView current_Heart = findViewById(heart_ID);
            game_IMG_Hearts.add(current_Heart);
        }
    }

    private void findViews() {
        findHearts();
        findScore();
        findBombs();
        findCars();
        findControl();
        findBackground();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        findViews();
        init();
        renderView();
    }

}