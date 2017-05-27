package com.example.luigi.travelapp;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.luigi.travelapp.datamodel.DataStore;
import com.example.luigi.travelapp.datamodel.Trip;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.places.AutocompleteFilter;
import com.google.android.gms.location.places.AutocompletePrediction;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.maps.android.SphericalUtil;

import java.text.DateFormat;
import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import static com.example.luigi.travelapp.costanti.Constants.DATE_PICKER_FROM;
import static com.example.luigi.travelapp.costanti.Constants.DATE_PICKER_TO;
import static com.example.luigi.travelapp.costanti.Constants.FIRSTLAUNCH;
import static com.example.luigi.travelapp.costanti.Constants.KEY_TRIP;

/**
 * Created by Luigi on 08/05/2017.
 */

public class CityActivity extends FragmentActivity implements GoogleApiClient.OnConnectionFailedListener {

    private DataStore dataStore = DataStore.getInstance();

    private Button addTripbtn;
    private TextView partenzaTextView;
    private TextView ritornoTextView;
    private EditText noteText;
    private int id;

    private int tmpIndex;

    private GoogleApiClient mGoogleApiClient;
    private PlaceAutocompleteAdapter mAdapter;

    private AutoCompleteTextView mAutocompleteView;

    private CharSequence title;

    LatLng center = new LatLng(41.893056, 12.482778); // geographic coordinates of Rome
    LatLng southwest = SphericalUtil.computeOffset(center, 2 * Math.sqrt(2.0), 225);
    LatLng northeast = SphericalUtil.computeOffset(center, 2 * Math.sqrt(2.0), 45);

    private final LatLngBounds BOUNDS_GREATER_ROME = new LatLngBounds(southwest, northeast);

    private DatePickerDialog.OnDateSetListener from_dateListener, to_dateListener;

    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_city);

        Bundle extras = getIntent().getExtras();
        tmpIndex = extras.getInt(KEY_TRIP);

        addTripbtn = (Button)findViewById(R.id.btnAddCity);

        noteText = (EditText)findViewById(R.id.noteText);

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, 0, this)
                .addApi(Places.GEO_DATA_API).build();
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

        mAutocompleteView = (AutoCompleteTextView) findViewById(R.id.autocomplete_places);

        // Ho bisogno di questa condizione per sapere se sto creando un nuovo viaggio
        // oppure ne sto modificando uno, in questo ultimo caso devo andare a settare
        // tutti i valori precedenti
        if (tmpIndex == -1) {
            ritornoTextView.setText(DateFormat.getDateInstance().format(Calendar.getInstance().getTime()));
            partenzaTextView.setText(DateFormat.getDateInstance().format(Calendar.getInstance().getTime()));
        }
        else {
            Trip trip = dataStore.getTrips().get(tmpIndex);
            mAutocompleteView.setText(trip.getTitle());
            noteText.setText(trip.getNotes());

            Calendar tmp = Calendar.getInstance();

            tmp.setTime(new Date(trip.getStartTime()));
            setDate2TextView(tmp, partenzaTextView);

            tmp.setTime(new Date(trip.getEndTime()));
            setDate2TextView(tmp, ritornoTextView);
        }

        /**
         * Creo un filtro in modo che la ricerca dei posti mi dia solo delle città
         * e non anche i punti di interessa
         */
        AutocompleteFilter filter =  new AutocompleteFilter.Builder()
                .setTypeFilter(AutocompleteFilter.TYPE_FILTER_CITIES)
                .build();

        // setto l'adapter all'EditText autocompletante
        mAdapter = new PlaceAutocompleteAdapter(getApplicationContext(), mGoogleApiClient, BOUNDS_GREATER_ROME, filter);
        mAutocompleteView.setAdapter(mAdapter);

        // Register a listener that receives callbacks when a suggestion has been selected
        mAutocompleteView.setOnItemClickListener(mAutocompleteClickListener);

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
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

                if (user == null) {
                    Intent intent = new Intent(CityActivity.this, SignUpActivity.class);
                    intent.putExtra(FIRSTLAUNCH, false);
                    startActivity(intent);
                }
                else {
                    if (title != null && checkValidDateRange()) {

                        Trip trip = new Trip((String)title,
                                String2Date(partenzaTextView.getText().toString()),
                                String2Date(ritornoTextView.getText().toString()),
                                noteText.getText().toString());

                        if (tmpIndex == -1)
                            dataStore.addTrip(trip);
                        else {
                            trip.setKey(dataStore.getTrips().get(tmpIndex).getKey());
                            dataStore.updateTrip(trip);
                        }

                        setResult(Activity.RESULT_OK, getIntent());
                        finish();
                    } else {
                        mAutocompleteView.setError(getString(R.string.TitleTripEmpty));
                    }
                }
            }
        });

    }

    private AdapterView.OnItemClickListener mAutocompleteClickListener
            = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            AutocompletePrediction item = mAdapter.getItem(position);
            title = item.getPrimaryText(null);
        }
    };

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Toast.makeText(this,
                "Impossibile connettersi alle Google API: errore  " + connectionResult.getErrorCode(),
                Toast.LENGTH_SHORT).show();
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
