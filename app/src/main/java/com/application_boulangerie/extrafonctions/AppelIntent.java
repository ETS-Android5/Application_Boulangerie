package com.application_boulangerie.extrafonctions;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.application_boulangerie.PageListeProduits;
import com.application_boulangerie.PageProduit;
import com.application_boulangerie.R;
import com.application_boulangerie.data.Produit;
import com.owlike.genson.Genson;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class AppelIntent {

    public static ArrayList<Produit> produitList = new ArrayList<>();

    public static void appelIntentSimple(Context context, Class classAAfficher) {

        // On crée un Intent (Une intention)

        Intent intent = new Intent(context, classAAfficher);

        //lancer Intent
        context.startActivity(intent);

    }

    public static void appelIntentAvecExtraPageProduit(Context context, Class classAAfficher, Produit produit) {

        String produit_id = String.valueOf(produit.getProduit_id());
        String produit_nom = produit.getProduit_nom();
        String produit_prix = String.valueOf(produit.getProduit_prix());
        String produit_quantite = String.valueOf(produit.getProduit_quantite());
        String produit_description_ingredients = produit.getProduit_description_ingredients();

        // On crée un Intent (Une intention)
        Intent intent = new Intent(context, classAAfficher);


        intent.putExtra("produit_id", produit_id);
        intent.putExtra("produit_nom", produit_nom);
        intent.putExtra("produit_prix", produit_prix);
        intent.putExtra("produit_quantite", produit_quantite);
        intent.putExtra("produit_description_ingredients", produit_description_ingredients);

        //  intent.putExtra("image_produit",imageProduit);

        //lancer Intent
        context.startActivity(intent);
    }



    // Fonction pour appeller Intent avec Extrat pour Page Produit
    public static void appelIntentAvecExtraPageProduitAvecImage(Context context, Produit produit, Class classAAfficher) {

        // Declarer les variables pour Extra

        String produit_id = String.valueOf(produit.getProduit_id());
        String produit_nom = produit.getProduit_nom();
        String produit_prix = String.valueOf(produit.getProduit_prix());
        String produit_description_ingredients = produit.getProduit_description_ingredients();
        int imageProduit = R.drawable.ic_launcher_background;


        // On crée un Intent (Une intention)
        Intent intent = new Intent(context, classAAfficher);

        intent.putExtra("id", produit_id);
        intent.putExtra("titre", produit_nom);
        intent.putExtra("liste_des_ingrédients", produit_description_ingredients);
        intent.putExtra("prix_produit", produit_prix);
        intent.putExtra("image_produit", imageProduit);

        //lancer Intent
        context.startActivity(intent);

    }

    // fonction pour ouvrir la page Produit
    public static int fct_remplirLaVuePageProduit(Context context,
                                                  Intent intent1,
                                                  TextView tvProduit_nom,
                                                  TextView tvPrixProduit,
                                                  TextView tvListIngredients,
                                                  ImageView imv) {
        try {
            if (intent1.hasExtra("titre") == false) {
                throw new Exception("Aucun Extra donne pour le titre");
            }
            if (intent1.hasExtra("liste_des_ingrédients") == false) {
                throw new Exception("Aucun Extra donne pour la liste des ingredients");
            }
            if (intent1.hasExtra("prix_produit") == false) {
                throw new Exception("Aucun Extra donne pour le prix");
            }
            if (intent1.hasExtra("image_produit") == false) {
                throw new Exception("Aucun Extra donne pour l'image");
            }
            if (intent1.hasExtra("id") == false) {
                throw new Exception("Aucun Extra donne pour l'id");
            }
        } catch (Exception e) {

            AppelToast.displayCustomToast(context, "Erreur détectée lors du remplissage de la vue [" + e.toString() + "]");

            intent1.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        }

        // mettre Nom du produit
        String produit_nom = intent1.getStringExtra("titre");
        tvProduit_nom.setText(produit_nom);

        // mettre list des ingredients du produit
        String text_listIngredients = intent1.getStringExtra("liste_des_ingrédients");
        tvListIngredients.setText(text_listIngredients);

        // mettre prix du produit
        String text_prixProduit = intent1.getStringExtra("prix_produit");
        tvPrixProduit.setText(text_prixProduit);

        // mettre image du produit
        int id_image = intent1.getIntExtra("image_produit", R.drawable.ic_launcher_background);
        imv.setImageResource(id_image);

        int id = Integer.parseInt(intent1.getStringExtra("id"));

        return id;
    }

    // fonction pour ouvrir la page Produit
    public static void fct_remplirLaVuePageModifierProduit(Context context,
                                                          Intent intent1,
                                                          EditText edt_produit_id_update,
                                                          EditText edt_produit_nom_update,
                                                          EditText edt_produit_prix_update,
                                                          EditText edt_produit_quantite_update,
                                                          EditText edt_produit_ingredient_update) {
        try {
            if (intent1.hasExtra("produit_id") == false) {
                throw new Exception("Aucun Extra donne pour l('id)");
            }
            if (intent1.hasExtra("produit_nom") == false) {
                throw new Exception("Aucun Extra donne pour la nom");
            }
            if (intent1.hasExtra("produit_prix") == false) {
                throw new Exception("Aucun Extra donne pour le prix");
            }
            if (intent1.hasExtra("produit_quantite") == false) {
                throw new Exception("Aucun Extra donne pour lq quantite");
            }
            if (intent1.hasExtra("produit_description_ingredients") == false) {
                throw new Exception("Aucun Extra donne pour l'ingredient");
            }
        } catch (Exception e) {

            AppelToast.displayCustomToast(context, "Erreur détectée lors du remplissage de la vue [" + e.toString() + "]");

            intent1.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        }

        // mettre Nom du produit
        String produit_id= intent1.getStringExtra("produit_id");
        edt_produit_id_update.setText(produit_id);

        // mettre list des ingredients du produit
        String produit_nom = intent1.getStringExtra("produit_nom");
        edt_produit_nom_update.setText(produit_nom);

        // mettre prix du produit
        String produit_prix = intent1.getStringExtra("produit_prix");
        edt_produit_prix_update.setText(produit_prix);

        // mettre quantite du produit
        String produit_quantite = intent1.getStringExtra("produit_quantite");
        edt_produit_quantite_update.setText(produit_quantite);

        // mettre prix du produit
        String produit_description_ingredients = intent1.getStringExtra("produit_description_ingredients");
        edt_produit_ingredient_update.setText(produit_description_ingredients);
    }





    // Creer fonction pour ouvrir page List Produits
    @RequiresApi(api = Build.VERSION_CODES.M)
    public static void fct_remplirLaVuePageListProduit(Context context,
                                                       TableLayout tl) {

        // Get connection to HTTP server throw Thread
        new Thread(new Runnable() {
            @Override
            public void run() {
                HttpURLConnection urlConnection = null;
                try {
                    // get connect to HTTP server
                    URL url = new URL("http://localhost:8080/Bouglangerie/webapi/produit");
                    urlConnection = (HttpURLConnection) url.openConnection();
                    urlConnection.setRequestMethod("GET");

                    // Creer 1 valeur type InputSteam pour recuper la flux
                    InputStream in = new BufferedInputStream(urlConnection.getInputStream());
                    //Permet de  décortiquer à partir d'un flux d'un certain nombre de methode indiqué
                    Scanner scanner = new Scanner(in);

                    // start connection
                    final List<Produit> listProduit = (List<Produit>) new Genson().deserialize(scanner.nextLine(), Produit.class);
                    Log.i("Exchange-JSON", "Resultat de la liste des produits " + listProduit);

                    for (Produit p : listProduit) {
                        String idProduit = String.valueOf(p.getProduit_id());
                        String nomProduit = p.getProduit_nom();
                        String prixProduit = String.valueOf(p.getProduit_prix());
                        String quantiteProduit = String.valueOf(p.getProduit_quantite());
                        ajouterRow(context, tl, idProduit, nomProduit, prixProduit, quantiteProduit);
                    }
                    // Close connection
                    in.close();

                } catch (Exception e) {
                    Log.e("Exchange-JSON", " On n'a pas trouve http server ", e);

                } finally {
                    if (urlConnection != null) urlConnection.disconnect();
                }
            }
        }).start();

    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public static void ajouterRow(Context context,
                                  TableLayout tl,
                                  String idProduit,
                                  String nomProduit,
                                  String prixProduit,
                                  String quantiteProduit) {

        // Create a new row to be added
        TableRow tr = new TableRow(context);
        tr.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.MATCH_PARENT));

        //Creer 5 TextView
        TextView tvNewIdProduit = new TextView(context);
        TextView tvNewNomProduit = new TextView(context);
        TextView tvNewPrixProduit = new TextView(context);
        TextView tvNewQuantiteProduit = new TextView(context);
        // TextView tvNewIngredientProduit = new TextView(context);

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

        //Creer 1 checkbox
        CheckBox cbModifierProduit = new CheckBox(context);

        //set Style
        cbModifierProduit.setTextAppearance(R.style.TableColumStyle0);
        cbModifierProduit.setText("select");

        //Ajouter les Textviews et checkbox a la ligne
        tr.addView(tvNewIdProduit);
        tr.addView(tvNewNomProduit);
        tr.addView(tvNewPrixProduit);
        tr.addView(tvNewQuantiteProduit);
        //   tr.addView(tvNewIngredientProduit);
        tr.addView(cbModifierProduit);

        // Add row to TableLayout.
        tl.addView(tr);
    }

    public static boolean checkProduct(Produit produit, ArrayList<Produit> listProduits) {
        for (Produit product : listProduits) {
            if (produit.getProduit_nom().equals(product.getProduit_nom()))
                return true;
        }
        return false;
    }
}






