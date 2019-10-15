package com.wellgel.london.FireBaseManager;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.util.Log;


import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import com.wellgel.london.Customer.Activities.Ecom_AppointlistDetail;
import com.wellgel.london.Provider.Activities.P_AcceptRejectAct;
import com.wellgel.london.R;
import com.wellgel.london.UtilClasses.ConstantClass;
import com.wellgel.london.UtilClasses.PreferencesShared;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;


public class MyFirebaseService extends FirebaseMessagingService {

    private static final String TAG = "FirebaseMessService";
    private int NOTIFICATION_ID = 0;
    private NotificationCompat.Builder mBuilder;
    private String title = "F17ONE";

    PreferencesShared shared;


    @Override
    public void onNewToken(String token) {
        Log.d(TAG, "Refreshedtoken: " + token);
        shared = new PreferencesShared(getApplicationContext());
        shared.setString(ConstantClass.FIREBASE_TOKEN, token);
        // If you want to send messages to this application instance or
        // manage this apps subscriptions on the server side, send the
        // Instance ID token to your app server.
    }

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);


        shared = new PreferencesShared(getApplicationContext());

        // TODO(developer): Handle FCM messages here.
        // Not getting messages here? See why this may be: https://goo.gl/39bRNJ
        Log.d(TAG, "From: " + remoteMessage.getFrom());

        // Check if message contains a data payload.
        if (remoteMessage.getData().size() > 0) {
            Log.d(TAG, "Message data payload: " + remoteMessage.getData().get("data"));
            String tragetId = "";

            String response = remoteMessage.getData().toString();
            try {

                JSONObject json = new JSONObject(response);
                JSONObject jsonObject = json.getJSONObject("data");
                tragetId = jsonObject.getString("target_id");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {


//                if (remoteMessage.getNotification() != null) {
//                    Log.d(TAG, "Message Notification Body: " + remoteMessage.getNotification().getBody());
                if (remoteMessage.getNotification() != null)
                    Greater_M_version(remoteMessage.getNotification().getTitle(), remoteMessage.getNotification().getBody(), tragetId);
            } else {
                if (remoteMessage.getNotification() != null)
                    setNotification(remoteMessage.getNotification().getTitle(), remoteMessage.getNotification().getBody(), tragetId);
//                }
            }
            if (/* Check if data needs to be processed by long running job */ true) {
                // For long-running tasks (10 seconds or more) use WorkManager.

            } else {
                // Handle message within 10 seconds

            }

        }

        // Check if message contains a notification payload.


    }

    private void setNotification(String title, String data, String target_id) {
        Intent intent = null;
        if (shared.getString(ConstantClass.ROLL_PLAY).equalsIgnoreCase(ConstantClass.ROLL_PROVIDER)) {
            intent = new Intent(this, P_AcceptRejectAct.class);
            intent.putExtra("appo_id_noti", target_id);
        } else if (shared.getString(ConstantClass.ROLL_PLAY).equalsIgnoreCase(ConstantClass.ROLL_CUSTOMER)) {
            intent = new Intent(this, Ecom_AppointlistDetail.class);
            intent.putExtra("appo_id_noti", target_id);
        }

        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        PendingIntent notifyPendingIntent = PendingIntent.getActivity(getApplicationContext(), 0, intent, PendingIntent.FLAG_ONE_SHOT);

        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(getApplicationContext());
        Notification notification;
        notification = mBuilder.setSmallIcon(R.mipmap.logo).setTicker("New Notification").setWhen(0)
                .setAutoCancel(true)
                .setContentIntent(notifyPendingIntent)
                .setContentTitle(title)
                .setSmallIcon(R.mipmap.logo)
                .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.logo))
                .setContentText(data)
                .setSound(defaultSoundUri)
                .build();
        notification.flags |= Notification.FLAG_AUTO_CANCEL;
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(NOTIFICATION_ID, notification);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void Greater_M_version(String title, String messageData, String target_id) {
        Intent intent = null;
        if (shared.getString(ConstantClass.ROLL_PLAY).equalsIgnoreCase(ConstantClass.ROLL_PROVIDER)) {
            intent = new Intent(this, P_AcceptRejectAct.class);
            intent.putExtra("appo_id_noti", target_id);
        } else if (shared.getString(ConstantClass.ROLL_PLAY).equalsIgnoreCase(ConstantClass.ROLL_CUSTOMER)) {
            intent = new Intent(this, Ecom_AppointlistDetail.class);
            intent.putExtra("appo_id_noti", target_id);

        }

        Notification notification;
        String channelId = getApplicationContext().getString(R.string.default_notification_channel_id);
        NotificationChannel channel = null;
        // channel = new NotificationChannel(getApplicationContext().getPackageName(), getApplicationContext().getString(R.string.app_name), NotificationManager.IMPORTANCE_HIGH);
        channel = new NotificationChannel(channelId, "a", NotificationManager.IMPORTANCE_HIGH);

        channel.enableVibration(true);
        channel.setDescription(messageData);
        channel.setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION), null);
        NotificationManager manager = (NotificationManager) getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);
        if (manager != null) {
            manager.createNotificationChannel(channel);
        }

        mBuilder = new NotificationCompat.Builder(this, channelId);
        mBuilder.setChannelId(channelId);
        Bitmap bitmap = BitmapFactory.decodeResource(getApplicationContext().getResources(), R.mipmap.logo);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        PendingIntent notifyPendingIntent = PendingIntent.getActivity(getApplicationContext(), 0, intent, PendingIntent.FLAG_ONE_SHOT);
        notification = mBuilder.setSmallIcon(R.mipmap.logo)
                //.setAutoCancel(true)
                .setContentTitle(title)
                .setContentIntent(notifyPendingIntent)
                .setLargeIcon(bitmap)
                .setContentText(messageData)
                .setSound(defaultSoundUri)
                .build();

        // notification.flags |= Notification.FLAG_AUTO_CANCEL;
        NotificationManager notificationManager = (NotificationManager) getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            if (notificationManager != null) {
                notificationManager.createNotificationChannel(channel);
                notificationManager.notify(1, notification);

            }
        }

//
    }
}
