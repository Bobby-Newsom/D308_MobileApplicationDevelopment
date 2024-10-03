package com.example.bobbynewsom_001265608.UI;
import com.example.bobbynewsom_001265608.R;

import static android.os.Build.VERSION_CODES.R;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.widget.Toast;

import androidx.core.app.NotificationCompat;



public class MyReceiver extends BroadcastReceiver {

    String channel_id = "vacation_channel";
    static int notificationID;

    @Override
    public void onReceive(Context context, Intent intent) {
        // Get action from intent and set the notification message accordingly
        String action = intent.getAction();
        String title = null;
        String contentText = null;

        if (action != null) {
            switch (action) {
                case "start action":
                    title = "Vacation Start Alert";
                    contentText = intent.getStringExtra("start key");
                    Toast.makeText(context, "Vacation Starting Today!", Toast.LENGTH_LONG).show();
                    break;
                case "end action":
                    title = "Vacation End Alert";
                    contentText = intent.getStringExtra("end key");
                    Toast.makeText(context, "Vacation Ending Today!", Toast.LENGTH_LONG).show();
                    break;
            }
        }

        // Create a notification channel and display the notification
        createNotificationChannel(context, channel_id);
        Notification notification = new NotificationCompat.Builder(context, channel_id)
                .setSmallIcon(android.R.drawable.ic_dialog_info)
                .setContentTitle(title)
                .setContentText(contentText)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .build();

        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(notificationID++, notification);
    }

    // Method to create a notification channel for Android O and above
    private void createNotificationChannel(Context context, String channel_id) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "Vacation Notification Channel";
            String description = "Channel for vacation start and end alerts";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(channel_id, name, importance);
            channel.setDescription(description);

            NotificationManager notificationManager = context.getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }
}
