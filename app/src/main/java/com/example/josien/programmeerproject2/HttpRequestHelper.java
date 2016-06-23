package com.example.josien.programmeerproject2;

/*
*
*  Josien Jansen
*  11162295
*  Programmeerproject
*  06-2016
*  Universiteit van Amsterdam
*/

import android.util.Base64;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/*
This class handles the Http-request done by the user.
 */
public class HttpRequestHelper {

    // Make string for URL.
    private static final String url = "http://webservices.ns.nl/ns-api-avt?station=";

    // Method to download from server.
    protected static synchronized String downloadFromServer(String... params) {

        // Declare return string result.
        String result = "";

        // Get chosen tag from argument.
        String chosenStation = params[0];

        // Complete string for URL.
        String complete_URL_string = url + chosenStation;

        // Turn string into URL.
        URL url = null;

        try {
            url = new URL(complete_URL_string);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        // Make the connection.
        if (url != null)
            try {
                HttpURLConnection connect = (HttpURLConnection) url.openConnection();
                connect.setReadTimeout(10000);
                connect.setConnectTimeout(15000);
                connect.setRequestMethod("GET");

                // Handles basic authorization of the NS API.
                connect.setRequestProperty("Authorization", "Basic " + getConnectionAuthHeaders());

                // Get response code.
                Integer responseCode = connect.getResponseCode();

                // If responseCode is between 200-300, read inputstream.
                if (200 >= responseCode && responseCode <= 299) {
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(connect.getInputStream()));
                    String line;
                    while ((line = bufferedReader.readLine()) != null) {
                        result = result + line;
                    }
                }
                else {
                    // Read the right errorstream.
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(connect.getErrorStream()));
                    if (responseCode >= 300 && responseCode < 400) {
                        result = "ERROR: redirect error";
                    }
                    if (responseCode >= 400 && responseCode < 500) {
                        result = "ERROR: client error";
                    }
                    if (responseCode >= 500) {
                        result = "ERROR: server error";
                    }
                    String currentLine;
                    while ((currentLine = bufferedReader.readLine()) != null) {
                        result = result + currentLine;
                    }
                }

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