package com.abings.baby.ui.register;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.abings.baby.R;
import com.abings.baby.WineApplication;
import com.abings.baby.ui.base.BaseActivity;
import com.abings.baby.ui.login.LoginActivity;
import com.abings.baby.utils.CustomAlertDialog;
import com.abings.baby.utils.PasswordUtils;
import com.abings.baby.utils.ProgressDialogHelper;
import com.abings.baby.utils.ScreenUtils;
import com.abings.baby.utils.SystemStatusManager;
import com.abings.baby.utils.ViewUtil;
import com.abings.baby.widget.ClearEditText;
import com.socks.library.KLog;

import java.util.List;
import java.util.regex.Pattern;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Created by huangbin on 16/2/19.
 */
public class RegisterActivity extends BaseActivity implements RegisterMvpView {

    @Inject
    RegisterPresenter registerPresenter;
    @Bind(R.id.input_phone)
    ClearEditText input_phone;
    @Bind(R.id.input_psw)
    ClearEditText input_psw;
    @Bind(R.id.input_psw2)
    ClearEditText input_psw2;
    @Bind(R.id.input_Code)
    ClearEditText input_Code;
    @Bind(R.id.input_id)
    ClearEditText input_id;
    @Bind(R.id.btn_code)
    TextView btn_code;

    @Bind(R.id.btn_register)
    TextView btn_register;

    @Bind(R.id.layou_id)
    LinearLayout layou_id;

    @Bind(R.id.logo)
    ImageView logo;

    @Bind(R.id.register_ll_easy_prompt)
    LinearLayout llEasyPrompt;
    @Bind(R.id.register_ll_psw2)
    LinearLayout llPsw2;

