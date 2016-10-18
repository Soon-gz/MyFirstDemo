package com.abings.baby.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.ImageView;

import com.abings.baby.R;
import com.abings.baby.data.remote.RetrofitUtils;
import com.bumptech.glide.Glide;
import com.nostra13.universalimageloader.cache.disc.naming.HashCodeFileNameGenerator;
import com.nostra13.universalimageloader.cache.memory.impl.LruMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.socks.library.KLog;

/**
 * 图片加载
 */
public class ImageLoaderUtil {
    private static final String TIME_FIRST = "clear_disk_cache_first";
    private static final String TIME = "clear_disk_cache_time";
    public static Context mContext;

    public static void init(Context context) {
        mContext = context;
        DisplayImageOptions options = new DisplayImageOptions.Builder().cacheOnDisk(true).bitmapConfig(Bitmap.Config
                .RGB_565).cacheInMemory(true).build();
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(context).memoryCacheExtraOptions(720,
                1280) // default = device screen dimensions 内存缓存文件的最大长宽
                .threadPoolSize(3) // default  线程池内加载的数量
                .threadPriority(Thread.NORM_PRIORITY - 2) // default 设置当前线程的优先级
                .denyCacheImageMultipleSizesInMemory().memoryCache(new LruMemoryCache(2 * 1024 * 1024)) //可以通过自己的内存缓存实现
                .memoryCacheSize(2 * 1024 * 1024)  // 内存缓存的最大值
                .memoryCacheSizePercentage(13) // default
                .diskCacheSize(50 * 1024 * 1024) // 50 Mb sd卡(本地)缓存的最大值
                .diskCacheFileCount(100)  // 可以缓存的文件数量
                        // default为使用HASHCODE对UIL进行加密命名， 还可以用MD5(new Md5FileNameGenerator())加密
                .diskCacheFileNameGenerator(new HashCodeFileNameGenerator()).defaultDisplayImageOptions(options) //
                        // default
//                .writeDebugLogs() // 打印debug log
                .build(); //开始构建
        ImageLoader.getInstance().init(config);
    }

    public static void display(String imgUrl, ImageView img) {
        if (StringUtils.isEmpty(imgUrl)) {
            return;
        }
        KLog.e("imgUrl = " + imgUrl);
        ImageLoader.getInstance().displayImage(imgUrl, img);
    }

    public static void displayDefault(String imgUrl, ImageView img) {
        if (StringUtils.isEmpty(imgUrl)) {
            return;
        }
        DisplayImageOptions options = new DisplayImageOptions.Builder().cacheInMemory(false).cacheOnDisc(false).build();
        ImageLoader.getInstance().displayImage(imgUrl, img, options);
    }

    public static Bitmap loadImageSync(String imgUrl) {
        if (StringUtils.isEmpty(imgUrl)) {
            return null;
        }
        KLog.e("imgUrl = " + imgUrl);
        DisplayImageOptions options = new DisplayImageOptions.Builder().bitmapConfig(Bitmap.Config.RGB_565)
                .imageScaleType(ImageScaleType.NONE).cacheInMemory(false).cacheOnDisk(false).build();
        return ImageLoader.getInstance().loadImageSync(imgUrl, options);
    }


    /**
     *
     * @param context
     * @param baseUrl 基础链接
     * @param imageName 图片名称
     * @param placeholderImageId 占位图片
     * @param errImageId 错误图片
     * @param imageView
     */
    public static void loadHeadImage(@NonNull Context context,@NonNull  String baseUrl, String imageName, int placeholderImageId,int errImageId, @NonNull ImageView imageView) {
        if (imageName == null || imageName.isEmpty()) {
            Glide.with(context).load(placeholderImageId).dontAnimate().into(imageView);
        } else {
            Glide.with(context).load(baseUrl + imageName).placeholder(placeholderImageId).error(errImageId).dontAnimate().into(imageView);
        }
    }

    /**
     * 通讯录中头像的图片
     * @param context
     * @param baseUrl 基础链接
     * @param imageName 图片名字
     * @param placeholderImageId 站位图片
     * @param errImageId 图片下载错误展示图片
     * @param imageView 图片控件
     */
    public static void loadContactsHeadImage(@NonNull Context context,@NonNull String baseUrl,String imageName,int placeholderImageId,int errImageId,@NonNull ImageView imageView){

        if(imageName==null || imageName.isEmpty()){
            Glide.with(context).load(errImageId).dontAnimate().thumbnail(0.1f).into(imageView);
        } else {
            Glide.with(context).load(baseUrl + imageName).placeholder(placeholderImageId).error(errImageId).dontAnimate()
                    .thumbnail(0.1f).into(imageView);
        }

    }

    /**
     *
     * @param context
     * @param baseUrl 基础链接
     * @param imageName 图片名称
     * @param placeholderImageId 占位图片
     * @param errImageId 错误图片
     * @param imageView
     */
    public static void loadAttendHeadImage(@NonNull Context context,@NonNull  String baseUrl, String imageName, int placeholderImageId,int errImageId, @NonNull ImageView imageView) {
        if (imageName == null || imageName.isEmpty()) {
            Glide.with(context).load(placeholderImageId).dontAnimate().into(imageView);
        } else {
            Glide.with(context).load(baseUrl + imageName).placeholder(placeholderImageId).error(errImageId).dontAnimate().into(imageView);
        }
    }


}
