package com.mantis.TakeNotes.Utils;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DateProvider {

    public static String getCurrentDate() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat( "EEE, MMM d 'at' h:mm a" );
        String currentDateAndTime = simpleDateFormat.format( new Date() );
        return currentDateAndTime;
    }
}
