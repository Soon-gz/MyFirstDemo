package com.abings.baby.data.remote.custom;

import org.json.JSONException;
import org.json.JSONObject;

import rx.Observable;

/**
 * Created by zwj on 2016/8/8.
 * 提供JSONObject手动解析
 */
public abstract class Func1JSONObject extends  Func1Base<JSONObject> {
    @Override
    protected Observable<JSONObject> callBase(JSONObject jsonObject) throws JSONException {
        return callJSONObject(jsonObject);
    }

    /**
     * 成功的返回接口
     *
     * @param jsonObject 对JSONObject进行解析
     * @return
     */
    protected abstract Observable<JSONObject> callJSONObject(JSONObject jsonObject) throws JSONException;

}
