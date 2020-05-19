package cxd.com.programlearning.widgets.banner;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import cxd.com.programlearning.R;
import cxd.com.programlearning.utils.AppUtils;
import cxd.com.programlearning.utils.Utils;


public class PostView extends RelativeLayout {
    private static final String TAG = "PostView";


    public static final float POSTER_REDIO = 2.5f;

    protected Context mContext;

    protected PostAdapter mAdAdapter = null;
    protected ArrayList<ListItem> mAdList = new ArrayList<>();

    private View mContextView = null;
    private OnTouchListener mGalleryTouchListener = null;
    private OnBannerClick mClickListener = null;

    private AlphaAnimation switchAnimation;
    private int mPrePosition = -1;
    private int mCurPos = 0;

    private boolean mShowTitleFlag = false;

    private LinearLayout mRadioGroup;
    private CirclePager mCirclePage;
    private TextView mTvAdTitle;

    public void release() {
        if (mContextView != null) {
            mCirclePage.setAdapter(null);
            mRadioGroup.removeAllViews();
            mTvAdTitle = null;
            mContextView = null;
        }

        if (null != mAdList) {
            mAdList.clear();
            mAdList = null;
        }
        switchAnimation = null;
        mContext = null;
    }

    public interface OnBannerClick {
        void onClick(View v, int position, Object obj);
    }

    public PostView(Context context) {
        this(context, null);
    }

    public PostView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        switchAnimation = new AlphaAnimation(0.1f, 1.0f);
        switchAnimation.setDuration(1000);
        mAdList = new ArrayList<>();

        mContextView = LayoutInflater.from(mContext).inflate(R.layout.view_widget_banner, this, true);
        mCirclePage = (CirclePager) mContextView.findViewById(R.id.circlePager);
        mCirclePage.setAnimation(switchAnimation);
        if (null != mGalleryTouchListener) {
            mCirclePage.setOnTouchListener(mGalleryTouchListener);
        }
        mRadioGroup = (LinearLayout) mContextView.findViewById(R.id.ad_radio);
        mTvAdTitle = (TextView) mContextView.findViewById(R.id.tv_ad_title);
    }

    public void setShowData(List<ListItem> items,
                            OnTouchListener listener, OnBannerClick l) {
        if (null == items || items.size() == 0) {
            return;
        }
        mGalleryTouchListener = listener;
        mClickListener = l;

        mAdList.clear();
        mAdList.addAll(items);
        mAdAdapter = null;
        mPrePosition = -1;
        showAd();
    }


    private void showAd() {
        if (mContext == null) {
            return;
        }

        if (mContextView != null) {
            if (mShowTitleFlag) {
                mTvAdTitle.setVisibility(View.VISIBLE);
            } else {
                mTvAdTitle.setVisibility(View.GONE);
            }

            mRadioGroup.removeAllViews();
            for (int i = 0; i < mAdList.size(); i++) {
                ImageView rb = new ImageView(this.mContext);
                rb.setBackgroundResource(R.drawable.ad_radio_unmark);
                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                        RadioGroup.LayoutParams.WRAP_CONTENT,
                        RadioGroup.LayoutParams.WRAP_CONTENT);
                layoutParams.setMargins((int) AppUtils.getRawSize(TypedValue.COMPLEX_UNIT_DIP, 2, mContext), 0,
                        (int) AppUtils.getRawSize(TypedValue.COMPLEX_UNIT_DIP, 2, mContext), 0);
                rb.setClickable(false);
                mRadioGroup.addView(rb, i, layoutParams);
            }

            mAdAdapter = new PostAdapter(mContext, mAdList);
            mAdAdapter.setClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (null != mClickListener) {
                        mClickListener.onClick(v, mCurPos, mAdList.get(mCurPos));
                    }
                }
            });
            mCirclePage.setAdapter(mAdAdapter);
            mCirclePage.setAutoPlay(true);
            mCirclePage.setOnPageChangeListener(new CirclePager.OnPageChangeListener() {
                @Override
                public void onPageSelected(int position) {
                    onFocusAd(position);
                }
            });
        }
    }

    public void onFocusAd(int position) {
        if (mContextView == null || Utils.isEmpty(mAdList))
            return;
        mCurPos = position;
        if (mAdList.size() > mCurPos && mAdList.get(mCurPos) != null) {
            ListItem item = mAdList.get(mCurPos);
            if (item != null) {
                if (TextUtils.isEmpty(item.mName)) {
                    mTvAdTitle.setText("");
                } else {
                    mTvAdTitle.setText(item.mName);
                }
            } else {
                mTvAdTitle.setText("");
            }
        }
        if (mPrePosition == -1) {
            (mRadioGroup.getChildAt(mCurPos)).setBackgroundResource(R.drawable.ad_radio_mark);
            mPrePosition = 0;
            return;
        }
        if (mCurPos != mPrePosition) {
            if (mRadioGroup != null) {
                (mRadioGroup.getChildAt(mPrePosition)).setBackgroundResource(R.drawable.ad_radio_unmark);
                (mRadioGroup.getChildAt(mCurPos)).setBackgroundResource(R.drawable.ad_radio_mark);
                mPrePosition = mCurPos;
            }
        }
    }


    public void setTitleShow(boolean show) {
        mShowTitleFlag = show;
    }

}
