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
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
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
    private CheckBox notifyCheckBox;
    private TextView TimePickerTextView;
    private ImageButton typeEvent;
    private TextView textViewtypeEvent;

    private String stringtypeEvent;
    private int resImage;

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

        Toolbar toolbarEvent = (Toolbar) findViewById(R.id.toolbarEvent);
        toolbarEvent.setTitle(R.string.NewEvent);
        toolbarEvent.inflateMenu(R.menu.menu_event);

        titleEventTextView = (EditText) findViewById(R.id.titleEventEdit);
        noteEditview = (EditText) findViewById(R.id.notesText);
        notifyCheckBox = (CheckBox) findViewById(R.id.checkBoxNotifica);

        TimePickerTextView = (TextView) findViewById(R.id.oraTextView);

        typeEvent = (ImageButton) findViewById(R.id.imageButton_typeEvent);

        textViewtypeEvent = (TextView) findViewById(R.id.textView_typeEvent);

        if (tmpIndex == -1) {
            setCurrentTime();
            resImage = integers[2];
            stringtypeEvent = textTypes[2].toString();
        }
        else {
            Event event = dataStore.getEvents().get(tmpIndex);
            titleEventTextView.setText(event.getTitle());
            noteEditview.setText(event.getNote());
            notifyCheckBox.setChecked(event.getNotify());
            TimePickerTextView.setText(event.getTimeString());
            int index = getResourceIndex(event.getType());
            if (index != -1) {
                resImage = integers[index];
                stringtypeEvent = textTypes[index].toString();
            } else {
                // non dovrebbe mai entrarci
                resImage = 0;
                stringtypeEvent = "Error?";
            }
        }

        typeEvent.setImageResource(resImage);
        textViewtypeEvent.setText(stringtypeEvent);

         // creo un Alert Dialog per avere più scelte sui vari tipi di eventi
        final AlertDialogAdapter alertDialogAdapter = new AlertDialogAdapter(this);

        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Scegli il tipo di Evento");

        builder.setAdapter(alertDialogAdapter, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Qui inserisco il metodo per prendere il testo e l'immagine dell'item
                // selezionato per poi sostituirli nel ImageButton and nella Text view sotto
                List<EventTypes> list = alertDialogAdapter.getList();
                resImage = list.get(which).getImage();
                typeEvent.setImageResource(resImage);
                stringtypeEvent = list.get(which).getText().toString();
                textViewtypeEvent.setText(stringtypeEvent);
            }
        });

        // Al click del button mostro l'Alert Dialog con i vari tipi di Eventi
        typeEvent.setOnClickListener(new View.OnClickListener() {
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
         * Gestisco i click sulla toolbar
         * Done: mi creo un nuovo evento e lo mando a DayListActivity
         */
        toolbarEvent.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.item_done:
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

                                Event event = new Event(stringtypeEvent, basetm + offsettm,
                                    titleEventTextView.getText().toString(),
                                    noteEditview.getText().toString(), notify);

                                if (tmpIndex == -1)
                                    dataStore.addEvent(event, day.getKey());
                                else {
                                    event.setKey(dataStore.getEvents().get(tmpIndex).getKey());
                                    dataStore.updateEvent(event, dayKey);
                                }

                                if (notify)
                                    notification(event.getTime(),
                                            resImage,
                                            event.getTitle() + " (" + event.getTimeString() + ")",
                                            event.getNote());
                                finish();
                            }
                            return true;
                        }
                        else {
                            titleEventTextView.setError(getString(R.string.TitleEventEmpty));
                        }
                }
                return false;
            }
        });

        notifyCheckBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                notify = notifyCheckBox.isChecked();
            }
        });

    }

    private class TimePickerFragment extends DialogFragment implements TimePickerDialog.OnTimeSetListener{

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState){
            // Uso il tempo corrente per stabilire l'orario iniziale del time picker
            Calendar c = Calendar.getInstance();
            int hour = c.get(Calendar.HOUR_OF_DAY);
            int minute = c.get(Calendar.MINUTE);

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
