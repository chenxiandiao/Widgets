package cxd.com.programlearning.widgets.recyclerview.layoutmanager;

import android.content.Context;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;


/**
 * Created by chenxiandiao on 16/11/30.
 */

public class FixedContentGridLayoutManager extends LinearLayoutManager {
    private int fixedHeight;

    public FixedContentGridLayoutManager(Context context, int fixedHeight) {
        super(context);
        this.fixedHeight = fixedHeight;
        setAutoMeasureEnabled(false);
    }

    public FixedContentGridLayoutManager(Context context, int orientation, boolean reverseLayout, int fixedHeight) {
        super(context, orientation, reverseLayout);
        this.fixedHeight = fixedHeight;
        setAutoMeasureEnabled(false);
    }

    @Override
    public boolean supportsPredictiveItemAnimations() {
        return false;
    }

    @Override
    public void onMeasure(RecyclerView.Recycler recycler, RecyclerView.State state, int widthSpec, int heightSpec) {
        setMeasuredDimension(View.MeasureSpec.getSize(widthSpec), fixedHeight);
    }

    @Override
    public void onLayoutChildren(RecyclerView.Recycler recycler, RecyclerView.State state) {
        try {
            super.onLayoutChildren(recycler, state);
        } catch (IndexOutOfBoundsException e) {
            e.printStackTrace();
        }
    }

    public int getFixedHeight() {
        return fixedHeight;
    }

    public void setFixedHeight(int fixedHeight) {
        this.fixedHeight = fixedHeight;
    }
}
