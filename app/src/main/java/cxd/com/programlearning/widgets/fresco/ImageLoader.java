package cxd.com.programlearning.widgets.fresco;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import androidx.annotation.Nullable;

import com.facebook.binaryresource.BinaryResource;
import com.facebook.binaryresource.FileBinaryResource;
import com.facebook.cache.common.CacheKey;
import com.facebook.common.executors.UiThreadImmediateExecutorService;
import com.facebook.common.memory.MemoryTrimType;
import com.facebook.common.memory.MemoryTrimmable;
import com.facebook.common.memory.MemoryTrimmableRegistry;
import com.facebook.common.references.CloseableReference;
import com.facebook.datasource.DataSource;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.imagepipeline.backends.okhttp3.OkHttpImagePipelineConfigFactory;
import com.facebook.imagepipeline.cache.DefaultCacheKeyFactory;
import com.facebook.imagepipeline.common.ImageDecodeOptions;
import com.facebook.imagepipeline.common.ResizeOptions;
import com.facebook.imagepipeline.common.RotationOptions;
import com.facebook.imagepipeline.core.ImagePipelineConfig;
import com.facebook.imagepipeline.core.ImagePipelineFactory;
import com.facebook.imagepipeline.datasource.BaseBitmapDataSubscriber;
import com.facebook.imagepipeline.image.CloseableImage;
import com.facebook.imagepipeline.request.ImageRequest;
import com.facebook.imagepipeline.request.ImageRequestBuilder;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.concurrent.TimeUnit;

import cxd.com.programlearning.utils.IOUtils;
import cxd.com.programlearning.utils.UriUtils;
import cxd.com.programlearning.utils.Utils;
import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.FlowableEmitter;
import io.reactivex.FlowableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import okhttp3.OkHttpClient;

/**
 * Created by Rancune@126.com on 2017/2/7.
 */

public class ImageLoader {

    private static final String TAG = "ImageLoader";
    private static final int HTTP_TIME_OUT = 10 * 1000;
    private static OkHttpClient mHttpClient;

    public static void initialize(Context context, OkHttpClient okHttpClient) {
        context = Utils.checkNotNull(context);
        context = context.getApplicationContext();
        if (mHttpClient == null) {
            OkHttpClient.Builder builder = new OkHttpClient.Builder();
            builder.connectTimeout(HTTP_TIME_OUT, TimeUnit.MILLISECONDS)//建立连接的超时时间
                    .readTimeout(HTTP_TIME_OUT, TimeUnit.MILLISECONDS)//传递数据的超时时间
                    .retryOnConnectionFailure(true)
                    .writeTimeout(HTTP_TIME_OUT, TimeUnit.MILLISECONDS);//写数据的超时时间
            mHttpClient = builder.build();
        }

        ImagePipelineConfig frescoConfig = OkHttpImagePipelineConfigFactory
                .newBuilder(context, mHttpClient)
                // 图片内存格式，默认为ARGB_8888,使用RGB_565可以减少内存
                .setBitmapsConfig(Bitmap.Config.RGB_565)
                // 向下采样，默认resize只对jpg有用，开启这个后，对png，webp的resize也有用
                .setDownsampleEnabled(true)
                // DownsampleEnabled为true的情况下，默认不支持对网络中加载的图片的EncodeImage的改变，只对本地的有效
                // 设置为true的情况下，对本地网络都有效
                .setResizeAndRotateEnabledForNetwork(true)
                .setMemoryTrimmableRegistry(new MemoryTrimmableRegistry() {
                    @Override
                    public void registerMemoryTrimmable(MemoryTrimmable trimmable) {
                        // app前台系统内存不足，清理内存
                        trimmable.trim(MemoryTrimType.OnSystemLowMemoryWhileAppInForeground);
                    }

                    @Override
                    public void unregisterMemoryTrimmable(MemoryTrimmable trimmable) {

                    }
                })
                .build();
        Fresco.initialize(context, frescoConfig);
    }

    public static void resume() {
        Fresco.getImagePipeline().resume();
    }

    public static void pause() {
        Fresco.getImagePipeline().pause();
    }

