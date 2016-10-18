package com.abings.baby.ui.publish;

import com.abings.baby.ui.base.IBaseView;


public interface PublishMvpView extends IBaseView {

    void showDialog(String msg);

    void dialogDismiss();

    void uploadCompleted();

    void onTokenError();

}
