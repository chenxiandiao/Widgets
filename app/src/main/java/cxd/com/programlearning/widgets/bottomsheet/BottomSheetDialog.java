package cxd.com.programlearning.widgets.bottomsheet;


import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import cxd.com.programlearning.R;
import cxd.com.programlearning.utils.AppUtils;
import cxd.com.programlearning.widgets.recyclerview.adapter.CommonRecyclerViewAdapter;
import cxd.com.programlearning.widgets.recyclerview.adapter.OnItemClickListener;
import cxd.com.programlearning.widgets.recyclerview.layoutmanager.FixedContentGridLayoutManager;

/**
 * Created by chenxiandiao on 17/11/27.
 */

public class BottomSheetDialog extends DialogFragment {

    private TextView mTvHello;
    private RecyclerView mRecyclerView;
    private List<ItemModel> mData = new ArrayList<>();
    private boolean mClicked = false;
    private FixedContentGridLayoutManager mLayoutManager;
    private Context mContext;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NO_TITLE, R.style.bottom_sheet_dialog);
        mContext = this.getContext();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_bottom_sheet, container, false);
        mRecyclerView = (RecyclerView) v.findViewById(R.id.recycler_view);
        mLayoutManager = new FixedContentGridLayoutManager(mContext, AppUtils.dip2px(mContext, 80));
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(new CommonRecyclerViewAdapter<ItemModel>(mData, R.layout.item_recyclerview_invite, new OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {

            }
        }) {

            @Override
            public void bind(ViewHolder holder, ItemModel item) {
                holder.setText(R.id.tv_title,item.title);
                holder.setText(R.id.tv_desc, item.desc);
            }
        });
        mTvHello = (TextView) v.findViewById(R.id.tv_hello);
        mTvHello.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mClicked) {
                    return;
                }
                mClicked = true;
                int height = mLayoutManager.getFixedHeight();
                ObjectAnimator heightAnimator = ObjectAnimator.ofInt(mLayoutManager, "fixedHeight", height, height * mData.size());
                heightAnimator.setDuration(300);
                heightAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                    @Override
                    public void onAnimationUpdate(ValueAnimator animation) {
                        mRecyclerView.requestLayout();
                    }
                });
                AnimatorSet set = new AnimatorSet();
                set.play(heightAnimator);
                set.start();

            }
        });

        mockData();
        return v;
    }

    private void mockData() {
        mData.add(new ItemModel("hi", "world"));
        for (int i = 0; i < 5; i++) {
            mData.add(new ItemModel("hello", "world"));
        }
    }


    @Override
    public void onStart() {
        super.onStart();
        Dialog dialog = getDialog();
        if (dialog == null) {
            return;
        }
        Window window = dialog.getWindow();
        if (window == null) {
            return;
        }
        WindowManager.LayoutParams lp = window.getAttributes();
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.gravity = Gravity.BOTTOM;
        window.setAttributes(lp);
    }
}
