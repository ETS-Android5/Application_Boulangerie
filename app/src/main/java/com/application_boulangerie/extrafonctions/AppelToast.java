package com.application_boulangerie.extrafonctions;

import android.content.Context;
import android.widget.Toast;

public class AppelToast {

    public static void displayCustomToast(Context context, String toastText) {

        final Toast toast;

        toast = Toast.makeText(context, toastText, Toast.LENGTH_LONG);
        toast.show();

    }
}
