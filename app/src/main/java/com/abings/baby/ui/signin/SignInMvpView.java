package com.abings.baby.ui.signin;

import com.abings.baby.data.model.Attend;
import com.abings.baby.ui.base.IBaseView;


public interface SignInMvpView extends IBaseView {
    //展现签到详情
    public void showAttend(Attend attend);
}
