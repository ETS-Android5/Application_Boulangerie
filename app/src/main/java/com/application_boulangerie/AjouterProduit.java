package com.application_boulangerie;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
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

import java.util.ArrayList;
import java.util.List;

public class AjouterProduit extends AppCompatActivity {

    TextView tv_message;
    EditText edt_produit_nom_cree;
    EditText edt_produit_prix_cree;
    EditText edt_produit_quantite_cree;
    EditText edt_produit_categorie_cree;
    Button bt_aide;
    ListView listView_aide_listCategorie;
    List<Categorie> listCategorie = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ajouter_produit);

        fct_associationViewAuJava();

    }



    // On associe le XML avec le JAVA
    private void fct_associationViewAuJava() {
        this.tv_message = findViewById(R.id.tv_message);
        this.edt_produit_nom_cree = findViewById(R.id.edt_produit_nom_ajouter);
        this.edt_produit_prix_cree = findViewById(R.id.edt_produit_prix_ajouter);
        this.edt_produit_quantite_cree = findViewById(R.id.edt_produit_quantite_ajouter);
        this.edt_produit_categorie_cree = findViewById(R.id.edt_produit_categorie_ajouter);
        this.bt_aide = findViewById(R.id.bt_aide);
    }

    public void act_retour_pageListProduit(View view) {
        AppelToast.displayCustomToast(this, "Retour à la page Liste des produits");
        AppelIntent.appelIntentSimple(this, PageListeProduits.class);
    }

    @SuppressLint("SetTextI18n")
    public void act_valide_ajouter_produit(View view) {

        try {
            String nom = Fonctions.prendreText(edt_produit_nom_cree);
            double prix = Double.parseDouble(Fonctions.prendreText(edt_produit_prix_cree));
            int quantite = Integer.parseInt(Fonctions.prendreText(edt_produit_quantite_cree));
            int categorie_id = Integer.parseInt(Fonctions.prendreText(edt_produit_categorie_cree));

            Produit produit = new Produit(nom, prix, quantite);
            ajoutProduit(produit, categorie_id);

        } catch (Exception e){
            Log.e("APP-BOULANGERIE: ","Valeur des parametres est null " + e.toString());
            AppelToast.displayCustomToast(this, "Entrer les informations, svp!");
        }
    }

    private void ajoutProduit(Produit produit, int categorie_id) {


        String textUrl1 = MyURL.TITLE.toString()+ MyURL.AJOUTEPRODUIT.toString() +categorie_id;
        String textUrl2 = MyURL.TITLE.toString() + MyURL.LISTPRODUIT.toString();

        // Convertir produit java en Json
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String json = gson.toJson(produit);

        // Methode d'appel les fonctions qui marche dans UI Thread
        SendHttpRequestTaskCreate t = new SendHttpRequestTaskCreate();

        // Declarer les paramettre utilisés dans ce methode
        String[] params = new String[]{textUrl1, json, textUrl2};

        //Executer methodes
        t.execute(params);

    }

    private class SendHttpRequestTaskCreate extends AsyncTask<String, Void, String> {

        //out le code qui nécessite un temps d'exécution sera placé dans cette fonction.
        // Étant donné que cette fonction est exécutée dans un thread complètement séparé du
        // thread d'interface utilisateur, vous n'êtes pas autorisé à mettre à jour l'interface ici.
        @Override
        protected String doInBackground(String... params) {
            String urlAjouterProduit = params[0];
            String str_produit = params[1];
            Produit produit = new Gson().fromJson(str_produit,Produit.class);
            String urlListProduit = params[2];
            String result = null;

            try {
                // prendre la reponse du server et mettre dans ce String
                result = MyHTTPConnection.startHttpRequestGET(urlListProduit);

                List<Produit> listProduit = Fonctions.changefromJsonListProduit(result);

                Boolean check = false;
                for (Produit p : listProduit) {
                    String test = p.getProduit_nom().toUpperCase();
                    if(test.equals(produit.getProduit_nom().toUpperCase())){
                        check = true;
                        break;
                    }
                }

                if (!check){
                    creeProduit(urlAjouterProduit,str_produit);
                    result = "OK";
                }

            } catch (Exception e) {
                e.printStackTrace();
             Log.e("APP-BOULANGERIE:", "Erreur: Ne peut pas connect au server");
            }

            return result;
        }
        //la resultat de doInBackground est repris et afficher à la vue
        @RequiresApi(api = Build.VERSION_CODES.M)
        @Override
        protected void onPostExecute( String result ) {

            if (!result.equals("OK")) {

                tv_message.setText("Ce produit est existée!");
                tv_message.setTextColor(Color.RED);
            } else {
                tv_message.setText("Nouveau produit est créé ");
                tv_message.setTextColor(Color.BLUE);
                AppelToast.displayCustomToast(AjouterProduit.this,"Nouveau produit est créé");
            }


        }
        private void creeProduit(String url, String produit) {
            try {
                MyHTTPConnection.httpConnectionRequestPOST(url,produit);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }


    }

    // Methode onclick sur bouton "aide" pour afficher la liste des categories et permettre utilisateur choisir 1 categorie
    public void act_affichage_aide_listCategorie(View view) {


        AppelToast.displayCustomToast(this, "Ouvrir la liste des categories");
        // Appeller la methode affichage liste des categorie et permettre utilisateur choisir categorie
        prendrelistCategorieHTTP();


    }

    // methode affichage liste des categorie et permettre utilisateur choisir categorie
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

            // On converti 1 objet categorie Java en JSON
            AjouterProduit.this.listCategorie = Fonctions.changefromJsonListCategorie(result);

            // Appeller AlerDialog pour afficher la liste avec option de select categorie
            AppelDialog.showAlertDialogListCategorie(AjouterProduit.this, listCategorie, edt_produit_categorie_cree);

            // ArrayAdapter a utilisé à afficher le ListView avec des ListItem simple
            // android.R.layout.simple_list_item_1 est une disposition prédéfinie constante d'Android.
            //ArrayAdapter arrayAdapter_listCours = new ArrayAdapter<Categorie>(AjouterProduit.this, android.R.layout.simple_gallery_item,listCategorie);

            //listView_aide_listCategorie.setAdapter(arrayAdapter_listCours);


        }

    }

}