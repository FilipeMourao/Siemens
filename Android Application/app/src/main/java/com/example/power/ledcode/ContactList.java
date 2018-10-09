package com.example.power.ledcode;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.app.Activity;
import android.provider.CalendarContract;
import android.provider.ContactsContract;
import android.support.v4.app.ActivityCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static android.support.v7.widget.RecyclerView.*;

public class ContactList extends Activity {
    int contactId ;
    String contactValue;
    int position;
    ArrayList<String> contactListView = new ArrayList<String>();
    ArrayList<String> listOfContacts = new ArrayList<String>();
    List<String> coloredTitles = new  ArrayList<String>();
    List<String> colorOfTheTitles = new  ArrayList<String>();
    List<Contact> listOfColorContacts = new ArrayList<Contact>();
    List<Contact> partialListOfColorContacts= new ArrayList<Contact>();
    int hasPhone;
    String color;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_list);
        final ListView listView = findViewById(R.id.ContactList);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        Cursor cursor = getContentResolver().query(ContactsContract.Contacts.CONTENT_URI, null, null, null, null);
        while (cursor.moveToNext()){
            if (cursor !=null){
                String id = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID));
                contactValue = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
                hasPhone = cursor.getInt(cursor.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER));
                if(hasPhone > 0){
                    Cursor cp = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,
                            ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?", new String[]{id}, null);
                    if (cp != null && cp.moveToFirst()) {
                        partialListOfColorContacts.add( new Contact(cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME)),cp.getString(cp.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER))));
                        contactListView.add(contactValue);
                        cp.close();
                    }
                }
                } else {
                break;

            }
        }
        Collections.sort(contactListView);
        Collections.sort( partialListOfColorContacts);
        if(getIntent().getStringArrayListExtra("Colored Titles") != null) coloredTitles = getIntent().getStringArrayListExtra("Colored Titles");
        if(getIntent().getStringArrayListExtra("Color of the Titles") != null) colorOfTheTitles = getIntent().getStringArrayListExtra("Color of the Titles");
        listView.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, contactListView) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View row = super.getView(position, convertView, parent);
                Boolean changeColor = false;
                if (!coloredTitles.isEmpty()){
                    for ( final String title : coloredTitles){
                        String test = listView.getItemAtPosition(position).toString();
                        if(test.equals(title)) {
                            changeColor = true;
                            color = colorOfTheTitles.get(coloredTitles.indexOf(title)).toLowerCase();
                            switch (color){
                                case "red":
                                    //row.setBackgroundColor(android.graphics.Color.RED);
                                    row.setBackgroundColor(android.graphics.Color.RED);
                                    partialListOfColorContacts.get(position).setColor("red");
                                    break;
                                case "blue":
                                    //row.setBackgroundColor(android.graphics.Color.BLUE);
                                    row.setBackgroundColor(android.graphics.Color.BLUE);
                                    partialListOfColorContacts.get(position).setColor("blue");
                                    break;
                                case "green":
                                    // row.setBackgroundColor(android.graphics.Color.GREEN);
                                    row.setBackgroundColor(android.graphics.Color.GREEN);
                                    partialListOfColorContacts.get(position).setColor("green");
                                    break;
                                case "yellow":
                                    // row.setBackgroundColor(Color.YELLOW);
                                    row.setBackgroundColor(Color.YELLOW);
                                    partialListOfColorContacts.get(position).setColor("yellow");
                                    break;
                            }
                        }
                    }
                    if (!changeColor) row.setBackgroundColor(Color.WHITE);
                }
                return row;
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(ContactList.this,  SettingContactColors.class);
                String  contactName= contactListView.get(position);
                intent.putExtra("Contact Name", contactName);
                String basicIpAdress = getIntent().getStringExtra("IpAdress");
                if(basicIpAdress == null) basicIpAdress = "192.168.1.117";
                intent.putExtra("IpAdress",basicIpAdress);
                intent.putStringArrayListExtra("Colored Titles", (ArrayList<String>) coloredTitles);
                intent.putStringArrayListExtra("Color of the Titles", (ArrayList<String>) colorOfTheTitles);
                startActivity(intent);
            }
        });

    }
    public void confirmButtonPressed(View v){
        for (Contact contact:  partialListOfColorContacts){
            if (contact.getColor() != null) listOfColorContacts.add(contact);
        }
        listOfColorContacts.get(0);
    }





}
