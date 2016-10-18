package com.abings.baby.ui.publishvideo;

import android.content.Context;

import com.abings.baby.WineApplication;
import com.abings.baby.data.DataManager;
import com.abings.baby.data.remote.custom.Func1JSONObject;
import com.abings.baby.data.remote.custom.SubscriberJSONObject;
import com.abings.baby.injection.ActivityContext;
import com.abings.baby.ui.base.Presenter;
import com.abings.baby.utils.StringUtils;
import com.abings.baby.utils.TLog;

import org.json.JSONException;
import org.json.JSONObject;

import javax.inject.Inject;

import rx.Observable;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by zwj on 2016/7/13.
 */
public class PublishVideoPresenter implements Presenter<PublishVideoMvpView> {

    private final DataManager mDataManager;
    public Subscription mSubscription;
    private PublishVideoMvpView mMvpView;
    private Context context;

    @Inject
    public PublishVideoPresenter(DataManager mDataManager,@ActivityContext Context context) {
        this.mDataManager = mDataManager;
        this.context = context;
    }

    @Override
    public void attachView(PublishVideoMvpView mvpView) {
        this.mMvpView = mvpView;
        if (mMvpView.bindEvents()) {
            mDataManager.getEventBus().register(this);
        }
    }

    @Override
    public void detachView() {
        if (mMvpView.bindEvents()) {
            mDataManager.getEventBus().unregister(this);
        }
        if (mSubscription != null) mSubscription.unsubscribe();
        mMvpView = null;
    }

    public void publishVideo(String content, final String videoThumbPath, final String videoPath) {
        final String role = WineApplication.getInstance().getRole();
        String fk_grade_class_id;
        String tag_id = "4";
        if (WineApplication.getInstance().isTeacher()) {
            fk_grade_class_id = WineApplication.getInstance().getNowClass().getPk_grade_class_id();
        } else {
            fk_grade_class_id = WineApplication.getInstance().getBaby().getFk_grade_class_id();
        }
        mSubscription = mDataManager.postMsgAdd(role, fk_grade_class_id, tag_id, content)
                .flatMap(new Func1JSONObject() {
                    @Override
                    protected Observable<JSONObject> callJSONObject(JSONObject jsonObject) throws JSONException {
                        String pk_image_news_id = jsonObject.getString("pk_image_news_id");
                        if (!StringUtils.isEmpty(pk_image_news_id)) {
                            String imageFileName = getFileName(videoThumbPath);
                            return mDataManager.addImage(role, pk_image_news_id, videoThumbPath, imageFileName);
                        }
                        return null;
                    }
                })
                .flatMap(new Func1JSONObject() {
                    @Override
                    protected Observable<JSONObject> callJSONObject(JSONObject jsonObject) throws JSONException {
                        String pk_image_news_id = jsonObject.getString("pk_image_news_image_id");
                        if (!StringUtils.isEmpty(pk_image_news_id)) {
                            String videoFileName = getFileName(videoPath);
                            return mDataManager.addVideo(role, pk_image_news_id, videoPath, videoFileName);
                        }
                        return null;
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SubscriberJSONObject(context,"正在上传视频..."){
                    @Override
                    protected void dataLog(JSONObject jsonObject) {
                        super.dataLog(jsonObject);
                        TLog.getInstance().i(jsonObject.toString());
                    }
                    @Override
                    protected void onNextJSONObject(JSONObject jsonObject) throws JSONException {
                        mMvpView.publishSuccess();
                    }

                    @Override
                    protected void onErrorToken() {
                        mMvpView.showError("上传失败");
                    }

                    @Override
                    protected void onErrorUnKnown() {
                        mMvpView.showError("上传失败");
                    }
                });
    }

    private String getFileName(String path) {
        return path.substring(path.lastIndexOf("/") + 1);
    }
}