    private int INTERFACE_TYPE = 0;
    private int llPsw2PaddingBottom;
    private int llPsw2PaddingLeft;
    private int llPsw2PaddingRight;
    private int llPsw2PaddingTop;

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.activity_rgister;
    }

    @Override
    protected void iniInjector() {
        activityComponent().inject(this);
        registerPresenter.attachView(this);
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

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            KLog.e("FLAG_TRANSLUCENT_STATUS");
            Window win = getWindow();
            WindowManager.LayoutParams winParams = win.getAttributes();
            final int bits = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
            winParams.flags |= bits;
            win.setAttributes(winParams);

            SystemStatusManager tintManager = new SystemStatusManager(this);
            //打开系统状态栏控制
            tintManager.setStatusBarTintEnabled(true);
            tintManager.setStatusBarTintColor(R.color.ese_white);
            tintManager.setStatusBarTintResource(R.color.ese_white);//设置背景
            View layoutAll = findViewById(R.id.layoutAll);
            if (layoutAll != null) {
                //设置系统栏需要的内偏移
                layoutAll.setPadding(0, ScreenUtils.getStatusHeight(this), 0, 0);
            }
        }

        if (WineApplication.getInstance().isTeacher()) {
            logo.setImageResource(R.drawable.logo_teacher);
        } else {
            logo.setImageResource(R.drawable.logo_user);
        }

        int i = getIntent().getIntExtra("type", 1);
        INTERFACE_TYPE = i;
        if (i == 1) {
            //register
            btn_register.setText("注册");
        } else if (i == 2) {
            //forgetPw
            btn_register.setText("修改密码");
        }

        llPsw2PaddingBottom = llPsw2.getPaddingBottom();
        llPsw2PaddingLeft = llPsw2.getPaddingLeft();
        llPsw2PaddingRight = llPsw2.getRight();
        llPsw2PaddingTop = llPsw2.getTop();
        input_psw.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus){
                    llEasyPrompt.setVisibility(View.GONE);
                    llPsw2.setPadding(llPsw2PaddingLeft,ViewUtil.dpToPx(8),llPsw2PaddingRight,llPsw2PaddingBottom);
                }else{
                    String pwd = input_psw.getText().toString().trim();
                    if(!PasswordUtils.isPwdAvailable(pwd)){
                        //提示密码过于简单
                        llEasyPrompt.setVisibility(View.VISIBLE);
                        llPsw2.setPadding(llPsw2PaddingLeft,0,llPsw2PaddingRight,llPsw2PaddingBottom);
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
        if (show) {
            ProgressDialogHelper.getInstance().showProgressDialog(this, "正在载入...");
        } else {
            ProgressDialogHelper.getInstance().hideProgressDialog();
        }
    }

    @Override
    public void showMessage(final String mes) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(RegisterActivity.this, mes, Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void showError(String mes) {

    }

    @Override
    public void onTokenError() {
        finish();
    }

    @OnClick(R.id.btn_back)
    void toBack() {

        mHandler.removeCallbacksAndMessages(null);
        String mobile_phone = input_phone.getText().toString().trim();
        String password = input_psw.getText().toString().trim();
        String passwordIdenty = input_psw2.getText().toString().trim();
        String Identify_id = input_id.getText().toString().trim();
        String smsCode = input_Code.getText().toString().trim();

        if (mobile_phone.isEmpty()||password.isEmpty()||passwordIdenty.isEmpty()||Identify_id.isEmpty()||smsCode.isEmpty()){
            finish();
        }else{
            CustomAlertDialog.dialogWithSureCancel("确定放弃编辑页面？", this);
        }
    }

    /**
     * changed by shuwen
     * 2016/5/6
     *
     * */
    @OnClick(R.id.btn_register)
    void Register() {
//        log("Register");


        String mobile_phone = input_phone.getText().toString().trim();
        String password = input_psw.getText().toString().trim();
        String passwordIdenty = input_psw2.getText().toString().trim();
        String Identify_id = input_id.getText().toString().trim();
        String smsCode = input_Code.getText().toString().trim();
        String id = "^\\d{15}|\\d{18}$";
        //先判断是否是为空
        if (mobile_phone.isEmpty()||password.isEmpty()||passwordIdenty.isEmpty()||Identify_id.isEmpty()||smsCode.isEmpty()){
            showMessage("对不起，请输入完整信息，不能有空白项！");
        }else{
            //Log.i("TAG00",Identify_id);
            if (Pattern.matches(id, Identify_id)){
                if (password.length()>0&&passwordIdenty.length()>0){
                    if (password.equals(passwordIdenty)){
                        if (mobile_phone.matches("1[3|5|7|8|][0-9]{9}")){
                            if(!PasswordUtils.isPwdLengthRange(password)){
                                showToast(getResources().getString(R.string.pwd_length_prompt));
                                return;
                            }
                            if (INTERFACE_TYPE == 1){
                                registerPresenter.register(mobile_phone, password, Identify_id, smsCode);
                            } else {
                                registerPresenter.chagePassWord(mobile_phone,password,smsCode);
                            }
                        }else{
                            showMessage("请输入11位以1为起始的电话号码");
                        }
                    }else{
                        showMessage("对不起，密码和确认密码必须一致！");
                    }
                }else{
                    showMessage("对不起，密码和确认密码项不能为空！");
                }
            }else{
                showMessage("对不起，请输入正确的身份证号码！");
            }
        }
    }

    int time = 60;
    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:

                case 1:
//                    if (isFinishing() || isDestroyed()) {
//                        return;
//                    }
                    if (time > 0) {
                        if (isFinishing() || isDestroyed()) {
                            return;
                        } else {
                            mHandler.sendEmptyMessageDelayed(1, 1000L);
                        }
                        btn_code.setClickable(false);
                        btn_code.setText(String.valueOf(time--));
                    } else {
                        time = 60;
                        btn_code.setText("获取验证码");
                        btn_code.setClickable(true);
                    }
                    break;
            }
        }
    };

    /**
     * changed by shuwen
     * 2016/5/6
     *
     * */
    @OnClick(R.id.btn_code)
    void getCode() {
        //发送短信
        String mobile_phone = input_phone.getText().toString().trim();
        if(mobile_phone.matches("1[3|5|7|8|][0-9]{9}")){
            mHandler.sendEmptyMessage(1);
            registerPresenter.getSmsCode(mobile_phone);
        }else{
            showMessage("请输入11位以1为起始的电话号码");
        }

    }

    @Override
    public void toLoginActivity(boolean isFirst) {
        if (isFirst){
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
            finish();
        }
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
    public void finishActivity() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                finish();
            }
        });
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        registerPresenter.detachView();
    }


}
