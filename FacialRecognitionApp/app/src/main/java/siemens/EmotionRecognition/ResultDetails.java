package siemens.EmotionRecognition;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;

import com.google.gson.Gson;

import siemens.EmotionRecognition.helpfulStructures.Result;

public class ResultDetails extends AppCompatActivity {

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
        imageView.setImageBitmap(result.getImageBitMap());
    }
}
