package codingfestivals.bucketdropsnew.adapters;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Adapter;
import android.widget.LinearLayout;
import android.widget.Toast;

import codingfestivals.bucketdropsnew.R;

public class Divider extends RecyclerView.ItemDecoration {
    private Drawable divider;
    private int orientation;
    public Divider(Context context,int orien){
        divider=context.getDrawable(R.drawable.divider);
        if (orien!= LinearLayout.VERTICAL){
            throw new IllegalArgumentException("This Item Decoration Can Be Only Used Vertical Orientation");
        }
        orientation=orien;
    }

    @Override
    public void onDraw(@NonNull Canvas c, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
        if(orientation== LinearLayoutManager.VERTICAL){
            drawHorizontalDivider(c,parent,state);
        }
    }

    private void drawHorizontalDivider(Canvas c, RecyclerView parent, RecyclerView.State state) {
        int left,right,bottom,top;
        //left=0;
        left=parent.getPaddingLeft();
        //right=parent.getWidth();
        right=parent.getWidth()-parent.getPaddingRight();
        int count=parent.getChildCount();
        for(int i=0;i<count;i++){
            if(AdapterDrops.FOOTER!=parent.getAdapter().getItemViewType(i)){
                View current=parent.getChildAt(i);
                RecyclerView.LayoutParams params= (RecyclerView.LayoutParams) current.getLayoutParams();
                //top=current.getTop();
                top=current.getTop()-params.topMargin;
                //top=current.getBottom()+params.bottomMargin;
                bottom=top+divider.getIntrinsicHeight();
                divider.setBounds(left,top,right,bottom);
                divider.draw(c);
                //Log.d("UTSAB","Divider "+left +top +right+bottom);
            }
        }
    }

    @Override
    public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
        if(orientation==LinearLayoutManager.VERTICAL){
            outRect.set(0,0,0,divider.getIntrinsicHeight());
        }
    }
}
