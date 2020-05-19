package cxd.com.programlearning.widgets.spanny;

import android.content.Context;
import android.text.TextPaint;
import android.text.style.ClickableSpan;
import android.view.View;


public class MyClickableSpan extends ClickableSpan {
    private Context mContext = null;
    private ClickableData mData;
    private boolean isUnderLine = false;

    private long mLastClickTime = 0;

    private static final String TAG = "MyClickableSpan";

    public MyClickableSpan(Context context, ClickableData data) {
        mContext = context;
        mData = data;
    }

    @Override
    public void onClick(View widget) {

        if (isFastClick()) {
            return;
        }

        if (null != mData && mData.mListener != null) {
            mData.mListener.onClick(widget);
        }

    }

    @Override
    public void updateDrawState(TextPaint ds) {
//		super.updateDrawState(ds);
        if (null != mData)
            ds.setColor(mData.mColor);
        if (isUnderLine) {
            ds.setUnderlineText(true);
        } else {
            ds.setUnderlineText(false);
        }

    }

    public static class ClickableData {
        private String mText;
        private int mColor;
        private View.OnClickListener mListener;

        public ClickableData(String text, int color, View.OnClickListener l) {
            mText = text;
            mColor = color;
            mListener = l;
        }
    }

    private boolean isFastClick() {
        long currentTime = System.currentTimeMillis();
        if (currentTime - mLastClickTime <= 1000) {
            return true;
        }
        mLastClickTime = currentTime;
        return false;
    }

    public void setIsUnderLine(boolean isUnderLine) {
        this.isUnderLine = isUnderLine;
    }
}
