package com.nitant.uberapp;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class Customer_Homepage extends AppCompatActivity implements LocationListener {

    protected LocationManager locationManager;
    protected LocationListener locationListener;
    protected Context context;
    double Latitude, Longitude;

    ListView availableToolList;
    DatabaseReference databaseReference, mRef, customerRef;
    List<ToolAddInfo_class> toolAddInfo_classList;

    ProgressDialog progressDialog;
    //DatabaseReference mRef;

    double custLat, custLong;
    double newLat, newLong;
    double array[];
    public Criteria criteria;
    public String bestProvider;

    Button checkStatus;

    @Override
    protected void onStart() {
        super.onStart();

        checkStatus = findViewById(R.id.checkStatusBtn);

        checkStatus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i = new Intent(Customer_Homepage.this, CustomerOrderStatusPage.class);
                startActivity(i);
            }
        });

        mRef = FirebaseDatabase.getInstance().getReference("Users").child("Drivers");



        mRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for(DataSnapshot driverIDSnapshot : dataSnapshot.getChildren()) {

                    String driver_id = driverIDSnapshot.getKey();

                    //DataSnapshot toolInfoSnapshot = dataSnapshot.child(driver_id).child("ToolInfo");

                    //Iterable<DataSnapshot> toolInfochildren = toolInfoSnapshot.getChildren();

                    for (DataSnapshot toolInfo : dataSnapshot.child(driver_id).child("ToolInfo").getChildren() /* toolInfochildren*/ ) {

                        //String tool_id = toolInfo.getKey();

                        Latitude = toolInfo.child("Location").child("latitude").getValue(double.class);

                        Longitude = toolInfo.child("Location").child("longitude").getValue(double.class);

                        getDriverLatLong(Latitude, Longitude);
                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        availableToolList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {


                Intent intent = new Intent(Customer_Homepage.this, RateCalculator.class);
                startActivity(intent);
            }
        });

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {

            case R.id.menuLogout:
                FirebaseAuth.getInstance().signOut();
                finish();
                startActivity(new Intent(this, MainActivity.class));
                break;

        }
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);

        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer__homepage);

        //  mRef = FirebaseDatabase.getInstance().getReference().child("Users").child("Drivers").child("ToolInfo").child("Location");

        array = new double[10];

        databaseReference = FirebaseDatabase.getInstance().getReference("Users").child("Drivers");

        availableToolList = (ListView) findViewById(R.id.availableToolList);

        toolAddInfo_classList = new ArrayList<>();


        progressDialog = new ProgressDialog(Customer_Homepage.this);
        progressDialog.setTitle("Loading");
        progressDialog.setMessage("Searching for Nearby Tools...");
        progressDialog.show();

       /* if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }*/



        //  Location location = locationManager.getLastKnownLocation(locationManager.NETWORK_PROVIDER);

        getLocation();

        Toolbar toolbar = findViewById(R.id.toolbar2);
        setSupportActionBar(toolbar);


    }

    public static boolean isLocationEnabled(Context context) {
        //...............
        return true;
    }

    protected void getLocation() {
        if (isLocationEnabled(Customer_Homepage.this)) {
            locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
            criteria = new Criteria();
            bestProvider = String.valueOf(locationManager.getBestProvider(criteria, true)).toString();

            //You can still do this if you like, you might get lucky:
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
            Location location = locationManager.getLastKnownLocation(bestProvider);
            if (location != null) {
                Log.e("TAG", "GPS is on");
                custLat = location.getLatitude();

                custLong = location.getLongitude();

                customerRef = FirebaseDatabase.getInstance().getReference("Users").child("Customers");

                customerRef.child("Location").child("Latitude").setValue(custLat);
                customerRef.child("Location").child("Longitude").setValue(custLong);

                getCustomerLatLong(custLat, custLong);

            }
            else{
                //This is what you need:
                locationManager.requestLocationUpdates(bestProvider, 0, 0, this);
            }
        }
        else
        {
            //prompt user to enable location....
            //.................
        }
    }

    @Override
    public void onLocationChanged(Location location) {


    }


    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {
        Log.d("Latitude", "status");
    }

    @Override
    public void onProviderEnabled(String s) {
        Log.d("Latitude", "enable");
    }

    @Override
    public void onProviderDisabled(String s) {
        Log.d("Latitude", "disable");
    }


    public void getDriverLatLong(double d1, double d2) {

        array[0] = d1;
        array[1] = d2;

        calc(array);

    }

    public void getCustomerLatLong(double c1, double c2) {

        array[2] = c1;

        array[3] = c2;

        calc(array);

    }

    //Double distance = SphericalUtil.computeDistanceBetween(from,to);

    public void calc(double array[]) {

        double driverLat, driverLong, custLat, custLong;

        driverLat = array[0];
        driverLong = array[1];
        custLat = array[2];
        custLong = array[3];

        //LatLng from = new LatLng(custLat,custLong);

        //LatLng to = new LatLng(driverLat,driverLong);

        //double distance = SphericalUtil.computeDistanceBetween(from,to);

        //Toast.makeText(getApplicationContext(),"Distance is "+distance,Toast.LENGTH_LONG).show();


        double theta = driverLong - custLong;
        double dist = Math.sin(deg2rad(driverLat))
                * Math.sin(deg2rad(custLat))
                + Math.cos(deg2rad(driverLat))
                * Math.cos(deg2rad(custLat))
                * Math.cos(deg2rad(theta));
        dist = Math.acos(dist);
        dist = rad2deg(dist);
        dist = dist * 60 * 1.1515*1.609344;

        if(dist < 1){


            databaseReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    toolAddInfo_classList.clear();

                    for(DataSnapshot driverIDSnapshot : dataSnapshot.getChildren()) {

                        String driver_id = driverIDSnapshot.getKey();

                        for (DataSnapshot toolSnapshot : dataSnapshot.child(driver_id).child("ToolInfo").getChildren()) {

                            ToolAddInfo_class tool = toolSnapshot.getValue(ToolAddInfo_class.class);

                            toolAddInfo_classList.add(tool);
                        }
                    }

                    ToolGetInfoToList_class adapter = new ToolGetInfoToList_class(Customer_Homepage.this,toolAddInfo_classList);
                    availableToolList.setAdapter(adapter);
                    progressDialog.dismiss();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });


        }





    }

    public double deg2rad(double deg) {
        return (deg * Math.PI / 180.0);
    }

    public double rad2deg(double rad) {
        return (rad * 180.0 / Math.PI);

    }

}

