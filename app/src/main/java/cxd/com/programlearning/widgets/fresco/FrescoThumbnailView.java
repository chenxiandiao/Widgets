package cxd.com.programlearning.widgets.fresco;

import android.content.Context;
import android.graphics.drawable.Animatable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import androidx.annotation.IntDef;
import android.util.AttributeSet;
import android.view.ViewGroup;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.backends.pipeline.PipelineDraweeControllerBuilder;
import com.facebook.drawee.controller.BaseControllerListener;
import com.facebook.drawee.controller.ControllerListener;
import com.facebook.drawee.drawable.ScalingUtils;
import com.facebook.drawee.generic.GenericDraweeHierarchy;
import com.facebook.drawee.generic.RoundingParams;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.common.ResizeOptions;
import com.facebook.imagepipeline.common.RotationOptions;
import com.facebook.imagepipeline.image.ImageInfo;
import com.facebook.imagepipeline.request.BasePostprocessor;
import com.facebook.imagepipeline.request.ImageRequest;
import com.facebook.imagepipeline.request.ImageRequestBuilder;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import cxd.com.programlearning.R;
import cxd.com.programlearning.utils.UriUtils;
import cxd.com.programlearning.widgets.fresco.processor.BlurPostprocessor;
import cxd.com.programlearning.widgets.fresco.processor.BubbleParams;
import cxd.com.programlearning.widgets.fresco.processor.BubbleProcessor;
import cxd.com.programlearning.widgets.fresco.processor.GrayPostprocessor;
import cxd.com.programlearning.widgets.fresco.processor.ShadowPostprocessor;


/**
 * Created by linhonghong on 2015/9/28.
 */
public class FrescoThumbnailView extends SimpleDraweeView {
    /**
     * 使用layoutparams中设置的大小
     */
    public static final int CHANGE_NONE = 0;
    
    /**
     * 使用layoutparams中设置的高度，宽度按图片比例缩放
     */
    public static final int CHANGE_WIDTH = 1;
    
    /**
     * 使用layoutparams中设置的宽度，高度按图片比例缩放
     */
    public static final int CHANGE_HEIGHT = 2;
    
    @IntDef({CHANGE_NONE, CHANGE_WIDTH, CHANGE_HEIGHT})
    @Retention(RetentionPolicy.SOURCE)
    @Target({ElementType.PARAMETER, ElementType.FIELD, ElementType.LOCAL_VARIABLE})
    public @interface Remeasure {
        int value() default CHANGE_NONE;
    }
    
    // gray processor
    private boolean mGray = false;
    
    // blur processor
    private boolean mGlur = false;
    
    // bubble processor
    private BubbleParams mBubbleParams;

    private ShadowPostprocessor.Params mShadowParams;

    // 显示动画gif
    private boolean mAnim = false;
    
    // 自动旋转
    private boolean mAutoRotated = false;

    private boolean mRetry = false;
    
    
    private ControllerListener<ImageInfo> mChangeWidthListener;
    private ControllerListener<ImageInfo> mChangeHeightListener;
    
    public FrescoThumbnailView(Context context) {
        super(context);
    }
    
