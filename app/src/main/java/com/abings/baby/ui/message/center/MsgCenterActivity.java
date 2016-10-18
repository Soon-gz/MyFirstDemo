package com.abings.baby.ui.message.center;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.Toast;

import com.abings.baby.R;
import com.abings.baby.data.model.MessageItem;
import com.abings.baby.ui.adapter.VPFragmentAdapter;
import com.abings.baby.ui.base.BaseActivity;
import com.abings.baby.ui.home2.CustomActionSheetDialog;
import com.abings.baby.ui.login.LoginActivity;
import com.abings.baby.ui.message.send.SendMsgActivity;
import com.abings.baby.widget.BadgeView;
import com.flyco.dialog.listener.OnOperItemClickL;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Created by Administrator on 2016/9/1.
 */
public class MsgCenterActivity extends BaseActivity implements MsgCenterMvpView {


    @Inject
    MsgCenterPresenter msgCenterPresenter;
    String mUnReadCount = "";
    @Bind(R.id.rl_rl_rl)
    View rl_rl_rl;
    @Bind(R.id.toptitle)
    ImageView toptitle;
    boolean pagePosition=false;
    ViewPager vp;
    BadgeView badgeView;

    private List<MessageItem> mDatas;


    @Override
    protected int getContentViewLayoutID() {
        return R.layout.activity_message_center;
    }

    @Override
    protected void iniInjector() {
        activityComponent().inject(this);
        msgCenterPresenter.attachView(this);
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
//        iniBgaRefreshLayout();
        msgCenterPresenter.loadCount(true);
        List<Fragment> fragments = new ArrayList<Fragment>();
        fragments.add(new MsgCenterListFragment());
        fragments.add(new MsgCenterListFragment_copy());
        VPFragmentAdapter adapter = new VPFragmentAdapter(getSupportFragmentManager(), fragments);
        vp = (ViewPager) findViewById(R.id.viewpager);
        vp.setAdapter(adapter);
        vp.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                switch (position) {
                    case 0:
                        pagePosition=false;
                        toptitle.setSelected(pagePosition);
                        break;
                    case 1:
                        pagePosition=true;
//                        badgeView.hide();
                        toptitle.setSelected(pagePosition);
                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

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
    public void updateUnReadCount(int count) {
        badgeView = new BadgeView(this, rl_rl_rl);
        badgeView.hide();
        if(count!=0) {
            mUnReadCount = String.valueOf(count);
            badgeView.setText(mUnReadCount);
            badgeView.setBadgePosition(BadgeView.POSITION_CP);
            badgeView.show();
        }else {
            badgeView.hide();
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

    }

    @Override
    public void showMessage(String mes) {
        Toast.makeText(this, mes, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showError(String mes) {
        Toast.makeText(this, mes, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onTokenError() {
        startActivity(new Intent(this, LoginActivity.class));
    }

    @OnClick(R.id.btn_back)
    void toBack() {
        finish();
    }

    @OnClick(R.id.btn_send)
    void toPost() {
        final String[] stringItems = {"老师","班级"};
        final CustomActionSheetDialog dialog = new CustomActionSheetDialog(this, stringItems, null);
        dialog.isTitleShow(false).isCancelShow(false)
                .itemTextColor(this.getResources().getColor(R.color.ese_white))
                .itemPressColor(this.getResources().getColor(R.color.ese_orange))
                .lvBgColor(this.getResources().getColor(R.color.baby_orange))
                .show();

        dialog.setOnOperItemClickL(new OnOperItemClickL() {
            @Override
            public void onOperItemClick(AdapterView<?> parent, View view, int position, long id) {
                dialog.dismiss();
                Intent intent ;
                switch (position) {
                    case 0:
                        intent = new Intent(MsgCenterActivity.this, SendMsgActivity.class);
                        intent.putExtra("type","teacher");
                        startActivity(intent);
                        break;
                    case 1:
                        intent = new Intent(MsgCenterActivity.this, SendMsgActivity.class);
                        intent.putExtra("type","classes");
                        startActivity(intent);
                        break;
                    default:
                        break;
                }
            }
        });

    }

    @OnClick(R.id.toptitle)
    void toChange() {
        switch (vp.getCurrentItem()){
            case 0:
                vp.setCurrentItem(1);
//                badgeView.hide();
                break;
            case 1:
                vp.setCurrentItem(0);
                break;
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        msgCenterPresenter.loadCount(true);
    }
}
