package com.nitant.uberapp;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Random;

public class MyFirebaseMessagingService extends FirebaseMessagingService {
    public MyFirebaseMessagingService() {
    }

    private final String ADMIN_CHANNEL_ID = "admin_channel";

    DatabaseReference databaseReference;

    @Override
    public void onMessageReceived(final RemoteMessage remoteMessage) {


        databaseReference = FirebaseDatabase.getInstance().getReference("Users").child("Customers");

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
               DataSnapshot childSnapshot = dataSnapshot.child("Name");
               DataSnapshot phoneSnapshot = dataSnapshot.child("Contact Number");
               DataSnapshot areaSnapshot = dataSnapshot.child("Order Details");
               DataSnapshot timeSnapshot = dataSnapshot.child("Order Details");
               DataSnapshot costSnapshot = dataSnapshot.child("Order Details");


              String custName = childSnapshot.getValue(String.class);
              String custPhone = phoneSnapshot.getValue(String.class);
              String custArea  = areaSnapshot.child("Area").getValue(String.class);
                Log.i("Value : ",String.valueOf(custArea));
              String custTime  = timeSnapshot.child("Time").getValue(String.class);
              String custCost  = costSnapshot.child("Estimated Price").getValue(String.class);

                final Intent intent = new Intent(MyFirebaseMessagingService.this, OrderRequestList.class);
                NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                int notificationID = new Random().nextInt(3000);
                intent.putExtra( "NotificationMessage" , " Order From : "+custName+"\n Contact Number : "+custPhone+"\n Land Area : "+custArea+" .acr"+"\n Rental Time: "+custTime+".hrs"+"\n Estimated Price: "+custCost);
                intent.addCategory(Intent. CATEGORY_LAUNCHER );
                intent.setAction(Intent. ACTION_MAIN );

                      /*
        Apps targeting SDK 26 or above (Android O) must implement notification channels and add its notifications
        to at least one of them. Therefore, confirm if version is Oreo or higher, then setup notification channel
      */
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                    setupChannels(notificationManager);
                }

                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                PendingIntent pendingIntent = PendingIntent.getActivity(MyFirebaseMessagingService.this, 0, intent,
                        PendingIntent.FLAG_ONE_SHOT);

                Bitmap largeIcon = BitmapFactory.decodeResource(getResources(),
                        R.drawable.ic_launcher_background);

                Uri notificationSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
                NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(MyFirebaseMessagingService.this, ADMIN_CHANNEL_ID)
                        .setSmallIcon(R.drawable.ic_launcher_background)
                        .setLargeIcon(largeIcon)
                        .setContentTitle(remoteMessage.getData().get("title"))
                        .setContentText(remoteMessage.getData().get("message"))
                        .setAutoCancel(true)
                        .setSound(notificationSoundUri)
                        .setContentIntent(pendingIntent);

                //Set notification color to match your app color template
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    notificationBuilder.setColor(getResources().getColor(R.color.colorPrimaryDark));
                }
                notificationManager.notify(notificationID, notificationBuilder.build());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void setupChannels(NotificationManager notificationManager) {
        CharSequence adminChannelName = "New notification";
        String adminChannelDescription = "Device to devie notification";

        NotificationChannel adminChannel;
        adminChannel = new NotificationChannel(ADMIN_CHANNEL_ID, adminChannelName, NotificationManager.IMPORTANCE_HIGH);
        adminChannel.setDescription(adminChannelDescription);
        adminChannel.enableLights(true);
        adminChannel.setLightColor(Color.RED);
        adminChannel.enableVibration(true);
        if (notificationManager != null) {
            notificationManager.createNotificationChannel(adminChannel);
        }
    }
}