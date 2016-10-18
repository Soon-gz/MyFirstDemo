package com.abings.baby.ui.message.center.detail;

import android.content.Context;

import com.abings.baby.data.DataManager;
import com.abings.baby.data.local.PhotoUpAlbumHelper;
import com.abings.baby.data.remote.custom.SubscriberJSONObject;
import com.abings.baby.injection.ActivityContext;
import com.abings.baby.ui.base.Presenter;
import com.socks.library.KLog;

import org.json.JSONException;
import org.json.JSONObject;

import javax.inject.Inject;

import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Administrator on 2016/1/18.
 */
public class MessageDetailPresenter implements Presenter<MessageDetailMvpView> {

    private final DataManager mDataManager;
    public Subscription mSubscription;
    private MessageDetailMvpView mMvpView;
    private Context mContext;


    @Inject
    public MessageDetailPresenter(DataManager dataManager, PhotoUpAlbumHelper photoUpAlbumHelper, @ActivityContext Context context) {
        mDataManager = dataManager;
        mContext = context;
    }

    @Override
    public void attachView(MessageDetailMvpView mvpView) {
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

    public void messageReaded(String pk_message_id) {
        mSubscription = mDataManager.messageReaded(pk_message_id).observeOn(AndroidSchedulers.mainThread()).subscribeOn
                (Schedulers.io()).subscribe(new SubscriberJSONObject(mContext) {
            @Override
            protected void onNextJSONObject(JSONObject jsonObject) throws JSONException {
                
            }
            //0 已读 2未读
        });
    }


}
