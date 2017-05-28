package com.example.luigi.travelapp;

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
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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
import static com.example.luigi.travelapp.costanti.Constants.ID;
import static com.example.luigi.travelapp.costanti.Constants.KEY_DAY;
import static com.example.luigi.travelapp.costanti.Constants.KEY_EVENT;
import static com.example.luigi.travelapp.costanti.Constants.KEY_TRIP;
import static com.example.luigi.travelapp.costanti.Constants.NUM_TIMES;
import static com.example.luigi.travelapp.costanti.Constants.TEXT;
import static com.example.luigi.travelapp.costanti.Constants.TITLE;
import static com.example.luigi.travelapp.costanti.Constants.anticipi;
import static com.example.luigi.travelapp.costanti.Constants.integers;
import static com.example.luigi.travelapp.costanti.Constants.textTypes;
import static com.example.luigi.travelapp.costanti.Constants.timeNotifications;


/**
 * Created by Luigi on 09/05/2017.
 */

public class EventActivity extends AppCompatActivity {

    private DataStore dataStore = DataStore.getInstance();
    private EditText titleEventTextView;
    private EditText noteEditview;
    private Switch notifySwitch;
    private static TextView TimePickerTextView;
    private FloatingActionButton btnTypesEvent;
    private Button btnDone;
    private TextView timeNotification;
    private ImageView imgEvent;
    private ImageView imgClock;
    private ImageView imgNotification;

    private int resImage;
    private CharSequence typeEvent;

    private boolean notify=false;

    private String dayKey;
    private String tripKey;
    private int tmpIndex;

    private static int id = 0;

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
        btnDone = (Button) findViewById(R.id.btnDone);
        timeNotification = (TextView) findViewById(R.id.textViewTimeNotifications);
        timeNotification.setText(timeNotifications[0]);

        imgEvent = (ImageView) findViewById(R.id.imageViewEvent);
        imgEvent.setImageResource(R.drawable.agenda);

        imgClock = (ImageView) findViewById(R.id.imageViewClock);
        imgClock.setImageResource(R.drawable.ic_action_name_clock);

        imgNotification = (ImageView) findViewById(R.id.imageViewNotification);
        imgNotification.setImageResource(R.drawable.ic_action_name_notifactions);


        // se tmpIndex =-1 ossia è un nuovo viaggio allora imposto il tempo corrente per il TimePicker
        // e come tipo evento di default Museo
        if (tmpIndex == -1) {
            setCurrentTime();
            resImage = integers[3];
            typeEvent = textTypes[3];
        } else {
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

        btnTypesEvent.setImageResource(resImage);

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

        final AlertAdapterNotifications alert2 = new AlertAdapterNotifications(this);

        final AlertDialog.Builder builder2 = new AlertDialog.Builder(this);
        builder2.setTitle("Quanto prima vuoi essere avvisato?");

        builder2.setAdapter(alert2, new DialogInterface.OnClickListener(){

            public void onClick(DialogInterface dialog, int which){
                timeNotification.setText(timeNotifications[which]);
            }

        });

        timeNotification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    builder2.show();
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

                       if (tmpIndex == -1)
                           dataStore.addEvent(event, day.getKey());
                       else {
                           event.setKey(dataStore.getEvents().get(tmpIndex).getKey());
                           dataStore.updateEvent(event, dayKey);
                       }

                       if (notify) {
                           notification(event, resImage, id);
                           id++;
                       }
                       finish();
                   }
               }
               else {
                   titleEventTextView.setError(getString(R.string.TitleEventEmpty));
               }
           }
       });

        notifySwitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                notify = notifySwitch.isChecked();
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

    public void notification(Event item, int icon, int id) {
        // cerco qual è l'anticipo della mia notifica
        int index = getTimeIndex(timeNotification.getText().toString());
        long anticipo;
        if (index != -1) {
            anticipo = anticipi[index];
        } else {
            anticipo = 0;
        }

        long time = item.getTime() - anticipo;
        String title = item.getTitle() + " (" + item.getTimeString() + ")";

        String text = "Questo evento si verificherà tra " + timeNotifications[index] + "! " + item.getNote();

        Intent alarmIntent = new Intent(this, Receiver.class);
        alarmIntent.putExtra(ICON, icon);
        alarmIntent.putExtra(TITLE, title);
        alarmIntent.putExtra(TEXT, text);
        alarmIntent.putExtra(ID, id);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, alarmIntent, 0);

        AlarmManager manager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date(time));

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

    private int getTimeIndex(String type) {
        int i = 0;
        while (i < NUM_TIMES) {
            if (type.equals(timeNotifications[i]))
                return i;
            i++;
        }
        return -1;
    };
}
