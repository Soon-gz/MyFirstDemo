package com.abings.baby.ui.publish;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.abings.baby.Const;
import com.abings.baby.data.DataManager;
import com.abings.baby.data.remote.BaseBusEvent;
import com.abings.baby.data.remote.custom.SubscriberJSONObject;
import com.abings.baby.injection.ActivityContext;
import com.abings.baby.ui.base.Presenter;
import com.abings.baby.utils.StringUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import javax.inject.Inject;

import rx.Observable;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by Administrator on 2016/1/18.
 */
public class PublishPresenter implements Presenter<PublishMvpView> {

    private final DataManager mDataManager;
    public Subscription mSubscription;
    private PublishMvpView mMvpView;
    private String mpk_image_news_id;
    private Context mContext;

    @Inject
    public PublishPresenter(DataManager dataManager, @ActivityContext Context context) {
        mDataManager = dataManager;
        mContext = context;
    }

    @Override
    public void attachView(PublishMvpView mvpView) {
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


    /**
     * 发布消息
     *
     * @param role
     * @param class_id
     * @param tag_id
     * @param content
     * @param imagePaths
     */
    public void publish(final String role, String class_id, String tag_id, String content, final List<String> imagePaths) {
        if (mSubscription != null) {
            mSubscription.unsubscribe();
        }
        mMvpView.showDialog("消息正在发布...");
        mSubscription = mDataManager.postMsgAdd(role, class_id, tag_id, content)
                .flatMap(new Func1<JSONObject, Observable<String>>() {
                    @Override
                    public Observable<String> call(JSONObject result) {
                        if (result.has("state")) {
                            try {
                                int state = Integer.valueOf(result.getString("state"));
                                switch (state) {
                                    case 0:
                                        String pk_image_news_id = result.getString("pk_image_news_id");
                                        if (!StringUtils.isEmpty(pk_image_news_id)) {
                                            mpk_image_news_id = pk_image_news_id;
                                            String[] images = new String[imagePaths.size()];
                                            for (int i = 0; i < images.length; i++) {
                                                images[i] = imagePaths.get(i);
                                            }
                                            return Observable.from(images);
                                        }
                                        break;
                                    case 1:
                                        mMvpView.onTokenError();
                                        break;
                                    case 3:
                                        mMvpView.showError("发布消息失败，请稍后再试");
                                        break;
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                        return null;
                    }
                })
                .flatMap(new Func1<String, Observable<JSONObject>>() {
                    @Override
                    public Observable<JSONObject> call(String s) {
                        if (!StringUtils.isEmpty(s)) {
                            File file = getCompressImage(s);
                            if (file != null) {
                                return mDataManager.addImage(role, mpk_image_news_id, file.getPath(), "image.jpg");
                            }
                        }
                        return null;
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())//
                .subscribeOn(Schedulers.io())//
                .subscribe(new SubscriberJSONObject(mContext) {
                    @Override
                    protected void onNextJSONObject(JSONObject jsonObject) throws JSONException {

                    }

                    @Override
                    public void onCompleted() {
                        mMvpView.dialogDismiss();
                        mMvpView.showMessage("发布消息成功");
                        mMvpView.uploadCompleted();
                        File file = new File(Const.imageCache, "image.jpg");
                        if (file.exists()) file.delete();
                    }

                    @Override
                    protected void onErrorState3() {
                        mMvpView.showError("发送图片失败，请稍后再试");
                    }
                });
    }


    public void onEventMainThread(BaseBusEvent event) {
//        Log.e("ab", "控制器里收到BUS数据" + event.mes);
    }


    /**
     * 获取压缩图片
     *
     * @param path
     * @return
     */
    private File getCompressImage(String path) {
        File file = new File(path);
        if (file.exists()) {
            //图片存在
            Bitmap resultBitmap = null;
            Bitmap bmp = BitmapFactory.decodeFile(path);
            int actualWidth;
            int actualHeight;
            double ratioWidth = 960 * 1.0 / bmp.getWidth();
            double ratioHeight = 960 * 1.0 / bmp.getHeight();
            double ratio = ratioHeight > ratioWidth ? ratioWidth : ratioHeight;
            actualWidth = (int) (bmp.getWidth() * ratio);
            actualHeight = (int) (bmp.getHeight() * ratio);
            if (actualWidth < bmp.getWidth() || actualHeight < bmp.getHeight()) {
                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inJustDecodeBounds = true;
                BitmapFactory.decodeFile(file.getAbsolutePath(), options);
                options.inSampleSize = calculateInSampleSize(options, actualWidth, actualHeight);
                options.inJustDecodeBounds = false;
                resultBitmap = BitmapFactory.decodeFile(file.getAbsolutePath(), options);
            } else {
                resultBitmap = bmp;
            }

            ByteArrayOutputStream output = new ByteArrayOutputStream();
            int options = 100;
            int maxSize = 1024 * 300;
            do {
                output.reset();
                resultBitmap.compress(Bitmap.CompressFormat.JPEG, options, output);
                options -= 10;
            } while (output.toByteArray().length > maxSize && options != 10);

            File dirFile = new File(Const.imageCache);
            if (!dirFile.exists()) {
                dirFile.mkdirs();
            }
            File imageCacheFile = new File(dirFile, "image.jpg");
            if (imageCacheFile.exists()) {
                imageCacheFile.delete();
            }

            try {
                FileOutputStream e = new FileOutputStream(imageCacheFile);
                e.write(output.toByteArray());
                e.close();
                return imageCacheFile;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    private int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        if (reqWidth == -1) {
            reqWidth = options.outWidth;
        }

        if (reqHeight == -1) {
            reqHeight = options.outHeight;
        }

        int height = options.outHeight;
        int width = options.outWidth;
        int inSampleSize = 1;
        if (height > reqHeight || width > reqWidth) {
            if (width > height) {
                inSampleSize = Math.round((float) height / (float) reqHeight);
            } else {
                inSampleSize = Math.round((float) width / (float) reqWidth);
            }
        }

        return inSampleSize;
    }
}
