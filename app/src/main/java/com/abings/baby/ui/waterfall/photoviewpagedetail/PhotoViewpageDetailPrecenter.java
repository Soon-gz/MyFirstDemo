package com.abings.baby.ui.waterfall.photoviewpagedetail;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.view.View;

import com.abings.baby.Const;
import com.abings.baby.WineApplication;
import com.abings.baby.data.DataManager;
import com.abings.baby.data.model.PhotoInfo;
import com.abings.baby.data.remote.RetrofitUtils;
import com.abings.baby.data.remote.custom.Func1JSONObject;
import com.abings.baby.data.remote.custom.SubscriberClass;
import com.abings.baby.data.remote.custom.SubscriberJSONObject;
import com.abings.baby.injection.ActivityContext;
import com.abings.baby.ui.base.Presenter;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import javax.inject.Inject;

import rx.Observable;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by HaomingXu on 2016/7/20.
 */
public class PhotoViewpageDetailPrecenter implements Presenter<PhotoViewpageMvpView> {

    private final DataManager mDataManager;
    public Subscription mSubscription;
    private PhotoViewpageMvpView mMvpView;
    private Context context;

    @Inject
    public PhotoViewpageDetailPrecenter( DataManager mDataManager,@ActivityContext Context context){
        this.mDataManager = mDataManager;
        this.context = context;
    }

    @Override
    public void attachView(PhotoViewpageMvpView mvpView) {
        mMvpView = mvpView;
    }

    @Override
    public void detachView() {
        if (mMvpView.bindEvents()){
            mDataManager.getEventBus().unregister(this);
        }
        mMvpView = null;
        if (mSubscription != null) mSubscription.unsubscribe();
    }
    /**
     *加载图片
     *@author Shuwen
     *created at 2016/8/10 10:53
     */
    public void loadData(String pic_id) {

        mSubscription = mDataManager.postPhotoInfo(pic_id)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new SubscriberClass<PhotoInfo>(context,PhotoInfo.class,false) {
                    @Override
                    protected void onNextList(List<PhotoInfo> photoInfos) {
                        mMvpView.updateView(photoInfos.get(0));
                    }
                });
    }

    /**
     *点击喜欢
     *@author Shuwen
     *created at 2016/8/10 10:54
     */
    public void likeAddCancle(final PhotoInfo photoInfo) {
        mSubscription = mDataManager.postLikeAddCancle(photoInfo.getFk_image_news_id())
                .flatMap(new Func1JSONObject() {
                    @Override
                    protected Observable<JSONObject> callJSONObject(JSONObject jsonObject) throws JSONException {
                        return mDataManager.postPhotoInfo(photoInfo.getPk_image_news_image_id());
                    }

                    @Override
                    protected Observable<JSONObject> onErrorState2() {
                        return mDataManager.postPhotoInfo(photoInfo.getPk_image_news_image_id());
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new SubscriberClass<PhotoInfo>(context, PhotoInfo.class, false) {
                    @Override
                    protected void onNextList(List<PhotoInfo> photoInfos) {
                        mMvpView.updateView(photoInfos.get(0));
                    }
                });
    }


    /**
     *保存图片到个人相册（测试成功）
     *@author Shuwen
     *created at 2016/7/29 16:47
     */
    public void saveImgToBaby(String fk_grade_class_id,String fk_tag_id,String content,String pk_image_news_image_id){
        mSubscription = mDataManager.saveImgToBaby(fk_grade_class_id, fk_tag_id, content, pk_image_news_image_id)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new SubscriberJSONObject(context,"正在保存中..."){
                    @Override
                    protected void onNextJSONObject(JSONObject jsonObject) throws JSONException {
                        mMvpView.showMessage("保存图片成功！");
                    }

                    @Override
                    protected void onErrorUnKnown() {
                        mMvpView.showMessage("图片保存失败！");
                    }
                });
    }



    /**
     *删除单张照片
     *@author Shuwen
     *created at 2016/7/29 17:03
     */
    public void deletePic(final PhotoInfo mInfo) {

        mSubscription = mDataManager.Fmsg_image_news_image_del(mInfo.getPk_image_news_image_id())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new SubscriberJSONObject(context,false){
                    @Override
                    protected void onNextJSONObject(JSONObject jsonObject) throws JSONException {
                        mMvpView.showMessage("删除图片成功！");
                        mMvpView.finishActivity();
                    }

                    @Override
                    protected void onErrorUnKnown() {
                        mMvpView.showMessage("删除图片失败！");
                    }
                });
    }




    /**
     *使用imaloader下载图片
     *@author Shuwen
     *created at 2016/8/10 10:54
     */
    void saveImage(final String url) {
        DisplayImageOptions options = new DisplayImageOptions.Builder().bitmapConfig(Bitmap.Config.RGB_565)
                .imageScaleType(ImageScaleType.NONE).cacheInMemory(false).cacheOnDisk(false).build();
        ImageLoader.getInstance().loadImage(RetrofitUtils.BASE_IMG_URL + url, options, new ImageLoadingListener() {
            @Override
            public void onLoadingStarted(String imageUri, View view) {
                mMvpView.showDialog("正在下载...");
            }

            @Override
            public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
                mMvpView.dialogDismiss();
                mMvpView.showMessage("下载失败");
            }

            @Override
            public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                saveImageToGallery(WineApplication.getInstance(), loadedImage, Const.basePath, "image", url);
                mMvpView.dialogDismiss();
                mMvpView.showMessage("保存成功!" + "路径：" + Const.basePath + "/image/");
            }

            @Override
            public void onLoadingCancelled(String imageUri, View view) {
                mMvpView.dialogDismiss();
            }
        });
    }


    public static void saveImageToGallery(Context context, Bitmap bmp, String path, String folder, String name) {
        // 首先保存图片
        File appDir = new File(path, folder);
        if (!appDir.exists()) {
            appDir.mkdir();
        }
        String fileName = name;
        File file = new File(appDir, fileName);
        try {
            FileOutputStream fos = new FileOutputStream(file);
            bmp.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            fos.flush();
            fos.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        // 其次把文件插入到系统图库
        try {
            MediaStore.Images.Media.insertImage(context.getContentResolver(),
                    file.getAbsolutePath(), fileName, null);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        // 最后通知图库更新
        context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.parse("file://" + path)));
    }

}
