package com.example.modernizedshapp.doctor.Diagnosis.helpers;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;

import com.example.modernizedshapp.R;
import com.example.modernizedshapp.doctor.Diagnosis.SplashActivity;

public class Receiver extends BroadcastReceiver {
    private final static String NOTIFICATION_CHANNEL_NAME = "SelfDiagnosis_Notification_Channel";
    private final static String NOTIFICATION_CHANNEL_ID = "SelfDiagnosis_Channel_01";

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onReceive(Context context, Intent intent) {
        showNotification(context);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void showNotification(Context context) {
        Intent intent = new Intent(context, SplashActivity.class);
        PendingIntent pi = PendingIntent.getActivity(context, NotificationHelper.REQUEST_CODE, intent, 0);
        createNotificationChannel(context);
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context)
                .setSmallIcon(R.drawable.stethoscope)
                .setContentTitle(context.getString(R.string.notification_title))
                .setChannelId(NOTIFICATION_CHANNEL_ID)
                .setContentText(context.getString(R.string.notification_message));
        mBuilder.setContentIntent(pi);
        mBuilder.setDefaults(Notification.DEFAULT_SOUND);
        mBuilder.setAutoCancel(true);
        NotificationManager mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.notify(NotificationHelper.REQUEST_CODE, mBuilder.build());
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void createNotificationChannel(Context context) {
        NotificationManager mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        int importance = NotificationManager.IMPORTANCE_LOW;
        NotificationChannel mChannel = new NotificationChannel(NOTIFICATION_CHANNEL_ID, NOTIFICATION_CHANNEL_NAME, importance);
        mChannel.enableLights(true);
        if (mNotificationManager != null) {
            mNotificationManager.createNotificationChannel(mChannel);
        }
    }


}