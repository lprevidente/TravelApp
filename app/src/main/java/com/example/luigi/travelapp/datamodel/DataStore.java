package com.example.luigi.travelapp.datamodel;

import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;

import static com.example.luigi.travelapp.costanti.Constants.KEY_DAY_LIST;
import static com.example.luigi.travelapp.costanti.Constants.KEY_DAY_NUMBER;
import static com.example.luigi.travelapp.costanti.Constants.KEY_EVENT_IMAGE;
import static com.example.luigi.travelapp.costanti.Constants.KEY_EVENT_LIST;
import static com.example.luigi.travelapp.costanti.Constants.KEY_EVENT_NOTE;
import static com.example.luigi.travelapp.costanti.Constants.KEY_EVENT_NOTIFY;
import static com.example.luigi.travelapp.costanti.Constants.KEY_EVENT_TIME;
import static com.example.luigi.travelapp.costanti.Constants.KEY_EVENT_TITLE;
import static com.example.luigi.travelapp.costanti.Constants.KEY_TRIP_END_TIME;
import static com.example.luigi.travelapp.costanti.Constants.KEY_TRIP_LIST;
import static com.example.luigi.travelapp.costanti.Constants.KEY_TRIP_START_TIME;
import static com.example.luigi.travelapp.costanti.Constants.KEY_TRIP_TITLE;

/**
 * Created by Bernardo on 08/05/2017.
 */

public class DataStore {
    private ArrayList<Trip> trips;
    private ArrayList<Day> days;
    private ArrayList<Event> events;

    private static DataStore dataStore = null;
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

    private ValueEventListener listenerTrips;
    private ValueEventListener listenerDays;
    private ValueEventListener listenerEvents;

    private DataStore() {
        trips = new ArrayList<>();
        days = new ArrayList<>();
        events = new ArrayList<>();
    }

    public static DataStore getInstance() {
        if (dataStore == null){
            dataStore = new DataStore();
        }
        return dataStore;
    }

    public interface UpdateListener {
        void tripsUpdated();
        void daysUpdated();
        void eventsUpdated();
    }

    public void beginTripsObs(final UpdateListener notification) {
        DatabaseReference reference = database.getReference(user.getUid()).child(KEY_TRIP_LIST);

        listenerTrips = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                trips.clear();
                for (DataSnapshot item : dataSnapshot.getChildren()) {
                    Trip trip = new Trip();

                    trip.setKey(item.getKey());
                    trip.setTitle(item.child(KEY_TRIP_TITLE).getValue(String.class));
                    trip.setStartTime(item.child(KEY_TRIP_START_TIME).getValue(Long.class));
                    trip.setEndTime(item.child(KEY_TRIP_END_TIME).getValue(Long.class));

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

    public void beginDaysObs(final UpdateListener notification, String tripReference) {
        DatabaseReference reference = database.getReference(user.getUid())
                .child(KEY_DAY_LIST)
                .child(tripReference);

        listenerDays = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                days.clear();
                for (DataSnapshot item : dataSnapshot.getChildren()) {
                    Day day = new Day();

                    day.setKey(item.getKey());
                    day.setNumber(item.child(KEY_DAY_NUMBER).getValue(Integer.class));


                    days.add(day);
                }
                notification.daysUpdated();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };

        reference.addValueEventListener(listenerDays);
    }

    public void beginEventsObs(final UpdateListener notification, String eventReference) {
        DatabaseReference reference = database.getReference(user.getUid())
                .child(KEY_EVENT_LIST)
                .child(eventReference);

        listenerEvents = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                events.clear();
                for (DataSnapshot item : dataSnapshot.getChildren()) {
                    Event event = new Event();
                    event.setKey(item.getKey());
                    event.setTitle(item.child(KEY_EVENT_TITLE).getValue(String.class));
                    Log.i("DATASTORE", "TITOLO EVENTO"+item.child(KEY_EVENT_TITLE).getValue(String.class));
                    event.setNote(item.child(KEY_EVENT_NOTE).getValue(String.class));
                    event.setImage(item.child(KEY_EVENT_IMAGE).getValue(Integer.class));
                    event.setNotify(item.child(KEY_EVENT_NOTIFY).getValue(Boolean.class));
                    event.setTime(item.child(KEY_EVENT_TIME).getValue(Long.class));

                    events.add(event);
                }

                Collections.sort(getEvents());
                notification.eventsUpdated();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };

        reference.addValueEventListener(listenerEvents);
    }


    public void endTripsObs() {
        if (listenerTrips != null)
            FirebaseDatabase.getInstance().getReference(user.getUid()).child(KEY_TRIP_LIST).removeEventListener(listenerTrips);
    }

    public void endDaysObs() {
        if (listenerDays != null)
            FirebaseDatabase.getInstance().getReference(user.getUid()).child(KEY_DAY_LIST).removeEventListener(listenerDays);
    }

    public void endEventsObs() {
        if (listenerEvents != null)
            FirebaseDatabase.getInstance().getReference(user.getUid()).child(KEY_EVENT_LIST).removeEventListener(listenerEvents);
    }


