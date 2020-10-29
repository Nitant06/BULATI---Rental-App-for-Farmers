package com.nitant.uberapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class NewOrderDetails extends AppCompatActivity {

    Button accept,reject;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_order_details);

        accept = (Button)findViewById(R.id.acceptBtn);
        reject = (Button)findViewById(R.id.rejectBtn);

        accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(NewOrderDetails.this, CustomerWorkTimerActivity.class);

                startActivity(i);
            }
        });

        reject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i = new Intent(NewOrderDetails.this,OrderRequestList.class);

                startActivity(i);
            }
        });
    }
}
