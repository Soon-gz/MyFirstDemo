package com.abings.baby.ui.setting.version;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.abings.baby.R;
import com.abings.baby.ui.base.BaseActivity;
import com.abings.baby.utils.AppUtils;

import java.util.List;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Created by huangbin on 16/2/19.
 */
public class VersionActivity extends BaseActivity implements VersionMvpView {

    @Inject
    VersionPresenter registerPresenter;
    @Bind(R.id.btn_back)
    TextView btnBack;
    @Bind(R.id.name)
    TextView name;
    @Bind(R.id.version)
    TextView version;
    @Bind(R.id.time)
    TextView time;


    @Override
    protected int getContentViewLayoutID() {
        return R.layout.activity_version;
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
        version.setText("V" + AppUtils.getVersionName(this));
        name.setText("Hello Baby" + "(" + getResources().getString(R.string.app_name) + ")");
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

    }

    @Override
    public void showError(String mes) {

    }

    @Override
    public void onTokenError() {

    }

    @OnClick(R.id.btn_back)
    void toBack() {
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        registerPresenter.detachView();
    }
}
