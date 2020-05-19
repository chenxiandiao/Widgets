package cxd.com.programlearning.activity;

import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

import cxd.com.programlearning.R;
import cxd.com.programlearning.widgets.banner.ListItem;
import cxd.com.programlearning.widgets.banner.PostView;

/**
 * Created by chenxiandiao on 17/9/25.
 */

public class BannerActivity extends AppCompatActivity {

    private PostView mPostView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_banner);
        mPostView = (PostView) findViewById(R.id.post_view);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mockData();
    }

    private void mockData() {
        ListItem item = new ListItem();
        item.mName = "item1";
        item.mUrl = "https://kascdn.kascend.com/jellyfish/mic/room/chat_1.png";
        ListItem item2 = new ListItem();
        item2.mName = "item2";
        item2.mUrl = "https://kascdn.kascend.com/jellyfish/opening/screen/170622/1498102274204.png";
        List<ListItem> mDatas = new ArrayList<>();
        mDatas.add(item);
        mDatas.add(item2);
        mPostView.setShowData(mDatas, null, null);
    }
}
