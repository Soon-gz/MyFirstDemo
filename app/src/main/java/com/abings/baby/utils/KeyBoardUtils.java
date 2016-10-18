package com.abings.baby.utils;

import android.app.Activity;
import android.content.Context;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

/**
 * Created by admin on 2015/6/29.
 */
public class KeyBoardUtils {
    /**
     * 打卡软键盘
     *
     * @param mEditText 输入框
     * @param mContext  上下文
     */
    public static void openKeybord(EditText mEditText, Context mContext) {
        InputMethodManager imm = (InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(mEditText, InputMethodManager.RESULT_SHOWN);
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED,InputMethodManager.HIDE_IMPLICIT_ONLY);
    }

    /**
     * 关闭软键盘
     *
     * @param mEditText 输入框
     * @param mContext  上下文
     */
    public static void closeKeybord(EditText mEditText, Context mContext) {
        InputMethodManager imm = (InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(mEditText.getWindowToken(), 0);
    }

    /**
     * 关闭软键盘
     *
     * @param mContext 上下文
     */
    public static void closeKeybord(Activity mContext) {
        InputMethodManager imm = (InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm.isActive() && mContext.getCurrentFocus() != null) {
            if(mContext.getCurrentFocus().getApplicationWindowToken()!=null){
                imm.hideSoftInputFromWindow(mContext.getCurrentFocus().getApplicationWindowToken(),InputMethodManager.HIDE_NOT_ALWAYS);// 隐藏软键盘
            }
        }else{
            hideKeybord(mContext);
        }
    }

/*    public static void hideKeybord(Activity mContext) {
        WindowManager.LayoutParams params = mContext.getWindow().getAttributes();
        // 隐藏软键盘
        mContext.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        params.softInputMode = WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN;
    }*/


    public static void hideKeybord(Activity mContext) {
        //WindowManager.LayoutParams params = mContext.getWindow().getAttributes();
        // 隐藏软键盘
        //mContext.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        //params.softInputMode = WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN;
        if (mContext.getCurrentFocus() != null) {
            ((InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE))
                    .hideSoftInputFromWindow(mContext.getCurrentFocus()
                                    .getWindowToken(),
                            InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }
}