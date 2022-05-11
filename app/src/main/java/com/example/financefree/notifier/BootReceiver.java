package com.example.financefree.notifier;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.example.financefree.database.AppDatabase;
import com.example.financefree.database.entities.NotificationInfo;

import java.util.Date;
import java.util.List;

public class BootReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        if(intent.getAction().equals(Intent.ACTION_BOOT_COMPLETED)) {
            Thread t = new Thread(() -> {
                Date d = new Date();
                AppDatabase db = AppDatabase.getDatabase(context);
                List<NotificationInfo> list = db.daoNotificationInfo().getAll();

                for(NotificationInfo ni: list) {
                    if(ni.notif_time > d.getTime()) {
                        NotificationReceiver.scheduleNotification(context, ni);
                    }
                }
                db.close();
            });
            t.start();
        }
    }
}
