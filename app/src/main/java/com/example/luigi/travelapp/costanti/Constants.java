package com.example.luigi.travelapp.costanti;

import com.example.luigi.travelapp.R;

/**
 * Created by Bernardo on 10/05/2017.
 */

public class Constants {

    public static final String FIRSTLAUNCH = "firstlaunch";
    public static final String EVENT = "event";

    // costanti per i datePicker
    public static final int DATE_PICKER_TO = 0;
    public static final int DATE_PICKER_FROM = 1;

    // costanti per le chiavi
    public static final String KEY_TRIP_LIST = "trips";
    public static final String KEY_DAY_LIST = "days";
    public static final String KEY_EVENT_LIST = "events";

    public static final String KEY_TRIP_TITLE = "title";
    public static final String KEY_TRIP_START_TIME = "startTime";
    public static final String KEY_TRIP_END_TIME = "endTime";
    public static final String KEY_TRIP_NOTES = "notes";

    public static final String KEY_DAY_NUMBER = "number";

    public static final String KEY_EVENT_TIME = "time";
    public static final String KEY_EVENT_TITLE = "title";
    public static final String KEY_EVENT_NOTE = "note";
    public static final String KEY_EVENT_NOTIFY = "notify";
    public static final String KEY_EVENT_TYPE = "type";

    public static final String KEY_TRIP = "keytrip";
    public static final String KEY_DAY = "keyday";
    public static final String KEY_EVENT = "keyevent";

    public static final int Num_Events = 3;

    // costanti per i tipi dell'evento
    public static final int EVENT_TYPES_NUMBER = 8;
    public static final CharSequence[] textTypes = new CharSequence [] {
            "Aereo",
            "Auto",
            "Treno",
            "Museo",
            "Ristorante",
            "Coffee Bar",
            "Mare",
            "Spa & Relax"
    };

    public static final Integer[] integers = new Integer[] {
            R.drawable.ic_action__flight,
            R.drawable.ic_action_car,
            R.drawable.ic_action_train,
            R.drawable.ic_action_museum,
            R.drawable.ic_action_restaurant,
            R.drawable.ic_action_coffee_bar,
            R.drawable.ic_action_beach,
            R.drawable.ic_action_spa
    };

    public static final Integer[] backgrounds = new Integer[] {
            R.drawable.airplane,
            R.drawable.road,
            R.drawable.train,
            R.drawable.museum,
            R.drawable.restaurant,
            R.drawable.coffee,
            R.drawable.sea,
            R.drawable.relax
    };

    public static final int NUM_TIMES = 4;
    public static final String[] timeNotifications = new String[] {
            "10 minuti",
            "30 minuti",
            "1 ora",
            "1 giorno"
    };

    public static final long[] anticipi = new long[] {
            600000,
            1800000,
            3600000,
            86400000
    };

    // costanti per le notifiche
    public static final String TITLE = "title";
    public static final String ICON = "icon";
    public static final String TEXT = "text";
 }
