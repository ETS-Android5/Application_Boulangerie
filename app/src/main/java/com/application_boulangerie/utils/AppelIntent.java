package com.application_boulangerie.utils;

import android.content.Context;
import android.content.Intent;

import com.application_boulangerie.R;
import com.application_boulangerie.data.Ingredient;
import com.application_boulangerie.data.MatierePremiere;
import com.application_boulangerie.data.Produit;
import com.google.gson.Gson;

import java.util.ArrayList;

public class AppelIntent {

    public static ArrayList<Produit> produitList = new ArrayList<>();

    public static void appelIntentSimple(Context context, Class classAAfficher) {

        // On crée un Intent (Une intention)

        Intent intent = new Intent(context, classAAfficher);

        //lancer Intent
        context.startActivity(intent);

    }

    public static void appelIntentAvecExtraProduit(Context context, Class classAAfficher, Produit produit) {

        String produit_id = String.valueOf(produit.getProduit_id());

        // On crée un Intent (Une intention)
        Intent intent = new Intent(context, classAAfficher);

        intent.putExtra(NameExtra.PRODUIT_ID.toString(), produit_id);

        //lancer Intent
        context.startActivity(intent);
    }

    public static void appelIntentAvecExtraMP(Context context, Class classAAfficher, MatierePremiere mp) {

        String mp_id = String.valueOf(mp.getMp_id());

        // On crée un Intent (Une intention)
        Intent intent = new Intent(context, classAAfficher);

        intent.putExtra(NameExtra.MP_ID.toString(), mp_id);

        //lancer Intent
        context.startActivity(intent);
    }

    public static void appelIntentAvecExtraProduit_ID(Context context, Class classAAfficher, String produit_id) {


        // On crée un Intent (Une intention)
        Intent intent = new Intent(context, classAAfficher);

        intent.putExtra(NameExtra.PRODUIT_ID.toString(), produit_id);

        //lancer Intent
        context.startActivity(intent);
    }

    public static void appelIntentAvecExtraIngredient(Context context, Class classAAfficher, Ingredient ingredient) {

        String ingredient_id = String.valueOf(ingredient.getIngredient_id());

        // On crée un Intent (Une intention)
        Intent intent = new Intent(context, classAAfficher);

        intent.putExtra(NameExtra.INGREDIENT_ID.toString(), ingredient_id);

        //lancer Intent
        context.startActivity(intent);
    }


    // Fonction pour appeller Intent avec Extrat pour Page Produit
    public static void appelIntentAvecExtraPageProduitAvecImage(Context context, Produit produit, Class classAAfficher) {

        // Declarer les variables pour Extra

        String produit_id = String.valueOf(produit.getProduit_id());
        String produit_nom = produit.getProduit_nom();
        String produit_prix = String.valueOf(produit.getProduit_prix());
        String produit_categorie = new Gson().toJson(produit.getProduitCategorie());
        int imageProduit = R.drawable.ic_launcher_background;


        // On crée un Intent (Une intention)
        Intent intent = new Intent(context, classAAfficher);

        intent.putExtra("id", produit_id);
        intent.putExtra("titre", produit_nom);
        intent.putExtra("categore", produit_categorie);
        intent.putExtra("prix_produit", produit_prix);
        intent.putExtra("image_produit", imageProduit);

        //lancer Intent
        context.startActivity(intent);

    }

}






