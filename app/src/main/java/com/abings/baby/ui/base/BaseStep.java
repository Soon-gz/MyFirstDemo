package com.abings.baby.ui.base;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.widget.EditText;

import java.util.regex.Pattern;

/**
 * Created by huangbin on 16/2/16.
 */
public abstract class BaseStep {

    protected Context context;
    protected Activity activity;
    protected OnNextActionListener mOnNextActionListener;
    private View mRootView;

    public BaseStep(Activity activity,View mRootView){
        this.context = (Context) activity;
        this.activity = activity;
        this.mRootView = mRootView;
        iniView();
        iniEvents();
    }

    public abstract void iniView();
    public abstract void iniEvents();
    public abstract boolean validate();
    public abstract boolean isChange();

    public View findViewById(int resId){
      return  mRootView.findViewById(resId);
    }

    public void doPrevious() {

    }

    public void doNext() {

    }

    public void setOnNextActionListener(OnNextActionListener listener){
        this.mOnNextActionListener = listener;
    }

    public  interface OnNextActionListener{
        void  next();
    }


    protected boolean isNull(EditText editText) {
        String text = editText.getText().toString().trim();
        if (text != null && text.length() > 0) {
            return false;
        }
        return true;
    }

    protected boolean matchPhone(String text) {
        if (Pattern.compile("(\\d{11})|(\\+\\d{3,})").matcher(text).matches()) {
            return true;
        }
        return false;
    }

    protected boolean matchEmail(String text) {
        if (Pattern.compile("\\w[\\w.-]*@[\\w.]+\\.\\w+").matcher(text)
                .matches()) {
            return true;
        }
        return false;
    }


}
