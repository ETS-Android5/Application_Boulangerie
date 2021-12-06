package com.application_boulangerie;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import com.application_boulangerie.data.Produit;
import com.application_boulangerie.extrafonctions.AppelIntent;
import com.application_boulangerie.extrafonctions.AppelToast;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.Locale;
import java.util.Scanner;


public class PageProduit extends AppCompatActivity {

    int id;
    TextView tv_id, tv_Produit, tv_ingredient,tv_prix, tv_quantite;
    ImageView imaProduit;
    Produit produit ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_page_produit);
        // Association view au java
        fct_associationViewAuJava();

        fct_remplirLaVuePageProduit(tv_id, tv_Produit,tv_ingredient,tv_prix,tv_quantite);

    }

    private void fct_associationViewAuJava(){
        this.tv_id = findViewById(R.id.tv_id);
        this.tv_Produit = findViewById(R.id.tv_Produit);
        this.tv_ingredient = findViewById(R.id.tv_ingredient);
        this.tv_prix = findViewById(R.id.tv_prix);
        this.tv_quantite= findViewById(R.id.tv_quantite);
        this.imaProduit = findViewById(R.id.imaProduit);
           }

    public void act_modifier_produit(View view) {

        String produit_id = tv_id.getText().toString();
        String produit_nom = tv_Produit.getText().toString();
        String produit_prix = tv_prix.getText().toString();

        if (produit_prix.startsWith("Prix (€) : ")){ produit_prix = produit_prix.substring(11); }
        String produit_quantite = tv_quantite.getText().toString();
        if (produit_quantite.startsWith("Quantite : ")){ produit_quantite = produit_quantite.substring(11); }
        String produit_description_ingredients = tv_ingredient.getText().toString();
        if (produit_description_ingredients.startsWith("Ingredients: \n"))
        {produit_description_ingredients = produit_description_ingredients.substring(14); }

        // On crée un Intent (Une intention)
        Intent intent = new Intent(this, ModifierProduit.class);

        intent.putExtra("produit_id", produit_id);
        intent.putExtra("produit_nom", produit_nom);
        intent.putExtra("produit_prix", produit_prix);
        intent.putExtra("produit_quantite", produit_quantite);
        intent.putExtra("produit_description_ingredients", produit_description_ingredients);

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

    //Methode pour afficher la page PageProduit
    public void fct_remplirLaVuePageProduit( TextView tv_id,
                                            TextView tv_Produit,
                                            TextView tv_ingredient,
                                            TextView tv_prix,
                                            TextView tv_quantite) {

        // methode try pour donner 1 exception s'il n'y a pas de Extra
        try {
            if (getIntent().hasExtra("produit_id") == false) {throw new Exception("Aucun Extra donne pour l('id)"); }
            if (getIntent().hasExtra("produit_nom") == false) {throw new Exception("Aucun Extra donne pour la nom"); }
            if (getIntent().hasExtra("produit_prix") == false) { throw new Exception("Aucun Extra donne pour le prix"); }
            if (getIntent().hasExtra("produit_quantite") == false) { throw new Exception("Aucun Extra donne pour lq quantite"); }
            if (getIntent().hasExtra("produit_description_ingredients") == false) { throw new Exception("Aucun Extra donne pour l'ingredient"); }
        } catch (Exception e) {
            // Appeller 1 toast
            AppelToast.displayCustomToast(this, "Erreur détectée lors du remplissage de la vue [" + e.toString() + "]");

            getIntent().setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        }

        // Prendre id du produit puis mettre dans 1 string
        String produit_id= getIntent().getStringExtra("produit_id");
        // Remettre this id to type Integer
        int id = Integer.parseInt(produit_id);
        // mettre la text du TextView
        tv_id.setText(produit_id);
        // mettre nom du produit
        String produit_nom = getIntent().getStringExtra("produit_nom");
        tv_Produit.setText(produit_nom);
        // mettre prix du produit
        String produit_prix = getIntent().getStringExtra("produit_prix");
        tv_prix.setText("Prix (€) : "+produit_prix);
        // mettre quantite du produit
        String produit_quantite = getIntent().getStringExtra("produit_quantite");
        tv_quantite.setText("Quantite : "+produit_quantite);
        // mettre list des ingredients  du produit
        String produit_description_ingredients = getIntent().getStringExtra("produit_description_ingredients");
        tv_ingredient.setText("Ingredients: \n"+produit_description_ingredients);

        // Get connection to HTTP server throw Thread
        String textUrl= "http://192.168.43.190:8080/Bouglangerie/webapi/produit/findbyIDProduit/"+id;
        String methode = "GET";
/*
       SendHttpRequestTask t = new SendHttpRequestTask();

        String[] params = new String[]{textUrl, methode};
        t.execute(params);

*/
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
                Log.e("APP_BOULANGERIE", "error connect server "+ e.toString());
            }

            return result;
        }

        //la resultat de doInBackground est repris et afficher à la vue
        @RequiresApi(api = Build.VERSION_CODES.M)
        @Override
        protected void onPostExecute( String result ) {

            Gson gson = new GsonBuilder().setPrettyPrinting().create();

            Produit produit = gson.fromJson(result,Produit.class);

            tv_id.setText(produit.getProduit_id());
            tv_Produit.setText(produit.getProduit_nom());
            tv_prix.setText("Prix (€) : "+(int) produit.getProduit_prix());
            tv_quantite.setText("Quantite : "+produit.getProduit_quantite());
            tv_ingredient.setText("Ingredients: \n"+produit.getProduit_description_ingredients());

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
    // Methode pour aller au page Supprimer un produit quand on clic sur button
    public void act_suprimer_produit(View view) {
        // Afficher 1 dialog pour confirmer la supprimation
        showAlertDialog(PageProduit.this, tv_id);
    }
    // Methode afficher dialog
    public static void showAlertDialog(final Context context,TextView tv_id )  {
        final Drawable icon = context.getResources().getDrawable(R.drawable.icon_app);
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        // Mettre la Title, Message et icon pour le dialog:
        builder.setTitle("Confirmation").setMessage("Vous voulez supprimer ce produit?");
        builder.setIcon(icon);
        // Permettre d'annuller la demande
        builder.setCancelable(true);
        // Creer button "Annuller avec OnClickListener qui permettre fermer le dialog et annuller la demmande
        builder.setPositiveButton("Annuller", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                AppelToast.displayCustomToast(context,"Vous avez annullé cette demande");
                dialog.cancel();
            }
        });
        // Creer le button "Supprimer" avec OnClickListener qui permmettre d'aller au page Supprimer pour supprimer un produit
        builder.setNegativeButton("Supprimer", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                AppelToast.displayCustomToast(context,"Vous avez choisi SUPPRIMER CE PRODUIT");
                String produit_id = tv_id.getText().toString();
                // On crée un Intent (Une intention)
                Intent intent = new Intent(context, SupprimerProduit.class);
                intent.putExtra("produit_id", produit_id);
                context.startActivity(intent);
            }
        });
        // Creer AlertDialog:
        AlertDialog alert = builder.create();
        alert.show();
    }

}