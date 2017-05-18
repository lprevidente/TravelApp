package com.example.luigi.travelapp.datamodel;

import android.support.annotation.NonNull;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Luigi on 08/05/2017.
 */

public class Event implements Comparable<Event>, Serializable{
    private Date date;
    private String title;
    private String note;
    private boolean notify;
    private int Image;
    private String typeEvent;

    /**
     * costruttore dell'oggetto di tipo Event
     * @param date oggetto Date che manterrà l'informazione sull'orario dell'evento
     *             TODO: usare Time per sprecare meno informazioni?
     * @param title titolo dell'evento
     * @param note descrizione aggiuntiva per l'evento (che verrà eventualmente mostrata in notifica)
     * @param notify notificare o meno l'evento
     */
    public Event(Date date, String title, String note, boolean notify, int Image, String string) {
        this.date = date;
        this.title = title;
        this.note = note;
        this.notify = notify;
        this.Image=Image;
        typeEvent=string;
    }

    // Get methods
    public Date getDate() {
        return date;
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
        String time = localDateFormat.format(date);
        return time;
    }

    public void setTypeEvent(String typeEvent) {
        this.typeEvent = typeEvent;
    }

    @Override
    public int compareTo(@NonNull Event o) {
        return getDate().compareTo(o.getDate());
    }
}
