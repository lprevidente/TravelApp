package com.example.luigi.travelapp.datamodel;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by Luigi on 08/05/2017.
 */

public class Day implements Serializable {
    private Date date;
    private int dayNumber;
    private ArrayList<Event> events;

    /**
     * Day object constructor
     * @param dayNumber ID for the day to be represented in the list view
     */
    public Day(Date date, int dayNumber) {
        this.date = date;
        this.dayNumber = dayNumber;
        events = new ArrayList<>();
    }

    // Get the day's number
    public int getDayNumber() {
        return dayNumber;
    }

    public Date getDate() {
        return date;
    }

    /**
     * restituisce la lista eventi dell'oggetto
     * @return ArrayList di oggetti di tipo evento
     */
    public ArrayList<Event> getEventList(){
       return events;
    }
}
