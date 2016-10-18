package com.abings.baby.service;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;

import com.abings.baby.WineApplication;
import com.abings.baby.data.DataManager;
import com.abings.baby.utils.AndroidComponentUtil;
import com.abings.baby.utils.NetworkUtil;

import javax.inject.Inject;

import rx.Subscription;

public class BeaconsSyncService extends Service {

    @Inject
    DataManager mDataManager;

    private Subscription mSubscription;

    public static Intent getStartIntent(Context context) {
        return new Intent(context, BeaconsSyncService.class);
    }

    public static boolean isRunning(Context context) {
        return AndroidComponentUtil.isServiceRunning(context, BeaconsSyncService.class);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        WineApplication.get(this).getComponent().inject(this);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, final int startId) {
       // Timber.i("Starting sync...");

        if (!NetworkUtil.isNetworkConnected(this)) {
           // Timber.i("Sync canceled, connection not available");
            AndroidComponentUtil.toggleComponent(this, SyncOnConnectionAvailable.class, true);
            stopSelf(startId);
            return START_NOT_STICKY;
        }

        if (mSubscription != null && !mSubscription.isUnsubscribed()) mSubscription.unsubscribe();
/*        mSubscription = mDataManager.syncRegisteredBeacons()
                .subscribeOn(Schedulers.io())
                .subscribe(new Subscriber<Void>() {
                    @Override
                    public void onCompleted() {
                        Timber.i("Synced successfully!");
                        stopSelf(startId);
                    }

                    @Override
                    public void onError(Throwable e) {
                        Timber.w(e, "Error syncing.");
                        stopSelf(startId);
                    }

                    @Override
                    public void onNext(Void aVoid) {

                    }
                });*/

        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        if (mSubscription != null) mSubscription.unsubscribe();
        super.onDestroy();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    public static class SyncOnConnectionAvailable extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            if (NetworkUtil.isNetworkConnected(context)) {
                //Timber.i("Connection is now available, triggering sync...");
                AndroidComponentUtil.toggleComponent(context, this.getClass(), false);
                context.startService(getStartIntent(context));
            }
        }
    }

}
