package com.example.josien.programmeerproject2;

/*
Josien Jansen
11162295
Programmeerproject
Universiteit van Amsterdam
 */

import android.util.Base64;
import android.util.Log;

import org.apache.http.HttpHeaders;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;



public class HttpRequestHelper {

    // make string for URL
    private static final String url = "http://webservices.ns.nl/ns-api-avt?station=";

    /*
    final String headerValue = "josienjansen1@gmail.com:OOmAtjby3uCWjhmXv1JuDQVdAahST1eCbDtPnpvh18Y6UQzxA6oS8A";
    String basicHeaderValue = "Basic " + Base64.encode(headerValue);
    if (basicHeaderValue.endsWith("\n")) {
        basicHeaderValue = basicHeaderValue.substring(0, basicHeaderValue.length() - 1);
    }

    HttpHeaders headers = new HttpHeaders();
    headers.setAuthorization(basicHeaderValue);
    httpRequest.setHeaders(headers);
*/
    // method to download from server
    protected static synchronized String downloadFromServer(String... params) {


        // declare return string result
        String result = "";

        // get chosen tag from argument
        String chosenStation = params[0];

        // complete string for URL
        String complete_URL_string = url + chosenStation;
        Log.d("url", "downloadFromServer() returned: " + complete_URL_string);
        // turn string into URL
        URL url = null;

        try {
            url = new URL(complete_URL_string);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        // make the connection
        HttpURLConnection connection;
        if (url != null)
            try {
                connection = (HttpURLConnection) url.openConnection();

                String userCredentials = "josienjansen1@gmail.com" + ":" + "OOmAtjby3uCWjhmXv1JuDQVdAahST1eCbDtPnpvh18Y6UQzxA6oS8A";
                String encoding = new String(android.util.Base64.encode(userCredentials.getBytes(), Base64.DEFAULT));
                encoding = encoding.replaceAll("\\s+","");

                connection.setRequestProperty("Authorization", "Basic " + encoding);

                // open connection, set request method
                connection.setRequestMethod("GET");
                connection.setDoOutput(true);

                // get response code
                Integer responseCode = connection.getResponseCode();
                Log.d("OK", "downloadFromServer() returned: " + result);

                // if responseCode is between 200-300, read inputstream
                if (200 >= responseCode && responseCode <= 299) {
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                    String line;
                    while ((line = bufferedReader.readLine()) != null) {
                        result = result + line;
                        Log.d("X", "downloadFromServer() returned: " + line + result);
                    }
                }
                // else, read error stream
                else {
                    Log.d("snapikdit? ", "downloadFromServer() returned: " + responseCode);
                }

            } catch (IOException e) {
                e.printStackTrace();
            }

        return result;
    }

}