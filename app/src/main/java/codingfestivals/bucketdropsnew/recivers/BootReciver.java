package codingfestivals.bucketdropsnew.recivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import codingfestivals.bucketdropsnew.extras.Util;

public class BootReciver extends BroadcastReceiver {

    public BootReciver() {
        Log.d("utsab", "BootReciver");
    }

    public void onReceive(Context context, Intent intent) {
        if (Intent.ACTION_BOOT_COMPLETED.equals(intent.getAction())) {
            Log.d("utsab", "Boot Completed");
        }
        Log.d("utsab", "onRecive");
        Log.d("utsab", "Boot Completed1");
        Log.d("utsab", "Boot Completed2");
        Log.d("utsab", "Boot Completed33");
        Log.d("utsab", "Boot Completed44");
        //Log.d("utsab", "Boot Completed3");
        //Log.d("utsab", "Boot Completed4");
        Util.scheduleAlarm(context);
        //Util.fireNotification(context);
    }
}
