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
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class TrainAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<TrainData> trains;

    public TrainAdapter (Context context, ArrayList<TrainData> trains){
        this.context = context;
        this.trains = trains;
    }

    @Override
    public int getCount(){
        return this.trains.size();
    }

    @Override
    public Object getItem(int arg0){
        return null;
    }

    @Override
    public long getItemId(int pos){
        return pos;
    }

    @Override
    public View getView(int pos, View view, ViewGroup parent){
        if (view == null){
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.row_layout, parent, false);
        }
        TrainData train = trains.get(pos);
        TextView eindbestemming = (TextView) view.findViewById(R.id.eindbestemming);
        TextView vertrektijd = (TextView) view.findViewById(R.id.vertrektijd);
        eindbestemming.setText(train.getEindbestemming());
        vertrektijd.setText(train.getVertrektijd());
        return view;
    }
}