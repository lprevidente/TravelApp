package com.example.luigi.travelapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.luigi.travelapp.datamodel.DataStore;
import com.example.luigi.travelapp.datamodel.Day;
import com.example.luigi.travelapp.datamodel.Event;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Collections;
import java.util.List;

import static com.example.luigi.travelapp.costanti.Constants.KEY_EVENT_LIST;
import static com.example.luigi.travelapp.costanti.Constants.Num_Events;


/**
 * Created by Luigi on 08/05/2017.
 */

public class DayListAdapter extends BaseAdapter {

    private Context context;
    private List<Day> days = Collections.emptyList();

    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

    public DayListAdapter(Context context) {
        this.context = context;
    }

    public void update(List<Day> newList) {
        days = newList;
        notifyDataSetChanged();
    }

    public View getView(int position, View view, ViewGroup parent) {
        if (view == null)
            view = LayoutInflater.from(context).inflate(R.layout.day_list_adapter, parent, false);

        TextView numDaytxt = (TextView)view.findViewById(R.id.dayTextView);

        final TextView Event1txt = (TextView)view.findViewById(R.id.textViewEvent1);
        final TextView Event2txt = (TextView)view.findViewById(R.id.textViewEvent2);
        final TextView Event3txt = (TextView)view.findViewById(R.id.textViewEvent3);

        numDaytxt.setText(days.get(position).getNumber()+"°");

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference(user.getUid())
                .child(KEY_EVENT_LIST)
                .child(days.get(position).getKey());

        // Mostro soltanto i primi 3 eventi di quel determinato giorno
        final String [] titlesEvent = new String[3];

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                //Se il numero degli eventi è maggiore di 3
                //allora il max lo pongo a 3
                long i= dataSnapshot.getChildrenCount();
                if(i > Num_Events)
                    i = Num_Events;

                int j = 0;
                for(DataSnapshot snapshot: dataSnapshot.getChildren()){
                    // prendo gli eventi uno ad uno e ne ricavo il titolo
                    if(j < i) {
                        Event post = snapshot.getValue(Event.class);
                        titlesEvent[j] = post.getTitle();
                        j++;
                    }
                }
                // dopo il mio ciclo setto i titoli uno ad uno
                Event1txt.setText(titlesEvent[0]);
                Event2txt.setText(titlesEvent[1]);
                Event3txt.setText(titlesEvent[2]);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {}
        });

        return view;
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
