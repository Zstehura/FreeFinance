package com.example.financefree.notifier;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.os.SystemClock;

import androidx.core.app.NotificationCompat;

import com.example.financefree.MainActivity;
import com.example.financefree.R;
import com.example.financefree.database.entities.RecurringPayment;
import com.example.financefree.database.entities.SinglePayment;
import com.example.financefree.structures.Frequency;

public final class NotificationHelper {
    public static final String NOTIFICATION_CHANNEL_ID = "FreeFinanceApp-notifier";
    public static final CharSequence NOTIFICATION_CHANNEL_NAME = "FreeFinanceApp";
    public static final int CHANNEL_IMPORTANCE = NotificationManager.IMPORTANCE_DEFAULT;

    public static NotificationChannel makeNotificationChannel() {
        return new NotificationChannel(NOTIFICATION_CHANNEL_ID, NOTIFICATION_CHANNEL_NAME, CHANNEL_IMPORTANCE);
    }

    private static Notification makeNotif(String title, String content, Context context) {
        return new NotificationCompat.Builder(context, NOTIFICATION_CHANNEL_ID)
                .setSmallIcon(R.drawable.brobot_excited)
                .setContentTitle(title)
                .setContentText(content)
                .setStyle(new NotificationCompat.BigTextStyle()
                        .bigText(content))
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setCategory(NotificationCompat.CATEGORY_REMINDER)
                .setVisibility(NotificationCompat.VISIBILITY_PRIVATE)
                .build();
    }

    public static void makeChannel(Context context) {
        CharSequence name = context.getString(R.string.channel_name);
        String desc = context.getString(R.string.channel_description);
        int imp = android.app.NotificationManager.IMPORTANCE_DEFAULT;
        NotificationChannel channel = new NotificationChannel("TAG" ,name, imp);
        channel.setDescription(desc);
        android.app.NotificationManager notificationManager = context.getSystemService(android.app.NotificationManager.class);
        notificationManager.createNotificationChannel(channel);
    }

    public void scheduleNotification(Context context, long delay, int notificationId) {//delay is after how much time(in millis) from current time you want to schedule the notification
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "TAG")
                .setContentTitle(context.getString(R.string.app_name))
                .setContentText(context.getString(R.string.loan_calc_desc))
                .setAutoCancel(true)
                .setSmallIcon(R.drawable.ic_down)
                .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION));

        Intent intent = new Intent(context, MainActivity.class);
        PendingIntent activity = PendingIntent.getActivity(context, notificationId, intent, PendingIntent.FLAG_CANCEL_CURRENT);
        builder.setContentIntent(activity);

        Notification notification = builder.build();

        //Intent notificationIntent = new Intent(context, MyNotificationManager.MyNotificationPublisher.class);
        //notificationIntent.putExtra(MyNotificationManager.MyNotificationPublisher.NOTIFICATION_ID, notificationId);
        //notificationIntent.putExtra(MyNotificationManager.MyNotificationPublisher.NOTIFICATION, notification);
        //PendingIntent pendingIntent = PendingIntent.getBroadcast(context, notificationId, notificationIntent, PendingIntent.FLAG_CANCEL_CURRENT);

        long futureInMillis = SystemClock.elapsedRealtime() + delay;
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        //alarmManager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, futureInMillis, pendingIntent);
    }

}
