package com.example.luigi.travelapp.util;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by Bernardo on 12/05/2017.
 */

public class DateUtil {
    public static Date incrementDay(Date date, int days) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.DATE, days);
        return cal.getTime();
    }
}
