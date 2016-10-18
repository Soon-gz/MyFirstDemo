package com.abings.baby.ui.message.unread;

import android.content.Context;
import android.util.Log;

import com.abings.baby.WineApplication;
import com.abings.baby.data.DataManager;
import com.abings.baby.data.model.ClassTeacherItem;
import com.abings.baby.data.model.Contact;
import com.abings.baby.data.model.UnreadContact;
import com.abings.baby.data.model.UserContact;
import com.abings.baby.data.remote.BaseBusEvent;
import com.abings.baby.data.remote.custom.Func1JSONObject;
import com.abings.baby.data.remote.custom.SubscriberJSONObject;
import com.abings.baby.injection.ActivityContext;
import com.abings.baby.ui.base.Presenter;
import com.abings.baby.utils.PingYinUtil;
import com.abings.baby.utils.PinyinComparator;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.socks.library.KLog;

import org.json.JSONArray;
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
public class UnreadPresenter implements Presenter<UnreadMvpView> {

    private final DataManager mDataManager;
    public Subscription mSubscription;
    private UnreadMvpView mMvpView;
    private JSONObject mCachedRibots;
    private List<UnreadContact> mContacts = new ArrayList<UnreadContact>();
    private Context mContext;

    @Inject
    public UnreadPresenter(DataManager dataManager, @ActivityContext Context context) {
        mDataManager = dataManager;
        mContext = context;
    }

    @Override
    public void attachView(UnreadMvpView mvpView) {
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

    public void loadContacts(final String creatTime,String role) {
        mSubscription = getRibotsObservable(false, creatTime,role).subscribe(new SubscriberJSONObject(mContext){
            @Override
            protected void onNextJSONObject(JSONObject result) throws JSONException {
                mCachedRibots = result;
                KLog.json(result.toString());
                Log.i("TAG00", "未读：" + result.toString());
                if (result.has("state")) {
                    try {
                        int state = Integer.valueOf(result.getString("state"));
                        switch (state) {
                            case 0:
                                Gson gson = new Gson();
                                List<UnreadContact> list = new ArrayList<UnreadContact>();
                                Type type = new TypeToken<ArrayList<UnreadContact>>() {
                                }.getType();
                                list = gson.fromJson(result.getJSONArray("result").toString(), type);
                                mMvpView.showData(list, false);
                                break;
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    mMvpView.showMessage("no data");
                }
            }
        });

    }


    private Observable<JSONObject> getRibotsObservable(boolean allowMemoryCacheVersion, String creatTime,String role) {
        if (allowMemoryCacheVersion && mCachedRibots != null) {
            return Observable.just(mCachedRibots);
        } else {
                return mDataManager.postUnreadContacts(creatTime,
                        "0",role).observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io());
        }
    }

    public void onEventMainThread(BaseBusEvent event) {

    }
}
