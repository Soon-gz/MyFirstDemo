package com.abings.baby.ui.feedback;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.abings.baby.R;
import com.abings.baby.ui.base.BaseActivity;
import com.abings.baby.utils.CustomAlertDialog;
import com.abings.baby.utils.ProgressDialogHelper;
import com.abings.baby.utils.StringUtils;

import java.util.List;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.OnClick;

public class FeedBackActivity extends BaseActivity implements FeedBackMvpView {

    @Bind(R.id.input_text)
    TextView inputText;

    @Inject
    FeedBackPresenter mPresenter;

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.activity_feed_back;
    }

    @Override
    protected void iniInjector() {
        activityComponent().inject(this);
        mPresenter.attachView(this);
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
    public void feedBackSucceed(boolean result) {
        if (result){
            showMessage("谢谢您的反馈，我们会及时处理。");
            finish();
        }else{
            showMessage("对不起，反馈异常！");
            finish();
        }
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
            ProgressDialogHelper.getInstance().showProgressDialog(this, "发送中...");
        } else {
            ProgressDialogHelper.getInstance().hideProgressDialog();
        }
    }

    @Override
    public void showMessage(String mes) {
        Toast.makeText(FeedBackActivity.this, mes, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showError(String mes) {

    }

    @Override
    public void onTokenError() {

    }

    @OnClick(R.id.btn_send)
    void Send() {
        //发送建议
        if (!StringUtils.isEmpty(inputText.getText().toString())) {
            mPresenter.feebackAdd(inputText.getText().toString());
        }else{
            showMessage("请输入您的宝贵意见。");
        }
    }
    @OnClick(R.id.text_back)
    void back() {
        if (!StringUtils.isEmpty(inputText.getText().toString())){
            CustomAlertDialog.dialogWithSureCancel("确定放弃编辑页面？", this);
        }else{
            finish();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mPresenter.detachView();
    }

}
