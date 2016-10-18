package com.abings.baby.ui.signin;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.widget.ImageView;

import com.abings.baby.R;
import com.abings.baby.widget.basepopup.BasePopupWindow;

public class NormalPopup extends BasePopupWindow implements View.OnClickListener {

    private View popupView;

    public NormalPopup(Activity context) {
        super(context);
        bindEvent();
    }


    @Override
    protected Animation getShowAnimation() {
        return getDefaultScaleAnimation();
    }


    @Override
    protected View getClickToDismissView() {
        return popupView.findViewById(R.id.click_to_dismiss);
    }

    @Override
    public View getPopupView() {
        popupView = LayoutInflater.from(mContext).inflate(R.layout.popup_normal, null);
        return popupView;
    }

    public ImageView getImageView() {
        ImageView img = (ImageView) popupView.findViewById(R.id.qrcode);
        return img;
    }

    @Override
    public View getAnimaView() {
        return popupView.findViewById(R.id.popup_anima);
    }

    private void bindEvent() {
    }

    @Override
    public void onClick(View v) {

    }
}
