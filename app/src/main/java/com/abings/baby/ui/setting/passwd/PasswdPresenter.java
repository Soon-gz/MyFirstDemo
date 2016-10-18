package com.abings.baby.ui.setting.passwd;

import android.content.Context;
import android.util.Log;

import com.abings.baby.WineApplication;
import com.abings.baby.data.DataManager;
import com.abings.baby.data.local.PhotoUpAlbumHelper;
import com.abings.baby.data.remote.BaseBusEvent;
import com.abings.baby.data.remote.custom.SubscriberJSONObject;
import com.abings.baby.injection.ActivityContext;
import com.abings.baby.ui.base.Presenter;

import org.json.JSONException;
import org.json.JSONObject;

import javax.inject.Inject;

import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Administrator on 2016/1/18.
 */
public class PasswdPresenter implements Presenter<PasswdMvpView> {

    private final DataManager mDataManager;
    public Subscription mSubscription;
    private PasswdMvpView mMvpView;
    private JSONObject mCachedRibots;
    private Context context;

    @Inject
    public PasswdPresenter(DataManager dataManager, PhotoUpAlbumHelper photoUpAlbumHelper,@ActivityContext Context context) {
        mDataManager = dataManager;
        this.context = context;
    }

    @Override
    public void attachView(PasswdMvpView mvpView) {
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
     *修改密码，在未忘记密码的情况下
     *@author Shuwen
     *created at 2016/7/29 16:18
     */
    public void changePassWord(String mobile_phone,String password,String newPassword){
        mMvpView.showLoadingProgress(true);
        mSubscription = mDataManager.postChangePassword(mobile_phone,password,newPassword,WineApplication.getInstance().getToken(), WineApplication.getInstance().getRole())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SubscriberJSONObject(context,"正在修改密码中..."){
                    @Override
                    protected void onNextJSONObject(JSONObject jsonObject) throws JSONException {

                        mMvpView.showMessage("密码修改成功！");
                    }

                    @Override
                    protected void onErrorToken() {
                        mMvpView.showMessage("修改密码失败,原密码错误！");
                    }

                    @Override
                    protected void onErrorUnKnown() {
                        mMvpView.showMessage("修改密码失败！");
                    }

                    @Override
                    protected void onErrorState2() {
                        mMvpView.showMessage("修改密码失败！");
                    }

                    @Override
                    protected void onErrorState3() {
                        mMvpView.showMessage("修改密码失败！");
                    }
                });
    }


    public void onEventMainThread(BaseBusEvent event) {
        Log.e("ab", "控制器里收到BUS数据" + event.mes);
    }

}
