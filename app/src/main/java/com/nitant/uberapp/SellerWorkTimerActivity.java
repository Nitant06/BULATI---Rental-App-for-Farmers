package com.nitant.uberapp;

import android.os.CountDownTimer;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class SellerWorkTimerActivity extends AppCompatActivity {

   TextView StatusMsg,bill;

   Button done,workStatus;

   DatabaseReference databaseReference,mRef;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seller_work_timer);

        StatusMsg = findViewById(R.id.MessageView);
        bill = findViewById(R.id.BillAmount);
        done = findViewById(R.id.paymentDoneBtn);
        done.setVisibility(View.GONE);
        workStatus = findViewById(R.id.workStatusBtn);

        workStatus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                databaseReference = FirebaseDatabase.getInstance().getReference("Users").child("Customers").child("Order Details");

                databaseReference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        mRef = FirebaseDatabase.getInstance().getReference("Users").child("Customers");
                        DataSnapshot csp = dataSnapshot.child("Work Status");
                        DataSnapshot billSnapshot = dataSnapshot.child("Estimated Price");

                        int status = csp.child("Status").getValue(Integer.class);
                        String price = billSnapshot.getValue(String.class);

                        if (status==1){

                            StatusMsg.setText("Work Completed.");
                            bill.setText("Total Cost : "+price);
                            done.setVisibility(View.VISIBLE);

                        }

                        else {

                            Toast.makeText(getApplicationContext(),"Please wait.The work will complete soon.",Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

            }
        });




    }



}
