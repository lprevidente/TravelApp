package com.example.luigi.travelapp.datamodel;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Luigi on 08/05/2017.
 */

public class Day implements Serializable{
    private int number;
    private ArrayList<Event> events;

    /**
     * Day object constructor
     * @param number ID for the day to be represented in the list view
     */
    public Day(int number) {
        this.number = number;
        events = new ArrayList<>();
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public ArrayList<Event> getEvents() {
        return events;
    }

    public void setEvents(ArrayList<Event> events) {
        this.events = events;
    }
}
