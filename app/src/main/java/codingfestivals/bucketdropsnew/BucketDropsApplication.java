package codingfestivals.bucketdropsnew;

import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Build;
import android.widget.TextView;


import codingfestivals.bucketdropsnew.adapters.Filter;
import io.realm.Realm;
import io.realm.RealmConfiguration;

public class BucketDropsApplication extends Application {
    public static final String CHANNEL_ID="CodingFestivals";
    public static final String CHANNEL_NAME="CodingFestivals";
    public static final String CHANNEL_DESC="CodingFestivals Notification";
    //this class is for Realm configuration
    @Override
    public void onCreate() {
        super.onCreate();
        Realm.init(this);
        createNotificationChannel();
        RealmConfiguration config = new RealmConfiguration.Builder().name("bucketdrops.realm").build();
        Realm.setDefaultConfiguration(config);
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, CHANNEL_NAME, NotificationManager.IMPORTANCE_DEFAULT);
            channel.setDescription(CHANNEL_DESC);
            NotificationManager notificationManager=getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    //when menu item click then that click state(complete/incomplete...) save into shared preference and we can retrive data
    //when application newly opened
    public static void save(Context context,int filterOption) {
        //MODE_PRIVATE for anohter application not enter my application data
        //SharedPreferences sharedPreferences = getPreferences(MODE_PRIVATE);
        //here application class can be used a context,but if method is static than application class will not a context
        //SharedPreferences userDetails = context.getSharedPreferences("userdetails", MODE_PRIVATE);
        SharedPreferences sharedPreferences= context.getSharedPreferences("menu_click",MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        //filterOption(1->MOST_TIME_LEFT,2->LEAST_TIME_LEFT,3->COMPLETE..) save into sharedpreference object
        editor.putInt("filter", filterOption);
        editor.apply();
    }

    //this method for retrive state when app is open
    //if we select state(complete/incomplete/...) perviously than we retrive now and
    //by that state(complete/incomplete/least time/...) we showing our data
    public static int load(Context context) {
        //SharedPreferences sharedPreferences = getPreferences(MODE_PRIVATE);
        SharedPreferences sharedPreferences = context.getSharedPreferences("menu_click",MODE_PRIVATE);
        int filter_option = sharedPreferences.getInt("filter", Filter.NONE);
        return filter_option;
    }
    public static void setRalewayRegular(Context context, TextView textView){
        Typeface typeface=Typeface.createFromAsset(context.getAssets(),"fonts/raleway_thin.ttf");
        textView.setTypeface(typeface);
    }
    public static void setRalewayRegular(Context context, TextView ...textViews){
        Typeface typeface=Typeface.createFromAsset(context.getAssets(),"fonts/raleway_thin.ttf");
        for(TextView textView:textViews){
            textView.setTypeface(typeface);
        }
    }
}
