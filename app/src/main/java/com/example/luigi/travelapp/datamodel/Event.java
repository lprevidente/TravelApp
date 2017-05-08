package com.example.luigi.travelapp.datamodel;

import android.widget.CheckBox;

import java.util.Date;

/**
 * Created by Luigi on 08/05/2017.
 */

public class Event {

    //Time Of Event
    private Date date;
    private String title;
    private String note;
    private CheckBox notify;

    /**
     * constructor for the Event object
     * @param date Date object to be passed (useful only as container for the event's time information
     * @param title String to represent the name of the event
     * @param note String to represent the extra information
     * @param notify Checkbox to represent the possibility to notify the event
     */
    public Event(Date date, String title, String note, CheckBox notify) {
        this.date = date;
        this.title = title;
        this.note = note;
        this.notify = notify;
    }
// getter methods

    public Date getDate() {
        return date;
    }

    public String getTitle() {
        return title;
    }

    public String getNote() {
        return note;
    }

    public CheckBox getNotify() {
        return notify;
    }

    //Setter methods


    public void setDate(Date date) {
        this.date = date;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public void setNotify(CheckBox notify) {
        this.notify = notify;
    }

    /**
     * set event's date from its date
     */

}
