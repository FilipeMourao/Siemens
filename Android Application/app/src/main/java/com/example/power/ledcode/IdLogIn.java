package com.example.power.ledcode;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class IdLogIn extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_id_log_in);

    }
    public void addIdButtonClicked(View v){
        EditText companyLogIn = findViewById(R.id.CompanyLogIn);
        EditText idFinal = findViewById(R.id.IpDetailNumber);
        String idFinalString = idFinal.getText().toString();
        String companyLogInString = companyLogIn.getText().toString();
        if(idFinalString.isEmpty())
            Toast.makeText(getApplicationContext(),
                    "Empty card number", Toast.LENGTH_LONG).show();
        if(companyLogInString.isEmpty())
            Toast.makeText(getApplicationContext(),
                    "Empty company Log in  ", Toast.LENGTH_LONG).show();
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("CompanyLogIn",companyLogInString);
        idFinalString = "192.168.1." + idFinalString;
        intent.putExtra("IpAdress",idFinalString);
        startActivity(intent);
    }

}
