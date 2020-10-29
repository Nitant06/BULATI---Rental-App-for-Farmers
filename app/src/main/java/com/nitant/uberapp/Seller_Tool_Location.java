package com.nitant.uberapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class Seller_Tool_Location extends AppCompatActivity {


    private Button LocButton,submit;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seller__tool__location);

        LocButton = (Button)findViewById(R.id.getLocationBtn);
        submit = (Button)findViewById(R.id.submit);

        LocButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(new Intent(Seller_Tool_Location.this,MapsActivity.class));
            }
        });

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Toast.makeText(Seller_Tool_Location.this,"Data Saved Successfully",Toast.LENGTH_LONG).show();

                startActivity(new Intent(Seller_Tool_Location.this,seller_welcome.class));
            }
        });

    }
}
