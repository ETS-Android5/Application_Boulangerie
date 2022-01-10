package com.application_boulangerie;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.BoringLayout;
import android.text.Layout;
import android.view.View;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.application_boulangerie.data.MatierePremiere;
import com.application_boulangerie.utils.AppelDialog;
import com.application_boulangerie.utils.AppelIntent;
import com.application_boulangerie.utils.AppelToast;
import com.application_boulangerie.utils.MyHTTPConnection;
import com.application_boulangerie.utils.MyURL;
import com.application_boulangerie.utils.NameExtra;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

public class PageListeMP extends AppCompatActivity {
    TableLayout tl_MP;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_page_liste_mp);
        // Association view au java
        this.tl_MP = findViewById(R.id.tl_list_mp);

        // Get connection to HTTP server throw Thread
        String textUrl = MyURL.TITLE.toString()+ MyURL.LISTMP.toString();

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

            Gson gson = new GsonBuilder().setPrettyPrinting().create();

            // cas 2: transformer la string en tableau Json (pour methode getAll)
            // Definir type pour List<Produit> pour get valeur from server et puis transfert valeur type Json to type List<Produi>
            Type type = new TypeToken<List<MatierePremiere>>() {}.getType();
            List<MatierePremiere> listMP = gson.fromJson(result,type);

            for (MatierePremiere mp : listMP) {

                // appel methode pour ajouter nouvelle ligne a la table
                checkAlert = ajouterRow(tl_MP, mp);

                // Verifier resultat, s'il existe 1 produit qui a son quantite < ou = 0, on mettre objet appelAlert = true
                if (checkAlert){
                    appelAlert = true;
                }
            }
            // si objet appelAlert = true, on appel fonction qui afficher AlertDialog
            if (appelAlert){
                // Appeller AlertDialog pour afficher 1 alert que quantite du produit est 0
                AppelDialog.showAlerDialogZero(PageListeMP.this, NameExtra.MP);
            }

        }

    }
    @SuppressLint("WrongConstant")
    @RequiresApi(api = Build.VERSION_CODES.M)
    public Boolean ajouterRow(TableLayout tl,
                           MatierePremiere mp ){

        // Creer objet Boolean pour stocker le resultat s'il y a 1 MP qui a son quantite = ou < 0;
        Boolean i_checkAlert = false;

        // Creer nouveau row pour ajouter dans la table
        TableRow tr = new TableRow(this);
        tr.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.MATCH_PARENT));

        //Creer 4 TextView
        TextView tvNewIdMp = new TextView(this) ;
        TextView tvNewNomMp= new TextView(this);
        TextView tvNewPrixMp = new TextView(this);
        TextView tvNewQuantiteMp =new TextView(this);
        TextView tvNewUniteMp = new TextView(this);


        // Mettre style pour les textViews
        tvNewIdMp.setTextAppearance(R.style.TableColumStyle0);
        tvNewNomMp.setTextAppearance(R.style.TableColumStyle0);
        tvNewPrixMp.setTextAppearance(R.style.TableColumStyle0);
        tvNewQuantiteMp.setTextAppearance(R.style.TableColumStyle0);
        tvNewUniteMp.setTextAppearance(R.style.TableColumStyle0);

        // Mettre valeur pour les textViews
        tvNewIdMp.setText(String.valueOf(mp.getMp_id()));
        tvNewNomMp.setText(mp.getMp_nom());
        tvNewQuantiteMp.setText(String.valueOf(mp.getMp_quantite()));
        tvNewPrixMp.setText(String.valueOf(mp.getMp_prix()));
        tvNewUniteMp.setText(mp.getMp_unite());

        // pour wrap text si le nom est long
        tvNewNomMp.setBreakStrategy(Layout.BREAK_STRATEGY_BALANCED);

        // si la quantite de la matiere premiere < 0 ou = 0, mettre ce mp en ROUGE et objet  i_checkAlert = true;
        if (mp.getMp_quantite()<=0){
            tvNewIdMp.setTextColor(Color.RED);
            tvNewNomMp.setTextColor(Color.RED);
            tvNewPrixMp.setTextColor(Color.RED);
            tvNewQuantiteMp.setTextColor(Color.RED);
            tvNewUniteMp.setTextColor(Color.RED);
            i_checkAlert = true;
        }

        //Creer 1 button pour aller au page PageProduit avec 1 seul clic
        Button bt_Select_MP = new Button(this);

        //set Style, text pour button
        bt_Select_MP.setTextAppearance(R.style.TableButonStyle_MP);
        bt_Select_MP.setText("SELECTE");
        bt_Select_MP.setOnClickListener(new View.OnClickListener() {
            // methode pour aller au PageProduit
            @Override
            public void onClick(View v) {

                AppelIntent.appelIntentAvecExtraMP(PageListeMP.this, PageMatierePremiere.class, mp);
            }
        });


        //Ajouter les Textviews et checkbox a la ligne
        tr.addView(tvNewIdMp);
        tr.addView(tvNewNomMp);
        tr.addView(tvNewPrixMp);
        tr.addView(tvNewQuantiteMp);
        tr.addView(tvNewUniteMp);
        tr.addView(bt_Select_MP);

        // Ajouter row dans TableLayout.
        tl.addView(tr);

        return  i_checkAlert;
    }

    public void act_ajouter_mp(View view) {
        AppelToast.displayCustomToast(this, "Aller à la page MP pour ajouter Matiere Premiere");

        AppelIntent.appelIntentSimple(this, PageMatierePremiere.class);

    }

    public void act_retour_a_la_page_menu(View view) {

        AppelToast.displayCustomToast(this, "Retour à la page Menu");

        AppelIntent.appelIntentSimple(this, Menu.class);
    }
}