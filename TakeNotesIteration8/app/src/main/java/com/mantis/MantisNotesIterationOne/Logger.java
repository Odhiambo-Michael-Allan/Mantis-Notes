package com.mantis.MantisNotesIterationOne;

import android.util.Log;

public class Logger {

    private static final String TAG = "Mantis Notes";

    public static void log( String message ) {
        Log.i( TAG, message );
    }
}
