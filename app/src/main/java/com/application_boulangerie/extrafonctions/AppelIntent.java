package com.application_boulangerie.extrafonctions;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.application_boulangerie.PageListeProduits;
import com.application_boulangerie.PageProduit;
import com.application_boulangerie.R;
import com.application_boulangerie.data.Produit;
import com.owlike.genson.Genson;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class AppelIntent {

    public static ArrayList<Produit> produitList = new ArrayList<>();

    public static void appelIntentSimple(Context context, Class classAAfficher) {

        // On crée un Intent (Une intention)

        Intent intent = new Intent(context, classAAfficher);

        //lancer Intent
        context.startActivity(intent);

    }

    public static void appelIntentAvecExtraPageProduit(Context context, Class classAAfficher, Produit produit) {

        String produit_id = String.valueOf(produit.getProduit_id());

        // On crée un Intent (Une intention)
        Intent intent = new Intent(context, classAAfficher);

        intent.putExtra("produit_id", produit_id);

        //lancer Intent
        context.startActivity(intent);
    }



    // Fonction pour appeller Intent avec Extrat pour Page Produit
    public static void appelIntentAvecExtraPageProduitAvecImage(Context context, Produit produit, Class classAAfficher) {

        // Declarer les variables pour Extra

        String produit_id = String.valueOf(produit.getProduit_id());
        String produit_nom = produit.getProduit_nom();
        String produit_prix = String.valueOf(produit.getProduit_prix());
        String produit_description_ingredients = produit.getProduit_description_ingredients();
        int imageProduit = R.drawable.ic_launcher_background;


        // On crée un Intent (Une intention)
        Intent intent = new Intent(context, classAAfficher);

        intent.putExtra("id", produit_id);
        intent.putExtra("titre", produit_nom);
        intent.putExtra("liste_des_ingrédients", produit_description_ingredients);
        intent.putExtra("prix_produit", produit_prix);
        intent.putExtra("image_produit", imageProduit);

        //lancer Intent
        context.startActivity(intent);

    }

}






