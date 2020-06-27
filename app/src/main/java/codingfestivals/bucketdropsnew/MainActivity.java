package codingfestivals.bucketdropsnew;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import codingfestivals.bucketdropsnew.adapters.AdapterDrops;
import codingfestivals.bucketdropsnew.adapters.AddListener;
import codingfestivals.bucketdropsnew.adapters.CompleteListener;
import codingfestivals.bucketdropsnew.adapters.Divider;
import codingfestivals.bucketdropsnew.adapters.Filter;
import codingfestivals.bucketdropsnew.adapters.MarkListener;
import codingfestivals.bucketdropsnew.adapters.ResetListener;
import codingfestivals.bucketdropsnew.adapters.SimpleTouchCallBack;
import codingfestivals.bucketdropsnew.beans.Drop;
import codingfestivals.bucketdropsnew.extras.Util;
import codingfestivals.bucketdropsnew.widgets.BucketRecyclerView;
import codingfestivals.bucketdropsnew.widgets.DialogMark;
import io.realm.Realm;
import io.realm.RealmChangeListener;
import io.realm.RealmResults;
import io.realm.Sort;


public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    ImageView imageView;
    Button button;
    Toolbar toolbar;
    BucketRecyclerView recyclerView;
    Realm realm;
    RealmResults<Drop> realmResults;
    AdapterDrops adapterDrops;
    View emptyView;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        imageView = findViewById(R.id.iv_background);
        toolbar = findViewById(R.id.toolbar);
        emptyView = findViewById(R.id.empty_drops);
        setSupportActionBar(toolbar);
        button = findViewById(R.id.btn_add);
        //custom font for button add
        BucketDropsApplication.setRalewayRegular(this, button);
        button.setOnClickListener(this);
        //get Realm Object
        realm = Realm.getDefaultInstance();
        //we get the state(wheich option click in menu),load method retrive which item click from sharedPreference
        //this work done in BucketDropsApplication
        int filterOption = BucketDropsApplication.load(this);
        //Toast.makeText(getApplicationContext(),filterOption+"",Toast.LENGTH_SHORT).show();
        loadResult(filterOption);
        //below query perform by loadResult() method
        // realmResults = realm.where(Drop.class).findAllAsync();
        recyclerView = (BucketRecyclerView) findViewById(R.id.id_rv_drops);
        recyclerView.addItemDecoration(new Divider(this, LinearLayoutManager.VERTICAL));
        recyclerView.hideIfEmpty(toolbar);
        recyclerView.showIfEmpty(emptyView);
        adapterDrops = new AdapterDrops(this, realm, realmResults, addListener, markListener, resetListener);
        //by,setHasStableIds() method we enable the animation in recycleView
        adapterDrops.setHasStableIds(true);
        recyclerView.setAdapter(adapterDrops);
        //for swipe work
        SimpleTouchCallBack simpleTouchCallBack = new SimpleTouchCallBack(adapterDrops);
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleTouchCallBack);
        itemTouchHelper.attachToRecyclerView(recyclerView);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
       //Schedule Alarm
        Util.scheduleAlarm(this);
    }

    //this mehtod for create menu in activity
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    //this method for perform action when an item click
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        boolean handled = true;
        int filterOption = Filter.NONE;
        switch (id) {
            case R.id.id_actoin_add:
                showDialogAdd();
                break;
            case R.id.id_action_asc_date:
                filterOption = Filter.LEAST_TIME_LEFT;
                // save(Filter.LEAST_TIME_LEFT);
                break;
            case R.id.id_action_desc_date:
                filterOption = Filter.MOST_TIME_LEFT;
                //  save(Filter.MOST_TIME_LEFT);
                break;
            case R.id.id_action_show_complete:
                filterOption = Filter.COMPLETE;
                //  save(Filter.COMPLETE);
                break;
            case R.id.id_action_show_incomplete:
                filterOption = Filter.INCOMPLETE;
                //  save(Filter.INCOMPLETE);
                break;
            default:
                handled = false;
                break;
        }
        //now we save which item click in shared preference by save method which are in BucketDropsApplication
        BucketDropsApplication.save(this, filterOption);
        loadResult(filterOption);
        return handled;
    }

    private void loadResult(int filterOption) {
        switch (filterOption) {
            case Filter.NONE:
                realmResults = realm.where(Drop.class).findAllAsync();
                break;
            case Filter.LEAST_TIME_LEFT:
                realmResults = realm.where(Drop.class).sort("when").findAllAsync();
                break;
            case Filter.MOST_TIME_LEFT:
                realmResults = realm.where(Drop.class).sort("when", Sort.DESCENDING).findAllAsync();
                break;
            case Filter.COMPLETE:
                realmResults = realm.where(Drop.class).equalTo("completed", true).findAllAsync();
                break;
            case Filter.INCOMPLETE:
                realmResults = realm.where(Drop.class).equalTo("completed", false).findAllAsync();
                break;
        }
        realmResults.addChangeListener(realmListener);
    }

    //mark listener for when an item click in recycleview then dialog is show but in adapter getSupperFragmentManager()
    //not available for showing dialog so that we show dialog from MainActivity and we do this by interface MarkListener
    //we send this markListener interface in adapter when we create object adapterDrop and send interface as parameter
    MarkListener markListener = new MarkListener() {
        @Override
        public void onMark(int position) {
            DialogMark dialogMark = new DialogMark();
            //here we send the position of recycleview item in DialogMark(dialog_mark.xml)
            //so that we can perfor action when complete button click in DialogMark
            Bundle bundle = new Bundle();
            bundle.putInt("POSITION", position);
            dialogMark.setArguments(bundle);
            dialogMark.setCompleteListener(completeListener);
            dialogMark.show(getSupportFragmentManager(), "Mark Complete");
        }
    };
    RealmChangeListener realmListener = new RealmChangeListener() {
        @Override
        public void onChange(Object o) {
            Log.d("UTSAB", "on Change Called");
            adapterDrops.update(realmResults);
        }
    };
    //here we make AddListener Interface because we want to  show Dialog when button in adapter will be clicked
    //but Adapter can't have method getSupportFragmentManager() so we define or implement AddListener in MainActivity
    //and  interface method add() can access from Adapter
    //we pass AddListener Interface from mainAcitivity when Adapter object create
    AddListener addListener = new AddListener() {
        @Override
        public void add() {
            //3rd(finally) show Dialog after click
            DialogAdd dialogAdd = new DialogAdd();
            dialogAdd.show(getSupportFragmentManager(), "Add");
        }
    };
    CompleteListener completeListener = new CompleteListener() {
        @Override
        public void onComplete(int position) {
            adapterDrops.markComplete(position);
            //Toast.makeText(getApplicationContext(),"Position In Activity "+position,Toast.LENGTH_SHORT).show();
        }
    };

    private ResetListener resetListener = new ResetListener() {
        @Override
        public void onReset() {
            BucketDropsApplication.save(MainActivity.this, Filter.NONE);
            loadResult(Filter.NONE);
        }
    };

    @Override
    protected void onStart() {
        super.onStart();
        realmResults.addChangeListener(realmListener);
    }

    @Override
    protected void onStop() {
        super.onStop();
        realmResults.removeChangeListener(realmListener);
    }


    public void onClick(View v) {
        showDialogAdd();
    }

    private void showDialogAdd() {
        DialogAdd dialogAdd = new DialogAdd();
        dialogAdd.show(getSupportFragmentManager(), "Add");
    }
}
