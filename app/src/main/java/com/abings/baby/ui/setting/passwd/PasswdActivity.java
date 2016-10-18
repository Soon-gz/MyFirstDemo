package com.abings.baby.ui.setting.passwd;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.abings.baby.R;
import com.abings.baby.ui.base.BaseActivity;
import com.abings.baby.ui.login.LoginActivity;
import com.abings.baby.utils.CustomAlertDialog;
import com.abings.baby.utils.PasswordUtils;
import com.abings.baby.utils.ProgressDialogHelper;
import com.abings.baby.utils.StringUtils;
import com.abings.baby.widget.ClearEditText;

import java.util.List;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Created by huangbin on 16/2/19.
 */
public class PasswdActivity extends BaseActivity implements PasswdMvpView {

    @Inject
    PasswdPresenter passwdPresenter;
    @Bind(R.id.btn_back)
    TextView btnBack;
    @Bind(R.id.title_center)
    TextView titleCenter;
    @Bind(R.id.input_pwd)
    ClearEditText inputPwd;
    @Bind(R.id.layou_id)
    LinearLayout layouId;
    @Bind(R.id.input_new_psw)
    ClearEditText inputNewPsw;
    @Bind(R.id.input_new_psw2)
    ClearEditText inputNewPsw2;
    @Bind(R.id.btn_change)
    Button btnChange;

    @Bind(R.id.pwdChange_tv_new_psw)
    TextView tvNewPsw;

    private SharedPreferences preferences;

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.activity_passwd;
    }

    @Override
    protected void iniInjector() {
        activityComponent().inject(this);
        passwdPresenter.attachView(this);
    }

    @Override
    protected boolean toggleOverridePendingTransition() {
        return false;
    }

    @Override
    protected TransitionMode getOverridePendingTransitionMode() {
        return TransitionMode.LEFT;
    }

    @Override
    protected void getBundleExtras(Bundle extras) {
    }

    @Override
    protected void initViewsAndEvents(Bundle savedInstanceState) {
        inputNewPsw.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus){
                    tvNewPsw.setVisibility(View.GONE);
                }else{

                    String pwd = inputNewPsw.getText().toString().trim();

                    if(!PasswordUtils.isPwdAvailable(pwd)){
                        //提示密码过于简单
                        tvNewPsw.setVisibility(View.VISIBLE);

                    }
                }
            }
        });
    }

    @Override
    protected View getLoadingTargetView() {
        return null;
    }

    @Override
    protected boolean isApplyStatusBarTranslucency() {
        return true;
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
        if (show) {
            ProgressDialogHelper.getInstance().showProgressDialog(this, "正在载入...");
        } else {
            ProgressDialogHelper.getInstance().hideProgressDialog();
        }
    }

    @Override
    public void showMessage(String mes) {
        Toast.makeText(PasswdActivity.this, mes, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showError(String mes) {

    }

    @Override
    public void onTokenError() {

    }


    @OnClick(R.id.btn_back)
    void toBack() {
        if (StringUtils.isEmpty(inputPwd.getText().toString()) || StringUtils.isEmpty(inputNewPsw.getText().toString()) || StringUtils.isEmpty(inputNewPsw2.getText().toString())) {
            finish();
        }else{
            CustomAlertDialog.dialogWithSureCancel("确定放弃编辑页面？", this);
        }
    }

    @OnClick(R.id.btn_change)
    void Passwd() {
        //修改密码
        if (StringUtils.isEmpty(inputPwd.getText().toString()) || StringUtils.isEmpty(inputNewPsw.getText().toString()) || StringUtils.isEmpty(inputNewPsw2.getText().toString())) {
            showToast("请输入密码");
            return;
        }
        preferences = getSharedPreferences("login", LoginActivity.MODE_PRIVATE);
        String oldpass = preferences.getString("password","");
        String phone = preferences.getString("userName", "");

        if(!PasswordUtils.isPwdLengthRange(inputPwd.getText().toString().trim())){
            showToast(getResources().getString(R.string.pwd_length_prompt));
            return;
        }

        if ((!StringUtils.isEmpty(preferences.getString("password","")) && !inputPwd.getText().toString().equals(preferences.getString("password",""))) ) {
            showToast("旧密码不正确！");
            return;
        }
        if (!inputNewPsw2.getText().toString().equals(inputNewPsw.getText().toString())) {
            showToast("两次输入的新密码不一致");
            return;
        }

        if(StringUtils.isEmpty(phone)){
            showToast("用户电话号码没有记录，请重新输入账号登录！");
            return;
        }
        passwdPresenter.changePassWord(phone,oldpass,inputNewPsw.getText().toString().trim());
    }

    @Override
    public void showDialog() {

    }

    @Override
    public void dialogDismiss() {

    }

    @Override
    public void finishActivity() {
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        passwdPresenter.detachView();
    }
}
