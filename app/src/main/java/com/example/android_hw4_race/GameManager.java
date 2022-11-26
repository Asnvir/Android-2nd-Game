package com.example.android_hw4_race;

import androidx.appcompat.widget.AppCompatImageView;


import java.util.ArrayList;
import java.util.Random;

public class GameManager {
    private int numOfHearts;
    private final int numOfRows;
    private final int numOfColumns;
    private int indexHeartRemove = -1;
    private int currentColumnCar;
    private final ArrayList<ArrayList<Item>> matrixBombItems = new ArrayList<>();
    private final ArrayList<Item> carItems = new ArrayList<>();
    private final ArrayList<Item> heartItems = new ArrayList<>();
    private final Activity_Game activity_game;
    private final String STRING_LOST_1_LIFE = "You lost 1 life";
    private final String STRING_GAME_OVER = "Game Over";


    public GameManager(int numOfHearts, int numOfRows, int numOfColumns, Activity_Game activity_game) {
        this.numOfHearts = numOfHearts;
        this.numOfRows = numOfRows;
        this.numOfColumns = numOfColumns;
        this.activity_game = activity_game;
    }

    private int getRandom(int boundary) {
        return new Random().nextInt(boundary);
    }

    private void moveItemDown(Item item, Type type) {
        item.setType(type);
    }

    private boolean isLastRow(int currentIndexRow, int lastIndexRow) {
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
        if (typeOfArray.equals("cars")) {
            activity_game.renderCars(carItems);
        } else if (typeOfArray.equals("hearts")) {
            activity_game.renderHearts(heartItems);
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
        activity_game.renderCars(carItems);
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

    public void updateActivityAfterBoom(String stringToast) {
        activity_game.renderHearts(heartItems);
        activity_game.renderToast(stringToast);
        activity_game.makeVibrate();
    }

    private boolean isBoom(int currentColumnBomb) {
        return currentColumnCar == currentColumnBomb;
    }

    private void decreaseLife() {
        numOfHearts -= 1;
        indexHeartRemove += 1;
        updateHeartsItems(indexHeartRemove);
    }

    private boolean gameOver() {
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

                boolean lastRow = isLastRow(indexRow, numOfRows - 1);
                boolean itemVisible = isItemVisible(currentItem);

                if (lastRow && itemVisible) {
                    currentItem.setType(Type.INVISIBLE);
                    boolean isBooom = isBoom(indexColumn);
                    if (isBooom) {
                        decreaseLife();
                        String stringToast = STRING_LOST_1_LIFE;
                        boolean isGameOver = gameOver();
                        if (isGameOver) {
                            stringToast = STRING_GAME_OVER;
                            activity_game.gameOver();
                        }
                        updateActivityAfterBoom(stringToast);
                    }

                } else if (!lastRow) {
                    Item lowerItem = matrixBombItems.get(indexRow + 1).get(indexColumn);
                    moveItemDown(lowerItem, currentItem.getType());
                }
            }
        }
    }

    public void updateBombs() {
        updateBombsTable();
        updateFirstRowBombPosition();
        activity_game.renderBombsTable(matrixBombItems);
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
}

