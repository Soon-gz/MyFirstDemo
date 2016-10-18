package com.abings.baby.ui.aboutme.association;

import android.content.Context;

import com.abings.baby.data.DataManager;
import com.abings.baby.data.model.RelativePerson;
import com.abings.baby.data.remote.custom.ApiException;
import com.abings.baby.data.remote.custom.SubscriberJSONObject;
import com.abings.baby.injection.ActivityContext;
import com.abings.baby.ui.base.Presenter;

import org.json.JSONException;
import org.json.JSONObject;

import javax.inject.Inject;

import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by zwj on 2016/7/26.
 * 子账号
 */
public class AssociationPresenter implements Presenter<AssociationMvpView> {
    private AssociationMvpView mMvpView;
    private final DataManager mDataManager;
    private Subscription mSubscription;
    private Context mContext;

    @Inject
    public AssociationPresenter(DataManager dataManager, @ActivityContext Context context) {
        this.mDataManager = dataManager;
        mContext = context;
    }

    @Override
    public void attachView(AssociationMvpView mvpView) {
        mMvpView = mvpView;
    }

    @Override
    public void detachView() {
        if (mMvpView.bindEvents()) {
            mDataManager.getEventBus().unregister(this);
        }
        mMvpView = null;
        releaseSubscription();
    }

    private void releaseSubscription() {
        if (mSubscription != null) mSubscription.unsubscribe();
    }

    public void addRelativePerson(final String name, final String relation_desc, final String mobile_phone) {
        releaseSubscription();
        mSubscription = mDataManager.addAssociation(name, relation_desc, mobile_phone).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new SubscriberJSONObject(mContext) {
            @Override
            protected void onErrorUnKnown() {
                super.onErrorUnKnown();
                mMvpView.showLoadingProgress(false);
                mMvpView.showError("添加子账号失败！");
            }

            @Override
            protected void onErrorState3() {
                mMvpView.showError("其它错误！");
            }

            @Override
            protected void onNextJSONObject(JSONObject jsonObject) throws JSONException {
                mMvpView.showError("添加用户成功！");
                RelativePerson.ResultBean resultBean = new RelativePerson.ResultBean();
                resultBean.setName(name);
                resultBean.setMobile_phone(mobile_phone);
                resultBean.setRelation_desc(relation_desc);
                mMvpView.addSuccess(resultBean);
            }
        });
    }

}
