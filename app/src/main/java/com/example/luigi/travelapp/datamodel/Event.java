package com.example.luigi.travelapp.datamodel;

import android.support.annotation.NonNull;

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
    private String type;

    public Event() { }

    public Event(String type, Date date, String title, String note, boolean notify) {
        time = date.getTime();
        this.title = title;
        this.note = note;
        this.notify = notify;
        this.type = type;
    }
    public String getType() {
        return type;
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

    public String getKey() {
        return key;
    }

    public String getTimeString() {
        SimpleDateFormat localDateFormat = new SimpleDateFormat("HH:mm");
        String str = localDateFormat.format(new Date(time));
        return str;
    }

    public long getTime() {
        return time;
    }

    //Set methods

    public void setType(String type) {
        this.type = type;
    }

    public void setDate(long time) {
        this.time = time;
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

    public void setTime(long time) {
        this.time = time;
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
