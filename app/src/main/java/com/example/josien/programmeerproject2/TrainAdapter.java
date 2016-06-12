package com.example.josien.programmeerproject2;

/*
Josien Jansen
11162295
Programmeerproject
Universiteit van Amsterdam
 */

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class TrainAdapter extends ArrayAdapter<TrainData> {

    public TrainAdapter(Context ctx, int textView, List<TrainData> trains){
        super(ctx, textView, trains);
    }

   @Override
    public View getView(int pos, View convertView, ViewGroup parent){
       RelativeLayout row = (RelativeLayout)convertView;

       if(row == null){
           LayoutInflater inflater = (LayoutInflater)parent.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
           row = (RelativeLayout)inflater.inflate(R.layout.row_layout, null);
       }

       TextView eindbestemming = (TextView)row.findViewById(R.id.eindbestemming);
       TextView vertrektijd = (TextView)row.findViewById(R.id.vertrektijd);

       eindbestemming.setText(getItem(pos).getEindbestemming());
       vertrektijd.setText(getItem(pos).getVertrektijd());

       return row;
   }

}