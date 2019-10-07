package com.wellgel.london.FireBaseManager;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

public class MyNotificationPublisher extends BroadcastReceiver {
    public void onReceive(Context context, Intent intent) {

        Toast.makeText(context, "" + intent.getAction(), Toast.LENGTH_SHORT).show();
    }
}