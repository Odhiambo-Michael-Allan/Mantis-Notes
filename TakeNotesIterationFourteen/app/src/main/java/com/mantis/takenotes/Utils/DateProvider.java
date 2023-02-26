package com.mantis.takenotes.Utils;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DateProvider {

    public static String getCurrentDate() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat( "EEE, MMM d 'at' h:mm a" );
        String currentDateAndTime = simpleDateFormat.format( new Date() );
        return currentDateAndTime;
    }

    public static String getSimpleDateFrom( Date date ) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat( "MMM d" );
        return simpleDateFormat.format( date );
    }
}
