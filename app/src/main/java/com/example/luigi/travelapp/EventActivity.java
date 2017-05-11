package com.example.luigi.travelapp;

import android.app.Activity;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.luigi.travelapp.datamodel.Event;

import java.text.DateFormat;
import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;

import static android.os.Build.VERSION_CODES.M;
import static com.example.luigi.travelapp.costanti.Constants.EVENT_INDEX;


/**
 * Created by Luigi on 09/05/2017.
 */

public class EventActivity extends Activity {

    private EditText titleEventTextView;
    private EditText noteEditview;
    private CheckBox notifyCheckBox;
    private TextView TimePickerTextView;private ImageButton imageBtnDone;
   // private Button button;
    private ImageView imageView;
/*
    private RadioButton radioFlight;
    private RadioButton radioResturant;
    private RadioButton radioPlaces;
    */

    private RadioGroup radioGroup;

    private int resImage;
    private TextView TimePickerTextView;
    private ImageButton imageBtnDone;

    private boolean notify = false;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event);

        Toolbar toolbarEvent = (Toolbar) findViewById(R.id.toolbarEvent);

        toolbarEvent.setTitle(R.string.NewEvent);
        toolbarEvent.inflateMenu(R.menu.menu_event);

        titleEventTextView = (EditText) findViewById(R.id.titleEventEdit);
        noteEditview = (EditText) findViewById(R.id.notesText);
        notifyCheckBox = (CheckBox) findViewById(R.id.checkBoxNotifica);

        // devo settarli in questo modo altrimenti non si vedono per via delle varie dimensioni
        imageView = (ImageView) findViewById(R.id.imageVisit);
        imageView.setImageResource(R.drawable.ic_action_name_place);

        imageView = (ImageView) findViewById(R.id.imageRestaurant);
        imageView.setImageResource(R.drawable.ic_action_name_rest7aurant);

        imageView = (ImageView) findViewById(R.id.imageFlight);
        imageView.setImageResource(R.drawable.ic_action_name_flight);


        TimePickerTextView = (TextView) findViewById(R.id.oraTextView);
        setCurrentTime();
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
                            Event event = new Event(String2Date((String)TimePickerTextView.getText()),
                                    titleEventTextView.getText().toString(), noteEditview.getText().toString(), notify);
                            Intent intent = getIntent();
                            intent.putExtra(EVENT_INDEX, event);
                            setResult(Activity.RESULT_OK, intent);
                            finish();
                            return true;
                        }
                }
                return false;
            }
        });

    }
    public void onRadioButtonClicked(View view) {
        // Is the button now checked?
        boolean checked = ((RadioButton) view).isChecked();

        // Check which radio button was clicked
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

    public void setCurrentTime() {
        Calendar c = Calendar.getInstance();
        int hour = c.get(Calendar.HOUR_OF_DAY);
        int minute = c.get(Calendar.MINUTE);
        TimePickerTextView.setText(String.valueOf(hour)+":" + String.valueOf(minute));
    }
}
