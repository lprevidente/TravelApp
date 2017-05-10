package com.example.luigi.travelapp.datamodel;

import android.app.Application;
import android.content.Context;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

/**
 * Created by Bernardo on 08/05/2017.
 */

public class DataStore extends Application {
    private Context context;
    private String path;
    private ArrayList<Trip> trips;

    /**
     * aggiunge un nuovo viaggio all'array contenente la lista dei viaggi
     * @param trip oggetto di tipo Trip da aggiungere da aggiungere
     */
    public void addTrip(Trip trip) {
        trips.add(trip);
    }

    /**
     * aggiunge un nuovo giorno al viaggio desiderato
     * @param tripIndex indice del viaggio
     * @param day oggetto di tipo Day da aggiungere
     */
    public void addDay(int tripIndex, Day day) {
        getDayList(tripIndex).add(day);
    }

    /**
     * aggiunge un evento al giorno desiderato del viaggio desiderato
     * @param tripIndex indice del viaggio
     * @param dayIndex indice del giorno
     * @param event oggetto di tipo Event da aggiungere
     */
    public void addEvent(int tripIndex, int dayIndex, Event event) {
        getEventList(tripIndex, dayIndex).add(event);
    }

    /**
     * aggiorna un viaggio nelll'arrayList dei viaggi
     * @param tripIndex indice del viaggio da modificare
     * @param trip Trip da aggiungere
     */
    public void updateTrip(int tripIndex, Trip trip) {
        Trip tmp = trips.get(tripIndex);
        int oldDayNumber = tmp.getDaysNumber();
        int newDayNumber = trip.getDaysNumber();

        // day check
        if (newDayNumber < oldDayNumber) {
            for (int i = oldDayNumber; i > newDayNumber; i--) {
                tmp.getDayList().remove(i);
            }
        } else if (newDayNumber > oldDayNumber) {
            for (int i = oldDayNumber; i < newDayNumber; i++) {
                tmp.addDay(new Day(i));
            }
        }

        trips.set(tripIndex, tmp);
    }

    /**
     * aggiorna un evento di un giorno di un viaggio
     * @param tripIndex indice del viaggio da modificare
     * @param dayIndex indice del giorno da modificare
     * @param eventIndex indice dell'evento da modificare
     * @param event evento da aggiornare
     */
    public void updateEvent(int tripIndex, int dayIndex, int eventIndex, Event event) {
        getEventList(tripIndex, dayIndex).set(eventIndex, event);
    }

    /**
     * cancella viaggio dall'arrayList dei viaggi
     * @param i indice del viaggio da eliminare
     */
    public void deleteTrip(int i) {
        trips.remove(i);
    }

    /**
     * cancella un evento dal giorno del viaggio desiderato
     * @param tripIndex indice del viaggio
     * @param dayIndex indice del giorno
     * @param eventIndex indice dell'evento
     */
    public void deleteEvent(int tripIndex, int dayIndex, int eventIndex) {
        getEventList(tripIndex, dayIndex).remove(eventIndex);
    }

    /**
     * restituisce la lista dei giorni
     * @param tripIndex indice del viaggio dal quale restituire i giorni
     * @return ArrayList dei giorni
     */
    public ArrayList<Day> getDayList(int tripIndex) {
        return trips.get(tripIndex).getDayList();
    }

    /**
     * restituisce un giorno dalla lista dei giorni
     * @param tripIndex indice del viaggio
     * @param dayIndex indice del giorno
     * @return giorno corrispondente all'indice
     */
    public Day getDay(int tripIndex, int dayIndex) {
        return getDayList(tripIndex).get(dayIndex);
    }

    /**
     * restituisce la lista degli eventi
     * @param tripIndex indice del viaggio
     * @param dayIndex indice del giorno
     * @return lista degli eventi relativi al giorno
     */
    public ArrayList<Event> getEventList(int tripIndex, int dayIndex) {
        return getDay(tripIndex, dayIndex).getEventList();
    }

    /**
     * restituisce la lista dei viaggi
     * @return ArrayList<Trip> contenente i viaggi
     */
    public ArrayList<Trip> getListTrip(){
        return trips;
    }

    /**
     * costruttore del datastore
     */
    public DataStore() {
        path = "yourtrips.dat";
        //context = getApplicationContext();
        trips = new ArrayList<>();

        /*if (fileExists(path)) {
            // the app loads existing data from disk
            deserialize();
        }
        else {
            // the app cannot find the file where trips are stored
            // so it initializes a new one and saves it to disk

            serialize();
        }*/
    }

    /**
     * metodo per serializzare l'arraylist dei viaggi
     */
    public void serialize() {
        FileOutputStream fos;
        try {
            fos = context.openFileOutput(path, Context.MODE_PRIVATE);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(trips);
            oos.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * metodo per deserializzare l'arraylist dei viaggi
     */
    public void deserialize() {
        try {
            FileInputStream fis = context.openFileInput(path);
            ObjectInputStream ois = new ObjectInputStream(fis);
            this.trips = (ArrayList<Trip>) ois.readObject();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * metodo per controllare se un file esiste o meno su disco (necessario per controllare se il file locale
     * che mantiene la lista dei viaggi è già presente o meno su disco)
     * @param path percorso del file
     * @return booleano true o false a seconda se il file ci sia o meno
     */
    public boolean fileExists(String path) {
        File file = context.getFileStreamPath(path);
        if (file == null || !file.exists()) {
            return false;
        }
        return true;
    }
}
