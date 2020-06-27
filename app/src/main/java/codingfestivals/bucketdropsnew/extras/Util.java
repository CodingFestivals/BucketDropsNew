package codingfestivals.bucketdropsnew.extras;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.util.Log;
import android.view.View;

import java.util.List;

import codingfestivals.bucketdropsnew.BucketDropsApplication;
import codingfestivals.bucketdropsnew.MainActivity;
import codingfestivals.bucketdropsnew.R;
import codingfestivals.bucketdropsnew.beans.Drop;
import codingfestivals.bucketdropsnew.services.NotificationService;

import static android.content.Context.ALARM_SERVICE;

public class Util {
    public static void showViews(List<View> views) {
        for (View view : views) {
            view.setVisibility(View.VISIBLE);
        }
    }

    public static void hideViews(List<View> views) {
        for (View view : views) {
            view.setVisibility(View.GONE);
        }
    }

    public static boolean moreThanJellyBean() {
        return Build.VERSION.SDK_INT > 15;
    }

    public static void setBackground(View itemView, Drawable drawable) {
        if (moreThanJellyBean()) {
            itemView.setBackground(drawable);
        } else {
            itemView.setBackgroundDrawable(drawable);
        }
    }

    public static void scheduleAlarm(Context context) {
        //get ALarmManager
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, NotificationService.class);
        //Context: The Context in which this PendingIntent should start the service.int: Private request code for the senderIntent: An Intent describing the service to be started. This value cannot be null.
        //Intent: An Intent describing the service to be started. This value cannot be null.
        // int: May be FLAG_ONE_SHOT, FLAG_NO_CREATE, FLAG_CANCEL_CURRENT, FLAG_UPDATE_CURRENT, FLAG_IMMUTABLE
        //Flag indicating that if the described PendingIntent already exists, then keep it but replace its extra data with what is in this new Intent.
        PendingIntent pendingIntent = PendingIntent.getService(context, 100, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        // ELAPSED_REALTIME_WAKEUP=>which will wake up the device when it goes off(if device is sleeping it wake up  the device and run service)
        //5000 use for after every 5 sec service will perform
        alarmManager.setInexactRepeating(AlarmManager.RTC, 1000, 15*60*1000, pendingIntent);
    }
    /*public static void fireNotification(Context context) {
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
        Log.d("utsab","FireNotification");
    }*/
}
