package com.nitant.uberapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class RateCalculator extends AppCompatActivity {

    //Notification Code
    RequestQueue mRequestQueue;
    final private String FCM_API = "https://fcm.googleapis.com/fcm/send";
    final private String serverKey = "key=" + "AAAAlcA4004:APA91bGjGsoEeSmmTwmn43SakqXdDbuGdJsoZk0hwrUfUw9pp2LJI0B520Bwcu4F4dL1F2lyGq94SiktIG_lNc1RNhjnkeTI3T6ZjD2N1W0rK6OgklH3HdOXTzjGPXpxkSFORS3Zy4LD";
    final private String contentType = "application/json";
    final String TAG = "NOTIFICATION TAG";

    String NOTIFICATION_TITLE;
    String NOTIFICATION_MESSAGE;
    String TOPIC;

    TextView waitMsg;

    private TextView totalPrice;
    private Spinner tpSpinner;
    private EditText area;

    private int Total_price,a;
    private String t,land_area;
    private Button calcBtn,back,confirmBtn;
    DatabaseReference mRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rate_calculator);

        area = findViewById(R.id.LandArea);
        tpSpinner = (Spinner)findViewById(R.id.timePeriodSpinner);
        totalPrice = findViewById(R.id.TotalPriceTF);
        calcBtn = findViewById(R.id.calculateBtn);
        back=findViewById(R.id.backBtn);
        confirmBtn = findViewById(R.id.confirmBtn);
        waitMsg = findViewById(R.id.WaitMessage);


        mRequestQueue = Volley.newRequestQueue(this);


        //Notification Code


        confirmBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i = new Intent(RateCalculator.this,Customer_Homepage.class);
                startActivity(i);

                String workArea = area.getText().toString();
                String workTime = tpSpinner.getSelectedItem().toString();
                String estPrice = totalPrice.getText().toString();

                mRef = FirebaseDatabase.getInstance().getReference("Users").child("Customers").child("Order Details");
                mRef.child("Area").setValue(workArea);
                mRef.child("Time").setValue(workTime);
                mRef.child("Estimated Price").setValue(estPrice);

                TOPIC = "/topics/userABC";
                NOTIFICATION_TITLE ="Order Request";
                NOTIFICATION_MESSAGE = "New Order Request.Click here to view.";

                JSONObject notification = new JSONObject();
                JSONObject notifcationBody = new JSONObject();
                try {
                    notifcationBody.put("title", NOTIFICATION_TITLE);
                    notifcationBody.put("message", NOTIFICATION_MESSAGE);
                    notification.put("to", TOPIC);
                    notification.put("data", notifcationBody);
                } catch (JSONException e) {
                    Log.e(TAG, "onCreate: " + e.getMessage() );
                }

                sendNotification(notification);
            }

        });

        calcBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                calculateRent();

            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i = new Intent(RateCalculator.this,Customer_Homepage.class);
                startActivity(i);
            }
        });
    }


    private void sendNotification(JSONObject notification) {


        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(FCM_API, notification,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.i(TAG, "onResponse: " + response.toString());


                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(RateCalculator.this, "Request error", Toast.LENGTH_LONG).show();
                        Log.i(TAG, "onErrorResponse: Didn't work");
                    }
                }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("Authorization", serverKey);
                params.put("Content-Type", contentType);
                return params;
            }
        };

        MySingleton.getInstance(getApplicationContext()).addToRequestQueue(jsonObjectRequest);

    }

    public void calculateRent() {

        land_area = area.getText().toString().trim();

        t= tpSpinner.getSelectedItem().toString().trim();

         int base_price_time = 100;
         int base_price_fuel = 50;
         int time_cost,area_cost;
         int time;

        if(!"".equals(land_area)) {

            a = Integer.parseInt(land_area);

            area_cost = a * base_price_fuel;

            switch (t) {

                case "1": time = 1;
                    time_cost = time * base_price_time;
                    Total_price = area_cost + time_cost;
                    totalPrice.setText("₹" + Total_price);
                    break;

                case "2":
                    time = 2;
                    time_cost = time * base_price_time;
                    Total_price = area_cost + time_cost;
                    totalPrice.setText("₹" + Total_price);
                    break;

                case "3":
                    time = 3;
                    time_cost = time * base_price_time;
                    Total_price = area_cost + time_cost;
                    totalPrice.setText("₹" + Total_price);
                    break;

                case "4":
                    time = 4;
                    time_cost = time * base_price_time;
                    Total_price = area_cost + time_cost;
                    totalPrice.setText("₹" + Total_price);
                    break;

                case "5":
                    time = 5;
                    time_cost = time * base_price_time;
                    Total_price = area_cost + time_cost;
                    totalPrice.setText("₹" + Total_price);
                    break;

                case "6":
                    time = 6;
                    time_cost = time * base_price_time;
                    Total_price = area_cost + time_cost;
                    totalPrice.setText("₹" + Total_price);
                    break;

                case "1 Day":
                    time = 24;
                    time_cost = time * base_price_time;
                    Total_price = area_cost + time_cost;
                    totalPrice.setText("₹" + Total_price);
                    break;

                default:
                    Toast.makeText(getApplicationContext(), "Nothing Selected", Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    }
}
