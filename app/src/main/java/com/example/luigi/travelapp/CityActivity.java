package com.example.luigi.travelapp;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Intent;
import android.icu.text.DateFormat;
import android.icu.util.Calendar;
import android.icu.util.GregorianCalendar;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import com.example.luigi.travelapp.datamodel.Trip;

import java.text.ParseException;
import java.util.Date;

/**
 * Created by Luigi on 08/05/2017.
 */

public class CityActivity extends Activity {

    private EditText newtripEdit;
    private Button addTripbnt;
    private Trip trip;
    public static String mTrip;
    private TextView partenzaTextView;
    private TextView ritornoTextView;
    private int id;

    public static final String TAG = "CityActivity";


    final int DATE_PICKER_TO = 0;
    final int DATE_PICKER_FROM = 1;

    DatePickerDialog.OnDateSetListener from_dateListener, to_dateListener;

    public void onCreate(Bundle savedInstanceStat){
        super.onCreate(savedInstanceStat);
        setContentView(R.layout.activity_city);

        newtripEdit=(EditText) findViewById(R.id.autocompleteEditText);
        addTripbnt= (Button) findViewById(R.id.btnAddCity);

        from_dateListener = new DatePickerDialog.OnDateSetListener(){
            public void onDateSet(DatePicker arg0, int year, int month, int day) {
                Calendar cal = new GregorianCalendar(year, month, day);
                setCurrentDate(cal);
            }
        };

        to_dateListener = new DatePickerDialog.OnDateSetListener(){
            public void onDateSet(DatePicker arg0, int year, int month, int day) {
                Calendar cal = new GregorianCalendar(year, month, day);
                setEndingDate(cal);
            }
        };

        partenzaTextView = (TextView)findViewById(R.id.partenzaTextView);
        partenzaTextView.setText(DateFormat.getDateInstance().format(Calendar.getInstance().getTime()));

        ritornoTextView = (TextView)findViewById(R.id.ritornoTextView);
        ritornoTextView.setText(DateFormat.getDateInstance().format(Calendar.getInstance().getTime()));

        partenzaTextView.setOnClickListener(new View.OnClickListener() {
            public void onClick (View v) {
                id = DATE_PICKER_FROM;
                DateDialogFragment fragment = new DateDialogFragment();
                fragment.show(getFragmentManager(), "date");
            }
        });

        ritornoTextView.setOnClickListener(new View.OnClickListener() {
            public void onClick (View v) {
                id = DATE_PICKER_TO;
                DateDialogFragment fragment = new DateDialogFragment();
                fragment.show(getFragmentManager(), "date");
            }
        });

        addTripbnt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    String titleTrip= newtripEdit.getText().toString();
                    trip = new Trip( titleTrip, String2Date(partenzaTextView.getText().toString()), String2Date(ritornoTextView.getText().toString()));
                Log.i(TAG, "Il numero dei giorni del viaggio sono "+trip.getDaysNumber());
                    Intent intent= getIntent();
                    intent.putExtra(mTrip, trip);
                    setResult(Activity.RESULT_OK, intent);
                    // In this way i close the current activity
                    finish();
            }
        });
    }
    public class DateDialogFragment extends DialogFragment {
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            String str = (id == DATE_PICKER_TO) ? ritornoTextView.getText().toString() : partenzaTextView.getText().toString();
            final Calendar c = Calendar.getInstance();
            c.setTime(String2Date(str));
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);

            switch (id) {
                case DATE_PICKER_FROM:
                    return new DatePickerDialog(getActivity(), from_dateListener, year, month, day);
                case DATE_PICKER_TO:
                    return new DatePickerDialog(getActivity(), to_dateListener, year, month, day);
            }
            return null;
        }
    }

    /**
     * sets FROM date to textview object
     * @param calendar FROM calendar
     */

    private void setCurrentDate(Calendar calendar) {
        DateFormat dateFormat = DateFormat.getDateInstance();
        partenzaTextView.setText(dateFormat.format(calendar.getTime()));
    }

    /**
     * sets TO date to textview object
     * @param calendar TO calendar
     */
    private void setEndingDate(Calendar calendar) {
        DateFormat dateFormat = DateFormat.getDateInstance();
        ritornoTextView.setText(dateFormat.format(calendar.getTime()));
    }

    /**
     * Convert String object to Date object
     * @param str string to be converted to Date, usually obtained from TextView.getText().toString()
     * @return Date object with the same date as the input string
     */
    public Date String2Date(String str) {
        Date date = null;
        DateFormat sdf = DateFormat.getDateInstance();
        try {
            date = sdf.parse(str);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }


}
