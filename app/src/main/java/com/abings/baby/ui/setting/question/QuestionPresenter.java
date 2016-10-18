package com.abings.baby.ui.setting.question;

import android.util.Log;

import com.abings.baby.data.DataManager;
import com.abings.baby.data.local.PhotoUpAlbumHelper;
import com.abings.baby.data.remote.BaseBusEvent;
import com.abings.baby.ui.base.Presenter;

import org.json.JSONObject;

import javax.inject.Inject;

import rx.Subscription;

/**
 * Created by Administrator on 2016/1/18.
 */
public class QuestionPresenter implements Presenter<QuestionMvpView> {

    private final DataManager mDataManager;
    public Subscription mSubscription;
    private QuestionMvpView mMvpView;
    private JSONObject mCachedRibots;

    @Inject
    public QuestionPresenter(DataManager dataManager, PhotoUpAlbumHelper photoUpAlbumHelper) {
        mDataManager = dataManager;
    }

    @Override
    public void attachView(QuestionMvpView mvpView) {
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


    public void onEventMainThread(BaseBusEvent event) {
        Log.e("ab", "控制器里收到BUS数据" + event.mes);
    }

}
