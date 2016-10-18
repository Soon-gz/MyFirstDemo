package com.abings.baby.ui.home2;

import com.abings.baby.ui.base.IBaseView;

import java.util.List;

/**
 * Created by Administrator on 2016/1/18.
 */
public interface HomeMvpView extends IBaseView {
    public void updateMainPageList(List data);

    public void updateNewsList(List data);
}
