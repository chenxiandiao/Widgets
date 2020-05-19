package cxd.com.programlearning.activity;

import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

import cxd.com.programlearning.R;
import cxd.com.programlearning.widgets.flowlayout.CropViewGroup;
import cxd.com.programlearning.widgets.flowlayout.SimpleUser;

/**
 * Created by chenxiandiao on 17/10/18.
 */

public class CropViewGroupActivity extends AppCompatActivity {

    private CropViewGroup mCropViewGroup;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crop_view_group);
        mCropViewGroup = (CropViewGroup) findViewById(R.id.crop_view_group);
        List<SimpleUser> userList = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            SimpleUser user = new SimpleUser();
            user.mAvatar = "https://kascdn.kascend.com/jellyfish/opening/screen/170622/1498102274204.png";
            userList.add(user);
        }
        mCropViewGroup.setData(userList);
    }
}
