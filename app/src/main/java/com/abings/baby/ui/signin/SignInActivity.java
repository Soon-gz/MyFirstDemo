package com.abings.baby.ui.signin;

import android.content.Intent;
import android.graphics.PointF;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.abings.baby.R;
import com.abings.baby.data.model.Attend;
import com.abings.baby.ui.base.BaseActivity;
import com.dlazaro66.qrcodereaderview.QRCodeReaderView;

import java.util.List;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Created by huangbin on 16/2/19.
 */
public class SignInActivity extends BaseActivity implements SignInMvpView, QRCodeReaderView.OnQRCodeReadListener {

    @Inject
    SignInPresenter registerPresenter;
    @Bind(R.id.btn_back)
    ImageView btnBack;
    @Bind(R.id.title_center)
    TextView titleCenter;
    @Bind(R.id.history)
    ImageView history;
    private QRCodeReaderView mydecoderview;

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.activity_signin;
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
        String title = getIntent().getStringExtra("title");
        titleCenter.setText(title);
        mydecoderview = (QRCodeReaderView) findViewById(R.id.qrdecoderview);
        mydecoderview.setOnQRCodeReadListener(this);
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

    }

    @Override
    public void showMessage(String mes) {
        showToast(mes);
        reStartPreview();
    }

    @Override
    public void showError(String mes) {

    }

    @Override
    public void onTokenError() {
        startLoginActivity();
    }

    @OnClick({R.id.btn_back, R.id.history})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_back:
                finish();
                break;
            case R.id.history:
                startActivity(new Intent(this,SingInHistoryActivity.class));
                break;
        }
    }

    @Override
    public void onQRCodeRead(String text, PointF[] points) {
        registerPresenter.getQRCode(text);
        mydecoderview.getCameraManager().stopPreview();
    }

    @Override
    public void cameraNotFound() {
        showToast("没有发现摄像头,请检查摄像头!");
    }

    @Override
    public void QRCodeNotFoundOnCamImage() {
    }

    @Override
    protected void onResume() {
        super.onResume();
        mydecoderview.getCameraManager().startPreview();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mydecoderview.getCameraManager().stopPreview();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        registerPresenter.detachView();
    }


    @Override
    public void showAttend(Attend attend) {
        new AttendInfoPopup(SignInActivity.this,attend,registerPresenter).showPopupWindow();
    }
    //重启摄像头
    public void reStartPreview(){
        mydecoderview.getCameraManager().startPreview();
    }
}
