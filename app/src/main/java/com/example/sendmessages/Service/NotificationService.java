package com.example.sendmessages.Service;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Build;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;

import com.example.sendmessages.R;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

/**
 * Класс отвечает за уведомления от сервера
 *
 * @see NotificationService
 * @deprecated
 */
@Deprecated
public class NotificationService extends FirebaseMessagingService {

    @Override
    public void onNewToken(@NonNull String token) {
        super.onNewToken(token);
    }

    @Override
    public void onMessageReceived(@NonNull RemoteMessage message) {
        super.onMessageReceived(message);
        getNotification(message.getNotification().getTitle(), message.getNotification().getBody());
    }

    private void getNotification(String title, String message) {
        NotificationCompat.Builder builder =
                new NotificationCompat.Builder(this, "channel")
                        .setSmallIcon(R.drawable.ic_baseline_sms_24)
                        .setContentTitle(title)
                        .setContentText(message)
                        .setAutoCancel(true);

        NotificationManager manager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    "channel",
                    "channel",
                    NotificationManager.IMPORTANCE_DEFAULT
            );
            manager.createNotificationChannel(channel);
        }
        manager.notify(1, builder.build());

        Log.i("папа", title + message);
    }
}
