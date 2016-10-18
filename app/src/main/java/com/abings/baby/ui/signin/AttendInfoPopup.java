package com.abings.baby.ui.signin;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.widget.TextView;

import com.abings.baby.R;
import com.abings.baby.data.model.Attend;
import com.abings.baby.data.remote.RetrofitUtils;
import com.abings.baby.utils.CustomAlertDialog;
import com.abings.baby.utils.DateUtil;
import com.abings.baby.utils.ImageLoaderUtil;
import com.abings.baby.widget.basepopup.BasePopupWindow;

import java.util.Arrays;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * 签到详情
 */
public class AttendInfoPopup extends BasePopupWindow implements View.OnClickListener {

    private View popupView;
    private Attend mAttend;
    private SignInActivity mSigninActivity;
    private SignInPresenter mSignInPresenter;
    //宝宝信息
    private CircleImageView ivBabyHead;
    private TextView tvBabyName;
    private TextView tvBabySchool;
    private TextView tvBabyClass;

    //关系人信息
    private CircleImageView ivAttendHead;
    private TextView tvAttendName;
    private TextView tvAttendRelation;
    private TextView tvAttendPhone;
    //下面三个按钮
    private TextView tvIn;
    private TextView tvOut;
    private TextView tvCancel;

    public AttendInfoPopup(SignInActivity context, Attend attend, SignInPresenter signInPresenter) {
        super(context);
        this.mAttend = attend;
        this.mSigninActivity = context;
        this.mSignInPresenter = signInPresenter;
        bindEvent();

    }


    @Override
    protected Animation getShowAnimation() {
        return getDefaultScaleAnimation();
    }


    @Override
    protected View getClickToDismissView() {
        return popupView.findViewById(R.id.popupAttend_rl_parent);
    }

    @Override
    public View getPopupView() {
        popupView = LayoutInflater.from(mContext).inflate(R.layout.popup_attend, null);
        return popupView;
    }


    @Override
    public View getAnimaView() {
        return popupView.findViewById(R.id.popupAttend_rl_parent);
    }

    private void bindEvent() {
        //
        ivBabyHead = findImageViewById(R.id.popupAttend_iv_babyHead);
        tvBabyName = findTextViewById(R.id.popupAttend_tv_babyName);
        tvBabySchool = findTextViewById(R.id.popupAttend_tv_babySchool);
        tvBabyClass = findTextViewById(R.id.popupAttend_tv_babyClass);
        //
        ivAttendHead = findImageViewById(R.id.popupAttend_iv_attendHead);
        tvAttendName = findTextViewById(R.id.popupAttend_tv_attendName);
        tvAttendRelation = findTextViewById(R.id.popupAttend_tv_attendRelation);
        tvAttendPhone = findTextViewById(R.id.popupAttend_tv_attendPhone);
        //
        tvIn = findTextViewById(R.id.popupAttend_tv_in);
        tvOut = findTextViewById(R.id.popupAttend_tv_out);
        tvCancel = findTextViewById(R.id.popupAttend_tv_cancel);


        ImageLoaderUtil.loadAttendHeadImage(mContext, RetrofitUtils.BASE_BABY_PHOTO_URL, mAttend.getPhoto(), R.drawable.msg_content, R.drawable.msg_content, ivBabyHead);
        tvBabyName.setText(mAttend.getName());
        tvBabySchool.setText(mAttend.getSchool_name());
        tvBabyClass.setText(mAttend.getClass_name());

        ImageLoaderUtil.loadAttendHeadImage(mContext, RetrofitUtils.BASE_USER_PHOTO_URL, mAttend.getUser_photo(), R.drawable.msg_content, R.drawable.msg_content, ivAttendHead);
        tvAttendName.setText(mAttend.getUser_name());
        tvAttendPhone.setText(mAttend.getMobile_phone());
        tvAttendRelation.setText(mAttend.getRelation_desc());


        tvIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //入园
                String nowTime = DateUtil.getDate();
                String[]times = nowTime.split(":");
                String hour = times[0];
                String m = times[1];
                if (Integer.parseInt(hour) > 12){
                    String msg = "当前时间为"+hour+":"+m+"是否确认入校";
                    CustomAlertDialog.dialogWithSureCancelClick(msg, mContext,mSignInPresenter,mAttend,"0");
                }else{
                    mSignInPresenter.attendAddIn(mAttend);
                }
            }
        });
        tvOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //出园
                String nowTime = DateUtil.getDate();
                String[]times = nowTime.split(":");
                String hour = times[0];
                String m = times[1];
                if (Integer.parseInt(hour) < 12){
                    String msg = "当前时间为'"+hour+":"+m+"'是否确认离校";
                    CustomAlertDialog.dialogWithSureCancelClick(msg,mContext,mSignInPresenter,mAttend,"1");
                }else{
                    mSignInPresenter.attendAddOut(mAttend);
                }

            }
        });
        tvCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSigninActivity.reStartPreview();
                dismiss();
            }
        });

    }


    @Override
    public void onClick(View v) {

    }

    private TextView findTextViewById(int id) {
        return (TextView) popupView.findViewById(id);
    }

    private CircleImageView findImageViewById(int id) {
        return (CircleImageView) popupView.findViewById(id);
    }
}
