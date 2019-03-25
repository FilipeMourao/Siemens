package siemens.PhotoGallery.helpfulStructures;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import java.util.List;

import siemens.PhotoGallery.R;
// adapted from http://androstock.com/tutorials/create-a-photo-gallery-app-in-android-android-studio.html
public class SingleAlbumAdapter extends BaseAdapter {
    private Activity activity;
    private List<Photo> photos;
    public SingleAlbumAdapter(Activity activity, List<Photo> photos ) {
        this.activity = activity;
        this.photos = photos;
    }
    public int getCount() {
        return photos.size();
    }
    public Object getItem(int position) {
        return position;
    }
    public long getItemId(int position) {
        return position;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(activity).inflate(
                    R.layout.images_single_album_row, parent, false);
            ImageView imageView = convertView.findViewById(R.id.galleryImage);
            imageView.setImageBitmap(photos.get(position).getImageBitMap());
        } else {
            ImageView imageView = convertView.findViewById(R.id.galleryImage);
            imageView.setImageBitmap(photos.get(position).getImageBitMap());
        }
        return convertView;
    }
}