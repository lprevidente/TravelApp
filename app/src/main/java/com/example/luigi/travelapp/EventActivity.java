package com.example.luigi.travelapp;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;

import com.example.luigi.travelapp.datamodel.DataStore;
import com.example.luigi.travelapp.datamodel.Day;
import com.example.luigi.travelapp.datamodel.Event;
import com.example.luigi.travelapp.datamodel.EventTypes;
import com.example.luigi.travelapp.datamodel.Trip;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static com.example.luigi.travelapp.costanti.Constants.EVENT_TYPES_NUMBER;
import static com.example.luigi.travelapp.costanti.Constants.ICON;
import static com.example.luigi.travelapp.costanti.Constants.KEY_DAY;
import static com.example.luigi.travelapp.costanti.Constants.KEY_EVENT;
import static com.example.luigi.travelapp.costanti.Constants.KEY_TRIP;
import static com.example.luigi.travelapp.costanti.Constants.TEXT;
import static com.example.luigi.travelapp.costanti.Constants.TITLE;
import static com.example.luigi.travelapp.costanti.Constants.integers;
import static com.example.luigi.travelapp.costanti.Constants.textTypes;


/**
 * Created by Luigi on 09/05/2017.
 */

public class EventActivity extends Activity {

    private DataStore dataStore = DataStore.getInstance();
    private EditText titleEventTextView;
    private EditText noteEditview;
    private Switch notifySwitch;
    private static TextView TimePickerTextView;
    private FloatingActionButton btnTypesEvent;
    private ImageButton btnDone;

    private ImageView imgEvent;
    private ImageView imgClock;
    private ImageView imgNotification;

    private int resImage;
    private CharSequence typeEvent;

    private boolean notify=false;

