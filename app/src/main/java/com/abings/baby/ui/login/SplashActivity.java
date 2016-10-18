package com.abings.baby.ui.login;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import com.abings.baby.Const;
import com.abings.baby.R;
import com.abings.baby.WineApplication;
import com.abings.baby.ui.base.BaseActivity;
import com.abings.baby.ui.main.MainActivity;
import com.abings.baby.ui.upapp.CheckVersionThread;
import com.abings.baby.utils.StringUtils;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import javax.inject.Inject;

/**
 * 开机动画界面 在这里判断程序是不是第一次安装
 * 如果是第一次安装 开机动画结束后进入引导界面 如果不是，直接进入主界面
 * @author Administrator
 *
 */
public class SplashActivity extends BaseActivity implements SplashMvpView {

	private SharedPreferences sharedPreferences;
	private Boolean isFirstIn;
	private SharedPreferences preferences;

	@Inject
	SplashPrecenter mPrecenter;

	@Override
	protected int getContentViewLayoutID() {
		return R.layout.activity_splash;
	}

	@Override
	protected void iniInjector() {
		activityComponent().inject(this);
		mPrecenter.attachView(this);
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
		preferences = getSharedPreferences("login", LoginActivity.MODE_PRIVATE);
		final String name = preferences.getString("userName","");
		final String passWord = preferences.getString("password","");

		if (WineApplication.getInstance().isTeacher()) {
			new CheckVersionThread(this, Const.apkDownPath, Const.url_teacher).start();
		} else {
			new CheckVersionThread(this, Const.apkDownPath, Const.url_baby).start();
		}
		//透明状态栏
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
		//透明导航栏
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
		sharedPreferences = getSharedPreferences("firstIn", MODE_PRIVATE);
		//取得相应的值，如果没有该值 说明还未写入 用true作为默认值
		isFirstIn = sharedPreferences.getBoolean("isFirstIn", true);

		//使用定时器 ， 延迟执行页面跳转
		new Timer().schedule(new TimerTask() {

			@Override
			public void run() {
				Intent intent = new Intent();
				if (isFirstIn) {
					Editor editor = sharedPreferences.edit();
					editor.putBoolean("isFirstIn", true);
					editor.commit();
					if (!StringUtils.isEmpty(name) && !StringUtils.isEmpty(passWord)){
						mPrecenter.MemberLogin(name,passWord);
					}else{
						intent.setClass(SplashActivity.this, LoginActivity.class);
						startActivity(intent);
						finish();
					}
				}

			}
		}, 2000);
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

	}

	@Override
	public void showMessage(String mes) {

	}

	@Override
	public void showError(final String mes) {
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				Toast.makeText(SplashActivity.this, mes, Toast.LENGTH_SHORT).show();
			}
		});
	}

	@Override
	public void onTokenError() {

	}

	@Override
	public void addBaby(List bbs) {
		((WineApplication) getApplication()).getMyBabys().clear();
		((WineApplication) getApplication()).getMyBabys().addAll(bbs);
	}

	@Override
	public void addClass(List cls) {
		((WineApplication) getApplication()).getMyClasss().clear();
		((WineApplication) getApplication()).getMyClasss().addAll(cls);
	}

	@Override
	public void toMainActivity(boolean isFirst) {
		if (isFirst) {
			startActivity(MainActivity.class, new Bundle());
		}
		finish();
	}

	@Override
	public void toLoginActivity() {
		startActivity(new Intent(SplashActivity.this,LoginActivity.class));
		finish();
	}
}
