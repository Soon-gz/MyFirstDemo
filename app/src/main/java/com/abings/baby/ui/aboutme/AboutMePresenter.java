package com.abings.baby.ui.aboutme;

import android.content.Context;

import com.abings.baby.data.DataManager;
import com.abings.baby.data.remote.custom.SubscriberJSONObject;
import com.abings.baby.injection.ActivityContext;
import com.abings.baby.ui.base.Presenter;
import com.abings.baby.utils.StringUtils;

import org.json.JSONException;
import org.json.JSONObject;

import javax.inject.Inject;

import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Administrator on 2016/1/18.
 */
public class AboutMePresenter implements Presenter<AboutMeMvpView> {

    private final DataManager mDataManager;
    public Subscription mSubscription;
    private AboutMeMvpView mMvpView;
    private JSONObject mCachedRibots;
    private Context mContext;

    @Inject
    public AboutMePresenter(DataManager dataManager, @ActivityContext Context context) {
        mDataManager = dataManager;
        mContext = context;
    }

    @Override
    public void attachView(AboutMeMvpView mvpView) {
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


    // 上传多文件示例
    public void uploadImage(String path, boolean isTeacher) {
        Observable<JSONObject> jsonObjectObservable = isTeacher ? mDataManager.uploadTeacherPhoto(path) : mDataManager.uploadMemberPhoto(path);
        mSubscription = jsonObjectObservable
                .subscribeOn(Schedulers.io())//
                .observeOn(AndroidSchedulers.mainThread())//
                .subscribe(new SubscriberJSONObject(mContext) {
                    @Override
                    protected void onNextJSONObject(JSONObject result) throws JSONException {
                        String name = result.getString("filename");
                        if (!StringUtils.isEmpty(name)) {
                            mMvpView.updateHeader(name);
                        }
                    }
                });
    }

}
