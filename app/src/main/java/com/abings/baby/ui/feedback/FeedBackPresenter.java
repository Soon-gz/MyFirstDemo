package com.abings.baby.ui.feedback;

import android.content.Context;
import android.util.Log;

import com.abings.baby.data.DataManager;
import com.abings.baby.data.model.NewInfo;
import com.abings.baby.data.remote.BaseBusEvent;
import com.abings.baby.data.remote.custom.ApiException;
import com.abings.baby.data.remote.custom.SubscriberJSONObject;
import com.abings.baby.injection.ActivityContext;
import com.abings.baby.ui.base.Presenter;
import com.abings.baby.utils.DateUtil;
import com.socks.library.KLog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by Administrator on 2016/1/18.
 */
public class FeedBackPresenter implements Presenter<FeedBackMvpView> {

    private final DataManager mDataManager;
    public Subscription mSubscription;
    private FeedBackMvpView mMvpView;
    private JSONObject mCachedRibots;
    private Context mContext;

    @Inject
    public FeedBackPresenter(DataManager dataManager, @ActivityContext Context context) {
        mDataManager = dataManager;
        mContext = context;
    }

    @Override
    public void attachView(FeedBackMvpView mvpView) {
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

    public void feebackAdd(String msg) {
        mSubscription = mDataManager.feebackAdd(msg).observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io()).subscribe(new SubscriberJSONObject(mContext,false) {
            @Override
            protected void onNextJSONObject(JSONObject result) throws JSONException {
                mMvpView.feedBackSucceed(true);
            }

            @Override
            protected void onErrorState1(JSONObject jsonObject) {
                mMvpView.feedBackSucceed(false);
            }
        });
    }

    public void onEventMainThread(BaseBusEvent event) {
        Log.e("ab", "控制器里收到BUS数据" + event.mes);
    }


}
