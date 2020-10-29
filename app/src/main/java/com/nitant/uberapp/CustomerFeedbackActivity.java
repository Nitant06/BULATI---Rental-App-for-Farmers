package com.nitant.uberapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class CustomerFeedbackActivity extends AppCompatActivity {

    RatingBar mRatingBar;
    TextView mRatingScale;
    EditText mFeedback;
    Button mSendFeedback;
    int rating;

    DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_feedback);

        mRatingBar = (RatingBar) findViewById(R.id.ratingBar);
        mRatingScale = (TextView) findViewById(R.id.tvRatingScale);
        mFeedback = (EditText) findViewById(R.id.etFeedback);
        mSendFeedback = (Button) findViewById(R.id.submitFeedbackBtn);

        mRatingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float v, boolean b) {
                mRatingScale.setText(String.valueOf(v));
                switch ((int) ratingBar.getRating()) {
                    case 1:
                        mRatingScale.setText("Very bad");
                        rating=1;
                        break;
                    case 2:
                        mRatingScale.setText("Need some improvement");
                        rating=2;
                        break;
                    case 3:
                        mRatingScale.setText("Good");
                        rating=3;
                        break;
                    case 4:
                        mRatingScale.setText("Great");
                        rating=4;
                        break;
                    case 5:
                        mRatingScale.setText("Awesome. I love it");
                        rating=5;
                        break;
                    default:
                        mRatingScale.setText("");
                }
            }
        });

        mSendFeedback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mFeedback.getText().toString().isEmpty()) {
                    Toast.makeText(CustomerFeedbackActivity.this, "Please fill in feedback text box", Toast.LENGTH_LONG).show();
                } else {

                    databaseReference = FirebaseDatabase.getInstance().getReference("Users").child("Customer Feedback");
                    String id = FirebaseAuth.getInstance().getCurrentUser().getUid();

                    String feedback = mFeedback.getText().toString().trim();

                    databaseReference.child(id).child("Rating").setValue(rating);
                    databaseReference.child(id).child("Feedback").setValue(feedback);
                    Intent i  = new Intent(CustomerFeedbackActivity.this,Customer_Homepage.class);
                    startActivity(i);
                    Toast.makeText(CustomerFeedbackActivity.this, "Thank you for sharing your feedback", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
}
