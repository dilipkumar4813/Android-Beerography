package iamdilipkumar.com.beerography.services;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.TaskStackBuilder;
import android.support.v7.app.NotificationCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import iamdilipkumar.com.beerography.R;
import iamdilipkumar.com.beerography.ui.activities.SplashActivity;

/**
 * Created on 10/07/17.
 *
 * @author dilipkumar4813
 * @version 1.0
 */

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);

        String message = null;

        // Check if message contains a notification payload.
        if (remoteMessage.getNotification() != null) {
            message = remoteMessage.getNotification().getBody();
        }

        if (message != null) {
            buildNotification(message);
        }
    }

    private void buildNotification(String message) {
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this);
        mBuilder.setSmallIcon(R.drawable.main_logo);
        mBuilder.setContentTitle(getString(R.string.app_name));
        mBuilder.setContentText(message);

        Intent resultIntent = new Intent(this, SplashActivity.class);

        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
        stackBuilder.addParentStack(SplashActivity.class);

        // Adds the Intent that starts the Activity to the top of the stack
        stackBuilder.addNextIntent(resultIntent);
        PendingIntent resultPendingIntent = stackBuilder
                .getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
        mBuilder.setContentIntent(resultPendingIntent);

        NotificationManager mNotificationManager = (NotificationManager)
                getSystemService(Context.NOTIFICATION_SERVICE);

        // notificationID allows you to update the notification later on.
        mNotificationManager.notify(0, mBuilder.build());
    }
}
