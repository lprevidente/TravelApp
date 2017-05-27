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
    private CharSequence type;

    public Event() { }

    public Event(CharSequence type, Long time, String title, String note, boolean notify) {
        this.time = time;
        this.title = title;
        this.note = note;
        this.notify = notify;
        this.type = type;
    }
    public CharSequence getType() {
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

    public void setType(String type) {
        this.type = type;
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

    /**
     * La classe implementa comparable in quanto mi serve ordinare gli eventi per data d'inizio
     */
    @Override
    public int compareTo(@NonNull Event o) {
        Date thisDate = new Date(time);
        Date thatDate = new Date(o.getTime());
        return thisDate.compareTo(thatDate);
    }
}
