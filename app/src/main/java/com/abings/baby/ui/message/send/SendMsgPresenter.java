package com.abings.baby.ui.message.send;

import android.app.Activity;
import android.content.Context;
import android.util.Log;

import com.abings.baby.WineApplication;
import com.abings.baby.data.DataManager;
import com.abings.baby.data.local.PhotoUpAlbumHelper;
import com.abings.baby.data.model.ClassTeacherItem;
import com.abings.baby.data.remote.BaseBusEvent;
import com.abings.baby.data.remote.custom.SubscriberJSONObject;
import com.abings.baby.injection.ActivityContext;
import com.abings.baby.ui.base.Presenter;
import com.abings.baby.utils.TLog;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.List;

import javax.inject.Inject;

import rx.Observable;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Administrator on 2016/1/18.
 */
public class SendMsgPresenter implements Presenter<SendMsgMvpView> {

    private final DataManager mDataManager;
    public Subscription mSubscription;
    private SendMsgMvpView mMvpView;
    private Activity mActivity;
    private Context mContext;

    @Inject
    public SendMsgPresenter(DataManager dataManager, PhotoUpAlbumHelper photoUpAlbumHelper, @ActivityContext Context context) {
        mDataManager = dataManager;
        mContext = context;
    }

    @Override
    public void attachView(SendMsgMvpView mvpView) {
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

    /**
     * 发送班级消息，以班级id连接起来作为参数
     *
     * @author Shuwen
     * created at 2016/7/29 15:19
     */
    public void sendClassMsg(String fk_receive_class_id, String receiver_type, String subject, String content,boolean isToTeacher) {
        TLog.getInstance().i("发送消息接口上传数据：fk_receive_class_id:"+fk_receive_class_id+",receiver_type:"+receiver_type+",subject:"
        +subject+",content:"+content+",Token:"+WineApplication.getInstance().getToken()+"WineApplication.getInstance().getRole():"+WineApplication.getInstance().getRole());
        mSubscription = getObverable( fk_receive_class_id, receiver_type, subject, content,isToTeacher)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new SubscriberJSONObject(mContext,true) {
                               @Override
                               protected void onNextJSONObject(JSONObject jsonObject) throws JSONException {
                                   mMvpView.showMessage("消息发送成功！");
                                   hideProgressNow();
                                   mMvpView.finishActivity();
                               }
                           }
                );
    }

    public Observable getObverable(String fk_receive_class_id, String receiver_type, String subject, String content,boolean isToteacher){
        if (!isToteacher){
            return mDataManager.sendClassMessage(WineApplication.getInstance().getToken(), WineApplication.getInstance().getRole(), fk_receive_class_id, receiver_type, subject, content);
        }else{
            return mDataManager.sendMessage(fk_receive_class_id,"1",subject,content);
        }
    }


    /**
     * 加载全校通讯录
     */
    public void loadSchoolContacts(){
        if(mSubscription !=null){
            mSubscription.unsubscribe();
        }

        mSubscription = mDataManager.postSchoolTeachersContact().observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io()).subscribe(new SubscriberJSONObject(mContext) {
            @Override
            protected void onNextJSONObject(JSONObject jsonObject) throws JSONException {
                Gson gson = new Gson();
                JSONArray array = jsonObject.getJSONArray("result");
                if (array != null && array.length() > 0) {
                    Type type = new TypeToken<List<ClassTeacherItem>>() {
                    }.getType();
                    List<ClassTeacherItem> list = gson.fromJson(array.toString(), type);
                    mMvpView.showSchoolContacts(list);
                }
            }
        });
    }

    public void onEventMainThread(BaseBusEvent event) {
        Log.e("ab", "控制器里收到BUS数据" + event.mes);
    }

    public static String convertFileSize(long size) {
        long kb = 1024;
        long mb = kb * 1024;
        long gb = mb * 1024;

        if (size >= gb) {
            return String.format("%.1f GB", (float) size / gb);
        } else if (size >= mb) {
            float f = (float) size / mb;
            return String.format(f > 100 ? "%.0f MB" : "%.1f MB", f);
        } else if (size >= kb) {
            float f = (float) size / kb;
            return String.format(f > 100 ? "%.0f KB" : "%.1f KB", f);
        } else return String.format("%d B", size);
    }

    private List<String> mPaths;


}
