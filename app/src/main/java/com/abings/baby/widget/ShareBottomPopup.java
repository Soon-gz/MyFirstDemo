package com.abings.baby.widget;

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
public class ShareBottomPopup extends BasePopupWindow implements View.OnClickListener {

    private View popupView;

    public ShareBottomPopup(Activity context) {
        super(context);
        bindEvent();
    }

    @Override
    protected Animation getShowAnimation() {
        return getTranslateAnimation(250 * 2, 0, 300);
    }

    @Override
    protected View getClickToDismissView() {
        return popupView.findViewById(R.id.click_to_dismiss);
    }

    @Override
    public View getPopupView() {
        popupView = LayoutInflater.from(mContext).inflate(R.layout.popup_slide_from_bottom, null);
        return popupView;
    }

    @Override
    public View getAnimaView() {
        return popupView.findViewById(R.id.popup_anima);
    }

    private void bindEvent() {
        if (popupView != null) {
            popupView.findViewById(R.id.weixin).setOnClickListener(this);
            popupView.findViewById(R.id.pyq).setOnClickListener(this);
            popupView.findViewById(R.id.qq).setOnClickListener(this);
            popupView.findViewById(R.id.weibo).setOnClickListener(this);
        }
    }

    public onItemClickLitener getListner() {
        return listner;
    }

    public ShareBottomPopup setListner(onItemClickLitener listner) {
        this.listner = listner;
        return this;
    }

    private onItemClickLitener listner;

    public interface onItemClickLitener {
        void onItemClick(int position);
    }

    @Override
    public void onClick(View v) {
        if (listner == null) {
            return;
        }
        switch (v.getId()) {
            case R.id.weixin:
                listner.onItemClick(0);
                break;
            case R.id.pyq:
                listner.onItemClick(1);
                break;
            case R.id.qq:
                listner.onItemClick(2);
                break;
            case R.id.weibo:
                listner.onItemClick(3);
                break;
            default:
                break;
        }
    }

    public void shareWeixin(Activity activity, UMImage umImage, UMShareListener listener) {
        new ShareAction(activity).setPlatform(SHARE_MEDIA.WEIXIN).setCallback(listener)
                .withMedia(umImage)
                .withText("hello baby")
                .share();
    }

    public void shareQQ(Activity activity, UMImage umImage, UMShareListener listener) {
        new ShareAction(activity).setPlatform(SHARE_MEDIA.QQ).setCallback(listener)
                .withText("hello baby")
                .withMedia(umImage)
                .withTitle("qqshare")
                .withTargetUrl("http://dev.umeng.com")
                .share();
    }

    public void shareWeixinCircle(Activity activity, UMImage umImage, UMShareListener listener) {
        new ShareAction(activity).setPlatform(SHARE_MEDIA.WEIXIN_CIRCLE).setCallback(listener)
                .withMedia(umImage)
                .share();
    }

    public void shareWeibo(Activity activity, UMImage umImage, UMShareListener listener) {
        new ShareAction(activity).setPlatform(SHARE_MEDIA.SINA).setCallback(listener)
                .withText("hello baby")
                .withMedia(umImage)
                .share();
    }
}
