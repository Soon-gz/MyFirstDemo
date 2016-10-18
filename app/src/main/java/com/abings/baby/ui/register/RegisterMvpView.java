package com.abings.baby.ui.register;

import com.abings.baby.ui.base.IBaseView;


public interface RegisterMvpView extends IBaseView {
    void toLoginActivity(boolean isFirst);

    void showDialog();

    void dialogDismiss();
    void finishActivity();
}
