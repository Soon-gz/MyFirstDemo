package com.abings.baby.data.remote.custom;

import android.content.Context;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by zwj on 2016/8/9.
 * 针对最后活的数据不是List的
 */
public abstract class SubscriberJSONObject extends SubscriberBase {
    public SubscriberJSONObject(Context context) {
        super(context);
    }

    public SubscriberJSONObject(Context context, String progressMes) {
        super(context, progressMes);
    }

    public SubscriberJSONObject(Context context, boolean isProgress) {
        super(context, isProgress);
    }

    @Override
    protected abstract void onNextJSONObject(JSONObject jsonObject) throws JSONException ;
}
