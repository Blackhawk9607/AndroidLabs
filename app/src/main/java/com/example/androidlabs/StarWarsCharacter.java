package com.example.androidlabs;

public class StarWarsCharacter {

    private final String name;
    private final String height;
    private final String mass;

    public StarWarsCharacter(String name, String height, String mass) {
        this.name = name;
        this.height = height;
        this.mass = mass;
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
}
