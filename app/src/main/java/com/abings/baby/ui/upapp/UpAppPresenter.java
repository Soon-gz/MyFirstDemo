package com.abings.baby.ui.upapp;


import com.abings.baby.Const;
import com.abings.baby.data.DataManager;
import com.abings.baby.ui.base.Presenter;

import javax.inject.Inject;

import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by zwj on 2016/6/25.
 */
public class UpAppPresenter implements Presenter<UpAppMvpView> {

    private final DataManager mDataManager;
    private UpAppMvpView mvpView;
    private Subscription mSubscription;

    @Inject
    public UpAppPresenter(DataManager mDataManager) {
        this.mDataManager = mDataManager;
    }

    public void toUpApp(String url) {

        String fileName = url.substring(url.lastIndexOf("/") + 1);
        mvpView.showLoadingProgress(true);
        mSubscription = mDataManager.downloadFile(url, Const.apkDownPath, fileName)//
                .observeOn(AndroidSchedulers.mainThread())//
                .subscribeOn(Schedulers.io())//
                .subscribe(new Subscriber<String>() {
                    @Override
                    public void onCompleted() {
                        //关闭当前的Dialog
                        mvpView.showLoadingProgress(false);
                    }

                    @Override
                    public void onError(Throwable e) {
                        mvpView.showLoadingProgress(false);
                        mvpView.showError("下载异常，请稍后再试");
                    }

                    @Override
                    public void onNext(String s) {
                        mvpView.showLoadingProgress(false);
                        mvpView.installApp(s);
                    }
                });
    }


    @Override
    public void attachView(UpAppMvpView mvpView) {
        this.mvpView = mvpView;
    }

    @Override
    public void detachView() {
        if (mvpView.bindEvents()) {
            mDataManager.getEventBus().unregister(this);
        }
        mvpView = null;
        if (mSubscription != null) mSubscription.unsubscribe();
    }
}
