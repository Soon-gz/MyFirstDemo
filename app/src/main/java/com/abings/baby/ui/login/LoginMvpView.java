package com.abings.baby.ui.login;

import com.abings.baby.ui.base.IBaseView;

import java.util.List;


public interface LoginMvpView extends IBaseView {

    void toMainActivity(boolean isFirst);

    void showDialog();

    void dialogDismiss();

    void addBaby(List bbs);

    void saveName(String name,String pass);

    void addClass(List cls);

}
