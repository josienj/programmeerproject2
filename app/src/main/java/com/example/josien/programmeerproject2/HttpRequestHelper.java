package com.example.josien.programmeerproject2;

/*
Josien Jansen
11162295
Programmeerproject
Universiteit van Amsterdam
 */

import android.util.Base64;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/*
This class handles the Http-request done by the user.
 */
public class HttpRequestHelper {

    // make string for URL
    private static final String url = "http://webservices.ns.nl/ns-api-avt?station=";

    // method to download from server
    protected static synchronized String downloadFromServer(String... params) {

        // declare return string result
        String result = "";

        // get chosen tag from argument
        String chosenStation = params[0];

        // complete string for URL
        String complete_URL_string = url + chosenStation;
        Log.d("URL", "downloadFromServer() returned: " + complete_URL_string);
        // turn string into URL
        URL url = null;

        try {
            url = new URL(complete_URL_string);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        // make the connection
        if (url != null)
            try {
                HttpURLConnection connect = (HttpURLConnection) url.openConnection();
                connect.setReadTimeout(10000);
                connect.setConnectTimeout(15000);
                connect.setRequestMethod("GET");

                // handles basic authorization of the NS API
                connect.setRequestProperty("Authorization", "Basic " + getConnectionAuthHeaders());

                // get response code
                Integer responseCode = connect.getResponseCode();

                // if responseCode is between 200-300, read inputstream
                if (200 >= responseCode && responseCode <= 299) {
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(connect.getInputStream()));
                    String line;
                    while ((line = bufferedReader.readLine()) != null) {
                        result = result + line;
                    }
                }




                InputStream stream = connect.getInputStream();


            } catch (IOException e) {
                e.printStackTrace();
            }

        return result;
    }

    /*
    This method handles the basic Authorization the NS uses for being able to see the data of the
    APIs.
     */
    private static String getConnectionAuthHeaders()
    {
        String userCredentials = "josienjansen1@gmail.com" + ":" + "OOmAtjby3uCWjhmXv1JuDQVdAahST1eCbDtPnpvh18Y6UQzxA6oS8A";
        String encoding = new String(android.util.Base64.encode(userCredentials.getBytes(), Base64.DEFAULT));
        encoding = encoding.replaceAll("\\s+", "");

        return encoding;
    }

}