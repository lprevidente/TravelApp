package com.example.luigi.travelapp.datamodel;

import android.widget.CheckBox;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by Luigi on 08/05/2017.
 */

public class Event implements Serializable {
    private Date date;
    private String title;
    private String note;
    private boolean notify;

    /**
     * costruttore dell'oggetto di tipo Event
     * @param date oggetto Date che manterrà l'informazione sull'orario dell'evento
     *             TODO: usare Time per sprecare meno informazioni?
     * @param title titolo dell'evento
     * @param note descrizione aggiuntiva per l'evento (che verrà eventualmente mostrata in notifica)
     * @param notify notificare o meno l'evento
     */
    public Event(Date date, String title, String note, boolean notify) {
        this.date = date;
        this.title = title;
        this.note = note;
        this.notify = notify;
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
}
