package siemens.EmotionRecognition.helpfulStructures;

import android.graphics.Bitmap;
import android.os.Build;
import android.support.annotation.RequiresApi;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

public class Result {
    private int  id;
    private int API_Type = 0;
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
        if (emotionString.isEmpty()) return "No emotion";
        if (emotionValues.isEmpty()) return "No emotion";
        int index = emotionValues.indexOf(Collections.max(emotionValues));
        String emoji = checkEmotion(emotionString.get(index));
       String detailString = emotionString.get(index)+" "+ emoji ;
        return detailString;
    }
    public float getMostPossibleEmotionValue(){
        if (emotionValues.isEmpty()) return 0.0f;
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
    public int getAPI_Type() {
        return API_Type;
    }

    public void setAPI_Type(int API_Type) {
        this.API_Type = API_Type;
    }

    public void setAge(String age) {
        this.age = age;
    }
    @RequiresApi(api = Build.VERSION_CODES.N)
    public List<String> getEmotionList(){
        List<String> emotionList = new ArrayList<>();
        if (emotionValues.isEmpty() || emotionString.isEmpty()) return emotionList;
        List<Float> emotionValueCopy = emotionValues;
        for (float value: emotionValueCopy ) {
            if (value < 0) value =0;
        }
        String emoji;
        String detailString;
        float totalValue = (emotionValueCopy.stream().reduce((a,b)-> a+b)).get();
        float value;
        DecimalFormat df = new DecimalFormat("#.###");
        for (int i = 0; i< emotionString.size(); i++){
            emoji = checkEmotion(emotionString.get(i));
            value =(emotionValues.get(i))/totalValue;
            if (value < 0) value = 0;
            detailString = emotionString.get(i) + " "+ emoji + " "+ df.format(value);
            emotionList.add(detailString);
        }
        return emotionList;
    }
    public List<String> getDetailsList(){
        List<String> detailsList = new ArrayList<>();
        String idString = "Image ID:" + Integer.toString(getId());
        String ageString = getAge();
        String ethinicityString = "Ethinicity: " + getEthinicity();
        String genreString = "Genre: " + getGender();
        String apiType;
        switch (getAPI_Type()){
            case 1:
                apiType = "API: Affectivia";
                break;
            case 2:
                apiType = "API: Microssoft";
                ageString = "Age: " + getAge();
                break;
                default:
                    apiType = "API: not provided";
                    break;
        }
        detailsList.add(idString);detailsList.add(ageString);
        detailsList.add(genreString);detailsList.add(ethinicityString);
        detailsList.add(apiType);
        return detailsList;
    }
    public String checkEmotion(String emotion){
        String anger  = "\uD83D\uDE21";
        String contempt  = "\uD83D\uDE11";
        String disgust  = "\uD83D\uDE12";
        String engagement  = "\uD83E\uDD14";
        String fear  = "\uD83D\uDE27";
        String joy  = "\uD83D\uDE00";
        String sadness  = "\uD83D\uDE22";
        String surprise  = "\uD83D\uDE2F";
        String valence  = "\uD83D\uDE0E";
        String neutral  = "\uD83D\uDE10";
        switch (emotion){
            case "anger":
                return anger;
            case "contempt":
                return contempt;
            case "disgust":
                return disgust;
            case "engagement":
                return engagement;
            case "fear":
                return fear;
            case "joy":
                return joy;
            case "hapiness":
                return joy;
            case "sadness":
                return sadness;
            case "surprise":
                return surprise;
            case "valence":
                return valence;
            case "neutral":
                return neutral;
                default:
                    return "\uDE21";
        }
    }
}
