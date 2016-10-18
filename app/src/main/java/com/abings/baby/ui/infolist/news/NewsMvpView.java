package com.abings.baby.ui.infolist.news;

import com.abings.baby.data.model.NewsDetail;
import com.abings.baby.ui.base.IBaseView;


public interface NewsMvpView extends IBaseView {
    void updateView(NewsDetail info);

    void updateFavorite(boolean flage);

    void showDialog(String msg);
    void finishActivity();
    void dialogDismiss();
}
