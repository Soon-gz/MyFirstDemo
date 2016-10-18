package com.abings.baby.data.remote;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;

import com.abings.baby.WineApplication;
import com.squareup.okhttp.Interceptor;
import com.squareup.okhttp.Response;

import java.io.IOException;


public class UnauthorisedInterceptor implements Interceptor {


    public UnauthorisedInterceptor(Context context) {
        WineApplication.get(context).getComponent().inject(this);
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Response response = chain.proceed(chain.request());
        if (response.code() == 401) {
            new Handler(Looper.getMainLooper()).post(new Runnable() {
                @Override
                public void run() {
                    // eventBus.post(new BusEvent.AuthenticationError());
                }
            });
        }
        return response;
    }
}
