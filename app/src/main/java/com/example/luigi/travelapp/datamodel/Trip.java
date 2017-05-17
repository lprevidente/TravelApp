package com.example.luigi.travelapp.datamodel;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;

import static com.example.luigi.travelapp.util.DateUtil.incrementDay;

/**
 * Created by Luigi on 08/05/2017.
 */

public class Trip implements Serializable {
    private String titleTrip;
    private Date startDate;
    private Date endDate;
    private ArrayList<Day> days;

    // Constructor
    public Trip(String titleTrip, Date startDate, Date endDate) {
        this.titleTrip = titleTrip;
        this.startDate = startDate;
        this.endDate = endDate;

        days = new ArrayList<>();
        for (int i = 1; i <= getDaysNumber(); i++) {
            days.add(new Day(i));
        }
    }

    // Get Methods
    public String getTitleTrip() {
        return titleTrip;
    }

    public Date getStartDate() {
        return startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    // Set Methods
    public void setTitleTrip(String titleTrip) {
        this.titleTrip = titleTrip;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public ArrayList<Day> getDayList(){
        return days;
    }

    public int getDaysNumber() {
        return (int)((endDate.getTime() - startDate.getTime()) / (1000 * 60 * 60 * 24)) + 1;
    }

    public void addDay(Day day){
        days.add(day);
    }

}
