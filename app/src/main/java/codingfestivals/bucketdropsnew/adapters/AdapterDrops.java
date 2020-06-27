package codingfestivals.bucketdropsnew.adapters;


import android.content.Context;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import codingfestivals.bucketdropsnew.BucketDropsApplication;
import codingfestivals.bucketdropsnew.R;
import codingfestivals.bucketdropsnew.beans.Drop;
import codingfestivals.bucketdropsnew.extras.Util;
import codingfestivals.bucketdropsnew.widgets.BucketPickerView;
import io.realm.Realm;
import io.realm.RealmResults;

public class AdapterDrops extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements SwipeListener{
    public static final int ITEM =0;
    public static final int NO_ITEM=1;
    public static final int FOOTER=2;
    private final ResetListener resetListener;
    Context context;
    RealmResults<Drop> arrayList;
    AddListener addListener;
    //MarkListener for showing dialog_mark.xml(Dialog)
    MarkListener markListener;
    //this object create for swipping work,for delete data we need to begin and commit transaction by realm object
    Realm realm;
    //filterOption variable store which item click prevously in menu
    private int filterOption;
    /* public AdapterDrops(Context context,RealmResults<Drop> results) {
            this.context = context;
            update(results);
        }*/
    public AdapterDrops(Context context, Realm rrealm, RealmResults<Drop> results, AddListener listener, MarkListener markListener, ResetListener resetListener) {
        this.context = context;
        //1st this is addListener assign from MainActivity
        addListener=listener;
        //realm object initialized,for swipping work
        realm=rrealm;
        this.markListener=markListener;
        this.resetListener=resetListener;
        update(results);
    }
    public void update(RealmResults<Drop> results){
        arrayList=results;
        //retrive filter option,by this filteroption data retrive from database and show in recycleview
        filterOption= BucketDropsApplication.load(context);
        notifyDataSetChanged();
    }

    @Override
    public int getItemViewType(int position) {

        if(!arrayList.isEmpty()){
            if(position<arrayList.size()){
                return ITEM;
            }else{
                return FOOTER;
            }
        }else{
            if(filterOption==Filter.COMPLETE||filterOption==Filter.INCOMPLETE){
                if(position==0){
                    return NO_ITEM;
                }else{
                    return FOOTER;
                }
            }else{
                return ITEM;
            }
        }
    }

    @NonNull
    @Override
    //1 onCreateView will be called first time for every item
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        LayoutInflater inflater= (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if(viewType==FOOTER){
            View row=inflater.inflate(R.layout.footer,viewGroup,false);
            FooterHolder holder=new FooterHolder(row);
            return holder;
        }
        else if(viewType==NO_ITEM){
            View view=inflater.inflate(R.layout.no_item,viewGroup,false);
            NoItemHolder holder=new NoItemHolder(view);
            return holder;
        }
        else{
            View row=inflater.inflate(R.layout.row_drop,viewGroup,false);
            DropHolder holder=new DropHolder(row);
            return holder;
        }

    }

