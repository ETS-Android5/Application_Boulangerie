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

import com.application_boulangerie.data.Utilisateur;
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

public class PageUser extends AppCompatActivity {

    TextView tv_message_page_user;
    TextView tv_title_page_user, tv_user_id;
    EditText edt_user_name,edt_user_pass;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_page_user);
        // Appeller fonction pour  association view au java
        fct_associationViewAuJava();
        // Appeller fonction pour affichage la page
        fct_remplirLaVuePageUser();
    }

    private void fct_associationViewAuJava(){
        this.tv_message_page_user = findViewById(R.id.tv_message_page_user);
        this.tv_title_page_user = findViewById(R.id.tv_title_page_user);
        this.tv_user_id = findViewById(R.id.tv_user_id);
        this.edt_user_name = findViewById(R.id.edt_user_name);
        this.edt_user_pass= findViewById(R.id.edt_user_pass);
    }

    //Methode pour afficher la page PageUser
    public void fct_remplirLaVuePageUser() {

        //  s'il y a extra, on va afficher information de l'Utilisateur
        if (getIntent().hasExtra(NameExtra.UTILISATEUR.toString())) {

            // Prendre id du produit puis mettre dans 1 string
            String user_id = getIntent().getStringExtra(NameExtra.UTILISATEUR.toString());
            // Remettre this id to type Integer
            int id = Integer.parseInt(user_id);

            // Get connection to HTTP server throw Thread
            String textUrl = MyURL.TITLE.toString() + MyURL.FINDUSERBYID.toString() + id;

           SendHttpRequestTaskAffichage t = new SendHttpRequestTaskAffichage();

            String[] params = new String[]{textUrl};
            t.execute(params);
        }
        else{
            tv_message_page_user.setText("Entrer information pour ajouter nouveau utilisateur");
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

           Utilisateur user = gson.fromJson(result, Utilisateur.class);

            // Appeller methode pour remplir la vue de la page
            affichageDuPage(user);

        }

    }

    // Methode pour remplir la vue de la page
    public void affichageDuPage(Utilisateur user ){
        String id = String.valueOf(user.getUser_id());
        String nom = user.getUser_nom();
        String pass = user.getUser_password();

        this.tv_user_id.setText(id);
        this.tv_title_page_user.setText(nom);
        this.edt_user_name.setText(nom);
        this.edt_user_pass.setText(pass);

    }

    // Methode onclick sur bouton "Modifier" pour modifier utilisateur
    public void act_modifier_user(View view) {

        // Verifier si le valeur dans tv_id_mp n'est pas entiere, donne une message
        String checkID = tv_user_id.getText().toString();
        if (!Fonctions.isNumeric(checkID)) {
            tv_message_page_user.setText("Il n'y a pas d'utilisateur pour modifier, il faut choisir 1 utilisateur dans la liste");
            tv_message_page_user.setTextColor(Color.RED);
        }
        // si oui, passer au prendre les informations et mettre au server
        try {
            int id = Integer.parseInt(tv_user_id.getText().toString());
            String nom = Fonctions.prendreText(edt_user_name);
            String pass = Fonctions.prendreText(edt_user_pass);
            if (id == 1 && !nom.equals("admin")) {
                tv_message_page_user.setText("Impossible de modifier cette utilisateur. Vous ne pouvez modifier que son mot de pass");
                tv_message_page_user.setTextColor(Color.RED);
            } else {
                // Creer nouveau utilisteur avec les valeurs pris dans la vue
                Utilisateur user = new Utilisateur(id, nom, pass);

                // mise a jour l'utilisateur dans le database
                modifierUser(user);
                AppelToast.displayCustomToast(this, "Cette utilisateur est modifié");
            }
        } catch (Exception e){
            Log.e("APP-BOULANGERIE: ", "Valeur des parametres est null "+ e.toString());
            AppelToast.displayCustomToast(this, "Enter les informations, svp!");

        }

    }

    // methode pour modifier MP au server
    private void modifierUser(Utilisateur user) {

        String textUrl1 = MyURL.TITLE.toString()+ MyURL.LISTUSERS;
        String textUrl2 = MyURL.TITLE.toString()+ MyURL.MODIFIERUSERS.toString()+user.getUser_id();

        // Convertir produit java en Json
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String json = gson.toJson(user);

        SendHttpRequestTaskOnUpdateUser t = new SendHttpRequestTaskOnUpdateUser();

        String[] params = new String[]{textUrl1, textUrl2, json};
        t.execute(params);


    }
    private class SendHttpRequestTaskOnUpdateUser extends AsyncTask<String, Void, String> {


        //out le code qui nécessite un temps d'exécution sera placé dans cette fonction.
        // Étant donné que cette fonction est exécutée dans un thread complètement séparé du thread d'interface utilisateur, vous n'êtes pas autorisé à mettre à jour l'interface ici.

        //out le code qui nécessite un temps d'exécution sera placé dans cette fonction.
        // Étant donné que cette fonction est exécutée dans un thread complètement séparé du thread d'interface utilisateur, vous n'êtes pas autorisé à mettre à jour l'interface ici.
        @Override
        protected String doInBackground(String... params) {
            String urlListUsers= params[0];
            String urlModifierUser = params[1];
            String str_user = params[2];
            Utilisateur user = new Gson().fromJson(str_user, Utilisateur.class);

            String result = null;

            try {
                result = MyHTTPConnection.startHttpRequestGET(urlListUsers);
               List<Utilisateur> listUsers = Fonctions.changefromJsonListUsers(result);
                Boolean check = false;

                // si les valeurs dans objet n'est pas changer, check = true pour demander modifier valeur avant valider
                for (Utilisateur u : listUsers)
                    if (user.getUser_id() == u.getUser_id()) {
                        check = user.getUser_nom().equals(u.getUser_nom())
                                && user.getUser_password().equals(u.getUser_password())
                                    && !user.getUser_nom().equals("admin");
                    }

                // si check = false, on peut modifier cette ingredient dans serveur
                if (!check){
                    mofidierUserAuServeur(urlModifierUser, str_user);
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
                String message = "Cette utilisateur est modifiée!";
                tv_message_page_user.setText(message);
                tv_message_page_user.setTextColor(Color.BLUE);
            } else if (result.equals("true")) {
                String message = "A modifier information pour modifier " + NameExtra.UTILISATEUR.toString();
                tv_message_page_user.setText(message);
                tv_message_page_user.setTextColor(Color.RED);
            }
        }

        private void mofidierUserAuServeur(String urlModifierUser, String str_user) throws Exception {
            String result = MyHTTPConnection.httpConnectionRequestPOST(urlModifierUser, str_user);
        }

    }

    // Methode onclick du button "Supprimer" pour supprimer Utilisateur
    public void act_suprimer_user(View view) {
        String checkID = tv_user_id.getText().toString();
        try {
            if (!Fonctions.isNumeric(checkID)) {
                tv_message_page_user.setText("Il n'y a pas d'utilisateur pour supprimer, il faut choisir 1 utilisateur dans la liste pour supprimer");
                tv_message_page_user.setTextColor(Color.RED);
            } else {
                int id = Integer.parseInt(checkID);
                if (id != 1) {
                    // Appeller AlertDialog pour confirmation la demande de supprimer
                    AppelDialog.showAlertDialogSuppimer(PageUser.this, tv_user_id, NameExtra.UTILISATEUR);
                } else {
                    tv_message_page_user.setText("Vous ne pouvez pas supprimer cette utilisateur. C'est ADMINISTRATEUR.");
                    tv_message_page_user.setTextColor(Color.RED);
                }
            }
        }catch (Exception e){
            Log.e("APP-BOULANGERIE: ", "Erreur de supprimer l'utilisateur', pas de utilisateur_id");
        }

    }
    // Methode onclick du button "Ajouter" pour creer nouveau Utilisateur
    public void act_ajouter_user(View view) {

        try {
            String nom = Fonctions.prendreText(edt_user_name);
            String pass = Fonctions.prendreText(edt_user_pass);
            if (!nom.isEmpty()&&!pass.isEmpty()) {
                Utilisateur user = new Utilisateur(nom, pass);

                // Appeller methode pour verifier si cette utilisateur est existe
                checkUserExiste(user);
            }else {
                tv_message_page_user.setText("Entrer nom et/ou mot de pass de l'utilisateur', svp!");
                tv_message_page_user.setTextColor(Color.RED);
            }
        } catch (Exception e){
            Log.e("APP-BOULANGERIE: ","Valeur des parametres est null " + e.toString());
            AppelToast.displayCustomToast(this, "Entrer les informations, svp!");
        }

    }

    // Methode pour verifier si cette MP est existe
    private void checkUserExiste(Utilisateur user) {
        String textUrl1 = MyURL.TITLE.toString()+ MyURL.LISTUSERS.toString();
        String textUrl2 = MyURL.TITLE.toString()+ MyURL.AJOUTEUSER.toString();
        String str_user = new Gson().toJson(user);
        // Connecter au server
       SendHttpRequestTaskCheckUser t = new SendHttpRequestTaskCheckUser();

        String[] params = new String[]{textUrl1,textUrl2, str_user};
        t.execute(params);
    }

    // Methode pour connecter au server
    private class SendHttpRequestTaskCheckUser extends AsyncTask<String, Void, String> {


        //out le code qui nécessite un temps d'exécution sera placé dans cette fonction.
        // Étant donné que cette fonction est exécutée dans un thread complètement séparé du thread d'interface utilisateur, vous n'êtes pas autorisé à mettre à jour l'interface ici.
        @RequiresApi(api = Build.VERSION_CODES.M)
        @Override
        protected String doInBackground(String... params) {
            String urlListUser = params[0];
            String urlAjouteUser = params[1];
            String str_user = params[2];
            Utilisateur user = new Gson().fromJson(str_user, Utilisateur.class);
            String result = null;
            try {
                result = MyHTTPConnection.startHttpRequestGET(urlListUser);

                List<Utilisateur> listUser = Fonctions.changefromJsonListUsers(result);

                //Creer 1 objet pour stocker le résultat de comparaison
                Boolean check = false;

                // Creer les objets pour stocker les valeurs du mp pour faire comparer dans le cas qu'on ecrit en minuscule ou majuscule
                String test_nom = user.getUser_nom().toUpperCase();

                // Faire une boucle pour savoir si cette matiere premiere est existee
                for (Utilisateur u : listUser) {
                    if (test_nom.equals(u.getUser_nom().toUpperCase())) {
                        check = true;
                    }
                }
                // si le resultat est false, on ajoute ce mp à la base de donnee
                if (!check) {
                    ajouterUser(urlAjouteUser, str_user);
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

                tv_message_page_user.setText("Cette utilisateur est existée!");
                tv_message_page_user.setTextColor(Color.RED);
            } else {
                tv_message_page_user.setText("Nouveau utilisateur est crée!");
                tv_message_page_user.setTextColor(Color.BLUE);
            }


        }

        // Methode pour creer nouvelle MP au server
        private void ajouterUser(String url, String str_user) {
            try {
                // prendre la reponse du server et mettre dans ce String
                String result = MyHTTPConnection.httpConnectionRequestPOST(url, str_user);
            } catch (Exception e) {
                e.printStackTrace();
                Log.e("APP-BOULANGERIE:", "Erreur: Ne peut pas connect au server");
            }
        }
    }

    // Methode onclick du button "retour" pour aller à la page Menu
    public void act_retour_menu(View view) {
        String userName = "admin";
        AppelIntent.appelIntentAvecExtraUser(this, Menu.class, userName);
    }
}