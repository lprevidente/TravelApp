package com.example.luigi.travelapp.datamodel;

import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;

import static com.example.luigi.travelapp.costanti.Constants.KEY_TRIP_DAY_LIST;
import static com.example.luigi.travelapp.costanti.Constants.KEY_TRIP_END_DATE;
import static com.example.luigi.travelapp.costanti.Constants.KEY_TRIP_START_DATE;
import static com.example.luigi.travelapp.costanti.Constants.KEY_TRIP_TITLE;

/**
 * Created by Bernardo on 08/05/2017.
 */

public class DataStore {
    private ArrayList<Trip> trips;
    private static DataStore dataStore = null;
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

    private ValueEventListener listenerTrips;

    private DataStore() {
        trips = new ArrayList<> ();
    }

    public static DataStore getInstance() {
        if (dataStore == null){
            dataStore = new DataStore();
        }
        return dataStore;
    }

    public interface UpdateListener {
        void tripsUpdated();
    }

    public void beginTripsObs(final UpdateListener notification) {
        DatabaseReference reference = database.getReference(user.getUid());

        listenerTrips = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                trips.clear();
                for (DataSnapshot item : dataSnapshot.getChildren()) {
                    Trip trip = new Trip();

                    // gli indici dell'arraylist sono le nostre chiavi per identificare i viaggi
                    trip.setTitle(item.child(Integer.toString(trips.size())).child(KEY_TRIP_TITLE).getValue(String.class));
                    trip.setStartDate(item.child(Integer.toString(trips.size())).child(KEY_TRIP_START_DATE).getValue(Date.class));
                    trip.setEndDate(item.child(Integer.toString(trips.size())).child(KEY_TRIP_END_DATE).getValue(Date.class));

                    GenericTypeIndicator<ArrayList<Day>> typeIndicator = new GenericTypeIndicator<ArrayList<Day>>() {};
                    trip.setDays(item.child(Integer.toString(trips.size())).child(KEY_TRIP_DAY_LIST).getValue(typeIndicator));

                    trips.add(trip);
                }
                notification.tripsUpdated();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };

        reference.addValueEventListener(listenerTrips);
    }

    public void endTripsObs() {
        if (listenerTrips != null)
            FirebaseDatabase.getInstance().getReference(user.getUid()).removeEventListener(listenerTrips);
    }

    /**
     * aggiunge un nuovo viaggio all'array contenente la lista dei viaggi
     * @param trip oggetto di tipo Trip da aggiungere da aggiungere
     */
    public void addTrip(Trip trip) {
        DatabaseReference reference = database.getReference(user.getUid()).child(Integer.toString(trips.size()));
        reference.setValue(trip);
    }

    /**
     * aggiunge un nuovo giorno al viaggio desiderato
     * @param tripIndex indice del viaggio
     * @param day oggetto di tipo Day da aggiungere
     */
    public void addDay(int tripIndex, Day day) {
        getDayList(tripIndex).add(day);
        /*DatabaseReference reference =  database.getReference(user.getUid())
                .child(Integer.toString(tripIndex));
        reference.setValue(day);*/

    }

    /**
     * aggiunge un evento al giorno desiderato del viaggio desiderato
     * @param tripIndex indice del viaggio
     * @param dayIndex indice del giorno
     * @param event oggetto di tipo Event da aggiungere
     */
    public void addEvent(int tripIndex, int dayIndex, Event event) {
        getEventList(tripIndex, dayIndex).add(event);
        Collections.sort(getEventList(tripIndex, dayIndex));
        /*DatabaseReference reference =  database.getReference(user.getUid())
                .child(Integer.toString(tripIndex)).child(Integer.toString(dayIndex));
        reference.setValue(event);*/
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
                tmp.getDays().remove(i);
            }
        } else if (newDayNumber > oldDayNumber) {
            for (int i = oldDayNumber + 1; i <= newDayNumber; i++) {
                tmp.addDay(new Day(i));
            }
        }

        trips.set(tripIndex, tmp);
        /*DatabaseReference reference =  database.getReference(user.getUid());
                //.child(Integer.toString(tripIndex));
        reference.setValue(tmp);*/

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
         return trips.get(tripIndex).getDays();
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
        return getDay(tripIndex, dayIndex).getEvents();
    }

    /**
     * restituisce la lista dei viaggi
     * @return ArrayList<Trip> contenente i viaggi
     */
    public ArrayList<Trip> getTrips() {
        return trips;
    }
}
