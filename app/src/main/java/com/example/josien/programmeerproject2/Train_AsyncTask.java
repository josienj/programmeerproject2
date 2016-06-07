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

import net.sf.json.JSON;
import net.sf.json.JSONArray;
import net.sf.json.JSONException;
import net.sf.json.JSONObject;
import net.sf.json.xml.XMLSerializer;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;





/*
This Activity handles the situations the data is going through with the four methods: onPreExecute,
doinBackground, onProgressUpdate and onPostExecute.
 */

public class Train_AsyncTask extends AsyncTask<String, Integer, String> {

    Context context;
    MainActivity activity;
    XmlPullParserFactory pullParserFactory;
    XmlPullParser parser;

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
        return HttpRequestHelper.downloadFromServer(params);
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

        // if nothing was found, inform user
        if (result.length() == 0) {
            Toast.makeText(context, "No data was found", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(context, "OKE" + result, Toast.LENGTH_SHORT).show();

            /*
                ArrayList<TrainData> leavingtrains = null;
                int eventType = parser.getEventType();
                TrainData currentTrain = null;

                while (eventType != XmlPullParser.END_DOCUMENT){
                    String name = null;
                    switch (eventType){
                        case XmlPullParser.START_DOCUMENT:
                            leavingtrains = new ArrayList();
                            break;
                        case XmlPullParser.START_TAG:
                            name = parser.getName();
                            if (name == "VertrekkendeTrein"){
                                currentTrain = new TrainData();
                            } else if (currentTrain != null){
                                if (name == "EindBestemming"){
                                    currentTrain.eindbestemming() = parser.nextText();
                                } else if (name == "VertrekTijd"){
                                    currentTrain.getVertrektijd() = parser.nextText();
                                } else if (name == "RitNummer"){
                                    currentTrain.getRitnummer()= parser.nextText();
                                }
                            }
                            break;
                        case XmlPullParser.END_TAG:
                            name = parser.getName();
                            if (name.equalsIgnoreCase("VertrekkendeTrein") && currentTrain != null){
                                leavingtrains.add(currentTrain);
                            }
                    }
                    eventType = parser.next();
                }

                this.activity.setData(TrainData);
            }*/
/*
            ArrayList<TrainData> leavingData = new ArrayList<>();
            try {
                XMLSerializer serializer = new XMLSerializer();
                JSON json = serializer.read( result );
                JSONObject respObj = new JSONObject();
                JSONArray actuelevertrektijden = respObj.getJSONArray("ActueleVertrekTijden");
                JSONArray vertrekkendetrein = respObj.getJSONArray("VertrekkendeTrein");

                // Loop through the items till it ends
                for (int i = 0; i < vertrekkendetrein.size(); i++) {
                    JSONObject tijd = vertrekkendetrein.getJSONObject(i);
                    String eindbestemming = tijd.getString("EindBestemming");
                    String vertrektijd = tijd.getString("VertrekTijd");
                    String ritnummer = tijd.getString("RitNummer");
                    leavingData.add(new TrainData(eindbestemming, vertrektijd, ritnummer));
                }
            }
            catch (JSONException e){
                Toast.makeText(context, "Dit gaat niet goed" + result, Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }      */
        }


    }
}






