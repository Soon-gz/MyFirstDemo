package com.abings.baby.ui.login;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.abings.baby.R;
import com.abings.baby.WineApplication;
import com.abings.baby.ui.base.BaseActivity;
import com.abings.baby.ui.main.MainActivity;
import com.abings.baby.ui.register.RegisterActivity;
import com.abings.baby.utils.ProgressDialogHelper;
import com.abings.baby.utils.ScreenUtils;
import com.abings.baby.utils.StringUtils;
import com.abings.baby.utils.SystemStatusManager;
import com.socks.library.KLog;

import java.util.List;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Created by huangbin on 16/2/17.
 */
public class LoginActivity extends BaseActivity implements LoginMvpView {

    @Inject
    LoginPresenter loginPresenter;

    @Bind(R.id.main_lay)
    View main_lay;
    @Bind(R.id.input_passwd)
    EditText input_passwd;
    @Bind(R.id.input_acount)
    EditText input_acount;
    @Bind(R.id.login_label_acount)
    LinearLayout login_label_acount;
    @Bind(R.id.login_label_passwd)
    LinearLayout login_label_passwd;

    @Bind(R.id.logo)
    ImageView logo;

    private SharedPreferences preferences;

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.activity_login;
    }

    @Override
    protected void iniInjector() {
        activityComponent().inject(this);
        loginPresenter.attachView(this);
    }

    @Override
    protected boolean toggleOverridePendingTransition() {
        return false;
    }

    @Override
    protected TransitionMode getOverridePendingTransitionMode() {
        return null;
    }

    @Override
    protected void getBundleExtras(Bundle extras) {

    }

    @Override
    protected void initViewsAndEvents(Bundle savedInstanceState) {
        Window win = getWindow();
        WindowManager.LayoutParams winParams = win.getAttributes();
        final int bits = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
        winParams.flags |= bits;
        win.setAttributes(winParams);

        SystemStatusManager tintManager = new SystemStatusManager(this);
        //打开系统状态栏控制
        tintManager.setStatusBarTintEnabled(true);
        tintManager.setStatusBarTintColor(R.color.white);
        tintManager.setStatusBarTintResource(R.color.white);//设置背景
        KLog.e("ScreenUtils.getStatusHeight(this) = " + ScreenUtils.getStatusHeight(this));
        KLog.e("ScreenUtils.getStatusHeight(this) = " +ScreenUtils.getStatusHeight(this));
        View layoutAll = findViewById(R.id.layoutAll);
        if (layoutAll != null) {
            //设置系统栏需要的内偏移
            layoutAll.setPadding(0, ScreenUtils.getStatusHeight(this), 0, 0);
        }
        preferences = getSharedPreferences("login", LoginActivity.MODE_PRIVATE);
        String name = preferences.getString("userName","");
        String passWord = preferences.getString("password","");
        input_acount.setText(name);
        input_passwd.setText(passWord);

        if (WineApplication.getInstance().isTeacher()) {
            logo.setImageResource(R.drawable.logo_teacher);
        } else {
            logo.setImageResource(R.drawable.logo_user);
        }

        final int labelFocusTrue = 0xFFFA5746;
        final int labelFocusFalse = 0xFFF2F2F2;
        login_label_acount.setBackgroundColor(labelFocusTrue);
        login_label_passwd.setBackgroundColor(labelFocusFalse);
        input_acount.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus){
                    login_label_acount.setBackgroundColor(labelFocusTrue);
                    login_label_passwd.setBackgroundColor(TextUtils.isEmpty(input_passwd.getText().toString().trim())?labelFocusFalse:labelFocusTrue);
                }else {
                    login_label_acount.setBackgroundColor(TextUtils.isEmpty(input_passwd.getText().toString().trim())?labelFocusFalse:labelFocusTrue);
                }
            }
        });
        input_passwd.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus){
                    login_label_passwd.setBackgroundColor(labelFocusTrue);
                    login_label_acount.setBackgroundColor(TextUtils.isEmpty(input_acount.getText().toString().trim())?labelFocusFalse:labelFocusTrue);
                }else{
                    login_label_passwd.setBackgroundColor(TextUtils.isEmpty(input_acount.getText().toString().trim())?labelFocusFalse:labelFocusTrue);
                }
            }
        });

    }

    @Override
    protected View getLoadingTargetView() {
        return main_lay;
    }

    @Override
    protected boolean isApplyStatusBarTranslucency() {
        return false;
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
        Toast.makeText(this, mes, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showError(final String mes) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(LoginActivity.this, mes, Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onTokenError() {

    }

    @OnClick(R.id.btn_login)
    void Login(View v) {
        log("登陆");
        if (!StringUtils.isEmpty(input_acount.getText().toString().trim()) && !StringUtils.isEmpty(input_passwd.getText().toString().trim())) {
            loginPresenter.MemberLogin(input_acount.getText().toString().trim(), input_passwd.getText().toString().trim());
        } else {
            showMessage("用户名或密码不能为空！");
        }

    }

    @OnClick(R.id.btn_forgetPw)
    void ForgetPw() {
        Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
        intent.putExtra("type", 2);
        startActivity(intent);
    }

    @OnClick(R.id.btn_register)
    void Register() {
        Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
        intent.putExtra("type", 1);
        startActivity(intent);
    }


    @Override
    public void toMainActivity(boolean isFirst) {
        if (isFirst) {
            startActivity(MainActivity.class, new Bundle());
        }
        finish();
    }

    @Override
    public void showDialog() {
            ProgressDialogHelper.getInstance().showProgressDialog(this, "正在载入...");
    }

    @Override
    public void dialogDismiss() {
        ProgressDialogHelper.getInstance().hideProgressDialog();
    }

    @Override
    public void addBaby(List bbs) {
        ((WineApplication) getApplication()).getMyBabys().clear();
        ((WineApplication) getApplication()).getMyBabys().addAll(bbs);
    }

    @Override
    public void saveName(String name, String pass) {
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("userName",name);
        editor.putString("password",pass);
        editor.commit();
    }

    @Override
    public void addClass(List cls) {
        ((WineApplication) getApplication()).getMyClasss().clear();
        ((WineApplication) getApplication()).getMyClasss().addAll(cls);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        loginPresenter.detachView();
    }
}
