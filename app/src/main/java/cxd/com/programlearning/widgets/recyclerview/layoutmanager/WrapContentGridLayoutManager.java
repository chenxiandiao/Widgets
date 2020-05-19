package cxd.com.programlearning.widgets.recyclerview.layoutmanager;

import android.content.Context;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;


/**
 * Created by chenxiandiao on 16/11/30.
 */

public class WrapContentGridLayoutManager extends GridLayoutManager {
    private int mMaxHeight;

    public WrapContentGridLayoutManager(Context context, int spanCount, int maxHeight) {
        super(context, spanCount);
        mMaxHeight = maxHeight;
        setAutoMeasureEnabled(false);
    }

    public WrapContentGridLayoutManager(Context context, int spanCount, int orientation, boolean reverseLayout, int maxHeight) {
        super(context, spanCount, orientation, reverseLayout);
        mMaxHeight = maxHeight;
        setAutoMeasureEnabled(false);
    }

    @Override
    public boolean supportsPredictiveItemAnimations() {
        return false;
    }

    @Override
    public void onMeasure(RecyclerView.Recycler recycler, RecyclerView.State state, int widthSpec, int heightSpec) {
        if (state.isPreLayout()) {
            super.onMeasure(recycler, state, widthSpec, heightSpec);
            return;
        }
        int height = 0;
        int childCount = getItemCount();
        for (int i = 0; i < childCount; i++) {
            try {
                View child = recycler.getViewForPosition(i);
                measureChild(child, widthSpec, heightSpec);
                if (i % getSpanCount() == 0) {
                    int measuredHeight = child.getMeasuredHeight() + getBottomDecorationHeight(child);
                    height += measuredHeight;
                }
            } catch (IndexOutOfBoundsException e) {
                e.printStackTrace();
                continue;
            }
        }

        if (mMaxHeight > 0 && height > mMaxHeight) {
            setMeasuredDimension(View.MeasureSpec.getSize(widthSpec), mMaxHeight);
        } else {
            setMeasuredDimension(View.MeasureSpec.getSize(widthSpec), height);
        }
    }

    @Override
    public void onLayoutChildren(RecyclerView.Recycler recycler, RecyclerView.State state) {
        try {
            super.onLayoutChildren(recycler, state);
        } catch (IndexOutOfBoundsException e) {
            e.printStackTrace();
        }
    }
}
