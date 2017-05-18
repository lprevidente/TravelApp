package com.example.luigi.travelapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.luigi.travelapp.datamodel.TypesEvent;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Luigi on 18/05/2017.
 */

public class AlertDialogAdapter extends BaseAdapter {

    List<TypesEvent> typesEvents= new ArrayList<>();
    CharSequence[] textTypes = new CharSequence [] {"Volo", "Ristorante", "Museo"};

    Integer[] integers = new Integer[] {R.drawable.ic_action_name_flight,
            R.drawable.ic_action_name_rest7aurant
            ,R.drawable.ic_action_name_place};

    private Context context;

    public AlertDialogAdapter(Context context) {
        this.context = context;
        for( int i=0; i<3; i++){
            typesEvents.add(new TypesEvent(integers[i], textTypes[i])) ;
        }
    }
    public List<TypesEvent> getList (){
        return typesEvents;
    }
    @Override
    public int getCount() {
        return typesEvents.size();
    }

    @Override
    public TypesEvent getItem(int position) {
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


        final TypesEvent tmp = typesEvents.get(position);
        textView.setText(tmp.getText());
        imageView.setImageResource(typesEvents.get(position).getImage());

        return view;
    }
}
