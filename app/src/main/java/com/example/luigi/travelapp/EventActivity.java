package com.example.luigi.travelapp;

import android.app.Activity;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.icu.text.DateFormat;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.TimePicker;

import com.example.luigi.travelapp.datamodel.Event;

import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;


/**
 * Created by Luigi on 09/05/2017.
 */

public class EventActivity extends Activity {

    private EditText titleEventTextView;
    private EditText noteEditview;
    private CheckBox notifyCheckBox;
    private TextView TimePickerTextView;
    private ImageButton imageBtnDone;

    public static String EVENT="";
    private Event event;
    private boolean notify=false;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event);

        Toolbar toolbarEvent= (Toolbar) findViewById(R.id.toolbarEvent);

        toolbarEvent.setTitle(R.string.NewEvent);
        toolbarEvent.setNavigationIcon(R.drawable.ic_action_name_done);

        toolbarEvent.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                event= new Event(String2Date((String)TimePickerTextView.getText()),
                        titleEventTextView.getText().toString(), noteEditview.getText().toString(),notify);
                Intent intent= getIntent();
                intent.putExtra(EVENT,event);
                setResult(Activity.RESULT_OK,intent);
                finish();
            }
        });

        //imageBtnDone=(ImageButton) findViewById(R.id.imageBtnDone);
        titleEventTextView=(EditText) findViewById(R.id.titleEventEdit);
        noteEditview=(EditText) findViewById(R.id.notesText);
        notifyCheckBox=(CheckBox) findViewById(R.id.checkBoxNotifica);
        TimePickerTextView=(TextView)findViewById(R.id.oraTextView);

        TimePickerTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment newFragment = new TimePickerFragment();
                newFragment.show(getFragmentManager(),"TimePicker");
            }

        });

    }

/*
    public boolean onCreateOptionsMenu(Menu menu){
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    } */
    private class TimePickerFragment extends DialogFragment implements TimePickerDialog.OnTimeSetListener{

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState){
            //Use the current time as the default values for the time picker
            final Calendar c = Calendar.getInstance();
            int hour = c.get(Calendar.HOUR_OF_DAY);
            int minute = c.get(Calendar.MINUTE);

            //Create and return a new instance of TimePickerDialog
            return new TimePickerDialog(getActivity(),this, hour, minute, android.text.format.DateFormat.is24HourFormat(getActivity()));
        }


        public void onTimeSet(TimePicker view, int hourOfDay, int minute){
            TimePickerTextView.setText(String.valueOf(hourOfDay)+":" + String.valueOf(minute));
        }
    }

    public Date String2Date(String str) {
        Date date = null;
        DateFormat sdf = DateFormat.getInstance();
        try {
            date = sdf.parse(str);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }
}
