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

import com.application_boulangerie.data.Categorie;
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

public class PageCategorie extends AppCompatActivity {

    TextView tv_id_categorie;
    TextView tv_message_page_categorie;
    EditText edt_categorie_nom;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_page_categorie);
        // Appeller methode pour association view au java
        fct_associationViewAuJava();
        // Appeller methode pour affichage la page
        fct_remplirLaVuePageCategorie();
    }

    private void fct_associationViewAuJava() {
        this.tv_id_categorie = findViewById(R.id.tv_title_page_user);
        this.tv_message_page_categorie = findViewById(R.id.tv_message_page_user);
        this.edt_categorie_nom = findViewById(R.id.edt_categorie_nom);
    }

    //Methode pour afficher la page PageCategorie
    public void fct_remplirLaVuePageCategorie() {

        if (getIntent().hasExtra(NameExtra.CATEGORIE_ID.toString())) {
            // Prendre id du produit puis mettre dans 1 string
            String categorie_id = getIntent().getStringExtra(NameExtra.CATEGORIE_ID.toString());
            // Remettre id dans le textView
            tv_id_categorie.setText(categorie_id);
            // Remettre this id to type Integer
            int id = Integer.parseInt(categorie_id);

            // Get connection to HTTP server throw Thread
            String textUrl = MyURL.TITLE.toString() + MyURL.FINDCATEGORIEBYID.toString() + id;

            SendHttpRequestTaskAffichage t = new SendHttpRequestTaskAffichage();

            String[] params = new String[]{textUrl};
            t.execute(params);
        } else {
            tv_message_page_categorie.setText("Ajouter nouveau categorie");
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

                Categorie categorie = gson.fromJson(result, Categorie.class);
                affichageDuPage(categorie);

            }

        }

        public void affichageDuPage(Categorie categorie){

            String categorie_id = String.valueOf(categorie.getCategorie_id());
            String categorie_nom = categorie.getCategorie_nom();

            this.tv_id_categorie.setText(categorie_id);
            this.edt_categorie_nom.setText(categorie_nom);

        }


    public void act_ajouter_categorie(View view) {

        try {
            String categorie_nom = Fonctions.prendreText(edt_categorie_nom);

            if (!categorie_nom.isEmpty()) {
                Categorie categorie = new Categorie(categorie_nom);

                checkCategorieExiste(categorie);
            } else {
                tv_message_page_categorie.setText("Entrer nom de la categorie pour ajouter nouvelle categorie, svp!");
                tv_message_page_categorie.setTextColor(Color.RED);
            }

        } catch (Exception e){
            Log.e("APP-BOULANGERIE: ","Valeur des parametres est null " + e.toString());
            AppelToast.displayCustomToast(this, "Enter les informations, svp!");
        }

    }
    private void checkCategorieExiste(Categorie categorie) {
        String textUrl1 = MyURL.TITLE.toString()+ MyURL.LISTCATEGORIE.toString();
        String textUrl2 = MyURL.TITLE.toString()+ MyURL.AJOUTECATEGORIE.toString();
        String str_categorie = new Gson().toJson(categorie);
        SendHttpRequestTaskCheckCategorie t = new SendHttpRequestTaskCheckCategorie();

        String[] params = new String[]{textUrl1,textUrl2, str_categorie};
        t.execute(params);
    }

    private class SendHttpRequestTaskCheckCategorie extends AsyncTask<String, Void, String> {


        //out le code qui nécessite un temps d'exécution sera placé dans cette fonction.
        // Étant donné que cette fonction est exécutée dans un thread complètement séparé du thread d'interface utilisateur, vous n'êtes pas autorisé à mettre à jour l'interface ici.
        @RequiresApi(api = Build.VERSION_CODES.M)
        @Override
        protected String doInBackground(String... params) {
            String urlListCategorie = params[0];
            String urlAjouteCategorie = params[1];
            String str_categorie = params[2];
            Categorie categorie = new Gson().fromJson(str_categorie, Categorie.class);
            String result = null;
            try {
                result = MyHTTPConnection.startHttpRequestGET(urlListCategorie);

                List<Categorie> listCate = Fonctions.changefromJsonListCategorie(result);

                //Creer 1 objet pour stocker le résultat de comparaison
                Boolean check = false;

                // Creer l' objet pour stocker le valeur du categorie pour faire comparer dans le cas qu'on ecrit en minuscule ou majuscule
                String test_nom = categorie.getCategorie_nom().toUpperCase();

                // Faire une boucle pour savoir si cette categorie est existee
                for (Categorie c : listCate) {
                    if (test_nom.equals(c.getCategorie_nom().toUpperCase())) {
                        check = true;
                        break;
                    }
                }
                // si le resultat est false, on ajoute ce mp à la base de donnee
                if (!check) {
                    ajouterCategorie(urlAjouteCategorie, str_categorie);
                    result = "OK";
                }

            } catch (Exception e) {
                e.printStackTrace();
            }

            return result;
        }

        //la resultat de doInBackground est repris et afficher à la vue
        @RequiresApi(api = Build.VERSION_CODES.M)
        @Override
        protected void onPostExecute(String result) {

            if (!result.equals("OK")) {

                tv_message_page_categorie.setText("Cette categorie est existée!");
                tv_message_page_categorie.setTextColor(Color.RED);
            } else {
                tv_message_page_categorie.setText("Nouvelle categorie est crée!");
                tv_message_page_categorie.setTextColor(Color.BLUE);
            }


        }

        private void ajouterCategorie(String url, String str_categorie) {
            try {
                // prendre la reponse du server et mettre dans ce String
                String result = MyHTTPConnection.httpConnectionRequestPOST(url, str_categorie);
            } catch (Exception e) {
                e.printStackTrace();
                Log.e("APP-BOULANGERIE:", "Erreur: Ne peut pas connect au server");
            }

        }
    }


    public void act_suprimer_categorie(View view) {
        String checkID = tv_id_categorie.getText().toString();
        try {
            if (!Fonctions.isNumeric(checkID)) {
                tv_message_page_categorie.setText("Il n'y a pas de categorie pour supprimer, il faut choisir 1 categorie dans la liste pour supprimer");
                tv_message_page_categorie.setTextColor(Color.RED);
            } else {
                AppelDialog.showAlertDialogSuppimer(PageCategorie.this, tv_id_categorie, NameExtra.CATEGORIE);
            }
        }catch (Exception e){
            Log.e("APP-BOULANGERIE: ", "Erreur de supprimer la MP, pas de mp_id");
            AppelToast.displayCustomToast(this, "Il n'y a pas de MP pour supprimer, il faut choisir 1 MP dans la liste pour supprimer");
        }
    }

    public void act_modifier_categorie(View view) {
        String checkID = tv_id_categorie.getText().toString();
        if (!Fonctions.isNumeric(checkID)) {
            tv_message_page_categorie.setText("Il n'y a pas de Categorie pour modifier, il faut choisir 1 categorie dans la liste du page Menu");
            tv_message_page_categorie.setTextColor(Color.RED);
        }
        try {

            int id = Integer.parseInt(tv_id_categorie.getText().toString());
            String nom = Fonctions.prendreText(edt_categorie_nom);

            // Creer nouveau produit avec les valeurs pris dans la vue
            Categorie categorie = new Categorie(id,nom);

            // mise a jour le produit dans le database
            modifierCategorie(categorie);
            AppelToast.displayCustomToast(this, "Cette categorie est modifiée");

        } catch (Exception e){
            Log.e("APP-BOULANGERIE: ", "Valeur des parametres est null "+ e.toString());
            AppelToast.displayCustomToast(this, "Enter les informations, svp!");

        }

    }


    private void modifierCategorie(Categorie categorie) {

        String textUrl = MyURL.TITLE.toString()+ MyURL.MODIFIERCATEGORIE.toString()+categorie.getCategorie_id();

        // Convertir produit java en Json
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String json = gson.toJson(categorie);

        SendHttpRequestTaskOnUpdateCategorie t = new SendHttpRequestTaskOnUpdateCategorie();

        String[] params = new String[]{textUrl, json};
        t.execute(params);


    }
    private class SendHttpRequestTaskOnUpdateCategorie extends AsyncTask<String, Void, String> {


        //out le code qui nécessite un temps d'exécution sera placé dans cette fonction.
        // Étant donné que cette fonction est exécutée dans un thread complètement séparé du thread d'interface utilisateur, vous n'êtes pas autorisé à mettre à jour l'interface ici.

        //out le code qui nécessite un temps d'exécution sera placé dans cette fonction.
        // Étant donné que cette fonction est exécutée dans un thread complètement séparé du thread d'interface utilisateur, vous n'êtes pas autorisé à mettre à jour l'interface ici.
        @Override
        protected String doInBackground(String... params) {
            String url = params[0];
            String mp = params[1];

            String result = null;

            try {
                result = MyHTTPConnection.httpConnectionRequestPOST(url,mp);
            } catch (Exception e) {
                e.printStackTrace();
            }

            return result;
        }

        //la resultat de doInBackground est repris et afficher à la vue
        @Override
        protected void onPostExecute(String result) {

            String message = "Cette categorie est modifiée!";
            tv_message_page_categorie.setText(message);
            tv_message_page_categorie.setTextColor(Color.BLUE);
        }


    }


    public void act_retour_menu(View view) {

        AppelToast.displayCustomToast(this, "Retour à la page Menu");

        AppelIntent.appelIntentSimple(this, Menu.class);


    }
}