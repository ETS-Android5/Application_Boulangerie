package com.application_boulangerie.utils;

import android.util.Log;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

public class MyHTTPConnection {

    public static String  startHttpRequestGET(String textUrl) throws Exception {

        HttpURLConnection urlConnection = null;
        try {
            // get connect to HTTP server
            URL url = new URL(textUrl);
            urlConnection= (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");

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

    // Methode pour connect au server
    public static String httpConnectionRequestPOST(String textUrl, String methode, String json) throws Exception {
        // Get connection to HTTP server
        HttpURLConnection urlConnection = null;
        try {

            // get connect to HTTP server
            URL url = new URL(textUrl);
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("POST");
            urlConnection.setDoOutput(true);
            urlConnection.setRequestProperty("Content-Type", "application/json");

            //écriture de texte dans un flux de sortie
            OutputStream out = new BufferedOutputStream(urlConnection.getOutputStream());
            out.write(json.getBytes());
            out.close();
            //The connection is opened implicitly by calling getInputStream.
            urlConnection.getInputStream();

            // Creer 1 valeur type InputSteam pour recuper la flux
            InputStream in = new BufferedInputStream(urlConnection.getInputStream());
            //Permet de  décortiquer à partir d'un flux d'un certain nombre de methode indiqué
            Scanner scanner = new Scanner(in);
            // Creer 1 chaine type String pour stocker la reponse du server
            String responseServer = null;

            // prendre la response du server et mettre dans ce String
            responseServer = scanner.nextLine();

            String result = "Ce produit est modifié";

            return result;

        } catch (Exception e) {
            Log.e("APPLICATION_BOULANGERIE", " On n'a pas trouve http server ", e);

        } finally {
            if (urlConnection != null) urlConnection.disconnect();
        }
        return null;
    }


    public static String httpConnectionRequestDELETE(String textUrl, String methode) throws Exception {
        HttpURLConnection urlConnection = null;
        try {
            // get connect to HTTP server
            URL url = new URL(textUrl);
            urlConnection= (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("DELETE");
            urlConnection.setDoOutput(true);
            urlConnection.setRequestProperty("Content-Type", "application/json");
            urlConnection.connect();


            // Creer 1 valeur type InputSteam pour recuper la flux
            InputStream in = new BufferedInputStream( urlConnection.getInputStream());
            //Permet de  décortiquer à partir d'un flux d'un certain nombre de methode indiqué
            Scanner scanner = new Scanner(in);
            // il faut remplir ce string avec le reponse du server (à voir avec GSON)
            String responseServer = null;

            // Prendre la reponser du server et mettre dans ce string
            responseServer= scanner.nextLine();
            Log.i("APPLICATION_BOULANGERIE", "Resultat du produit "+ responseServer);

            String result = "Ce produit est supprimé!!!";
            // Close connection
            in.close();
            return result;

        }
        catch (Exception e){
            Log.e("Exchange-JSON", " On n'a pas trouve http server ", e);

        } finally {
            if(urlConnection != null) urlConnection.disconnect();
        }

        return null;
    }
}
