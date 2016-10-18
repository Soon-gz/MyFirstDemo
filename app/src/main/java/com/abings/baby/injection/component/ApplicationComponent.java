package com.abings.baby.injection.component;

import android.accounts.AccountManager;
import android.app.Application;
import android.content.Context;

import com.abings.baby.WineApplication;
import com.abings.baby.data.DataManager;
import com.abings.baby.data.local.DatabaseHelper;
import com.abings.baby.data.local.PhotoUpAlbumHelper;
import com.abings.baby.data.local.PreferencesHelper;
import com.abings.baby.data.remote.APIService;
import com.abings.baby.data.remote.GoogleAuthHelper;
import com.abings.baby.data.remote.UnauthorisedInterceptor;
import com.abings.baby.injection.ApplicationContext;
import com.abings.baby.injection.module.ApplicationModule;
import com.abings.baby.service.AutoCheckInService;
import com.abings.baby.service.BeaconsSyncService;
import com.abings.baby.service.BootCompletedReceiver;

import javax.inject.Singleton;

import dagger.Component;
import de.greenrobot.event.EventBus;

@Singleton
@Component(modules = ApplicationModule.class)
public interface ApplicationComponent {

    void inject(WineApplication ribotApplication);
    void inject(UnauthorisedInterceptor unauthorisedInterceptor);
    void inject(AutoCheckInService autoCheckInService);
    void inject(BeaconsSyncService beaconsSyncService);
    void inject(BootCompletedReceiver bootCompletedReceiver);

    @ApplicationContext
    Context context();
    Application application();
    APIService apiService();
    PreferencesHelper preferencesHelper();
    DatabaseHelper databaseHelper();
    DataManager dataManager();
    EventBus eventBus();
    AccountManager accountManager();
    GoogleAuthHelper googleAuthHelper();
    PhotoUpAlbumHelper photoUpAlbumHelper();
}
