package com.nitant.uberapp;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.rilixtech.widget.countrycodepicker.CountryCodePicker;

import java.util.concurrent.TimeUnit;

public class Customer_OTP_Verification extends AppCompatActivity {

    private Button nextbtn;
    FirebaseAuth fAuth;
    EditText phoneNumber,codeEnter,name;
    ProgressBar progressBar;
    TextView state;
    CountryCodePicker codePicker;
    String verificationId;
    PhoneAuthProvider.ForceResendingToken token;
    Boolean verificationInProgress = false;
    private FirebaseAuth.AuthStateListener firebaseAuthListener;

    @Override
    protected void onStart() {
        super.onStart();
        fAuth.addAuthStateListener(firebaseAuthListener);
    }

    @Override
    protected void onStop() {
        super.onStop();
        fAuth.removeAuthStateListener(firebaseAuthListener);
    }




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer__otp__verification);

        fAuth = FirebaseAuth.getInstance();

        firebaseAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {

                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                if(user!=null){
                    Intent i  = new Intent(Customer_OTP_Verification.this,Customer_Homepage.class);
                    startActivity(i);
                    finish();

                    return;
                }
            }
        };



        nextbtn = (Button)findViewById(R.id.nextBtn);
        phoneNumber = findViewById(R.id.phone);
        codeEnter = findViewById(R.id.codeEnter);
        progressBar = findViewById(R.id.progressBar);
        state = findViewById(R.id.state);
        codePicker=findViewById(R.id.ccp);
        name = findViewById(R.id.name);


        nextbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (!verificationInProgress) {

                    if (!phoneNumber.getText().toString().isEmpty() && phoneNumber.getText().toString().length() == 10) {

                        String phoneNum = "+" + codePicker.getSelectedCountryCode() + phoneNumber.getText().toString();

                        progressBar.setVisibility(View.VISIBLE);
                        state.setText("Sending OTP...");
                        state.setVisibility(View.VISIBLE);
                        requestOTP(phoneNum);
                        verificationInProgress = true;


                    }else {
                        phoneNumber.setError("Phone number is not valid");
                    }

                }else{
                    
                    String userOTP = codeEnter.getText().toString();

                    if(!userOTP.isEmpty()&& userOTP.length()==6){

                        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationId,userOTP);
                        verifyAuth(credential);
                    }

                    else {
                        codeEnter.setError("Valid OTP is required");
                    }
                }
            }
        });


    }

    private void verifyAuth(PhoneAuthCredential credential) {

        fAuth.signInWithCredential(credential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if (task.isSuccessful()){

                   // String user_id = fAuth.getCurrentUser().getUid();
                    String username = name.getText().toString();
                    String phone = phoneNumber.getText().toString();
                    DatabaseReference current_user_db = FirebaseDatabase.getInstance().getReference().child("Users").child("Customers");
                    current_user_db.child("Name").setValue(username);
                    current_user_db.child("Contact Number").setValue(phone);
                    Toast.makeText(Customer_OTP_Verification.this,"Authentication is Successful.",Toast.LENGTH_SHORT).show();
                    Intent i = new Intent(Customer_OTP_Verification.this, Customer_Homepage.class);
                    startActivity(i);

                    /* String username = name.getText().toString();
                    String user_id = fAuth.getCurrentUser().getUid();
                    DatabaseReference current_user_db = FirebaseDatabase.getInstance().getReference().child("Users").child("Customers").child(user_id).child("Name").child(username);
                    current_user_db.setValue(true);
                    Intent i = new Intent(Customer_OTP_Verification.this, Customer_Homepage.class);
                    startActivity(i);
                     */


                }

                else{

                    Toast.makeText(Customer_OTP_Verification.this,"Authentication Failed",Toast.LENGTH_SHORT).show();
                }

            }
        });
    }

    private void requestOTP(String phoneNum){

        PhoneAuthProvider.getInstance().verifyPhoneNumber(phoneNum, 60L, TimeUnit.SECONDS, this, new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

            @Override
            public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                super.onCodeSent(s, forceResendingToken);

                progressBar.setVisibility(View.GONE);
                state.setVisibility(View.GONE);
                codeEnter.setVisibility(View.VISIBLE);
                verificationId = s;
                token = forceResendingToken;
                nextbtn.setText("Verify");
                verificationInProgress = true;

            }

            @Override
            public void onCodeAutoRetrievalTimeOut(String s) {
                super.onCodeAutoRetrievalTimeOut(s);
            }

            @Override
            public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {


            }

            @Override
            public void onVerificationFailed(FirebaseException e) {

                Toast.makeText(Customer_OTP_Verification.this,"Cannot create Account"+e.getMessage(),Toast.LENGTH_SHORT).show();

            }
        });
    }
}
