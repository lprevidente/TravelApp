package com.example.luigi.travelapp;

import android.content.Context;
import android.support.v4.content.res.ResourcesCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.luigi.travelapp.datamodel.Day;
import com.example.luigi.travelapp.datamodel.Event;
import com.example.luigi.travelapp.datamodel.Trip;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import static com.example.luigi.travelapp.costanti.Constants.KEY_EVENT_LIST;
import static com.example.luigi.travelapp.costanti.Constants.KEY_TRIP_LIST;
import static com.example.luigi.travelapp.costanti.Constants.Num_Events;


/**
 * Created by Luigi on 08/05/2017.
 */

public class DayListAdapter extends BaseAdapter {

    private Context context;
    private List<Day> days = Collections.emptyList();
    private String tripKey;

    private FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

    public DayListAdapter(Context context, String tripKey) {
        this.context = context;
        this.tripKey=tripKey;
    }

    public void update(List<Day> newList) {
        days = newList;
        notifyDataSetChanged();
    }

    public View getView(final int position, View view, ViewGroup parent) {
        if (view == null)
            view = LayoutInflater.from(context).inflate(R.layout.day_list_adapter, parent, false);

        final TextView numDaytxt = (TextView)view.findViewById(R.id.dayTextView);
        final TextView dateDay = (TextView) view.findViewById(R.id.dateDay);
        final TextView dateinWeek = (TextView) view.findViewById(R.id.dayInWeek);

        final TextView Event1txt= (TextView)view.findViewById(R.id.titleEvent1);
        final TextView Event1date = (TextView)view.findViewById(R.id.timeEvent1);

        final TextView Event2txt = (TextView)view.findViewById(R.id.titleEvent2);
        final TextView Event2date= (TextView)view.findViewById(R.id.timeEvent2);

        final TextView Event3txt = (TextView)view.findViewById(R.id.titleEvent3);
        final TextView Event3date = (TextView)view.findViewById(R.id.timeEvent3);

        final LinearLayout layout1 = (LinearLayout) view.findViewById(R.id.LayoutEvent1);
        final LinearLayout layout2 = (LinearLayout) view.findViewById(R.id.LayoutEvent2);
        final LinearLayout layout3 = (LinearLayout) view.findViewById(R.id.LayoutEvent3);

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference(user.getUid())
                .child(KEY_EVENT_LIST)
                .child(days.get(position).getKey());

        // Mostro soltanto i primi 3 eventi di quel determinato giorno
        final String[] titlesEvent = new String[3];
        final String[] timesEvent = new String[3];

        final View finalView = view;
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                //Se il numero degli eventi è maggiore di 3
                //allora il max lo pongo a 3
                long i = dataSnapshot.getChildrenCount();
                if (i > Num_Events)
                    i = Num_Events;

                int j = 0;
                for (DataSnapshot snapshot: dataSnapshot.getChildren()){
                    // prendo gli eventi uno ad uno e ne ricavo il titolo
                    if(j < i) {
                        Event post = snapshot.getValue(Event.class);
                        titlesEvent[j] = post.getTitle();
                        timesEvent[j] = post.getTimeString();
                        j++;
                    }
                }

                // dopo il ciclo setto i titoli e le date una ad una;
                // nel caso non ci siano setto lo sfondo trasparente
                if (titlesEvent[0] == null && timesEvent[0] == null) {
                    layout1.setBackground(ResourcesCompat.getDrawable(finalView.getResources(), R.color.colorTransparet, null));
                    Event1txt.getLayoutParams().height = 0;
                    Event1date.getLayoutParams().height = 0;
                }
                else {
                    layout1.setBackground(ResourcesCompat.getDrawable(finalView.getResources(), R.color.cast_intro_overlay_button_background_color, null));
                    Event1txt.setText(titlesEvent[0]);
                    Event1date.setText(timesEvent[0]);
                }

                if (titlesEvent[1] == null && timesEvent[1] == null) {
                    layout2.setBackground(ResourcesCompat.getDrawable(finalView.getResources(), R.color.colorTransparet, null));
                    Event2txt.getLayoutParams().height = 0;
                    Event2date.getLayoutParams().height = 0;
                }
                else {
                    layout2.setBackground(ResourcesCompat.getDrawable(finalView.getResources(), R.color.colorFucsia, null));
                    Event2txt.setText(titlesEvent[1]);
                    Event2date.setText(timesEvent[1]);
                }

                if (titlesEvent[2] == null && timesEvent[2] == null) {
                    layout3.setBackground(ResourcesCompat.getDrawable(finalView.getResources(), R.color.colorTransparet, null));
                    Event3txt.getLayoutParams().height = 0;
                    Event3date.getLayoutParams().height = 0;
                }
                else {
                    layout3.setBackground(ResourcesCompat.getDrawable(finalView.getResources(), R.color.colorOrange, null));
                    Event3txt.setText(titlesEvent[2]);
                    Event3date.setText(timesEvent[2]);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) { }
        });

        DatabaseReference reference2 = FirebaseDatabase.getInstance().getReference(user.getUid())
                .child(KEY_TRIP_LIST)
                .child(tripKey);

        reference2.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Trip trip = dataSnapshot.getValue(Trip.class);

                numDaytxt.setText(getCalendarDayNumber(trip, position));
                dateDay.setText(getCalendarMonthString(trip, position));
                dateinWeek.setText(getCalendarDayName(trip, position));
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        return view;
    }

    private String getCalendarDayNumber(Trip trip, int position) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date(trip.getStartTime()));
        calendar.add(Calendar.DATE, position);
        SimpleDateFormat localDateFormat = new SimpleDateFormat("dd");
        String str = localDateFormat.format(calendar.getTime());
        return str;
    }

    private String getCalendarMonthString(Trip trip, int position) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date(trip.getStartTime()));
        calendar.add(Calendar.DATE, position);
        SimpleDateFormat localDateFormat = new SimpleDateFormat("MMM yyyy");
        String str = localDateFormat.format(calendar.getTime());
        return str;
    }

    private String getCalendarDayName(Trip trip, int position) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date(trip.getStartTime()));
        calendar.add(Calendar.DATE, position);
        SimpleDateFormat localDateFormat = new SimpleDateFormat("E");
        String str = localDateFormat.format(calendar.getTime());
        return str;
    }

    @Override
    public int getCount() {
        return days.size();
    }

    @Override
    public Day getItem(int position) {
        return days.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }
}
