package codingfestivals.bucketdropsnew.services;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.util.Log;


import codingfestivals.bucketdropsnew.BucketDropsApplication;
import codingfestivals.bucketdropsnew.MainActivity;
import codingfestivals.bucketdropsnew.R;
import codingfestivals.bucketdropsnew.beans.Drop;
import io.realm.Realm;
import io.realm.RealmResults;


/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p>
 * TODO: Customize class - update intent actions and extra parameters.
 */
public class NotificationService extends IntentService {
    public static int x = 0;

    public NotificationService() {
        super("NotificationService");
        Log.d("utsab", "Notification Service" + x);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Log.d("utsab", "onHandleIntent :" + x++);
        Realm realm = null;
        try {
            realm = Realm.getDefaultInstance();
            RealmResults<Drop> realmResults = realm.where(Drop.class).equalTo("completed", false).findAll();
            //fireNotification(realmResults.first());
            for (Drop drop : realmResults) {
                if (isNotificationNeed(drop.getAdded(), drop.getWhen())) {
                    //Log.d("utsab","onHandleIntent: notifcation needed");
                    fireNotification(drop);
                }
            }
        } finally {
            if (realm != null) {
                realm.close();
            }
        }
    }

    private static int i = 1;

    private void fireNotification(Drop drop) {
        //Log.d("utsab", "onHandleIntentvvv :" + (++i));
       /* Bitmap largeIcon = BitmapFactory.decodeResource(getResources(), R.drawable.ic_drop);
        Intent intent = new Intent(this, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 1, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        String message = drop.getWhat();
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, BucketDropsApplication.CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_mail_outline)
                .setLargeIcon(largeIcon)
                .setStyle(new NotificationCompat.BigTextStyle()
                        .bigText(message)
                        .setBigContentTitle(getString(R.string.notif_title))
                        .setSummaryText(getString(R.string.notif_message)))
                .setContentTitle(getString(R.string.notif_title))
                .setContentText(getString(R.string.notif_message))
                .setAutoCancel(true)
                .setContentIntent(pendingIntent)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);
        NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(this);
        notificationManagerCompat.notify(100 + (++i), builder.build());*/
        String message = drop.getWhat();
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, BucketDropsApplication.CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_mail_outline)
                .setContentTitle(message)
                .setContentText(getString(R.string.notif_message))
                .setAutoCancel(true)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);
        NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(this);
        notificationManagerCompat.notify(100+(++i), builder.build());
        Log.d("utsab","Notification "+i);
    }

    /*public  void fireNotification(Context context) {
        //Log.d("utsab", "onHandleIntentvvv :" + (++i));
        //Bitmap largeIcon = BitmapFactory.decodeResource(getResources(), R.drawable.ic_drop);
        Intent intent = new Intent(context, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 1, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, BucketDropsApplication.CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_mail_outline)
                .setContentTitle(context.getString(R.string.notif_title))
                .setContentText(context.getString(R.string.notif_message))
                .setAutoCancel(true)
                .setContentIntent(pendingIntent)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);
        NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(context);
        notificationManagerCompat.notify(100, builder.build());
    }*/

    private boolean isNotificationNeed(long added, long when) {
        long now = System.currentTimeMillis();
        //present date greater than future time than we false
        if (now > when) {
            return false;
        } else {
            //when is future date and added is past date when entry added
            //here we store 90 percent of time (when-added) in difference90
            long difference90 = (long) (0.9 * (when - added));
            //if today is greater than 90 percent of time than we return true(true means give notification)
            //if we added a time less than 90 percent of time than we not give notification(means return false)
            return (now > (added + difference90)) ? true : false;
        }
    }
}
