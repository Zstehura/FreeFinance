package com.example.financefree;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.media.RingtoneManager;
import android.os.SystemClock;

import androidx.core.app.AlarmManagerCompat;
import androidx.core.app.NotificationCompat;


public class NotificationReceiver {
    private static final String TAG = "FreeFinanceAppNotifier";

    public void test1Not(Context context, long time) {
        AlarmManager alarmManager = context.getSystemService(AlarmManager.class);
        // alarmManager.set(AlarmManager.ELAPSED_REALTIME, time, );
    }

    public static Notification makeNotif(String title, String content, Context context) {
        return new NotificationCompat.Builder(context, TAG)
                .setSmallIcon(R.drawable.brobot_ded)
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
        int imp = NotificationManager.IMPORTANCE_DEFAULT;
        NotificationChannel channel = new NotificationChannel(TAG ,name, imp);
        channel.setDescription(desc);
        NotificationManager notificationManager = context.getSystemService(NotificationManager.class);
        notificationManager.createNotificationChannel(channel);
    }

    public void scheduleNotification(Context context, long delay, int notificationId) {//delay is after how much time(in millis) from current time you want to schedule the notification
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, TAG)
                .setContentTitle(context.getString(R.string.app_name))
                .setContentText(context.getString(R.string.loan_calc_desc))
                .setAutoCancel(true)
                .setSmallIcon(R.drawable.ic_down)
                .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION));

        Intent intent = new Intent(context, MainActivity.class);
        PendingIntent activity = PendingIntent.getActivity(context, notificationId, intent, PendingIntent.FLAG_CANCEL_CURRENT);
        builder.setContentIntent(activity);

        Notification notification = builder.build();

        Intent notificationIntent = new Intent(context, MyNotificationPublisher.class);
        notificationIntent.putExtra(MyNotificationPublisher.NOTIFICATION_ID, notificationId);
        notificationIntent.putExtra(MyNotificationPublisher.NOTIFICATION, notification);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, notificationId, notificationIntent, PendingIntent.FLAG_CANCEL_CURRENT);

        long futureInMillis = SystemClock.elapsedRealtime() + delay;
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarmManager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, futureInMillis, pendingIntent);
    }

    public static class MyNotificationPublisher extends BroadcastReceiver {

        public static String NOTIFICATION_ID = "notification_id";
        public static String NOTIFICATION = "notification";

        @Override
        public void onReceive(final Context context, Intent intent) {

            NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

            Notification notification = intent.getParcelableExtra(NOTIFICATION);
            int notificationId = intent.getIntExtra(NOTIFICATION_ID, 0);
            notificationManager.notify(notificationId, notification);
        }
    }
}
