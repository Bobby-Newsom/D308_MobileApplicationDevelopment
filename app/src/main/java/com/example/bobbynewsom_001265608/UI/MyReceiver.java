package com.example.bobbynewsom_001265608.UI;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;
import android.widget.Toast;

import androidx.core.app.NotificationCompat;

public class MyReceiver extends BroadcastReceiver {

    String CHANNEL_ID = "vacation_channel";
    static int notificationID;

    @Override
    public void onReceive(Context context, Intent intent) {
        // Retrieve the alert message from the intent
        String alertMessage = intent.getStringExtra("alert_message");

        // If the message is null or empty, provide a fallback message
        if (alertMessage == null || alertMessage.isEmpty()) {
            alertMessage = "Alert triggered, but no message available.";
        }

        // Show the toast with the alert message
        Toast.makeText(context, alertMessage, Toast.LENGTH_LONG).show();

        // Create a notification channel and build the notification
        createNotificationChannel(context);
        Notification notification = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(android.R.drawable.ic_dialog_info) // Ensure you have a valid icon
                .setContentTitle("Excursion Alert")
                .setContentText(alertMessage)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .build();

        // Display the notification
        NotificationManager notificationManager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(notificationID++, notification);
    }


    // Create a notification channel for Android O and above
    private void createNotificationChannel(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "Vacation Notification Channel";
            String description = "Channel for vacation and excursion alerts";
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);

            NotificationManager notificationManager = context.getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }
}
