package com.example.josien.programmeerproject2;

/*
Josien Jansen
11162295
Programmeerproject
Universiteit van Amsterdam
 */


public class TrainData {

    private String eindbestemming;
    private String vertrektijd;
    private String ritnummer;

    public TrainData (String eindbestemming, String vertrektijd, String ritnummer){
        super();
        this.eindbestemming = eindbestemming;
        this.vertrektijd = vertrektijd;
        this.ritnummer = ritnummer;
    }

    public String getEindbestemming(){
        return eindbestemming;
    }

    public void setEindbestemming(String eindbestemming){
        this.eindbestemming = eindbestemming;
    }

    public String getVertrektijd(){
        return vertrektijd;
    }

    public void setVertrektijd(String vertrektijd){
        this.vertrektijd = vertrektijd;
    }

    public String getRitnummer(){
        return ritnummer;
    }

    public void setRitnummer(String ritnummer){
        this.ritnummer = ritnummer;
    }
}