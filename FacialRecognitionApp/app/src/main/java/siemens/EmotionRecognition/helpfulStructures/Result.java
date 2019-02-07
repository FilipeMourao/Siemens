package siemens.EmotionRecognition.helpfulStructures;

import android.graphics.Bitmap;

import java.util.UUID;

public class Result {
    private String  id;
    private Bitmap imageBitMap;
    private  String resultGenre;
    private String resultEmotion;

    public Result(Bitmap imageBitMap, String resultGenre, String resultEmotion) {
        this.id = UUID.randomUUID().toString();
        this.imageBitMap = imageBitMap;
        this.resultGenre = resultGenre;
        this.resultEmotion = resultEmotion;
    }
    public Result(Bitmap imageBitMap) {
        this.id = UUID.randomUUID().toString();
        this.imageBitMap = imageBitMap;
        this.resultGenre = "";
        this.resultEmotion = "";
    }

    public String getId() {
        return id;
    }


    public Bitmap getImageBitMap() {
        return imageBitMap;
    }

    public void setImageBitMap(Bitmap imageBitMap) {
        this.imageBitMap = imageBitMap;
    }

    public String getResultGenre() {
        return resultGenre;
    }

    public void setResultGenre(String resultGenre) {
        this.resultGenre = resultGenre;
    }

    public String getResultEmotion() {
        return resultEmotion;
    }

    public void setResultEmotion(String resultEmotion) {
        this.resultEmotion = resultEmotion;
    }
}
