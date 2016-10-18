package com.abings.baby.ui.upapp;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.abings.baby.R;
import com.abings.baby.ui.base.BaseActivity;
import com.abings.baby.widget.dialog.animation.BounceEnter.BounceTopEnter;
import com.abings.baby.widget.dialog.animation.SlideExit.SlideBottomExit;
import com.abings.baby.widget.dialog.dialog.widget.MaterialDialog;

import java.io.File;
import java.util.List;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class UpAppDialogActivity extends BaseActivity implements UpAppMvpView {

    @Inject
    UpAppPresenter upAppPresenter;
    @Bind(R.id.upappdialog_tv_desc)
    TextView tvDesc;
    @Bind(R.id.upappdialog_btn_cancel)
    Button btnCancel;
    @Bind(R.id.upappdialog_btn_ok)
    Button btnOk;
    private MaterialDialog dialog;


    @Override
    protected int getContentViewLayoutID() {
        return R.layout.activity_upapp_dialog;
    }

    @Override
    protected void iniInjector() {
        activityComponent().inject(this);
        upAppPresenter.attachView(this);
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
        String desc = getIntent().getStringExtra("desc");
        tvDesc.setText(desc);
    }

    @OnClick(R.id.upappdialog_btn_ok)
    public void upAppClick() {
        if(Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())){
            String url = getIntent().getStringExtra("url");
            this.upAppPresenter.toUpApp(url);
        }else{
            Toast.makeText(this,"SDCard不存在",Toast.LENGTH_SHORT).show();
        }
    }

    @OnClick(R.id.upappdialog_btn_cancel)
    public void upAppCancelClick() {
        finish();
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
            dialog = new MaterialDialog(this, R.layout.dialog_progress);
            dialog.setCanceledOnTouchOutside(false);
            dialog.btnNum(1).btnText("取消").title("正在下载最新版本...").showAnim(new BounceTopEnter())//
                    .dismissAnim(new SlideBottomExit())//
                    .show();
        } else {
            dialog.dismiss();
        }
    }

    @Override
    public void showMessage(String mes) {

    }

    @Override
    public void showError(String mes) {
        Toast.makeText(this, mes, Toast.LENGTH_SHORT).show();
        finish();
    }

    @Override
    public void onTokenError() {
        finish();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        upAppPresenter.detachView();
    }

    @Override
    public void installApp(String path) {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_VIEW);
        File file = new File(path);
        intent.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive");
        startActivity(intent);
    }
}
