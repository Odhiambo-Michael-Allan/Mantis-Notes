package com.mantis.takenotes.Utils;

import android.content.Context;
import android.widget.Toast;

public class ToastProvider {

    public static void showToast( Context context, String message ) {
        Toast.makeText( context, message, Toast.LENGTH_SHORT ).show();
    }
}
