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

    private ArrayList<Day> days;

    public Trip() {
    }

    // Constructor
    public Trip(String title, Date startDate, Date endDate) {
        this.title = title;
        startTime = startDate.getTime();
        endTime = endDate.getTime();

        days = new ArrayList<>();
        for (int i = 1; i <= getDaysNumber(); i++) {
            days.add(new Day(i));
        }
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

    public int getDaysNumber() {
        return (int)((endTime - startTime) / (1000 * 60 * 60 * 24)) + 1;
    }

    public ArrayList<Day> getDays() {
        return days;
    }

    public void setDays(ArrayList<Day> days) {
        this.days = days;
    }

    public void addDay(Day day){
        days.add(day);
    }
}