    public FrescoThumbnailView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }
    
    public FrescoThumbnailView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }
    
    public FrescoThumbnailView(Context context, GenericDraweeHierarchy hierarchy) {
        super(context, hierarchy);
    }
    
    /**
     * @param defaultResId 资源图片
     */
    public void loadResource(int defaultResId) {
        loadViewInternal(null, null, defaultResId, 0, 0, CHANGE_NONE);
    }
    
    /**
     * @param localPath    本地图片的绝对地址，加不加file:// 的scheme都可以
     * @param defaultResId 默认图片／色值
     * @param maxWidth     最大宽
     * @param maxHeight    最大高
     */
    public void loadLocalPath(String localPath, int defaultResId, int maxWidth, int maxHeight) {
        if (localPath == null || localPath.length() == 0) {
            loadViewInternal(null, null, defaultResId, maxWidth, maxHeight, CHANGE_NONE);
            return;
        }
        if (!localPath.startsWith("file://")) {
            localPath = "file://" + localPath;
        }
        loadViewInternal(null, localPath, defaultResId, maxWidth, maxHeight, CHANGE_NONE);
    }
    
    /**
     * @param thumbnailUri 网络图片，本地图片，资源图片
     *                     其中网络，直接用url
     *                     本地图片必须自己加上file://的scheme
     *                     而资源文件必须自己加上res://的scheme
     * @param defaultResID 默认图片／色值
     */
    public void loadView(String thumbnailUri, int defaultResID) {
        loadViewInternal(null, thumbnailUri, defaultResID, 0, 0, CHANGE_NONE);
    }
    
    /**
     * @param thumbnailUri 网络图片，本地图片，资源图片
     *                     其中网络，直接用url
     *                     本地图片必须自己加上file://的scheme
     *                     而资源文件必须自己加上res://的scheme
     * @param defaultResID 默认图片／色值
     * @param maxWidth     最大宽
     * @param maxHeight    最大高
     */
    public void loadView(String thumbnailUri, int defaultResID, int maxWidth, int maxHeight) {
        loadViewInternal(null, thumbnailUri, defaultResID, maxWidth, maxHeight, CHANGE_NONE);
    }
    
    /**
     * @param thumbnailUri 网络图片，本地图片，资源图片
     *                     其中网络，直接用url
     *                     本地图片必须自己加上file://的scheme
     *                     而资源文件必须自己加上res://的scheme
     * @param defautlResID 默认图片／色值
     *                     <p>
     *                     主要用于recyclerview, listview, gridview等可能回收重用holder的图片加载
     *                     通过设置tag,减少图片重新加载次数
     * @param maxWidth     最大宽
     * @param maxHeight    最大高
     */
    public void loadViewIfNecessary(String thumbnailUri, int defautlResID, int maxWidth, int maxHeight) {
        loadViewIfNecessary(thumbnailUri, defautlResID, maxWidth, maxHeight, CHANGE_NONE);
    }
    
    public void loadViewIfNecessary(String thumbnailUri, int defautlResID, int maxWidth, int maxHeight, @Remeasure int changeSize) {
        Object obj = getTag(R.id.zues_fresco_url_tag);
        boolean bSame = false;
        if (null != obj && obj.equals(thumbnailUri)) {
            bSame = true;
        }
        if (!bSame) {
            this.loadViewInternal(null, thumbnailUri, defautlResID, maxWidth, maxHeight, changeSize);
            this.setTag(R.id.zues_fresco_url_tag, thumbnailUri);
        }
    }
    
    public void setGray(boolean b) {
        mGray = b;
    }
    
    public void setBlur(boolean b) {
        mGlur = b;
    }
    
    public void setBubbleBgRes(BubbleParams params) {
        mBubbleParams = params;
    }

    public void setShadowParams(ShadowPostprocessor.Params shadowParams) {
        this.mShadowParams = shadowParams;
    }

    public void setAutoRotated(boolean autoRotated) {
        this.mAutoRotated = autoRotated;
    }

    public void setAnim(boolean b) {
        mAnim = b;
    }

    public void setCornerRadius(float topLeft, float topRight,
                                float bottomRight, float bottomLeft) {
        RoundingParams roundingParams = RoundingParams.fromCornersRadii(topLeft, topRight, bottomRight, bottomLeft);
        this.getHierarchy().setRoundingParams(roundingParams);
    }
    
    public void setCircle(int overlay_color) {
        RoundingParams roundingParams = RoundingParams.asCircle().
                setRoundingMethod(RoundingParams.RoundingMethod.OVERLAY_COLOR).
                setOverlayColor(overlay_color);
        this.getHierarchy().setRoundingParams(roundingParams);
    }
    
    public void setRoundAsCircle(boolean roundAsCircle) {
        RoundingParams roundingParams = new RoundingParams();
        roundingParams.setRoundAsCircle(roundAsCircle);
        this.getHierarchy().setRoundingParams(roundingParams);
    }
    
    public void setProgressBarDrawable(Drawable drawable) {
        this.getHierarchy().setProgressBarImage(drawable, ScalingUtils.ScaleType.CENTER);
    }
    
    public void setFailureImage(Drawable drawable) {
        this.getHierarchy().setFailureImage(drawable, ScalingUtils.ScaleType.CENTER);
    }

    public void setRetryImage(Drawable drawable) {
        this.mRetry = true;
        this.getHierarchy().setRetryImage(drawable, ScalingUtils.ScaleType.CENTER);
    }

    /**
     * 1. 因为可能出现的oom，不过应当是在图片解析的线程，而不是本线程
     * 2. ImageRequestBuilder.newBuilderWithResourceId源码中说只能用具体的图片,但是用颜色貌似也没有出错，
     * 所以以防万一，这里catch throwable
     */
    private void loadViewInternal(String lowUrl, String thumbnailUri, int defaultResID, int width, int height, @Remeasure int changeSize) {
        try {
            if (defaultResID > 0) {
                getHierarchy().setPlaceholderImage(defaultResID);
            }
            Uri uri = UriUtils.parseUriOrNull(thumbnailUri);
            if (uri == null) {
                if (defaultResID > 0) {
                    setResourceController(defaultResID, width, height);
                }
            } else {
                Uri lowUri = UriUtils.parseUriOrNull(lowUrl);
                setUrlController(lowUri, uri, width, height, changeSize);
            }
        } catch (Throwable ignored) {
        }
    }
    
    private void setResourceController(int resid, int width, int height) {
        if (resid <= 0) {
            return;
        }
        BasePostprocessor processor = null;
        if (mBubbleParams != null) {
            processor = new BubbleProcessor(mBubbleParams, width, height);
        } else if (mShadowParams != null) {
            processor = new ShadowPostprocessor(mShadowParams);
        } else if (mGray) {
            processor = GrayPostprocessor.get();
        } else if (mGlur) {
            processor = BlurPostprocessor.get();
        }
        
        ImageRequestBuilder builder = ImageRequestBuilder.newBuilderWithResourceId(resid)
                .setLocalThumbnailPreviewsEnabled(true)
                .setRotationOptions(mAutoRotated ? RotationOptions.autoRotate() : RotationOptions.disableRotation());
        if (null != processor) {
            builder.setPostprocessor(processor);
        }
        if (width > 0 && height > 0) {
            builder.setResizeOptions(new ResizeOptions(width, height));
        }
        PipelineDraweeControllerBuilder pdcBuilder = Fresco.newDraweeControllerBuilder()
                .setImageRequest(builder.build())
                .setAutoPlayAnimations(mAnim)
                .setOldController(getController());
        setController(pdcBuilder.build());
    }
    
    private void setUrlController(Uri lowUri, Uri uri, int width, int height, @Remeasure int changeSize) {
        if (uri == null) {
            return;
        }
        BasePostprocessor processor = null;
        if (mBubbleParams != null){
            processor = new BubbleProcessor(mBubbleParams, width, height);
        } else if (mShadowParams != null) {
            processor = new ShadowPostprocessor(mShadowParams);
        } else if (mGray) {
            processor = GrayPostprocessor.get();
        } else if (mGlur) {
            processor = BlurPostprocessor.get();
        }
        
        ImageRequestBuilder builder = ImageRequestBuilder.newBuilderWithSource(uri)
                .setLocalThumbnailPreviewsEnabled(true)
                .setRotationOptions(mAutoRotated ? RotationOptions.autoRotate() : RotationOptions.disableRotation());
        if (null != processor) {
            builder.setPostprocessor(processor);
        }
        if (width > 0 && height > 0) {
            builder.setResizeOptions(new ResizeOptions(width, height));
        }
        
        PipelineDraweeControllerBuilder pdcBuilder = Fresco.newDraweeControllerBuilder()
                .setImageRequest(builder.build())
                .setTapToRetryEnabled(mRetry)
                .setAutoPlayAnimations(mAnim)
                .setOldController(this.getController());
        
        if (changeSize != CHANGE_NONE) {
            pdcBuilder.setControllerListener(getControllerListener(changeSize));
        }
        if (ImageRequest.fromUri(lowUri) != null) {
            pdcBuilder.setLowResImageRequest(ImageRequest.fromUri(lowUri));
        }
        setController(pdcBuilder.build());
    }
    
    private ControllerListener<ImageInfo> getControllerListener(@Remeasure int type) {
        if (type == CHANGE_WIDTH) {
            if (mChangeWidthListener == null) {
                mChangeWidthListener = new BaseControllerListener<ImageInfo>() {
                    
                    @Override
                    public void onFinalImageSet(String id, ImageInfo imageInfo, Animatable animatable) {
                        super.onFinalImageSet(id, imageInfo, animatable);
                        if (imageInfo == null || imageInfo.getHeight() <= 0) {
                            return;
                        }
                        int height = imageInfo.getHeight();
                        int width = imageInfo.getWidth();
                        ViewGroup.LayoutParams layoutParams = getLayoutParams();
                        layoutParams.width = (layoutParams.height * width) / height;
                        setLayoutParams(layoutParams);
                    }
                };
            }
            return mChangeWidthListener;
        } else if (type == CHANGE_HEIGHT) {
            if (mChangeHeightListener == null) {
                mChangeHeightListener = new BaseControllerListener<ImageInfo>() {
                    
                    @Override
                    public void onFinalImageSet(String id, ImageInfo imageInfo, Animatable animatable) {
                        super.onFinalImageSet(id, imageInfo, animatable);
                        if (imageInfo == null || imageInfo.getWidth() <= 0) {
                            return;
                        }
                        int height = imageInfo.getHeight();
                        int width = imageInfo.getWidth();
                        ViewGroup.LayoutParams layoutParams = getLayoutParams();
                        layoutParams.height = height * layoutParams.width / width;
                        setLayoutParams(layoutParams);
                    }
                };
            }
            return mChangeHeightListener;
        }
        return null;
    }
    
}