    //put the value in single view
    //this will be called when view is show in activity
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int i) {
        if(holder instanceof DropHolder){
            DropHolder dropHolder= (DropHolder) holder;
            Drop drop=arrayList.get(i);
            //set animation in image view
            //dropHolder.imageView.setAnimation(AnimationUtils.loadAnimation(context,R.anim.fade_transaction_animation));
            dropHolder.textView_what.setText(drop.getWhat());
            //here we create a method because we dont't want to write more code in onBindViewHolder,otherwise you can write all code here
            dropHolder.setBackground(drop.isCompleted());
            //show the difference between present day and future day
                //dropHolder.textView_when.setText(drop.getWhen()+"");
            //here 4 parameter used 1->future date 2->present date 3->total date show in days(today/tomorrow/...) 4->now show date in sort form(December to DEC)
            //you can show remainning date as your own choice see the link given in paint folder
            dropHolder.textView_when.setText(DateUtils.getRelativeTimeSpanString(drop.getWhen(),System.currentTimeMillis(),DateUtils.DAY_IN_MILLIS, DateUtils.FORMAT_ABBREV_ALL));
        }
    }

    //getItemId(position) method use for return a id for each item in recycle view,by that id recycle view can allow to show animantion
    //in recycliview for item delete or remove
    @Override
    public long getItemId(int position) {
        if (position<arrayList.size()){
            return arrayList.get(position).getAdded();
        }
        return RecyclerView.NO_ID;
    }

    //how many data you want to show
    //specially all data store in arraylist
    //so size of array list size is used
    @Override
    public int getItemCount() {
       //if arraylist not empty that means we have  items to show in screen
       if(!arrayList.isEmpty()){
           //here we add extra 1 for showing item with footer
           //add,extra 1 for showing footer(footer means,Add a drop button)
           return arrayList.size()+1;
       }
       //else, means we have no item to show in recycleview
       //now we check which menu item is clicked from menu
       //if MOST_TIME_LEFT or LEAST_TIME_LEFT click and no item have than we show empty view,so we return 0
       //if complete or incomplete click and no item have than we don't show empty view,we show no_item.xml,so we return 1+1
       //now we need to retrive which item click from shared preference ,because sharedPreference stored which menu item is clicked
       //we retrive which itme click  by load() method
       else{
           //if MOST_TIME_LEFT or LEAST_TIME_LEFT click and no item have than we show empty view,so we return 0
           if(filterOption==Filter.NONE||filterOption==Filter.MOST_TIME_LEFT||filterOption==Filter.LEAST_TIME_LEFT){
               return 0;
           }else{
               //if complete or incomplete click and no item have than we don't show empty view,we show no_item.xml,so we return 1+1
                //here we return 1+1
               //1st 1 for showing no_item.xml
               //2nd 1 from showing fotter.xml
               //in tutorial  1 represent by COUNT_NO_ITME,another 1  represented by COUNT_FOOTER,so return COUNT_NO_ITEM+COUNT_FOOTER
                return 1+1;
           }
       }
    }

    //in this method we delete a item from adapter
    @Override
    public void onSwipe(final int position) {
        Log.d("UTSAB","on Swipe Called In Adapter Drop");
        //this condition for we have a footer in recycle view,if no footer in recycle view than it is no need
        if(position<arrayList.size()) {
            realm.executeTransaction(new Realm.Transaction() {
                @Override
                public void execute(Realm realm) {
                    arrayList.get(position).deleteFromRealm();
                }
            });
            notifyDataSetChanged();
        }
        resetFilterIfEmpty();
    }
    private void resetFilterIfEmpty(){
        if(arrayList.isEmpty()&&(filterOption==Filter.COMPLETE||filterOption==Filter.INCOMPLETE)){
            resetListener.onReset();
        }
    }
    public void markComplete(int position) {
        if(position<arrayList.size()){
            realm.beginTransaction();
            arrayList.get(position).setCompleted(true);
            realm.commitTransaction();
            notifyItemChanged(position);
            //notifyDataSetChanged();
        }
    }

    //2 find the all view(TextView,ImageView) in single_item of recyle view
    public class DropHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView textView_what,textView_when;
        ImageView imageView;
        Context contextInDropHolder;
        View itemView;
        public DropHolder(@NonNull View itemView) {
            super(itemView);
            this.itemView=itemView;
            //when an item is clicked in recycleview than onClicklistener called
            itemView.setOnClickListener(this);
            contextInDropHolder=itemView.getContext();
            textView_what=itemView.findViewById(R.id.id_tv_what);
            textView_when=itemView.findViewById(R.id.id_tv_when);
            /*Typeface typeface=Typeface.createFromAsset(context.getAssets(),"fonts/raleway_thin.ttf");
            textView_what.setTypeface(typeface);
            textView_when.setTypeface(typeface);*/
            BucketDropsApplication.setRalewayRegular(context,textView_what);
            BucketDropsApplication.setRalewayRegular(context,textView_when);
            imageView=itemView.findViewById(R.id.id_iv_drop);
        }

        //here we pass the position in MarkListener interface which is implemented in MainActivity
        // in that interface we show the dialog(dialog_mark.xml)
        public void onClick(View v) {
            markListener.onMark(getAdapterPosition());
        }

        public void setBackground(boolean completed) {
            Drawable drawable;
            if(completed){
                drawable=ContextCompat.getDrawable(contextInDropHolder,R.color.bg_drop_complete);
            }else{
                drawable=ContextCompat.getDrawable(contextInDropHolder,R.drawable.bg_row_drop);
            }
            /*if(Build.VERSION.SDK_INT>15){
                itemView.setBackground(drawable);
            }else {
                itemView.setBackgroundDrawable(drawable);
            }*/
            Util.setBackground(itemView,drawable);
        }
    }
    //find the all view(TextView,ImageView) in single_item of recyle view
    public class FooterHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        Button button;
        public FooterHolder(@NonNull View itemView) {
            super(itemView);
            button=itemView.findViewById(R.id.btn_footer);
            button.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            //when footer add drop button click then DatePicker(dialog_add.xml) will be shown
            //2nd when button clicked,this method(add) is called which are overide in MainActivity
            addListener.add();
        }
    }
    public class NoItemHolder extends RecyclerView.ViewHolder{

        public NoItemHolder(@NonNull View itemView) {
            super(itemView);
        }
    }
}
