package com.abings.baby.ui.base;

/**
 * Every presenter in the app must either implement this interface or extend BasePresenter
 * indicating the MvpView type that wants to be attached with.
 */
public interface Presenter<V extends IBaseView> {

    void attachView(V mvpView);

    void detachView();
}
