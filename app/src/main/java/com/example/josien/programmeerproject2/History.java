package com.example.josien.programmeerproject2;

/**
 * Created by Josien on 14-6-2016.
 */
public class History {

        private int _id;
        private String _eindbestemming;
        private String _vertrektijd;


        public History () {

        }

        public History(String _eindbestemming, String _vertrektijd) {
            this._eindbestemming = _eindbestemming;
            this._vertrektijd = _vertrektijd;
        }

        public void set_id(int _id) {
            this._id = _id;
        }

        public void set_eindbestemming(String _eindbestemming) {
            this._eindbestemming = _eindbestemming;
        }

        public int get_id() {
            return _id;
        }

        public String get_vertrektijd(){
            return _vertrektijd;
        }

        public void set_vertrektijd(String _vertrektijd){
            this._vertrektijd = _vertrektijd;
        }

        public String get_eindbestemming() {
            return _eindbestemming;
        }

        public String toString() {
            return _eindbestemming+_vertrektijd;
        }
    }
