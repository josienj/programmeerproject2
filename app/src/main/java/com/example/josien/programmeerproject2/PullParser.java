package com.example.josien.programmeerproject2;

import android.content.Context;
import android.util.Log;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Josien on 12-6-2016.
 */
public class PullParser {

    static final String KEY_VERTREKKENDETREIN = "VertrekkendeTrein";
    static final String KEY_EINDBESTEMMING = "EindBestemming";
    static final String KEY_VERTREKTIJD = "VertrekTijd";
    static final String KEY_RITNUMMER = "RitNummer";

    public PullParser(XmlPullParser myParser) {

            List<TrainData> trainDatas;
            trainDatas = new ArrayList<>();

            TrainData traindata = null;

            String curtext = "";

            try {

                XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
                XmlPullParser xpp = factory.newPullParser();
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
                                trainDatas.add(traindata);
                            } else if (name.equalsIgnoreCase(KEY_RITNUMMER)) {
                                traindata.setRitnummer(curtext);
                                Log.d("Ritnummer", "ParseXml() returned: " + curtext);
                            } else if (name.equalsIgnoreCase(KEY_VERTREKTIJD)) {
                                traindata.setVertrektijd(curtext);
                                Log.d("Vertrektijd", "ParseXml() returned: " + curtext);
                            } else if (name.equalsIgnoreCase(KEY_EINDBESTEMMING)) {
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
        }



    public static List<TrainData> get_data(Context ctx) {

        List<TrainData> trainDatas;
        trainDatas = new ArrayList<>();

        TrainData traindata = null;

        String curtext = "";

        try {

            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            XmlPullParser xpp = factory.newPullParser();
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
                            trainDatas.add(traindata);
                        } else if (name.equalsIgnoreCase(KEY_RITNUMMER)) {
                            traindata.setRitnummer(curtext);
                            Log.d("Ritnummer", "ParseXml() returned: " + curtext);
                        } else if (name.equalsIgnoreCase(KEY_VERTREKTIJD)) {
                            traindata.setVertrektijd(curtext);
                            Log.d("Vertrektijd", "ParseXml() returned: " + curtext);
                        } else if (name.equalsIgnoreCase(KEY_EINDBESTEMMING)) {
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
        return trainDatas;
    }
}
