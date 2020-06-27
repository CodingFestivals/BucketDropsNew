package codingfestivals.bucketdropsnew.adapters;

import android.graphics.Canvas;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;

public class SimpleTouchCallBack extends ItemTouchHelper.Callback {
    SwipeListener swipeListener;

    public SimpleTouchCallBack(SwipeListener swipeListener) {
        this.swipeListener = swipeListener;
    }

    public int getMovementFlags(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder) {
        return makeMovementFlags(0,ItemTouchHelper.END);
    }

    //here we enable false drag
    @Override
    public boolean isLongPressDragEnabled() {
        return false;
    }

    //here we enable true
    @Override
    public boolean isItemViewSwipeEnabled() {
        return true;
    }

    //here we return false because we not drag we swipe
    @Override
    public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder viewHolder1) {
        return false;
    }

    @Override
    public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
        //here we can swap an item only when item is single item(that means we can't swap Footer.xml item and No_item.xml item)
        if(viewHolder instanceof AdapterDrops.DropHolder){
            super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
        }
    }

    @Override
    public void onChildDrawOver(@NonNull Canvas c, @NonNull RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
        //here we can swap an item only when item is single item(that means we can't swap Footer.xml item and No_item.xml item)
        if(viewHolder instanceof AdapterDrops.DropHolder){
            super.onChildDrawOver(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
        }
    }

    //in onswiped method we communicate with adapter,sothat when we swipe item than item will be deleted
    @Override
    public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
        //Log.d("UTSAB","Swiped From SimpleTouch Callback"+viewHolder.getLayoutPosition());
        //here we can swap an item only when item is single item(that means we can't swap Footer.xml item and No_item.xml item)
        if(viewHolder instanceof  AdapterDrops.DropHolder){
            swipeListener.onSwipe(viewHolder.getLayoutPosition());
        }
    }
}
