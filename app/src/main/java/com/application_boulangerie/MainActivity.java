package com.application_boulangerie;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.application_boulangerie.adapter.CustomDialog;
import com.application_boulangerie.data.Utilisateur;
import com.application_boulangerie.utils.AppelDialog;
import com.application_boulangerie.utils.AppelIntent;
import com.application_boulangerie.utils.AppelToast;
import com.application_boulangerie.utils.Fonctions;
import com.application_boulangerie.utils.MyHTTPConnection;
import com.application_boulangerie.utils.MyURL;
import com.google.gson.Gson;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    //Appeller intent simple
    public void connexion(View view) {

       // AppelDialog.showProgressDialog(this);

        CustomDialog.ConnectionListener listener = new CustomDialog.ConnectionListener() {
            @Override
            public void userEntered(String fullName, String password) {

                Utilisateur user = new Utilisateur(fullName, password);
                // Appelle methode pour verifier utilisateur
                verifierUtilisateur(user);

            }

        };
        final CustomDialog dialog = new CustomDialog(this, listener);

        dialog.show();

    }

    private void verifierUtilisateur(Utilisateur user) {

        String textUrl1 = MyURL.TITLE.toString()+ MyURL.LISTUSERS.toString();
        String str_user = new Gson().toJson(user);
        SendHttpRequestTaskCheckUser t = new SendHttpRequestTaskCheckUser();

        String[] params = new String[]{textUrl1, str_user};
        t.execute(params);

    }
    private class SendHttpRequestTaskCheckUser extends AsyncTask<String, Void, String> {


        //out le code qui nécessite un temps d'exécution sera placé dans cette fonction.
        // Étant donné que cette fonction est exécutée dans un thread complètement séparé du thread d'interface utilisateur, vous n'êtes pas autorisé à mettre à jour l'interface ici.
        @RequiresApi(api = Build.VERSION_CODES.M)
        @Override
        protected String doInBackground(String... params) {
            String urlListUsers = params[0];
            String str_user = params[1];
            Utilisateur user = new Gson().fromJson(str_user, Utilisateur.class);
            String result = null;
            try {

                result = MyHTTPConnection.startHttpRequestGET(urlListUsers);

                List<Utilisateur> listUsers = Fonctions.changefromJsonListUsers(result);

                // Creer l' objet pour stocker le valeur du categorie pour faire comparer dans le cas qu'on ecrit en minuscule ou majuscule
                String test_nom = user.getUser_nom();
                String test_pass = user.getUser_password();

                // Faire une boucle pour savoir si cette utilisateur est existee
                for (Utilisateur u: listUsers) {
                    if (test_nom.equals(u.getUser_nom())&&test_pass.equals(u.getUser_password())) {
                        result = "OK";
                       break;
                    }
                }
                return result;
            } catch (Exception e) {
                e.printStackTrace();
            }
            return result;
        }

        //la resultat de doInBackground est repris et afficher à la vue
        @RequiresApi(api = Build.VERSION_CODES.M)
        @Override
        protected void onPostExecute(String result) {

            if (result == "OK") {
                AppelIntent.appelIntentSimple(MainActivity.this, Menu.class);
            } else{
                AppelToast.displayCustomToast(MainActivity.this, "Verifier votre nom et mot de pass");
            }
        }
    }
}