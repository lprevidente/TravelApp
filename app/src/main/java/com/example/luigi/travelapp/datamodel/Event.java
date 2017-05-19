package com.example.luigi.travelapp.datamodel;

import android.support.annotation.NonNull;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Luigi on 08/05/2017.
 */

public class Event implements Comparable<Event> {

    private String key;
    private long time;
    private String title;
    private String note;
    private boolean notify;
    private int Image;
    private String typeEvent;

    public Event() { }

    public Event(Date date, String title, String note, boolean notify, int Image) {
        time = date.getTime();
        this.title = title;
        this.note = note;
        this.notify = notify;
        this.Image=Image;
        typeEvent=string;
    }

    public String getTitle() {
        return title;
    }

    public String getNote() {
        return note;
    }

    public boolean getNotify() {
        return notify;
    }

    public int getImage() {
        return Image;
    }

    public String getTypeEvent() {
        return typeEvent;
    }

    //Set methods
    public void setDate(Date date) {
        this.date = date;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public void setNotify(boolean notify) {
        this.notify = notify;
    }

    public void setImage(int image) {
        Image = image;
    }

    public String getTimeString() {
        SimpleDateFormat localDateFormat = new SimpleDateFormat("HH:mm");
        String str = localDateFormat.format(new Date(time));
        return str;
    }

    public long getTime() {
        return time;
    }

    public void setTypeEvent(String typeEvent) {
        this.typeEvent = typeEvent;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    @Override
    public int compareTo(@NonNull Event o) {
        Date thisDate = new Date(time);
        Date thatDate = new Date(o.getTime());
        return thisDate.compareTo(thatDate);
    }
}
