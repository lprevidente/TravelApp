package com.example.luigi.travelapp.datamodel;

/**
 * Created by Luigi on 08/05/2017.
 */

public class Day {

    private String key;
    private int number;
    // private String eventsReference;

    public Day() { }

    public Day(int number) {
        this.number = number;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }
}
