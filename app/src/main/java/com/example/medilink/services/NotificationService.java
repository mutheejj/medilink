package com.example.medilink.services;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import androidx.core.app.NotificationCompat;

import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.RemoteMessage;
import com.example.medilink.R;
import com.example.medilink.activities.HomeActivity;

public class NotificationService {
    private static NotificationService instance;
    private Context context;
    private static final String CHANNEL_ID = "MediLink_Channel";
    private static final String CHANNEL_NAME = "MediLink Notifications";
    private static final String CHANNEL_DESC = "Notifications for bed availability and patient updates";

    private NotificationService(Context context) {
        this.context = context;
        createNotificationChannel();
    }

    public static synchronized NotificationService getInstance(Context context) {
        if (instance == null) {
            instance = new NotificationService(context.getApplicationContext());
        }
        return instance;
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    CHANNEL_ID,
                    CHANNEL_NAME,
                    NotificationManager.IMPORTANCE_HIGH
            );
            channel.setDescription(CHANNEL_DESC);

            NotificationManager manager = context.getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel);
        }
    }

    public void subscribeToTopic(String topic) {
        FirebaseMessaging.getInstance().subscribeToTopic(topic)
                .addOnCompleteListener(task -> {
                    if (!task.isSuccessful()) {
                        // Handle subscription failure
                    }
                });
    }

    public void unsubscribeFromTopic(String topic) {
        FirebaseMessaging.getInstance().unsubscribeFromTopic(topic)
                .addOnCompleteListener(task -> {
                    if (!task.isSuccessful()) {
                        // Handle unsubscription failure
                    }
                });
    }

    public void showNotification(String title, String message) {
        Intent intent = new Intent(context, HomeActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent,
                PendingIntent.FLAG_IMMUTABLE);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_notification)
                .setContentTitle(title)
                .setContentText(message)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setAutoCancel(true)
                .setContentIntent(pendingIntent);

        NotificationManager notificationManager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(0, builder.build());
    }

    public void handleFCMMessage(RemoteMessage remoteMessage) {
        if (remoteMessage.getData().size() > 0) {
            // Handle data payload
            String title = remoteMessage.getData().get("title");
            String message = remoteMessage.getData().get("message");
            if (title != null && message != null) {
                showNotification(title, message);
            }
        }

        if (remoteMessage.getNotification() != null) {
            // Handle notification payload
            showNotification(
                    remoteMessage.getNotification().getTitle(),
                    remoteMessage.getNotification().getBody()
            );
        }
    }
}