package com.abings.baby.utils;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;

import com.abings.baby.R;
import com.abings.baby.data.model.Attend;
import com.abings.baby.ui.login.LoginActivity;
import com.abings.baby.ui.setting.SettingFragment;
import com.abings.baby.ui.signin.SignInPresenter;

/**
 * Created by HaomingXu on 2016/8/3.
 */
public class CustomAlertDialog {
    private static AlertDialog myDialog = null;

    /**
     *活动界面专用对话框
     *@author Shuwen
     *created at 2016/8/3 10:17
     */
    public static void dialogExSureCancel(String mes, final Context context) {

        myDialog = new AlertDialog.Builder(context).create();

        myDialog.show();

        myDialog.getWindow().setContentView(R.layout.alert_dialog_layout);
        TextView textView = (TextView) myDialog.getWindow().findViewById(R.id.input);
        textView.setText(mes);
        myDialog.setCanceledOnTouchOutside(false);
        myDialog.getWindow()
                .findViewById(R.id.alert_btn_cancel0)
                .setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {

                        myDialog.dismiss();

                    }

                });

        myDialog.getWindow()
                .findViewById(R.id.alert_btn_sure0)
                .setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {

                        myDialog.dismiss();
                        dialogWithSure("取消报名成功！", context);
                    }

                });
    }

    /**
     *对话框带确认和取消
     *@author Shuwen
     *created at 2016/8/3 10:17
     */
    public static void dialogWithSureCancel(String mes, final Activity context) {

        myDialog = new AlertDialog.Builder(context).create();

        myDialog.show();

        myDialog.getWindow().setContentView(R.layout.alert_dialog_layout);
        TextView textView = (TextView) myDialog.getWindow().findViewById(R.id.input);
        textView.setText(mes);
        myDialog.setCanceledOnTouchOutside(false);
        myDialog.getWindow()
                .findViewById(R.id.alert_btn_cancel0)
                .setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {

                        myDialog.dismiss();

                    }

                });

        myDialog.getWindow()
                .findViewById(R.id.alert_btn_sure0)
                .setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {

                        myDialog.dismiss();
                        context.finish();
                    }

                });
    }

    public static AlertDialog dialogWithSureCancelClick(String mes, final Activity context, final SignInPresenter signInPresenter, final Attend a, final String leave) {

        myDialog = new AlertDialog.Builder(context).create();

        myDialog.show();

        myDialog.getWindow().setContentView(R.layout.alert_dialog_layout);
        TextView textView = (TextView) myDialog.getWindow().findViewById(R.id.input);
        textView.setText(mes);
        myDialog.setCanceledOnTouchOutside(false);
        myDialog.getWindow()
                .findViewById(R.id.alert_btn_cancel0)
                .setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {

                        myDialog.dismiss();

                    }

                });

        myDialog.getWindow()
                .findViewById(R.id.alert_btn_sure0)
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        myDialog.dismiss();
                        signInPresenter.attendAdd(a,leave);
                    }
                });
        return myDialog;
    }



    /**
     *对话框只带确认按钮
     *@author Shuwen
     *created at 2016/8/3 10:16
     */
    public static void dialogWithSure(String mes,Context context) {

        myDialog = new AlertDialog.Builder(context).create();

        myDialog.show();

        myDialog.getWindow().setContentView(R.layout.alert_dialog);
        myDialog.setCanceledOnTouchOutside(false);


        TextView textView = (TextView) myDialog.getWindow().findViewById(R.id.input0);
        textView.setText(mes);
        myDialog.getWindow()
                .findViewById(R.id.alert_btn_sure1)
                .setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {

                        myDialog.dismiss();

                    }

                });

    }


    /**
     *对话框带输入
     *@author Shuwen
     *created at 2016/8/3 10:16
     */
    public static void dialogWithInput(final Context context,EditText people) {

        myDialog = new AlertDialog.Builder(context).create();

        myDialog.setCanceledOnTouchOutside(false);
        myDialog.show();
        myDialog.getWindow().setContentView(R.layout.alert_dialog_withedittext_layout);

        myDialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
        myDialog.getWindow()
                .findViewById(R.id.alert_btn_cancel)
                .setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {

                        myDialog.dismiss();

                    }

                });
        people = (EditText) myDialog.getWindow().findViewById(R.id.people);

        final EditText finalPeople = people;
        myDialog.getWindow()
                .findViewById(R.id.alert_btn_sure)
                .setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        if (!StringUtils.isEmpty(finalPeople.getText().toString())) {
                            myDialog.dismiss();
                            dialogWithSure("报名成功！", context);
                        } else {
                            dialogWithSure("人数不能为空！", context);
                        }

                    }

                });
    }


    /**
     *设置界面退出时的对话框
     *@author Shuwen
     *created at 2016/8/3 15:09
     */
    public static void dialogExit(String mes, final SettingFragment  settingFragment,final SharedPreferences.Editor editor) {

        myDialog = new AlertDialog.Builder(settingFragment.getActivity()).create();

        myDialog.show();
        myDialog.getWindow().setContentView(R.layout.alert_dialog_layout);
        myDialog.setCanceledOnTouchOutside(false);

        TextView textView = (TextView) myDialog.getWindow().findViewById(R.id.input);
        textView.setText(mes);
        myDialog.getWindow()
                .findViewById(R.id.alert_btn_cancel0)
                .setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {

                        myDialog.dismiss();

                    }

                });

        myDialog.getWindow()
                .findViewById(R.id.alert_btn_sure0)
                .setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {

                        myDialog.dismiss();
                        editor.putString("userName", null);
                        editor.putString("password", null);
                        editor.commit();
                        settingFragment.startActivity(new Intent(settingFragment.getActivity(), LoginActivity.class));
                        settingFragment.getActivity().finish();
                    }

                });

    }

    /**
     * 进入网络设置界面
     * @param context
     */
    public static void dialogNetSet(final Context context) {

        myDialog = new AlertDialog.Builder(context).create();

        myDialog.show();

        myDialog.getWindow().setContentView(R.layout.alert_dialog_layout);
        TextView textView = (TextView) myDialog.getWindow().findViewById(R.id.input);
        textView.setText("网络异常，请检查网络设置");
        myDialog.setCanceledOnTouchOutside(false);
        myDialog.getWindow()
                .findViewById(R.id.alert_btn_cancel0)
                .setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        myDialog.dismiss();

                    }
                });

        myDialog.getWindow()
                .findViewById(R.id.alert_btn_sure0)
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent("/");
                        if (android.os.Build.VERSION.SDK_INT > 10) {
                            intent = new Intent(
                                    android.provider.Settings.ACTION_SETTINGS);
                        } else {
                            intent = new Intent();
                            ComponentName component = new ComponentName(
                                    "com.android.settings",
                                    "com.android.settings.Settings");
                            intent.setComponent(component);
                            intent.setAction("android.intent.action.VIEW");
                        }
                        context.startActivity(intent);
                        myDialog.dismiss();
                    }
                });
    }
}
