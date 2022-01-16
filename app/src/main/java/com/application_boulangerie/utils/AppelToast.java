package com.application_boulangerie.utils;

import android.content.Context;
import android.view.Gravity;
import android.widget.Toast;

public class AppelToast {

    public static void displayCustomToast(Context context, String toastText) {
        // Declare 1 objet type Toast
        final Toast toast;
        // Creer 1 toast et mettre dans l'objet type Toast
        toast = Toast.makeText(context, toastText, Toast.LENGTH_SHORT);

        // Afficher le toast
        toast.show();

    }
}
