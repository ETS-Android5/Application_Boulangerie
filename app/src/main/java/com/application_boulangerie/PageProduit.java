package com.application_boulangerie;

import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.application_boulangerie.data.Ingredient;
import com.application_boulangerie.data.MatierePremiere;
import com.application_boulangerie.data.Produit;
import com.application_boulangerie.utils.AppelDialog;
import com.application_boulangerie.utils.AppelIntent;
import com.application_boulangerie.utils.AppelToast;
import com.application_boulangerie.utils.Fonctions;
import com.application_boulangerie.utils.MyHTTPConnection;
import com.application_boulangerie.utils.MyURL;
import com.application_boulangerie.utils.NameExtra;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;


public class PageProduit extends AppCompatActivity {

    TextView tv_id, tv_Produit, tv_ingredient,tv_prix, tv_quantite;
    ImageView imaProduit;
    Produit produit;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_page_produit);
        // Appeller methode pour association view au java
        fct_associationViewAuJava();
        // Appeller methode pour remplir la vue du produit
        fct_remplirLaVuePageProduit();

    }

    // methode pour association view au java
    private void fct_associationViewAuJava(){
        this.tv_id = findViewById(R.id.tv_id);
        this.tv_Produit = findViewById(R.id.tv_Produit);
        this.tv_ingredient = findViewById(R.id.tv_ingredient);
        this.tv_prix = findViewById(R.id.tv_prix);
        this.tv_quantite= findViewById(R.id.tv_quantite);
        this.imaProduit = findViewById(R.id.imaProduit);
    }

    // Methode onclic sur boutton "Modifier" pour aller au page Modifier Produit
    public void act_modifier_produit(View view) {

        String produit_id = tv_id.getText().toString();
        String produit_nom = tv_Produit.getText().toString();
        String produit_prix = tv_prix.getText().toString();
        String produit_categorie = new Gson().toJson(this.produit.getProduitCategorie());

        if (produit_prix.startsWith("Prix (€) : ")){ produit_prix = produit_prix.substring(11); }
        String produit_quantite = tv_quantite.getText().toString();
        if (produit_quantite.startsWith("Quantité : ")){ produit_quantite = produit_quantite.substring(11); }

        // On crée un Intent (Une intention)
        Intent intent = new Intent(this, ModifierProduit.class);

        intent.putExtra("produit_id", produit_id);
        intent.putExtra("produit_nom", produit_nom);
        intent.putExtra("produit_prix", produit_prix);
        intent.putExtra("produit_quantite", produit_quantite);
        intent.putExtra("produit_categorie", produit_categorie);

        //lancer Intent
        startActivity(intent);
        // Appeller 1 toast
        AppelToast.displayCustomToast(this,"Aller à la page Modifier ce produit");
    }

    // Methode pour aller à la liste des produit quand on clic sur button retour
    public void act_retour_list_produits(View view) {
        // Appeller 1 toast
        AppelToast.displayCustomToast(this,"Retour à la page Liste des produits");
        // Appeller 1 simple intent vers la page List des produit
        AppelIntent.appelIntentSimple(this,PageListeProduits.class);
    }

    // Methode onclic sur bouton " Ajouter Ingredient" pour aller au page Ingredient
    public void act_ajouter_ingredient(View view) {

        AppelToast.displayCustomToast(this, "Aller à la page Ingredient pour ajouter nouveau ingredient");

        AppelIntent.appelIntentAvecExtraProduit(this, PageIngredient.class, this.produit);
    }

    //Methode pour afficher la page PageProduit
    public void fct_remplirLaVuePageProduit() {

        // methode try pour donner 1 exception s'il n'y a pas de Extra
        try {
            if (getIntent().hasExtra(NameExtra.PRODUIT_ID.toString()) == false) {throw new Exception("Aucun Extra donne pour l('id)"); }

        } catch (Exception e) {
            // Appeller 1 toast
            AppelToast.displayCustomToast(this, "Erreur détectée lors du remplissage de la vue [" + e.toString() + "]");

            getIntent().setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        }

        // Prendre id du produit puis mettre dans 1 string
        String produit_id= getIntent().getStringExtra(NameExtra.PRODUIT_ID.toString());
        // Remettre this id to type Integer
        int id = Integer.parseInt(produit_id);

        // Get connection to HTTP server throw Thread
        String textUrl = MyURL.TITLE.toString()+ MyURL.FINDPRODUITID.toString() +id;

       SendHttpRequestTask t = new SendHttpRequestTask();

        String[] params = new String[]{textUrl};
        t.execute(params);

    }


    private class SendHttpRequestTask extends AsyncTask<String, Void, String> {


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

            PageProduit.this.produit = gson.fromJson(result,Produit.class);
            affichageDuPage(produit);

        }

    }

    // Methode pour remplir la vue du page Produit apres prendre les informations necessaire
    public void affichageDuPage(Produit produit ){
        String id = String.valueOf(produit.getProduit_id());
        String nom = produit.getProduit_nom();
        String prix = String.valueOf(produit.getProduit_prix());
        String quantite = String.valueOf(produit.getProduit_quantite());

        // Appeller methode pour afficher list les ingredients du produit dans la page Produit
        prendreListIngredientProduit(produit.getProduit_id());

        this.tv_id.setText(id);
        this.tv_Produit.setText(nom);
        this.tv_prix.setText("Prix (€) : "+prix);
        this.tv_quantite.setText("Quantité : "+quantite);

        if ( produit.getProduit_quantite() <= 0){
            AppelDialog.showAlerDialogZero(this, NameExtra.PRODUIT_ID);
            this.tv_quantite.setBackgroundColor(Color.RED);
            this.tv_quantite.setTextColor(Color.WHITE);
        }


    }

    // Methode pour afficher list les ingredients du produit dans la page Produit
    private void prendreListIngredientProduit(int produit_id) {

        // Get connection to HTTP server throw Thread
        String textUrl1 = MyURL.TITLE.toString()+MyURL.LISTINGREDIENTBYPRODUIT +produit_id;
        String textUrl2 = MyURL.TITLE.toString()+ MyURL.FINDMPBYID;
        SendHttpRequestTaskListIngredient t = new SendHttpRequestTaskListIngredient();

        String[] params = new String[]{textUrl1, textUrl2};
        t.execute(params);



    }

    private class SendHttpRequestTaskListIngredient extends AsyncTask<String, Void, String> {
        Boolean appelAlert = false;

        //out le code qui nécessite un temps d'exécution sera placé dans cette fonction.
        // Étant donné que cette fonction est exécutée dans un thread complètement séparé du thread d'interface utilisateur, vous n'êtes pas autorisé à mettre à jour l'interface ici.
        @RequiresApi(api = Build.VERSION_CODES.M)
        @Override
        protected String doInBackground(String... params) {
            String urlListIngredients = params[0];
            String urlListMP = params[1];

            String result = null;

            try {
                String listIngred = MyHTTPConnection.startHttpRequestGET(urlListIngredients);
                String ingredient = " ";
                List<Ingredient> listIngredients = Fonctions.changefromJsonListIngredient(listIngred);


                for (Ingredient ing: listIngredients) {

                    int i = ing.getMp_id();

                    MatierePremiere mp= prendMP(urlListMP+i);

                    ingredient = ingredient+" \n - " + mp.getMp_nom()+" : "+ing.getIngredient_quantite()+ing.getIngredient_unite();

                    // Verifier resultat, s'il existe 1 Ingredient qui a son quantite de Matiere Premiere < ou = 0, on mettre objet appelAlert = true
                    if(mp.getMp_quantite()<=0){
                        appelAlert = true;
                    }

                }
                result = ingredient;
            } catch (Exception e) {
                e.printStackTrace();
            }

            return result;
        }

        //la resultat de doInBackground est repris et afficher à la vue
        @RequiresApi(api = Build.VERSION_CODES.M)
        @Override
        protected void onPostExecute(String result) {

            PageProduit.this.tv_ingredient.setText("Ingredients : \n"+ result);

            // si objet appelAlert = true, on appel fonction qui afficher AlertDialog
            if (appelAlert){
                // Appeller AlertDialog pour afficher 1 alert que quantite du produit est 0
                AppelDialog.showAlerDialogZero(PageProduit.this, NameExtra.MP);
                PageProduit.this.tv_ingredient.setTextColor(Color.RED);
            }
        }

        private MatierePremiere prendMP(String url) throws Exception {

            String st_mp = MyHTTPConnection.startHttpRequestGET(url);
            MatierePremiere mp = new Gson().fromJson(st_mp,MatierePremiere.class);

            return mp;
        }
    }


        // Methode pour aller au page Supprimer un produit quand on clic sur button
    public void act_suprimer_produit(View view) {
        // Afficher 1 dialog pour confirmer la supprimation
        AppelDialog.showAlertDialogSuppimer(this, tv_id, NameExtra.PRODUIT);
    }

}