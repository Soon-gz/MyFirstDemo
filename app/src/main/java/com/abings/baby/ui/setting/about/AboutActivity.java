package com.abings.baby.ui.setting.about;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.abings.baby.Const;
import com.abings.baby.R;
import com.abings.baby.WineApplication;
import com.abings.baby.ui.base.BaseActivity;
import com.abings.baby.ui.setting.question.QuestionActivity;
import com.abings.baby.ui.setting.version.VersionActivity;
import com.abings.baby.ui.upapp.CheckVersionThread;

import java.util.List;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Created by huangbin on 16/2/19.
 */
public class AboutActivity extends BaseActivity implements AboutMvpView {

    @Inject
    AboutPresenter aboutPresenter;
    @Bind(R.id.btn_back)
    TextView btnBack;
    @Bind(R.id.version)
    TextView version;
    @Bind(R.id.grade)
    TextView grade;
    @Bind(R.id.question)
    TextView question;
    @Bind(R.id.about_tv_checkversion)
    TextView tvCheckVersion;

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.activity_about;
    }

    @Override
    protected void iniInjector() {
        activityComponent().inject(this);
        aboutPresenter.attachView(this);
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
        startLoginActivity();
    }

    @OnClick({R.id.btn_back, R.id.version, R.id.grade, R.id.question,R.id.about_tv_checkversion})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_back:
                finish();
                break;
            case R.id.version:
                startActivity(new Intent(AboutActivity.this, VersionActivity.class));
                break;
            case R.id.grade:
//                startActivity(new Intent(AboutActivity.this, VersionActivity.class));
                break;
            case R.id.question:
                startActivity(new Intent(AboutActivity.this, QuestionActivity.class));
                break;
            case R.id.about_tv_checkversion:
                if (WineApplication.getInstance().isTeacher()) {
                    new CheckVersionThread(this, Const.apkDownPath, Const.url_teacher,true).start();
                } else {
                    new CheckVersionThread(this, Const.apkDownPath, Const.url_baby,true).start();
                }
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        aboutPresenter.detachView();
    }
}
