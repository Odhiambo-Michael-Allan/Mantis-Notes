package com.mantis.TakeNotes.Utils;

import android.util.Log;

public class Logger {

    private static final String TAG = "Take Notes";

    public static void log( String message ) {
        Log.i( TAG, message );
    }
}
