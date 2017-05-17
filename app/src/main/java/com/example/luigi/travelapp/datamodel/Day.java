package com.example.luigi.travelapp.datamodel;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Luigi on 08/05/2017.
 */

public class Day implements Serializable{
    private int dayNumber;
    private ArrayList<Event> events;

    /**
     * Day object constructor
     * @param dayNumber ID for the day to be represented in the list view
     */
    public Day(int dayNumber) {
        this.dayNumber = dayNumber;
        events = new ArrayList<>();
    }

    // Get the day's number
    public int getDayNumber() {
        return dayNumber;
    }

    /**
     * restituisce la lista eventi dell'oggetto
     * @return ArrayList di oggetti di tipo evento
     */
    public ArrayList<Event> getEventList(){
       return events;
    }
}
