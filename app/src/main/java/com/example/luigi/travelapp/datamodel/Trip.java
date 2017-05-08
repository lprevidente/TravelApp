package com.example.luigi.travelapp.datamodel;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;

/**
 *
 * Created by Luigi on 08/05/2017.
 */

public class Trip implements Serializable {

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
        // Creation a List of the day
        days = new ArrayList<>();
        for( int i=1; i<=getDaysNumber(); i++){
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

    public ArrayList<Day> getDayList(){
        return days;
    }

    public int getDaysNumber() {
        if((endDate.getTime() - startDate.getTime()) / (1000 * 60 * 60 * 24)!=0)
        return (int)((endDate.getTime() - startDate.getTime()) / (1000 * 60 * 60 * 24))+1;
        else return 1;
    }

    public void addDay(Day day){
        days.add(day);
    }
}
