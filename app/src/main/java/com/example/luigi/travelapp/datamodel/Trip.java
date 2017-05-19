package com.example.luigi.travelapp.datamodel;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;

import static com.example.luigi.travelapp.util.DateUtil.incrementDay;

/**
 * Created by Luigi on 08/05/2017.
 */

public class Trip implements Serializable {
    private String title;
    private long startTime;
    private long endTime;
    private String daysReference;
    private String key;

    public Trip() { }

    public Trip(String title, Date startDate, Date endDate) {
        this.title = title;
        startTime = startDate.getTime();
        endTime = endDate.getTime();
    }

    public String getTitle() {
        return title;
    }

    public long getStartTime() {
        return startTime;
    }

    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }

    public long getEndTime() {
        return endTime;
    }

    public void setEndTime(long endTime) {
        this.endTime = endTime;
    }

    public void setTitle(String titleTrip) {
        this.title = titleTrip;
    }

    public String getDaysReference() {
        return daysReference;
    }

    public void setDaysReference(String daysReference) {
        this.daysReference = daysReference;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public int getDaysNumber() {
        return (int)((endTime - startTime) / (1000 * 60 * 60 * 24)) + 1;
    }
}
