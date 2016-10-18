package com.abings.baby.ui.main;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.abings.baby.Const;
import com.abings.baby.WineApplication;
import com.abings.baby.data.DataManager;
import com.abings.baby.data.model.TeacherDetail;
import com.abings.baby.data.model.UserDetail;
import com.abings.baby.data.remote.custom.Func1JSONObject;
import com.abings.baby.data.remote.custom.SubscriberJSONObject;
import com.abings.baby.injection.ActivityContext;
import com.abings.baby.ui.adapter.VPFragmentAdapter;
import com.abings.baby.ui.base.Presenter;
import com.abings.baby.ui.feedback.FeedBackFragment;
import com.abings.baby.ui.finder.FinderFragment;
import com.abings.baby.ui.home2.HomeFragment2;
import com.abings.baby.ui.message.center.MsgCenterListFragment;
import com.abings.baby.ui.message.center.MsgCenterListFragment_copy;
import com.abings.baby.ui.setting.SettingFragment;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import cn.bingoogolapple.bgabanner.BGAViewPager;
import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class MainPresenter implements Presenter<MainMvpView> {

    private final DataManager mDataManager;
    private MainMvpView mMvpView;
    private Subscription mSubscription;
    private List<Fragment> fragments;
    private Context mContext;

    @Inject
    public MainPresenter(DataManager dataManager, @ActivityContext Context context) {
        mDataManager = dataManager;
        mContext = context;
    }


    public List<Fragment> getPagerFragments() {
        fragments = new ArrayList<Fragment>();
        fragments.add(new HomeFragment2());  //首页
        fragments.add(new SettingFragment()); //通讯录
        fragments.add(new SettingFragment());  //设置
        fragments.add(new FeedBackFragment()); //意见反馈


        Bundle bundle_0 = new Bundle();
        bundle_0.putString("title", "消息中心");
        bundle_0.putInt("tag", Const.Fragment_Meg);
        fragments.add(MsgCenterListFragment_copy.newInstance(bundle_0));

        fragments.add(new FinderFragment());

//        Bundle bundle_1 = new Bundle();
//        bundle_1.putString("title", "午餐");
//        bundle_1.putInt("tag", Const.Fragment_Lunch);
//        fragments.add(InfoListFragment.newInstance(bundle_1));
//        Bundle bundle_2 = new Bundle();
//        bundle_1.putInt("tag", Const.Fragment_Operation);
//        bundle_2.putString("title", "作业");
//        fragments.add(InfoListFragment.newInstance(bundle_2));
//        Bundle bundle_3 = new Bundle();
//        bundle_1.putInt("tag", Const.Fragment_Play);
//        bundle_3.putString("title", "活动");
//        fragments.add(InfoListFragment.newInstance(bundle_3));
//        Bundle bundle_4 = new Bundle();
//        bundle_4.putString("title", "日志");
//        bundle_1.putInt("tag", Const.Fragment_Journal);
//        fragments.add(InfoListFragment.newInstance(bundle_4));
        return fragments;
    }

    public Fragment getOneFragment() {
        return (Fragment) fragments.get(0);
    }

    public void intFragments(BGAViewPager mViewPager, List<Fragment> fragments, MainActivity mActivity) {
        if (null != fragments && !fragments.isEmpty()) {
            mViewPager.setAllowUserScrollable(false);// .setEnableScroll(false);
            mViewPager.setOffscreenPageLimit(fragments.size());
            mViewPager.setAdapter(new VPFragmentAdapter(mActivity.getSupportFragmentManager(), fragments));
        }
    }

    public void toFragment(BGAViewPager mViewPager, int position) {
        mViewPager.setCurrentItem(position, false);
    }


    @Override
    public void attachView(MainMvpView mvpView) {
        mMvpView = mvpView;
    }

    @Override
    public void detachView() {
        mMvpView = null;
        if (mSubscription != null) mSubscription.unsubscribe();
    }

    public void signOut() {
        mSubscription = mDataManager.signOut().observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io())
                .subscribe(new Subscriber<Void>() {
                    @Override
                    public void onCompleted() {
                        // Timber.i("Sign out successful!");
                        mMvpView.onSignedOut();
                    }

                    @Override
                    public void onError(Throwable e) {
                        //  Timber.e("Error signing out: " + e);
                    }

                    @Override
                    public void onNext(Void aVoid) {

                    }
                });
    }


    public void loadUserInfo(final boolean isTeacher) {
        mMvpView.showLoadingProgress(true);
        if (mSubscription != null) mSubscription.unsubscribe();
        mSubscription = getObservable(isTeacher).flatMap(new Func1JSONObject() {
            @Override
            protected Observable<JSONObject> callJSONObject(JSONObject result) throws JSONException {
                Gson gson = new Gson();
                if (!result.isNull("result") || result.getJSONArray("result").length() > 0) {
                    if (isTeacher) {
                        TeacherDetail data = gson.fromJson(result.getJSONArray("result")
                                .getJSONObject(0).toString(), TeacherDetail.class);
                        WineApplication.getInstance().setPhoto_url(data.getPhoto());
                        if (data != null) {
                            mMvpView.updateView(data);
                        }
                    } else {
                        UserDetail data = gson.fromJson(result.getJSONArray("result").getJSONObject
                                (0).toString(), UserDetail.class);
                        if (data != null) {
                            mMvpView.updateView(data);
                        }
                    }
                }
                if (isTeacher) {
                    return mDataManager.teacherMessageUnReadCount();
                } else {
                    return mDataManager.userMessageUnReadCount();
                }
            }
        }).observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io()).subscribe(new SubscriberJSONObject(mContext) {
            @Override
            protected void onNextJSONObject(JSONObject result) throws JSONException {
                String count = result.getString("count");
                mMvpView.updateUnReadCount(Integer.valueOf(count));
            }

            @Override
            protected void onErrorState2() {
                mMvpView.showError("无数据！");
            }
        });
    }

    private Observable<JSONObject> getObservable(boolean isTeacher) {
        if (isTeacher) {
            return mDataManager.getTeacherProfile();
        } else {
            return mDataManager.getUserProfile();
        }
    }
}
