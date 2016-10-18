package com.abings.baby.data.remote.custom;

import android.content.Context;

import rx.Subscriber;

/**
 * Created by zwj on 2016/8/3.
 * 对error的处理
 */
public abstract class SubscriberError<T> extends Subscriber<T> {
    protected Context mContext;

    public SubscriberError(Context context) {
        this.mContext = context;
    }

    @Override
    public void onError(Throwable e) {
        onError(ExceptionHandle.handleException(e));
    }

    protected abstract void onError(ApiException ex);
}