    public static File getCachedImageOnDisk(Uri loadUri) {
        if (loadUri == null) {
            return null;
        }
        CacheKey cacheKey = DefaultCacheKeyFactory.getInstance().getEncodedCacheKey(ImageRequest.fromUri(loadUri), null);
        if (ImagePipelineFactory.getInstance().getMainFileCache().hasKey(cacheKey)) {
            BinaryResource resource = ImagePipelineFactory.getInstance().getMainFileCache().getResource(cacheKey);
            if (null != resource) {
                return ((FileBinaryResource) resource).getFile();
            }
        } else if (ImagePipelineFactory.getInstance().getSmallImageFileCache().hasKey(cacheKey)) {
            BinaryResource resource = ImagePipelineFactory.getInstance().getSmallImageFileCache().getResource(cacheKey);
            if (null != resource) {
                return ((FileBinaryResource) resource).getFile();
            }
        }
        return null;
    }

    public static void clearMemoryCache() {
        Fresco.getImagePipeline().clearMemoryCaches();
    }

//    @Nullable
//    public static Bitmap loadImageSync(String imgUrl, int maxW, int maxH) {
//        if (Utils.isEmpty(imgUrl)) {
//            return null;
//        }
//        InputStream in = null;
//        try {
//            URL url = new URL(imgUrl);
//            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
//            connection.setRequestMethod("GET");
//            connection.setDoInput(true);
//            connection.setConnectTimeout(ZuesConstants.HTTP_TIME_OUT);
//            connection.setReadTimeout(ZuesConstants.HTTP_TIME_OUT);
//            if (connection.getResponseCode() == 200) {
//                in = connection.getInputStream();
//                byte[] data = IOUtils.toByteArray(in);
//                if (data == null) {
//                    return null;
//                }
//                BitmapFactory.Options opts = new BitmapFactory.Options();
//                if (maxH > 0 && maxW > 0) {
//                    opts.inJustDecodeBounds = true;
//                    BitmapFactory.decodeByteArray(data, 0, data.length, opts);
//                    int orgw = opts.outWidth;
//                    int orgh = opts.outHeight;
//                    KasLog.d(TAG, "get bmp orignal w:" + orgw + " h:" + orgh);
//                    int maxNumOfPixels;
//                    if (orgw * orgh > maxH * maxW) {
//                        maxNumOfPixels = maxH * maxW;
//                        opts.inSampleSize = ImageUtils.computeSampleSize(opts, -1, maxNumOfPixels);
//                    }
//                }
//                opts.inJustDecodeBounds = false;
//                return BitmapFactory.decodeByteArray(data, 0, data.length, opts);
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        } finally {
//            IOUtils.closeQuietly(in);
//        }
//        return null;
//    }

    public interface ImageLoadListener {
        void onComplete(@Nullable Bitmap bp);
    }

    public static void loadImageAsync(final String thumbUrl, int width, int height, final ImageLoadListener callback) {
        if (Utils.isEmpty(thumbUrl)) {
            if (callback != null) {
                callback.onComplete(null);
            }
            return;
        }

        ImageDecodeOptions decodeOptions = ImageDecodeOptions.newBuilder()
                .build();

        ImageRequestBuilder builder = ImageRequestBuilder
                .newBuilderWithSource(Uri.parse(thumbUrl))
                .setImageDecodeOptions(decodeOptions)
                .setRotationOptions(RotationOptions.autoRotate())
                .setLocalThumbnailPreviewsEnabled(true)
                .setLowestPermittedRequestLevel(ImageRequest.RequestLevel.FULL_FETCH)
                .setProgressiveRenderingEnabled(false);
        if (width > 0 && height > 0) {
            builder.setResizeOptions(new ResizeOptions(width, height));
        }
        Fresco.getImagePipeline().fetchDecodedImage(builder.build(), Utils.mContext)
                .subscribe(new BaseBitmapDataSubscriber() {
                    @Override
                    protected void onNewResultImpl(Bitmap bitmap) {
                        if (callback != null) {
                            callback.onComplete(bitmap);
                        }
                    }

                    @Override
                    protected void onFailureImpl(DataSource<CloseableReference<CloseableImage>> dataSource) {
                        if (callback != null) {
                            callback.onComplete(null);
                        }
                    }
                }, UiThreadImmediateExecutorService.getInstance());
    }

    public interface ImageSaveListener {
        void onComplete(boolean success, String localPath);
    }

    public static void savePicture(final String remoteUrl, final String directory, final ImageSaveListener listener) {
        if (Utils.isEmpty(remoteUrl) || Utils.isEmpty(directory)) {
            if (listener != null) {
                listener.onComplete(false, null);
            }
            return;
        }
        final String filename = String.valueOf(remoteUrl.hashCode()) + ".jpg";
        final File targetFile = new File(directory, filename);

        Flowable.create(new FlowableOnSubscribe<Boolean>() {
            @Override
            public void subscribe(FlowableEmitter<Boolean> e) throws Exception {
                if (!e.isCancelled()) {
                    File picDir = new File(directory);
                    if (!picDir.exists()) {
                        //noinspection ResultOfMethodCallIgnored
                        picDir.mkdirs();
                    }

                    boolean success = false;

                    File cacheFile = getCachedImageOnDisk(UriUtils.parseUriOrNull(remoteUrl));

                    if (cacheFile == null) {
                        success = IOUtils.downloadFileSync(remoteUrl, targetFile);
                    } else {
                        InputStream in = null;
                        OutputStream out = null;
                        try {
                            in = new FileInputStream(cacheFile);
                            out = new FileOutputStream(targetFile);
                            IOUtils.copy(in, out);
                            success = true;
                        } catch (Exception ignored) {
                            success = false;
                        } finally {
                            IOUtils.closeQuietly(in, out);
                        }
                    }
                    e.onNext(success);
                    e.onComplete();
                }
            }
        }, BackpressureStrategy.BUFFER)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Boolean>() {
                    @Override
                    public void accept(Boolean aBoolean) throws Exception {
                        if (aBoolean) {
                            Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
                            Uri uri = Uri.fromFile(targetFile);
                            intent.setData(uri);
                            Utils.mContext.sendBroadcast(intent);
                            if (listener != null) {
                                listener.onComplete(true, targetFile.getAbsolutePath());
                            }
                        } else {
                            if (listener != null) {
                                listener.onComplete(false, null);
                            }
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        if (listener != null) {
                            listener.onComplete(false, null);
                        }
                    }
                });

    }

    public static void evictFromCache(Uri uri) {
        if (uri == null) {
            return;
        }
        Fresco.getImagePipeline().evictFromCache(uri);
    }


//    public static void loadImImage(Uri uri, final Callback callback) {
//        ImageRequest request = ImageRequest.fromUri(uri);
//
//        File localCache = getCacheFile(request);
//        if (localCache.exists()) {
//            try {
//                ImageFormat imageFormat = ImageFormatChecker.getImageFormat(new FileInputStream(localCache));
//                if (imageFormat == DefaultImageFormats.GIF) {
//                    callback.onCacheHit(localCache, true);
//                } else {
//                    callback.onCacheHit(localCache, false);
//                }
//            } catch (IOException e) {
//                //保存失败处理
//                e.printStackTrace();
//            }
//        } else {
//            callback.onStart(); // ensure `onStart` is called before `onProgress` and `onFinish`
//            callback.onProgress(0); // show 0 progress immediately
//
//            ImagePipeline pipeline = Fresco.getImagePipeline();
//            DataSource<CloseableReference<PooledByteBuffer>> source
//                    = pipeline.fetchEncodedImage(request, true);
//            source.subscribe(new ImageDownloadSubscriber() {
//                @Override
//                protected void onProgress(int progress) {
//                    callback.onProgress(progress);
//                }
//
//                @Override
//                protected void onSuccess(File image, boolean frescoView) {
//                    callback.onFinish();
//                    callback.onCacheMiss(image, frescoView);
//                }
//
//                @Override
//                protected void onFail(Throwable t) {
//                    t.printStackTrace();
//                }
//            },  UiThreadImmediateExecutorService.getInstance());
//        }
//    }


//    private static File getCacheFile(final ImageRequest request) {
//        FileCache mainFileCache = ImagePipelineFactory
//                .getInstance()
//                .getMainFileCache();
//        final CacheKey cacheKey = DefaultCacheKeyFactory
//                .getInstance()
//                .getEncodedCacheKey(request, false); // we don't need context, but avoid null
//        File cacheFile = request.getSourceFile();
//        // http://crashes.to/s/ee10638fb31
//        if (mainFileCache.hasKey(cacheKey) && mainFileCache.getResource(cacheKey) != null) {
//            cacheFile = ((FileBinaryResource) mainFileCache.getResource(cacheKey)).getFile();
//        }
//        return cacheFile;
//    }

}
