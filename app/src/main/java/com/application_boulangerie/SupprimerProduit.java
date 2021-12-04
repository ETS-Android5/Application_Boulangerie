package com.application_boulangerie;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.application_boulangerie.data.Produit;
import com.application_boulangerie.extrafonctions.AppelIntent;
import com.application_boulangerie.extrafonctions.AppelToast;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class SupprimerProduit extends AppCompatActivity {
   TextView tv_information_delete;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_supprimer_produit);

        this.tv_information_delete = findViewById(R.id.tv_information_delete);

        try {
            if (getIntent().hasExtra("produit_id") == false) {
                throw new Exception("Aucun Extra donne pour le titre");
                     }
        } catch (Exception e) {

            AppelToast.displayCustomToast(this, "Erreur détectée lors du remplissage de la vue [" + e.toString() + "]");

            getIntent().setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        }

        // mettre Nom du produit
        String produit_id = getIntent().getStringExtra("produit_id");

            try {
                supprimer_produit(produit_id,tv_information_delete);

              } catch (Exception e) {
                  e.printStackTrace();
          }


    }

    public void act_fermer_page_supprimer(View view) {

        AppelIntent.appelIntentSimple(this,PageListeProduits.class);
    }

    private void supprimer_produit(String id, TextView tv_information_delete) throws Exception {
        String textUrl = "http://192.168.43.189:8080/Bouglangerie/webapi/produit/deleteProduit/"+id;
        String methode = "DELETE";

        SendHttpRequestTask t = new SendHttpRequestTask();

        String[] params = new String[]{textUrl, methode};
        t.execute(params);

    }

    private class SendHttpRequestTask extends AsyncTask<String, Void, String> {


        //out le code qui nécessite un temps d'exécution sera placé dans cette fonction.
        // Étant donné que cette fonction est exécutée dans un thread complètement séparé du thread d'interface utilisateur, vous n'êtes pas autorisé à mettre à jour l'interface ici.
        @Override
        protected String doInBackground(String... params) {
            String url = params[0];
            String methode = params[1];

            String data = null;
            try {
                data = httpConnection(url, methode);
            } catch (Exception e) {
                e.printStackTrace();
            }

            return data;
        }

        //la resultat de doInBackground est repris et afficher à la vue
        @Override
        protected void onPostExecute(String result) {
            tv_information_delete.setText(result);

        }

    }

    public String httpConnection(String textUrl, String methode) throws Exception {
        HttpURLConnection urlConnection = null;
        try {
            // get connect to HTTP server
            URL url = new URL(textUrl);
            urlConnection= (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod(methode);
            urlConnection.setDoOutput(true);
            urlConnection.setRequestProperty("Content-Type", "application/json");
            urlConnection.connect();


            // Creer 1 valeur type InputSteam pour recuper la flux
            InputStream in = new BufferedInputStream( urlConnection.getInputStream());
            //Permet de  décortiquer à partir d'un flux d'un certain nombre de methode indiqué
            Scanner scanner = new Scanner(in);
            // il faut remplir ce string avec le reponse du server (à voir avec GSON)
            String responseServer = null;

            // Prendre la reponser du server et mettre dans ce string
            responseServer= scanner.nextLine();
            Log.i("APPLICATION_BOULANGERIE", "Resultat du produit "+ responseServer);

            String result = "Ce produit est supprimé!!!";
            // Close connection
            in.close();
            return result;

        }
        catch (Exception e){
            Log.e("Exchange-JSON", " On n'a pas trouve http server ", e);

        } finally {
            if(urlConnection != null) urlConnection.disconnect();
        }

        return null;
    }


}