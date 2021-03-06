package com.example.luigi.travelapp.datamodel;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;

import static com.example.luigi.travelapp.costanti.Constants.KEY_DAY_LIST;
import static com.example.luigi.travelapp.costanti.Constants.KEY_DAY_NUMBER;
import static com.example.luigi.travelapp.costanti.Constants.KEY_EVENT_LIST;
import static com.example.luigi.travelapp.costanti.Constants.KEY_EVENT_NOTE;
import static com.example.luigi.travelapp.costanti.Constants.KEY_EVENT_NOTIFY;
import static com.example.luigi.travelapp.costanti.Constants.KEY_EVENT_TIME;
import static com.example.luigi.travelapp.costanti.Constants.KEY_EVENT_TITLE;
import static com.example.luigi.travelapp.costanti.Constants.KEY_EVENT_TYPE;
import static com.example.luigi.travelapp.costanti.Constants.KEY_TRIP_END_TIME;
import static com.example.luigi.travelapp.costanti.Constants.KEY_TRIP_LIST;
import static com.example.luigi.travelapp.costanti.Constants.KEY_TRIP_NOTES;
import static com.example.luigi.travelapp.costanti.Constants.KEY_TRIP_START_TIME;
import static com.example.luigi.travelapp.costanti.Constants.KEY_TRIP_TITLE;

/**
 * Created by Bernardo on 08/05/2017.
 */

public class DataStore {

    private ArrayList<Trip> trips;
    private ArrayList<Day> days;
    private ArrayList<Event> events;
    private ArrayList<String> supportList;

    private static DataStore dataStore = null;
    private static FirebaseDatabase database = FirebaseDatabase.getInstance();

    private ValueEventListener listenerTrips;
    private ValueEventListener listenerDays;
    private ValueEventListener listenerEvents;

    private DataStore() {
        trips = new ArrayList<>();
        days = new ArrayList<>();
        events = new ArrayList<>();
        supportList = new ArrayList<>();
    }

    public static DataStore getInstance() {
        if (dataStore == null) {
            dataStore = new DataStore();
            database.setPersistenceEnabled(true);
        }
        return dataStore;
    }

    public interface UpdateListener {
        void tripsUpdated();
        void daysUpdated();
        void eventsUpdated();
    }

    public void attachTripsListener(final UpdateListener notification) {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
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
                    trip.setNotes(item.child(KEY_TRIP_NOTES).getValue(String.class));

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

    public void attachDaysListener(final UpdateListener notification, String tripReference) {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
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
            public void onCancelled(DatabaseError databaseError) {}
        };

        reference.addValueEventListener(listenerDays);
    }

    public void attachEventsListener(final UpdateListener notification, String eventReference) {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
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
                    event.setType(item.child(KEY_EVENT_TYPE).getValue(String.class));
                    event.setTitle(item.child(KEY_EVENT_TITLE).getValue(String.class));
                    event.setNote(item.child(KEY_EVENT_NOTE).getValue(String.class));
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


    public void removeTripsListener() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (listenerTrips != null)
            FirebaseDatabase.getInstance().getReference(user.getUid()).child(KEY_TRIP_LIST).removeEventListener(listenerTrips);
    }

