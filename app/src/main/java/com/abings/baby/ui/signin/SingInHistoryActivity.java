package com.abings.baby.ui.signin;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TextView;

import com.abings.baby.R;
import com.abings.baby.ui.aboutme.BottomDialogUtils;
import com.abings.baby.ui.aboutme.user.BottomPickerDateDialog;
import com.abings.baby.ui.base.BaseActivity;
import com.abings.baby.utils.DateUtil;
import com.abings.baby.utils.TLog;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;
import de.greenrobot.event.EventBus;

public class SingInHistoryActivity extends BaseActivity {

    @Bind(R.id.leaftext)
    TextView leftText;
    @Bind(R.id.righttext)
    TextView rightText;
    @Bind(R.id.signinVpager)
    ViewPager viewPager;

    private List<SingInFragment> fragments;
    private SingInFragment leftFragment;
    private SingInFragment rightFragment;
    private MyAdapter myAdapter;
    private boolean isLeftSelected = true;
    public String datetime;

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.activity_sign_in_history;
    }

    @Override
    protected void iniInjector() {

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
        fragments = new ArrayList<>();
        datetime = DateUtil.getCurrentTime("yyyy-MM-dd");
        leftFragment = createFrament("0");
        rightFragment = createFrament("1");
        myAdapter = new MyAdapter(getSupportFragmentManager());

        fragments.add(leftFragment);
        fragments.add(rightFragment);

        viewPager.setAdapter(myAdapter);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (!isLeftSelected) {
                    isLeftSelected = true;
                    rightText.setTextColor(Color.parseColor("#ffffff"));
                    rightText.setBackgroundResource(R.drawable.custom_titleshape_right);
                    leftText.setTextColor(Color.parseColor("#FA5746"));
                    leftText.setBackgroundResource(R.drawable.custom_titleshape_left_selected);
                } else {
                    isLeftSelected = false;
                    rightText.setTextColor(Color.parseColor("#FA5746"));
                    rightText.setBackgroundResource(R.drawable.custom_titleshape_right_selected);
                    leftText.setTextColor(Color.parseColor("#ffffff"));
                    leftText.setBackgroundResource(R.drawable.custom_titleshape_left);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    public SingInFragment createFrament(String type) {
        SingInFragment fragment = new SingInFragment();
        Bundle bundle = new Bundle();
        bundle.putString("leave", type);
        bundle.putString("datetime",datetime);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    protected View getLoadingTargetView() {
        return null;
    }

    @Override
    protected boolean isApplyStatusBarTranslucency() {
        return true;
    }

    //所有点击事件
    @OnClick({R.id.btn_back, R.id.btn_clock, R.id.leaftext, R.id.righttext})
    public void onclick(View view) {
        switch (view.getId()) {
            case R.id.btn_back:
                finish();
                break;
            case R.id.btn_clock:
                String currentime = DateUtil.getCurrentTime("yyyy-MM-dd");
                datetime = currentime;
                TLog.getInstance().i("当前时间：" + currentime);
                BottomDialogUtils.getBottomDatePickerDialog(this, currentime, new BottomPickerDateDialog.BottomOnDateChangedListener() {
                    @Override
                    public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        String time = year+"-"+(monthOfYear<10?("0"+monthOfYear):monthOfYear)+"-"+(dayOfMonth<10?("0"+dayOfMonth):dayOfMonth);
                        EventBus.getDefault().post(new SingFrEventClick(time));
                    }
                });
                break;
            case R.id.leaftext:
                if (!isLeftSelected) {
                    viewPager.setCurrentItem(0);
                }
                break;
            case R.id.righttext:
                if (isLeftSelected) {
                    viewPager.setCurrentItem(1);
                }
                break;
        }
    }


    class MyAdapter extends FragmentStatePagerAdapter {

        public MyAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return fragments.get(position);
        }

        @Override
        public int getCount() {
            return fragments.size();
        }
    }

}

