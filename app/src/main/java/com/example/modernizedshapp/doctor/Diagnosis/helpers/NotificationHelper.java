package com.example.modernizedshapp.doctor.Diagnosis.helpers;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;

import static android.content.Context.ALARM_SERVICE;

public class NotificationHelper {
    public static final int REQUEST_CODE = 2137;
    public static final int DAYS_NOTIFICATION_DELAY = 7;

    public static void setNotification(PendingIntent pendingIntent, Context context) {
        AlarmManager am = (AlarmManager) context.getSystemService(ALARM_SERVICE);
        if (am != null) {
            am.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(),
                    AlarmManager.INTERVAL_DAY * DAYS_NOTIFICATION_DELAY, pendingIntent);
        }

    }

}
