package com.nitant.uberapp;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class SellerOrderTracker extends AppCompatActivity implements LocationListener {

    private LocationManager locationManager;
    DatabaseReference mRef,mRef2;
    Button yes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seller_order_tracker);

        yes = findViewById(R.id.yesBtn);

        yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i = new Intent(SellerOrderTracker.this,SellerWorkTimerActivity.class);
                startActivity(i);

                int flag = 1;

                mRef2 = FirebaseDatabase.getInstance().getReference("Users").child("Customers").child("Order Details");

                mRef2.child("Trip Complete Status").setValue(flag);

            }
        });

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        Location location = locationManager.getLastKnownLocation(locationManager.NETWORK_PROVIDER);

        onLocationChanged(location);
    }

    @Override
    public void onLocationChanged(Location location) {

        final String driverLat = String.valueOf(location.getLatitude());
        final String driverLong = String.valueOf(location.getLongitude());

        mRef = FirebaseDatabase.getInstance().getReference("Users");
        mRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                DataSnapshot customerSnapshot = dataSnapshot.child("Customers");

                Iterable<DataSnapshot> customerChildrenSnapshot = customerSnapshot.getChildren();

                for (DataSnapshot childSnapshot : customerChildrenSnapshot){

                    double Lat,Long;
                    String custLat,custLong;

                    //Lat = 22.305140;
                    //Long = 73.213235;
                    Lat = customerSnapshot.child("Location").child("Latitude").getValue(double.class);
                    Long = customerSnapshot.child("Location").child("Longitude").getValue(double.class);

                    custLat = String.valueOf(Lat);
                    custLong = String.valueOf(Long);

                    String uri = "http://maps.google.com/maps?saddr="+driverLat+ "," +driverLong+"&daddr="+custLat+ ","+custLong;
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
                    intent.setPackage("com.google.android.apps.maps");
                    startActivity(intent);

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    @Override
    public void onProviderEnabled(String s) {

    }

    @Override
    public void onProviderDisabled(String s) {

    }
}
