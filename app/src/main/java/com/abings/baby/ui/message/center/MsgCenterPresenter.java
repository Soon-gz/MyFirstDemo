package com.abings.baby.ui.message.center;

import android.content.Context;
import android.util.Log;

import com.abings.baby.WineApplication;
import com.abings.baby.data.DataManager;
import com.abings.baby.data.model.MessageItem;
import com.abings.baby.data.model.MessageSendItem;
import com.abings.baby.data.remote.BaseBusEvent;
import com.abings.baby.data.remote.custom.ApiException;
import com.abings.baby.data.remote.custom.Func1JSONObject;
import com.abings.baby.data.remote.custom.SubscriberJSONObject;
import com.abings.baby.injection.ActivityContext;
import com.abings.baby.ui.base.Presenter;
import com.abings.baby.utils.AssetsUtils;
import com.abings.baby.utils.TLog;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.inject.Inject;

import rx.Observable;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Administrator on 2016/1/18.
 */
public class MsgCenterPresenter implements Presenter<MsgCenterMvpView> {

    private final DataManager mDataManager;
    public Subscription mSubscription;
    private MsgCenterMvpView mMvpView;
    private JSONObject mCachedRibots;
    private Context mContext;

    @Inject
    public MsgCenterPresenter(DataManager dataManager, @ActivityContext Context context) {
        mDataManager = dataManager;
        mContext = context;
    }

    @Override
    public void attachView(MsgCenterMvpView mvpView) {
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

    public void loadData(boolean showLoading) {
        loadData(WineApplication.getInstance().isTeacher(), showLoading);
    }

    public void loadCount(boolean showLoading) {
        mSubscription = mDataManager.teacherMessageUnReadCount().observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new SubscriberJSONObject(mContext) {
                               @Override
                               protected void onNextJSONObject(JSONObject jsonObject) throws JSONException {
                                   String count = jsonObject.getString("count");
                                   mMvpView.updateUnReadCount(Integer.valueOf(count));
                               }
                           }
                );
    }

    public void loadData(final boolean isTeacher, boolean showLoading) {
        if (mSubscription != null) mSubscription.unsubscribe();
        mSubscription = getObservable(isTeacher).flatMap(new Func1JSONObject() {
            @Override
            protected Observable<JSONObject> callJSONObject(JSONObject result) throws JSONException {
                String count = result.getString("count");
                mMvpView.updateUnReadCount(Integer.valueOf(count));
                if (isTeacher) {
                    return mDataManager.getTeacherMsgReceived();
                } else {
                    return mDataManager.getUserMsg();
                }
            }
        }).observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io()).subscribe(new SubscriberJSONObject(mContext, true) {
            @Override
            protected void onNextJSONObject(JSONObject result) throws JSONException {
                Gson gson = new Gson();
                Type type = new TypeToken<ArrayList<MessageItem>>() {
                }.getType();
                List<MessageItem> data1 = gson.fromJson(result.getJSONArray("result").toString(), type);
                List<MessageItem> data = reverseData(data1);
                mMvpView.showData(data, false);
            }

            @Override
            protected void onErrorState2() {
                mMvpView.showMessage("没有数据，请稍后再试");
                mMvpView.showData(null, false);
            }

            @Override
            public void onErrorNet(ApiException ex) {
                mMvpView.showData(null, false);
            }
        });
    }

    public void loadSent(final boolean isTeacher) {
        if (mSubscription != null) mSubscription.unsubscribe();
        mSubscription = mDataManager.teacherMessageSent()
                .observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io()).subscribe(new SubscriberJSONObject(mContext, true) {
                    List<MessageSendItem> sentdata1=new ArrayList<MessageSendItem>();
                    List<MessageSendItem> sentdata2=new ArrayList<MessageSendItem>();
                    List<MessageSendItem> sentdata3=new ArrayList<MessageSendItem>();
                    @Override
                    protected void onNextJSONObject(JSONObject result) throws JSONException {
                        Gson gson = new Gson();
                        Type type = new TypeToken<ArrayList<MessageSendItem>>() {
                        }.getType();
                        sentdata1 = gson.fromJson(result.getJSONArray("result").toString(), type);
                    }

                    @Override
                    protected void onErrorState2() {
//                        mMvpView.showMessage("没有数据，请稍后再试");
//                        mMvpView.showData(null, false);
//                        onCompleted();
                    }

                    @Override
                    public void onErrorNet(ApiException ex) {
                        mMvpView.showData(null, false);
                    }

                    @Override
                    public void onCompleted() {
                        mDataManager.teacherMessageSent2().observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io()).subscribe(new SubscriberJSONObject(mContext, true) {

                            @Override
                            protected void onNextJSONObject(JSONObject jsonObject) throws JSONException {
                                Gson gson = new Gson();
                                Type type = new TypeToken<ArrayList<MessageSendItem>>() {
                                }.getType();
                                sentdata2 = gson.fromJson(jsonObject.getJSONArray("result").toString(), type);
                                for(MessageSendItem sd:sentdata2){
                                    sd.setIsSchool("1");
                                    sentdata3.add(sd);
                                }
                                if(null!=sentdata3) {
                                    sentdata1.addAll(sentdata3);
                                }
                                SendMessageItemComparator comparator = new SendMessageItemComparator();
                                Collections.sort(sentdata1, comparator);
                                for (int i = 1; i < sentdata1.size(); i++) {
                                    if (sentdata1.get(i).getCreate_datetime().equals(sentdata1.get(i-1).getCreate_datetime())) {
                                        sentdata1.remove(i);
                                    }
                                }
                                mMvpView.showData(sentdata1, false);
                            }
                            @Override
                            protected void onErrorState2() {
                                if(null!=sentdata1)
                                mMvpView.showMessage("没有数据，请稍后再试");
                                Collections.reverse(sentdata1);
                                mMvpView.showData(sentdata1, false);
                            }

                            @Override
                            public void onErrorNet(ApiException ex) {
                                mMvpView.showData(null, false);
                            }
                        });
                    }
                });
    }

    private List<MessageItem> reverseData(List<MessageItem> data) {
        List<MessageItem> dataReverse = new ArrayList<>();
        for (int i = data.size() - 1; i >= 0; i--) {
            dataReverse.add(data.get(i));
        }
        return dataReverse;
    }

    private List<MessageSendItem> reverseSendData(List<MessageSendItem> data) {
        List<MessageSendItem> dataReverse = new ArrayList<>();
        for (int i = data.size() - 1; i >= 0; i--) {
            dataReverse.add(data.get(i));
        }
        return dataReverse;
    }

    private Observable<JSONObject> getObservable(boolean isTeacher) {
        if (isTeacher) {
            return mDataManager.teacherMessageUnReadCount();
        } else {
            return mDataManager.userMessageUnReadCount();
        }
    }

    public void onEventMainThread(BaseBusEvent event) {
    }

    public void fakeData() {
        String data = AssetsUtils.getDataFromAssets(WineApplication.getInstance(), "home/messages.json");
        try {
            JSONObject obj = new JSONObject(data);

            Gson gson = new Gson();
            Type type = new TypeToken<ArrayList<MessageItem>>() {
            }.getType();
            List<MessageItem> items = gson.fromJson(obj.getJSONArray("result").toString(), type);
            if (items != null) {
                mMvpView.showData(items, false);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
