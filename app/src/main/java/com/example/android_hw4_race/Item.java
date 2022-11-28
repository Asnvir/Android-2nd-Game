package com.example.android_hw4_race;

import android.widget.ImageView;

import androidx.appcompat.widget.AppCompatImageView;

public class Item {
    private AppCompatImageView image;
    private Type type;

    public Item() {
    }

    public ImageView getImage() {
        return image;
    }

    public Type getType() {
        return type;
    }

    public Item setImage(AppCompatImageView image) {
        this.image = image;
        return this;
    }

    public Item setType(Type type) {
        this.type = type;
        return this;
    }

}
