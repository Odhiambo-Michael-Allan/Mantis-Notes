package com.mantis.MantisNotesIterationOne.Models;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DateProvider {

    public static String getCurrentDate() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat( "yyyy.MM.dd G 'at' HH:mm:ss z" );
        String currentDateAndTime = simpleDateFormat.format( new Date() );
        return currentDateAndTime;
    }
}
