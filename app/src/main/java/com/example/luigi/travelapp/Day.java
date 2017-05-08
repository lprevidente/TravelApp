package com.example.luigi.travelapp;

import java.util.ArrayList;

/**
 * Created by Luigi on 08/05/2017.
 */

public class Day {

    private int dayNumber;

    private ArrayList<Event> Eventsoftheday;

    /**
     * Day object constructor
     * @param dayNumber ID for the day to be represented in the list view
     */
    public Day(int dayNumber) {
        this.dayNumber = dayNumber;
        Eventsoftheday = new ArrayList<>();
    }
 // Get the day's number
    public int getDayNumber() {
        return dayNumber;
    }
}
