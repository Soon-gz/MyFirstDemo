package com.abings.baby.ui.contacts.detail;

import android.app.Activity;
import android.content.Context;
import android.util.Log;

import com.abings.baby.data.DataManager;
import com.abings.baby.data.local.PhotoUpAlbumHelper;
import com.abings.baby.data.remote.BaseBusEvent;
import com.abings.baby.injection.ActivityContext;
import com.abings.baby.ui.base.Presenter;

import javax.inject.Inject;

import rx.Subscription;

/**
 * Created by Administrator on 2016/1/18.
 */
public class ContactDetailPresenter implements Presenter<ContactDetailMvpView> {

    private final DataManager mDataManager;
    public Subscription mSubscription;
    private ContactDetailMvpView mMvpView;
    private Activity mActivity;
    private Context mContext;

    @Inject
    public ContactDetailPresenter(DataManager dataManager, PhotoUpAlbumHelper photoUpAlbumHelper, @ActivityContext Context context) {
        mDataManager = dataManager;
        mContext = context;
    }

    @Override
    public void attachView(ContactDetailMvpView mvpView) {
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
