package com.abings.baby.ui.setting;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.abings.baby.R;
import com.abings.baby.ui.base.BaseActivity;
import com.abings.baby.ui.base.BaseFragment;
import com.abings.baby.ui.login.LoginActivity;
import com.abings.baby.ui.setting.about.AboutActivity;
import com.abings.baby.ui.setting.passwd.PasswdActivity;
import com.abings.baby.utils.CustomAlertDialog;
import com.abings.baby.widget.ToggleButton;

import java.util.List;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * 作者：黄斌 on 2016/2/28 19:07
 * 说明：
 */
public class SettingFragment extends BaseFragment implements SettingMvpView {
    @Inject
    SettingPresenter settingPresenter;
    @Bind(R.id.about)
    TextView about;
    @Bind(R.id.change_pwd)
    TextView change_pwd;
    @Bind(R.id.clear)
    TextView clear;

    @Bind(R.id.btn_public)
    ToggleButton btn_public;

    @Bind(R.id.btn_notice)
    ToggleButton btn_notice;

    @Bind(R.id.main_lay)
    View main_lay;
    @Bind(R.id.exit)
    Button exit;

    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.fragment_setting;
    }

    @Override
    protected void iniInjector() {
        ((BaseActivity) getActivity()).activityComponent().inject(this);
        settingPresenter.attachView(this);
    }

    @Override
    protected void onFirstUserVisible() {

    }

    @Override
    protected View getLoadingTargetView() {
        return main_lay;
    }

    @Override
    protected void initViewsAndEvents() {
        btn_public.setOnToggleChanged(new ToggleButton.OnToggleChanged() {
            @Override
            public void onToggle(boolean on) {

            }
        });

        btn_notice.setOnToggleChanged(new ToggleButton.OnToggleChanged() {
            @Override
            public void onToggle(boolean on) {

            }
        });
    }

    @Override
    protected void onUserVisible() {

    }

    @Override
    protected void onUserInvisible() {

    }

    @Override
    public boolean bindEvents() {
        return false;
    }

    @Override
    public void showData(List data, boolean canNext) {

    }

    @Override
    public void showLoadingProgress(boolean show) {

    }

    @Override
    public void showMessage(String mes) {

    }

    @Override
    public void showError(String mes) {

    }

    @Override
    public void onTokenError() {

    }
    private AlertDialog myDialog = null;
    private TextView textView;

    private void creatAlertDialog() {

        myDialog = new AlertDialog.Builder(getActivity()).create();

        myDialog.show();

        myDialog.getWindow().setContentView(R.layout.alert_dialog_layout);
        myDialog.setCanceledOnTouchOutside(false);

        textView = (TextView) myDialog.getWindow().findViewById(R.id.input);
        textView.setText("确认要退出吗？");
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
                        preferences = getActivity().getSharedPreferences("login", getActivity().MODE_PRIVATE);
                        editor = preferences.edit();
                        editor.putString("userName", null);
                        editor.putString("password", null);
                        editor.commit();
                        startActivity(new Intent(getActivity(), LoginActivity.class));
                        getActivity().finish();
                    }

                });

    }

    @OnClick({R.id.change_pwd, R.id.clear, R.id.about, R.id.exit})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.change_pwd:
                startActivity(new Intent(getActivity(), PasswdActivity.class));
                break;
            case R.id.clear:
                break;
            case R.id.about:
                startActivity(new Intent(getActivity(), AboutActivity.class));
                break;
            case R.id.exit:
                preferences = getActivity().getSharedPreferences("login", getActivity().MODE_PRIVATE);
                editor = preferences.edit();
                CustomAlertDialog.dialogExit("确认要退出吗？",this,editor);
                break;
        }
    }
}
