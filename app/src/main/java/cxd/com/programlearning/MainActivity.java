package cxd.com.programlearning;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cxd.com.programlearning.activity.BannerActivity;
import cxd.com.programlearning.activity.BottomSheetActivity;
import cxd.com.programlearning.activity.CropViewGroupActivity;
import cxd.com.programlearning.activity.FilterActivity;
import cxd.com.programlearning.behavior.BehaviorActivity;
import cxd.com.programlearning.model.MainItem;

public class MainActivity extends AppCompatActivity {


    @BindView(R.id.recycler_view)
    RecyclerView mRecyclerView;

    private Context mContext;
    private List<MainItem> mListData = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        mContext = this;
//        startActivity(new Intent(MainActivity.this, BehaviorActivity.class));
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setAdapter(new MyAdapter(mListData));
        mockData();

    }

    private void mockData() {
        mListData.add(new MainItem(
                BehaviorActivity.class,
                mContext.getString(R.string.sample_title_fragment_pager),
                mContext.getString(R.string.sample_description_fragment_pager))
        );
        mListData.add(new MainItem(FilterActivity.class, "filter", "filterActivity"));
        mListData.add(new MainItem(BannerActivity.class, "banner", "bannerActivity"));
        mListData.add(new MainItem(CropViewGroupActivity.class, "cropview", "cropViewGroupActivity"));
        mListData.add(new MainItem(BottomSheetActivity.class, "bottomsheet", "bottomsheetActivity"));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d("cxd", resultCode + ":" + resultCode + ":");
    }

    interface ListItemClickListener<T> {
        void onItemClick(View v, T item);
    }


    class MyAdapter extends RecyclerView.Adapter<MyHolder> {

        private List<MainItem> mListData;

        public MyAdapter(List<MainItem> mListData) {
            this.mListData = mListData;
        }

        @Override
        public MyHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(mContext).inflate(R.layout.adapter_sample_list_item, parent, false);
            return new MyHolder(v, new ListItemClickListener<MainItem>() {
                @Override
                public void onItemClick(View v, MainItem item) {
                    final Intent intent = new Intent(mContext, item.sampleActivityClass());
//                    startActivity(intent);
                    startActivityForResult(intent, 1);
                }
            });
        }

        @Override
        public void onBindViewHolder(MyHolder holder, int position) {
            holder.bindView(mListData.get(position));
        }

        @Override
        public int getItemCount() {
            return mListData != null ? mListData.size() : 0;
        }
    }

    static class MyHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.sample_list_item_title)
        TextView mTvTitle;
        @BindView(R.id.sample_list_item_description)
        TextView mTvDesc;
        private MainItem mItem;

        @OnClick(R.id.card_content)
        public void onClick(View view) {
            if (mListener != null) {
                mListener.onItemClick(view, mItem);
            }
        }

        private ListItemClickListener<MainItem> mListener;

        public MyHolder(View itemView, ListItemClickListener<MainItem> listener) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            mListener = listener;
        }

        public void bindView(MainItem item) {
            mTvTitle.setText(item.title());
            mTvDesc.setText(item.description());
            mItem = item;

        }

    }


}
