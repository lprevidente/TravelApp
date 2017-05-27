package com.example.luigi.travelapp;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Intent;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.text.Html;
import android.text.Spanned;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;

import com.example.luigi.travelapp.datamodel.DataStore;
import com.example.luigi.travelapp.datamodel.Trip;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.location.places.AutocompletePrediction;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.PlaceBuffer;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.maps.android.SphericalUtil;

import java.text.DateFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import static android.R.attr.id;
import static com.example.luigi.travelapp.R.id.partenzaTextView;
import static com.example.luigi.travelapp.R.id.ritornoTextView;
import static com.example.luigi.travelapp.costanti.Constants.DATE_PICKER_FROM;
import static com.example.luigi.travelapp.costanti.Constants.DATE_PICKER_TO;
import static com.example.luigi.travelapp.costanti.Constants.FIRSTLAUNCH;
import static com.example.luigi.travelapp.costanti.Constants.KEY_TRIP;

/**
 * Created by Luigi on 08/05/2017.
 */

public class CityActivity extends FragmentActivity implements GoogleApiClient.OnConnectionFailedListener {

    private static final String TAG = "City_activity" ;
    private DataStore dataStore = DataStore.getInstance();

    private Button addTripbtn;
    private TextView partenzaTextView;
    private TextView ritornoTextView;
    private int id;

    private int tmpIndex;

    protected GoogleApiClient mGoogleApiClient;
    private PlaceAutocompleteAdapter mAdapter;

    private AutoCompleteTextView mAutocompleteView;

    private CharSequence title;

   // todo: sostituirlo con le coordinate date dalla posizione del telefono
    LatLng center = new LatLng(41.893056, 12.482778); // geographic coordinates of Rome
    float radius = 2;
    // coordinates of southwest and northeast
    LatLng southwest = SphericalUtil.computeOffset(center, radius * Math.sqrt(2.0), 225);
    LatLng northeast = SphericalUtil.computeOffset(center, radius * Math.sqrt(2.0), 45);

    private final LatLngBounds BOUNDS_GREATER_ROME = new LatLngBounds(southwest, northeast);

    private DatePickerDialog.OnDateSetListener from_dateListener, to_dateListener;

    public void onCreate(Bundle savedInstanceStat) {

        super.onCreate(savedInstanceStat);
        setContentView(R.layout.activity_city);

        Bundle extras = getIntent().getExtras();
        tmpIndex = extras.getInt(KEY_TRIP);

        addTripbtn = (Button)findViewById(R.id.btnAddCity);

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this,0 , this)
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


         //Retrieve the AutoCompleteTextView that will display Place suggestions.
          mAutocompleteView = (AutoCompleteTextView) findViewById(R.id.autocomplete_places);

        /**
         * Ho bisogno di questa condizione per sapere se sto creando un nuovo viaggio
         * oppure ne sto modificando uno, in questo ultimo caso devo andare a settare
         * tutti i valori precedenti
         */
        if (tmpIndex == -1) {
            ritornoTextView.setText(DateFormat.getDateInstance().format(Calendar.getInstance().getTime()));
            partenzaTextView.setText(DateFormat.getDateInstance().format(Calendar.getInstance().getTime()));
        }
        else {
            Trip trip = dataStore.getTrips().get(tmpIndex);
            mAutocompleteView.setText(trip.getTitle());
            Calendar tmp = Calendar.getInstance();

            tmp.setTime(new Date(trip.getStartTime()));
            setDate2TextView(tmp, partenzaTextView);

            tmp.setTime(new Date(trip.getEndTime()));
            setDate2TextView(tmp, ritornoTextView);
        }

        // Set up the adapter that will retrieve suggestions from the Places Geo Data API that cover
        // the entire world.
        mAdapter = new PlaceAutocompleteAdapter(getApplicationContext(), mGoogleApiClient, BOUNDS_GREATER_ROME, null);
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
                    // String titleTrip = mAutocompleteView.getText().toString();

