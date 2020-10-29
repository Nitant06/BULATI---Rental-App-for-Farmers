package com.nitant.uberapp;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Calendar;

public class AddToolDetails extends AppCompatActivity {

    private Spinner spinner1,spinner2;
   // private static final String[] items = new String[]{"Tractor","Trolley","Other"};
   // private static final String[] power = new String[]{"1","1","1"};

    DatabaseReference databaseTools,dbRef;

    private EditText equipmentName,registerDetails;
    private Button nextStep;
    private TextView startDate,endDate;
    private DatePickerDialog.OnDateSetListener startDateListener;
    private DatePickerDialog.OnDateSetListener endDateListener;

    String driver_id = FirebaseAuth.getInstance().getCurrentUser().getUid();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_tool_details);


        databaseTools = FirebaseDatabase.getInstance().getReference("Users").child("Drivers").child(driver_id).child("ToolInfo");


        spinner1 = findViewById(R.id.spinnerEquipments);
        spinner2 = findViewById(R.id.powerSpinner);
        equipmentName = findViewById(R.id.equipmentNameTF);
        registerDetails=findViewById(R.id.RCTF);
        startDate = (TextView)findViewById(R.id.startDate);
        endDate = (TextView)findViewById(R.id.endDate);
        nextStep=findViewById(R.id.nextStepBtn);



        startDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar cal = Calendar.getInstance();
                int day = cal.get(Calendar.DAY_OF_MONTH);
                int month = cal.get((Calendar.MONTH));
                int year = cal.get(Calendar.YEAR);

                DatePickerDialog dialog = new DatePickerDialog(
                        AddToolDetails.this,android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                        startDateListener,year,month,day);

                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();

            }
        });



        startDateListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {

                month = month + 1;

                String date = day +"-"+month+"-"+year;
                startDate.setText(date);
            }
        };


        endDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar cal = Calendar.getInstance();
                int day = cal.get(Calendar.DAY_OF_MONTH);
                int month = cal.get((Calendar.MONTH));
                int year = cal.get(Calendar.YEAR);

                DatePickerDialog dialog = new DatePickerDialog(
                        AddToolDetails.this,android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                        endDateListener,year,month,day);

                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();

            }
        });


        endDateListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {

                month = month + 1;


                String date = day +"-"+month+"-"+year;
                endDate.setText(date);

            }
        };





        nextStep.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addDetails();

                Intent i = new Intent(AddToolDetails.this,UploadToolPictures.class);

                startActivity(i);
            }
        });






       /* ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(AddToolDetails.this,
                android.R.layout.simple_spinner_item,items);
        ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(AddToolDetails.this,
                android.R.layout.simple_spinner_item,power);

        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner1.setAdapter(adapter1);
        spinner2.setAdapter(adapter2);

        */

    }

    public void addDetails(){

        String ename = equipmentName.getText().toString().trim();
        String spin1 = spinner1.getSelectedItem().toString();
        String rc = registerDetails.getText().toString().trim();
        String spin2 = spinner2.getSelectedItem().toString();
        String date1 = startDate.getText().toString();
        String date2 = endDate.getText().toString();


        if(!TextUtils.isEmpty(ename)&&!TextUtils.isEmpty(rc)){


            String tool_id = databaseTools.child("ToolInfo").push().getKey();

            ToolAddInfo_class toolClass = new ToolAddInfo_class(ename,rc,spin1,spin2,date1,date2);

            databaseTools.child(tool_id).setValue(toolClass);

            Toast.makeText(getApplicationContext(),"Data Added",Toast.LENGTH_SHORT).show();

        }

        else {


            Toast.makeText(this,"Please enter all the details",Toast.LENGTH_SHORT).show();

        }

    }

}
