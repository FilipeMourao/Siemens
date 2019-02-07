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

public class ArrayAdapterResult extends ArrayAdapter<Result> {
    private Context context;
    private List<Result> resultList;
    private int resourceId;

    public ArrayAdapterResult(@NonNull Context context, int resource, @NonNull List<Result> resultList) {
        super(context, resource, resultList);
        this.context=context;
        this.resultList = resultList;
        this.resourceId = resource;
    }
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(resourceId, parent, false);
        ImageView imageView = rowView.findViewById(R.id.imageView);
        TextView userInformation = rowView.findViewById(R.id.list_results_user);
        TextView emotionInformation = rowView.findViewById(R.id.list_results_emotion);
        Result result = resultList.get(position);
        String resultUser = result.getResultGenre();
        String resultEmotion =  result.getResultEmotion() ;
        userInformation.setText(resultUser);
        emotionInformation.setText(resultEmotion);
        imageView.setImageBitmap(result.getImageBitMap());
        return rowView;
    }

}
