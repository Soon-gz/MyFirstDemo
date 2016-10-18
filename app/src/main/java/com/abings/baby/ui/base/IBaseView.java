package com.abings.baby.ui.base;

import java.util.List;

/**
 * Created by Administrator on 2016/1/18.
 */
public interface IBaseView<T> {

    boolean bindEvents();//是否需要在对应的控制器里绑定总线消息接听

    void showData(List<T> data, boolean canNext);

    void showLoadingProgress(boolean show);

    void showMessage(String mes);

    void showError(String mes);

    void onTokenError();

}
