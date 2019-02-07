package siemens.EmotionRecognition;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.google.gson.Gson;

import java.util.List;

import siemens.EmotionRecognition.helpfulStructures.ArrayAdapterResult;
import siemens.EmotionRecognition.helpfulStructures.Result;
import siemens.EmotionRecognition.helpfulStructures.ResultsDatabase;

import static android.app.Activity.RESULT_OK;


public class FragmentResults extends Fragment implements AdapterView.OnItemClickListener, SwipeRefreshLayout.OnRefreshListener {
    private FloatingActionButton fab;
    private ListView listView;
    private SwipeRefreshLayout swipeRefreshLayout;
    private ResultsDatabase resultsDatabase;
    private List<Result> results;
    public static int SELECT_IMAGE_GALLERY = 12445;
    public static int SELECT_IMAGE_CAMERA = 124451;
    public static String RESULTJSON = "RESULTJSON";


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //Inflate the layout
        View view = inflater.inflate(R.layout.fragment_results, container, false);
        resultsDatabase = new ResultsDatabase(getActivity());
        listView = view.findViewById(R.id.result_list);
        listView.setOnItemClickListener(this::onItemClick);
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                FragmentResults.this.onItemLongClick(parent,view,position,id);
                return false;
            }
        });
        swipeRefreshLayout = view.findViewById(R.id.swiperefresh);
        swipeRefreshLayout.setOnRefreshListener(this);
        //Get reference to FloatingActionButton, set correct icon and register OnClickListener
        fab = view.findViewById(R.id.fab);
        fab.setOnClickListener(this::onButtonPressed);
        updateList();
        return view;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Result result = results.get(position);
        Gson gson = new Gson();
        String resultString = gson.toJson(result);
        Intent intent = new Intent(getActivity(),ResultDetails.class);
        intent.putExtra(RESULTJSON,resultString);
        startActivity(intent);
    }

    public void onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        AlertDialog.Builder builder;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            builder = new AlertDialog.Builder(getActivity(), android.R.style.Theme_Material_Dialog_Alert);
        } else {
            builder = new AlertDialog.Builder(getActivity());
        }
        builder.setTitle("Delete Result")
                .setMessage("Do you want to delete this result :")
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                    }
                })
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        resultsDatabase.delete(results.get(position).getImageBitMap());
                        updateList();
                    }
                })
                .show();
    }

    @Override
    public void onRefresh() {updateList();  }

    public boolean checkPicturesPermission() {
        if ((ContextCompat.checkSelfPermission(getActivity(),
                Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) ||
                (ContextCompat.checkSelfPermission(getActivity(),
                        Manifest.permission.READ_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED
                )
                ) {
            // No explanation needed; request the permission
            ActivityCompat.requestPermissions(getActivity(),
                    new String[]{Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE}, 0);
            return false;

        } else {
            return true;
            // Permission has already been granted
        }
    }

    public void onButtonPressed(View v) {
        if (checkPicturesPermission()) {
            AlertDialog.Builder builder;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                builder = new AlertDialog.Builder(getActivity(), android.R.style.Theme_Material_Dialog_Alert);
            } else {
                builder = new AlertDialog.Builder(getActivity());
            }
            builder.setTitle("Get a new picture")
                    .setMessage("Take the picture from:")
                    .setNegativeButton("From camera", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            Intent intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                            startActivityForResult(intent, SELECT_IMAGE_CAMERA);
                        }
                    })
                    .setNeutralButton("From gallery", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            Intent intent = new   Intent(Intent.ACTION_PICK,android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                            startActivityForResult(intent, SELECT_IMAGE_GALLERY);
                        }
                    })
                    .setPositiveButton("Cancel", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                        }
                    })
                    .show();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == SELECT_IMAGE_CAMERA) {
                Bitmap bitmap = (Bitmap) data.getExtras().get("data");
                Result result = new Result(bitmap);
                resultsDatabase.addResult(result);
                updateList();

            } else if (requestCode == SELECT_IMAGE_GALLERY) {
                Uri selectedImage = data.getData();
                // h=1;
                //imgui = selectedImage;
                String[] filePath = {MediaStore.Images.Media.DATA};
                Cursor c = getActivity().getContentResolver().query(selectedImage, filePath, null, null, null);
                c.moveToFirst();
                int columnIndex = c.getColumnIndex(filePath[0]);
                String picturePath = c.getString(columnIndex);
                c.close();
                Bitmap thumbnail = (BitmapFactory.decodeFile(picturePath));
                Result result = new Result(thumbnail);
                resultsDatabase.addResult(result);
                updateList();

            }

        }
    }
    public void updateList(){
        results = resultsDatabase.getAllResults();
        ArrayAdapterResult arrayAdapterResult = new ArrayAdapterResult
                (getActivity(), R.layout.result_list_row, results);
        listView.setAdapter(arrayAdapterResult);
        swipeRefreshLayout.setRefreshing(false);
    }

}
