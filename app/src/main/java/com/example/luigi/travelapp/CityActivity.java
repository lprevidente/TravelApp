package com.example.luigi.travelapp;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Intent;
import java.text.DateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import android.os.Bundle;
import android.support.v7.view.menu.MenuView;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import com.example.luigi.travelapp.datamodel.DataStore;
import com.example.luigi.travelapp.datamodel.Trip;

import java.text.ParseException;
import java.util.Date;

import static android.R.string.yes;
import static com.example.luigi.travelapp.costanti.Constants.DATE_PICKER_FROM;
import static com.example.luigi.travelapp.costanti.Constants.DATE_PICKER_TO;
import static com.example.luigi.travelapp.costanti.Constants.EVENT;
import static com.example.luigi.travelapp.costanti.Constants.EVENTNEW;
import static com.example.luigi.travelapp.costanti.Constants.KEY_TRIP;
import static com.example.luigi.travelapp.costanti.Constants.NULLTITLE;
import static com.example.luigi.travelapp.costanti.Constants.SEND_TRIP;

/**
 * Created by Luigi on 08/05/2017.
 */

public class CityActivity extends Activity {
    private DataStore dataStore = DataStore.getInstance();

    private EditText newtripEdit;
    private Button addTripbtn;
    private TextView partenzaTextView;
    private TextView ritornoTextView;
    private int id;

    private int tmpIndex;

    private DatePickerDialog.OnDateSetListener from_dateListener, to_dateListener;

    public void onCreate(Bundle savedInstanceStat){

        super.onCreate(savedInstanceStat);
        setContentView(R.layout.activity_city);

        Bundle extras = getIntent().getExtras();
        tmpIndex = extras.getInt(KEY_TRIP);

        newtripEdit = (EditText)findViewById(R.id.autocompleteEditText);
        addTripbtn = (Button)findViewById(R.id.btnAddCity);

        from_dateListener = new DatePickerDialog.OnDateSetListener(){
            public void onDateSet(DatePicker dp, int year, int month, int day) {
                Calendar cal = new GregorianCalendar(year, month, day);
                setDate2TextView(cal, partenzaTextView);
            }
        };

        to_dateListener = new DatePickerDialog.OnDateSetListener(){
            public void onDateSet(DatePicker dp, int year, int month, int day) {
                Calendar cal = new GregorianCalendar(year, month, day);
                setDate2TextView(cal, ritornoTextView);
            }
        };

        partenzaTextView = (TextView)findViewById(R.id.partenzaTextView);
        ritornoTextView = (TextView)findViewById(R.id.ritornoTextView);

        if (tmpIndex == -1) {
            ritornoTextView.setText(DateFormat.getDateInstance().format(Calendar.getInstance().getTime()));
            partenzaTextView.setText(DateFormat.getDateInstance().format(Calendar.getInstance().getTime()));
        }
        else {
            Trip trip = dataStore.getTrips().get(tmpIndex);
            newtripEdit.setText(trip.getTitle());
            Calendar tmp = Calendar.getInstance();

            tmp.setTime(new Date(trip.getStartTime()));
            setDate2TextView(tmp, partenzaTextView);

            tmp.setTime(new Date(trip.getEndTime()));
            setDate2TextView(tmp, ritornoTextView);
        }

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

        addTripbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DataStore dataStore = DataStore.getInstance();
                String titleTrip = newtripEdit.getText().toString();

                if (!titleTrip.isEmpty() && checkValidDateRange()) {
                    Trip trip = new Trip(titleTrip, String2Date(partenzaTextView.getText().toString()), String2Date(ritornoTextView.getText().toString()));
                    if (tmpIndex == -1)
                        dataStore.addTrip(trip);
                    else {
                        trip.setKey(dataStore.getTrips().get(tmpIndex).getKey());
                        trip.setDaysReference(dataStore.getTrips().get(tmpIndex).getDaysReference());
                        dataStore.updateTrip(trip);
                    }
                    setResult(Activity.RESULT_OK, getIntent());
                    finish();
                } else {
                   newtripEdit.setError(getString(R.string.TitleTripEmpty));
                }
            }
        });
    }
    private class DateDialogFragment extends DialogFragment {
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            String str = (id == DATE_PICKER_TO) ? ritornoTextView.getText().toString() : partenzaTextView.getText().toString();
            Calendar c = Calendar.getInstance();
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
     * setta il contenuto di una textView alla data corrispondente
     * @param calendar informazione sulla data da inserire
     * @param textView textView alla quale cambiare il testo
     */
    public void setDate2TextView(Calendar calendar, TextView textView) {
        DateFormat dateFormat = DateFormat.getDateInstance();
        textView.setText(dateFormat.format(calendar.getTime()));
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

    /**
     * controlla se il range di date sia corretto
     * @return vero se la data di ritorno è successiva alla data di partenza
     */
    public boolean checkValidDateRange() {
        return (String2Date(ritornoTextView.getText().toString()).getTime() - String2Date(partenzaTextView.getText().toString()).getTime()) / (1000 * 60 * 60 * 24) + 1 > 0;
    }
}
