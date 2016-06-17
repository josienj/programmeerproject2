package com.example.josien.programmeerproject2;

public class History {

        private int _id;
        private String _eindbestemming;
        private String _vertrektijd;


        public History () {
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

        public void set_vertrektijd(String _vertrektijd){
            this._vertrektijd = _vertrektijd;
        }

        public String toString() {
            return _eindbestemming+_vertrektijd;
        }
    }
