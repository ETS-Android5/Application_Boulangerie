package com.application_boulangerie.adapter;

import android.app.Dialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.application_boulangerie.MainActivity;
import com.application_boulangerie.Menu;
import com.application_boulangerie.R;
import com.application_boulangerie.data.Utilisateur;
import com.application_boulangerie.utils.AppelDialog;
import com.application_boulangerie.utils.AppelIntent;
import com.application_boulangerie.utils.AppelToast;
import com.application_boulangerie.utils.Fonctions;
import com.application_boulangerie.utils.MyHTTPConnection;
import com.application_boulangerie.utils.MyURL;
import com.google.gson.Gson;

public class CustomDialog extends Dialog {

    public interface ConnectionListener {
        public void userEntered(String fullName, String password);
    }

    public Context context;

    private TextView tv_dialog_title;
    private EditText edt_dialog_user_name;
    private EditText edt_dialog_user_pass;
    private Button bt_dialog_valid;
    private Button bt_dialog_annuller;

    private ConnectionListener listener;

    public CustomDialog(Context context, ConnectionListener listener) {
        super(context);
        this.context = context;
        this.listener = listener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.layout_custom_dialog);

        this.tv_dialog_title = findViewById(R.id.tv_dialog_title);
        this.edt_dialog_user_name = findViewById(R.id.edt_dialog_user_name);
        this.edt_dialog_user_pass = findViewById(R.id.edt_dialog_user_pass);
        this.bt_dialog_valid = findViewById(R.id.bt_dialog_valid);
        this.bt_dialog_annuller = findViewById(R.id.bt_dialog_annuller);

        this.bt_dialog_valid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buttonOKClick();
            }
        });
        this.bt_dialog_annuller.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buttonCancelClick();
            }
        });
    }

    // User click "OK" button.
    private void buttonOKClick()  {
        String fullName = Fonctions.prendreText(this.edt_dialog_user_name);
        String password = Fonctions.prendreText(this.edt_dialog_user_pass);

        if(fullName== null || fullName.isEmpty())  {
            AppelToast.displayCustomToast(context, "Entrer nom utilisateur, svp!");
            return;
        }else if (password == null || password.isEmpty()){
            AppelToast.displayCustomToast(context, "Entrer mot de pass, svp!");
            return;
        }
        this.dismiss(); // Close Dialog

        if(this.listener!= null)  {
            this.listener.userEntered(fullName, password);

        }
    }

    // User click "Cancel" button.
    private void buttonCancelClick()  {
        this.dismiss();
    }

}
