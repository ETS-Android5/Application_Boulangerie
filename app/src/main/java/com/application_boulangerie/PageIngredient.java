package com.application_boulangerie;

import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.application_boulangerie.data.Ingredient;
import com.application_boulangerie.data.MatierePremiere;
import com.application_boulangerie.utils.AppelDialog;
import com.application_boulangerie.utils.AppelIntent;
import com.application_boulangerie.utils.AppelToast;
import com.application_boulangerie.utils.Fonctions;
import com.application_boulangerie.utils.MyHTTPConnection;
import com.application_boulangerie.utils.MyURL;
import com.application_boulangerie.utils.NameExtra;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.List;

public class PageIngredient extends AppCompatActivity {

    TextView tv_message_page_ingredient;
    TextView tv_id_ingredient, tv_ingredient_produitID;
    EditText edt_ingredient_mp_id,edt_ingredient_quantite,edt_ingredient_unite;
    Ingredient ingredient;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_page_ingredient);

        // Appeller focntion pour view au Java
        fct_associationViewAuJava();

        // Appeller fonction pour afficher la page
        fct_remplirLaVuePageIngredient();

    }

    private void fct_associationViewAuJava(){
        this.tv_message_page_ingredient = findViewById(R.id.tv_message_page_ingredient);
        this.tv_id_ingredient = findViewById(R.id.tv_id_ingredient);
        this.tv_ingredient_produitID = findViewById(R.id.tv_user_id);
        this.edt_ingredient_mp_id = findViewById(R.id.edt_user_name);
        this.edt_ingredient_quantite= findViewById(R.id.edt_user_pass);
        this.edt_ingredient_unite = findViewById(R.id.edt_ingredient_unite);
    }

    //Methode pour afficher la page PageIngredient
    public void fct_remplirLaVuePageIngredient() {

        // methode try pour donner 1 exception s'il n'y a pas de Extra
        if (getIntent().hasExtra(NameExtra.INGREDIENT_ID.toString())) {

            // Prendre id du produit puis mettre dans 1 string
            String ingredient_id = getIntent().getStringExtra(NameExtra.INGREDIENT_ID.toString());
            // Mettre le valeur pour textview tv_id_ingredient
            tv_id_ingredient.setText(ingredient_id);

            // Remettre this id to type Integer
            int id = Integer.parseInt(ingredient_id);

            // Get connection to HTTP server throw Thread
            String textUrl = MyURL.TITLE.toString() + MyURL.FINDINGREDIENTBYID.toString()+ id;

          SendHttpRequestTaskAffichage t = new SendHttpRequestTaskAffichage();

            String[] params = new String[]{textUrl};
            t.execute(params);
        }
        else if (getIntent().hasExtra(NameExtra.PRODUIT_ID.toString())){
            String produit_id = getIntent().getStringExtra(NameExtra.PRODUIT_ID.toString());
            tv_ingredient_produitID.setText(produit_id);
            tv_message_page_ingredient.setText("Entrer information pour ajouter nouveau Ingredient");
        }

    }

    private class SendHttpRequestTaskAffichage extends AsyncTask<String, Void, String> {


        //out le code qui nécessite un temps d'exécution sera placé dans cette fonction.
        // Étant donné que cette fonction est exécutée dans un thread complètement séparé du thread d'interface utilisateur, vous n'êtes pas autorisé à mettre à jour l'interface ici.
        @RequiresApi(api = Build.VERSION_CODES.M)
        @Override
        protected  String  doInBackground(String... params) {
            String url = params[0];

            String result = null;
            try {
                result = MyHTTPConnection.startHttpRequestGET(url);
            } catch (Exception e) {
                e.printStackTrace();
            }

            return result;
        }

        //la resultat de doInBackground est repris et afficher à la vue
        @RequiresApi(api = Build.VERSION_CODES.M)
        @Override
        protected void onPostExecute( String result ) {

            Gson gson = new GsonBuilder().setPrettyPrinting().create();

            PageIngredient.this.ingredient = gson.fromJson(result, Ingredient.class);
            affichageDuPage(ingredient);

        }

    }

    public void affichageDuPage(Ingredient ingredient ){

        String produit_id = String.valueOf(ingredient.getProduit_id());
        String mp_id = String.valueOf(ingredient.getMp_id());
        String quantite = String.valueOf(ingredient.getIngredient_quantite());
        String unite = ingredient.getIngredient_unite();

        this.tv_ingredient_produitID.setText(produit_id);
        this.edt_ingredient_mp_id.setText(mp_id);
        this.edt_ingredient_quantite.setText(quantite);
        this.edt_ingredient_unite.setText(unite);

    }

    public void act_ajouteringredient(View view) {

        try {
            int produit_id = Integer.parseInt(tv_ingredient_produitID.getText().toString());
            int mp_id = Integer.parseInt(Fonctions.prendreText(edt_ingredient_mp_id));
            int quantite = Integer.parseInt(Fonctions.prendreText(edt_ingredient_quantite));
            String unite = Fonctions.prendreText(edt_ingredient_unite);

          Ingredient ingredient = new Ingredient(produit_id, mp_id, quantite, unite);

            ajouternouveauIngredient(ingredient);

            tv_message_page_ingredient.setText("Nouveau ingredient est créé ");
            tv_message_page_ingredient.setTextColor(Color.BLUE);



        } catch (Exception e){
            Log.e("APP-BOULANGERIE: ","Valeur des parametres est null " + e.toString());
            AppelToast.displayCustomToast(this, "Enter les informations, svp!");
        }

    }

    public void ajouternouveauIngredient(Ingredient ingredient){
        String textUrl = MyURL.TITLE.toString()+ MyURL.AJOUTEINGREDIENT.toString();

        // Convertir produit java en Json
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String json = gson.toJson(ingredient);

        // Methode d'appel les fonctions qui marche dans UI Thread
       SendHttpRequestTaskCreate t = new SendHttpRequestTaskCreate();

        // Declarer les paramettre utilisés dans ce methode
        String[] params = new String[]{textUrl, json};

        //Executer methodes
        t.execute(params);
    }
    private class SendHttpRequestTaskCreate extends AsyncTask<String, Void, String> {

        //out le code qui nécessite un temps d'exécution sera placé dans cette fonction.
        // Étant donné que cette fonction est exécutée dans un thread complètement séparé du
        // thread d'interface utilisateur, vous n'êtes pas autorisé à mettre à jour l'interface ici.
        @Override
        protected String doInBackground(String... params) {
            String url = params[0];
            String ingredient = params[1];
            String result = null;

            try {
                // prendre la reponse du server et mettre dans ce String
                result = MyHTTPConnection.httpConnectionRequestPOST(url,ingredient);
            } catch (Exception e) {
                e.printStackTrace();
                Log.e("APP-BOULANGERIE:", "Erreur: Ne peut pas connect au server");
            }

            return result;
        }

    }

    public void act_retour_pageProduit(View view) {

        String produit_id = tv_ingredient_produitID.getText().toString();

        AppelToast.displayCustomToast(this, "Retour à la page Produit");

        AppelIntent.appelIntentAvecExtraProduit_ID(this, PageProduit.class,produit_id );


    }

    public void act_suprimer_ingredient(View view) {

        String checkID = tv_id_ingredient.getText().toString();
        try {
            if (!Fonctions.isNumeric(checkID)) {
                tv_message_page_ingredient.setText("Il n'y a pas de Ingredient pour supprimer, il faut choisir 1 ingredient dans la liste du page Modifier Produit");
                tv_message_page_ingredient.setTextColor(Color.RED);
            } else {
                AppelDialog.showAlertDialogSuppimer(PageIngredient.this, tv_id_ingredient, NameExtra.INGREDIENT);
            }
        }catch (Exception e){
            Log.e("APP-BOULANGERIE: ", "Erreur de supprimer la MP, pas de mp_id");
            AppelToast.displayCustomToast(this, "Il n'y a pas de Ingredient pour supprimer, il faut choisir 1 ingredient dans la liste pour supprimer");
        }
    }

    public void act_modifier_ingredient(View view) {
        String checkID = tv_id_ingredient.getText().toString();
        if (!Fonctions.isNumeric(checkID)) {
            tv_message_page_ingredient.setText("Il n'y a pas de Ingredient pour modifier, il faut choisir 1 ingredient dans la liste du page Modifier Produit");
            tv_message_page_ingredient.setTextColor(Color.RED);
        }
        try {

            int id = Integer.parseInt(tv_id_ingredient.getText().toString());
            int produit_id = Integer.parseInt(tv_ingredient_produitID.getText().toString());
            int mp_id = Integer.valueOf(Fonctions.prendreText(edt_ingredient_mp_id));
            int quantite = Integer.parseInt(Fonctions.prendreText(edt_ingredient_quantite));
            String unite = Fonctions.prendreText(edt_ingredient_unite);

            // Creer nouveau produit avec les valeurs pris dans la vue
            this.ingredient = new Ingredient(id, produit_id, mp_id, quantite, unite);

            // mise a jour le produit dans le database
            modifierIngredient(this.ingredient);
            AppelToast.displayCustomToast(this, "Cette ingredient est modifié");

        } catch (Exception e){
            Log.e("APP-BOULANGERIE: ", "Valeur des parametres est null "+ e.toString());
            AppelToast.displayCustomToast(this, "Enter les informations, svp!");

        }

    }
    private void modifierIngredient(Ingredient ingredient) {

        String textUrl1 = MyURL.TITLE.toString()+ MyURL.MODIFIERINGREDIENT.toString()+ingredient.getIngredient_id();
        String textUrl2 = MyURL.TITLE.toString() + MyURL.LISTINGREDIENT.toString();

        // Convertir produit java en Json
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String json = gson.toJson(ingredient);

        SendHttpRequestTaskOnUpdateIngredient t = new SendHttpRequestTaskOnUpdateIngredient();

        String[] params = new String[]{textUrl1,textUrl2, json};
        t.execute(params);


    }
    private class SendHttpRequestTaskOnUpdateIngredient extends AsyncTask<String, Void, String> {


        //out le code qui nécessite un temps d'exécution sera placé dans cette fonction.
        // Étant donné que cette fonction est exécutée dans un thread complètement séparé du thread d'interface utilisateur, vous n'êtes pas autorisé à mettre à jour l'interface ici.

        //out le code qui nécessite un temps d'exécution sera placé dans cette fonction.
        // Étant donné que cette fonction est exécutée dans un thread complètement séparé du thread d'interface utilisateur, vous n'êtes pas autorisé à mettre à jour l'interface ici.
        @Override
        protected String doInBackground(String... params) {
            String urlModifierIngredient = params[0];
            String urlListIngredient = params[1];
            String str_ingredient = params[2];
            Ingredient ingredient = new Gson().fromJson(str_ingredient, Ingredient.class);
            String result = null;

            try {
                result = MyHTTPConnection.startHttpRequestGET(urlListIngredient);
               List<Ingredient> listIngredient = Fonctions.changefromJsonListIngredient(result);

               Boolean check = false;

               // si les valeurs dans objet n'est pas changer, check = true pour demander modifier valeur avant valider
                for (Ingredient ingd: listIngredient) {
                    if (ingd.getIngredient_id() == ingredient.getIngredient_id()) {
                        check = ingredient.getProduit_id() == ingd.getProduit_id()
                                && ingredient.getMp_id() == ingd.getMp_id()
                                && ingredient.getIngredient_unite().equals(ingd.getIngredient_unite())
                                && ingredient.getIngredient_quantite() == ingd.getIngredient_quantite();
                    }
                }

                // si check = false, on peut modifier cette ingredient dans serveur
                if (!check){
                   mofidierIngredientAuServeur(urlModifierIngredient, str_ingredient);
                }

                return result = check.toString();
            } catch (Exception e) {
                Log.e("APP-BOULANGERIE: ","Ne peut pas connecter HTTP " + e.toString());

            }

            return result;
        }

        //la resultat de doInBackground est repris et afficher à la vue
        @Override
        protected void onPostExecute(String result) {
            if (result.equals("false")) {
                String message = "Cette ingredient est modifiée!";
                tv_message_page_ingredient.setText(message);
                tv_message_page_ingredient.setTextColor(Color.BLUE);
            } else if (result.equals("true")) {
                String message = "A modifier information pour modifier " + NameExtra.INGREDIENT.toString();
                tv_message_page_ingredient.setText(message);
                tv_message_page_ingredient.setTextColor(Color.RED);

            }
        }

        private void mofidierIngredientAuServeur(String urlModifierIngredient, String str_ingredient) throws Exception {
            String result = MyHTTPConnection.httpConnectionRequestPOST(urlModifierIngredient, str_ingredient);
        }
    }

    // Methode onclick sur bouton "aide" pour afficher la liste les MP et permettre utilisateur choisir 1 MP
    public void act_aide_affichage_listMP(View view) {
        String textUrl = MyURL.TITLE.toString()+ MyURL.LISTMP.toString();

      SendHttpRequestTaskListMP t = new SendHttpRequestTaskListMP();

        String[] params = new String[]{textUrl};
        t.execute(params);

    }
    private class SendHttpRequestTaskListMP extends AsyncTask<String, Void, String> {


        //out le code qui nécessite un temps d'exécution sera placé dans cette fonction.
        // Étant donné que cette fonction est exécutée dans un thread complètement séparé du thread d'interface utilisateur, vous n'êtes pas autorisé à mettre à jour l'interface ici.
        @RequiresApi(api = Build.VERSION_CODES.M)
        @Override
        protected  String  doInBackground(String... params) {
            String url = params[0];

            String result = null;
            try {
                result = MyHTTPConnection.startHttpRequestGET(url);
            } catch (Exception e) {
                e.printStackTrace();
            }

            return result;
        }

        //la resultat de doInBackground est repris et afficher à la vue
        @RequiresApi(api = Build.VERSION_CODES.M)
        @Override
        protected void onPostExecute( String result ) {


            List<MatierePremiere> listMP = Fonctions.changefromJsonListMP(result);

            AppelDialog.showAlertDialogListMPAvecEDT(PageIngredient.this, listMP, edt_ingredient_mp_id, edt_ingredient_unite);


        }

    }

}