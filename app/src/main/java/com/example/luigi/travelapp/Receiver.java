package com.example.luigi.travelapp;

import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.widget.Toast;

import static com.example.luigi.travelapp.costanti.Constants.ICON;
import static com.example.luigi.travelapp.costanti.Constants.TEXT;
import static com.example.luigi.travelapp.costanti.Constants.TITLE;

/**
 * Created by bernardogiordano on 24/05/17.
 */

public class Receiver extends BroadcastReceiver {
    private static int mId = 0;

    @Override
    public void onReceive(Context context, Intent intent) {
        Bundle extras = intent.getExtras();
        int icon = extras.getInt(ICON);
        String title = extras.getString(TITLE);
        String text = extras.getString(TEXT);
        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(context)
                        .setSmallIcon(icon)
                        .setContentTitle(title)
                        .setContentText(text);

        NotificationManager mNotificationManager =
                (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.notify(mId, mBuilder.build());
        mId++;
    }
}
