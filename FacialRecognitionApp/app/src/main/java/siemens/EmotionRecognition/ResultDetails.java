package siemens.EmotionRecognition;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import siemens.EmotionRecognition.helpfulStructures.ArrayAdapterEmotion;
import siemens.EmotionRecognition.helpfulStructures.Result;

public class ResultDetails extends AppCompatActivity {
    private ListView listViewEmotions;
    private ListView listViewDetails;
    private List<String> emotionList = new ArrayList<>();
    private List<String> detailsList = new ArrayList<>();

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result_details);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        ImageView imageView = findViewById(R.id.resultImage);
        Bundle extras = getIntent().getExtras();
        String resultString = extras.getString(FragmentResults.RESULTJSON);
        Gson gson = new Gson();
        Result result = gson.fromJson(resultString, Result.class);
        emotionList = result.getEmotionList();
        detailsList = result.getDetailsList();
        listViewEmotions = findViewById(R.id.emotionList_list);
        ArrayAdapterEmotion arrayAdapterEmotion = new ArrayAdapterEmotion
                (this, R.layout.emotion_list_row, emotionList);
        listViewEmotions.setAdapter(arrayAdapterEmotion);
        listViewDetails = findViewById(R.id.details_List);
        ArrayAdapterEmotion arrayAdapterDetails = new ArrayAdapterEmotion
                (this, R.layout.emotion_list_row, detailsList);
        listViewDetails.setAdapter(arrayAdapterDetails);
        imageView.setImageBitmap(result.getImageBitMap());
    }
}
