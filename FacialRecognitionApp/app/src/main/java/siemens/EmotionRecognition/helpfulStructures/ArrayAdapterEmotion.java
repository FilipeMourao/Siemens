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

public class ArrayAdapterEmotion extends ArrayAdapter<Float> {
    private Context context;
    private List<Float> emotionValue;
    private int resourceId;

    public ArrayAdapterEmotion(@NonNull Context context, int resource, @NonNull List<Float> emotionValue) {
        super(context, resource, emotionValue);
        this.context=context;
        this.emotionValue = emotionValue;
        this.resourceId = resource;
    }
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(resourceId, parent, false);
        String anger;String contempt; String disgust; String engagement;
        String fear; String joy; String sadness; String surprise; String valence;

//        emotionString.add("anger");emotionString.add("contempt");emotionString.add("disgust");
//        emotionString.add("engagement");emotionString.add("fear");emotionString.add("joy");
//        emotionString.add("sadness");emotionString.add("surprise");emotionString.add("valence");
        ImageView imageView = rowView.findViewById(R.id.imageView);
        TextView userInformation = rowView.findViewById(R.id.list_results_user);
        TextView emotionInformation = rowView.findViewById(R.id.list_results_emotion);
        float value = emotionValue.get(position);
        
        return rowView;
    }

}
