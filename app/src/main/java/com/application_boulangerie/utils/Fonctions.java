package com.application_boulangerie.utils;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.widget.EditText;

import com.application_boulangerie.MainActivity;
import com.application_boulangerie.Menu;
import com.application_boulangerie.adapter.CustomDialog;
import com.application_boulangerie.data.Categorie;
import com.application_boulangerie.data.Ingredient;
import com.application_boulangerie.data.MatierePremiere;
import com.application_boulangerie.data.Produit;
import com.application_boulangerie.data.Utilisateur;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

public class Fonctions {

    public static boolean isNumeric(String str) {
        if (str == null) {
            return false;
        }
        int sz = str.length();
        for (int i = 0; i < sz; i++) {
            if (Character.isDigit(str.charAt(i)) == false) {
                return false;
            }
        }
        return true;
    }

    public static String prendreText(EditText edT) {
        String t= null;

        t = edT.getText().toString();

        return t;
    }

    public static List<Categorie> changefromJsonListCategorie(String json){
        Gson gson = new GsonBuilder().setPrettyPrinting().create();

        // cas 2: transformer la string en tableau Json (pour methode getAll)
        // Definir type pour List<Categorie> pour get valeur from server et puis transfert valeur type Json to type List<Categorie>
        Type type = new TypeToken<List<Categorie>>() {}.getType();
        List<Categorie> listCategorie = gson.fromJson(json,type);

        return listCategorie;
    }

    public static List<MatierePremiere> changefromJsonListMP (String json){
        Gson gson = new GsonBuilder().setPrettyPrinting().create();

        // cas 2: transformer la string en tableau Json (pour methode getAll)
        // Definir type pour List<Categorie> pour get valeur from server et puis transfert valeur type Json to type List<Categorie>
        Type type = new TypeToken<List<MatierePremiere>>() {}.getType();
        List<MatierePremiere> listMP = gson.fromJson(json,type);

        return listMP;
    }

    public static  List<Produit> changefromJsonListProduit (String json) {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();

        // cas 2: transformer la string en tableau Json (pour methode getAll)
        // Definir type pour List<Produit> pour get valeur from server et puis transfert valeur type Json to type List<Produi>
        Type type = new TypeToken<List<Produit>>() {}.getType();
        List<Produit> listProduit = gson.fromJson(json, type);
        return  listProduit;

    }

    public static List<Ingredient> changefromJsonListIngredient (String json){
        Gson gson = new GsonBuilder().setPrettyPrinting().create();

        // cas 2: transformer la string en tableau Json (pour methode getAll)
        // Definir type pour List<Categorie> pour get valeur from server et puis transfert valeur type Json to type List<Categorie>
        Type type = new TypeToken<List<Ingredient>>() {}.getType();
        List<Ingredient> listIng = gson.fromJson(json,type);

        return listIng;
    }

    public static List<Utilisateur> changefromJsonListUsers(String json) {
        Gson gson = new Gson();

        // cas 2: transformer la string en tableau Json (pour methode getAll)
        // Definir type pour List<Categorie> pour get valeur from server et puis transfert valeur type Json to type List<Categorie>
        Type type = new TypeToken<List<Utilisateur>>() {}.getType();
        List<Utilisateur> listUser = gson.fromJson(json,type);
         return listUser;
    }

}
