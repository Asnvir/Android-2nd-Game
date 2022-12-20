package com.example.android_hw4_race.data;

import androidx.appcompat.widget.AppCompatImageView;


import com.example.android_hw4_race.util.TypeItem;
import com.example.android_hw4_race.util.TypeVisibility;
import com.example.android_hw4_race.init.MyGPS;
import com.example.android_hw4_race.init.MySP;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Random;

public class GameManager {
    private static final int COIN_VALUE = 10;
    private static final int DEFAULT_INCREASE_VALUE = 1;
    private static GameManager gameManager = null;
    private int numOfHearts;
    private int numOfRows;
    private int numOfColumns;
    private int indexHeartRemove = -1;
    private int score = 0;
    private int DELAY = 1000;


    private int currentColumnCar;
    private final ArrayList<ArrayList<Item>> matrixItems = new ArrayList<>();
    private final ArrayList<Item> carItems = new ArrayList<>();
    private final ArrayList<Item> heartItems = new ArrayList<>();

    private boolean isGameOver = false;
    private boolean isSensorMode = false;
    private boolean isButtonMode = false;
    private boolean isBombCollision = false;
    private boolean isCoinCollision = false;


    private GameManager() {
    }

    public static void init() {
        if (gameManager == null) {
            gameManager = new GameManager();
        }
    }

    public static GameManager getInstance() {
        return gameManager;
    }

    public static void updateGameManager(){
        gameManager = new GameManager();
    }


    private void moveItemDown(Item item, TypeVisibility typeVisibility, TypeItem typeItem) {
        item.setTypeItem(typeItem);
        item.setTypeVisibility(typeVisibility);
    }

    private boolean checkLastRow(int currentIndexRow, int lastIndexRow) {
        return currentIndexRow == lastIndexRow;
    }

    private boolean isItemVisible(Item item) {
        return item.getTypeVisibility() == TypeVisibility.VISIBLE;
    }

    public void initBombsMatrix(ArrayList<ArrayList<AppCompatImageView>> game_IMG_Bombs, int imageCoin) {
        int numRows = game_IMG_Bombs.size();

        for (int indexRow = 0; indexRow < numRows; indexRow++) {
            int numColumns = game_IMG_Bombs.get(indexRow).size();
            matrixItems.add(new ArrayList<>());

            for (int indexColumn = 0; indexColumn < numColumns; indexColumn++) {
                AppCompatImageView currentImage = game_IMG_Bombs.get(indexRow).get(indexColumn);
                Item currentItem = new Item()
                        .setImageBomb(currentImage)
                        .setImageCoin(imageCoin)
                        .setTypeVisibility(TypeVisibility.INVISIBLE);

                matrixItems.get(indexRow).add(currentItem);
            }
        }
    }

    public void initItems(ArrayList<AppCompatImageView> arrayList, String typeOfArray) {
        int numOfItems = arrayList.size();
        int randomCarIndex = getRandom(numOfItems);

        for (int indexItem = 0; indexItem < numOfItems; indexItem++) {
            AppCompatImageView currentImage = arrayList.get(indexItem);
            Item currentItem = new Item().setImageBomb(currentImage);

            switch (typeOfArray) {
                case "cars":
                    currentItem.setTypeItem(TypeItem.Car);
                    currentItem.setTypeVisibility(indexItem == randomCarIndex ? TypeVisibility.VISIBLE : TypeVisibility.INVISIBLE);
                    carItems.add(currentItem);
                    if (indexItem == randomCarIndex) {
                        currentColumnCar = randomCarIndex;
                    }
                    break;
                case "hearts":
                    currentItem.setTypeItem(TypeItem.Heart);
                    currentItem.setTypeVisibility(TypeVisibility.VISIBLE);
                    heartItems.add(currentItem);
                    break;
                default:
                    break;
            }
        }
    }

    public void movement(String direction) {
        int nextColumnCar;
        boolean moveAllowed = false;

        switch (direction) {
            case "left":
                nextColumnCar = currentColumnCar - 1;
                moveAllowed = (nextColumnCar >= 0);
                break;
            case "right":
                nextColumnCar = currentColumnCar + 1;
                moveAllowed = (nextColumnCar < numOfColumns);
                break;
            default:
                nextColumnCar = currentColumnCar;
                break;
        }

        if (moveAllowed) {
            updateCars(nextColumnCar);
            currentColumnCar = nextColumnCar;
        }
    }

    private void updateCars(int indexVisibleCar) {
        for (Item currentCar : carItems) {
            currentCar.setTypeVisibility(carItems.indexOf(currentCar) == indexVisibleCar ? TypeVisibility.VISIBLE : TypeVisibility.INVISIBLE);
        }
    }

    private void increaseScore(int value) {
        score = score + value;
    }

    private boolean checkCollision(int currentColumnBomb) {
        return currentColumnCar == currentColumnBomb;
    }

    private void decreaseLife() {
        numOfHearts -= 1;
        indexHeartRemove += 1;
        updateHeartsItems(indexHeartRemove);
    }

    private boolean checkGameOver() {
        return numOfHearts == 0;
    }

