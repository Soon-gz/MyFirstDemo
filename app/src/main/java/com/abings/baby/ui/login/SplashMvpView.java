package com.abings.baby.ui.login;

import com.abings.baby.ui.base.IBaseView;

import java.util.List;

/**
 * Created by HaomingXu on 2016/7/15.
 */
public interface SplashMvpView extends IBaseView {
    void addBaby(List bbs);

    void addClass(List cls);
    void toMainActivity(boolean isFirst);
    void toLoginActivity();
}
