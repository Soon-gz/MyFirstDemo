package com.abings.baby.ui.baby;

import com.abings.baby.ui.base.IBaseView;

/**
 * Created by Administrator on 2016/1/18.
 */
public interface BabyInfoMvpView extends IBaseView {
    void updateHeader(String name);
    void updateUserInfo(boolean result);
}
