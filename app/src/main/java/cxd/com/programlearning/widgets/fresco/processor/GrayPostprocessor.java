package cxd.com.programlearning.widgets.fresco.processor;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Paint;

import com.facebook.cache.common.CacheKey;
import com.facebook.cache.common.SimpleCacheKey;
import com.facebook.imagepipeline.request.BasePostprocessor;

/**
 * Created by linhonghong on 2015/9/29.
 */
public class GrayPostprocessor extends BasePostprocessor {

    private static final GrayPostprocessor GRAY_PROCESSOR = new GrayPostprocessor();

    public static GrayPostprocessor get() {
        return GRAY_PROCESSOR;
    }

    private static final CacheKey CACHE_KEY = new SimpleCacheKey("GrayPostprocessor");

    private GrayPostprocessor() {
    }

    @Override
    public String getName() {
        return "GrayPostprocessor";
    }

    @Override
    public void process(Bitmap bitmap) {
        Canvas c = new Canvas(bitmap);
        Paint paint = new Paint();
        ColorMatrix cm = new ColorMatrix();
        cm.setSaturation(0);
        ColorMatrixColorFilter f = new ColorMatrixColorFilter(cm);
        paint.setColorFilter(f);
        c.drawBitmap(bitmap, 0, 0, paint);
    }

    @Override
    public CacheKey getPostprocessorCacheKey() {
        return CACHE_KEY;
    }
}
