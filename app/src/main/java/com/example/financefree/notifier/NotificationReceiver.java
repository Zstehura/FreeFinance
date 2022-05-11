package com.example.financefree.notifier;


import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import androidx.core.app.NotificationCompat;

import com.example.financefree.R;
import com.example.financefree.database.entities.NotificationInfo;

public class NotificationReceiver extends BroadcastReceiver {

    public static final String NOTIFICATION_CHANNEL_ID = "FreeFinanceApp-notifier";
    public static final CharSequence NOTIFICATION_CHANNEL_NAME = "FreeFinanceApp";
    public static final String TITLE_KEY = "title";
    public static final String CONTENT_KEY = "content";
    public static final String ID_KEY = "id";
    public static final int CHANNEL_IMPORTANCE = NotificationManager.IMPORTANCE_DEFAULT;

    private static NotificationChannel notificationChannel;

    @Override
    public void onReceive(Context context, Intent intent) {
        createNotificationChannel(context);

        // Build notification based on Intent
        String content = intent.getStringExtra(CONTENT_KEY);
        Notification notification = new NotificationCompat.Builder(context, NOTIFICATION_CHANNEL_ID)
                .setSmallIcon(R.drawable.brobot_excited)
                .setContentTitle(intent.getStringExtra(TITLE_KEY))
                .setContentText(content)
                .setStyle(new NotificationCompat.BigTextStyle().bigText(content))
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setVisibility(NotificationCompat.VISIBILITY_PRIVATE)
                .build();
        // Show notification
        NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        manager.notify((int) intent.getLongExtra(ID_KEY, 0), notification);
    }

    public static void scheduleNotification(Context context, NotificationInfo ni) {
        Intent intent = new Intent(context, NotificationReceiver.class);
        intent.putExtra(TITLE_KEY, ni.title);
        intent.putExtra(CONTENT_KEY, ni.message);
        intent.putExtra(ID_KEY, ni.notif_id);
        PendingIntent pending = PendingIntent.getBroadcast(context, (int) ni.notif_id, intent, PendingIntent.FLAG_ONE_SHOT + PendingIntent.FLAG_IMMUTABLE);
        // Schedule notification
        AlarmManager manager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        manager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, ni.notif_time, pending);
    }

    private void createNotificationChannel(Context context) {
        if(notificationChannel == null) {
            String description = context.getString(R.string.channel_description);
            notificationChannel = new NotificationChannel(NOTIFICATION_CHANNEL_ID, NOTIFICATION_CHANNEL_NAME,
                    CHANNEL_IMPORTANCE);
            notificationChannel.setDescription(description);
            NotificationManager nm = context.getSystemService(NotificationManager.class);
            nm.createNotificationChannel(notificationChannel);
        }
    }
}
