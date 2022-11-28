package com.example.android_hw4_race;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageView;


import android.os.Bundle;
import android.view.View;


import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;


public class Activity_Game extends AppCompatActivity {

    private static final int NUM_OF_HEARTS = 3;
    private static final int NUM_OF_ROWS = 4;
    private static final int NUM_OF_COLUMNS = 3;
    private static final int NUM_OF_CARS = 3;
    private static final int NUM_OF_BUTTONS = 2;
    private static final int DELAY = 1000;

    private ArrayList<AppCompatImageView> game_IMG_Hearts;
    private ArrayList<ArrayList<AppCompatImageView>> game_IMG_Bombs;
    private ArrayList<AppCompatImageView> game_IMG_Cars;
    private ArrayList<ExtendedFloatingActionButton> game_BTNS;
    private boolean isActiveButtons = true;

    private Timer timer;
    GameManager gameManager;


    public void renderToast(String text) {
        MySignal.getInstance().toast(text);
    }

    public void makeVibrate() {
        MySignal.getInstance().vibrate();
    }

    public void renderCars() {
        ArrayList<Item> carItems = gameManager.getCarItems();
        int numOfCars = carItems.size();
        for (int index = 0; index < numOfCars; index++) {
            AppCompatImageView imgCar = game_IMG_Cars.get(index);
            Type type = carItems.get(index).getType();
            if (type == Type.VISIBLE) {
                imgCar.setVisibility(View.VISIBLE);
            } else {
                imgCar.setVisibility(View.INVISIBLE);
            }
        }
    }

    public void renderHearts() {
        ArrayList<Item> heartItems = gameManager.getHeartItems();
        int numOfHearts = heartItems.size();
        for (int index = 0; index < numOfHearts; index++) {
            AppCompatImageView imgHeart = game_IMG_Hearts.get(index);
            Type type = heartItems.get(index).getType();
            if (type == Type.VISIBLE) {
                imgHeart.setVisibility(View.VISIBLE);
            } else {
                imgHeart.setVisibility(View.INVISIBLE);
            }
        }
    }

    public void updateUI() {

        boolean isBoom = gameManager.isBoom();
        if (isBoom) {
            String msg = gameManager.getSTRING_LOST_1_LIFE();
            renderHearts();
            renderToast(msg);
            makeVibrate();
        }
        renderBombsTable();
    }

    private void renderGameOver() {
        String msg = gameManager.getSTRING_GAME_OVER();
        renderHearts();
        renderBombsTable();
        renderToast(msg);
        makeVibrate();
    }

    private void updateUIGameOver() {
        renderGameOver();
        isActiveButtons = false;
        stopTimer();
    }

    public void updateUIonTime() {
        gameManager.updateBombs();
        boolean isGameOver = gameManager.isGameOver();

        if (isGameOver) {
            updateUIGameOver();
        } else {
            updateUI();
        }
    }

    public void renderBombsTable() {
        ArrayList<ArrayList<Item>> matrixBombItems = gameManager.getMatrixBombItems();
        int numOfRows = matrixBombItems.size();

        for (int rowIndex = 0; rowIndex < numOfRows; rowIndex++) {
            int numOfColumns = matrixBombItems.get(rowIndex).size();
            for (int columnIndex = 0; columnIndex < numOfColumns; columnIndex++) {
                Item itemBomb = matrixBombItems.get(rowIndex).get(columnIndex);
                Type type = itemBomb.getType();
                AppCompatImageView imgBomb = game_IMG_Bombs.get(rowIndex).get(columnIndex);
                if (type == Type.VISIBLE) {
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
        timer.cancel();
    }

    @Override
    protected void onPause() {
        super.onPause();
        stopTimer();
    }

    private void startTimer() {
        timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(() -> updateUIonTime());
            }
        }, DELAY, DELAY);
    }

    @Override
    protected void onResume() {
        super.onResume();
        startTimer();
    }

    private void initBTNS() {

        game_BTNS.get(0).setOnClickListener(v -> {
            if (isActiveButtons) {
                gameManager.clicked("left");
                renderCars();
            }
        });

        game_BTNS.get(1).setOnClickListener(v -> {
            if (isActiveButtons) {
                gameManager.clicked("right");
                renderCars();
            }
        });
    }

    private void initGameManager() {
        gameManager = new GameManager(game_IMG_Hearts.size(), NUM_OF_ROWS, NUM_OF_COLUMNS);
        gameManager.initItems(game_IMG_Hearts, "hearts");
        gameManager.initBombsMatrix(game_IMG_Bombs);
        gameManager.initItems(game_IMG_Cars, "cars");
    }

    private void init() {
        initGameManager();
        initBTNS();
    }

    private void findButtons() {
        game_BTNS = new ArrayList<>();
        for (int buttonIndex = 0; buttonIndex < NUM_OF_BUTTONS; buttonIndex++) {
            String btn_name = "game_BTN_" + String.valueOf(buttonIndex);
            int btn_ID = getResources().getIdentifier(btn_name, "id", getPackageName());
            ExtendedFloatingActionButton current_BTN = findViewById(btn_ID);
            game_BTNS.add(current_BTN);
        }
    }

    private void findCars() {
        game_IMG_Cars = new ArrayList<>(NUM_OF_CARS);
        for (int carIndex = 0; carIndex < NUM_OF_CARS; carIndex++) {
            String car_name = "game_IMG_car_" + String.valueOf(carIndex);
            int car_ID = getResources().getIdentifier(car_name, "id", getPackageName());
            AppCompatImageView currentCar = findViewById(car_ID);
            game_IMG_Cars.add(currentCar);
        }
    }

    private void findBombs() {
        game_IMG_Bombs = new ArrayList<>(NUM_OF_ROWS);
        for (int rowIndex = 0; rowIndex < NUM_OF_ROWS; rowIndex++) {
            game_IMG_Bombs.add(new ArrayList<>(NUM_OF_COLUMNS));
            for (int columnIndex = 0; columnIndex < NUM_OF_COLUMNS; columnIndex++) {
                String bomb_name = "game_IMG_bomb_" + String.valueOf(rowIndex) + "_" + String.valueOf(columnIndex);
                int bomb_ID = getResources().getIdentifier(bomb_name, "id", getPackageName());
                AppCompatImageView current_Bomb = findViewById(bomb_ID);
                game_IMG_Bombs.get(rowIndex).add(current_Bomb);
            }
        }
    }

    private void findHearts() {
        game_IMG_Hearts = new ArrayList<>(NUM_OF_HEARTS);
        for (int heartIndex = 0; heartIndex < NUM_OF_HEARTS; heartIndex++) {
            String heart_name = "game_IMG_heart_" + String.valueOf(heartIndex);
            int heart_ID = getResources().getIdentifier(heart_name, "id", getPackageName());
            AppCompatImageView current_Heart = findViewById(heart_ID);
            game_IMG_Hearts.add(current_Heart);
        }
    }

    private void findViews() {
        findHearts();
        findBombs();
        findCars();
        findButtons();
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