package com.abings.baby.ui.aboutme.user;

import android.content.Context;

import com.abings.baby.data.DataManager;
import com.abings.baby.data.model.RelativePerson;
import com.abings.baby.data.model.TeacherDetail;
import com.abings.baby.data.model.UserDetail;
import com.abings.baby.data.remote.custom.Func1JSONObject;
import com.abings.baby.data.remote.custom.SubscriberJSONObject;
import com.abings.baby.injection.ActivityContext;
import com.abings.baby.ui.base.Presenter;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import javax.inject.Inject;

import rx.Observable;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by zwj on 2016/7/22.
 */
public class AboutMeFragmentPresenter implements Presenter<AboutMeFragmentMvpView> {

    private AboutMeFragmentMvpView mMvpView;
    private final DataManager mDataManager;
    private Subscription mSubscription;
    private Context mContext;

    @Inject
    public AboutMeFragmentPresenter(DataManager dataManager, @ActivityContext Context context) {
        mDataManager = dataManager;
        mContext = context;
    }

    @Override
    public void attachView(AboutMeFragmentMvpView mvpView) {
        this.mMvpView = mvpView;
        releaseSubscription();
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

    /**
     * 获取用户的个人资料，相关联系人
     */
    public void loadUserDetail() {
        releaseSubscription();
        mSubscription = mDataManager.getUserProfile()//
                .flatMap(new Func1JSONObject() {
                    @Override
                    protected Observable<JSONObject> callJSONObject(JSONObject result) throws JSONException {
                        Gson gson = new Gson();
                        if (!result.isNull("result") || result.getJSONArray("result").length() > 0) {

                            UserDetail data = gson.fromJson(result.getJSONArray("result").getJSONObject(0).toString(), UserDetail.class);
                            if (data != null) {
                                mMvpView.showUserDetail(data);
                            }
                            return mDataManager.getAssociation();
                        }
                        return null;
                    }

                    @Override
                    protected void onErrorState3() {
                        mMvpView.showError("服务器异常！");
                    }
                })
                .subscribeOn(Schedulers.io())//
                .observeOn(AndroidSchedulers.mainThread())//
                .subscribe(new SubscriberRelative(mContext));
    }

    /**
     * 获取用户的个人资料，相关联系人
     */
    public void loadTeacherDetail() {
        releaseSubscription();
        mSubscription = mDataManager.getTeacherProfile()//
                .subscribeOn(Schedulers.io())//
                .observeOn(AndroidSchedulers.mainThread())//
                .subscribe(new SubscriberJSONObject(mContext) {
                    @Override
                    protected void onNextJSONObject(JSONObject result) throws JSONException {
                        Gson gson = new Gson();
                        if (!result.isNull("result") || result.getJSONArray("result").length() > 0) {
                            TeacherDetail data = gson.fromJson(result.getJSONArray("result").getJSONObject(0).toString(), TeacherDetail.class);
                            if (data != null) {
                                mMvpView.showTeacherDetail(data);
                            }
                        }
                    }
                });
    }

    /**
     * 更改教师的资料
     *
     * @param email
     * @param birthday
     * @param sex
     */
    public void updateTeacherDetail(String email, String birthday, int sex) {
        releaseSubscription();
        mSubscription = mDataManager.updateTeacherProfile(email, birthday, sex)//
                .observeOn(AndroidSchedulers.mainThread())//
                .subscribeOn(Schedulers.io())//
                .subscribe(new SubscriberJSONObject(mContext){
                    @Override
                    protected void onErrorState3() {
                        mMvpView.showError("修改个人资料失败，请稍后再试");
                    }

                    @Override
                    protected void onNextJSONObject(JSONObject jsonObject) throws JSONException {
                        mMvpView.updateDetailResult();
                        mMvpView.showMessage("修改成功");
                    }
                });
    }

    /**
     * 修改用户信息
     *
     * @param email
     * @param birthday
     * @param address
     * @param relation_desc
     * @param sex           性别0，1
     */
    public void updateUserDetail(String email, String birthday, String address, String relation_desc, int sex) {
        releaseSubscription();
        mSubscription = mDataManager.updateUserProfile(email, birthday, address, relation_desc, sex)
                .observeOn(AndroidSchedulers.mainThread())//
                .subscribeOn(Schedulers.io())//
                .subscribe(new SubscriberJSONObject(mContext){
                    @Override
                    protected void onErrorState3() {
                        mMvpView.showError("修改个人资料失败，请稍后再试");
                    }

                    @Override
                    protected void onNextJSONObject(JSONObject jsonObject) throws JSONException {
                        mMvpView.updateDetailResult();
                        mMvpView.showMessage("修改成功");
                    }
                });
    }

    //刷新添加
    public void refreshAddRelative() {
        releaseSubscription();
        mDataManager.getAssociation()
                .subscribeOn(Schedulers.io())//
                .observeOn(AndroidSchedulers.mainThread())//
                .subscribe(new SubscriberRelative(mContext));
    }


    /**
     * 删除某个用户
     *
     * @param pk_user_id
     */
    public void deleteRelativePerson(String pk_user_id, final int delIndex) {
        releaseSubscription();
        mSubscription = mDataManager.delAssociation(pk_user_id)//
                .subscribeOn(Schedulers.io())//
                .observeOn(AndroidSchedulers.mainThread())//
                .subscribe(new SubscriberJSONObject(mContext) {
                    @Override
                    protected void onErrorState2() {
                        mMvpView.showError("其它错误！");
                    }

                    @Override
                    protected void onNextJSONObject(JSONObject jsonObject) throws JSONException {
                        mMvpView.showError("删除子帐号成功！");
                        mMvpView.updateRelativeByDel(delIndex);
                    }
                });
    }

    private class SubscriberRelative extends SubscriberJSONObject {

        private SubscriberRelative(Context context) {
            super(context);
        }

        @Override
        protected void onErrorState2() {
            mMvpView.showUserRelative(null);
        }

        @Override
        protected void onNextJSONObject(JSONObject jsonObject) throws JSONException {
            Gson gson = new Gson();
            RelativePerson relativePerson = gson.fromJson(jsonObject.toString(), RelativePerson.class);
            mMvpView.showUserRelative(relativePerson.getResult());
        }

        @Override
        protected void onErrorToken() {
            mMvpView.showUserRelative(null);
            mMvpView.showError("未检测到该手机账号！");
        }
    }
}
