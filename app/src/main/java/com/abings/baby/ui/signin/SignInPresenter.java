package com.abings.baby.ui.signin;

import android.content.Context;
import android.util.Log;

import com.abings.baby.data.DataManager;
import com.abings.baby.data.local.PhotoUpAlbumHelper;
import com.abings.baby.data.model.Attend;
import com.abings.baby.data.remote.BaseBusEvent;
import com.abings.baby.data.remote.custom.SubscriberJSONObject;
import com.abings.baby.injection.ActivityContext;
import com.abings.baby.ui.base.Presenter;
import com.abings.baby.utils.DateUtil;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;

import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Administrator on 2016/1/18.
 */
public class SignInPresenter implements Presenter<SignInMvpView> {

    private final DataManager mDataManager;
    public Subscription mSubscription;
    private SignInMvpView mMvpView;
    private Context mContext;

    @Inject
    public SignInPresenter(DataManager dataManager, PhotoUpAlbumHelper photoUpAlbumHelper, @ActivityContext Context context) {
        mDataManager = dataManager;
        mContext = context;
    }

    @Override
    public void attachView(SignInMvpView mvpView) {
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

    public void onEventMainThread(BaseBusEvent event) {
        Log.e("ab", "控制器里收到BUS数据" + event.mes);
    }

    public void getQRCode(String content) {
        // pk_grade_class_id,
        // pk_school_id,
        // pk_baby_id,
        // pk_user_id
        if (content == null || content.isEmpty()) {
            return;
        }
        String[] values = content.split(",");
        if (values.length < 3) {
            return;
        }
        String pk_grade_class_id = values[0];
        String pk_school_id = values[1];
        String pk_baby_id = values[2];
        String pk_user_id = values[3];
        if (mSubscription != null) {
            mSubscription.unsubscribe();
        }
        mSubscription = mDataManager.attendVaildate(pk_grade_class_id, pk_school_id, pk_baby_id, pk_user_id)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new SubscriberJSONObject(mContext) {
                    @Override
                    protected void onNextJSONObject(JSONObject jsonObject) throws JSONException {
                        Gson gson = new Gson();
                        Type type = new TypeToken<List<Attend>>() {}.getType();
                        List<Attend> attends = gson.fromJson(jsonObject.get("result").toString(), type);
                        Attend attend = attends.get(0);
                        mMvpView.showAttend(attend);
                    }
                });
    }

    //0=到校,1=离校
    public void attendAddIn(Attend attend) {
        attendAdd(attend, "0");
    }

    //0=到校,1=离校
    public void attendAddOut(Attend attend) {
        attendAdd(attend, "1");
    }

    /**
     * @param attend
     * @param leave  0=到校 1=离校
     */
    public void attendAdd(Attend attend,final String leave) {

        if (mSubscription != null) {
            mSubscription.unsubscribe();
        }
        mSubscription = mDataManager.attendAdd(attend.getFk_grade_class_id(), attend.getFk_school_id(),
                attend.getPk_baby_id(), attend.getPk_user_id(), attend.getRelation_desc(), leave)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new SubscriberJSONObject(mContext) {
                    @Override
                    protected void onNextJSONObject(JSONObject jsonObject) throws JSONException {
                        String msg ;
                        if("0".equals(leave)){
                            msg = "入园打卡成功";
                        }else{
                            msg = "出园打卡成功";
                        }
                        mMvpView.showMessage(msg);
                    }

                    //错误码为1，这里表示已经考勤过了
                    @Override
                    protected void onErrorState1(JSONObject jsonObject) {
                        String msg;

                        // {"relation_desc":"Mom","datetime":"2016\/9\/20 5:51:15","state":"1","user_name":"舒文"}
                        try {
                            String datetime = jsonObject.getString("datetime");

                            String relation_desc = jsonObject.getString("relation_desc");
                            String user_name = jsonObject.getString("user_name");

                            Date date = DateUtil.getTimeFromString(datetime,"yyyy/MM/dd hh:mm:ss");
                            String hourminute = DateUtil.getStringFromTime(date,"HH:mm");
                            if("0".equals(leave)){
                                msg = "宝宝已于'"+hourminute+"'到校";
                            }else{
                                msg = "宝宝已于'"+hourminute+"'被"+user_name+"("+relation_desc+")接走";
                            }



                            mMvpView.showMessage(msg);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                });

    }
}
