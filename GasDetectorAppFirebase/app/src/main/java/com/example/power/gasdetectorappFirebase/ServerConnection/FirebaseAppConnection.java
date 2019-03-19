package com.example.power.gasdetectorappFirebase.ServerConnection;

import android.content.Context;
import com.example.power.gasdetectorappFirebase.ObjectsAndDatabase.ClassificationGasMeasure;
import com.example.power.gasdetectorappFirebase.ObjectsAndDatabase.GasSensorDataBase;
import com.example.power.gasdetectorappFirebase.ObjectsAndDatabase.GasSensorMeasure;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import java.util.List;

// this function is used to save data in the online  database
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
       // mDatabase.child("Measures").removeValue();
        for (GasSensorMeasure gasSensorMeasure : gasSensorMeasures) saveSensorMeasure(gasSensorMeasure);
    }
    public void saveSensorMeasure(GasSensorMeasure gasSensorMeasure){

        mDatabase = FirebaseDatabase.getInstance().getReference();// get the firebase instance
        mDatabase.child("Measures").child(gasSensorMeasure.getUniqueID()).setValue(gasSensorMeasure);// create a child and set the value of the child
    }
    public void saveSensorMeasuresClassification(){
        List<ClassificationGasMeasure> gasSensorMeasures =  gasSensorDataBase.getAllClassifications() ;
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mDatabase.child("Classifications").removeValue();
        for (ClassificationGasMeasure  classificationGasMeasure : gasSensorMeasures) saveSensorMeasureClassification(classificationGasMeasure);

    }
    public void saveSensorMeasureClassification(ClassificationGasMeasure classificationGasMeasure){
        mDatabase = FirebaseDatabase.getInstance().getReference();
        String uniqueKey = classificationGasMeasure.getUniqueID();
        mDatabase.child("Classifications").child(uniqueKey).setValue(classificationGasMeasure);
    }
    public void deleteGasSensorMeasure(GasSensorMeasure gasSensorMeasure){
        gasSensorDataBase.removeGasSensorMeasure(gasSensorMeasure);
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mDatabase.child("Measures").child(gasSensorMeasure.getUniqueID()).removeValue();
    }
    public void deleteGasSensorClassification(ClassificationGasMeasure classificationGasMeasure){
        gasSensorDataBase.removeGasSensorClassification(classificationGasMeasure);
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mDatabase.child("Classifications").child(classificationGasMeasure.getUniqueID()).removeValue();
    }
}
// This is the way to call for the firebase parameters, but it`s done async so you have to write this function and what to do next of getting the values in the correct class
//    public void getGasSensorMeasures(){
//        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Measures");
//        reference.addListenerForSingleValueEvent(new ValueEventListener() {
//            @RequiresApi(api = Build.VERSION_CODES.N)
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                if (dataSnapshot.exists()) {
//                    // dataSnapshot is the "issue" node with all children with id 0
//                    for (DataSnapshot eventSnapshot : dataSnapshot.getChildren()) {
//                        GasSensorMeasure gasSensorMeasure = eventSnapshot.getValue(GasSensorMeasure.class);
//                        if (gasSensorMeasure != null)
//                            gasSensorDataBase.addMeasure(gasSensorMeasure);
//                    }
//
//                }
//            }
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//
//            }
//        });
//    }