package com.example.power.gasdetectorappFirebase.ServerConnection;

import android.content.Context;
import android.os.Build;
import android.support.annotation.RequiresApi;

import com.example.power.gasdetectorappFirebase.ObjectsAndDatabase.GasSensorDataBase;
import com.example.power.gasdetectorappFirebase.ObjectsAndDatabase.GasSensorMeasure;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

public class FirebaseAppConnection {
    Context context;
    private DatabaseReference mDatabase;
    private GasSensorDataBase gasSensorDataBase;
    public FirebaseAppConnection(Context context) {
        this.context = context;
        gasSensorDataBase = new GasSensorDataBase(context);
    }
    public void saveSensorMeasures(){
        List<GasSensorMeasure> gasSensorMeasures =  gasSensorDataBase.getAllMeasures() ;
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mDatabase.child("Measure").removeValue();
        for (GasSensorMeasure gasSensorMeasure : gasSensorMeasures) saveSensorMeasure(gasSensorMeasure);
    }
    public void saveSensorMeasure(GasSensorMeasure gasSensorMeasure){
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mDatabase.child("Measure").child(gasSensorMeasure.getUniquID()).setValue(gasSensorMeasure);
    }
    public void getGasSensorMeasures(){
        gasSensorDataBase.removeAllMeasures();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Measures");
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    // dataSnapshot is the "issue" node with all children with id 0
                    for (DataSnapshot eventSnapshot : dataSnapshot.getChildren()) {
                        GasSensorMeasure gasSensorMeasure = eventSnapshot.getValue(GasSensorMeasure.class);
                        if (gasSensorMeasure != null)
                            gasSensorDataBase.addMeasure(gasSensorMeasure);
                    }
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
    public void deleteGasSensorMeasure(GasSensorMeasure gasSensorMeasure){ // take the user out of the current event;
        gasSensorDataBase.removeGasSensorMeasure(gasSensorMeasure);
        saveSensorMeasures();
    }
}
