package codingfestivals.bucketdropsnew.widgets;

import android.content.Context;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import codingfestivals.bucketdropsnew.R;

public class BucketPickerView extends LinearLayout implements View.OnTouchListener {
    private static final int MESSAGE_WHAT = 123;
    private TextView txt_date,txt_month,txt_year;
    private Calendar calendar;
    SimpleDateFormat dateFormat;
    //(Handeler 8)this four variable we created for indication index of drawable array in processEventsFor() method
    public static final int LEFT=0;
    public static final int TOP=1;
    public static final int RIGHT=2;
    public static final int BOTTOM=3;
    //(Handeler 1)for identify increament or decreament value in textview
    private boolean inCreament;
    private boolean deCreament;
    //this variable store the id of which textview is clicked
    private int activeId;
    //DELAY for we increament or decreament value of textview after 250 milisecond,if we want we reduce or increase the value of DELAY
    public static final int DELAY=250;
    //(Handeler 2)handeler which handle the message
    private Handler handler=new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(@NonNull Message msg) {
            //Handeler(10) this is for if inCreament is true that means we hold on textview so we increament value of textview
            if(inCreament){
                increament(activeId);
            }
            //Handeler(10) this is for if deCreament is true that means we hold on textview so we decrement value of textview
            if(deCreament){
                decreament(activeId);
            }
            if(inCreament||deCreament){
                handler.sendEmptyMessageDelayed(MESSAGE_WHAT,DELAY);
            }
            return true;
        }
    });
    public BucketPickerView(Context context) {
        super(context);
        init(context);
    }

    public BucketPickerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public BucketPickerView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    public BucketPickerView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context);
    }
    private void init(Context context){
        View view= LayoutInflater.from(context).inflate(R.layout.bucket_picker_view,this);
        calendar=Calendar.getInstance();
        dateFormat=new SimpleDateFormat("MMM");
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        txt_date=this.findViewById(R.id.id_tv_date);
        txt_month=this.findViewById(R.id.id_tv_month);
        txt_year=this.findViewById(R.id.id_tv_year);
        //touch listener set
        txt_date.setOnTouchListener(this);
        txt_month.setOnTouchListener(this);
        txt_year.setOnTouchListener(this);
        int date=calendar.get(Calendar.DATE);
        int month=calendar.get(Calendar.MONTH);
        int year=calendar.get(Calendar.YEAR);
        update(date,month,year,0,0,0);
    }
    private void update(int date,int month,int year,int hour,int minute,int second){
        calendar.set(Calendar.DATE,date);
        calendar.set(Calendar.MONTH,month);
        calendar.set(Calendar.YEAR,year);
        calendar.set(Calendar.HOUR,hour);
        calendar.set(Calendar.MINUTE,minute);
        calendar.set(Calendar.SECOND,second);
        txt_year.setText(year+"");
        txt_month.setText(dateFormat.format(calendar.getTime()));
        txt_date.setText(date+"");
    }
    public long getTime(){
        return calendar.getTimeInMillis();
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        //for specific textview we perform action
        switch (v.getId()){
            case R.id.id_tv_date:
                processEventsFor(txt_date,event);
                break;
            case R.id.id_tv_month:
                processEventsFor(txt_month,event);
                break;
            case R.id.id_tv_year:
                processEventsFor(txt_year,event);
                break;
        }
        return true;
    }
    //
    private void processEventsFor(TextView textView,MotionEvent motionEvent){
        //getCompoundDrawables(),Returns drawables for the left(0), top(1), right(2), and bottom(3) borders.
        Drawable drawable[]=textView.getCompoundDrawables();
        //this method(hasDrawableTop()) for check drawable[top] has value and method(hasDrawableBottom() has value)
        if(hasDrawableTop(drawable)&&hasDrawableBottom(drawable)){
            //(Handeler 9) get the id of textview which textview is clicked
            activeId=textView.getId();
            Rect topBounds=drawable[TOP].getBounds();
            Rect bottomBounds=drawable[BOTTOM].getBounds();
            //this is for x axis loaction of textview
            float x=motionEvent.getX();
            //this is for y axis location of textview
            float y=motionEvent.getY();
            //if top of textview click increament value
            if(topDrawableHit(textView,topBounds.height(),x,y)){
                //fingure pressed in top of textview
                if(isActionDown(motionEvent)){
                    //(Handeler 3) if fingure pressed than inCreament will be true
                    inCreament=true;
                    //increament value of which textview(date/month/year) is clicked
                    increament(textView.getId());
                    //(Handeler 7) now we remove all messages from Handeler if any garbage message have in handeler
                    handler.removeMessages(MESSAGE_WHAT);
                    //(Handeler 6) now we send message by handeler with delay,delay use for send message some delay
                    //MESSAGE_WHAT you can use any value it is use for when multiple message send than differentiate messages
                    handler.sendEmptyMessageDelayed(MESSAGE_WHAT,DELAY);
                    //top of textview is clicked than we show pressed icon in upper of textview
                    toggleDrawable(textView,true);
                }
                //(Handeler 4) fingure up or cancel than increament will be false and value not increamented
                if(isActionUpOrCancel(motionEvent)){
                    inCreament=false;
                    //otherwise showing normal icon in top
                    toggleDrawable(textView,false);
                }
            }
            //if bottom of textview click decreament value
            else if(bottomDrawableHit(textView,bottomBounds.height(),x,y)){
                //fingure pressed in bottom of textview
                if(isActionDown(motionEvent)){
                    //(Handeler 3) if fingure pressed than deCreament will be true
                    deCreament=true;
                    //decreament value of which textview(date/month/year) is clicked
                    decreament(textView.getId());
                    //(Handeler 7) now we remove all messages from Handeler if any garbage message have in handeler
                    handler.removeMessages(MESSAGE_WHAT);
                    //(Handeler 6) now we send message by handeler with delay,delay use for send message some delay
                    //MESSAGE_WHAT you can use any value it is use for when multiple message send than differentiate messages
                    handler.sendEmptyMessageDelayed(MESSAGE_WHAT,DELAY);
                    //top of textview is clicked than we show pressed icon in bottom of textview
                    toggleDrawable(textView,true);
                }
                //(Handeler 4) fingure up or cancel than increament will be false and value not increamented
                if(isActionUpOrCancel(motionEvent)){
                    deCreament=false;
                    //otherwise showing normal icon in bottom
                    toggleDrawable(textView,false);
                }
            }else{
                //else means we click in middle in textview
                //Handeler(5) in middle clicked than inCreament and deCreament will be false
                inCreament=false;
                deCreament=false;
                //otherwise(inCreament and deCreament) showing normal icon in top and bottom
                toggleDrawable(textView,false);
            }

        }
    }

    //this method for check fingure pressed in textview
    //if presessed(down) than we will increament our value
    //if fingure up after pressed no action we perfor
    private boolean isActionUpOrCancel(MotionEvent event) {
        return event.getAction()==MotionEvent.ACTION_UP||event.getAction()==MotionEvent.ACTION_CANCEL;
    }

    //when top of textview click than increament method called and increament date or month or year
    //after increament by set() method data set in textview
    private void increament(int id){
        switch (id){
            case R.id.id_tv_date:
                calendar.add(Calendar.DATE,1);
                break;
            case R.id.id_tv_month:
                calendar.add(Calendar.MONTH,1);
                break;
            case R.id.id_tv_year:
                calendar.add(Calendar.YEAR,1);
                break;
        }
        set(calendar);
    }
    //when bottom of textview click than decreament method called and decreament date or month or year
    //after decreament by set() method data set in textview
    private void decreament(int id){
        switch (id){
            case R.id.id_tv_date:
                calendar.add(Calendar.DATE,-1);
                break;
            case R.id.id_tv_month:
                calendar.add(Calendar.MONTH,-1);
                break;
            case R.id.id_tv_year:
                calendar.add(Calendar.YEAR,-1);
                break;
        }
        //set data in calender data in textview
        set(calendar);
    }
    //this method will set date,month,year in textview from calender
    private void set(Calendar calendar) {
        int date=calendar.get(Calendar.DATE);
        int year=calendar.get(Calendar.YEAR);
        txt_date.setText(date+"");
        txt_year.setText(year+"");
        txt_month.setText(dateFormat.format(calendar.getTime()));
    }
    //this method for check fingure pressed in textview
    //if presessed(down) than we will increament our value
    //if fingure up after pressed no action we perfor
    private boolean isActionDown(MotionEvent event){
        return event.getAction()==MotionEvent.ACTION_DOWN;
    }
    //this method for check upper part of textview click
    //if top of textview click increament value
    //if click in range xmin to xmax than return true
    //bellow calculation shown paint work
    private boolean topDrawableHit(TextView textView,int drawableHeight,float x,float y){
        int xmin=textView.getPaddingLeft();
        int xmax=textView.getWidth()-textView.getPaddingRight();
        int ymin=textView.getPaddingTop();
        int ymax=textView.getPaddingTop()+drawableHeight;
        return x>xmin && x<xmax && y>ymin && y<ymax;
    }
    //this method for check lower part of textview click
    //if bottom of textview click decreament our value
    //if click in range ymin to ymax than return true
    //bellow calculation shown paint work
    private boolean bottomDrawableHit(TextView textView,int drawableHeight,float x,float y){
        int xmin=textView.getPaddingLeft();
        int xmax=textView.getWidth()-textView.getPaddingRight();
        int ymax=textView.getHeight()-textView.getPaddingBottom();
        int ymin=ymax-drawableHeight;
        return x>xmin && x<xmax && y>ymin && y<ymax;
    }
    //this method for check drawable[top] has value
    private boolean hasDrawableTop(Drawable[] drawables){
        return drawables[TOP]!=null;
    }
    //this method for check drawable[bottom] has value
    private boolean hasDrawableBottom(Drawable[] drawables){
        return drawables[BOTTOM]!=null;
    }
    //when  upper of textview clicked we show the upper of textview as pressed
    //when bottom of textview clicked we show the bottom of textview as pressed
    private void toggleDrawable(TextView textView,boolean pressed){
        //whne fingure is preseed in textview , pressed will be true
        //when fingure is up than pressed will be false;
        if(pressed){
            //inCreament wil be true until we pressed in textView,until inCreament is true than we show pressed icon on textview
            if(inCreament){
                textView.setCompoundDrawablesWithIntrinsicBounds(0,R.drawable.up_pressed,0,R.drawable.down_normal);
            }
            //deCreament wil be true until we pressed in textView,until deCreament is true than we show pressed icon on textview
            if(deCreament){
                textView.setCompoundDrawablesWithIntrinsicBounds(0,R.drawable.up_normal,0,R.drawable.down_pressed);
            }
        }else{
            //pressed is false than we show normal textivew
            textView.setCompoundDrawablesWithIntrinsicBounds(0,R.drawable.up_normal,0,R.drawable.down_normal);
        }
    }

    @Nullable
    @Override
    //this method send/save data before rotate
    //we need to return Parcelable but it is interfaec we can return Bundle object because Bundle object implement method
    //of Parcelabel
    //that's why we can return Bundle object instead of Parcelable
    protected Parcelable onSaveInstanceState() {
        //Bundle object is use for save all data in bundle and send
        Bundle bundle=new Bundle();
        bundle.putParcelable("super",super.onSaveInstanceState());
        bundle.putInt("date",calendar.get(Calendar.DATE));
        bundle.putInt("month",calendar.get(Calendar.MONTH));
        bundle.putInt("year",calendar.get(Calendar.YEAR));
        return bundle;
    }

    @Override
    //this method use for restore data after rotate
    protected void onRestoreInstanceState(Parcelable state) {
        if(state instanceof Parcelable){
            Bundle bundle=(Bundle) state;
            state=bundle.getParcelable("super");
            int date=bundle.getInt("date");
            int month=bundle.getInt("month");
            int year=bundle.getInt("year");
            update(date,month,year,0,0,0);
        }
        super.onRestoreInstanceState(state);
    }
}
