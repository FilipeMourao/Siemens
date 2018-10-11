package com.example.power.ledcode;

import android.Manifest;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.app.Activity;
import android.provider.CalendarContract;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.telephony.TelephonyManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static android.support.v7.widget.RecyclerView.*;

public class ContactList extends Activity {
    String contactValue;
    ArrayList<String> contactListView = new ArrayList<String>();
    List<String> coloredTitles = new  ArrayList<String>();
    List<String> colorOfTheTitles = new  ArrayList<String>();
  //  List<Contact> listOfColorContacts = db.getAllContacts();
    List<Contact> listOfContacts =  new ArrayList<Contact>();
    int hasPhone;
    String color;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_list);
        final ListView listView = findViewById(R.id.ContactList);
        ColoredContactDatabase db = new ColoredContactDatabase(this.getApplicationContext());
//        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED ){
//            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_PHONE_STATE)) {
//                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_PHONE_STATE}, 1);
//            } else {
//                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_PHONE_STATE}, 1);
//
//            }
//
//        } else { // do nothing
//        }
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED){
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_CONTACTS)) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_CONTACTS}, 1);
            }
            else {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_CONTACTS}, 1);
            }

        } else {
            Cursor cursor = getContentResolver().query(ContactsContract.Contacts.CONTENT_URI, null, null, null, null);
            int i = 0;
            while (cursor.moveToNext()){
                i++;
                if (cursor !=null){
                    String id = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID));
                    contactValue = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
                    hasPhone = cursor.getInt(cursor.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER));
                    if(hasPhone > 0){
                        Cursor cp = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,
                                ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?", new String[]{id}, null);
                        if (cp != null && cp.moveToFirst()) {
                            String number = cp.getString(cp.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                            number = number.replaceAll("\\s+","");
                            number = number.replaceAll("-","");
                            number = number.replaceAll("()","");
                            number = number.replaceAll("","");
                            listOfContacts.add( new Contact(i,cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME)),number));
                            contactListView.add(contactValue);
                            cp.close();
                        }
                    }
                } else {
                    break;
                }
            }
            Collections.sort(contactListView);
            Collections.sort( listOfContacts);
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
                                        listOfContacts.get(position).setColor("red");
                                        break;
                                    case "blue":
                                        //row.setBackgroundColor(android.graphics.Color.BLUE);
                                        row.setBackgroundColor(android.graphics.Color.BLUE);
                                        listOfContacts.get(position).setColor("blue");
                                        break;
                                    case "green":
                                        // row.setBackgroundColor(android.graphics.Color.GREEN);
                                        row.setBackgroundColor(android.graphics.Color.GREEN);
                                        listOfContacts.get(position).setColor("green");
                                        break;
                                    case "yellow":
                                        // row.setBackgroundColor(Color.YELLOW);
                                        row.setBackgroundColor(Color.YELLOW);
                                        listOfContacts.get(position).setColor("yellow");
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


    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case 1:  {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS) == PackageManager.PERMISSION_GRANTED){
                    final ListView listView = findViewById(R.id.ContactList);
                    Toast.makeText(this, "Permission Granted!", Toast.LENGTH_SHORT).show();
                    Cursor cursor = getContentResolver().query(ContactsContract.Contacts.CONTENT_URI, null, null, null, null);
                    int i = 0;
                    while (cursor.moveToNext()){
                        i++;
                        if (cursor !=null){
                            String id = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID));
                            contactValue = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
                            hasPhone = cursor.getInt(cursor.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER));
                            if(hasPhone > 0){
                                Cursor cp = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,
                                        ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?", new String[]{id}, null);
                                if (cp != null && cp.moveToFirst()) {
                                    listOfContacts.add( new Contact(i,cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME)),cp.getString(cp.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER))));
                                    contactListView.add(contactValue);
                                    cp.close();
                                }
                            }
                        } else {
                            break;

                        }
                    }
                    Collections.sort(contactListView);
                    Collections.sort( listOfContacts);
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
                                                listOfContacts.get(position).setColor("red");
                                                break;
                                            case "blue":
                                                //row.setBackgroundColor(android.graphics.Color.BLUE);
                                                row.setBackgroundColor(android.graphics.Color.BLUE);
                                                listOfContacts.get(position).setColor("blue");
                                                break;
                                            case "green":
                                                // row.setBackgroundColor(android.graphics.Color.GREEN);
                                                row.setBackgroundColor(android.graphics.Color.GREEN);
                                                listOfContacts.get(position).setColor("green");
                                                break;
                                            case "yellow":
                                                // row.setBackgroundColor(Color.YELLOW);
                                                row.setBackgroundColor(Color.YELLOW);
                                                listOfContacts.get(position).setColor("yellow");
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
            }


            break;
            }
            case 2:{
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED){
                        Toast.makeText(this, "Permission Granted !", Toast.LENGTH_SHORT).show();
                        ColoredContactDatabase db = new ColoredContactDatabase(this.getApplicationContext());
                        for (Contact contact:  listOfContacts) {
                            if (contact.getColor() != null){
                                if (db.getContact(contact.getNumber()) != null) db.updateContact(contact);
                                else db.addContact(contact);
                            }
                        }

                        Intent intent = new Intent(this, MainActivity.class);
                        String companyLogIn = getIntent().getStringExtra("CompanyLogIn");
                        String basicIpAdress = getIntent().getStringExtra("IpAdress");
                        intent.putExtra("IpAdress",basicIpAdress);
                        intent.putExtra("CompanyLogIn",companyLogIn);
                        intent.putStringArrayListExtra("Colored Titles", (ArrayList<String>) coloredTitles);
                        intent.putStringArrayListExtra("Color of the Titles", (ArrayList<String>) colorOfTheTitles);
                        startActivity(intent);

                    }
                    if ((ContextCompat.checkSelfPermission(this, Manifest.permission.READ_SMS) == PackageManager.PERMISSION_GRANTED)
                            && (ContextCompat.checkSelfPermission(this, Manifest.permission.RECEIVE_SMS) == PackageManager.PERMISSION_GRANTED) ){
                       Toast.makeText(this, "Permission Granted !", Toast.LENGTH_SHORT).show();


                    }

                }
                break;
            }
        }

    }

    public void confirmButtonPressed(View v){
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED
                || ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_SMS) != PackageManager.PERMISSION_GRANTED ){
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_PHONE_STATE)) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_PHONE_STATE, Manifest.permission.READ_SMS}, 2);
            }
            else {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_PHONE_STATE, Manifest.permission.READ_SMS,Manifest.permission.RECEIVE_SMS}, 2);
            }
        }
        else {
            ColoredContactDatabase db = new ColoredContactDatabase(this.getApplicationContext());
            for (Contact contact:  listOfContacts) {
                if (contact.getColor() != null){
                    contact.setIpAdress(getIntent().getStringExtra("IpAdress"));
                    if (db.getContact(contact.getNumber()) != null) db.updateContact(contact);
                    else db.addContact(contact);
                }
            }
            Intent intent = new Intent(this, MainActivity.class);
            String companyLogIn = getIntent().getStringExtra("CompanyLogIn");
            String basicIpAdress = getIntent().getStringExtra("IpAdress");
            intent.putExtra("IpAdress",basicIpAdress);
            intent.putExtra("CompanyLogIn",companyLogIn);
            intent.putStringArrayListExtra("Colored Titles", (ArrayList<String>) coloredTitles);
            intent.putStringArrayListExtra("Color of the Titles", (ArrayList<String>) colorOfTheTitles);
            startActivity(intent);
        }

    }
    public void cancelButtonPressed(View v){
        Intent intent = new Intent(this, MainActivity.class);
        String companyLogIn = getIntent().getStringExtra("CompanyLogIn");
        String basicIpAdress = getIntent().getStringExtra("IpAdress");
        intent.putExtra("IpAdress",basicIpAdress);
        intent.putExtra("CompanyLogIn",companyLogIn);
        startActivity(intent);
    }
}
