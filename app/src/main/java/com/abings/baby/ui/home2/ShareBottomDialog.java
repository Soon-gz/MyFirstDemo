package com.abings.baby.ui.home2;

import android.app.Activity;
import android.content.Context;
import android.view.View;

import com.abings.baby.R;
import com.flyco.dialog.widget.base.BottomBaseDialog;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;

public class ShareBottomDialog extends BottomBaseDialog<ShareBottomDialog> implements View.OnClickListener {

    public ShareBottomDialog(Context context, View animateView) {
        super(context, animateView);
    }

    public ShareBottomDialog(Context context) {
        super(context);
    }

    @Override
    public View onCreateView() {
//        showAnim(new FlipVerticalSwingEnter());
        dismissAnim(null);
        View inflate = View.inflate(mContext, R.layout.popup_slide_from_bottom, null);
        inflate.findViewById(R.id.weixin).setOnClickListener(this);
        inflate.findViewById(R.id.pyq).setOnClickListener(this);
        inflate.findViewById(R.id.qq).setOnClickListener(this);
        inflate.findViewById(R.id.weibo).setOnClickListener(this);
        return inflate;
    }

    @Override
    public void setUiBeforShow() {
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

    public onItemClickLitener getListner() {
        return listner;
    }

    public ShareBottomDialog setListner(onItemClickLitener listner) {
        this.listner = listner;
        return this;
    }

    private onItemClickLitener listner;

    public interface onItemClickLitener {
        void onItemClick(int position);
    }

    public void shareWeixin(Activity activity, String title, String text, UMImage umImage, UMShareListener listener,Boolean is_log) {
        ShareAction action = new ShareAction(activity).setPlatform(SHARE_MEDIA.WEIXIN).setCallback(listener);
        if (umImage != null) {
            action.withMedia(umImage);
        }else{
            action.withTitle(title)
            .withText(text);
        }
        if(is_log) {
            action.withTitle(title)
                    .withText(text)
                    .withTargetUrl(umImage.toUrl());
        }
        action.share();
    }

    public void shareQQ(Activity activity, String title, String text, UMImage umImage, UMShareListener listener) {
        ShareAction action = new ShareAction(activity).setPlatform(SHARE_MEDIA.QQ).setCallback(listener);
        if (umImage != null) {
            action.withMedia(umImage);
        }else{
            action.withTitle(title)
                    .withText(text);
        }
        action.share();
    }

    public void shareWeixinCircle(Activity activity, String title, String text, UMImage umImage, UMShareListener listener,Boolean is_log) {
        ShareAction action = new ShareAction(activity).setPlatform(SHARE_MEDIA.WEIXIN_CIRCLE).setCallback(listener);
        if (umImage != null) {
            action.withMedia(umImage);
        }else{
            action.withTitle(title)
                    .withText(text);
        }
        if(is_log) {
            action.withTitle(title)
                    .withText(text)
                    .withTargetUrl(umImage.toUrl());
        }
        action.share();
    }

    public void shareWeibo(Activity activity, String title, String text, UMImage umImage, UMShareListener listener) {
        ShareAction action = new ShareAction(activity).setPlatform(SHARE_MEDIA.SINA).setCallback(listener);
        if (umImage != null) {
            action.withMedia(umImage);
        }
        action.withTitle(title)
                .withText(text)
                .share();
    }
}
