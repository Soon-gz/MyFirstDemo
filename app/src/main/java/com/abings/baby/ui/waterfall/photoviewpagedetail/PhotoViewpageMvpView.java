package com.abings.baby.ui.waterfall.photoviewpagedetail;

import com.abings.baby.data.model.PhotoInfo;
import com.abings.baby.ui.base.IBaseView;

/**
 * Created by HaomingXu on 2016/7/20.
 */
public interface PhotoViewpageMvpView extends IBaseView {
    void showDialog(String msg);
    void updateView(PhotoInfo info);
    void finishActivity();
    void dialogDismiss();
}
