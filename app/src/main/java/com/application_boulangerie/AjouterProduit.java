package com.application_boulangerie;

import android.graphics.Color;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.application_boulangerie.data.Produit;
import com.application_boulangerie.extrafonctions.AppelIntent;
import com.application_boulangerie.extrafonctions.AppelToast;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

public class AjouterProduit extends AppCompatActivity {

    TextView tv_message;
    EditText edt_produit_nom_cree;
    EditText edt_produit_prix_cree;
    EditText edt_produit_quantite_cree;
    EditText edt_produit_ingredient_cree;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ajouter_produit);

        fct_associationViewAuJava();
    }

    // On associe le XML avec le JAVA
    private void fct_associationViewAuJava() {
        this.tv_message = findViewById(R.id.tv_message);
        this.edt_produit_nom_cree = findViewById(R.id.edt_produit_nom_update);
        this.edt_produit_prix_cree = findViewById(R.id.edt_produit_prix_update);
        this.edt_produit_quantite_cree = findViewById(R.id.edt_produit_quantite_update);
        this.edt_produit_ingredient_cree = findViewById(R.id.edt_produit_ingredient_update);
    }

    public void act_retour_pageListProduit(View view) {
        AppelToast.displayCustomToast(this, "Retour à la page Liste des produits");
        AppelIntent.appelIntentSimple(this, PageListeProduits.class);
    }

    public void act_valide_ajouter_produit(View view) {

        String nom = ajouteText(edt_produit_nom_cree);
        Double prix = Double.valueOf(ajouteText(edt_produit_prix_cree));
        Integer quantite = Integer.valueOf(ajouteText(edt_produit_quantite_cree));
        String ingredient = ajouteText(edt_produit_ingredient_cree);

        Produit produit = new Produit(nom, prix, quantite, ingredient);

            tv_message.setText("Nouveau produit est créé");
            tv_message.setTextColor(Color.BLUE);

            ajoutProduit(produit, tv_message);
    }

    private void ajoutProduit(Produit produit, TextView tv_message) {

        AppelToast.displayCustomToast(this,"Nouveau produit est créé");
        String textUrl = "http://192.168.43.190:8080/Bouglangerie/webapi/produit/insertionProduit";
        String methode = "POST";

        // Convertir produit java en Json
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String json = gson.toJson(produit);

        // Methode d'appel les fonctions qui marche dans UI Thread
        SendHttpRequestTaskCreate t = new SendHttpRequestTaskCreate();

        // Declarer les paramettre utilisés dans ce methode
        String[] params = new String[]{textUrl, methode, json};

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
            String methode = params[1];
            String produit = params[2];
            String result = null;

            try {
                // prendre la reponse du server et mettre dans ce String
                result = httpConnection(url,methode, produit);
            } catch (Exception e) {
                e.printStackTrace();
                // Afficher un toast s'il y a erreur
                AppelToast.displayCustomToast(AjouterProduit.this, "Ne peut pas connect au server");
            }

            return result;
        }

    }

    public String  httpConnection(String textUrl, String methode, String json) throws Exception {

            // Get connection to HTTP server
            HttpURLConnection urlConnection = null;
            try {

                // get connect to HTTP server
                URL url = new URL(textUrl);
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod(methode);
                urlConnection.setDoOutput(true);
                urlConnection.setRequestProperty("Content-Type", "application/json");

                //écriture de texte dans un flux de sortie
                OutputStream out = new BufferedOutputStream(urlConnection.getOutputStream());
                out.write(json.getBytes());
                out.close();
                //The connection is opened implicitly by calling getInputStream.
                urlConnection.getInputStream();

                // Creer 1 valeur type InputSteam pour recuper la flux
                InputStream in = new BufferedInputStream(urlConnection.getInputStream());
                //Permet de  décortiquer à partir d'un flux d'un certain nombre de methode indiqué
                Scanner scanner = new Scanner(in);
                // Creer 1 chaine type String pour stocker la reponse du server
                String responseServer = null;

                // prendre la response du server et mettre dans ce String
                responseServer = scanner.nextLine();


                return responseServer;

            } catch (Exception e) {
                Log.e("APPLICATION_BOULANGERIE", " On n'a pas trouve http server ", e);

            } finally {
                if (urlConnection != null) urlConnection.disconnect();
            }
     return null;
    }

    public String ajouteText( EditText edT) {
        String t= null;
        // Verifier si editText est vide
            t = edT.getText().toString();
            return t;
    }

}