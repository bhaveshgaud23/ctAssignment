package com.example.myctapp;

import android.os.Bundle;
import android.util.Log;
import com.clevertap.android.sdk.CleverTapAPI;
import com.clevertap.android.sdk.pushnotification.NotificationInfo;
import java.util.Map;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

public class MyFcmMessageListenerService extends FirebaseMessagingService {

    @Override
    public void onMessageReceived(RemoteMessage message){
        try {
            if (message.getData().size() > 0) {
                Bundle extras = new Bundle();
                for (Map.Entry<String, String> entry : message.getData().entrySet()) {
                    extras.putString(entry.getKey(), entry.getValue());
                }

                NotificationInfo info = CleverTapAPI.getNotificationInfo(extras);
                // Do not do any network/db IO before this
                if (info.fromCleverTap) {
                    CleverTapAPI.createNotification(getApplicationContext(), extras);

                } else {
                    Log.d("MYFCMLIST", "Received push notification NOT from CleverTap");
                }

            }
        } catch (Throwable t) {
            Log.d("MYFCMLIST", "Error parsing FCM message", t);
        }
    }
}

