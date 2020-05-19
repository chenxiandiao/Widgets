package cxd.com.programlearning.widgets.recyclerviewfilter;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.PopupWindow;

import java.util.ArrayList;
import java.util.List;

import cxd.com.programlearning.R;

/**
 * Created by chenxiandiao on 17/8/30.
 */

public class FilterPopupWindow extends PopupWindow implements OnFilterItemChoose {


    private Context mContext;
    private List<List<FilterItem>> mListData = new ArrayList<>();
    private LinearLayout mLLFilter;
    private OnFilterItemChoose mOnChooseListener;


    public void setOnChooseListener(OnFilterItemChoose onChooseListener) {
        this.mOnChooseListener = onChooseListener;
    }

    public FilterPopupWindow(Context context) {
        this(context, null, 0);
    }

    public FilterPopupWindow(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public FilterPopupWindow(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        View mContentView = LayoutInflater.from(context).inflate(R.layout.tree_view_filter_pop_window, null);
        //设置布局
        setContentView(mContentView);
        setWidth(LinearLayout.LayoutParams.MATCH_PARENT);
        setHeight(LinearLayout.LayoutParams.MATCH_PARENT);
        setAnimationStyle(R.style.filter_pop_wnd_style);
//        setOutsideTouchable(false);
//        setBackgroundDrawable(new BitmapDrawable());
        initUI();
    }

    private void initUI() {
        mLLFilter = (LinearLayout) getContentView().findViewById(R.id.ll_filter);
        getContentView().findViewById(R.id.v_popwin_bottom).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }

    /**
     * 初始化界面
     * @param listData
     */
    public void setData(List<List<FilterItem>> listData) {
        if (mListData.isEmpty()) {
            mListData.addAll(listData);
            for (int i = 0; i < listData.size(); i++) {
                initRecyclerView(listData.get(i));
            }
        }
    }

    private void initRecyclerView(List<FilterItem> data) {
        FilterRecyclerViewItem recyclerViewItem = new FilterRecyclerViewItem(mContext);
        mLLFilter.addView(recyclerViewItem);
        recyclerViewItem.setData(data);
        recyclerViewItem.setOnChooseListener(this);
    }

    @Override
    public void showAtLocation(View parent, int gravity, int x, int y) {
//        Animation mInAnimation = AnimationUtils.loadAnimation(mContext, R.anim.zues_filter_slide_in_top_anim);
//        mLLFilter.startAnimation(mInAnimation);
        super.showAtLocation(parent, gravity, x, y);
    }

    @Override
    public void onChoose() {
        if (mOnChooseListener != null) {
            mOnChooseListener.onChoose();
        }
    }
}
