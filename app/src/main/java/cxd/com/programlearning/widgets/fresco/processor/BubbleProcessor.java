package cxd.com.programlearning.widgets.fresco.processor;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import androidx.annotation.DrawableRes;
import android.util.SparseArray;

import com.facebook.cache.common.CacheKey;
import com.facebook.cache.common.SimpleCacheKey;
import com.facebook.common.references.CloseableReference;
import com.facebook.imagepipeline.bitmaps.PlatformBitmapFactory;
import com.facebook.imagepipeline.request.BasePostprocessor;

import java.lang.ref.WeakReference;
import java.util.Locale;

import cxd.com.programlearning.utils.Utils;


/**
 * Created by Rancune@126.com on 2017/6/6.
 */

public class BubbleProcessor extends BasePostprocessor {
    private static final String BUBBLE_PROCESSOR = "BubbleProcessor";
    private static final int DEFAULT_SIZE = 300;

    private BubbleParams mParams;
    private int mWidth = DEFAULT_SIZE;
    private int mHeight = DEFAULT_SIZE;

    private CacheKey mCacheKey;

    private static final SparseArray<WeakReference<Bitmap>> sCache = new SparseArray<>();

    public BubbleProcessor(BubbleParams params, int width, int height) {
        mParams = params;
        mWidth = width <= 0 ? DEFAULT_SIZE : width;
        mHeight = height <= 0 ? DEFAULT_SIZE : height;
    }

