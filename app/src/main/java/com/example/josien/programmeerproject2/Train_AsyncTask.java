package com.example.josien.programmeerproject2;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import net.sf.json.JSON;
import net.sf.json.xml.XMLSerializer;

import java.util.ArrayList;

/*
This Activity handles the situations the data is going through with the four methods: onPreExecute,
doinBackground, onProgressUpdate and onPostExecute.
 */

public class Train_AsyncTask extends AsyncTask<String, Integer, String> {

    Context context;
    MainActivity activity;

    // constructor
    public Train_AsyncTask(MainActivity activity){
        this.activity = activity;
        this.context = this.activity.getApplicationContext();
    }

    @Override
    protected void onPreExecute(){
        // inform user
        Toast.makeText(context, "Getting data from server", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected String doInBackground(String... params){
        // fetch data
        return HttpRequestHelper.downloadFromServer(params);
    }

    @Override
    protected void onProgressUpdate(Integer...values){
        super.onProgressUpdate(values);
    }

    /*
    This method handles the Data after the request has been made, when there is no data found,
    inform the user and otherwise parse JSON to get the data the right way.
     */
    @Override
    protected void onPostExecute(String result){
        super.onPostExecute(result);

        // if nothing was found, inform user
        if (result.length() == 0){
            Toast.makeText(context, "No data was found", Toast.LENGTH_SHORT).show();
        }

        else {
            // Create new arraylist for Traindata objects
            ArrayList<TrainData> trainData = new ArrayList<>();

            try {
                String xml = result;
                XMLSerializer xmlSerializer = new XMLSerializer();
                JSON json = xmlSerializer.read( xml );

                // Make new JSONArray from result
                org.json.JSONObject respObj = new org.json.JSONObject(result);
                org.json.JSONObject ActVertrektijd = respObj.getJSONObject("ActueleVertrektijden");
                org.json.JSONArray VertrekkendeTrein = ActVertrektijd.getJSONArray("VertrekkendeTrein");


                // For whole JSONArray
                for (int i = 0; i < VertrekkendeTrein.length(); i++) {
                    // Get JSON object
                    org.json.JSONObject trein = VertrekkendeTrein.getJSONObject(i);
                    String eindbestemming = trein.getString("EindBestemming");
                    String vertrektijd = trein.getString("VertrekTijd");
                    String ritnummer = trein.getString("RitNummer");

                    // Add new MinisterieData object with title and itemcontent to newsData
                    trainData.add(new TrainData(eindbestemming, vertrektijd, ritnummer));

                    // Set data from newsdata in listview
                    this.activity.setData(trainData);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }


        }


    }


        }

