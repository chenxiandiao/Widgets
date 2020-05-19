package cxd.com.programlearning.widgets.banner;

import android.content.Context;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

import cxd.com.programlearning.R;
import cxd.com.programlearning.widgets.fresco.FrescoThumbnailView;
import cxd.com.programlearning.widgets.fresco.Resize;

/**
 * Created by chenxiandiao on 17/1/12.
 */

public class PostAdapter extends BaseAdapter implements View.OnClickListener {

    private Context mContext;
    private List<ListItem> mPicList;
    private View itemView;
    private View.OnClickListener mClickListener;

    public PostAdapter(Context context, List<ListItem> picList) {
        this.mContext = context;
        if (picList == null || picList.size() == 0) {
            this.mPicList = new ArrayList<>();
        } else {
            this.mPicList = picList;
        }
    }

    @Override
    public View getView(int position) {
        itemView = View.inflate(mContext, R.layout.view_item_banner, null);
        FrescoThumbnailView imageView = ((FrescoThumbnailView) itemView.findViewById(R.id.iv_image));
        String imageUrl = mPicList.get(position % mPicList.size()).mUrl;
        imageView.loadView(imageUrl, R.drawable.default_live_big, Resize.screen_width, (int) (Resize.screen_width / PostView.POSTER_REDIO));
        itemView.setOnClickListener(this);
        return itemView;
    }


    @Override
    public int getCount() {
        return mPicList != null ? mPicList.size() : 0;
    }


    @Override
    public void onClick(View v) {
        if (mClickListener != null) {
            mClickListener.onClick(v);
        }
    }

    public void setClickListener(View.OnClickListener mClickListener) {
        this.mClickListener = mClickListener;
    }
}
