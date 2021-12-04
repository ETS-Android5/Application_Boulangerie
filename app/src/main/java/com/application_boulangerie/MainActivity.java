package com.application_boulangerie;

import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.application_boulangerie.extrafonctions.AppelIntent;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    //Appeller intent simple
    public void list_produit(View view) {

        ProgressDialog pd;
        pd = new ProgressDialog(this);
        pd.setMessage("Please wait...");
        pd.setCancelable(false);
        pd.show();

        AppelIntent.appelIntentSimple(this,PageListeProduits.class);
    }
}