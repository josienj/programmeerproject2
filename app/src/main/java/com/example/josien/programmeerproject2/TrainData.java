package com.example.josien.programmeerproject2;

/*
Josien Jansen
11162295
Programmeerproject
Universiteit van Amsterdam
 */


public class TrainData {

    private String data;
    private String ritnummer;
    private String vertrektijd;
    private String eindbestemming;

    public String getData(){
        return data;
    }

    public String getRitnummer() {
        return ritnummer;
    }

    public String getVertrektijd() {
        return vertrektijd;
    }

    public String getEindbestemming() {
        return eindbestemming;
    }

    public void setRitnummer(String ritnummer) {
        this.ritnummer = ritnummer;
    }

    public void setVertrektijd(String vertrektijd) {
        this.vertrektijd = vertrektijd;
    }

    public void setEindbestemming(String eindbestemming) {
        this.eindbestemming = eindbestemming;
    }

    public void setData(String data) {
        this.data = data;
    }
}