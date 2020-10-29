package com.nitant.uberapp;


import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;


import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;



public class CustomerOrderStatusPage extends AppCompatActivity  {

    DatabaseReference mRef,customerRef,driverRef;
    TextView statusMsg,waitMsg;
    Button proceed,track;
    ProgressDialog progressDialog;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_order_status_page);

        progressDialog = new ProgressDialog(CustomerOrderStatusPage.this);
        progressDialog.setTitle("Loading");
        progressDialog.setMessage("Please wait while we check your order Status.");
        progressDialog.show();

        track = findViewById(R.id.trackDriverBtn);

        track.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i = new Intent(CustomerOrderStatusPage.this,driverMap.class );
                startActivity(i);

            }
        });


        mRef = FirebaseDatabase.getInstance().getReference("Users").child("Customers").child("Order Details");


        waitMsg = findViewById(R.id.waitMsg2);
        statusMsg = findViewById(R.id.statusMessage);
        proceed = findViewById(R.id.proceedBtn);
        proceed.setVisibility(View.GONE);

        proceed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i = new Intent(CustomerOrderStatusPage.this, CustomerWorkTimerActivity.class);
                startActivity(i);


            }
        });

        mRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                DataSnapshot statusSnapshot = dataSnapshot.child("Status");
                DataSnapshot tripStatusSnapshot = dataSnapshot.child("Trip Complete Status");

                int status = statusSnapshot.getValue(Integer.class);

                int tripStatus = tripStatusSnapshot.getValue(Integer.class);

                if(status==1){
                    statusMsg.setText("Your Order has been accepted");


                    progressDialog.dismiss();
                }

                else if (status==0){

                    statusMsg.setText("Sorry not available right now.Search for new tools");
                    progressDialog.dismiss();
                }


                if (tripStatus == 1){

                    waitMsg.setText("Order has reached your destination."+"\n Click below to proceed.");
                    proceed.setVisibility(View.VISIBLE);
                }

                else {

                    Toast.makeText(getApplicationContext(),"Your order is on the way.Come back after some time.",Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }



}
