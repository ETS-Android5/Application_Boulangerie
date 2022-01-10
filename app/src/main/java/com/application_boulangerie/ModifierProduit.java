package com.application_boulangerie;

import android.content.Intent;
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

import com.application_boulangerie.data.Categorie;
import com.application_boulangerie.data.Produit;
import com.application_boulangerie.utils.AppelDialog;
import com.application_boulangerie.utils.AppelIntent;
import com.application_boulangerie.utils.AppelToast;
import com.application_boulangerie.utils.Fonctions;
import com.application_boulangerie.utils.MyHTTPConnection;
import com.application_boulangerie.utils.MyURL;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.List;

public class ModifierProduit extends AppCompatActivity {

    TextView tv_message_modifier;
    EditText edt_produit_id_update;
    EditText edt_produit_nom_update;
    EditText edt_produit_prix_update;
    EditText edt_produit_quantite_update;
    EditText edt_produit_categorie;

    Produit produit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modifier_produit);

        fct_associationViewAuJava();

        fct_remplirLaVuePageModifierProduit(edt_produit_id_update,
                                            edt_produit_nom_update,
                                            edt_produit_prix_update,
                                            edt_produit_quantite_update,
                                            edt_produit_categorie);



    }

    // On associe le XML avec le JAVA
    private void fct_associationViewAuJava(){
        this.tv_message_modifier = findViewById(R.id.tv_message_modifier);
        this.edt_produit_id_update = findViewById(R.id.edt_produit_id_update);
        this.edt_produit_nom_update = findViewById(R.id.edt_produit_nom_ajouter);
        this.edt_produit_prix_update = findViewById(R.id.edt_produit_prix_ajouter);
        this.edt_produit_quantite_update = findViewById(R.id.edt_produit_quantite_ajouter);
        this.edt_produit_categorie = findViewById(R.id.edt_produit_categorie);
    }

    // Methode onclick sur bouton "Ajouter" pour creer nouveau produit au serveur
    public void act_valide_update_produit(View view) {
        String checkID= edt_produit_categorie.getText().toString();
        if (!Fonctions.isNumeric(checkID)){
            tv_message_modifier.setText("Il n'y a pas de Categorie ID pour modifier, il faut choisir 1 Categorie dans la liste d'aide");
            tv_message_modifier.setTextColor(Color.RED);
        }
        try {
            int id = Integer.parseInt(prendreText(edt_produit_id_update));
            String nom = prendreText(edt_produit_nom_update);
            Double prix = Double.valueOf(prendreText(edt_produit_prix_update));
            int quantite = Integer.valueOf(prendreText(edt_produit_quantite_update));
            int categorieProd = Integer.parseInt((prendreText(edt_produit_categorie)));


            // Creer nouveau produit avec les valeurs pris dans la vue
            this.produit = new Produit(id, nom, prix, quantite);

            // mise a jour le produit dans le database
            modifierProduit(this.produit);
            AppelToast.displayCustomToast(this, "Ce produit est modifié");
        } catch (Exception e){
            Log.e("APP-BOULANGERIE: ", "Valeur des parametres est null "+ e.toString());
            AppelToast.displayCustomToast(this, "Enter les informations, svp!");

        }

    }

    private void modifierProduit(Produit produit) {

        String textUrl = MyURL.TITLE.toString()+ MyURL.MODIFIERPRODUIT.toString() +produit.getProduit_id();

        // Convertir produit java en Json
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String json = gson.toJson(produit);

        SendHttpRequestTaskOnUpdateProduit t = new SendHttpRequestTaskOnUpdateProduit();

        String[] params = new String[]{textUrl, json};
        t.execute(params);

    }



    private class SendHttpRequestTaskOnUpdateProduit extends AsyncTask<String, Void, String> {


    //out le code qui nécessite un temps d'exécution sera placé dans cette fonction.
    // Étant donné que cette fonction est exécutée dans un thread complètement séparé du thread d'interface utilisateur, vous n'êtes pas autorisé à mettre à jour l'interface ici.

    //out le code qui nécessite un temps d'exécution sera placé dans cette fonction.
    // Étant donné que cette fonction est exécutée dans un thread complètement séparé du thread d'interface utilisateur, vous n'êtes pas autorisé à mettre à jour l'interface ici.
    @Override
    protected String doInBackground(String... params) {
        String url = params[0];
        String produit = params[1];

        String result = null;

        try {
            result = MyHTTPConnection.httpConnectionRequestPOST(url,produit);
        } catch (Exception e) {
            e.printStackTrace();
            AppelToast.displayCustomToast(ModifierProduit.this, "Ne peut pas connect au server");
        }

        return result;
    }

    //la resultat de doInBackground est repris et afficher à la vue
    @Override
    protected void onPostExecute(String result) {

        String message = "Ce produit est modifié!";
        tv_message_modifier.setText(message);
       tv_message_modifier.setTextColor(Color.BLUE);
    }

}


   public static String prendreText(EditText edT) {
        String t= null;

        t = edT.getText().toString();

        return t;
    }

    public void fct_remplirLaVuePageModifierProduit(       EditText edt_produit_id_update,
                                                           EditText edt_produit_nom_update,
                                                           EditText edt_produit_prix_update,
                                                           EditText edt_produit_quantite_update,
                                                           EditText edt_produit_categorie) {
        try {
            if (getIntent().hasExtra("produit_id") == false) {
                throw new Exception("Aucun Extra donne pour l('id)");
            }
            if (getIntent().hasExtra("produit_nom") == false) {
                throw new Exception("Aucun Extra donne pour la nom");
            }
            if (getIntent().hasExtra("produit_prix") == false) {
                throw new Exception("Aucun Extra donne pour le prix");
            }
            if (getIntent().hasExtra("produit_quantite") == false) {
                throw new Exception("Aucun Extra donne pour lq quantite");
            }
            if (getIntent().hasExtra("produit_categorie") == false) {
                throw new Exception("Aucun Extra donne pour cateogrie de produit");
            }
        } catch (Exception e) {

            AppelToast.displayCustomToast(this, "Erreur détectée lors du remplissage de la vue [" + e.toString() + "]");

            getIntent().setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        }

        // mettre Nom du produit
        String produit_id= getIntent().getStringExtra("produit_id");
        edt_produit_id_update.setText(produit_id);

        // mettre list des ingredients du produit
        String produit_nom = getIntent().getStringExtra("produit_nom");
        edt_produit_nom_update.setText(produit_nom);

        // mettre prix du produit
        String produit_prix = getIntent().getStringExtra("produit_prix");
        edt_produit_prix_update.setText(produit_prix);

        // mettre quantite du produit
        String produit_quantite = getIntent().getStringExtra("produit_quantite");
        edt_produit_quantite_update.setText(produit_quantite);

        // mettre prix du produit
        String produit_categorie = getIntent().getStringExtra("produit_categorie");

        edt_produit_categorie.setText(produit_categorie);



        int id = Integer.parseInt(produit_id);
        Double prix = Double.valueOf(produit_prix);
        int quantite = Integer.valueOf(produit_quantite);

        // Creer nouveau produit avec les valeurs pris dans la vue
        this.produit = new Produit(id, produit_nom, prix,quantite);
    }

    // Methode onclick sur button "retour" pour retour à la page Produit
    public void act_retour_page_produit(View view) {

        AppelToast.displayCustomToast(this,"Retour a la page de produit");

        AppelIntent.appelIntentAvecExtraProduit(this, PageProduit.class, this.produit);


    }
    // Methode onclick sur bouton "aide" pour afficher la liste des categories et permettre utilisateur choisir 1 categorie
    public void act_aide_affichage_listCategorie(View view) {


        AppelToast.displayCustomToast(this, "Ouvrir la liste des categories");

        prendrelistCategorieHTTP();


    }


    private void prendrelistCategorieHTTP(){
        // Get connection to HTTP server throw Thread
        String textUrl =MyURL.TITLE.toString()+ MyURL.LISTCATEGORIE.toString();

        SendHttpRequestTaskCategorie t = new SendHttpRequestTaskCategorie();

        String[] params = new String[]{textUrl};
        t.execute(params);


    }

    private class SendHttpRequestTaskCategorie extends AsyncTask<String, Void, String> {


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


            List<Categorie> listCategorie = Fonctions.changefromJsonListCategorie(result);

            AppelDialog.showAlertDialogListCategorie(ModifierProduit.this, listCategorie, edt_produit_categorie);

        }

    }

    public void act_modifier_ingredients(View view) {

        AppelToast.displayCustomToast(this, "Aller à la page liste des ingredients du produit");
        AppelIntent.appelIntentAvecExtraProduit(this, PageListIngredient.class, this.produit);

    }
}