    private void updateHeartsItems(int indexHeartRemove) {
        Item heartRemove = heartItems.get(indexHeartRemove);
        heartRemove.setTypeVisibility(TypeVisibility.INVISIBLE);
    }

    private void updateFirstRowVisibleItems(boolean insertCoin) {
        int randomColumnBomb = getRandom(numOfColumns);
        int randomColumnCoin = -1;
        if (insertCoin) {
            do {
                randomColumnCoin = getRandom(numOfColumns);
            } while (randomColumnBomb == randomColumnCoin);
        }

        for (int i = 0; i < numOfColumns; i++) {
            Item currentItem = matrixItems.get(0).get(i);
            if (i == randomColumnBomb) {
                currentItem.setTypeItem(TypeItem.Bomb);
                currentItem.setTypeVisibility(TypeVisibility.VISIBLE);
            } else if (insertCoin && i == randomColumnCoin) {
                currentItem.setTypeItem(TypeItem.Coin);
                currentItem.setTypeVisibility(TypeVisibility.VISIBLE);
            } else {
                currentItem.setTypeVisibility(TypeVisibility.INVISIBLE);
            }
        }
    }

    private void updateItemsTable() {

        for (int indexRow = numOfRows - 1; indexRow >= 0; indexRow--) {
            for (int indexColumn = 0; indexColumn < numOfColumns; indexColumn++) {
                Item currentItem = matrixItems.get(indexRow).get(indexColumn);
                boolean isLastRow = checkLastRow(indexRow, numOfRows - 1);
                boolean isCurrentItemVisible = isItemVisible(currentItem);

                if (isLastRow && isCurrentItemVisible) {
                    currentItem.setTypeVisibility(TypeVisibility.INVISIBLE);
                    boolean isCollision = checkCollision(indexColumn);
                    if (isCollision) {
                        checkTypeCollision(currentItem);
                    }
                }

                if (!isLastRow) {
                    Item lowerItem = matrixItems.get(indexRow + 1).get(indexColumn);
                    moveItemDown(lowerItem, currentItem.getTypeVisibility(), currentItem.getTypeItem());
                    currentItem.setTypeItem(TypeItem.None);
                }
            }
        }
    }

    private void checkTypeCollision(Item item) {
        switch (item.getTypeItem()) {
            case Bomb:
                isBombCollision = true;
                decreaseLife();
                isGameOver = checkGameOver();
                break;
            case Coin:
                isCoinCollision = true;
                increaseScore(COIN_VALUE);
                break;
            default:
                break;
        }
    }

    public void updateTable(boolean insertCoin) {
        updateItemsTable();
        increaseScore(DEFAULT_INCREASE_VALUE);
        updateFirstRowVisibleItems(insertCoin);
    }

    public ArrayList<ArrayList<Item>> getMatrixItems() {
        return matrixItems;
    }

    public ArrayList<Item> getCarItems() {
        return carItems;
    }

    public ArrayList<Item> getHeartItems() {
        return heartItems;
    }

    public String getSTRING_LOST_1_LIFE() {
        return "You lost 1 life";
    }

    public String getSTRING_GAME_OVER() {
        return "Game Over";
    }

    public String getSTRING_PLUS_10_COINS() {
        return "+ 10 coins";
    }

    public boolean isGameOver() {
        return isGameOver;
    }

    public void setBombCollision(boolean bombCollision) {
        isBombCollision = bombCollision;
    }

    public void setCoinCollision(boolean coinCollision) {
        isCoinCollision = coinCollision;
    }

    public boolean isSensorMode() {
        return isSensorMode;
    }

    public boolean isButtonMode() {
        return isButtonMode;
    }

    public void setSensorMode(boolean sensorMode) {
        isSensorMode = sensorMode;
    }

    public void setButtonMode(boolean buttonMode) {
        isButtonMode = buttonMode;
    }

    public GameManager setNumOfHearts(int numOfHearts) {
        this.numOfHearts = numOfHearts;
        return this;
    }

    public boolean isBombCollision() {
        return isBombCollision;
    }

    public boolean isCoinCollision() {
        return isCoinCollision;
    }

    public int getScore() {
        return score;
    }

    public GameManager setNumOfRows(int numOfRows) {
        this.numOfRows = numOfRows;
        return this;
    }

    public GameManager setNumOfColumns(int numOfColumns) {
        this.numOfColumns = numOfColumns;
        return this;
    }

    private int getRandom(int boundary) {
        return new Random().nextInt(boundary);
    }

    public int getDELAY() {
        return DELAY;
    }

    public void setDELAY(int DELAY) {
        this.DELAY = DELAY;
    }

    public void saveNewScoreRecord() {
        ScoreRecord newRecord = new ScoreRecord(score, MyGPS.getInstance().getLatitude(), MyGPS.getInstance().getLongitude());

        String json = MySP.getInstance().getString("records", "");
        ListOfScoreRecords listOfScoreRecords = new Gson().fromJson(json, ListOfScoreRecords.class);
        if (listOfScoreRecords == null) {
            listOfScoreRecords = new ListOfScoreRecords();
        }
        listOfScoreRecords.getListOfScoreRecords().add(newRecord);
        MySP.getInstance().putString("records", new Gson().toJson(listOfScoreRecords));
    }


}

