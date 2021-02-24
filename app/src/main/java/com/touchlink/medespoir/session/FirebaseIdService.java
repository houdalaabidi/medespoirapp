package com.touchlink.medespoir.session;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.touchlink.medespoir.MVP.views.ui.activities.MainActivity;
import com.touchlink.medespoir.R;
import com.touchlink.medespoir.utils.MEConstants;

import java.util.Map;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;

public class FirebaseIdService extends FirebaseMessagingService {

    public static String refreshedToken = null;


    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public void onNewToken(@NonNull String s) {
        super.onNewToken(s);
        refreshedToken = s;

    }


    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {


        String channelId = getString(R.string.default_notification_channel_id);
        String title;
        String content;

        if (remoteMessage.getData().size() > 0) {
            Map<String, String> dataObject = remoteMessage.getData();
            title = dataObject.get("title");
            content = dataObject.get("body");

            Intent intent = new Intent(FirebaseIdService.this, MainActivity.class);
            PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
            Uri defaultSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            NotificationCompat.Builder builder = new NotificationCompat.Builder(this, channelId)
                    .setContentText(title)
                    .setContentText(content)
                    .setSmallIcon(R.drawable.logo_medespoir)
                    .setAutoCancel(true)
                    .setSound(defaultSound)
                    .setContentIntent(pendingIntent);

            NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            //Utility.isNotificated = true ;

            //  Support Version >= Android 8
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

                CharSequence channelName = "Message provenant de Firebase";
                int importance = NotificationManager.IMPORTANCE_HIGH;
                NotificationChannel mChannel = new NotificationChannel(channelId, channelName, importance);
                mChannel.setVibrationPattern(new long[]{0, 1000, 500, 1000});
                mChannel.enableVibration(true);
                mChannel.enableLights(true);
                notificationManager = getSystemService(NotificationManager.class);
                notificationManager.createNotificationChannel(mChannel);
            }
            if( SharedPreferencesUserFactory.getNotificationsSettings()){
                notificationManager.notify(007, builder.build());
            }

        }
    }
}