    @Override
    public String getName() {
        return BUBBLE_PROCESSOR;
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
        Canvas mCanvas = new Canvas(destBitmap);
        Rect rect = new Rect(0, 0, mWidth, mHeight);
        Rect rectF;
        if (sourceBitmap.getHeight() / sourceBitmap.getWidth() > 3) {
            int top = (sourceBitmap.getHeight() - 3 * sourceBitmap.getWidth()) / 2;
            rectF = new Rect(0, top, sourceBitmap.getWidth(), top + 3 * sourceBitmap.getWidth());
        } else if (sourceBitmap.getWidth() / sourceBitmap.getHeight() > 3) {
            int left = (sourceBitmap.getWidth() - 3 * sourceBitmap.getHeight()) / 2;
            rectF = new Rect(left, 0, left + sourceBitmap.getHeight() * 3, sourceBitmap.getHeight());
        } else {
            rectF = new Rect(0, 0, sourceBitmap.getWidth(), sourceBitmap.getHeight());
        }

        // Perform the bubble
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_IN));
        mCanvas.drawBitmap(sourceBitmap, rectF, rect, null);

        Bitmap bubble_TL = getBitmap(mParams.bubble_top_left);
        Rect src_TL = new Rect(0, 0, bubble_TL.getWidth(), bubble_TL.getHeight());
        Rect dst_TL = new Rect(0, 0, bubble_TL.getWidth(), bubble_TL.getHeight());
        mCanvas.drawBitmap(bubble_TL, src_TL, dst_TL, paint);

        Bitmap bubble_BL = getBitmap(mParams.bubble_bottom_left);
        Rect src_BL = new Rect(0, 0, bubble_BL.getWidth(), bubble_BL.getHeight());
        Rect dst_BL = new Rect(0, mHeight - bubble_BL.getHeight(), bubble_BL.getWidth(), mHeight);
        mCanvas.drawBitmap(bubble_BL, src_BL, dst_BL, paint);

        Bitmap bubble_L = getBitmap(mParams.bubble_left);
        Rect src_left = new Rect(0, 0, bubble_L.getWidth(), bubble_L.getHeight());
        Rect dst_left = new Rect(0, bubble_TL.getHeight(), bubble_L.getWidth(), mHeight - bubble_BL.getHeight());
        mCanvas.drawBitmap(bubble_L, src_left, dst_left, paint);

        Bitmap bubble_TR = getBitmap(mParams.bubble_top_right);
        Rect src_TR = new Rect(0, 0, bubble_TR.getWidth(), bubble_TR.getHeight());
        Rect dst_TR = new Rect(mWidth - bubble_TR.getWidth(), 0, mWidth, bubble_TR.getHeight());
        mCanvas.drawBitmap(bubble_TR, src_TR, dst_TR, paint);

        Bitmap bubble_BR = getBitmap(mParams.bubble_bottom_right);
        Rect src_BR = new Rect(0, 0, bubble_BR.getWidth(), bubble_BR.getHeight());
        Rect dst_BR = new Rect(mWidth - bubble_BR.getWidth(), mHeight - bubble_BR.getHeight(), mWidth, mHeight);
        mCanvas.drawBitmap(bubble_BR, src_BR, dst_BR, paint);

        Bitmap bubble_R = getBitmap(mParams.bubble_right);
        Rect src_right = new Rect(0, 0, bubble_R.getWidth(), bubble_R.getHeight());
        Rect dst_right = new Rect(mWidth - bubble_R.getWidth(), bubble_TR.getHeight(), mWidth, mHeight - bubble_BR.getHeight());
        mCanvas.drawBitmap(bubble_R, src_right, dst_right, paint);

        // Draw border
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_OVER));
        Bitmap border_TL = getBitmap(mParams.border_top_left);
        Rect border_src = new Rect(0, 0, border_TL.getWidth(), border_TL.getHeight());
        Rect image_dst = new Rect(0, 0, border_TL.getWidth(), border_TL.getHeight());
        mCanvas.drawBitmap(border_TL, border_src, image_dst, paint);

        Bitmap border_BL = getBitmap(mParams.border_bottom_left);
        Rect border_src_BL = new Rect(0, 0, border_BL.getWidth(), border_BL.getHeight());
        Rect border_dst_BL = new Rect(0, mHeight - border_BL.getHeight(), border_BL.getWidth(), mHeight);
        mCanvas.drawBitmap(border_BL, border_src_BL, border_dst_BL, paint);

        Bitmap border_Line_L = getBitmap(mParams.border_left);
        Rect border_src_left = new Rect(0, 0, border_Line_L.getWidth(), border_Line_L.getHeight());
        Rect border_dst_left = new Rect(0, border_TL.getHeight(), border_Line_L.getWidth(), mHeight - border_BL.getHeight());
        mCanvas.drawBitmap(border_Line_L, border_src_left, border_dst_left, paint);

        Bitmap border_TR = getBitmap(mParams.border_top_right);
        Rect border_src_TR = new Rect(0, 0, bubble_TR.getWidth(), bubble_TR.getHeight());
        Rect border_dst_TR = new Rect(mWidth - border_TR.getWidth(), 0, mWidth, border_TR.getHeight());
        mCanvas.drawBitmap(border_TR, border_src_TR, border_dst_TR, paint);

        Bitmap border_BR = getBitmap(mParams.border_bottom_right);
        Rect border_src_BR = new Rect(0, 0, bubble_BR.getWidth(), bubble_BR.getHeight());
        Rect border_dst_BR = new Rect(mWidth - border_BR.getWidth(), mHeight - border_BR.getHeight(), mWidth, mHeight);
        mCanvas.drawBitmap(border_BR, border_src_BR, border_dst_BR, paint);

        Bitmap border_Line_R = getBitmap(mParams.border_right);
        Rect border_src_Right = new Rect(0, 0, border_Line_R.getWidth(), border_Line_R.getHeight());
        Rect border_dst_Right = new Rect(mWidth - border_Line_R.getWidth(), border_TR.getHeight(),
                mWidth, mHeight - border_BR.getHeight());
        mCanvas.drawBitmap(border_Line_R, border_src_Right, border_dst_Right, paint);

        Bitmap border_Line_TB = getBitmap(mParams.border_top);
        Rect border_src_tb = new Rect(0, 0, border_Line_TB.getWidth(), border_Line_TB.getHeight());
        Rect border_dst_top = new Rect(border_TL.getWidth(), 0, mWidth - border_TR.getWidth(),
                border_Line_TB.getHeight());
        mCanvas.drawBitmap(border_Line_TB, border_src_tb, border_dst_top, paint);

        Rect border_dst_bottom =
                new Rect(border_BL.getWidth(), mHeight - border_Line_TB.getHeight(),
                        mWidth - border_BR.getWidth(), mHeight);
        mCanvas.drawBitmap(border_Line_TB, border_src_tb, border_dst_bottom, paint);

        paint.setXfermode(null);

    }

    @Override
    public CacheKey getPostprocessorCacheKey() {
        if (mCacheKey == null) {
            final String key = String.format((Locale) null, "%s%d%d", mParams.key, mWidth, mHeight);
            mCacheKey = new SimpleCacheKey(key);
        }
        return mCacheKey;
    }

    private Bitmap getBitmap(@DrawableRes int bitmapRes) {
        if (bitmapRes <= 0) {
            return null;
        }
        synchronized (sCache) {
            WeakReference<Bitmap> reference = sCache.get(bitmapRes);
            if (reference != null && reference.get() != null) {
                return reference.get();
            } else {
                Bitmap bitmap = BitmapFactory.decodeResource(Utils.mContext.getResources(), bitmapRes);
                sCache.put(bitmapRes, new WeakReference<>(bitmap));
                return bitmap;
            }
        }
    }


}
