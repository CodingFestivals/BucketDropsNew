package codingfestivals.bucketdropsnew.widgets;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.AttributeSet;
import android.view.View;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import codingfestivals.bucketdropsnew.extras.Util;

public class BucketRecyclerView extends RecyclerView {
    private List<View> nonEmptyViews= Collections.emptyList();
    private List<View> emptyViews= Collections.emptyList();
    private AdapterDataObserver observer=new AdapterDataObserver() {
        @Override
        public void onChanged() {
            tooggleViews();
        }

        @Override
        public void onItemRangeChanged(int positionStart, int itemCount) {
            tooggleViews();
        }

        @Override
        public void onItemRangeChanged(int positionStart, int itemCount, @Nullable Object payload) {
            tooggleViews();
        }

        @Override
        public void onItemRangeInserted(int positionStart, int itemCount) {
            tooggleViews();
        }

        @Override
        public void onItemRangeRemoved(int positionStart, int itemCount) {
            tooggleViews();
        }

        @Override
        public void onItemRangeMoved(int fromPosition, int toPosition, int itemCount) {
            tooggleViews();
        }
    };

    private void tooggleViews() {
        if(getAdapter()!=null && !emptyViews.isEmpty() && !nonEmptyViews.isEmpty()){
            if(getAdapter().getItemCount()==0){
                //show all empty views
                Util.showViews(emptyViews);
                //hide the recycler view
                setVisibility(View.GONE);
                //hide all views which are meant to be hidden
                Util.hideViews(nonEmptyViews);
            }else{
                //show all empty views
                Util.showViews(nonEmptyViews);
                //hide the recycler view
                setVisibility(View.VISIBLE);
                //hide all views which are meant to be hidden
                Util.hideViews(emptyViews);
            }
        }
    }

    public BucketRecyclerView(@NonNull Context context) {
        super(context);
    }

    public BucketRecyclerView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public BucketRecyclerView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public void setAdapter(@Nullable Adapter adapter) {
        super.setAdapter(adapter);
        if (adapter != null) {
            adapter.registerAdapterDataObserver(observer);
        }
        observer.onChanged();
    }

    public void hideIfEmpty(View... views) {
        nonEmptyViews= Arrays.asList(views);
    }

    public void showIfEmpty(View... empViews) {
       emptyViews=Arrays.asList(empViews);
    }
}