    private String dayKey;
    private String tripKey;
    private int tmpIndex;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event);

        Bundle extras = getIntent().getExtras();
        tripKey = extras.getString(KEY_TRIP);
        dayKey = extras.getString(KEY_DAY);
        tmpIndex = extras.getInt(KEY_EVENT);


        titleEventTextView = (EditText) findViewById(R.id.titleEventEdit);
        noteEditview = (EditText) findViewById(R.id.notesText);
        notifySwitch = (Switch) findViewById(R.id.switchNotifica);
        btnTypesEvent = (FloatingActionButton) findViewById(R.id.floatingActionButton);
        TimePickerTextView = (TextView) findViewById(R.id.oraTextView);
        btnDone = (ImageButton) findViewById(R.id.imageButtonDone);
        btnDone.setImageResource(R.drawable.ic_action_name_done);

        imgEvent = (ImageView) findViewById(R.id.imageViewEvent);
        imgEvent.setImageResource(R.drawable.agenda);

        imgClock = (ImageView) findViewById(R.id.imageViewClock);
        imgClock.setImageResource(R.drawable.ic_action_name_clock);

        imgNotification = (ImageView) findViewById(R.id.imageViewNotification);
        imgNotification.setImageResource(R.drawable.ic_action_name_notifactions);


        /**
         * se tmpIndex =-1 ossia è un nuovo viaggio allora
         * imposto il tempo corrente per il TimePicker
         * e come tipo evento di default Museo
         *
         */
        if (tmpIndex == -1) {
            setCurrentTime();
            resImage = integers[4];
            typeEvent = textTypes[4];
        }
        /**
         * In caso contrario, significa che sto modificando un viaggio quindi
         * devo settare il vecchio titolo, orario e la notifica
         */
        else {
            Event event = dataStore.getEvents().get(tmpIndex);
            titleEventTextView.setText(event.getTitle());
            noteEditview.setText(event.getNote());
            notifySwitch.setChecked(event.getNotify());
            TimePickerTextView.setText(event.getTimeString());

            int index = getResourceIndex(event.getType().toString());

            if (index != -1) {
                resImage = integers[index];
                typeEvent = textTypes[index];


            } else {
                // non dovrebbe mai entrarci
                resImage = 0;
            }
        }
        typeEvent = textTypes[4];
        btnTypesEvent.setImageResource(resImage);

        /**
         * Creo un Alert Dialog per avere
         * più scelte sui vari tipi di eventi
         */

        final AlertDialogAdapter alertDialogAdapter = new AlertDialogAdapter(this);

        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Scegli il tipo di Evento");

        builder.setAdapter(alertDialogAdapter, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Qui inserisco il metodo per prendere il testo e l'immagine dell'item
                // selezionato per poi sostituirli nel Floating Action Button
                List<EventTypes> list = alertDialogAdapter.getList();
                resImage = list.get(which).getImage();
                btnTypesEvent.setImageResource(resImage);
                typeEvent = textTypes[which];

            }
        });

        // Al click del Floating Action Button mostro l'Alert Dialog con i vari tipi di Eventi
        btnTypesEvent.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               builder.show();
           }
       });

        TimePickerTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment newFragment = new TimePickerFragment();
                newFragment.show(getFragmentManager(), "TimePicker");
            }

        });

        /**
         * Gestisco i click sull'ImageButton
         * Done: mi creo un nuovo evento e lo mando a DayListActivity
         */
       btnDone.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {

               if (!titleEventTextView.getText().toString().isEmpty()) {

                   Calendar oldcal = Calendar.getInstance();

                   int index = dataStore.tripIndex(tripKey);

                   if (index != -1) {
                       Trip tmpTrip = dataStore.getTrips().get(index);
                       Day day = dataStore.getDays().get(dataStore.dayIndex(dayKey));

                       oldcal.setTime(new Date(tmpTrip.getStartTime()));
                       oldcal.add(Calendar.DATE, day.getNumber());

                       // memorizzo il time fino al giorno dell'evento
                       // calcolo l'offset del tempo e sottraggo un offset di 23 ore (in ms) dovuto a non so cosa
                       Long basetm = oldcal.getTime().getTime();
                       Long offsettm = setString2DateTime(oldcal.getTime(),
                               TimePickerTextView.getText().toString()).getTime() - 82800000;


                       Event event = new Event(typeEvent, basetm + offsettm,
                               titleEventTextView.getText().toString(),
                               noteEditview.getText().toString(), notify);
                       if(tmpIndex != -1)
                           event.setKey(dataStore.getEvents().get(tmpIndex).getKey());

                       boolean ispossible = true;
                                /*int i = 0;
                                ArrayList<Event> events = dataStore.getEvents();
                                while (ispossible && i < events.size()){
                                    if(events.get(i).getTime() < (event.getTime() + IntervalEvent) &&
                                            !events.get(i).getKey().equals(event.getKey()))
                                        ispossible = false;
                                    i++;
                                }*/

                       if (ispossible) {
                           if (tmpIndex == -1)
                               dataStore.addEvent(event, day.getKey());
                           else {
                               //event.setKey(dataStore.getEvents().get(tmpIndex).getKey());
                               dataStore.updateEvent(event, dayKey);
                           }

                           if (notify)
                               notification(event.getTime(), resImage, event.getTitle() + "(" + event.getTimeString() + ")", event.getNote());
                           finish();
                       } else {
                           //Toast.makeText(getApplicationContext(), "Hai già un evento nei prossimi 10 minuti", Toast.LENGTH_SHORT).show();
                       }
                   }

               }
               else {
                   titleEventTextView.setError(getString(R.string.TitleEventEmpty));
               }

           }
       });



        /**
         * Vedo se l'user ha selezionato o meno la notifica
         */
        notifySwitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (notifySwitch.isChecked())
                    notify=true;
                else notify=false;
            }
        });

    }

    public static class TimePickerFragment extends DialogFragment implements TimePickerDialog.OnTimeSetListener{

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState){
            //Use the current time as the default values for the time picker
            Calendar c = Calendar.getInstance();
            int hour = c.get(Calendar.HOUR_OF_DAY);
            int minute = c.get(Calendar.MINUTE);

            //Create and return a new instance of TimePickerDialog
            return new TimePickerDialog(getActivity(),this, hour, minute, android.text.format.DateFormat.is24HourFormat(getActivity()));
        }

        public void onTimeSet(TimePicker view, int hourOfDay, int minute){
            TimePickerTextView.setText(String.format("%02d", Integer.parseInt(String.valueOf(hourOfDay))) + ":"
                    + String.format("%02d", Integer.parseInt(String.valueOf(minute))));
        }
    }

    public void notification(long timevar, int icon, String title, String text) {
        Intent alarmIntent = new Intent(this, Receiver.class);
        alarmIntent.putExtra(ICON, icon);
        alarmIntent.putExtra(TITLE, title);
        alarmIntent.putExtra(TEXT, text);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, alarmIntent, 0);

        AlarmManager manager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date(timevar));

        manager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),
                AlarmManager.INTERVAL_DAY, pendingIntent);
    }

    public Date setString2DateTime(Date date, String str) {
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
        try {
            date = sdf.parse(str);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }

    public void setCurrentTime() {
        Calendar c = Calendar.getInstance();
        int hour = c.get(Calendar.HOUR_OF_DAY);
        int minute = c.get(Calendar.MINUTE);
        TimePickerTextView.setText(String.format("%02d", Integer.parseInt(String.valueOf(hour))) + ":"
                + String.format("%02d", Integer.parseInt(String.valueOf(minute))));
    }

    private int getResourceIndex(String type) {
        int i = 0;
        while (i < EVENT_TYPES_NUMBER) {
            if (type.equals(textTypes[i]))
                return i;
            i++;
        }
        return -1;
    };
}
