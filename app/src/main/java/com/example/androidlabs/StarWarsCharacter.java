package com.example.androidlabs;

import android.util.Log;

import androidx.annotation.NonNull;

public class StarWarsCharacter {

    private static final String TAG = "StarWarsCharacter"; // Tag for logging

    private final String name;
    private final String height;
    private final String mass;

    public StarWarsCharacter(String name, String height, String mass) {
        this.name = name;
        this.height = height;
        this.mass = mass;

        // Log the creation of a new StarWarsCharacter
        Log.d(TAG, "StarWarsCharacter created: " + this);
    }

    public String getName() {
        return name;
    }

    public String getHeight() {
        return height;
    }

    public String getMass() {
        return mass;
    }

    @NonNull
    @Override
    public String toString() {
        return "StarWarsCharacter{" +
                "name='" + name + '\'' +
                ", height='" + height + '\'' +
                ", mass='" + mass + '\'' +
                '}';
    }
}
