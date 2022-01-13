package com.application_boulangerie.utils;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.widget.EditText;
import android.widget.TextView;

import com.application_boulangerie.PageCategorie;
import com.application_boulangerie.PageDeconnexion;
import com.application_boulangerie.PageListeProduits;
import com.application_boulangerie.PageUser;
import com.application_boulangerie.R;
import com.application_boulangerie.PageSupprimer;
import com.application_boulangerie.data.Categorie;
import com.application_boulangerie.data.MatierePremiere;
import com.application_boulangerie.data.Utilisateur;

import java.util.List;

public class AppelDialog {

    // Methode afficher dialog
    public static void showAlertDialogSuppimer(final Context context, TextView tv_id, NameExtra i_nameExtra )  {
        final Drawable icon = context.getResources().getDrawable(R.drawable.icon_app);
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        // Mettre la Title, Message et icon pour le dialog:
        builder.setTitle("Confirmation").setMessage(Message.DIALOG_CONFIRMATION_SUPPRIMER+i_nameExtra.toString()+"?");
        builder.setIcon(icon);
        // Permettre d'annuller la demande
        builder.setCancelable(true);
        // Creer button "Annuller avec OnClickListener qui permettre fermer le dialog et annuller la demmande
        builder.setPositiveButton("Annuller", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                AppelToast.displayCustomToast(context,Message.TOAST_DIALOG_BOUTON_ANNULLER.toString());
                dialog.cancel();
            }
        });
        // Creer le button "Supprimer" avec OnClickListener qui permmettre d'aller au page Supprimer pour supprimer un produit
       builder.setNegativeButton("Supprimer", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                AppelToast.displayCustomToast(context, Message.TOAST_DIALOG_BUTTON_SUPPRIMER.toString()+ i_nameExtra.toString());
                String produit_id = tv_id.getText().toString();
                // On crée un Intent (Une intention)
                Intent intent = new Intent(context, PageSupprimer.class);
                if (i_nameExtra == NameExtra.PRODUIT) {
                    intent.putExtra(NameExtra.PRODUIT_ID.toString(), produit_id);
                } else if (i_nameExtra== NameExtra.MP) {
                    intent.putExtra(NameExtra.MP_ID.toString(), produit_id);
                }else if (i_nameExtra== NameExtra.CATEGORIE) {
                    intent.putExtra(NameExtra.CATEGORIE_ID.toString(), produit_id);
                }else if (i_nameExtra== NameExtra.INGREDIENT) {
                    intent.putExtra(NameExtra.INGREDIENT_ID.toString(), produit_id);
                }else if (i_nameExtra== NameExtra.UTILISATEUR) {
                    intent.putExtra(NameExtra.UTILISATEUR_ID.toString(), produit_id);
                }

                context.startActivity(intent);
        }
    });
        // Creer AlertDialog:
        AlertDialog alert = builder.create();
        alert.show();
    }

    public static void showAlertDialogListMP (final Activity activity, List<MatierePremiere> listMP)  {

        AlertDialog.Builder builder = new AlertDialog.Builder(activity);

        // Set Title.
        builder.setTitle(Message.DIALOG_TITRE_LISTMP.toString());

        // Add a list
        final String[] listMP_nom = new String[listMP.size()];
        int i = 0;
        for (MatierePremiere mp: listMP) {
                listMP_nom[i] = " MP ID "+mp.getMp_id()+" -> "+ mp.getMp_nom() ;
                i++;
            }

        builder.setItems(listMP_nom, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                AppelToast.displayCustomToast(activity, Message.TOAST_DIALOG_MP.toString() + listMP_nom[which]);
            }
        });

        builder.setCancelable(true);
        builder.setIcon(R.drawable.icon_app);

        // Creer button "Fermer" with OnClickListener.
        builder.setNegativeButton("Fermer", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                AppelToast.displayCustomToast(activity, Message.TOAST_DIALOG_BOUTON_FERMER.toString());
                //  Cancel
                dialog.cancel();
            }
        });

        // Creer AlertDialog:
        AlertDialog alert = builder.create();
        alert.show();
    }

    public static void showAlertDialogListMPAvecEDT(final Activity activity, List<MatierePremiere> listMP, EditText edt_id, EditText edt_mp_unite)  {

        AlertDialog.Builder builder = new AlertDialog.Builder(activity);

        // Set Title.
        builder.setTitle(Message.DIALOG_TITRE_LISTMP.toString());

        // Add a list
        final String[] listMP_nom = new String[listMP.size()];
        final String[] listMP_id = new String[listMP.size()];
        final String[] listMP_unite = new String[listMP.size()];

        int i = 0;
        for (MatierePremiere mp: listMP) {
            listMP_nom[i] = " MP ID "+mp.getMp_id()+" -> "+ mp.getMp_nom() ;
            listMP_id[i] = String.valueOf(mp.getMp_id());
            listMP_unite[i] = mp.getMp_unite();
            i++;
        }

        builder.setItems(listMP_nom, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                edt_id.setText(listMP_id[which]);
                edt_mp_unite.setText(listMP_unite[which]);
                AppelToast.displayCustomToast(activity, Message.TOAST_DIALOG_MP.toString() + listMP_nom[which]);
            }
        });

        builder.setCancelable(true);
        builder.setIcon(R.drawable.icon_app);

        // Creer button "Fermer" with OnClickListener.
        builder.setNegativeButton("Fermer", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                AppelToast.displayCustomToast(activity, Message.TOAST_DIALOG_BOUTON_FERMER.toString());
                //  Cancel
                dialog.cancel();
            }
        });

        // Creer AlertDialog:
        AlertDialog alert = builder.create();
        alert.show();
    }

    public static void showAlertDialogListCategorie (final Activity activity, List<Categorie> listCategories, EditText edt_id)  {

        AlertDialog.Builder builder = new AlertDialog.Builder(activity);

        // Set Title.
        builder.setTitle(Message.DIALOG_TITRE_LISTCATEGORIE.toString());

        // Add a list
        final String[] str_listCategorie = new String[listCategories.size()];
        final String[] str_listCategorie_id = new String[listCategories.size()];

        int i = 0;
        for (Categorie categorie: listCategories) {
            str_listCategorie[i] = categorie.toString() ;
            str_listCategorie_id[i] = String.valueOf(categorie.getCategorie_id());
            i++;
        }

        builder.setItems(str_listCategorie, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                edt_id.setText(str_listCategorie_id[which]);
                AppelToast.displayCustomToast(activity, "C'est " + str_listCategorie[which]);
            }
        });

        builder.setCancelable(true);
        builder.setIcon(R.drawable.icon_app);

        // Creer button "Fermer" with OnClickListener.
        builder.setNegativeButton("Fermer", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                AppelToast.displayCustomToast(activity, Message.TOAST_DIALOG_BOUTON_FERMER.toString());
                //  Cancel
                dialog.cancel();
            }
        });

        // Creer AlertDialog:
        AlertDialog alert = builder.create();
        alert.show();
    }

    public static void showAlertDialogCategorie(final Context context, Categorie categorie) {
        final Drawable icon = context.getResources().getDrawable(R.drawable.icon_app);
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        // Mettre la Title, Message et icon pour le dialog:
        builder.setTitle("Confirmation").setMessage(Message.DIALOG_CONFIRMATION_CATEGORIE.toString());
        builder.setIcon(icon);
        // Permettre d'annuller la demande
        builder.setCancelable(true);
        // Creer button "Annuller avec OnClickListener qui permettre fermer le dialog et annuller la demmande
        builder.setNeutralButton("Annuller", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                AppelToast.displayCustomToast(context,Message.TOAST_DIALOG_BOUTON_ANNULLER.toString());
                dialog.cancel();
            }
        });
        // Creer le button "Modifier" avec OnClickListener qui permmettre d'aller au page Supprimer pour supprimer un produit
        builder.setNegativeButton("Modifier", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                AppelToast.displayCustomToast(context, Message.TOAST_DIALOG_BOUTON_MODIFIER.toString() + NameExtra.CATEGORIE.toString());
                String categorie_id = String.valueOf(categorie.getCategorie_id());
                // On crée un Intent (Une intention)
                Intent intent = new Intent(context, PageCategorie.class);

                intent.putExtra(NameExtra.CATEGORIE_ID.toString(), categorie_id);

                context.startActivity(intent);
            }
        });

        // Creer le button "Afficher liste des Produits" avec OnClickListener qui permmettre d'aller au page Supprimer pour supprimer un produit
        builder.setPositiveButton("Afficher", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                AppelToast.displayCustomToast(context, Message.TOAST_DIALOG_BUTTON_AFFICHER_PRODUIT.toString());
                String categorie_id = String.valueOf(categorie.getCategorie_id());
                // On crée un Intent (Une intention)
                Intent intent = new Intent(context, PageListeProduits.class);

                intent.putExtra(NameExtra.CATEGORIE_ID.toString(), categorie_id);

                context.startActivity(intent);
            }
        });

        // Creer AlertDialog:
        AlertDialog alert = builder.create();
        alert.show();

    }

    public static void showProgressDialog(Context context){

            ProgressDialog pd;
            pd = new ProgressDialog(context);
            pd.setMessage(Message.PROGRESS.toString());
            pd.setCancelable(false);
            pd.show();
    }

    public static void showAlerDialogZero(Context context, NameExtra i_nameExtra){

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        // Set Title and Message:
        if ((i_nameExtra.equals(NameExtra.PRODUIT)) || (i_nameExtra.equals(NameExtra.MP))) {
            builder.setTitle("ATTENTION").setMessage(" Il y a " + i_nameExtra.toString() +Message.DIALOG_MESSAGE_PHASE_PAS_EN_STOCK.toString());
        } else if(i_nameExtra.equals(NameExtra.PRODUIT_ID)){
            builder.setTitle("ATTENTION").setMessage( NameExtra.PRODUIT.toString().toUpperCase() + Message.DIALOG_MESSAGE_PAS_EN_STOCK.toString());
        } else if (i_nameExtra.equals(NameExtra.MP_ID)){
            builder.setTitle("ATTENTION").setMessage(NameExtra.MP.toString().toUpperCase() + Message.DIALOG_MESSAGE_PAS_EN_STOCK.toString());
        }

        //
        builder.setCancelable(true);
        builder.setIcon(R.drawable.icon_app);

        // Create "Negative" button with OnClickListener.
        builder.setNegativeButton("Fermer", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                //  Cancel
                dialog.cancel();
            }
        });
        // Create AlertDialog:
        AlertDialog alert = builder.create();
        alert.show();
    }

    public static void showAlerDialogDeconnexion(final Context context){
        AlertDialog.Builder builder = new AlertDialog.Builder(context);

        // Set Title and Message:
        builder.setTitle("Déconnexion").setMessage(Message.DIALOG_MESSAGE_DECONNEXION.toString());

        //
        builder.setCancelable(true);
        builder.setIcon(R.drawable.icon_app);

        // Create "Positive" button with OnClickListener.
        builder.setPositiveButton("Oui", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {

                AppelToast.displayCustomToast(context, Message.TOAST_DIALOG_BUTTON_OUI_DECONNEXION.toString());
                AppelIntent.appelIntentSimple(context, PageDeconnexion.class);
            }
        });

        // Create "Negative" button with OnClickListener.
        builder.setNegativeButton("Non", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                AppelToast.displayCustomToast(context, Message.TOAST_DIALOG_BUTON_NON_DECONNEXION.toString());
                //  Cancel
                dialog.cancel();
            }
        });

        // Create AlertDialog:
        AlertDialog alert = builder.create();
        alert.show();
    }

    public static void showAlertDialogListUsers (final Activity activity, List<Utilisateur> listUsers)  {

        AlertDialog.Builder builder = new AlertDialog.Builder(activity);

        // Set Title.
        builder.setTitle(Message.DIALOG_TITRE_LISTUSERS.toString());

        // Add a list
        final String[] str_listUsers = new String[listUsers.size()];
        final String[] str_listUsers_id = new String[listUsers.size()];

        int i = 0;
        for (Utilisateur user: listUsers) {
            str_listUsers[i] = user.toString() ;
            str_listUsers_id[i] = String.valueOf(user.getUser_id());
            i++;
        }

        builder.setItems(str_listUsers, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                AppelIntent.appelIntentAvecExtraUser(activity, PageUser.class, str_listUsers_id[which] );
            }
        });

        builder.setCancelable(true);
        builder.setIcon(R.drawable.icon_app);

        // Creer button "Fermer" with OnClickListener.
        builder.setNegativeButton("Fermer", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                AppelToast.displayCustomToast(activity, Message.TOAST_DIALOG_BOUTON_FERMER.toString());
                //  Cancel
                dialog.cancel();
            }
        });

        // Creer button "Ajouter" with OnClickListener.
        builder.setPositiveButton("Nouveau Utilisateur", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                AppelIntent.appelIntentSimple(activity,PageUser.class);
                dialog.cancel();
            }
        });

        // Creer AlertDialog:
        AlertDialog alert = builder.create();
        alert.show();
    }
}
