package com.example.luigi.travelapp;

import android.app.Activity;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.TimePickerDialog;

import java.sql.Time;
import java.util.Calendar;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;

/**
 * Created by Luigi on 09/05/2017.
 */

public class EventActivity extends Activity {

    private EditText titleEventTextView;
    private EditText noteEditview;
    private CheckBox notifyCheckBox;
    private TextView TimePickeTextView;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event);

        titleEventTextView=(EditText) findViewById(R.id.titleEventEdit);
        noteEditview=(EditText) findViewById(R.id.notesText);
        notifyCheckBox=(CheckBox) findViewById(R.id.checkBoxNotifica);
        TimePickeTextView=(TextView)findViewById(R.id.oraTextView);

        TimePickeTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                DialogFragment newFragment = new TimePickerFragment();
                newFragment.show(getFragmentManager(),"TimePicker");
            }

        });
    }

    private class TimePickerFragment extends DialogFragment implements TimePickerDialog.OnTimeSetListener{

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState){
            //Use the current time as the default values for the time picker
            final Calendar c = Calendar.getInstance();
            int hour = c.get(Calendar.HOUR_OF_DAY);
            int minute = c.get(Calendar.MINUTE);

            //Create and return a new instance of TimePickerDialog
            return new TimePickerDialog(getActivity(),this, hour, minute,
                    DateFormat.is24HourFormat(getActivity()));
        }

        //onTimeSet() callback method
        public void onTimeSet(TimePicker view, int hourOfDay, int minute){

            TimePickeTextView.setText(String.valueOf(hourOfDay)+":" + String.valueOf(minute));
        }
    }
}
