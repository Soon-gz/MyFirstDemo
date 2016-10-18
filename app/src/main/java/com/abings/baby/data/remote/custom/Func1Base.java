package com.abings.baby.data.remote.custom;

import org.json.JSONException;
import org.json.JSONObject;

import rx.Observable;
import rx.functions.Func1;

/**
 * Created by zwj on 2016/8/9.
 * 基类
 */
public abstract class Func1Base<T> implements Func1<JSONObject, Observable<T>> {

    @Override
    public Observable<T> call(JSONObject jsonObject) {
        dataLog(jsonObject);
        try {
            String state = jsonObject.getString("state");
            if ("0".equals(state)) {
                //正确，返回正确
              return callBase(jsonObject);
            } else if ("1".equals(state)) {
                //token错误
                onErrorToken();
            } else if ("2".equals(state)) {
                //一般是无数据
                return onErrorState2();
            } else if ("3".equals(state)) {
                //包含3的未知错误
                onErrorState3();
            } else {
                onErrorUnKnown();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 成功的返回接口
     *
     * @param jsonObject 解析结果
     * @return
     */
    protected abstract Observable<T> callBase(JSONObject jsonObject) throws JSONException;


    /**
     * Token错误
     */
    protected void onErrorToken() {
        ServerException serverException = new ServerException();
        serverException.errorCode = ApiException.ERROR_SERVER.TOKEN.code;
        serverException.errorMessage = ApiException.ERROR_SERVER.TOKEN.mes;
        throw serverException;
    }

    /**
     * 为2的错误码
     * @return
     */
    protected Observable<T> onErrorState2() {
        ServerException serverException = new ServerException();
        serverException.errorCode = ApiException.ERROR_SERVER.STATE2.code;
        serverException.errorMessage = ApiException.ERROR_SERVER.STATE2.mes;
        throw serverException;
    }

    /**
     * 为3的错误啊
     */
    protected void onErrorState3() {
        ServerException serverException = new ServerException();
        serverException.errorCode = ApiException.ERROR_SERVER.STATE3.code;
        serverException.errorMessage = ApiException.ERROR_SERVER.STATE3.mes;
        throw serverException;
    }


    /**
     * 未知错误
     */
    protected void onErrorUnKnown() {
        onErrorState3();
    }

    protected void dataLog(JSONObject jsonObject){

    }
}
