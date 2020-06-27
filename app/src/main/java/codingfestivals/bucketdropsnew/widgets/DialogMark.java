package codingfestivals.bucketdropsnew.widgets;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import codingfestivals.bucketdropsnew.BucketDropsApplication;
import codingfestivals.bucketdropsnew.R;
import codingfestivals.bucketdropsnew.adapters.CompleteListener;

public class DialogMark extends DialogFragment {
    private ImageButton btn_close;
    Button btn_completed;
    View.OnClickListener onClickListener=new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            //any button click dialog is dismiss
            switch (v.getId()){
                case R.id.id_btn_complete:
                    markAsComplete();
                    break;
            }
            dismiss();
        }
    };
    private CompleteListener completeListener;

    private void markAsComplete() {
        //recive data send from Adapter drop
        Bundle arguments=getArguments();
        if(arguments!=null && completeListener!=null) {
            //get the position
            int pos= (int) arguments.get("POSITION");
            completeListener.onComplete(pos);
        }
    }

    public DialogMark() {
    }
    //apply theme
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL,R.style.DialogTheme);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.dialog_mark,container,false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        btn_close=view.findViewById(R.id.id_btn_close);
        btn_completed=view.findViewById(R.id.id_btn_complete);
        btn_close.setOnClickListener(onClickListener);
        btn_completed.setOnClickListener(onClickListener);
        //set cutom font btn text
        BucketDropsApplication.setRalewayRegular(getActivity(),btn_completed);
    }

    public void setCompleteListener(CompleteListener completeListener) {
        this.completeListener=completeListener;
    }
}
