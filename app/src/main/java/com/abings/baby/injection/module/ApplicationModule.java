package com.abings.baby.injection.module;

import android.accounts.AccountManager;
import android.app.Application;
import android.content.Context;

import com.abings.baby.data.remote.APIService;
import com.abings.baby.data.remote.RetrofitUtils;
import com.abings.baby.injection.ApplicationContext;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import de.greenrobot.event.EventBus;
import retrofit.RxJavaCallAdapterFactory;

/**
 * Provide application-level dependencies. Mainly singleton object that can be injected from
 * anywhere in the app.
 */
@Module
public class ApplicationModule {
    protected final Application mApplication;

    public ApplicationModule(Application application) {
        mApplication = application;
    }

    @Provides
    Application provideApplication() {
        return mApplication;
    }

    @Provides
    @ApplicationContext
    Context provideContext() {
        return mApplication;
    }

    @Provides
    @Singleton
    EventBus provideEventBus() {
        return EventBus.getDefault();
       // return new Bus();
    }

    @Provides
    @Singleton
    APIService provideAPIService() {
        return RetrofitUtils.createApi(RxJavaCallAdapterFactory.create());// RibotService.Factory.makeRibotService(mApplication);
    }

    @Provides
    AccountManager provideAccountManager() {
        return AccountManager.get(mApplication);
    }

}
