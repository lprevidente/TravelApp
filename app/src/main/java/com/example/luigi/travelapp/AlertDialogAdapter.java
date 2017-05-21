package com.example.luigi.travelapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.luigi.travelapp.datamodel.EventTypes;

import java.util.ArrayList;
import java.util.List;

import static com.example.luigi.travelapp.costanti.Constants.integers;
import static com.example.luigi.travelapp.costanti.Constants.textTypes;

/**
 * Created by Luigi on 18/05/2017.
 */

public class AlertDialogAdapter extends BaseAdapter {
    private List<EventTypes> typesEvents = new ArrayList<>();
    private Context context;

    public AlertDialogAdapter(Context context) {
        this.context = context;
        for (int i = 0; i < 3; i++){
            typesEvents.add(new EventTypes(integers[i], textTypes[i])) ;
        }
    }

    public List<EventTypes> getList (){
        return typesEvents;
    }

    @Override
    public int getCount() {
        return typesEvents.size();
    }

    @Override
    public EventTypes getItem(int position) {
        return typesEvents.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {

        if (view == null)
            view = LayoutInflater.from(context).inflate(R.layout.alert_dialog_types_event, parent, false);

        TextView textView = (TextView) view.findViewById(R.id.textViewDescrTyoeEvent);
        ImageView imageView = (ImageView) view.findViewById(R.id.imageViewTypeEvent);

        final EventTypes tmp = typesEvents.get(position);
        textView.setText(tmp.getText());
        imageView.setImageResource(typesEvents.get(position).getImage());

        return view;
    }
}
