package com.abings.baby.data.remote.custom;

import android.content.Context;

import com.abings.baby.utils.JsonUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zwj on 2016/8/5.
 * 解析一个json
 *
 * @param <T> 解析对象
 */
public abstract class SubscriberClass<T> extends SubscriberBase {

    protected Class<T> mClazz;

    public SubscriberClass(Context context,Class<T> clazz) {
        super(context);
        this.mClazz = clazz;
    }

    public SubscriberClass(Context context,Class<T> clazz, String progressMes) {
        super(context, progressMes);
        this.mClazz = clazz;
    }

    public SubscriberClass(Context context,Class<T> clazz, boolean isProgress) {
        super(context, isProgress);
        this.mClazz = clazz;
    }

    @Override
    protected void onNextJSONObject(JSONObject jsonObject) throws JSONException {
        try {
            onNextList(parseJson(jsonObject,mClazz));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    protected abstract void onNextList(List<T> ts);



    protected  <N> List<N> parseJson(JSONObject jsonObject, Class<N> c) throws JSONException {
        List<N> ts = new ArrayList<>();
        if (jsonObject.has("result")) {
            String result = jsonObject.getString("result");
            ts = JsonUtils.fromJson(result, c);
        }
        return ts;
    }
}
