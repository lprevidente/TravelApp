package com.example.luigi.travelapp;

import android.app.Activity;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.TimePicker;

import com.example.luigi.travelapp.datamodel.DataStore;
import com.example.luigi.travelapp.datamodel.Event;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import static com.example.luigi.travelapp.R.id.radioGroup;
import static com.example.luigi.travelapp.costanti.Constants.DAY_INDEX;
import static com.example.luigi.travelapp.costanti.Constants.EVENT;
import static com.example.luigi.travelapp.costanti.Constants.EVENT_INDEX;
import static com.example.luigi.travelapp.costanti.Constants.NULLTITLE;
import static com.example.luigi.travelapp.costanti.Constants.TRIP_INDEX;


/**
 * Created by Luigi on 09/05/2017.
 */

public class EventActivity extends Activity {

    private DataStore dataStore = DataStore.getInstance();
    private int tripIndex;
    private int dayIndex;
    private EditText titleEventTextView;
    private EditText noteEditview;
    private CheckBox notifyCheckBox;
    private TextView TimePickerTextView;

    private ImageView imageView;

    private Intent intent;

    // todo: bisogna aggiustare i radio button e allinearli con le immagini
    private RadioButton radioFlight;
    private RadioButton radioResturant;
    private RadioButton radioPlaces;

    private int resImage;
    private boolean notify=false;

    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event);

        intent= getIntent();
        Bundle extras = getIntent().getExtras();
        tripIndex = extras.getInt(TRIP_INDEX);
        dayIndex = extras.getInt(DAY_INDEX);

        Toolbar toolbarEvent = (Toolbar) findViewById(R.id.toolbarEvent);

        toolbarEvent.setTitle(R.string.NewEvent);
        toolbarEvent.inflateMenu(R.menu.menu_event);

        titleEventTextView = (EditText) findViewById(R.id.titleEventEdit);
        noteEditview = (EditText) findViewById(R.id.notesText);
        notifyCheckBox = (CheckBox) findViewById(R.id.checkBoxNotifica);

        radioFlight= (RadioButton) findViewById(R.id.radioFlight);
        radioPlaces= (RadioButton) findViewById(R.id.radioPlaces);
        radioResturant= (RadioButton) findViewById(R.id.radioRestaurant);


        // devo settarli in questo modo altrimenti non si vedono per via delle varie dimensioni
        imageView = (ImageView) findViewById(R.id.imageVisit);
        imageView.setImageResource(R.drawable.ic_action_name_place);

        imageView = (ImageView) findViewById(R.id.imageRestaurant);
        imageView.setImageResource(R.drawable.ic_action_name_rest7aurant);

        imageView = (ImageView) findViewById(R.id.imageFlight);
        imageView.setImageResource(R.drawable.ic_action_name_flight);


        TimePickerTextView = (TextView) findViewById(R.id.oraTextView);

        if(extras.getString(EVENT)!=("new")){

            Event event=(Event) extras.getSerializable(EVENT);
            titleEventTextView.setText(event.getTitle());
            noteEditview.setText(event.getNote());
            notifyCheckBox.setChecked(event.getNotify());
            TimePickerTextView.setText(event.getTimeString());
            int resImageOld=event.getImage();

            if(resImageOld==R.drawable.ic_action_name_place) {
                radioPlaces.setChecked(true);
                resImage = R.drawable.ic_action_name_place;
            }
            else if (resImageOld==R.drawable.ic_action_name_flight) {
                radioFlight.setChecked(true);
                resImage = R.drawable.ic_action_name_flight;
            }
            else if (resImageOld==R.drawable.ic_action_name_rest7aurant) {
                radioResturant.setChecked(true);
                resImage = R.drawable.ic_action_name_rest7aurant;
            }
        }
        else{
        setCurrentTime();}


        TimePickerTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment newFragment = new TimePickerFragment();
                newFragment.show(getFragmentManager(), "TimePicker");
            }

        });

        toolbarEvent.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.item_done:
                        if (!titleEventTextView.getText().toString().equals(NULLTITLE)) {
                            // get the event's day date and set the new hour and minute
                            Calendar oldcal = Calendar.getInstance();
                            Calendar newcal = Calendar.getInstance();
                            oldcal.setTime(dataStore.getDay(tripIndex, dayIndex).getDate());
                            newcal.setTime(setString2DateTime(oldcal.getTime(), TimePickerTextView.getText().toString()));

                            Event event = new Event(newcal.getTime(),
                                    titleEventTextView.getText().toString(), noteEditview.getText().toString(), notify, resImage);

                            intent.putExtra(EVENT_INDEX, event);
                            setResult(Activity.RESULT_OK, intent);
                            finish();
                            return true;
                        }
                }
                return false;
            }
        });

        notifyCheckBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (notifyCheckBox.isChecked())
                    notify=true;
                else notify=false;
            }
        });

    }
    public void onRadioButtonClicked(View view) {

        boolean checked = ((RadioButton) view).isChecked();

        switch(view.getId()) {
            case R.id.radioFlight:
                if (checked)
                    resImage= R.drawable.ic_action_name_flight;
                    break;
            case R.id.radioPlaces:
                if (checked)
                    resImage= R.drawable.ic_action_name_place;
                    break;
            case R.id.radioRestaurant:
                if(checked)
                    resImage=R.drawable.ic_action_name_rest7aurant;
                    break;
        }
    }

    private class TimePickerFragment extends DialogFragment implements TimePickerDialog.OnTimeSetListener{

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
            TimePickerTextView.setText(String.format("%02d", Integer.parseInt(String.valueOf(hourOfDay))) + ":" + String.format("%02d", Integer.parseInt(String.valueOf(minute))));
        }
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
        TimePickerTextView.setText(String.valueOf(hour)+":" + String.valueOf(minute));
    }
}
