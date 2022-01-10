package com.application_boulangerie;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Layout;
import android.view.View;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import com.application_boulangerie.data.Ingredient;
import com.application_boulangerie.data.MatierePremiere;
import com.application_boulangerie.utils.AppelDialog;
import com.application_boulangerie.utils.AppelIntent;
import com.application_boulangerie.utils.AppelToast;
import com.application_boulangerie.utils.Fonctions;
import com.application_boulangerie.utils.MyHTTPConnection;
import com.application_boulangerie.utils.MyURL;
import com.application_boulangerie.utils.NameExtra;

import java.util.List;

public class PageListIngredient extends AppCompatActivity {

    TextView tv_id_produit_ingredient;
    TableLayout tl_list_ingredients;
    Button bt_aide_modifier;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_page_list_ingredient);

        // Appeller fonction pour association view au java
        fct_associationViewAuJava();

        // Appeller fonction pour afficher la page
        fct_remplirLaVueDuPageListIngredient();

    }

    // On associe le XML avec le JAVA
    private void fct_associationViewAuJava(){
        this.tv_id_produit_ingredient= findViewById(R.id.tv_id_ingredient);
        this.tl_list_ingredients = findViewById(R.id.tl_list_ingredients);
        this.bt_aide_modifier = findViewById(R.id.bt_aide_modifier);
    }

    //Methode pour afficher la page
    public void fct_remplirLaVueDuPageListIngredient() {

        // methode try pour donner 1 exception s'il n'y a pas de Extra
        try {
            if (!getIntent().hasExtra(NameExtra.PRODUIT_ID.toString())) {throw new Exception("Aucun Extra donne pour l('id)"); }

        } catch (Exception e) {
            // Appeller 1 toast
            AppelToast.displayCustomToast(this, "Erreur détectée lors du remplissage de la vue [" + e.toString() + "]");

            getIntent().setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        }

        // Prendre id du produit puis mettre dans 1 string
        String produit_id= getIntent().getStringExtra("produit_id");

        // mettre la valeur pour textview tv_id_produit_ingredient pour stocker produit_id
        tv_id_produit_ingredient.setText(produit_id);

        // Remettre this id to type Integer
        int id = Integer.parseInt(produit_id);

        // Get connection to HTTP server throw Thread
        String textUrl1 = MyURL.TITLE.toString()+ MyURL.LISTINGREDIENTBYPRODUIT.toString() +id;
        String textUrl2 = MyURL.TITLE.toString() + MyURL.FINDMPBYID.toString();

        SendHttpRequestTaskListIngredient t = new SendHttpRequestTaskListIngredient();

        String[] params = new String[]{textUrl1,textUrl2};
        t.execute(params);

    }


    private class SendHttpRequestTaskListIngredient extends AsyncTask<String, Void, String> {


        //out le code qui nécessite un temps d'exécution sera placé dans cette fonction.
        // Étant donné que cette fonction est exécutée dans un thread complètement séparé du thread d'interface utilisateur, vous n'êtes pas autorisé à mettre à jour l'interface ici.
        @RequiresApi(api = Build.VERSION_CODES.M)
        @Override
        protected  String  doInBackground(String... params) {
            String urlListIngredients = params[0];
            //String urlMP = params[1];

            String result = null;
            try {
                result = MyHTTPConnection.startHttpRequestGET(urlListIngredients);

                } catch (Exception e) {
                e.printStackTrace();
            }
            return result;
        }
        //la resultat de doInBackground est repris et afficher à la vue
        @RequiresApi(api = Build.VERSION_CODES.M)
        @Override
        protected void onPostExecute( String result ) {

            List<Ingredient> listIngredients = Fonctions.changefromJsonListIngredient(result);

            for (Ingredient ing: listIngredients) {
                ajouterRow(tl_list_ingredients, ing);
            }

        }

    }

    @SuppressLint("WrongConstant")
    @RequiresApi(api = Build.VERSION_CODES.M)
    public void ajouterRow(TableLayout tl,
                           Ingredient ingredient){

        // Create a new row to be added
        TableRow tr = new TableRow(this);
        tr.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.MATCH_PARENT));

        //Creer 4 TextView
        TextView tvNewIdIngredient = new TextView(this) ;
        TextView tvNewNomMp= new TextView(this);
        TextView tvNewQuantiteIngredient =new TextView(this);
        TextView tvNewUniteIngredient = new TextView(this);


        // Mettre style pour les textViews
        tvNewIdIngredient.setTextAppearance(R.style.TableColumStyle0);
        tvNewNomMp.setTextAppearance(R.style.TableColumStyle0);
        tvNewQuantiteIngredient.setTextAppearance(R.style.TableColumStyle0);
        tvNewUniteIngredient.setTextAppearance(R.style.TableColumStyle0);

        // Mettre valeur pour les textViews
        tvNewIdIngredient.setText(String.valueOf(ingredient.getIngredient_id()));
        tvNewNomMp.setText(String.valueOf(ingredient.getMp_id()));
        tvNewNomMp.setBreakStrategy(Layout.BREAK_STRATEGY_BALANCED);
        tvNewQuantiteIngredient.setText(String.valueOf(ingredient.getIngredient_quantite()));
        tvNewUniteIngredient.setText(ingredient.getIngredient_unite());


        //Creer 1 button pour modifier Ingredient au page PageIngredient avec 1 seul clic
        Button bt_Select_MP = new Button(this);

        //set Style, text pour button
        bt_Select_MP.setTextAppearance(R.style.TableButonStyle_MP);
        bt_Select_MP.setText("SELECTE");
        // methode pour aller au PageProduit (utiliser Lamda)
        bt_Select_MP.setOnClickListener(v -> AppelIntent.appelIntentAvecExtraIngredient(PageListIngredient.this, PageIngredient.class, ingredient));
        //Ajouter les Textviews et checkbox a la ligne
        tr.addView(tvNewIdIngredient);
        tr.addView(tvNewNomMp);
        tr.addView(tvNewQuantiteIngredient);
        tr.addView(tvNewUniteIngredient);
        tr.addView(bt_Select_MP);

        // Ajouter row dans TableLayout.
        tl.addView(tr);
    }

    // Methode onclick sur bouton "Retour" pour retour à la page Produit
    public void act_retour_a_la_pageProduit(View view) {
        String produit_id = tv_id_produit_ingredient.getText().toString();

        AppelToast.displayCustomToast(this, "Retour à la page Produit");

        AppelIntent.appelIntentAvecExtraProduit_ID(this, PageProduit.class, produit_id);


    }

    // Methode pour affichage la list MP et permettre utilisateur savoir le nom du MP correspondant avec son ID
    public void act_aide_affichage_listMP(View view) {

        String textUrl =MyURL.TITLE.toString()+ MyURL.LISTMP.toString();

        SendHttpRequestTaskListMP t = new SendHttpRequestTaskListMP();

        String[] params = new String[]{textUrl};
        t.execute(params);


    }

    private class SendHttpRequestTaskListMP extends AsyncTask<String, Void, String> {


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

            List<MatierePremiere> listMP = Fonctions.changefromJsonListMP(result);

            AppelDialog.showAlertDialogListMP(PageListIngredient.this, listMP);


        }

    }


}