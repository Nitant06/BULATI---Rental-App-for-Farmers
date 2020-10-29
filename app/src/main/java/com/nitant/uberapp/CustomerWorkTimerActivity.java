package com.nitant.uberapp;

import android.content.Intent;
import android.os.CountDownTimer;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class CustomerWorkTimerActivity extends AppCompatActivity {

    Button start,end,pay;
    TextView timer;
    CountDownTimer countDownTimer;
    long timeLeft;//in milliseconds => 10 mins
    boolean timerRunning;
    int workCompleteStatus=0;

    DatabaseReference mRef,mRef2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_work_timer);

        timer = findViewById(R.id.timerView);
        start = findViewById(R.id.startWorkBtn);
        end= findViewById(R.id.endWorkBtn);
        pay = findViewById(R.id.payBtn);

        pay.setVisibility(View.GONE);
        mRef = FirebaseDatabase.getInstance().getReference("Users").child("Customers").child("Order Details");

        mRef2 = FirebaseDatabase.getInstance().getReference("Users").child("Customers").child("Order Details").child("Work Status").child("Status");
        mRef2.setValue(workCompleteStatus);


        mRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                DataSnapshot timeSnapshot = dataSnapshot.child("Time");

                String getTime = timeSnapshot.getValue(String.class);

                timeLeft = Long.parseLong(getTime) * 3600000;

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startStop();
            }
        });

        end.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                workCompleteStatus = 1;


                mRef2.setValue(workCompleteStatus);
                pay.setVisibility(View.VISIBLE);
            }
        });

        pay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                pay.setText("Payment Done");
                Intent i = new Intent(CustomerWorkTimerActivity.this,CustomerFeedbackActivity.class);
                startActivity(i);
            }
        });
    }

    private void startStop() {

        if(timerRunning){

            stopTimer();

        }else {

            startTimer();
        }
    }

    private void startTimer() {

        countDownTimer = new CountDownTimer(timeLeft,1000) {
            @Override
            public void onTick(long l) {

                timeLeft = l;
                updateTimer();
            }

            @Override
            public void onFinish() {

            }
        }.start();

        timerRunning = true;
        start.setText("Stop");
    }

    private void updateTimer() {

        int timerHrs = (int) (timeLeft/1000)/3600;
        int timerMins = (int) ((timeLeft/1000)%3600)/60;
        int timerSecs = (int) (timeLeft /1000)%60;

        String timeLeftText;

        timeLeftText = ""+timerHrs;
        timeLeftText += ":";
        timeLeftText += ""+timerMins;
        timeLeftText += ":";
        if (timerSecs<10) timeLeftText += "0";

        timeLeftText += ""+timerSecs;

        timer.setText(timeLeftText);
    }

    private void stopTimer() {

        countDownTimer.cancel();
        timerRunning = false;
        start.setText("Start");
    }
}
