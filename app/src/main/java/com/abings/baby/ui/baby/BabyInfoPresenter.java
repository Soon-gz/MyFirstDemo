package com.abings.baby.ui.baby;

import android.content.Context;

import com.abings.baby.WineApplication;
import com.abings.baby.data.DataManager;
import com.abings.baby.data.remote.custom.SubscriberJSONObject;
import com.abings.baby.injection.ActivityContext;
import com.abings.baby.ui.base.Presenter;
import com.abings.baby.utils.StringUtils;
import com.socks.library.KLog;

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
public class BabyInfoPresenter implements Presenter<BabyInfoMvpView> {

    private final DataManager mDataManager;
    public Subscription mSubscription;
    private BabyInfoMvpView mMvpView;
    private JSONObject mCachedRibots;
    private Context mContext;

    @Inject
    public BabyInfoPresenter(DataManager dataManager, @ActivityContext Context context) {
        mDataManager = dataManager;
        mContext = context;
    }

    @Override
    public void attachView(BabyInfoMvpView mvpView) {
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


    public void updateUserInfo(String nick_name) {
        if (mSubscription != null) mSubscription.unsubscribe();
        mSubscription = mDataManager.Fbaby_update_name(WineApplication.getInstance().getBaby().getPk_baby_id(),nick_name).observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io()).subscribe(new SubscriberJSONObject(mContext) {
                    @Override
                    protected void onNextJSONObject(JSONObject jsonObject) throws JSONException {
                        mMvpView.updateUserInfo(true);
                        mMvpView.showMessage("修改成功");
                    }
                });
    }
/**
 * pk_teacher_id : 1
 * fk_school_id : 2
 * name : 苍老师
 * mobile_phone : 18628023869
 * identification_id :
 * sex : False
 * birthday :
 * email :
 * race_name :
 * phot*/

//    private Observable<JSONObject> getObservable(boolean isTeacher) {
//        if (isTeacher) {
//            return mDataManager.getTeacherProfile().observeOn(AndroidSchedulers.mainThread()).subscribeOn
//                    (Schedulers.io());
//        } else {
//            return mDataManager.getUserProfile().observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io());
//        }
//    }



    /**
     *使用retrofit取代xUtils做网络请求,更新宝宝的头像
     *@author Shuwen
     *created at 2016/7/29 13:35
     */
    public void uploadImage(String path){
        mMvpView.showLoadingProgress(true);
        mSubscription = mDataManager.uploadBabyPhoto(WineApplication.getInstance().getBaby().getPk_baby_id(),path).observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io()).subscribe(new SubscriberJSONObject(mContext) {
                    @Override
                    protected void onNextJSONObject(JSONObject jsonObject) throws JSONException {
                        String name = jsonObject.getString("filename");
                        if (!StringUtils.isEmpty(name)) {
                            mMvpView.updateHeader(name);
                        }
                        mMvpView.showMessage("头像保存成功");
                    }
        });
    }


    /**
     *可用的xUitls网络请求逻辑（废弃）
     *@author Shuwen
     *created at 2016/7/29 13:36
     */
//    public void uploadImage(String path, boolean isTeacher) {
//        mMvpView.showLoadingProgress(true);
//        String temp = "Fupload_baby_photo.asp";
//        RequestParams params = new RequestParams(RetrofitUtils.BASE_URL + temp);
//        // 添加到请求body体的参数, 只有POST, PUT, PATCH, DELETE请求支持.
//        // params.addBodyParameter("wd", "xUtils");
//        // 使用multipart表单上传文件
//        params.setMultipart(true);
//        params.addBodyParameter("token", WineApplication.getInstance().getToken());
//        params.addBodyParameter("pk_baby_id",WineApplication.getInstance().getBaby().getPk_baby_id());
//        File img = new File(path);
//        params.addBodyParameter("file1", img, "multipart/form-data"); // 如果文件没有扩展名, 最好设置contentType参数.
//        x.http().post(params, new Callback.CommonCallback<JSONObject>() {
//            @Override
//            public void onSuccess(JSONObject result) {
//                KLog.json(result.toString());
//                if (result.has("state")) {
//                    try {
//                        int state = Integer.valueOf(result.getString("state"));
//                        switch (state) {
//                            case 0:
//                                String name = result.getString("filename");
//                                if (!StringUtils.isEmpty(name)) {
//                                    mMvpView.updateHeader(name);
//                                }
//                                mMvpView.showMessage("头像保存成功");
//                                break;
//                            case 1:
//                                mMvpView.onTokenError();
//                                break;
//                            case 2:
//                                break;
//                            case 3:
//                                break;
//                        }
//                    } catch (JSONException e) {
//                        e.printStackTrace();
//                    }
//                }
//            }
//
//            @Override
//            public void onError(Throwable ex, boolean isOnCallback) {
//                ex.printStackTrace();
//            }
//
//            @Override
//            public void onCancelled(CancelledException cex) {
//            }
//
//            @Override
//            public void onFinished() {
//                mMvpView.showLoadingProgress(false);
//            }
//
//        });
//    }

}
