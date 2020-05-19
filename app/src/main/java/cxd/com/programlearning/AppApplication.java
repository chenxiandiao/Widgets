package cxd.com.programlearning;

import android.app.Application;

import cxd.com.programlearning.utils.Utils;
import cxd.com.programlearning.widgets.fresco.ImageLoader;

/**
 * Created by chenxiandiao on 17/9/25.
 */

public class AppApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        ImageLoader.initialize(this, null);
        Utils.initialize(this);
    }
}
