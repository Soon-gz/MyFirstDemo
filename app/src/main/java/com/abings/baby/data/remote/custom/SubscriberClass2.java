package com.abings.baby.data.remote.custom;

import android.content.Context;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

/**
 * Created by zwj on 2016/8/8.
 * 为了解决家长端和教师端同用一个subscribe解析不通用的问题
 *
 * @param <T> 解析对象
 */
public abstract class SubscriberClass2<T, T2> extends SubscriberClass<T> {
    private Class<T2> clazz2;
    private boolean isClazz1;

    /**
     *
     * @param context
     * @param clazz
     * @param clazz2
     * @param isClazz1 判断是否为第一个T
     */
    public SubscriberClass2(Context context, Class<T> clazz, Class<T2> clazz2, boolean isClazz1) {
        super(context, clazz);
        this.clazz2 = clazz2;
        this.isClazz1 = isClazz1;
    }

    /**
     *
     * @param context
     * @param progressMes 进度条文字
     * @param clazz
     * @param clazz2
     * @param isClazz1 判断是否为第一个T
     */
    public SubscriberClass2(Context context, String progressMes, Class<T> clazz, Class<T2> clazz2, boolean isClazz1) {
        super(context, clazz, progressMes);
        this.clazz2 = clazz2;
        this.isClazz1 = isClazz1;
    }

    /**
     *
     * @param context
     * @param isProgress
     * @param clazz
     * @param clazz2
     * @param isClazz1 判断是否为第一个T
     */
    public SubscriberClass2(Context context, boolean isProgress, Class<T> clazz, Class<T2> clazz2, boolean isClazz1) {
        super(context, clazz, isProgress);
        this.clazz2 = clazz2;
        this.isClazz1 = isClazz1;
    }


    @Override
    protected void onNextJSONObject(JSONObject jsonObject) throws JSONException {
        if (isClazz1) {
            onNextList(parseJson(jsonObject, mClazz));
        } else {
            onNextList2(parseJson(jsonObject, clazz2));
        }
    }

    /**
     * 第二个参数
     *
     * @param ts
     */
    protected abstract void onNextList2(List<T2> ts);

}