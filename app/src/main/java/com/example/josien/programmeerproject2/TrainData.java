package com.example.josien.programmeerproject2;

/*
Josien Jansen
11162295
Programmeerproject
Universiteit van Amsterdam
 */


public class TrainData {

    private String ritnummer;
    private String vertrektijd;
    private String eindbestemming;
    private String treinsoort;
    private String routeText;
    private String vervoerder;
    private String vertrekspoor;


    public String getRitnummer() {
        return ritnummer;
    }

    public String getVertrektijd() {
        return vertrektijd;
    }

    public String getEindbestemming() {
        return eindbestemming;
    }

    public String getTreinsoort() {
        return treinsoort;
    }

    public String getRouteText() {
        return routeText;
    }

    public String getVervoerder() {
        return vervoerder;
    }

    public String getVertrekspoor() {
        return vertrekspoor;
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

    public void setTreinsoort(String treinsoort) {
        this.treinsoort = treinsoort;
    }

    public void setRouteText(String routeText) {
        this.routeText = routeText;
    }

    public void setVervoerder(String vervoerder) {
        this.vervoerder = vervoerder;
    }

    public void setVertrekspoor(String vertrekspoor) {
        this.vertrekspoor = vertrekspoor;
    }
}