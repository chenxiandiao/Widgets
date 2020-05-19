package cxd.com.programlearning.widgets.recyclerview.adapter;

import androidx.annotation.ColorInt;
import androidx.annotation.DrawableRes;
import androidx.annotation.LayoutRes;
import androidx.annotation.StringRes;
import androidx.recyclerview.widget.RecyclerView;
import android.text.SpannableStringBuilder;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;


/**
 * Created by Rancune@126.com on 2016/4/30.
 */
@SuppressWarnings("unused")
public abstract class CommonRecyclerViewAdapter<T> extends RecyclerView.Adapter<CommonRecyclerViewAdapter.ViewHolder> {
    private List<T> data;
    private int itemLayoutId;
    private OnItemClickListener clickListener;
    private OnItemLongClickListener longClickListener;

    public CommonRecyclerViewAdapter(List<T> data, @LayoutRes int itemLayoutId, OnItemClickListener clickListener) {
        this.data = data;
        this.itemLayoutId = itemLayoutId;
        this.clickListener = clickListener;
    }

    public CommonRecyclerViewAdapter(List<T> data, @LayoutRes int itemLayoutId, OnItemClickListener clickListener, OnItemLongClickListener longClickListener) {
        this.data = data;
        this.itemLayoutId = itemLayoutId;
        this.clickListener = clickListener;
        this.longClickListener = longClickListener;
    }

    public void setData(List<T> data) {
        this.data = data;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(itemLayoutId, parent, false);
        return new ViewHolder(v, clickListener, longClickListener);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        T item = data.get(position);
        bind(holder, item);
    }

    public abstract void bind(ViewHolder holder, T item);

    @Override
    public int getItemCount() {
        return data == null ? 0 : data.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {
        private SparseArray<View> views;
        private OnItemClickListener clickListener;
        private OnItemLongClickListener longClickListener;

        public ViewHolder(View itemView, OnItemClickListener clickListener, OnItemLongClickListener longClickListener) {
            super(itemView);
            this.clickListener = clickListener;
            this.longClickListener = longClickListener;
            views = new SparseArray<>();
            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);
        }

        public <V extends View> V getView(int viewId) {
            View view = views.get(viewId);
            if (view == null) {
                view = itemView.findViewById(viewId);
                views.put(viewId, view);
            }
            //noinspection unchecked
            return (V) view;
        }

        /**
         * 设置文字内容
         */
        public ViewHolder setText(int viewId, CharSequence text) {
            TextView view = getView(viewId);
            view.setText(text);
            return this;
        }

        public ViewHolder setText(int viewId, SpannableStringBuilder text) {
            TextView view = getView(viewId);
            view.setText(text);
            return this;
        }

        public ViewHolder setText(int viewId, @StringRes int textRes) {
            TextView view = getView(viewId);
            view.setText(textRes);
            return this;
        }

        public ViewHolder setTextColor(int viewId, @ColorInt int color) {
            TextView view = getView(viewId);
            view.setTextColor(color);
            return this;
        }

        public ViewHolder setImageResource(int viewId, @DrawableRes int drawableRes) {
            ImageView view = getView(viewId);
            view.setImageResource(drawableRes);
            return this;
        }

        /**
         * 给FrescoThumbnailView，设置图片链接， 设置占位符
         */
//        public ViewHolder setImageUrl(int id, String url, int defaultRes, int width, int height) {
//            FrescoThumbnailView view = getView(id);
//            view.loadViewIfNecessary(url, defaultRes, width, height);
//            return this;
//        }
//
//        public ViewHolder setImageUrl(int id, String url, int defaultRes, int width, int height, @FrescoThumbnailView.Remeasure int changeSize) {
//            FrescoThumbnailView view = getView(id);
//            view.loadViewIfNecessary(url, defaultRes, width, height, changeSize);
//            return this;
//        }
//
//        public ViewHolder setImageUrl(int id, String url, int defaultRes
//                , int width, int height, @FrescoThumbnailView.Remeasure int changeSize, boolean gray) {
//            FrescoThumbnailView view = getView(id);
//            view.setGray(gray);
//            view.loadViewIfNecessary(url, defaultRes, width, height, changeSize);
//            return this;
//        }
        public ViewHolder setVisible(boolean visible, int... ids) {
            for (int id : ids) {
                View v = getView(id);
                v.setVisibility(visible ? View.VISIBLE : View.GONE);
            }
            return this;
        }

        /**
         * 将viewholder中的某些view的点击事件交给adapter统一处理
         */
        public ViewHolder setOnClickListener(int... ids) {
            for (int i : ids) {
                View v = getView(i);
                v.setOnClickListener(this);
            }
            return this;
        }

        public ViewHolder setChecked(int viewid, boolean checked) {
            CheckBox checkBox = getView(viewid);
            checkBox.setChecked(checked);
            return this;
        }

        /**
         * 将viewholder中的某些view的长按事件交给adapter统一处理
         */
        public ViewHolder setOnLongClickListener(int... ids) {
            for (int i : ids) {
                View v = getView(i);
                if (!v.isLongClickable()) {
                    v.setLongClickable(true);
                }
                v.setOnLongClickListener(this);
            }
            return this;
        }


        @Override
        public void onClick(View v) {
            if (clickListener != null) {
                clickListener.onItemClick(v, getLayoutPosition());
            }
        }

        @Override
        public boolean onLongClick(View v) {
            if (longClickListener != null) {
                longClickListener.onItemLongClick(v, getLayoutPosition());
            }
            return false;
        }
    }
}
