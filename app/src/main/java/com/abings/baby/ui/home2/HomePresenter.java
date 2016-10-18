package com.abings.baby.ui.home2;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.util.Log;

import com.abings.baby.data.DataManager;
import com.abings.baby.data.model.MainNewsItem;
import com.abings.baby.data.model.WaterFallGridItem;
import com.abings.baby.data.remote.BaseBusEvent;
import com.abings.baby.data.remote.custom.ApiException;
import com.abings.baby.data.remote.custom.Func1Class;
import com.abings.baby.data.remote.custom.SubscriberClass;
import com.abings.baby.injection.ActivityContext;
import com.abings.baby.ui.base.Presenter;

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
public class HomePresenter implements Presenter<HomeMvpView> {

    private final DataManager mDataManager;
    public Subscription mSubscription;
    private HomeMvpView mMvpView;
    private JSONObject mCachedRibots;
    private List<Fragment> fragments;
    private Context mContext;

    @Inject
    public HomePresenter(DataManager dataManager, @ActivityContext Context context) {
        mDataManager = dataManager;
        this.mContext = context;
    }

    @Override
    public void attachView(HomeMvpView mvpView) {
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


    public void loadData(String class_id, final String select_date, final boolean showLoading) {
        loadMainPageListData(class_id, select_date, showLoading);
    }


    public void loadMainPageListData(final String class_id, final String select_date, final boolean showLoading) {

        if (mSubscription != null) mSubscription.unsubscribe();

        mSubscription = mDataManager.postMainPageImage(class_id,select_date)
                .flatMap(new Func1Class<JSONObject,WaterFallGridItem>(WaterFallGridItem.class) {

                    @Override
                    protected Observable<JSONObject> callClass(List<WaterFallGridItem> waterFallGridItems) {
                        mMvpView.updateMainPageList(waterFallGridItems);
                        return mDataManager.postMainPageList(class_id, select_date);
                    }

                    @Override
                    protected Observable<JSONObject> onErrorState2() {
                        mMvpView.updateMainPageList(null);
                        return mDataManager.postMainPageList(class_id, select_date);
                    }
                }).observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io())
                .subscribe(new SubscriberClass<MainNewsItem>(mContext,MainNewsItem.class,showLoading) {

                    @Override
                    public void onNextList(List<MainNewsItem> mainNewsItems) {
                        mMvpView.updateNewsList(mainNewsItems);
                    }

                    @Override
                    protected void onErrorState2() {
                        mMvpView.updateNewsList(null);
                    }

                    @Override
                    public void onCompleted() {
                        super.onCompleted();
                        mMvpView.showLoadingProgress(false);
                    }

                    @Override
                    protected void onError(ApiException ex) {
                        super.onError(ex);
                        mMvpView.showLoadingProgress(false);
                    }
                });

//        mSubscription = mDataManager.postMainPageImage(class_id, select_date)
//                .flatMap(new Func1<JSONObject,Observable<JSONObject>>() {
//            @Override
//            public Observable<JSONObject> call(JSONObject result) {
//                //Log.i("TAG00",result.toString());
////                KLog.e(result.toString());
//                if (result.has("state")) {
//                    mMvpView.showLoadingProgress(false);
//                    try {
//                        int state = Integer.valueOf(result.getString("state"));
//                        switch (state) {
//                            case 0:
//                                Gson gson = new Gson();
//                                Type type = new TypeToken<ArrayList<WaterFallGridItem>>() {
//                                }.getType();
//                                List<WaterFallGridItem> data = gson.fromJson(result.getJSONArray("result").toString()
//                                        , type);
//                                if (data != null) {
//                                    mMvpView.updateMainPageList(data);
//                                    return mDataManager.postMainPageList(class_id, select_date);
//                                }
//                                break;
//                            case 1:
//                                mMvpView.onTokenError();
//                                break;
//                            case 2:
////                                if ("".equals(select_date)){
////                                    mMvpView.showMessage(select_date+"今天无图片数据！");
////                                }else{
////                                    mMvpView.showMessage(select_date+"无图片数据！");
////                                }
//                                mMvpView.updateMainPageList(null);
//                                return mDataManager.postMainPageList(class_id, select_date);
//                            case 3:
//                                mMvpView.showError("服务器异常！");
//                                break;
//                        }
//                    } catch (JSONException e) {
//                        e.printStackTrace();
//                    }
//                } else {
//                    mMvpView.showMessage("");
//                }
//                return null;
//            }
//        }).observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io()).subscribe(new Subscriber<JSONObject>() {
//            @Override
//            public void onCompleted() {
//                mMvpView.showLoadingProgress(false);
//            }
//
//            @Override
//            public void onError(Throwable e) {
//                e.printStackTrace();
//                mMvpView.showLoadingProgress(false);
//            }
//
//            @Override
//            public void onNext(JSONObject result) {
//                KLog.json(result.toString());
//                mMvpView.showLoadingProgress(false);
////                        KLog.e(result.toString());
//                if (result.has("state")) {
//                    try {
//                        int state = Integer.valueOf(result.getString("state"));
//                        switch (state) {
//                            case 0:
//                                Gson gson = new Gson();
//                                Type type = new TypeToken<ArrayList<MainNewsItem>>() {
//                                }.getType();
//                                List<MainNewsItem> data = gson.fromJson(result.getJSONArray("result").toString(), type);
//                                if (data != null) {
//                                    mMvpView.updateNewsList(data);
//                                }
//                                break;
//                            case 1:
//                                mMvpView.onTokenError();
//                                break;
//                            case 2:
////                                if ("".equals(select_date)){
////                                    mMvpView.showMessage(select_date+"今天无讯息数据！");
////                                }else{
////                                    mMvpView.showMessage(select_date+"无讯息数据！");
////                                }
//                                mMvpView.updateNewsList(null);
//                            case 3:
//                                mMvpView.showError("服务器异常！");
//                                break;
//                        }
//                    } catch (JSONException e) {
//                        e.printStackTrace();
//                    }
//                } else {
//                    mMvpView.showMessage("");
//                }
//            }
//        });
    }




    public void onEventMainThread(BaseBusEvent event) {
        Log.e("ab", "控制器里收到BUS数据" + event.mes);
    }

}
