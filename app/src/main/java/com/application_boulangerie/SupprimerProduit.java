package com.application_boulangerie;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.application_boulangerie.utils.AppelIntent;
import com.application_boulangerie.utils.MyHTTPConnection;
import com.application_boulangerie.utils.MyURL;
import com.application_boulangerie.utils.NameExtra;

public class SupprimerProduit extends AppCompatActivity {
   TextView tv_information_delete;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_supprimer_produit);

        this.tv_information_delete = findViewById(R.id.tv_information_delete);

        String id;

        try {
            // si intent a une extra Produit_id pour demander supprimer 1 produit, on va appeller methode supprimer produit
            if (getIntent().hasExtra(NameExtra.PRODUIT_ID.toString())) {
                id = getIntent().getStringExtra(NameExtra.PRODUIT_ID.toString());
                act_supprimer(id, NameExtra.PRODUIT);
            }
            // si intent a une extra MP_id pour demander supprimer 1 MP, on va appeller methode supprimer MP
            else if (getIntent().hasExtra(NameExtra.MP_ID.toString())){
                id = getIntent().getStringExtra(NameExtra.MP_ID.toString());
                act_supprimer(id, NameExtra.MP);
            }
            // si intent a une extra Categorie_id pour demander supprimer 1 categorie, on va appeller methode supprimer categorie
            else if (getIntent().hasExtra(NameExtra.CATEGORIE_ID.toString())){
                id = getIntent().getStringExtra(NameExtra.CATEGORIE_ID.toString());
                act_supprimer(id, NameExtra.CATEGORIE);
            }
            // si intent a une extra Ingredient_id pour demander supprimer 1 ingredient, on va appeller methode supprimer ingredient
            else if (getIntent().hasExtra(NameExtra.INGREDIENT_ID.toString())){
                id = getIntent().getStringExtra(NameExtra.INGREDIENT_ID.toString());
                act_supprimer(id, NameExtra.INGREDIENT);
            }

            // Afficher 1 message dans l'objet textview
            tv_information_delete.setText("SUPPRIMEE!!!");
              } catch (Exception e) {
                  e.printStackTrace();
                  Log.e("APP-BOULANGERIE: ", "Erreur: Ne peut pas supprime " + e.toString());
          }



    }

    // Methode onclick sur bouton "Fermer" pour fermer la page et retour du page Menu
    public void act_fermer_page_supprimer(View view) {

        AppelIntent.appelIntentSimple(this,Menu.class);
    }

    // Methode pour supprimer objet selon la classe demande
    private void act_supprimer(String id, NameExtra nameExtra) throws Exception {
        String textUrl = null;

            // si on demander supprimer 1 produit, on va prendre l'url du supprimer Produit pour prendre la connextion au server
           if (nameExtra == NameExtra.PRODUIT) {
               textUrl = MyURL.TITLE.toString() + MyURL.DELETEPRODUIT.toString() + id;
           }
           // sinon,  on demander supprimer 1 MP, on va prendre l'url du supprimer MP pour prendre la connextion au server
           else if ( nameExtra == NameExtra.MP) {
               textUrl = MyURL.TITLE.toString() + MyURL.DELETEMP.toString() + id;
           }
           //sinon, on demander supprimer 1 categorie, on va prendre l'url du supprimer categorie pour prendre la connextion au server
           else if (nameExtra == NameExtra.CATEGORIE) {
            textUrl = MyURL.TITLE.toString() + MyURL.DELETECATEGORIE.toString() + id;
           }
           // sinon, on demander supprimer 1 ingredient, on va prendre l'url du supprimer ingredient pour prendre la connextion au server
           else if (nameExtra == NameExtra.INGREDIENT){
               textUrl= MyURL.TITLE.toString() + MyURL.DELETEINGREDIENT.toString()+id;
            }

        SendHttpRequestTask t = new SendHttpRequestTask();

        String[] params = new String[]{textUrl};
        t.execute(params);

    }

    // Connecter au server pour faire la demande de supprimation
    private class SendHttpRequestTask extends AsyncTask<String, Void, String> {

        //out le code qui nécessite un temps d'exécution sera placé dans cette fonction.
        // Étant donné que cette fonction est exécutée dans un thread complètement séparé du thread d'interface utilisateur, vous n'êtes pas autorisé à mettre à jour l'interface ici.
        @Override
        protected String doInBackground(String... params) {
            String url = params[0];
            String data = null;
            try {

                data = MyHTTPConnection.httpConnectionRequestDELETE(url);

            } catch (Exception e) {
                e.printStackTrace();
            }

            return data;
        }

    }

}