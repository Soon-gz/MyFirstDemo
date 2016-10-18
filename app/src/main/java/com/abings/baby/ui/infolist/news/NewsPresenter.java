package com.abings.baby.ui.infolist.news;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;

import com.abings.baby.WineApplication;
import com.abings.baby.data.DataManager;
import com.abings.baby.data.local.PhotoUpAlbumHelper;
import com.abings.baby.data.model.NewsDetail;
import com.abings.baby.data.remote.RetrofitUtils;
import com.abings.baby.data.remote.custom.SubscriberJSONObject;
import com.abings.baby.injection.ActivityContext;
import com.abings.baby.ui.base.Presenter;
import com.google.gson.Gson;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.socks.library.KLog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Administrator on 2016/1/18.
 */
public class NewsPresenter implements Presenter<NewsMvpView> {

    private final DataManager mDataManager;
    public Subscription mSubscription;
    private NewsMvpView mMvpView;
    private Context mContext;


    @Inject
    public NewsPresenter(DataManager dataManager, PhotoUpAlbumHelper photoUpAlbumHelper, @ActivityContext Context context) {
        mDataManager = dataManager;
        mContext = context;
    }

    @Override
    public void attachView(NewsMvpView mvpView) {
        mMvpView = mvpView;
        if (mMvpView.bindEvents()) {
            mDataManager.getEventBus().register(this);
        }
    }

    @Override
    public void detachView() {
        if (mMvpView.bindEvents()) {
            mDataManager.getEventBus().unregister(this);
        }
        mMvpView = null;
        if (mSubscription != null) mSubscription.unsubscribe();

    }

    public void likeAddCancle(String pic_id) {
        mSubscription = mDataManager.postLikeAddCancle(pic_id).observeOn(AndroidSchedulers.mainThread()).subscribeOn
                (Schedulers.io()).subscribe(new SubscriberJSONObject(mContext){
            @Override
            protected void onNextJSONObject(JSONObject jsonObject) throws JSONException {
                mMvpView.updateFavorite(true);
            }

            @Override
            protected void onErrorState2() {
                mMvpView.updateFavorite(false);
            }
        });
    }

    public void Fmsg_del(String pk_image_news_id) {
        mSubscription = mDataManager.Fmsg_del(pk_image_news_id).observeOn(AndroidSchedulers.mainThread()).subscribeOn
                (Schedulers.io()).subscribe(new SubscriberJSONObject(mContext) {
            protected void onNextJSONObject(JSONObject jsonObject) throws JSONException {
                mMvpView.showMessage("删除成功");
            }

            @Override
            protected void onErrorState2() {
                mMvpView.showMessage("删除失败");
            }
        });
    }

    public void loadData(String pk_image_news_id) {
        mSubscription = mDataManager.postNewsDetail(pk_image_news_id).observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io()).subscribe(new SubscriberJSONObject(mContext) {
                    @Override
                    protected void onNextJSONObject(JSONObject result) throws JSONException {
                        Gson gson = new Gson();
                        JSONArray array = result.getJSONArray("result");
                        if (array != null && array.length() > 0) {
                            JSONObject obj = array.getJSONObject(0);
                            NewsDetail data = new NewsDetail();
                            data.setContent(obj.getString("content"));
                            data.setCreate_datetime(obj.getString("create_datetime"));
                            data.setImage_count(obj.getString("image_count"));
                            data.setLike_count(obj.getString("like_count"));
                            data.setPk_image_news_id(obj.getString("pk_image_news_id"));
                            data.setName(obj.getString("name"));
                            if (Integer.valueOf(data.getImage_count()) > 0 && !obj.isNull("images")) {
                                JSONArray imagesArray = obj.getJSONArray("images");
                                KLog.e("imagesArray .size() = " + imagesArray.length());
                                if (imagesArray != null && imagesArray.length() > 0) {
                                    List<NewsDetail.ImagesBean> list1 = new ArrayList<NewsDetail
                                            .ImagesBean>();
                                    for (int j = 0; j < imagesArray.length(); j++) {
                                        NewsDetail.ImagesBean data1 = gson.fromJson(imagesArray.getJSONObject
                                                (j).toString(), NewsDetail.ImagesBean.class);
                                        list1.add(data1);
                                    }
                                    data.setImages(list1);
                                    KLog.e("ImagesBean .size() = " + list1.size());
                                }
                            }
                            if (data != null) {
                                mMvpView.updateView(data);
                            }
                        }
                    }
                    @Override
                    protected void onErrorState2() {
                        mMvpView.showData(null, false);
                        mMvpView.showError("没有数据！");
                    }
                });
    }

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
                mMvpView.showMessage("下载失败");
            }

            @Override
            public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                saveImageToGallery(WineApplication.getInstance(), loadedImage, Environment.getExternalStorageDirectory().getAbsolutePath(), "HelloBaby", url);
                mMvpView.showMessage("保存成功");
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

    /**
     * 删除动态日志
     *
     * @author Shuwen
     * created at 2016/8/2 11:39
     */
    public void deletePic(String pk_image_news_id) {
        mMvpView.showDialog("正在删除中...");
        mSubscription = mDataManager.deletePic(pk_image_news_id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SubscriberJSONObject(mContext) {
                    @Override
                    protected void onNextJSONObject(JSONObject jsonObject) throws JSONException {
                        mMvpView.showMessage("删除成功！");
                        mMvpView.finishActivity();
                    }

                    @Override
                    protected void onErrorToken() {
                        mMvpView.showMessage("用户Token错误！");
                        mMvpView.onTokenError();
                    }

                    @Override
                    protected void onErrorState2() {
                        mMvpView.showMessage("删除失败！");
                    }

                    @Override
                    protected void onErrorState3() {
                        mMvpView.showMessage("服务器异常！");
                    }
                });
    }

}
