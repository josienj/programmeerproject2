package com.example.josien.programmeerproject2;

/*
Josien Jansen
11162295
Programmeerproject
Universiteit van Amsterdam
 */


public class TrainData {

    private Xml eindbestemming;
    private Xml vertrektijd;
    private Xml ritnummer;

    public TrainData (Xml eindbestemming, Xml vertrektijd, Xml ritnummer){
        super();
        this.eindbestemming = eindbestemming;
        this.vertrektijd = vertrektijd;
        this.ritnummer = ritnummer;
    }

    public Xml getEindbestemming(){
        return eindbestemming;
    }


    public Xml getVertrektijd(){
        return vertrektijd;
    }

    public Xml getRitnummer(){
        return ritnummer;
    }

}