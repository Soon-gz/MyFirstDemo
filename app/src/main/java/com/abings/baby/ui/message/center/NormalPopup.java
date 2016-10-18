package com.abings.baby.ui.message.center;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.widget.ImageView;
import android.widget.TextView;

import com.abings.baby.R;
import com.abings.baby.utils.DateUtil;
import com.abings.baby.widget.basepopup.BasePopupWindow;

import butterknife.Bind;

public class NormalPopup extends BasePopupWindow implements View.OnClickListener {

    TextView date;
    TextView count;
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
        popupView = LayoutInflater.from(mContext).inflate(R.layout.popup_message_center_normal, null);
        date = (TextView) popupView.findViewById(R.id.date);
        count = (TextView) popupView.findViewById(R.id.count);
        popupView.setOnClickListener(this);
        return popupView;
    }

    @Override
    public View getAnimaView() {
        return popupView.findViewById(R.id.popup_anima);
    }

    public NormalPopup setListner(onItemClickLitener listner) {
        this.listner = listner;
        return this;
    }

    public NormalPopup setUnReadMsgCount(int num) {
        date.setText(" 更新日期：" + DateUtil.getCurrentTime("yyyy/MM/dd"));
        count.setText(num + "封新消息未读取");
        return this;
    }

    private onItemClickLitener listner;

    public interface onItemClickLitener {
        void onItemClick(View v);
    }

    private void bindEvent() {
    }

    @Override
    public void onClick(View v) {
        if (listner == null) {
            return;
        }
        listner.onItemClick(v);
    }
}
