package com.abings.baby.ui.login;

import android.content.Context;
import android.text.Selection;
import android.text.Spannable;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.widget.TextView;

import com.abings.baby.WineApplication;
import com.abings.baby.data.DataManager;
import com.abings.baby.data.local.PhotoUpAlbumHelper;
import com.abings.baby.data.model.BabyItem;
import com.abings.baby.data.model.ClassItem;
import com.abings.baby.data.remote.BaseBusEvent;
import com.abings.baby.data.remote.custom.Func1JSONObject;
import com.abings.baby.data.remote.custom.SubscriberClass2;
import com.abings.baby.injection.ActivityContext;
import com.abings.baby.preference.SettingsPreference;
import com.abings.baby.ui.base.Presenter;
import com.abings.baby.utils.AppUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import javax.inject.Inject;

import rx.Observable;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Administrator on 2016/1/18.
 */
public class LoginPresenter implements Presenter<LoginMvpView> {

    private final DataManager mDataManager;
    public Subscription mSubscription;
    private LoginMvpView mMvpView;
    public String type;
    private Context mContext;

    @Inject
    public LoginPresenter(DataManager dataManager, PhotoUpAlbumHelper photoUpAlbumHelper, @ActivityContext Context context) {
        mDataManager = dataManager;
        mContext = context;
    }

    @Override
    public void attachView(LoginMvpView mvpView) {
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


    public void changePassWdShowState(TextView et, boolean isShow) {
        if (isShow) {
            et.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
        } else {
            et.setTransformationMethod(PasswordTransformationMethod.getInstance());
        }

        //光标定位到最后
        CharSequence text = et.getText();
        if (text instanceof Spannable) {
            Spannable spanText = (Spannable) text;
            Selection.setSelection(spanText, text.length());
        }
    }


    public void MemberLogin(final String acount, final String passwd) {
        if(mSubscription!=null){
            mSubscription.unsubscribe();
        }

        mSubscription = mDataManager.postCheckSchool(acount)
                .flatMap(new Func1JSONObject() {
                    @Override
            protected Observable<JSONObject> callJSONObject(JSONObject jsonObject) throws JSONException {
                String fk_school_id = jsonObject.getJSONArray("result").getJSONObject(0).getString("fk_school_id");
                WineApplication.getInstance().setFk_school_id(fk_school_id);
                if (WineApplication.getInstance().isTeacher()) {
                    return mDataManager.postTeacherLogin(acount, passwd, WineApplication.getInstance().getFk_school_id(),
                            AppUtils.getSystemVersion(), AppUtils.getDeviceId(WineApplication.getInstance()));
                } else {
                    return mDataManager.postMemberLogin(acount, passwd, WineApplication.getInstance().getFk_school_id(),
                            AppUtils.getSystemVersion(), AppUtils.getDeviceId(WineApplication.getInstance()));
                }
            }
            @Override
            protected void onErrorToken() {
                mMvpView.showError("手机号未注册！");
            }

                    @Override
                    protected Observable<JSONObject> onErrorState2() {
                        mMvpView.showError("密码错误！");
                        return null;
                    }



            @Override
            protected void onErrorState3() {
                mMvpView.showError("登录失败！");
            }
        }).flatMap(new Func1JSONObject() {
            @Override
            protected Observable<JSONObject> callJSONObject(JSONObject jsonObject) throws JSONException {
                mMvpView.saveName(acount,passwd);
                SettingsPreference.setUserAcount(WineApplication.getInstance(), acount);
                SettingsPreference.setUserPasswd(WineApplication.getInstance(), passwd);
                WineApplication.getInstance().setToken(mDataManager.getPreferencesHelper().getAccessToken());
                mDataManager.getPreferencesHelper().putAccessToken(jsonObject.getString("token"));
                if (WineApplication.getInstance().isTeacher()) {
                    return mDataManager.postMyClass(mDataManager.getPreferencesHelper().getAccessToken());
                } else {
                    return mDataManager.postMyBabys(mDataManager.getPreferencesHelper().getAccessToken());
                }
            }
            @Override
            protected void onErrorToken() {
                mMvpView.showError("手机号未注册！");
            }
            @Override
            protected Observable<JSONObject> onErrorState2() {
                mMvpView.showError("密码错误！");
                return null;
            }
            @Override
            protected void onErrorState3() {
                mMvpView.showError("登录失败！");
            }
        })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new SubscriberClass2<ClassItem, BabyItem>(mContext, ClassItem.class, BabyItem.class, WineApplication.getInstance().isTeacher()) {
                    @Override
                    protected void onNextList(List<ClassItem> classItems) {
                        mMvpView.addClass(classItems);
                        mMvpView.toMainActivity(true);
                    }
                    @Override
                    protected void onNextList2(List<BabyItem> ts) {
                        mMvpView.addBaby(ts);
                        mMvpView.toMainActivity(true);
                    }
                    @Override
                    protected void onErrorState2() {
                        mMvpView.showError("没有数据，请联系相关幼儿园！");
                    }
                    @Override
                    protected void onErrorUnKnown() {
                        mMvpView.showError("登录失败");
                    }
                });
    }


    public void onEventMainThread(BaseBusEvent event) {
        Log.e("ab", "控制器里收到BUS数据" + event.mes);
    }

}