    public void removeDaysListener() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (listenerDays != null)
            FirebaseDatabase.getInstance().getReference(user.getUid()).child(KEY_DAY_LIST).removeEventListener(listenerDays);
    }

    public void removeEventsListener() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (listenerEvents != null)
            FirebaseDatabase.getInstance().getReference(user.getUid()).child(KEY_EVENT_LIST).removeEventListener(listenerEvents);
    }

    /**
     * Metodo per aggiungere un viaggio al database remoto
     * @param trip viaggio da aggiungere
     */
    public void addTrip(Trip trip) {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
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

        // genero i relativi giorni
        for (int i = 0; i < daysNumber; i++) {
            addDay(new Day(i + 1), tripKey);
        }
    }

    /**
     * Metodo per aggiungere un giorno al database remoto
     * @param day oggetto di tipo Day da aggiungere
     * @param tripReference chiave del viaggio associato al giorno
     */
    public void addDay(Day day, String tripReference) {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
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

    /**
     * Metodo per aggiungere un evento al database remoto
     * @param event oggetto di tipo Event da aggiungere
     * @param dayReference chiave del giorno associato all'evento
     */
    public void addEvent(Event event, String dayReference) {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
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

    /**
     * Metodo per aggiornare un giorno
     * @param trip viaggio aggiornato
     */
    public void updateTrip(Trip trip) {
        int index = tripIndex(trip.getKey());
        final Trip tmp = trips.get(index);
        final int oldDayNumber = tmp.getDaysNumber();
        final int newDayNumber = trip.getDaysNumber();

        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        if (newDayNumber < oldDayNumber) {
            // sovrascrivo il vecchio viaggio col nuovo viaggio
            DatabaseReference reference2 = database.getReference(user.getUid())
                    .child(KEY_TRIP_LIST)
                    .child(tmp.getKey());
            trip.setKey(tmp.getKey());
            reference2.setValue(trip);

            // dopodiché cancello i giorni con una query
            DatabaseReference reference = database.getReference(user.getUid())
                    .child(KEY_DAY_LIST)
                    .child(tmp.getKey());
            Query query = reference.orderByChild("number").startAt(newDayNumber+1);

            query.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {

                        dataSnapshot1.getRef().removeValue();

                        // cancello tutti gli eventi associati ai giorni
                        DatabaseReference reference= database.getReference(user.getUid())
                                .child(KEY_EVENT_LIST)
                                .child(dataSnapshot1.getKey());
                        reference.removeValue();
                    }
                }
                @Override
                public void onCancelled(DatabaseError databaseError) { }
            });
        }
        else if (newDayNumber >= oldDayNumber) {
            for (int i = oldDayNumber + 1; i <= newDayNumber; i++) {
                Day day = new Day(i);
                addDay(day, trip.getKey());
            }

            DatabaseReference reference = database.getReference(user.getUid())
                    .child(KEY_TRIP_LIST)
                    .child(trip.getKey());
            reference.setValue(trip);
        }

    }

    /**
     * Metodo per aggiornare un evento
     * @param event evento aggiornato
     * @param dayReference chiave del giorno a cui fare riferimento
     */
    public void updateEvent(Event event, String dayReference) {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference reference = database.getReference(user.getUid())
                .child(KEY_EVENT_LIST)
                .child(dayReference)
                .child(event.getKey());
        reference.setValue(event);
    }

    /**
     * Metodo per cancellare un viaggio
     * @param key chiave del viaggio
     */
    public void deleteTrip(final String key) {
        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        // implemento un event remover che viene triggerato quando un giorno è cancellato
        // lo faccio usando un childEventListener, che ascolta quando tutto il nodo dei giorni
        // viene cancellato. Memorizzo le chiavi dei giorni cancellati in un array di supporto
        // e lo utilizzo per cancellare tutti gli eventi, in quanto essi saranno
        // figli dei nodi aventi come chiave proprio quella ottenuta dal listener
        DatabaseReference dayRef = database.getReference(user.getUid())
                .child(KEY_DAY_LIST);
        dayRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {}

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {}

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                supportList.clear();
                for (DataSnapshot item : dataSnapshot.getChildren()) {
                    String path = item.getKey();
                    supportList.add(path);
                }

                // delete events
                for (int i = 0; i < supportList.size(); i++) {
                    DatabaseReference eventRef = database.getReference(user.getUid())
                            .child(KEY_EVENT_LIST)
                            .child(supportList.get(i));
                    eventRef.removeValue();
                }
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {}

            @Override
            public void onCancelled(DatabaseError databaseError) {
                //handle databaseError
            }
        });

        // cancello il viaggio
        DatabaseReference reference = database.getReference(user.getUid())
                .child(KEY_TRIP_LIST)
                .child(key);
        reference.removeValue();

        // cancello tutti i giorni
        reference = database.getReference(user.getUid())
                .child(KEY_DAY_LIST)
                .child(key);
        reference.removeValue();

    }

    /**
     * Metodo per cancellare un evento
     * @param dayKey chiave del giorno
     * @param eventKey chiave dell'evento
     */
    public void deleteEvent(String dayKey, String eventKey) {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
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

    /**
     * Metodo per risalire all'indice del viaggio nell'arraylist partendo dalla sua chiave
     * @param key chiave del viaggio
     * @return Int
     */
    public int tripIndex(String key) {
        int i = 0;
        while (i < trips.size()) {
            if (trips.get(i).getKey().equals(key))
                return i;
            i++;
        }
        return -1;
    }

    /**
     * Metodo per risalire all'indice del giorno nell'arraylist partendo dalla sua chiave
     * @param key chiave del giorno
     * @return Int
     */
    public int dayIndex(String key) {
        int i = 0;
        while (i < days.size()) {
            if (days.get(i).getKey().equals(key))
                return i;
            i++;
        }
        return -1;
    }
}
