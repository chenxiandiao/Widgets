package cxd.com.programlearning.widgets.recyclerviewfilter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import cxd.com.programlearning.R;
import cxd.com.programlearning.utils.AppUtils;
import cxd.com.programlearning.utils.Utils;
import cxd.com.programlearning.widgets.spanny.Spanny;

/**
 * Created by chenxiandiao on 17/8/30.
 */

public class FilterMenu extends LinearLayout implements OnFilterItemChoose {
    private String mLastSelectedId = "";
    private List<List<FilterItem>> mListData = new ArrayList<>();
    private Context mContext;
    private TextView mTvFilterContent;
    private FilterPopupWindow mPopupWnd;
    private OnChangeFilterListener mListener;
    private Drawable mUpArrowDrawable;
    private Drawable mDownArrowDrawable;
    private int mAnchorY;

    public interface OnChangeFilterListener {
        void onChangeFilter();
    }

    public void setOnChangeFilterListener(OnChangeFilterListener mListener) {
        this.mListener = mListener;
    }

    public FilterMenu(Context context) {
        this(context, null);
    }

    public FilterMenu(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        mUpArrowDrawable = ContextCompat.getDrawable(mContext, R.drawable.icon_arrow_up_gray);
        mUpArrowDrawable.setBounds(0, 0, mUpArrowDrawable.getIntrinsicWidth(), mUpArrowDrawable.getIntrinsicHeight());
        mDownArrowDrawable = ContextCompat.getDrawable(mContext, R.drawable.icon_arrow_down_gray);
        mDownArrowDrawable.setBounds(0, 0, mDownArrowDrawable.getIntrinsicWidth(), mDownArrowDrawable.getIntrinsicHeight());
        initUI();
    }

    public void initUI() {
        View v = LayoutInflater.from(mContext).inflate(R.layout.tree_view_filter_menu, this, true);
        final View line = v.findViewById(R.id.v_line);
        initPopWnd();
        mTvFilterContent = (TextView) v.findViewById(R.id.tv_filter_content);
        mTvFilterContent.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mPopupWnd != null) {
                    if (mPopupWnd.isShowing()) {
                        mPopupWnd.dismiss();
                    } else {
                        if (mAnchorY == 0) {
                            int[] loc = new int[2];
                            line.getLocationOnScreen(loc);
                            mAnchorY = loc[1];
                        }
                        mTvFilterContent.setCompoundDrawables(null, null, mUpArrowDrawable, null);
                        mPopupWnd.setHeight(AppUtils.getScreenSize(mContext).y - mAnchorY - 1);
                        mPopupWnd.showAtLocation(line, Gravity.NO_GRAVITY, 0, mAnchorY + 1);
                    }
                }
            }
        });
    }


    private void initPopWnd() {
        if (mPopupWnd == null) {
            mPopupWnd = new FilterPopupWindow(mContext);
            mPopupWnd.setOnChooseListener(this);
            mPopupWnd.setOnDismissListener(new PopupWindow.OnDismissListener() {
                @Override
                public void onDismiss() {
                    mTvFilterContent.setCompoundDrawables(null, null, mDownArrowDrawable, null);
                    String selectedId = getSelectedId();
                    //筛选条件改变了
                    if (!mLastSelectedId.equals(selectedId)) {
                        mLastSelectedId = selectedId;
                        if (mListener != null) {
                            mListener.onChangeFilter();
                        }
                    }
                }
            });
        }
    }


    public void setData(List<List<FilterItem>> listData) {
        if (!Utils.isEmpty(listData) && mPopupWnd != null) {
            if (mListData.isEmpty()) {
                mListData.addAll(listData);
                mLastSelectedId = getSelectedId();
                setFilterTitle();
                mPopupWnd.setData(listData);
            }
        } else {
            this.setVisibility(View.GONE);
        }
    }


    private String getSelectedId() {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < mListData.size(); i++) {
            List<FilterItem> items = mListData.get(i);
            for (int j = 0; j < items.size(); j++) {
                FilterItem item = items.get(j);
                if (item.mSelected) {
                    builder.append("_").append(item.mFilterId);
                    break;
                }
            }
        }
        return builder.toString();
    }


    @Override
    public void onChoose() {
        setFilterTitle();
    }

    private void setFilterTitle() {
        int appendIndex = 0;
        Spanny spanny = new Spanny();
        boolean hasFilter = false;
        for (int i = 0; i < mListData.size(); i++) {
            List<FilterItem> data = mListData.get(i);
            for (int j = 0; j < data.size(); j++) {
                if (data.get(j).mSelected) {
                    //全部文字不显示
                    if (data.get(j).mFilterId != 0) {
                        hasFilter = true;
                        if (appendIndex != 0) {
                            spanny.append("  ");
                            spanny.append(mContext, R.drawable.icon_black_dot);
                            spanny.append("  ");
                        }
                        spanny.append(data.get(j).mFilterName);
                        appendIndex++;
                    }
                    break;
                }
            }
        }
        if (hasFilter) {
            mTvFilterContent.setText(spanny);
        } else {
            mTvFilterContent.setText(mContext.getString(R.string.str_all));
        }

    }


    public boolean closePopWnd() {
        if (mPopupWnd.isShowing()) {
            mPopupWnd.dismiss();
            return true;
        } else {
            return false;
        }
    }
}