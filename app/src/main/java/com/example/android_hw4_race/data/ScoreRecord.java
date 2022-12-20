package com.example.android_hw4_race.data;

import androidx.annotation.NonNull;

public class ScoreRecord {

    private final int score;
    private final double latitude;
    private final double longitude;

    public ScoreRecord(int score,double latitude,double longitude){
        this.score = score;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public int getScore() {
        return score;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    @NonNull
    public String toString(){
        return "score: " +score + "\nlatitude: " + latitude + "\nlongitude: " + longitude;
    }

}
