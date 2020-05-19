package cxd.com.programlearning.widgets.filter;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.PopupWindow;

import java.util.ArrayList;
import java.util.List;

import cxd.com.programlearning.R;
import cxd.com.programlearning.utils.AppUtils;
import cxd.com.programlearning.widgets.recyclerview.adapter.CommonRecyclerViewAdapter;
import cxd.com.programlearning.widgets.recyclerview.adapter.OnItemClickListener;
import cxd.com.programlearning.widgets.recyclerview.layoutmanager.WrapContentGridLayoutManager;


/**
 * Created by chenxiandiao on 17/8/30.
 */

public class FilterPopupWindow extends PopupWindow {

    private static final String TAG = "FilterPopupWindow";
    private int mAnchorY;

    public interface OnFilterModelChoose {
        void onChoose(Object item);
    }

    private Context mContext;
    private List<Object> mListData = new ArrayList<>();
    private RecyclerView mRecyclerView;
    private CommonRecyclerViewAdapter<Object> mAdapter;
    private OnDismissListener mListener;
    private OnFilterModelChoose mOnChooseListener;

    public void setListener(OnDismissListener listener) {
        this.mListener = listener;
    }

    public void setOnChooseListener(OnFilterModelChoose onChooseListener) {
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
        View mContentView = LayoutInflater.from(context).inflate(R.layout.view_filter_pop_window, null);
        //设置布局
        setContentView(mContentView);
        setWidth(LinearLayout.LayoutParams.MATCH_PARENT);
        setHeight(LinearLayout.LayoutParams.MATCH_PARENT);
        setAnimationStyle(R.style.filter_pop_wnd_style);
//        setOutsideTouchable(false);
//        setBackgroundDrawable(new BitmapDrawable());
        initUI(context);
    }

    private void initUI(Context context) {
        mRecyclerView = (RecyclerView) getContentView().findViewById(R.id.recycler_view);
        mRecyclerView.setLayoutManager(new WrapContentGridLayoutManager(context, 3, AppUtils.getScreenSize(context).y / 2));

        mAdapter = new CommonRecyclerViewAdapter<Object>(mListData, R.layout.view_filter_pop_window_item, new OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                dismiss();
                if (mListener != null) {
                    mListener.onDismiss();
                }
                if (mOnChooseListener != null) {
                    mOnChooseListener.onChoose(mListData.get(position));
                }
            }
        }) {
            @Override
            public void bind(ViewHolder holder, Object item) {
                holder.setText(R.id.tv_name, (String) item);
            }
        };
        mRecyclerView.setAdapter(mAdapter);
        getContentView().findViewById(R.id.v_popwin_bottom).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
                if (mListener != null) {
                    mListener.onDismiss();
                }
            }
        });
    }

    public void showAsDropDown(View view, List<Object> listData) {
        mListData.clear();
        mListData.addAll(listData);
        if (mListData.size() < 3) {
            mRecyclerView.setLayoutManager(new WrapContentGridLayoutManager(mContext, 1, AppUtils.getScreenSize(mContext).y / 2));
        } else {
            mRecyclerView.setLayoutManager(new WrapContentGridLayoutManager(mContext, 3, AppUtils.getScreenSize(mContext).y / 2));
        }

        mAdapter.notifyDataSetChanged();
        Animation mInAnimation = AnimationUtils.loadAnimation(mContext, R.anim.zues_filter_slide_in_top_anim);
        mRecyclerView.startAnimation(mInAnimation);
//        super.showAsDropDown(view);
        showPopWindow(view);
    }

    private void showPopWindow(View view) {
        if (mAnchorY == 0) {
            int[] loc = new int[2];
            view.getLocationOnScreen(loc);
            mAnchorY = loc[1];
            Log.e(TAG, String.valueOf(mAnchorY));
        }
//        mPopupWnd.setHeight(AppUtils.getScreenSize(mContext).y - mAnchorY - 1);
//        showAtLocation(view, Gravity.NO_GRAVITY, 0, mAnchorY + 1);
        showAtLocation(view, Gravity.NO_GRAVITY, 0, 500);
    }
   

}
