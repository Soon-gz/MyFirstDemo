package com.abings.baby.ui.base;

import android.annotation.TargetApi;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.abings.baby.BaseAppManager;
import com.abings.baby.R;
import com.abings.baby.WineApplication;
import com.abings.baby.injection.component.ActivityComponent;
import com.abings.baby.injection.component.DaggerActivityComponent;
import com.abings.baby.injection.module.ActivityModule;
import com.abings.baby.ui.loading.VaryViewHelperController;
import com.abings.baby.ui.login.LoginActivity;
import com.abings.baby.utils.ScreenUtils;
import com.abings.baby.utils.SystemStatusManager;
import com.abings.baby.utils.TLog;
import com.socks.library.KLog;

import butterknife.ButterKnife;


public abstract class BaseActivity extends AppCompatActivity {

    private static final String EXTRA_AUTO_CHECK_IN_DISABLED = "io.ribot.app.ui.main.MainActivity" + "" +
            ".EXTRA_AUTO_CHECK_IN_DISABLED";

    public enum TransitionMode {
        LEFT, RIGHT, TOP, BOTTOM, SCALE, FADE
    }

    private ActivityComponent mActivityComponent;
    private WineApplication app;
    /**
     * loading view controller
     */
    private VaryViewHelperController mVaryViewHelperController = null;
    protected String TAG;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if (toggleOverridePendingTransition()) {
            switch (getOverridePendingTransitionMode()) {
                case LEFT:
                    overridePendingTransition(R.anim.left_in, R.anim.left_out);
                    break;
                case RIGHT:
                    overridePendingTransition(R.anim.right_in, R.anim.right_out);
                    break;
                case TOP:
                    overridePendingTransition(R.anim.top_in, R.anim.top_out);
                    break;
                case BOTTOM:
                    overridePendingTransition(R.anim.bottom_in, R.anim.bottom_out);
                    break;
                case SCALE:
                    overridePendingTransition(R.anim.scale_in, R.anim.scale_out);
                    break;
                case FADE:
                    overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                    break;
            }
        }
        super.onCreate(savedInstanceState);
        TAG = this.getClass().getSimpleName();
        app = (WineApplication) getApplication();
        Bundle extras = getIntent().getExtras();
        if (null != extras) {
            getBundleExtras(extras);
        }

        //注入控制器
        iniInjector();

        if (getContentViewLayoutID() != 0) {
            setContentView(getContentViewLayoutID());
        } else {
            throw new IllegalArgumentException("你必须设置一个可以用的Layout资源 ID");
        }

        initViewsAndEvents(savedInstanceState);

    }

    @Override
    public void setContentView(int layoutResID) {
        super.setContentView(layoutResID);
        if (isApplyStatusBarTranslucency()) {
            setTranslucentStatus();
        }
        ButterKnife.bind(this);
        if (null != getLoadingTargetView()) {
            mVaryViewHelperController = new VaryViewHelperController(getLoadingTargetView());
        }
    }

    public ActivityComponent activityComponent() {
        if (mActivityComponent == null) {
            mActivityComponent = DaggerActivityComponent.builder().activityModule(new ActivityModule(this))
                    .applicationComponent(WineApplication.get(this).getComponent()).build();
        }
        return mActivityComponent;
    }

    @TargetApi(Build.VERSION_CODES.M)
    public void requestPermissionsSafely(String[] permissions, int requestCode) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(permissions, requestCode);
        }
    }

    @TargetApi(Build.VERSION_CODES.M)
    public boolean hasPermission(String permission) {
        return Build.VERSION.SDK_INT < Build.VERSION_CODES.M || checkSelfPermission(permission) == PackageManager
                .PERMISSION_GRANTED;
    }

    @Override
    public void finish() {
        super.finish();
        ButterKnife.unbind(this);
        BaseAppManager.getInstance().removeActivity(this);
        if (toggleOverridePendingTransition()) {
            switch (getOverridePendingTransitionMode()) {
                case LEFT:
                    overridePendingTransition(R.anim.left_in, R.anim.left_out);
                    break;
                case RIGHT:
                    overridePendingTransition(R.anim.right_in, R.anim.right_out);
                    break;
                case TOP:
                    overridePendingTransition(R.anim.top_in, R.anim.top_out);
                    break;
                case BOTTOM:
                    overridePendingTransition(R.anim.bottom_in, R.anim.bottom_out);
                    break;
                case SCALE:
                    overridePendingTransition(R.anim.scale_in, R.anim.scale_out);
                    break;
                case FADE:
                    overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                    break;
            }
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
    }

    //载入layoutID
    protected abstract int getContentViewLayoutID();

    //依赖注入
    protected abstract void iniInjector();

    //是否需要进入动画处理
    protected abstract boolean toggleOverridePendingTransition();

    //设置进入动画处理
    protected abstract TransitionMode getOverridePendingTransitionMode();

    //获取Bundle数据
    protected abstract void getBundleExtras(Bundle extras);

    //初始化视图或者方法
    protected abstract void initViewsAndEvents(Bundle savedInstanceState);

    //get loading target view
    protected abstract View getLoadingTargetView();

    //视图处理系统状态栏
    protected abstract boolean isApplyStatusBarTranslucency();


    protected void log(String mes) {
        TLog.getInstance().e(TAG, mes);
    }

    //设置系统状态栏
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    private void setTranslucentStatus() {
        //判断版本是4.4以上
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
            tintManager.setStatusBarTintColor(R.color.baby_orange);
            tintManager.setStatusBarTintResource(R.color.baby_orange);//设置背景
            KLog.e("ScreenUtils.getStatusHeight(this) = " +ScreenUtils.getStatusHeight(this));
            KLog.e("ScreenUtils.getStatusHeight(this) = " +ScreenUtils.getStatusHeight(this));
            View layoutAll = findViewById(R.id.layoutAll);
            if (layoutAll != null) {
                //设置系统栏需要的内偏移
                layoutAll.setPadding(0, ScreenUtils.getStatusHeight(this), 0, 0);
            }
        }
    }

    /**
     * 通过Class跳转界面
     **/
    protected void startActivity(Class<?> cls, Bundle bl) {
        Intent intent = new Intent();
        intent.putExtras(bl);
        intent.setClass(this, cls);
        startActivity(intent);

    }

    /**
     * 跳转登录界面
     **/
    protected void startLoginActivity() {
        startActivity(new Intent(this, LoginActivity.class));
        finish();
    }

    protected void showToast(final String msg) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(WineApplication.getInstance(), msg, Toast.LENGTH_SHORT).show();
            }
        });
    }

}
