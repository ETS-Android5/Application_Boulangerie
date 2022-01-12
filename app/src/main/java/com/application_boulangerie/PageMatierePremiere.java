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

public class PageMatierePremiere extends AppCompatActivity {

    TextView tv_message_page_mp;
    TextView tv_id_mp = null;
    EditText edt_mp_nom, edt_mp_prix, edt_mp_quantite, edt_mp_unite, edt_mp_marque;
    MatierePremiere matierePremiere;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_page_matiere_premiere);

        fct_associationViewAuJava();
        //Appelle methode pour afficher la page PageMP
        fct_remplirLaVuePageMP();
    }

    private void fct_associationViewAuJava(){
        this.tv_message_page_mp = findViewById(R.id.tv_message_page_mp);
        this.tv_id_mp = findViewById(R.id.tv_id_mp);
        this.edt_mp_nom = findViewById(R.id.edt_mp_nom);
        this.edt_mp_prix = findViewById(R.id.edt_user_name);
        this.edt_mp_quantite= findViewById(R.id.edt_user_pass);
        this.edt_mp_unite = findViewById(R.id.edt_ingredient_unite);
        this.edt_mp_marque = findViewById(R.id.edt_mp_marque);

    }


    //Methode pour afficher la page PageMP
    public void fct_remplirLaVuePageMP() {

        //  s'il y a extra, on va afficher information de la MP
            if (getIntent().hasExtra(NameExtra.MP_ID.toString())) {

                // Prendre id du produit puis mettre dans 1 string
                String mp_id = getIntent().getStringExtra(NameExtra.MP_ID.toString());
                // Remettre this id to type Integer
                int id = Integer.parseInt(mp_id);

                // Get connection to HTTP server throw Thread
                String textUrl = MyURL.TITLE.toString() + MyURL.FINDMPBYID + id;

                SendHttpRequestTaskAffichage t = new SendHttpRequestTaskAffichage();

                String[] params = new String[]{textUrl};
                t.execute(params);
            }
            else{
                tv_message_page_mp.setText("Entrer information pour ajouter nouvelle Matière Première");
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

            MatierePremiere mp = gson.fromJson(result, MatierePremiere.class);

            // Appeller methode pour remplir la vue de la page
            affichageDuPage(mp);

        }

    }

    // Methode pour remplir la vue de la page
    public void affichageDuPage(MatierePremiere mp ){
        String id = String.valueOf(mp.getMp_id());
        String nom = mp.getMp_nom();
        String prix = String.valueOf(mp.getMp_prix());
        String quantite = String.valueOf(mp.getMp_quantite());
        String unite = mp.getMp_unite();
        String marque = mp.getMp_marque();

        this.tv_id_mp.setText(id);
        this.edt_mp_nom.setText(nom);
        this.edt_mp_prix.setText(prix);
        this.edt_mp_quantite.setText(quantite);
        this.edt_mp_unite.setText(unite);
        this.edt_mp_marque.setText(marque);

        if (mp.getMp_quantite() <= 0){
            AppelDialog.showAlerDialogZero(this, NameExtra.MP_ID);
            this.edt_mp_quantite.setTextColor(Color.RED);
        }

    }

    // Methode onclick du button "Supprimer" pour supprimer MP
    public void act_suprimer_mp(View view) {
        String checkID = tv_id_mp.getText().toString();
        try {
            if (!Fonctions.isNumeric(checkID)) {
                tv_message_page_mp.setText("Il n'y a pas de MP pour supprimer, il faut choisir 1 MP dans la liste pour supprimer");
                tv_message_page_mp.setTextColor(Color.RED);
            } else {
            // Appeller AlertDialog pour confirmation la demande de supprimer
                AppelDialog.showAlertDialogSuppimer(PageMatierePremiere.this, tv_id_mp, NameExtra.MP);
            }
            }catch (Exception e){
                    Log.e("APP-BOULANGERIE: ", "Erreur de supprimer la MP, pas de mp_id");
                    AppelToast.displayCustomToast(this, "Il n'y a pas de MP pour supprimer, il faut choisir 1 MP dans la liste pour supprimer");
        }

    }

    // Methode onclick du button "Ajouter" pour creer nouvelle MP
    public void act_ajouterMP(View view) {


        try {
            String nom = Fonctions.prendreText(edt_mp_nom);

            double prix = Double.parseDouble(Fonctions.prendreText(edt_mp_prix));
            int quantite = Integer.parseInt(Fonctions.prendreText(edt_mp_quantite));
            String unite = Fonctions.prendreText(edt_mp_unite);
            String marque = Fonctions.prendreText(edt_mp_marque);
            if (!nom.isEmpty()&&!unite.isEmpty()) {
                MatierePremiere mp = new MatierePremiere(nom, prix, quantite, unite, marque);

                // Appeller methode pour verifier si cette MP est existe
                checkMPexiste(mp);
            }else {
                tv_message_page_mp.setText("Entrer nom et/ou unité de la Matière Première, svp!");
                tv_message_page_mp.setTextColor(Color.RED);
            }
        } catch (Exception e){
            Log.e("APP-BOULANGERIE: ","Valeur des parametres est null " + e.toString());
            AppelToast.displayCustomToast(this, "Entrer les informations, svp!");
        }

    }

    // Methode pour verifier si cette MP est existe
    private void checkMPexiste(MatierePremiere mp) {
        String textUrl1 = MyURL.TITLE.toString()+ MyURL.LISTMP.toString();
        String textUrl2 = MyURL.TITLE.toString()+ MyURL.AJOUTEMP.toString();
        String str_mp = new Gson().toJson(mp);
        // Connecter au server
        SendHttpRequestTaskCheckMP t = new SendHttpRequestTaskCheckMP();

        String[] params = new String[]{textUrl1,textUrl2, str_mp};
        t.execute(params);
    }

    // Methode pour connecter au server
    private class SendHttpRequestTaskCheckMP extends AsyncTask<String, Void, String> {


        //out le code qui nécessite un temps d'exécution sera placé dans cette fonction.
        // Étant donné que cette fonction est exécutée dans un thread complètement séparé du thread d'interface utilisateur, vous n'êtes pas autorisé à mettre à jour l'interface ici.
        @RequiresApi(api = Build.VERSION_CODES.M)
        @Override
        protected  String  doInBackground(String... params) {
            String urlListMP = params[0];
            String urlAjouteMP = params[1];
            String str_mp = params[2];
            MatierePremiere mp = new Gson().fromJson(str_mp, MatierePremiere.class);
            String result = null;
            try {
                result = MyHTTPConnection.startHttpRequestGET(urlListMP);

                List<MatierePremiere> listMp = Fonctions.changefromJsonListMP(result);

                //Creer 1 objet pour stocker le résultat de comparaison
                Boolean check = false;

                // Creer les objets pour stocker les valeurs du mp pour faire comparer dans le cas qu'on ecrit en minuscule ou majuscule
                String test_nom= mp.getMp_nom().toUpperCase();
                String test_marques = mp.getMp_marque().toUpperCase();

                // Faire une boucle pour savoir si cette matiere premiere est existee
                for (MatierePremiere matiereP: listMp) {
                    if (test_nom.equals(matiereP.getMp_nom().toUpperCase())&&test_marques.equals(matiereP.getMp_marque().toUpperCase())) {
                        check = true;
                    }
                }
                // si le resultat est false, on ajoute ce mp à la base de donnee
                if(!check){
                    ajouterMP(urlAjouteMP, str_mp);
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
        protected void onPostExecute( String result ) {

            if (!result.equals("OK")) {

                tv_message_page_mp.setText("Cette Matière première est existée!");
                tv_message_page_mp.setTextColor(Color.RED);
            } else {
                tv_message_page_mp.setText("Nouvelle Matière première est crée!");
                tv_message_page_mp.setTextColor(Color.BLUE);
            }


        }

        // Methode pour creer nouvelle MP au server
        private void ajouterMP(String url, String mp){
            try {
                // prendre la reponse du server et mettre dans ce String
                String result = MyHTTPConnection.httpConnectionRequestPOST(url,mp);
            } catch (Exception e) {
                e.printStackTrace();
                Log.e("APP-BOULANGERIE:", "Erreur: Ne peut pas connect au server");
            }

        }

    }

    // Methode onclic sur button "Modifier" pour modifier 1 MP
    public void act_modifier_mp(View view) {

        // Verifier si le valeur dans tv_id_mp n'est pas entiere, donne une message
        String checkID = tv_id_mp.getText().toString();
        if (!Fonctions.isNumeric(checkID)) {
            tv_message_page_mp.setText("Il n'y a pas de MP pour modifier, il faut choisir 1 MP dans la liste");
            tv_message_page_mp.setTextColor(Color.RED);
        }
        // si oui, passer au prendre les informations et mettre au server
        try {

                int id = Integer.parseInt(tv_id_mp.getText().toString());
                String nom = Fonctions.prendreText(edt_mp_nom);
                Double prix = Double.valueOf(Fonctions.prendreText(edt_mp_prix));
                int quantite = Integer.valueOf(Fonctions.prendreText(edt_mp_quantite));
                String unite = Fonctions.prendreText(edt_mp_unite);
                String marque = Fonctions.prendreText(edt_mp_marque);


                // Creer nouveau produit avec les valeurs pris dans la vue
                this.matierePremiere = new MatierePremiere(id, nom, prix, quantite, unite, marque);

                // mise a jour le produit dans le database
                modifierMP(this.matierePremiere);
                AppelToast.displayCustomToast(this, "Ce produit est modifié");

        } catch (Exception e){
            Log.e("APP-BOULANGERIE: ", "Valeur des parametres est null "+ e.toString());
            AppelToast.displayCustomToast(this, "Enter les informations, svp!");

        }

    }

    // methode pour modifier MP au server
    private void modifierMP(MatierePremiere matierePremiere) {

        String textUrl1 = MyURL.TITLE.toString()+ MyURL.LISTMP.toString();
        String textUrl2 = MyURL.TITLE.toString()+ MyURL.MODIFIERMP.toString()+matierePremiere.getMp_id();

        // Convertir produit java en Json
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String json = gson.toJson(matierePremiere);

        SendHttpRequestTaskOnUpdateMP t = new SendHttpRequestTaskOnUpdateMP();

        String[] params = new String[]{textUrl1, textUrl2, json};
        t.execute(params);


    }
    private class SendHttpRequestTaskOnUpdateMP extends AsyncTask<String, Void, String> {


        //out le code qui nécessite un temps d'exécution sera placé dans cette fonction.
        // Étant donné que cette fonction est exécutée dans un thread complètement séparé du thread d'interface utilisateur, vous n'êtes pas autorisé à mettre à jour l'interface ici.

        //out le code qui nécessite un temps d'exécution sera placé dans cette fonction.
        // Étant donné que cette fonction est exécutée dans un thread complètement séparé du thread d'interface utilisateur, vous n'êtes pas autorisé à mettre à jour l'interface ici.
        @Override
        protected String doInBackground(String... params) {
            String urlListMP= params[0];
            String urlModifierMP = params[1];
            String str_mp = params[2];
            MatierePremiere mp = new Gson().fromJson(str_mp, MatierePremiere.class);

            String result = null;

            try {
                result = MyHTTPConnection.startHttpRequestGET(urlListMP);
                List<MatierePremiere> listMP = Fonctions.changefromJsonListMP(result);
                Boolean check = false;

                // si les valeurs dans objet n'est pas changer, check = true pour demander modifier valeur avant valider
                for (MatierePremiere matierePremiere : listMP)
                    if (matierePremiere.getMp_id() == mp.getMp_id()) {
                        check = matierePremiere.getMp_nom().equals(mp.getMp_nom())
                                && matierePremiere.getMp_prix().equals(mp.getMp_prix())
                                && matierePremiere.getMp_quantite() == mp.getMp_quantite()
                                && matierePremiere.getMp_unite().equals(mp.getMp_unite())
                                && matierePremiere.getMp_marque().equals(mp.getMp_marque());
                    }

                // si check = false, on peut modifier cette ingredient dans serveur
                if (!check){
                    mofidierMPAuServeur(urlModifierMP, str_mp);
                }

                return result = check.toString();

            } catch (Exception e) {
                e.printStackTrace();
            }

            return result;
        }

        //la resultat de doInBackground est repris et afficher à la vue
        @Override
        protected void onPostExecute(String result) {
            if (result.equals("false")) {
                String message = "Cette matière première est modifiée!";
                tv_message_page_mp.setText(message);
                tv_message_page_mp.setTextColor(Color.BLUE);
            } else if (result.equals("true")) {
                String message = "A modifier information pour modifier " + NameExtra.MP.toString();
                tv_message_page_mp.setText(message);
                tv_message_page_mp.setTextColor(Color.RED);
            }
        }

        private void mofidierMPAuServeur(String urlModifierMP, String str_mp) throws Exception {
            String result = MyHTTPConnection.httpConnectionRequestPOST(urlModifierMP, str_mp);
        }

    }

    // Methode onclick sur bouton "Retour" pour retour du list MP
    public void act_retour_list_mp(View view) {

        AppelToast.displayCustomToast(this, "Retour à la page Liste des MP");
        AppelIntent.appelIntentSimple(this, PageListeMP.class);
    }

}