                    if ( checkValidDateRange()) {

                        Trip trip = new Trip((String) title, String2Date(partenzaTextView.getText().toString()),
                                String2Date(ritornoTextView.getText().toString()));

                        if (tmpIndex != -1)
                            trip.setKey(dataStore.getTrips().get(tmpIndex).getKey());

                        ArrayList<Trip> trips = dataStore.getTrips();
                        boolean ispossible = true;
                        int i = 0;
                        while (ispossible && i<trips.size()) {
                            // todo: modificare meglio le restrizioni
                            /**
                             * Casi che possono accadere '<' = inizio Viaggio '>' = fine Viaggio
                             *            <------------------->
                             *
                             *         <---->     <---->    <---->
                             */
                            if (trip.getEndTime() < trips.get(i).getStartTime() && !(trips.get(i).getKey().equals(trip.getKey())))
                                ispossible = false;
                            i++;
                        }

                        if (ispossible) {
                            if (tmpIndex == -1)
                                dataStore.addTrip(trip);
                            else {
                                dataStore.updateTrip(trip);
                            }

                            setResult(Activity.RESULT_OK, getIntent());
                            finish();
                        } else {
                            Toast.makeText(getApplicationContext(), "Esiste gia un viaggio in questa data", Toast.LENGTH_SHORT).show();
                        }
                    }  else {mAutocompleteView.setError(getString(R.string.TitleTripEmpty));}
                }
            }
        });

    }

    /**
     * Listener that handles selections from suggestions from the AutoCompleteTextView that
     * displays Place suggestions.
     * Gets the place id of the selected item and issues a request to the Places Geo Data API
     * to retrieve more details about the place.
     *
     * @see com.google.android.gms.location.places.GeoDataApi#getPlaceById(com.google.android.gms.common.api.GoogleApiClient,
     * String...)
     */
    private AdapterView.OnItemClickListener mAutocompleteClickListener
            = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            /*
             Retrieve the place ID of the selected item from the Adapter.
             The adapter stores each Place suggestion in a AutocompletePrediction from which we
             read the place ID and title.
              */
            final AutocompletePrediction item = mAdapter.getItem(position);
            final String placeId = item.getPlaceId();
            final CharSequence primaryText = item.getPrimaryText(null);

            Log.i(TAG, "Autocomplete item selected: " + primaryText);

            /*
             Issue a request to the Places Geo Data API to retrieve a Place object with additional
             details about the place.
              */
            PendingResult<PlaceBuffer> placeResult = Places.GeoDataApi
                    .getPlaceById(mGoogleApiClient, placeId);
            placeResult.setResultCallback(mUpdatePlaceDetailsCallback);

            Toast.makeText(getApplicationContext(), "Clicked: " + primaryText,
                    Toast.LENGTH_SHORT).show();
            Log.i(TAG, "Called getPlaceById to get Place details for " + placeId);
        }
    };

    /**
     * Callback for results from a Places Geo Data API query that shows the first place result in
     * the details view on screen.
     */
    private ResultCallback<PlaceBuffer> mUpdatePlaceDetailsCallback
            = new ResultCallback<PlaceBuffer>() {
        @Override
        public void onResult(PlaceBuffer places) {
            if (!places.getStatus().isSuccess()) {
                // Request did not complete successfully
                Log.e(TAG, "Place query did not complete. Error: " + places.getStatus().toString());
                places.release();
                return;
            }
            // Get the Place object from the buffer.
            final Place place = places.get(0);

            Log.i(TAG, "Place details received: " + place.getName());

            places.release();
        }
    };


    /**
     * Called when the Activity could not connect to Google Play services and the auto manager
     * could resolve the error automatically.
     * In this case the API is not available and notify the user.
     *
     * @param connectionResult can be inspected to determine the cause of the failure
     */
    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

        Toast.makeText(this,
                "Could not connect to Google API Client: Error " + connectionResult.getErrorCode(),
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
