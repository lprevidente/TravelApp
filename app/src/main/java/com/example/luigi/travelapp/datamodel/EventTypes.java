package com.example.luigi.travelapp.datamodel;

/**
 * Created by Luigi on 18/05/2017.
 */

public class EventTypes {

    private int image;
    private CharSequence text;

    public EventTypes(int image, CharSequence text) {
        this.image = image;
        this.text = text;
    }

    public int getImage() {
        return image;
    }

    public CharSequence getText() {
        return text;
    }

    public void setText(String textView) {
        this.text = textView;
    }
}
