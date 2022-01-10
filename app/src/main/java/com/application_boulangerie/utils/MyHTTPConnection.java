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
        // Creer 1 valeur type InputSteam pour recuper la flux
        InputStream in = null;
        // Creer 1 valeur type scanner pour lire le resutat du serveur
        Scanner scanner = null;
        // Creer 1 objet type HTTPURLConnection pour prendre la connection du HTTP server
        HttpURLConnection urlConnection = null;
        try {
            // get connect to HTTP server
            URL url = new URL(textUrl);
            urlConnection= (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod(Methode.GET.toString());

            //  recuper la flux et mettre dans objet type InputSteam
            in = new BufferedInputStream( urlConnection.getInputStream());
            //Permet de  décortiquer à partir d'un flux d'un certain nombre de methode indiqué
            scanner= new Scanner(in);
            // Creer 1 chaine type String pour stocker la reponse du server
            String responseServer = null;

            // mettre la reponse du sever au ce String
            responseServer= scanner.nextLine();

            return responseServer;
        }
        catch (Exception e){
            Log.e("Exchange-JSON", " On n'a pas trouve http server ", e);

        } finally {
            scanner.close();
            in.close();
            if(urlConnection != null) urlConnection.disconnect();
        }
        return null;
    }

    // Methode pour connect au server
    public static String httpConnectionRequestPOST(String textUrl, String json) throws Exception {
        // Creer 1 valeur type InputSteam pour recuper la flux
        InputStream in = null;
        // Creer 1 valeur type scanner pour lire le resutat du serveur
        Scanner scanner = null;
        // Creer 1 objet type HTTPURLConnection pour prendre la connection du HTTP server
        HttpURLConnection urlConnection = null;
        try {

            // get connect to HTTP server
            URL url = new URL(textUrl);
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod(Methode.POST.toString());
            urlConnection.setDoOutput(true);
            urlConnection.setRequestProperty("Content-Type", "application/json");

            //écriture de texte dans un flux de sortie
            OutputStream out = new BufferedOutputStream(urlConnection.getOutputStream());
            out.write(json.getBytes());
            out.close();
            //The connection is opened implicitly by calling getInputStream.
            urlConnection.getInputStream();

            //  recuper la flux et mettre dans objet type InputSteam
           in = new BufferedInputStream(urlConnection.getInputStream());
            //Permet de  décortiquer à partir d'un flux d'un certain nombre de methode indiqué
            scanner = new Scanner(in);
            // Creer 1 chaine type String pour stocker la reponse du server
            String responseServer = null;

            // prendre la response du server et mettre dans ce String
            responseServer = scanner.nextLine();

            return responseServer;

        } catch (Exception e) {
            Log.e("APPLICATION_BOULANGERIE", " On n'a pas trouve http server ", e);

        } finally {
            scanner.close();
            in.close();
            if (urlConnection != null) urlConnection.disconnect();
        }
        return null;
    }


    public static String httpConnectionRequestDELETE(String textUrl) throws Exception {
        // Creer 1 valeur type InputSteam pour recuper la flux
        InputStream in = null;
        // Creer 1 valeur type scanner pour lire le resutat du serveur
        Scanner scanner = null;
        // Creer 1 objet type HTTPURLConnection pour prendre la connection du HTTP server
        HttpURLConnection urlConnection = null;
        try {
            // get connect to HTTP server
            URL url = new URL(textUrl);
            urlConnection= (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod(Methode.DELETE.toString());
            urlConnection.setDoOutput(true);
            urlConnection.setRequestProperty("Content-Type", "application/json");
            urlConnection.connect();


            //  recuper la flux et mettre dans objet type InputSteam
            in = new BufferedInputStream( urlConnection.getInputStream());
            //Permet de  décortiquer à partir d'un flux d'un certain nombre de methode indiqué
            scanner = new Scanner(in);
            // il faut remplir ce string avec le reponse du server (à voir avec GSON)
            String responseServer = null;

            // Prendre la reponser du server et mettre dans ce string
            responseServer= scanner.nextLine();
            Log.i("APPLICATION_BOULANGERIE", "Resultat du produit "+ responseServer);

            return responseServer;

        }
        catch (Exception e){
            Log.e("Exchange-JSON", " On n'a pas trouve http server ", e);

        } finally {
            scanner.close();
            in.close();
            if(urlConnection != null) urlConnection.disconnect();
        }

        return null;
    }
}
