package cxd.com.programlearning.widgets.flowlayout;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import cxd.com.programlearning.R;
import cxd.com.programlearning.widgets.fresco.FrescoThumbnailView;
import cxd.com.programlearning.widgets.fresco.Resize;

/**
 * Created by chenxiandiao on 17/9/1.
 */

public class CropViewGroup extends ViewGroup {

    private DisplayMetrics dm;
    private int mInVisibleIndex = Integer.MAX_VALUE;
    private Context mContext;
    private int mHorizontalSpacing = 10;
    private int mRightSpaceLeft = 25;


    public CropViewGroup(Context context) {
        this(context, null);
    }

    public CropViewGroup(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        dm = getResources().getDisplayMetrics();
        mHorizontalSpacing = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, mHorizontalSpacing, dm);
        mRightSpaceLeft = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, mRightSpaceLeft, dm);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.CropViewGroup);
        try {
            mHorizontalSpacing = a.getDimensionPixelSize(
                    R.styleable.CropViewGroup_horizontal_divder, mHorizontalSpacing);
            mRightSpaceLeft = a.getDimensionPixelOffset(R.styleable.CropViewGroup_right_space_left, mRightSpaceLeft);
        } finally {
            a.recycle();
        }
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int myWidth = resolveSize(0, widthMeasureSpec) - mRightSpaceLeft;
        int myCalcWidth = 0;
        int myCalcHeight = 0;
        for (int i = 0, childCount = getChildCount(); i < childCount; ++i) {
            View child = getChildAt(i);
            if (child.getVisibility() != View.GONE) {
                measureChild(child, widthMeasureSpec, heightMeasureSpec);
            } else {
                continue;
            }

            int childWidth = child.getMeasuredWidth();
            int childHeight = child.getMeasuredHeight();
            myCalcHeight = childHeight;
            myCalcWidth = myCalcWidth + childWidth + mHorizontalSpacing;
            if (myCalcWidth > myWidth) {
                mInVisibleIndex = i;
                myCalcWidth = myCalcWidth - childWidth - mHorizontalSpacing;
                break;
            }
        }
        setMeasuredDimension(myCalcWidth, myCalcHeight);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        int childLeft = 0;
        int childTop = 0;
        for (int i = 0, childCount = getChildCount(); i < childCount; ++i) {
            View childView = getChildAt(i);
            if (i >= mInVisibleIndex) {
                childView.setVisibility(View.GONE);
            } else {
                childView.setVisibility(View.VISIBLE);
            }

            if (childView.getVisibility() == View.GONE) {
                continue;
            }

            int childWidth = childView.getMeasuredWidth();
            int childHeight = childView.getMeasuredHeight();

            childView.layout(childLeft, childTop, childLeft + childWidth, childTop + childHeight);
            childLeft += childWidth + mHorizontalSpacing;
        }
    }

    public void setData(List<SimpleUser> userList) {
        removeAllViews();
        setVisibility(View.VISIBLE);
        for (int i = 0; i < userList.size(); i++) {
            View v = LayoutInflater.from(mContext).inflate(R.layout.item_room_member_icon, this, false);
            FrescoThumbnailView image = (FrescoThumbnailView) v.findViewById(R.id.iv_image);
            image.loadViewIfNecessary(userList.get(i).mAvatar, R.drawable.default_user_icon, Resize.avatar.small, Resize.avatar.small);
            addView(v);
        }
        if (userList.size() <= 0) {
            setVisibility(View.GONE);
        }
    }
}
