package com.example.luigi.travelapp.datamodel;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Luigi on 08/05/2017.
 */

public class Trip {

    private String title;
    private long startTime;
    private long endTime;
    private String key;
    private String notes;

    public Trip() {
    }

    public Trip(String title, Date startDate, Date endDate, String notes) {
        this.title = title;
        startTime = startDate.getTime();
        endTime = endDate.getTime();
        this.notes = notes;
    }

    public String getTitle() {
        return title;
    }

    public long getStartTime() {
        return startTime;
    }

    public long getEndTime() {
        return endTime;
    }

    public String getKey() {
        return key;
    }

    public String getNotes() {
        return notes;
    }

    public int getDaysNumber() {
        return (int) ((endTime - startTime) / (1000 * 60 * 60 * 24)) + 1;
    }


    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }

    public void setEndTime(long endTime) {
        this.endTime = endTime;
    }

    public void setTitle(String titleTrip) {
        this.title = titleTrip;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

}
