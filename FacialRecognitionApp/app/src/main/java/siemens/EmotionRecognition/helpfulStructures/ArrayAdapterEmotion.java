package siemens.EmotionRecognition.helpfulStructures;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import siemens.EmotionRecognition.R;

public class ArrayAdapterEmotion extends ArrayAdapter<String> {
    private Context context;
    private List<String> emotionList;
    private int resourceId;

    public ArrayAdapterEmotion(@NonNull Context context, int resource, @NonNull List<String> emotionList) {
        super(context, resource, emotionList);
        this.context=context;
        this.emotionList = emotionList;
        this.resourceId = resource;
    }
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(resourceId, parent, false);
        TextView textView = rowView.findViewById(R.id.emotion_list_element);
        textView.setText(emotionList.get(position));
//        emotionString.add("anger");emotionString.add("contempt");emotionString.add("disgust");
//        emotionString.add("engagement");emotionString.add("fear");emotionString.add("joy");
//        emotionString.add("sadness");emotionString.add("surprise");emotionString.add("valence");

        return rowView;
    }
}
