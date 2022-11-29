package com.example.android_hw4_race;

import androidx.appcompat.widget.AppCompatImageView;


import java.util.ArrayList;
import java.util.Random;

public class GameManager {
    private static GameManager gameManager = null;
    private int numOfHearts;
    private int numOfRows;
    private int numOfColumns;
    private int indexHeartRemove = -1;
    private int currentColumnCar;
    private final ArrayList<ArrayList<Item>> matrixBombItems = new ArrayList<>();
    private final ArrayList<Item> carItems = new ArrayList<>();
    private final ArrayList<Item> heartItems = new ArrayList<>();
    private final String STRING_LOST_1_LIFE = "You lost 1 life";
    private final String STRING_GAME_OVER = "Game Over";
    private boolean isBoom = false;
    private boolean isGameOver = false;


    private GameManager() {
    }

    public static GameManager getInstance() {
        if (gameManager == null) {
            gameManager = new GameManager();
        }
        return gameManager;
    }

    public GameManager setNumOfHearts(int numOfHearts) {
        this.numOfHearts = numOfHearts;
        return this;
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

    private void moveItemDown(Item item, Type type) {
        item.setType(type);
    }

    private boolean checkLastRow(int currentIndexRow, int lastIndexRow) {
        return currentIndexRow == lastIndexRow;
    }

    private boolean isItemVisible(Item item) {
        return item.getType() == Type.VISIBLE;
    }

    public void initBombsMatrix(ArrayList<ArrayList<AppCompatImageView>> game_IMG_Bombs) {
        int numRows = game_IMG_Bombs.size();

        for (int indexRow = 0; indexRow < numRows; indexRow++) {
            int numColumns = game_IMG_Bombs.get(indexRow).size();
            matrixBombItems.add(new ArrayList<>());

            for (int indexColumn = 0; indexColumn < numColumns; indexColumn++) {
                AppCompatImageView currentImage = game_IMG_Bombs.get(indexRow).get(indexColumn);
                Item currentItem = new Item()
                        .setImage(currentImage)
                        .setType(Type.INVISIBLE);
                matrixBombItems.get(indexRow).add(currentItem);
            }
        }
    }

    public void initItems(ArrayList<AppCompatImageView> arrayList, String typeOfArray) {
        int numOfItems = arrayList.size();
        int randomCarIndex = getRandom(numOfItems);

        for (int indexItem = 0; indexItem < numOfItems; indexItem++) {
            AppCompatImageView currentImage = arrayList.get(indexItem);
            Item currentItem = new Item()
                    .setImage(currentImage);
            if (typeOfArray.equals("cars")) {
                if (indexItem == randomCarIndex) {
                    currentColumnCar = randomCarIndex;
                    currentItem.setType(Type.VISIBLE);
                } else {
                    currentItem.setType(Type.INVISIBLE);
                }
                carItems.add(currentItem);

            } else if (typeOfArray.equals("hearts")) {
                currentItem.setType(Type.VISIBLE);
                heartItems.add(currentItem);
            }
        }
    }

    public void clicked(String direction) {
        int nextColumnCar;

        if (direction.equals("left")) {
            nextColumnCar = currentColumnCar - 1;
            if (nextColumnCar >= 0) {
                updateCars(nextColumnCar);
                currentColumnCar = nextColumnCar;
            }
        } else if (direction.equals("right")) {
            nextColumnCar = currentColumnCar + 1;
            if (nextColumnCar < numOfColumns) {
                updateCars(nextColumnCar);
                currentColumnCar = nextColumnCar;
            }
        }
    }

    private void updateCars(int indexVisibleCar) {
        int numOfCars = carItems.size();

        for (int indexCar = 0; indexCar < numOfCars; indexCar++) {
            Item currentCar = carItems.get(indexCar);
            if (indexCar == indexVisibleCar) {
                currentCar.setType(Type.VISIBLE);
            } else {
                currentCar.setType(Type.INVISIBLE);
            }
        }
    }

    private boolean checkBoom(int currentColumnBomb) {
        isBoom = currentColumnCar == currentColumnBomb;
        return isBoom;
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
        heartRemove.setType(Type.INVISIBLE);
    }

    private void updateFirstRowBombPosition() {
        int random = getRandom(numOfColumns);

        for (int i = 0; i < numOfColumns; i++) {
            Item currentItem = matrixBombItems.get(0).get(i);
            if (i == random) {
                currentItem.setType(Type.VISIBLE);
            } else {
                currentItem.setType(Type.INVISIBLE);
            }
        }
    }

    private void updateBombsTable() {

        for (int indexRow = numOfRows - 1; indexRow >= 0; indexRow--) {
            for (int indexColumn = 0; indexColumn < numOfColumns; indexColumn++) {

                Item currentItem = matrixBombItems.get(indexRow).get(indexColumn);
                boolean isLastRow = checkLastRow(indexRow, numOfRows - 1);
                boolean isCurrentItemVisible = isItemVisible(currentItem);

                if (isLastRow && isCurrentItemVisible) {
                    currentItem.setType(Type.INVISIBLE);
                    isBoom = checkBoom(indexColumn);
                    if (isBoom) {
                        decreaseLife();
                        isGameOver = checkGameOver();
                    }

                } else if (!isLastRow) {
                    Item lowerItem = matrixBombItems.get(indexRow + 1).get(indexColumn);
                    moveItemDown(lowerItem, currentItem.getType());
                }
            }
        }
    }

    public void updateBombs() {
        updateBombsTable();
        updateFirstRowBombPosition();
    }

    public ArrayList<ArrayList<Item>> getMatrixBombItems() {
        return matrixBombItems;
    }

    public ArrayList<Item> getCarItems() {
        return carItems;
    }

    public ArrayList<Item> getHeartItems() {
        return heartItems;
    }

    public String getSTRING_LOST_1_LIFE() {
        return STRING_LOST_1_LIFE;
    }

    public String getSTRING_GAME_OVER() {
        return STRING_GAME_OVER;
    }

    public boolean isGameOver() {
        return isGameOver;
    }

    public boolean isBoom() {
        return isBoom;
    }

}

