package com.abings.baby.ui.signin;

import android.content.Context;
import android.util.Log;

import com.abings.baby.data.DataManager;
import com.abings.baby.data.model.SignInHistoryModel;
import com.abings.baby.data.remote.custom.ApiException;
import com.abings.baby.data.remote.custom.Func1JSONObject;
import com.abings.baby.data.remote.custom.SubscriberJSONObject;
import com.abings.baby.injection.ActivityContext;
import com.abings.baby.ui.base.Presenter;
import com.abings.baby.utils.TLog;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import rx.Observable;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by HaomingXu on 2016/9/15.
 */
public class SingInFrgPrecenter implements Presenter<SingInFrgMvpView> {
    private final DataManager mDataManager;
    public Subscription mSubscription;
    private SingInFrgMvpView mMvpView;
    private Context mContext;
    List<SignInHistoryModel> historys = new ArrayList<>();

    @Inject
    public SingInFrgPrecenter(DataManager dataManager, @ActivityContext Context context) {
        mDataManager = dataManager;
        mContext = context;
    }

    //按日期查询签到记录
    public void loadAttendSearchData(final String fk_grade_class_id, final String fk_school_id, final String datetime, final String leave) {

        historys.clear();
        TLog.getInstance().i("fk_grade_class_id：" + fk_grade_class_id + ",fk_school_id:" + fk_school_id + ",datetime:" + datetime + ",leave:" + leave);
        if (mSubscription != null) mSubscription.unsubscribe();
        mSubscription = mDataManager.attendSearch(fk_grade_class_id, fk_school_id, datetime, leave, "1")
                .flatMap(new Func1JSONObject() {

                    @Override
                    protected Observable<JSONObject> callJSONObject(JSONObject jsonObject) throws JSONException {
                        Gson gson = new Gson();
                        Type type = new TypeToken<ArrayList<SignInHistoryModel>>() {
                        }.getType();
                        try {
                            List<SignInHistoryModel> history = gson.fromJson(jsonObject.getJSONArray("result").toString(), type);
                            for (SignInHistoryModel s : history) {
                                s.setIsIn(true);
                            }
                            historys.addAll(history);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        return mDataManager.attendSearch(fk_grade_class_id, fk_school_id, datetime, leave, "0");
                    }

                    @Override
                    protected Observable<JSONObject> onErrorState2() {
                        return mDataManager.attendSearch(fk_grade_class_id, fk_school_id, datetime, leave, "0");
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SubscriberJSONObject(mContext, true) {
                    @Override
                    protected void onNextJSONObject(JSONObject jsonObject) throws JSONException {
                        Gson gson = new Gson();
                        Type type = new TypeToken<ArrayList<SignInHistoryModel>>() {
                        }.getType();
                        try {
                            List<SignInHistoryModel> history = gson.fromJson(jsonObject.getJSONArray("result").toString(), type);
                            for (SignInHistoryModel s : history) {
                                s.setIsIn(false);
                            }
                            historys.addAll(history);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        mMvpView.showData(historys, false);
                    }

                    @Override
                    protected void onErrorState2() {
                        mMvpView.showMessage(leave + "没有数据，请稍后再试。");
                        mMvpView.showData(null, false);
                    }

                    @Override
                    public void onErrorNet(ApiException ex) {
                        mMvpView.showData(null, false);
                    }
                });
    }


    @Override
    public void attachView(SingInFrgMvpView mvpView) {
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

}
