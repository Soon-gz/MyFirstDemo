package com.abings.baby.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.abings.baby.WineApplication;
import com.abings.baby.data.DataManager;

import javax.inject.Inject;


/**
 * Starts auto-check in service on boot completed if user is signed in.
 */
public class BootCompletedReceiver extends BroadcastReceiver {

    @Inject
    DataManager mDataManager;

    @Override
    public void onReceive(Context context, Intent intent) {
        WineApplication.get(context).getComponent().inject(this);
        if (mDataManager.getPreferencesHelper().getAccessToken() != null) {
            context.startService(AutoCheckInService.getStartIntent(context));
        }
    }

}
