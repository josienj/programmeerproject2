package com.example.josien.programmeerproject2;

/*
Josien Jansen
11162295
Programmeerproject
Universiteit van Amsterdam
 */

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

/**
 * This class handles the http-request by getting the real data, parse it and return it the right
 * way.
 */
public class TrainAsyncTask extends AsyncTask<String, Integer, String> {

    // Declare variables.
    static final String KEY_VERTREKKENDETREIN = "VertrekkendeTrein";
    static final String KEY_EINDBESTEMMING = "EindBestemming";
    static final String KEY_VERTREKTIJD = "VertrekTijd";
    static final String KEY_RITNUMMER = "RitNummer";

    Context context;
    MainActivity activity;

    // constructor
    public TrainAsyncTask(MainActivity activity){
        this.activity = activity;
        this.context = this.activity.getApplicationContext();
    }


    @Override
    protected void onPreExecute(){
        // inform user
        Toast.makeText(context, "Loading...", Toast.LENGTH_SHORT).show();
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
    protected void onPostExecute(String result) {
        super.onPostExecute(result);

        // if nothing was found, inform user
        if (result.length() == 0) {
            Toast.makeText(context, "No data was found", Toast.LENGTH_SHORT).show();
        } else {
            // Parse XML

            List<TrainData> traindatas;
            traindatas = new ArrayList<>();

            TrainData traindata = null;

            String curtext = "";


            try {

                XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
                XmlPullParser xpp = factory.newPullParser();
                xpp.setInput(new StringReader(result));

                int event = xpp.getEventType();


                while (event != XmlPullParser.END_DOCUMENT) {
                    String name = xpp.getName();

                    switch (event) {
                        case XmlPullParser.START_TAG:
                            if (name.equalsIgnoreCase(KEY_VERTREKKENDETREIN)) {
                                traindata = new TrainData();
                            }
                            break;

                        case XmlPullParser.TEXT:
                            curtext = xpp.getText();
                            break;

                        case XmlPullParser.END_TAG:
                            if (name.equalsIgnoreCase(KEY_VERTREKKENDETREIN)) {
                                traindatas.add(traindata);
                            } else if (name.equalsIgnoreCase(KEY_RITNUMMER)) {
                                assert traindata != null;
                                traindata.setRitnummer(curtext);
                                Log.d("Ritnummer", "ParseXml() returned: " + curtext);
                            } else if (name.equalsIgnoreCase(KEY_VERTREKTIJD)) {
                                assert traindata != null;
                                String vertrektijd = curtext;
                                curtext = vertrektijd.substring(11, 16);
                                traindata.setVertrektijd(curtext);
                                Log.d("Vertrektijd", "ParseXml() returned: " + curtext);
                            } else if (name.equalsIgnoreCase(KEY_EINDBESTEMMING)) {
                                assert traindata != null;
                                traindata.setEindbestemming(curtext);
                                Log.d("Eindbestemming", "ParseXml() returned: " + curtext);
                            }
                            break;
                        default:
                            break;
                    }
                    event = xpp.next();
                }
            } catch (XmlPullParserException | IOException e) {
                e.printStackTrace();
            }
            // Return the data to be able to show it in ListView.
            this.activity.setData(traindatas);
        }

    }
    }
