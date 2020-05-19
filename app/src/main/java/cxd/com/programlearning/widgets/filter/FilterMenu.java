package cxd.com.programlearning.widgets.filter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.AttributeSet;
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
import cxd.com.programlearning.widgets.recyclerview.adapter.CommonRecyclerViewAdapter;
import cxd.com.programlearning.widgets.recyclerview.adapter.OnItemClickListener;
import cxd.com.programlearning.widgets.recyclerview.layoutmanager.WrapContentGridLayoutManager;
import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.FlowableEmitter;
import io.reactivex.FlowableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;


/**
 * Created by chenxiandiao on 17/8/30.
 */

public class FilterMenu extends LinearLayout implements FilterPopupWindow.OnFilterModelChoose {
    private List<FilterModel> mListData = new ArrayList<>();
    private Context mContext;
    private RecyclerView mRecyclerView;
    private CommonRecyclerViewAdapter<FilterModel> mAdapter;
    private FilterPopupWindow mPopupWnd;

    private FilterPopupWindow.OnFilterModelChoose mOnChooseListener;

    public void setOnChooseListener(FilterPopupWindow.OnFilterModelChoose onChooseListener) {
        this.mOnChooseListener = onChooseListener;
    }

    public FilterMenu(Context context) {
        this(context, null);
    }

    public FilterMenu(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        initUI();
    }

    public void initUI() {
        View v = LayoutInflater.from(mContext).inflate(R.layout.view_filter_menu, this, true);
        initPopWnd();
        mRecyclerView = (RecyclerView) v.findViewById(R.id.recycler_view);
        mRecyclerView.setLayoutManager(new GridLayoutManager(mContext, 1, RecyclerView.VERTICAL,false));
        mAdapter = (new CommonRecyclerViewAdapter<FilterModel>(mListData, R.layout.view_filter_menu_item, new OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                if (mListData.get(position).selected) {
                    if (mPopupWnd != null) {
                        mPopupWnd.dismiss();
                    }
                } else {
                    for (int i = 0; i < mListData.size(); i++) {
                        mListData.get(i).selected = false;
                    }
                    if (mPopupWnd != null) {
                        mPopupWnd.showAsDropDown(view, mListData.get(position).filterData);
                    }
                }
                mListData.get(position).selected = !mListData.get(position).selected;
                mAdapter.notifyDataSetChanged();
            }
        }) {
            @Override
            public void bind(ViewHolder holder, FilterModel item) {
                if (item.selected) {
                    ((TextView) holder.getView(R.id.tv_name)).setTextColor(ContextCompat.getColor(mContext, R.color.kas_item_red));
                    Drawable up = ContextCompat.getDrawable(mContext, R.drawable.icon_arrow_up_red);
                    up.setBounds(0, 0, up.getMinimumWidth(), up.getMinimumHeight());
                    ((TextView) holder.getView(R.id.tv_name)).setCompoundDrawables(null, null, up, null);
                } else {
                    ((TextView) holder.getView(R.id.tv_name)).setTextColor(ContextCompat.getColor(mContext, R.color.kas_black));
                    Drawable down = ContextCompat.getDrawable(mContext, R.drawable.icon_arrow_down_gray);
                    down.setBounds(0, 0, down.getMinimumWidth(), down.getMinimumHeight());
                    ((TextView) holder.getView(R.id.tv_name)).setCompoundDrawables(null, null, down, null);
                }
                holder.setText(R.id.tv_name, (String) item.mSelectedData);
            }
        });
        mRecyclerView.setAdapter(mAdapter);
        mockData();
    }


    private void initPopWnd() {
        if (mPopupWnd == null) {
            mPopupWnd = new FilterPopupWindow(mContext);
            mPopupWnd.setOnChooseListener(this);
            mPopupWnd.setListener(new PopupWindow.OnDismissListener() {
                @Override
                public void onDismiss() {
                    for (int i = 0; i < mListData.size(); i++) {
                        mListData.get(i).selected = false;
                    }
                    mAdapter.notifyDataSetChanged();
                }
            });
        }
    }

    private void mockData() {
        Flowable.create(new FlowableOnSubscribe<Boolean>() {
            @Override
            public void subscribe(FlowableEmitter<Boolean> e) throws Exception {

                e.onNext(true);

                e.onComplete();
            }
        }, BackpressureStrategy.BUFFER)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Boolean>() {
                    @Override
                    public void accept(Boolean aBoolean) throws Exception {
                        if (aBoolean) {
                            FilterModel model1 = new FilterModel();
                            model1.selected = false;
                            model1.filterData.add("女");
                            model1.filterData.add("男");
                            model1.mSelectedData = model1.filterData.get(0);

                            FilterModel model2 = new FilterModel();
                            model2.selected = false;
                            model2.filterData.add("青铜局");
                            model2.filterData.add("白银局");
                            model2.filterData.add("黄金局");
                            model2.filterData.add("铂金局");
                            model2.filterData.add("钻石局");
                            model2.filterData.add("王者局");
                            model2.mSelectedData = model2.filterData.get(0);
                            List<FilterModel> listData = new ArrayList<>();
                            mListData.clear();
                            mListData.add(model1);
                            mListData.add(model2);
                            setData();
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                    }
                });
    }


    public void setData() {
        if (!Utils.isEmpty(mListData)) {
//            mListData.clear();
//            mListData.addAll(listData);
            mRecyclerView.setLayoutManager(new GridLayoutManager(mContext, mListData.size(), RecyclerView.VERTICAL,false));
            mAdapter.notifyDataSetChanged();
        } else {
            this.setVisibility(View.GONE);
        }
    }


    @Override
    public void onChoose(Object item) {
        for (int i = 0; i < mListData.size(); i++) {
            List<Object> data = mListData.get(i).filterData;
            for (int j = 0; j < data.size(); j++) {
                if (data.get(j) == item) {
                    //选择发生了变化
                    if (mListData.get(i).mSelectedData != item) {
                        mListData.get(i).mSelectedData = item;
                        mAdapter.notifyDataSetChanged();
                        if (mOnChooseListener != null) {
                            mOnChooseListener.onChoose(item);
                        }
                    }
                    return;
                }
            }
        }
    }
}