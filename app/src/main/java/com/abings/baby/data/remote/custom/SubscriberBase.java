package com.abings.baby.data.remote.custom;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.widget.Toast;

import com.abings.baby.ui.login.LoginActivity;
import com.abings.baby.utils.CustomAlertDialog;
import com.abings.baby.utils.NetworkUtil;
import com.abings.baby.utils.ProgressDialogHelper;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by zwj on 2016/8/9.
 * 基类
 */
public abstract class SubscriberBase extends SubscriberError<JSONObject> {


    private boolean isProgress = false;
    private long mStartTime;
    protected long mMaxProgressTime = 800;//最大等待时间
    protected String mProgressMes = "正在拼命加载...";

    /**
     * 默认有等待条
     */
    public SubscriberBase(Context context) {
        super(context);
        this.isProgress = true;
    }

    /**
     * @param context
     * @param progressMes 进度条的内容
     */
    public SubscriberBase(Context context, String progressMes) {
        this(context);
        mProgressMes = progressMes;
    }

    /**
     * 选择设置是否有等待条
     *
     * @param context
     * @param isProgress 是否展示进度条
     */
    public SubscriberBase(Context context, boolean isProgress) {
        this(context);
        this.isProgress = isProgress;
    }


    @Override
    public void onStart() {
        boolean isConnected = NetworkUtil.isNetworkConnected(mContext);
        if (isConnected) {
            //有网络
            showProgress();
        } else {
            //无网络
            unsubscribe();
            dialog();
        }
    }

    @Override
    protected void onError(ApiException ex) {
        hideProgress();
        if (ex.isTokenError()) {
            onErrorToken();
        } else if (ex.isState2()) {
            onErrorState2();
        } else if (ex.isState3()) {
            onErrorState3();
        } else if (ex.isNetError()) {
            onErrorNet(ex);
        } else {
            onErrorUnKnown();
            ex.printStackTrace();
        }
    }


    @Override
    public void onCompleted() {
        hideProgress();
    }

    @Override
    public void onNext(JSONObject jsonObject) {
        dataLog(jsonObject);
        try {
            String state = jsonObject.getString("state");
            if ("0".equals(state)) {
                //正确，返回正确
                onNextJSONObject(jsonObject);
            } else if ("1".equals(state)) {
                //token错误
                onErrorState1(jsonObject);
            } else if ("2".equals(state)) {
                //一般是无数据
                onErrorState2();
            } else if ("3".equals(state)) {
                //包含3的未知错误
                onErrorState3();
            } else {
                onErrorUnKnown();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    protected void dialog() {
        CustomAlertDialog.dialogNetSet(mContext);
    }

    //显示等待条
    private void showProgress() {
        if (isProgress) {
            mStartTime = System.currentTimeMillis();
            ProgressDialogHelper.getInstance().showProgressDialog(mContext, mProgressMes);
        }
    }

    //隐藏等待条
    private void hideProgress() {
        if (isProgress) {
            long endTime = System.currentTimeMillis();
            long time = mMaxProgressTime - endTime + mStartTime;
            time = time < 0 ? 0 : time;
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    hideProgressNow();
                }
            }, time);
        }
    }

    //马上结束进度条
    public void hideProgressNow() {
        isProgress = false;
        ProgressDialogHelper.getInstance().hideProgressDialog();
    }

    protected abstract void onNextJSONObject(JSONObject jsonObject) throws JSONException;

    /**
     * Token错误
     */
    protected void onErrorToken() {
        mContext.startActivity(new Intent(mContext, LoginActivity.class));
        showToast(ApiException.ERROR_SERVER.TOKEN.mes);
    }   /**
     * Token错误
     */
    protected void onErrorState1(JSONObject jsonObject) {
        onErrorToken();
    }

    /**
     * 为2的错误码
     */
    protected void onErrorState2() {
        showToast(ApiException.ERROR_SERVER.STATE2.mes);
    }

    /**
     * 为3的错误啊
     */
    protected void onErrorState3() {
        showToast(ApiException.ERROR_SERVER.STATE3.mes);
    }

    /**
     * 网络错误
     *
     * @param ex
     */
    public void onErrorNet(ApiException ex) {
        if (ex.errorCode == ExceptionHandle.ERROR.Connect_TimeOut.code) {
            dialog();
        } else {
            showToast(ex.errorMessage + "(" + ex.errorCode + ")");
        }
    }

    /**
     * 未知错误
     */
    protected void onErrorUnKnown() {
        onErrorState3();
    }


    protected void showToast(String str) {
        Toast.makeText(mContext, str, Toast.LENGTH_SHORT).show();
    }


    protected void dataLog(JSONObject jsonObject) {

    }
}