    public void addTrip(Trip trip) {
        // Genero una  nuova chiave per il viaggio
        String tripKey = database.getReference(user.getUid())
                .child(KEY_TRIP_LIST)
                .push()
                .getKey();

        //mi salvo la nuova chiave nel viaggio
        trip.setKey(tripKey);

        // Trasferisco l'intero viaggio
        DatabaseReference reference = database.getReference(user.getUid())
                .child(KEY_TRIP_LIST)
                .child(tripKey);
        reference.setValue(trip);

        int daysNumber = trip.getDaysNumber();
        // genero gli i-giorni
        for (int i = 0; i < daysNumber; i++) {
            addDay(new Day(i + 1), tripKey);
        }
    }

    public void addDay(Day day, String tripReference) {
        String dayKey = database.getReference(user.getUid())
                .child(KEY_DAY_LIST)
                .child(tripReference)
                .push()
                .getKey();
        day.setKey(dayKey);

        DatabaseReference reference = database.getReference(user.getUid())
                .child(KEY_DAY_LIST)
                .child(tripReference)
                .child(dayKey);
        reference.setValue(day);
    }

    public void addEvent(Event event, String dayReference) {
        String eventKey = database.getReference(user.getUid())
                .child(KEY_EVENT_LIST)
                .child(dayReference)
                .push()
                .getKey();
        event.setKey(eventKey);

        DatabaseReference reference = database.getReference(user.getUid())
                .child(KEY_EVENT_LIST)
                .child(dayReference)
                .child(eventKey);
        reference.setValue(event);
    }


    public void updateTrip(Trip trip) {
        int index = tripIndex(trip.getKey());
        Trip tmp = trips.get(index);
        int oldDayNumber = tmp.getDaysNumber();
        int newDayNumber = trip.getDaysNumber();

        // TODO: DAY CHECK
        /*if (newDayNumber < oldDayNumber) {
            for (int i = oldDayNumber; i > newDayNumber; i--) {
                tmp.getDayList().remove(i);
            }
        } else if (newDayNumber > oldDayNumber) {
            for (int i = oldDayNumber + 1; i <= newDayNumber; i++) {
                tmp.addDay(new Day(incrementDay(tmp.getStartDate(), i), i));
            }
        }*/

        DatabaseReference reference = database.getReference(user.getUid())
                .child(KEY_TRIP_LIST)
                .child(trip.getKey());
        reference.setValue(trip);
    }

    public void updateEvent(int tripIndex, int dayIndex, int eventIndex, Event event) {}


    public void deleteTrip(final String key) {
        // implement event remover which is triggered when days are erased
        DatabaseReference dayRef = database.getReference(user.getUid())
                .child(KEY_DAY_LIST).child(key);
        dayRef.addChildEventListener(
                new ChildEventListener() {
                    @Override
                    public void onChildAdded(DataSnapshot dataSnapshot, String s) {

                    }

                    @Override
                    public void onChildChanged(DataSnapshot dataSnapshot, String s) {

                    }

                    @Override
                    public void onChildRemoved(DataSnapshot dataSnapshot) {
                        ArrayList<String> dayKeys = new ArrayList<>();
                        for (DataSnapshot item : dataSnapshot.getChildren()) {
                            //Log.d("FIREBASELOGITEM", String.valueOf(item));
                            dayKeys.add(item.getKey());
                        }

                        for (int i = 0; i < dayKeys.size(); i++) {
                            DatabaseReference reference = database.getReference(user.getUid())
                                    .child(KEY_EVENT_LIST)
                                    .child(dayKeys.get(i));
                            reference.removeValue();
                        }
                    }

                    @Override
                    public void onChildMoved(DataSnapshot dataSnapshot, String s) {

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        //handle databaseError
                    }
                });

        // delete trip
        DatabaseReference reference = database.getReference(user.getUid())
                .child(KEY_TRIP_LIST)
                .child(key);
        reference.removeValue();

        // delete days' list
        reference = database.getReference(user.getUid())
                .child(KEY_DAY_LIST)
                .child(key);
        reference.removeValue();
    }

    public void deleteDay(String tripKey, String dayKey) {
        DatabaseReference reference = database.getReference(user.getUid())
                .child(KEY_DAY_LIST)
                .child(tripKey)
                .child(dayKey);
        reference.removeValue();
    }

    public void deleteEvent(String dayKey, String eventKey) {
        DatabaseReference reference = database.getReference(user.getUid())
                .child(KEY_EVENT_LIST)
                .child(dayKey)
                .child(eventKey);
        reference.removeValue();
    }

    public ArrayList<Trip> getTrips() {
        return trips;
    }

    public ArrayList<Day> getDays() {
        return days;
    }

    public ArrayList<Event> getEvents() {
        return events;
    }

    public int tripIndex(String key) {
        int i = 0;
        while (i < trips.size()) {
            if (trips.get(i).getKey().equals(key))
                return i;
            i++;
        }
        return -1;
    }

    public int dayIndex(String key) {
        int i = 0;
        while (i < days.size()) {
            if (days.get(i).getKey().equals(key))
                return i;
            i++;
        }
        return -1;
    }

    public int eventIndex(String key) {
        int i = 0;
        while (i < events.size()) {
            if (events.get(i).getKey().equals(key))
                return i;
            i++;
        }
        return -1;
    }
}
