package com.nitant.uberapp;

import android.content.Intent;
import android.location.Location;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ToolsListPage extends AppCompatActivity {

    Button addTool;
    ListView toolsList;
    DatabaseReference databaseReference;
    List<ToolAddInfo_class> toolAddInfo_classList;

    String driver_id = FirebaseAuth.getInstance().getCurrentUser().getUid();

    @Override
    protected void onStart() {
        super.onStart();

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                toolAddInfo_classList.clear();

                for(DataSnapshot driverIdSnapshot : dataSnapshot.getChildren()) {

                    String driverIdSnap = driverIdSnapshot.child("Drivers").child(driver_id).getKey();

                    if(driverIdSnap==driver_id) {

                        for (DataSnapshot toolSnapshot : dataSnapshot.getChildren()) {

                            String tool_id = toolSnapshot.getKey();

                            databaseReference.child(tool_id);

                            ToolAddInfo_class tool = toolSnapshot.getValue(ToolAddInfo_class.class);


                            toolAddInfo_classList.add(tool);
                        }

                    }

                }
                ToolGetInfoToList_class adapter = new ToolGetInfoToList_class(ToolsListPage.this,toolAddInfo_classList);

                toolsList.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tools_list);


        databaseReference = FirebaseDatabase.getInstance().getReference("Users").child("Drivers").child(driver_id).child("ToolInfo");

        addTool = (Button)findViewById(R.id.addToolBtn);
        toolsList = (ListView)findViewById(R.id.toolsListView);

        toolAddInfo_classList = new ArrayList<>();

        addTool.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(ToolsListPage.this,AddToolDetails.class);

                startActivity(i);
            }
        });


    }
}
