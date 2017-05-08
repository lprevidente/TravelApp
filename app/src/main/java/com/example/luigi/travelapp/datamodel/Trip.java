package com.example.luigi.travelapp.datamodel;

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

    // Constructor
    public Trip(String titleTrip, Date startDate, Date endDate) {
        this.titleTrip = titleTrip;
        this.startDate = startDate;
        this.endDate = endDate;

        // Crea un'arrayList per i giorni
        days = new ArrayList<>();
        for (int i = 0; i < getDaysNumber(); i++){
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
        return (int)((endDate.getTime() - startDate.getTime()) / (1000 * 60 * 60 * 24));
    }

    public void addDay(Day day){
        days.add(day);
    }
}
