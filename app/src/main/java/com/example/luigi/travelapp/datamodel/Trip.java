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
    private Date startDate;
    private Date endDate;

    private ArrayList<Day> days;

    public Trip() {
    }

    // Constructor
    public Trip(String title, Date startDate, Date endDate) {
        this.title = title;
        this.startDate = startDate;
        this.endDate = endDate;

        days = new ArrayList<>();
        for (int i = 1; i <= getDaysNumber(); i++) {
            days.add(new Day(i));
        }
    }

    // Get Methods
    public String getTitle() {
        return title;
    }

    public Date getStartDate() {
        return startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    // Set Methods
    public void setTitle(String titleTrip) {
        this.title = titleTrip;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public int getDaysNumber() {
        return (int)((endDate.getTime() - startDate.getTime()) / (1000 * 60 * 60 * 24)) + 1;
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
