package com.application_boulangerie;

import android.content.Intent;
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

public class ModifierProduit extends AppCompatActivity {

    TextView tv_message_modifier;
    EditText edt_produit_id_update;
    EditText edt_produit_nom_update;
    EditText edt_produit_prix_update;
    EditText edt_produit_quantite_update;
    EditText edt_produit_ingredient_update;

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
                                            edt_produit_ingredient_update);



    }

    // On associe le XML avec le JAVA
    private void fct_associationViewAuJava(){
        this.tv_message_modifier = findViewById(R.id.tv_message_modifier);
        this.edt_produit_id_update = findViewById(R.id.edt_produit_id_update);
        this.edt_produit_nom_update = findViewById(R.id.edt_produit_nom_update);
        this.edt_produit_prix_update = findViewById(R.id.edt_produit_prix_update);
        this.edt_produit_quantite_update = findViewById(R.id.edt_produit_quantite_update);
        this.edt_produit_ingredient_update = findViewById(R.id.edt_produit_ingredient_update);
    }

    public void act_valide_update_produit(View view) {

      AppelToast.displayCustomToast(this, "Ce produit est modifié");

        int id = Integer.parseInt(ajouteText(edt_produit_id_update));
        String nom = ajouteText(edt_produit_nom_update);
        Double prix = Double.valueOf(ajouteText(edt_produit_prix_update));
        int quantite = Integer.valueOf(ajouteText(edt_produit_quantite_update ));
        String ingredient = ajouteText(edt_produit_ingredient_update);

        // Creer nouveau produit avec les valeurs pris dans la vue
        this.produit = new Produit(id, nom, prix,quantite,ingredient);

        // mise a jour le produit dans le database
        modifierProduit(this.produit, tv_message_modifier);

       // modifierProduit(produit);

    }

    private void modifierProduit(Produit produit, TextView tv_message_modifier) {

        String textUrl = "http://192.168.43.190:8080/Bouglangerie/webapi/produit/updateProduit/"+produit.getProduit_id();
        String methode = "POST";

        // Convertir produit java en Json
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String json = gson.toJson(produit);

        SendHttpRequestTaskOnUpdateProduit t = new SendHttpRequestTaskOnUpdateProduit();

        String[] params = new String[]{textUrl, methode, json};
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
        String methode = params[1];
        String produit = params[2];

        String result = null;

        try {
            result = httpConnection(url,methode, produit);
        } catch (Exception e) {
            e.printStackTrace();
            AppelToast.displayCustomToast(ModifierProduit.this, "Ne peut pas connect au server");
        }

        return result;
    }

    //la resultat de doInBackground est repris et afficher à la vue
    @Override
    protected void onPostExecute(String result) {
        //Afficher message de validation
        tv_message_modifier.setText(result);
       tv_message_modifier.setTextColor(Color.BLUE);
    }

}
    // Methode pour connect au server
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

            String result = "Ce produit est modifié";

            return result;

        } catch (Exception e) {
            Log.e("APPLICATION_BOULANGERIE", " On n'a pas trouve http server ", e);

        } finally {
            if (urlConnection != null) urlConnection.disconnect();
        }
        return null;
    }


   public static String ajouteText(EditText edT) {
        String t= null;

        t = edT.getText().toString();

        return t;
    }

    public void fct_remplirLaVuePageModifierProduit(       EditText edt_produit_id_update,
                                                           EditText edt_produit_nom_update,
                                                           EditText edt_produit_prix_update,
                                                           EditText edt_produit_quantite_update,
                                                           EditText edt_produit_ingredient_update) {
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
            if (getIntent().hasExtra("produit_description_ingredients") == false) {
                throw new Exception("Aucun Extra donne pour l'ingredient");
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
        String produit_description_ingredients = getIntent().getStringExtra("produit_description_ingredients");
        edt_produit_ingredient_update.setText(produit_description_ingredients);

        int id = Integer.parseInt(produit_id);
      //  String nom = produit_nom;
        Double prix = Double.valueOf(produit_prix);
        int quantite = Integer.valueOf(produit_quantite);
       //String ingredient = produit_description_ingredients;

        // Creer nouveau produit avec les valeurs pris dans la vue
        this.produit = new Produit(id, produit_nom, prix,quantite,produit_description_ingredients);
    }

    public void act_retour_page_list_produit(View view) {

        AppelToast.displayCustomToast(this,"Retour a la page de produit");

        AppelIntent.appelIntentAvecExtraPageProduit(this, PageProduit.class, this.produit);


    }
   }