package codingfestivals.bucketdropsnew;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.text.Editable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import java.util.Calendar;

import codingfestivals.bucketdropsnew.beans.Drop;
import codingfestivals.bucketdropsnew.widgets.BucketPickerView;
import io.realm.Realm;
public class DialogAdd extends DialogFragment {
    ImageButton btn_close;
    EditText et_inputWhat;
    Button btn_add;
    //dp(date picker)
    BucketPickerView dp_inputWhen;
    View.OnClickListener btn_clickListener=new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            int id=v.getId();
            switch (id){
                case R.id.id_btin_close:
                    dismiss();
                    break;
                case R.id.id_btn_addIt:
                    addAction();
                    break;
            }
            dismiss();
        }
    };
    //TODO proceess date
    private void addAction() {
        //inputWhat edittext for plan
        String what=et_inputWhat.getText().toString();
        //today date
        long now=System.currentTimeMillis();
        // Initialize Realm (just once per application)
        //Realm.init(getActivity());
        // Get a Realm instance for this thread
        //Realm.setDefaultConfiguration(new RealmConfiguration.Builder().build());
        Realm realm=Realm.getDefaultInstance();
        // Persist your data in a transaction
        //here getTimeMillis() use for total year,month,day converted into milisecond
        Drop drop=new Drop(what,now,dp_inputWhen.getTime(),false);
        realm.beginTransaction();
        realm.copyToRealm(drop);
        realm.commitTransaction();
        realm.close();
        //Toast.makeText(getActivity(),"Ok",Toast.LENGTH_SHORT).show();
    }

    public DialogAdd(){}

    //apply theme on Add dialog
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL,R.style.DialogTheme);
    }

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.dialog_add,container,false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        btn_close=view.findViewById(R.id.id_btin_close);
        et_inputWhat=view.findViewById(R.id.id_et_drop);
        dp_inputWhen=view.findViewById(R.id.id_bpv_date);
        btn_add=view.findViewById(R.id.id_btn_addIt);
        btn_close.setOnClickListener(btn_clickListener);
        btn_add.setOnClickListener(btn_clickListener);
        //set custom font for every texted view
        BucketDropsApplication.setRalewayRegular(getActivity(),et_inputWhat,btn_add);
    }
}
