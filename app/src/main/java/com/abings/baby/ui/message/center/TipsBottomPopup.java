package com.abings.baby.ui.message.center;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.widget.TextView;

import com.abings.baby.R;
import com.abings.baby.widget.basepopup.BasePopupWindow;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;

/**
 * Created by 大灯泡 on 2016/1/15.
 * 从底部滑上来的popup
 */
public class TipsBottomPopup extends BasePopupWindow implements View.OnClickListener {

    private View popupView;

    public TipsBottomPopup(Activity context) {
        super(context);
        bindEvent();
    }

    @Override
    protected Animation getShowAnimation() {
        return getTranslateAnimation(250 * 2, 0, 300);
    }

    @Override
    protected View getClickToDismissView() {
        return popupView;
    }

    @Override
    public View getPopupView() {
        popupView = LayoutInflater.from(mContext).inflate(R.layout.popup_message_center_normal, null);
        return popupView;
    }

    @Override
    public View getAnimaView() {
        return popupView.findViewById(R.id.popup_anima);
    }

    private void bindEvent() {
        if (popupView != null) {
            popupView.setOnClickListener(this);
        }
    }

    public onPopupClickLitener getListner() {
        return listner;
    }

    public TipsBottomPopup setListner(onPopupClickLitener listner) {
        this.listner = listner;
        return this;
    }

    public void showPopupWindow() {
        super.showPopupWindow();
    }

    public TipsBottomPopup setDate(String count, String date) {
        TextView txdate = (TextView) popupView.findViewById(R.id.date);
        txdate.setText("更新日期：" + date);
        TextView txcount = (TextView) popupView.findViewById(R.id.count);
        txcount.setText(count + "封未读");
        return this;
    }

    private onPopupClickLitener listner;

    public interface onPopupClickLitener {
        void onPopupClick(View v);
    }

    @Override
    public void onClick(View v) {
        if (listner == null) {
            return;
        }
        listner.onPopupClick(v);
    }

}
