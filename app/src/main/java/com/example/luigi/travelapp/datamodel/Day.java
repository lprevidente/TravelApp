package com.example.luigi.travelapp.datamodel;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Luigi on 08/05/2017.
 */

public class Day implements Serializable{
    private int number;
    private String eventsReference;

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

    public String getEventsReference() {
        return eventsReference;
    }

    public void setEventsReference(String eventsReference) {
        this.eventsReference = eventsReference;
    }
}
