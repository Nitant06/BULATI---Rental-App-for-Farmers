package com.nitant.uberapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class OrderRequestList extends AppCompatActivity {

    //String items[] = new String[]{"Apple", "Orange", "Banana", "Grapes", "PineApple"};

    //ListView requestList;
    Button accept,reject;

    DatabaseReference databaseReference;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_request_list);

        /*requestList = (ListView)findViewById(R.id.RequestList);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,android.R.layout.simple_list_item_1,items);
        requestList.setAdapter(adapter);

        requestList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(OrderRequestList.this,NewOrderDetails.class);

                startActivity(intent);
            }
        });
    }*/

        accept = findViewById(R.id.accept);
        reject = findViewById(R.id.Reject);

        accept.setVisibility(View.GONE);
        reject.setVisibility(View.GONE);

        accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                databaseReference = FirebaseDatabase.getInstance().getReference("Users").child("Customers").child("Order Details");

                int order_accepted = 1;
                int trip_status = 0;

                databaseReference.child("Status").setValue(order_accepted);
                databaseReference.child("Trip Complete Status").setValue(trip_status);

                Intent i = new Intent(OrderRequestList.this,seller_welcome.class);
                startActivity(i);



            }
        });

        reject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                databaseReference = FirebaseDatabase.getInstance().getReference("Users").child("Customers").child("Order Details");

                int order_rejected = 0;

                databaseReference.child("Status").setValue(order_rejected);

                Intent i = new Intent(OrderRequestList.this,seller_welcome.class);
                startActivity(i);
            }
        });


        onNewIntent(getIntent()) ;


    }


    @Override
    protected void onNewIntent (Intent intent) {
        super .onNewIntent(intent) ;
        Bundle extras = intent.getExtras() ;
        if (extras != null ) {
            if (extras.containsKey( "NotificationMessage" )) {

                accept.setVisibility(View.VISIBLE);
                reject.setVisibility(View.VISIBLE);
                String msg = extras.getString( "NotificationMessage" );
                TextView tvNotify = findViewById(R.id.RequestMessage);
                tvNotify.setText(msg) ;

            }
        }
    }
}
