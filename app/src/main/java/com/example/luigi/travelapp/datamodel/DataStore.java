package com.example.luigi.travelapp.datamodel;

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

public class DataStore {
    private Context context;
    private String path;
    private static ArrayList<Trip> trips;

    public void addTrip(Trip trip) {
        trips.add(trip);
    }

    public void addDay(int tripIndex, Day day) {
        getDayList(tripIndex).add(day);
    }

    public void addEvent(int tripIndex, int dayIndex, Event event) {
        getEventList(tripIndex, dayIndex).add(event);
    }

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

    public void updateEvent(int tripIndex, int dayIndex, int eventIndex, Event event) {
        getEventList(tripIndex, dayIndex).set(eventIndex, event);
    }

    public void deleteTrip(Trip trip) {
        for (int i = trips.size(); i > 0; i--) {
            if (trips.get(i)
                    .getTitleTrip()
                    .equals(trip.getTitleTrip())) {
                trips.remove(i);
            }
        }
    }

    public void deleteEvent(int tripIndex, int dayIndex, Event event) {
        for (int i = getEventList(tripIndex, dayIndex).size(); i > 0; i--) {
            if (getEventList(tripIndex, dayIndex)
                    .get(i)
                    .getTitle()
                    .equals(event.getTitle())) {
                getEventList(tripIndex, dayIndex).remove(i);
            }
        }
    }

    public ArrayList<Day> getDayList(int tripIndex) {
        return trips.get(tripIndex).getDayList();
    }

    public Day getDay(int tripIndex, int dayIndex) {
        return getDayList(tripIndex).get(dayIndex);
    }

    public ArrayList<Event> getEventList(int tripIndex, int dayIndex) {
        return getDay(tripIndex, dayIndex).getEventList();
    }

    public DataStore(Context context) {
        path = "yourtrips.dat";
        this.context = context;
        if (fileExists()) {
            // the app loads existing data from disk
            deserialize();
        }
        else {
            // the app cannot find the file where trips are stored
            // so it initializes a new one and saves it to disk
            this.trips = new ArrayList<>();
            serialize();
        }
    }

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

    public boolean fileExists() {
        File file = context.getFileStreamPath(path);
        if (file == null || !file.exists()) {
            return false;
        }
        return true;
    }
}
