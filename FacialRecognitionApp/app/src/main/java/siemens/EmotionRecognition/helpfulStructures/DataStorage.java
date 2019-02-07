package siemens.EmotionRecognition.helpfulStructures;

import android.app.Application;

import java.util.ArrayList;
import java.util.List;

public class DataStorage extends Application {
    public List<Result> resultList = new ArrayList<>();

    public List<Result> getResultList() {
        return resultList;
    }

    public void setResultList(List<Result> resultList) {
        this.resultList = resultList;
    }
}
