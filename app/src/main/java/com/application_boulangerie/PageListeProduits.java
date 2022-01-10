package com.application_boulangerie;

import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

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

public class PageListeProduits extends AppCompatActivity {

   TableLayout tl;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_page_liste_produits);

        // Association view au java
        this.tl = findViewById(R.id.tl_list_mp);
        // Creer 1 objet pour get connection to HTTP server throw Thread
        String textUrl;
        if(getIntent().hasExtra(NameExtra.CATEGORIE_ID.toString())){
            int categorie_id = Integer.parseInt(getIntent().getStringExtra(NameExtra.CATEGORIE_ID.toString()));
            textUrl= MyURL.TITLE.toString() + MyURL.FINDLISTPRODUITCATEGORIE.toString()+categorie_id;
        } else {
            textUrl = MyURL.TITLE.toString() + MyURL.LISTPRODUIT.toString();
        }
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

            Boolean appelAlert = false;
            Boolean checkAlert;

            List<Produit> listProduit = Fonctions.changefromJsonListProduit(result);

            // Affichage liste des produit et verifier s'il y a produit qui a son quantite < ou = 0
            for (Produit produit : listProduit) {
                // appel methode pour ajouter nouvelle ligne a la table
                checkAlert = ajouterRow(tl, produit);

                // Verifier resultat, s'il existe 1 produit qui a son quantite < ou = 0, on mettre objet appelAlert = true
                if (checkAlert){
                    appelAlert = true;
                }
            }
            // si objet appelAlert = true, on appel fonction qui afficher AlertDialog
            if (appelAlert){
                // Appeller AlertDialog pour afficher 1 alert que quantite du produit est 0
                AppelDialog.showAlerDialogZero(PageListeProduits.this, NameExtra.PRODUIT);
            }
        }

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
        AppelToast.displayCustomToast(this, "Retour au page MENU");
        // Apppler 1 simple intent  pour vers la page MainActivity
        AppelIntent.appelIntentSimple(this,Menu.class);

    }

    //Methode pour ajouter la nouvelle ligne à la table avec les informations de produit
    @RequiresApi(api = Build.VERSION_CODES.M)
    public Boolean ajouterRow(TableLayout tl,
                                  Produit produit){
        // Creer objet Boolean pour stocker le resultat s'il y a 1 produit qui a son quantite = ou < 0;
        Boolean i_checkAlert = false;

        // Creer nouveau row pour ajouter dans la table
        TableRow tr = new TableRow(this);
        tr.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.MATCH_PARENT));

        //Creer 4 TextView
        TextView tvNewIdProduit = new TextView(this) ;
        TextView tvNewNomProduit= new TextView(this);
        TextView tvNewPrixProduit = new TextView(this);
        TextView tvNewQuantiteProduit =new TextView(this);


        // Mettre style pour les textViews
        tvNewIdProduit.setTextAppearance(R.style.TableColumStyle0);
        tvNewNomProduit.setTextAppearance(R.style.TableColumStyle0);
        tvNewPrixProduit.setTextAppearance(R.style.TableColumStyle0);
        tvNewQuantiteProduit.setTextAppearance(R.style.TableColumStyle0);

        // Mettre les valeur pour les textView
        tvNewIdProduit.setText(String.valueOf(produit.getProduit_id()));
        tvNewNomProduit.setText(produit.getProduit_nom());
        tvNewPrixProduit.setText(String.valueOf(produit.getProduit_prix()));
        tvNewQuantiteProduit.setText(String.valueOf(produit.getProduit_quantite()));

        // si la quantite du produit < 0 ou = 0, mettre ce produit en ROUGE et objet i_checkAlert = true;
        if (produit.getProduit_quantite()<=0){
            tvNewIdProduit.setTextColor(Color.RED);
            tvNewNomProduit.setTextColor(Color.RED);
            tvNewPrixProduit.setTextColor(Color.RED);
            tvNewQuantiteProduit.setTextColor(Color.RED);
            i_checkAlert = true;
        }


        //Creer 1 button pour aller au page PageProduit avec 1 seul clic
        Button bt_Select_Produit = new Button(this);

        //set Style, text pour button
        bt_Select_Produit.setTextAppearance(R.style.TableButonStyle);
        bt_Select_Produit.setText("Select");
        bt_Select_Produit.setOnClickListener(new View.OnClickListener() {
            // methode pour aller au PageProduit
            @Override
            public void onClick(View v) {

               AppelIntent.appelIntentAvecExtraProduit(PageListeProduits.this, PageProduit.class, produit);
            }
        });


        //Ajouter les Textviews et checkbox a la ligne
        tr.addView(tvNewIdProduit);
        tr.addView(tvNewNomProduit);
        tr.addView(tvNewPrixProduit);
        tr.addView(tvNewQuantiteProduit);
        tr.addView(bt_Select_Produit);

        // Ajouter row dans TableLayout.
        tl.addView(tr);

        return i_checkAlert;
    }


}