package cxd.com.programlearning.utils;

import android.content.Context;
import android.graphics.Point;
import android.util.TypedValue;
import android.view.WindowManager;

/**
 * Created by chenxiandiao on 17/9/21.
 */

public class AppUtils {

    /**
     * @param context context
     * @param dpValue value in unit of TypedValue.COMPLEX_UNIT_DIP
     * @return the raw pixel size
     */
    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    /**
     * @param context context
     * @return a point which contains the width and height of the screen
     */
    public static Point getScreenSize(Context context) {
        WindowManager manager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Point pt = new Point();
        manager.getDefaultDisplay().getSize(pt);
        return pt;
    }

    public static float getRawSize(int unit, float size, Context context) {
        return TypedValue.applyDimension(unit, size,
                context.getResources().getDisplayMetrics());
    }

}
