package com.example.financefree.notifier;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import androidx.core.app.AlarmManagerCompat;

public class NotificationReceiver extends BroadcastReceiver {
    public static final String RECURRING_NOTE = "recurring";
    public static final String TEMP_TITLE = "placeholder";

    @Override
    public void onReceive(Context context, Intent intent) {
        if (Intent.ACTION_BOOT_COMPLETED.equals(intent.getAction())) {
            //startRescheduleAlarmsService(context);
        }
        else {
            if(!intent.getBooleanExtra(RECURRING_NOTE, false)) {

            }
        }
    }

    private void startAlarmService(Context context, Intent intent) {
        Intent intentService = new Intent(context, AlarmManagerCompat.class);
        intentService.putExtra(TEMP_TITLE, intent.getStringExtra(TEMP_TITLE));
        context.startForegroundService(intentService);
    }

    private void startRescheduleAlarmsService(Context context) {
        //Intent intentService = new Intent(context, RescheduleAlarmsService.class);
        //context.startForegroundService(intentService);

    }

/*
    private void startRescheduleAlarmsService(Context context) {
        Intent intentService = new Intent(context, RescheduleAlarmsService.class);
        context.startForegroundService(intentService);
        context.startService(intentService);
    }

 */
}
