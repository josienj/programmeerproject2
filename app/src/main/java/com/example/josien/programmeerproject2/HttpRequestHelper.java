package com.example.josien.programmeerproject2;

import android.app.Activity;
import android.content.Context;
import android.util.Base64;
import android.util.Log;
import android.widget.Toast;

import org.apache.http.HttpHeaders;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;



public class HttpRequestHelper {

    static File file;

    // make string for URL
    private static final String url = "http://webservices.ns.nl/ns-api-avt?station=";
    private static BufferedReader bufferedReader;

    // method to download from server
    protected static synchronized String downloadFromServer(String... params) throws IOException {

        // declare return string result
        String result = "";

        // get chosen tag from argument
        String chosenStation = params[0];

        // complete string for URL
        String complete_URL_string = url + chosenStation;
        Log.d("HTTP", "downloadFromServer() returned: " + complete_URL_string);
        // turn string into URL
        URL url = null;

        try {
            url = new URL(complete_URL_string);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        // make the connection
        HttpURLConnection connection;
        if (url != null) {
            try {
                connection = (HttpURLConnection) url.openConnection();

                String userCredentials = "josienjansen1@gmail.com" + ":" + "OOmAtjby3uCWjhmXv1JuDQVdAahST1eCbDtPnpvh18Y6UQzxA6oS8A";
                String encoding = new String(android.util.Base64.encode(userCredentials.getBytes(), Base64.DEFAULT));
                encoding = encoding.replaceAll("\\s+", "");

                connection.setRequestProperty("Authorization", "Basic " + encoding);


                // open connection, set request method
                try {
                    connection.setRequestMethod("GET");
                } catch (ProtocolException e) {
                    e.printStackTrace();
                }

                // get response code
                Integer responseCode = null;
                try {
                    responseCode = connection.getResponseCode();
                    Log.d("Responsecode", "downloadFromServer() returned: " + responseCode);
                } catch (IOException e) {
                    e.printStackTrace();
                }

                // if responseCode is between 200-300, read inputstream
                if (200 >= responseCode && responseCode <= 299) {


                    InputStream inputStream = connection.getInputStream();
                    bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                    String line;
                    while ((line = bufferedReader.readLine()) != null) {
                        result = result + line;
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return result;
    }
}