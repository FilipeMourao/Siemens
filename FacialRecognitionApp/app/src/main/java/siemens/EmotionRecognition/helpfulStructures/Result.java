package siemens.EmotionRecognition.helpfulStructures;

import android.graphics.Bitmap;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

public class Result {
    private int  id;
    private Bitmap imageBitMap;
    private List<String> emotionString = new ArrayList<>();
    private List<Float> emotionValues = new ArrayList<>();
    private String gender = "Unknow";
    private String ethinicity = "Unknow";
    private String age = "Unknow";
    public Result(Bitmap imageBitMap) {
        this.imageBitMap = imageBitMap;

    }

    public Result(Bitmap imageBitMap, List<String> emotionString, List<Float> emotionValues, String gender, String ethinicity, String age) {
        this.imageBitMap = imageBitMap;
        this.emotionString = emotionString;
        this.emotionValues = emotionValues;
        this.gender = gender;
        this.ethinicity = ethinicity;
        this.age = age;
    }

    public List<String> getEmotionString() {
        return emotionString;
    }

    public void setEmotionString(List<String> emotionString) {
        this.emotionString = emotionString;
    }

    public List<Float> getEmotionValues() {
        return emotionValues;
    }

    public void setEmotionValues(List<Float> emotionValues) {
        this.emotionValues = emotionValues;
    }
    public String getMostPossibleEmotionName(){
        int index = emotionValues.indexOf(Collections.max(emotionValues));
        return emotionString.get(index);
    }
    public float getMostPossibleEmotionValue(){
        return Collections.max(emotionValues);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Bitmap getImageBitMap() {
        return imageBitMap;
    }

    public void setImageBitMap(Bitmap imageBitMap) {
        this.imageBitMap = imageBitMap;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getEthinicity() {
        return ethinicity;
    }

    public void setEthinicity(String ethinicity) {
        this.ethinicity = ethinicity;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }
}
