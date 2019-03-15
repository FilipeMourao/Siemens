package com.example.power.gasdetectorappFirebase;

import android.content.ContentValues;
import android.graphics.Color;
import android.os.Bundle;
import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.power.gasdetectorappFirebase.ObjectsAndDatabase.GasSensorDataBase;
import com.example.power.gasdetectorappFirebase.ObjectsAndDatabase.GasSensorMeasure;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.ValueDependentColor;
import com.jjoe64.graphview.series.BarGraphSeries;
import com.jjoe64.graphview.series.DataPoint;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class PlottingGraphics extends Activity {
    List<String> listOfGases = Arrays.asList(
            "Nitrogen"
            ,"Oxygen"
            ,"Argon"
            ,"Carbon dioxide"
            ,"Neon"
            ,"Helium"
            ,"Methane");

    List<Integer> colors = createColors(listOfGases.size());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plotting_graphics);
        final ListView listView = findViewById(R.id.listOfGases);
        GasSensorDataBase db = new GasSensorDataBase(this.getApplicationContext());
        db.getReadableDatabase();
        List<GasSensorMeasure> measures = db.getAllMeasures();
        //GasSensorMeasure measure = measures.get(measures.size() - 1);
        GasSensorMeasure measure = new GasSensorMeasure(0,0,0,0,0,0,"test");
        ContentValues values = getPercentageWithSensor(measure);
        GraphView graph = (GraphView) findViewById(R.id.graphView);
        BarGraphSeries<DataPoint> series = new BarGraphSeries<>();
        final List<String> listOfGasesView = new ArrayList<>();
        for (int i = 0; i < values.size() ; i++){
            series.appendData(new DataPoint(
                    i,
                    values.getAsDouble(listOfGases.get(i))),
                    true,
                    values.size());
            listOfGasesView.add(listOfGases.get(i) + " " + Double.toString(values.getAsDouble(listOfGases.get(i))) );
        }
        // styling
        series.setValueDependentColor(new ValueDependentColor<DataPoint>() {
            @Override
            public int get(DataPoint data) {
                //return Color.rgb((int) data.getX()*255/listOfGasesView.size(), (int) Math.abs((data.getY() - 1)*255/listOfGasesView.size()), 100);
                return colors.get((int) data.getX());

            }
        });
        listView.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, listOfGasesView) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View row = super.getView(position, convertView, parent);
                row.setBackgroundColor( colors.get(position));
                return row;
            }
        });


       series.setSpacing(50);
//
//// draw values on top
//        series.setDrawValuesOnTop(true);
//        series.setValuesOnTopColor(Color.RED);
        graph.addSeries(series);
    }

    public ContentValues getPercentageWithSensor(GasSensorMeasure measure) {
        ContentValues values = new ContentValues();
        // some calculation for the percentages;
        values.put(listOfGases.get(0),78.084);
        values.put(listOfGases.get(1),20.946);
        values.put(listOfGases.get(2),9.34);
        values.put(listOfGases.get(3),0.04);
        values.put(listOfGases.get(4),0.0018);
        values.put(listOfGases.get(5),0.00052);
        values.put(listOfGases.get(6),0.00017);
        return values;
    }

    public List<Integer> createColors(int size) {
        List<Integer> colors = new ArrayList<>();
        Random rand = new Random();
        for(int i = 0 ; i < size; i++) colors.add(Color.rgb(100,(int) (i*255/size), (int) Math.abs(rand.nextDouble()*255/size) ));
        return colors;
    }
}

