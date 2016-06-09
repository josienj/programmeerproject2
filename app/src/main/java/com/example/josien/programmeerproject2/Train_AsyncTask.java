package com.example.josien.programmeerproject2;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

/*
This Activity handles the situations the data is going through with the four methods: onPreExecute,
doinBackground, onProgressUpdate and onPostExecute.
 */

public class Train_AsyncTask extends AsyncTask<String, Integer, String> {

    Context context;
    MainActivity activity;
    static File file;
    private FileOutputStream outputStream;

    // constructor
    public Train_AsyncTask(MainActivity activity) {
        this.activity = activity;
        this.context = this.activity.getApplicationContext();
    }

    @Override
    protected void onPreExecute() {
        // inform user
        Toast.makeText(context, "Getting data from server", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected String doInBackground(String... params) {
        // fetch data
        try {
            return HttpRequestHelper.downloadFromServer(params);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }

    @Override
    protected void onProgressUpdate(Integer... values) {
        super.onProgressUpdate(values);
    }

    /*
    This method handles the Data after the request has been made, when there is no data found,
    inform the user and otherwise parse JSON to get the data the right way.
     */
    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);
        Log.d("Result", "onPostExecute() returned: " + result);

        // Maak nou gvd eenfile dan jeweet
        try {
            new BufferedWriter(new FileWriter(context.getFilesDir() + "data.xml"));
        } catch (IOException e) {
            e.printStackTrace();
        }


        File file = new File(context.getFilesDir() + "data.xml");

        try {
            outputStream = new FileOutputStream(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        try {
            outputStream.write(result.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            outputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        // if nothing was found, inform user
        if (result.length() == 0) {
            Toast.makeText(context, "No data was found", Toast.LENGTH_SHORT).show();
        } else {

            // Create new arraylist for Traindata objects
            ArrayList<TrainData> trainData = new ArrayList<>();

            Xml vertrekkende_trein = null;
            try {
                vertrekkende_trein = new Xml(context.getFilesDir() + "data.xml","VertrekkendeTrein");
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }

            Xml eindbestemming = vertrekkende_trein.child("EindBestemming");
            System.out.println("EindBestemming: "+ vertrekkende_trein.child("EindBestemming").content());

            Xml ritnummer = vertrekkende_trein.child("RitNummer");
            System.out.println("RitNummer: "+ vertrekkende_trein.child("RitNummer").content());

            Xml vertrektijd = vertrekkende_trein.child("VertrekTijd");
            System.out.println("VertrekTijd: "+ vertrekkende_trein.child("VertrekTijd").content());



                // Add new MinisterieData object with title and itemcontent to newsData
                trainData.add(new TrainData(eindbestemming, vertrektijd, ritnummer));

                // Set data from newsdata in listview
                this.activity.setData(trainData);


            }
            
        }


    }


