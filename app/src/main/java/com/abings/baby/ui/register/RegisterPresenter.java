package com.abings.baby.ui.register;

import android.app.Activity;
import android.content.Context;
import android.util.Log;

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
public class RegisterPresenter implements Presenter<RegisterMvpView> {

    private final DataManager mDataManager;
    public Subscription mSubscription;
    private RegisterMvpView mMvpView;
    private Activity mActivity;
    private Context context;

    @Inject
    public RegisterPresenter(DataManager dataManager, PhotoUpAlbumHelper photoUpAlbumHelper,@ActivityContext Context context) {
        mDataManager = dataManager;
        this.context = context;
    }

    @Override
    public void attachView(RegisterMvpView mvpView) {
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
 * 修改密码
 *统一网络请求使用retrofit2.0（目前只有用户端可以忘记密码，教师端会出错）
 *@author Shuwen
 *created at 2016/7/29 15:34
 */
    public void chagePassWord( String mobile_phone,String password,String sms_code){
        mSubscription = mDataManager.forgetPassword(mobile_phone,password,sms_code)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new SubscriberJSONObject(context,false){
                    @Override
                    protected void onNextJSONObject(JSONObject jsonObject) throws JSONException {
                        mMvpView.showMessage("恭喜,密码修改成功！");
                        mMvpView.finishActivity();
                    }

                    @Override
                    protected void onErrorToken() {
                        mMvpView.showMessage("对不起,查无此号,请先到相关幼儿园注册！");
                    }
                });
    }

    /**
     * 获得验证码
     *统一网络请求，使用retrofit2.0(测试成功)
     *@author Shuwen
     *created at 2016/7/29 15:56
     */
    public void getSmsCode(String mobile_phone){
        mSubscription = mDataManager.getSmsCode(mobile_phone)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new SubscriberJSONObject(context,false){
                    @Override
                    protected void onNextJSONObject(JSONObject jsonObject) throws JSONException {
                        mMvpView.showMessage("恭喜,获取验证码成功！");
                    }

                    @Override
                    protected void onErrorUnKnown() {
                        mMvpView.showMessage("获取验证码失败，请联系相关幼儿园！");
                    }

                    @Override
                    protected void onErrorToken() {
                        mMvpView.showMessage("对不起,查无此号,请在相关幼儿园注册账号！");
                    }
                });
    }


    /**
     *用户注册激活（已测试）
     *@author Shuwen
     *created at 2016/7/29 16:14
     */
    public void register(String mobile_phone,String password, String identification_id,String sms_code){
        mSubscription = mDataManager.FmemberRegester(mobile_phone,password,"111111111111111111",sms_code)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new SubscriberJSONObject(context,false){
                    @Override
                    protected void onNextJSONObject(JSONObject jsonObject) throws JSONException {
                        mMvpView.showMessage("恭喜,账号激活成功！");
                        mMvpView.finishActivity();
                    }

                    @Override
                    protected void onErrorToken() {
                        mMvpView.showMessage("对不起,查无此号,请在相关幼儿园注册账号！");
                    }

                });
//        mSubscription = mDataManager.FmemberRegester(mobile_phone,password,identification_id,sms_code)
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribeOn(Schedulers.io())
//                .subscribe(new Subscriber<JSONObject>() {
//                    @Override
//                    public void onCompleted() {
//                        mMvpView.dialogDismiss();
//                    }
//
//                    @Override
//                    public void onError(Throwable e) {
//                        mMvpView.dialogDismiss();
//                        mMvpView.showMessage("对不起,账号激活失败！");
//                        e.printStackTrace();
//                    }
//
//                    @Override
//                    public void onNext(JSONObject jsonObject) {
//                        mMvpView.dialogDismiss();
//                        if (jsonObject.has("state")) {
//                            try {
//                                int state = Integer.valueOf(jsonObject.getString("state"));
//                                switch (state) {
//                                    case 0:
//                                        mMvpView.showMessage("恭喜,账号激活成功！");
//                                        mMvpView.finishActivity();
//                                        break;
//                                    case 1:
//                                        mMvpView.showMessage("对不起,账号激活失败！");
//                                        break;
//                                    case 2:
//                                        mMvpView.showMessage("未知错误发生");
//                                        break;
//                                    case 3:
//                                        mMvpView.showMessage("对不起,服务器数据异常！");
//                                        break;
//                                }
//                            } catch (JSONException e) {
//                                e.printStackTrace();
//                            }
//                        }
//                    }
//                });
    }




    public void onEventMainThread(BaseBusEvent event) {
        Log.e("ab", "控制器里收到BUS数据" + event.mes);
    }


}
