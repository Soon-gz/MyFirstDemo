package com.abings.baby.ui.login;

import android.util.Log;

import com.abings.baby.WineApplication;
import com.abings.baby.data.DataManager;
import com.abings.baby.data.model.BabyItem;
import com.abings.baby.data.model.ClassItem;
import com.abings.baby.preference.SettingsPreference;
import com.abings.baby.ui.base.Presenter;
import com.abings.baby.utils.AppUtils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.socks.library.KLog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
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
 * Created by HaomingXu on 2016/7/15.
 */
public class SplashPrecenter implements Presenter<SplashMvpView> {

    private final DataManager mDataManager;
    private SplashMvpView mMvpView;
    public Subscription mSubscription;

    @Inject
    public SplashPrecenter(DataManager mDataManager){
        this.mDataManager = mDataManager;
    }

    @Override
    public void attachView(SplashMvpView mvpView) {
        mMvpView = mvpView;
    }

    @Override
    public void detachView() {
        mMvpView = null;
        if (mSubscription != null) mSubscription.unsubscribe();
    }

    public void MemberLogin(final String acount, final String passwd) {
        mSubscription = mDataManager.postCheckSchool(acount).flatMap(new Func1<JSONObject, Observable<JSONObject>>() {
            @Override
            public Observable<JSONObject> call(JSONObject result) {
                KLog.json(result.toString());
                Log.i("TAG00", "flatMap:" + result.toString());
                if (result.has("state")) {
                    try {
                        int state = Integer.valueOf(result.getString("state"));
                        switch (state) {
                            case 0:
                                String fk_school_id = result.getJSONArray("result").getJSONObject(0).getString("fk_school_id");//String fk_school_id = "1";//暂时只用1学校
                                WineApplication.getInstance().setFk_school_id(fk_school_id);
                                if (WineApplication.getInstance().isTeacher()) {
                                    return mDataManager.postTeacherLogin(acount, passwd, WineApplication.getInstance
                                            ().getFk_school_id(), AppUtils.getSystemVersion(), AppUtils.getDeviceId
                                            (WineApplication.getInstance()));
                                } else {
                                    return mDataManager.postMemberLogin(acount, passwd, WineApplication.getInstance()
                                            .getFk_school_id(), AppUtils.getSystemVersion(), AppUtils.getDeviceId
                                            (WineApplication.getInstance()));
                                }
                            case 1:
                                mMvpView.showError("手机号未注册！");
                                mMvpView.toLoginActivity();
                                break;
                            case 2:
                                mMvpView.showError("密码错误！");
                                mMvpView.toLoginActivity();
                                break;
                            case 3:
                                mMvpView.showError("登录失败！");
                                mMvpView.toLoginActivity();
                                break;
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                return null;
            }
        }).flatMap(new Func1<JSONObject, Observable<JSONObject>>() {
            @Override
            public Observable<JSONObject> call(JSONObject result) {
                KLog.json(result.toString());
                Log.i("TAG00", "flatMap2:" + result.toString());
                if (result.has("state")) {
                    try {
                        int state = Integer.valueOf(result.getString("state"));
                        switch (state) {
                            case 0:
                                SettingsPreference.setUserAcount(WineApplication.getInstance(), acount);
                                SettingsPreference.setUserPasswd(WineApplication.getInstance(), passwd);
                                WineApplication.getInstance().setToken(mDataManager.getPreferencesHelper()
                                        .getAccessToken());
                                mDataManager.getPreferencesHelper().putAccessToken(result.getString("token"));
                                KLog.e("token ==" + mDataManager.getPreferencesHelper().getAccessToken());

                                if (WineApplication.getInstance().isTeacher()) {
                                    return mDataManager.postMyClass(mDataManager.getPreferencesHelper()
                                            .getAccessToken());
                                } else {
                                    return mDataManager.postMyBabys(mDataManager.getPreferencesHelper()
                                            .getAccessToken());
                                }
                            case 1:
                                mMvpView.showError("手机号未注册！");
                                mMvpView.toLoginActivity();
                                break;
                            case 2:
                                mMvpView.showError("密码错误！");
                                mMvpView.toLoginActivity();
                                break;
                            case 3:
                                mMvpView.showError("登录失败！");
                                mMvpView.toLoginActivity();
                                break;
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                return null;
            }
        }).observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io()).subscribe(new Subscriber<JSONObject>() {
            @Override
            public void onCompleted() {
            }

            @Override
            public void onError(Throwable e) {
                e.printStackTrace();
                Log.i("TAG00", "error:");
                mMvpView.showError("登录失败！");
                mMvpView.toLoginActivity();
            }

            @Override
            public void onNext(JSONObject result) {
                KLog.json(result.toString());
                Log.i("TAG00", "onNext:" + result.toString());
                if (result.has("state")) {
                    try {
                        int state = Integer.valueOf(result.getString("state"));
                        switch (state) {
                            case 0:
                                Gson gson = new Gson();
                                JSONArray jsonArray = result.getJSONArray("result");
                                if (WineApplication.getInstance().isTeacher()) {
                                    Type type = new TypeToken<ArrayList<ClassItem>>() {
                                    }.getType();
                                    List<ClassItem> list = gson.fromJson(jsonArray.toString(), type);
                                    mMvpView.addClass(list);
                                } else {
                                    Type type = new TypeToken<ArrayList<BabyItem>>() {
                                    }.getType();
                                    List<BabyItem> list = gson.fromJson(jsonArray.toString(), type);
                                    mMvpView.addBaby(list);
                                }
                                mMvpView.toMainActivity(true);
                                break;
                            case 1:
                                mMvpView.showError("token 错误！");
                                mMvpView.toLoginActivity();
                                break;
                            case 2:
                                mMvpView.showError("没有数据，请联系相关幼儿园！");
                                mMvpView.toLoginActivity();
                                break;
                            case 3:
                                mMvpView.showError("服务器异常！");
                                mMvpView.toLoginActivity();
                                break;
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

}
