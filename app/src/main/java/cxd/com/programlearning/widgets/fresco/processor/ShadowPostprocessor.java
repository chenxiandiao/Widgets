package cxd.com.programlearning.widgets.fresco.processor;

import android.graphics.Bitmap;
import android.graphics.BlurMaskFilter;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;

import com.facebook.cache.common.CacheKey;
import com.facebook.cache.common.SimpleCacheKey;
import com.facebook.common.references.CloseableReference;
import com.facebook.imagepipeline.bitmaps.PlatformBitmapFactory;
import com.facebook.imagepipeline.request.BasePostprocessor;

import java.util.Locale;

/**
 * Created by chenxiandiao on 17/9/14.
 */

public class ShadowPostprocessor extends BasePostprocessor {
    public int mDx;
    public int mDy;
    public float mRadius;
    public int mShadowColor;
    public int mWidth;
    public int mHeight;
    private CacheKey mCacheKey;

    public ShadowPostprocessor(Params params) {
        mDx = params.mDx;
        mDy = params.mDy;
        mRadius = params.mRadius;
        mShadowColor = params.mShadowColor;
        mWidth = params.mWidth;
        mHeight = params.mHeight;
    }

    @Override
    public String getName() {
        return "ShadowPostprocessor";
    }


    @Override
    public CloseableReference<Bitmap> process(Bitmap sourceBitmap, PlatformBitmapFactory bitmapFactory) {
        final Bitmap.Config sourceBitmapConfig = sourceBitmap.getConfig();
        CloseableReference<Bitmap> destBitmapRef =
                bitmapFactory.createBitmapInternal(
                        mWidth,
                        mHeight,
                        sourceBitmapConfig != null ? sourceBitmapConfig : FALLBACK_BITMAP_CONFIGURATION);
        try {
            process(destBitmapRef.get(), sourceBitmap);
            return CloseableReference.cloneOrNull(destBitmapRef);
        } finally {
            CloseableReference.closeSafely(destBitmapRef);
        }
    }

    @Override
    public void process(Bitmap destBitmap, Bitmap sourceBitmap) {
        Canvas canvas = new Canvas(destBitmap);
        int width = mWidth - mDx;
        int height = mHeight - mDy;
        Paint paint = new Paint();
        Bitmap shadowBitmap = sourceBitmap.extractAlpha();
        //绘制阴影
        paint.setColor(mShadowColor);
        paint.setMaskFilter(new BlurMaskFilter(mRadius, BlurMaskFilter.Blur.NORMAL));
        canvas.drawBitmap(shadowBitmap, null, new Rect(mDx, mDy, mWidth, mHeight), paint);
        //绘制原图像
        Paint nPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        nPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_OVER));
        canvas.drawBitmap(sourceBitmap, null, new Rect(0, 0, width, height), nPaint);
    }


    @Override
    public CacheKey getPostprocessorCacheKey() {
        if (mCacheKey == null) {
            final String key = String.format((Locale) null, "%f%d%d%d%d%d", mRadius, mDx, mDy,
                    mShadowColor, mWidth, mHeight);
            mCacheKey = new SimpleCacheKey(key);
        }
        return mCacheKey;
    }

    static public class Params {
        public int mDx;
        public int mDy;
        public float mRadius;
        public int mShadowColor;
        public int mWidth;
        public int mHeight;

        public Params(int mDx, int mDy, float mRadius, int mShadowColor, int mWidth, int mHeight) {
            this.mDx = mDx;
            this.mDy = mDy;
            this.mRadius = mRadius;
            this.mShadowColor = mShadowColor;
            this.mWidth = mWidth;
            this.mHeight = mHeight;
        }
    }
}
