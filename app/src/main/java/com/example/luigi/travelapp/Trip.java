package com.example.luigi.travelapp;

import java.util.ArrayList;
import java.util.Date;

/**
 *
 * Created by Luigi on 08/05/2017.
 */

public class Trip {

    private String titleTrip;

    private Date startDate;
    private Date endDate;
    private ArrayList<Day> days;
    private Day mday;

    // Constructor
    public Trip(String titleTrip, Date startDate, Date endDate) {
        this.titleTrip = titleTrip;
        this.startDate = startDate;
        this.endDate = endDate;
        days = new ArrayList<>();
        // Create a List of the day
        for( int i=0; i<(endDate.getTime() - startDate.getTime() / (1000 * 60 * 60 * 24)); i++){
            mday= new Day(i);
            days.add(mday);
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


}
