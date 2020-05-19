package cxd.com.programlearning.widgets.recyclerviewfilter;

import android.content.Context;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import cxd.com.programlearning.R;
import cxd.com.programlearning.widgets.recyclerview.adapter.CommonRecyclerViewAdapter;
import cxd.com.programlearning.widgets.recyclerview.adapter.OnItemClickListener;

/**
 * Created by chenxiandiao on 17/9/7.
 */

public class FilterRecyclerViewItem extends LinearLayout {
    private RecyclerView mRecyclerView;
    private CommonRecyclerViewAdapter<FilterItem> mAdapter;
    private List<FilterItem> mListData = new ArrayList<>();
    private OnFilterItemChoose mOnChooseListener;

    public void setOnChooseListener(OnFilterItemChoose onChooseListener) {
        this.mOnChooseListener = onChooseListener;
    }

    public FilterRecyclerViewItem(Context context) {
        this(context, null);
    }

    public FilterRecyclerViewItem(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public FilterRecyclerViewItem(final Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initUI(context);
    }

    private void initUI(final Context context) {
        mRecyclerView = (RecyclerView) LayoutInflater.from(context).inflate(R.layout.tree_view_filter_pop_window_recyclerview_item, this, false);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));
        mAdapter = new CommonRecyclerViewAdapter<FilterItem>(mListData, R.layout.tree_view_filter_pop_window_tiny_item, new OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                if (mListData.get(position).mSelected) {
                    return;
                } else {
                    for (int i = 0; i < mListData.size(); i++) {
                        mListData.get(i).mSelected = false;
                    }
                    mListData.get(position).mSelected = true;
                    mAdapter.notifyDataSetChanged();
                    if (mOnChooseListener != null) {
                        mOnChooseListener.onChoose();
                    }
                }
            }
        }) {
            @Override
            public void bind(ViewHolder holder, FilterItem item) {
                TextView textView = holder.getView(R.id.tv_name);
                textView.setText(item.mFilterName);
                if (item.mSelected) {
                    textView.setTextColor(ContextCompat.getColor(context, R.color.kas_item_red));
                    textView.setBackgroundResource(R.drawable.bg_popup_filter_item);
                } else {
                    textView.setTextColor(ContextCompat.getColor(context, R.color.kas_black));
                    textView.setBackgroundResource(0);
                }
            }
        };
        mRecyclerView.setAdapter(mAdapter);
        addView(mRecyclerView);
    }

    public void setData(List<FilterItem> data) {
        if (mListData.isEmpty()) {
            mListData.addAll(data);
            mAdapter.notifyDataSetChanged();
        }
    }


}
