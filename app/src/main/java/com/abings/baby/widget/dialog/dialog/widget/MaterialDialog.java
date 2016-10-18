package com.abings.baby.widget.dialog.dialog.widget;

import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import com.abings.baby.widget.dialog.dialog.utils.CornerUtils;
import com.abings.baby.widget.dialog.dialog.widget.internal.BaseAlertDialog;



/**
 * Dialog like Material Design Dialog
 */
public class MaterialDialog extends BaseAlertDialog<MaterialDialog> {

    private int contentResId = -1;
    private View newContent;

    public MaterialDialog(Context context,int resId) {
        super(context);
        iniDefaultValue();
        contentResId = resId;
    }

    public MaterialDialog(Context context) {
        super(context);
        iniDefaultValue();
    }

    private void iniDefaultValue(){
        /** default value*/
        mTitleTextColor = Color.parseColor("#DE000000");
        mTitleTextSize = 18f;
        mContentTextColor = Color.parseColor("#8a000000");
        mContentTextSize = 16f;
        mLeftBtnTextColor = Color.parseColor("#383838");
        mRightBtnTextColor = Color.parseColor("#468ED0");
        mMiddleBtnTextColor = Color.parseColor("#00796B");
        /** default value*/
    }

    @Override
    public View onCreateView() {

        /** title */
        mTvTitle.setGravity(Gravity.CENTER_VERTICAL);
        mTvTitle.setPadding(dp2px(20), dp2px(20), dp2px(20), dp2px(0));
        mTvTitle.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT));
        mLlContainer.addView(mTvTitle);

        /** content */
        mTvContent.setPadding(dp2px(20), dp2px(20), dp2px(20), dp2px(20));
        mTvContent.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT));
        //如果有自定义内容，就不载入默认的textview内容了，原来的内容设置也取消
        if (contentResId > 0){
            LayoutInflater inflater = LayoutInflater.from(getContext());
            newContent = inflater.inflate(contentResId,null);
            mLlContainer.addView(newContent);
        } else {
            mLlContainer.addView(mTvContent);
        }

        /**btns*/
        mLlBtns.setGravity(Gravity.RIGHT);
        mLlBtns.addView(mTvBtnLeft);
        mLlBtns.addView(mTvBtnMiddle);
        mLlBtns.addView(mTvBtnRight);

        mTvBtnLeft.setPadding(dp2px(15), dp2px(8), dp2px(15), dp2px(8));
        mTvBtnRight.setPadding(dp2px(15), dp2px(8), dp2px(15), dp2px(8));
        mTvBtnMiddle.setPadding(dp2px(15), dp2px(8), dp2px(15), dp2px(8));
        mLlBtns.setPadding(dp2px(20), dp2px(0), dp2px(10), dp2px(10));

        mLlContainer.addView(mLlBtns);

        return mLlContainer;
    }

    public View getNewContent(){
        return newContent;
    }

    @Override
    public void setUiBeforShow() {
        super.setUiBeforShow();
        /**set background color and corner radius */
        float radius = dp2px(mCornerRadius);
        mLlContainer.setBackgroundDrawable(CornerUtils.cornerDrawable(mBgColor, radius));
        mTvBtnLeft.setBackgroundDrawable(CornerUtils.btnSelector(radius, mBgColor, mBtnPressColor, -2));
        mTvBtnRight.setBackgroundDrawable(CornerUtils.btnSelector(radius, mBgColor, mBtnPressColor, -2));
        mTvBtnMiddle.setBackgroundDrawable(CornerUtils.btnSelector(radius, mBgColor, mBtnPressColor, -2));
    }
}
