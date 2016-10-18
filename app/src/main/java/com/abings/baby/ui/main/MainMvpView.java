package com.abings.baby.ui.main;


import com.abings.baby.data.model.TeacherDetail;
import com.abings.baby.data.model.UserDetail;
import com.abings.baby.ui.base.IBaseView;

public interface MainMvpView extends IBaseView {

    void onSignedOut();

    void updateView(UserDetail info);

    void updateView(TeacherDetail info);

    void updateUnReadCount(int count);
}
