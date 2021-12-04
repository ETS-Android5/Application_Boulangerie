package com.application_boulangerie;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.widget.TextViewCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import com.application_boulangerie.data.Produit;
import com.application_boulangerie.extrafonctions.AppelIntent;
import com.application_boulangerie.extrafonctions.AppelToast;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class PageListeProduits extends AppCompatActivity {

   TableLayout tl;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_page_liste_produits);

        // Association view au java
        this.tl = findViewById(R.id.tl_list_produit);

        // Get connection to HTTP server throw Thread
        String textUrl= "http://192.168.43.189:8080/Bouglangerie/webapi/produit/getAllProduits";
        String methode = "GET";

        SendHttpRequestTask t = new SendHttpRequestTask();

        String[] params = new String[]{textUrl, methode};
        t.execute(params);
    }

    private class SendHttpRequestTask extends AsyncTask<String, Void, String> {


        //out le code qui nécessite un temps d'exécution sera placé dans cette fonction.
        // Étant donné que cette fonction est exécutée dans un thread complètement séparé du thread d'interface utilisateur, vous n'êtes pas autorisé à mettre à jour l'interface ici.
        @RequiresApi(api = Build.VERSION_CODES.M)
        @Override
        protected  String  doInBackground(String... params) {
            String url = params[0];
            String methode = params[1];

            String result = null;
            try {
                result = httpConnection(url, methode);
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

            // cas 2: transformer la string en tableau Json (pour methode getAll)
            // Definir type pour List<Produit> pour get valeur from server et puis transfert valeur type Json to type List<Produi>
            Type type = new TypeToken<List<Produit>>() {}.getType();
            List<Produit> listProduit = gson.fromJson(result,type);

            for (Produit produit : listProduit) {

                String idProduit = String.valueOf(produit.getProduit_id());
                String nomProduit = produit.getProduit_nom();
                String prixProduit = String.valueOf(produit.getProduit_prix());
                String quantiteProduit = String.valueOf(produit.getProduit_quantite());

                // appel methode pour ajouter nouvelle ligne a la table
                ajouterRow(tl, idProduit, nomProduit, prixProduit, quantiteProduit, produit);
            }

        }



    }

    // Methode pour connect HTTP Server
    @RequiresApi(api = Build.VERSION_CODES.M)
    public String  httpConnection(String textUrl, String methode) throws Exception {

                HttpURLConnection urlConnection = null;
                try {
                    // get connect to HTTP server
                    URL url = new URL(textUrl);
                    urlConnection= (HttpURLConnection) url.openConnection();
                    urlConnection.setRequestMethod(methode);

                    // Creer 1 valeur type InputSteam pour recuper la flux
                    InputStream in = new BufferedInputStream( urlConnection.getInputStream());
                    //Permet de  décortiquer à partir d'un flux d'un certain nombre de methode indiqué
                    Scanner scanner = new Scanner(in);
                    // Creer 1 chaine type String pour stocker la reponse du server
                    String responseServer = null;

                    // mettre la reponse du sever au ce String
                    responseServer= scanner.nextLine();

                    in.close();

                    return responseServer;
                }
                catch (Exception e){
                    Log.e("Exchange-JSON", " On n'a pas trouve http server ", e);

                } finally {
                    if(urlConnection != null) urlConnection.disconnect();
                }
            return null;
    }

    // Methode pour appeller au AjouterProduit activity quand on clic bouton ADD (+)
    public void act_ajouter_produit(View view) {
       // Appeller 1 toast pour information de ce button
        AppelToast.displayCustomToast(this, "Aller à la page Ajouter nouveau produit");
        // Apppler 1 simple intent  pour vers la page AjouterProduit
        AppelIntent.appelIntentSimple(this,AjouterProduit.class);
    }

    // Methode pour appeller MainActivity quand on clic boutton "retour"
    public void act_retour(View view) {
        // Appeller 1 toast pour information de ce button
        AppelToast.displayCustomToast(this, "Retour au page d'acceuil");
        // Apppler 1 simple intent  pour vers la page MainActivity
        AppelIntent.appelIntentSimple(this,MainActivity.class);

    }

    //Methode pour ajouter la nouvelle ligne à la table avec les informations de produit
    @RequiresApi(api = Build.VERSION_CODES.M)
    public void ajouterRow(TableLayout tl,
                                  String idProduit,
                                  String nomProduit,
                                  String prixProduit,
                                  String quantiteProduit, Produit produit ){
      
        // Create a new row to be added
        TableRow tr = new TableRow(this);
        tr.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.MATCH_PARENT));

        //Creer 4 TextView
        TextView tvNewIdProduit = new TextView(this) ;
        TextView tvNewNomProduit= new TextView(this);
        TextView tvNewPrixProduit = new TextView(this);
        TextView tvNewQuantiteProduit =new TextView(this);
        // TextView tvNewIngredientProduit = new TextView(this);

        // Mettre style pour les textViews
        tvNewIdProduit.setTextAppearance(R.style.TableColumStyle0);
        tvNewNomProduit.setTextAppearance(R.style.TableColumStyle0);
        tvNewPrixProduit.setTextAppearance(R.style.TableColumStyle0);
        tvNewQuantiteProduit.setTextAppearance(R.style.TableColumStyle0);
        //  tvNewIngredientProduit.setTextAppearance((R.style.TableColumStyle0));

        // Mettre valeur pour les textViews
        tvNewIdProduit.setText(idProduit);
        tvNewNomProduit.setText(nomProduit);
        tvNewQuantiteProduit.setText(quantiteProduit);
        tvNewPrixProduit.setText(prixProduit);
        // tvNewIngredientProduit.setText(ingredientProduit);

        //Creer 1 button pour aller au page PageProduit avec 1 seul clic
        Button bt_Select_Produit = new Button(this);

        //set Style, text pour button
        bt_Select_Produit.setTextAppearance(R.style.TableButonStyle);
        bt_Select_Produit.setText("Select");
        bt_Select_Produit.setOnClickListener(new View.OnClickListener() {
            // methode pour aller au PageProduit
            @Override
            public void onClick(View v) {
                String produit_id = String.valueOf(produit.getProduit_id());
                String produit_nom = produit.getProduit_nom();
                String produit_prix = String.valueOf(produit.getProduit_prix());
                String produit_quantite = String.valueOf(produit.getProduit_quantite());
                String produit_description_ingredients = produit.getProduit_description_ingredients();

                // On crée un Intent (Une intention)
                Intent intent = new Intent(PageListeProduits.this, PageProduit.class);


                intent.putExtra("produit_id", produit_id);
                intent.putExtra("produit_nom", produit_nom);
                intent.putExtra("produit_prix", produit_prix);
                intent.putExtra("produit_quantite", produit_quantite);
                intent.putExtra("produit_description_ingredients", produit_description_ingredients);

                //  intent.putExtra("image_produit",imageProduit);

                //lancer Intent
                startActivity(intent);
            }
        });


        //Ajouter les Textviews et checkbox a la ligne
        tr.addView(tvNewIdProduit);
        tr.addView(tvNewNomProduit);
        tr.addView(tvNewPrixProduit);
        tr.addView(tvNewQuantiteProduit);
        //   tr.addView(tvNewIngredientProduit);
        tr.addView(bt_Select_Produit);

        // Ajouter row dans TableLayout.
        tl.addView(tr);
    }


}