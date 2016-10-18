package com.abings.baby.ui.infolist;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.abings.baby.R;
import com.abings.baby.WineApplication;
import com.abings.baby.data.model.MainListItem;
import com.abings.baby.data.model.NewsListItem;
import com.abings.baby.ui.adapter.ABOnRVItemClickListener;
import com.abings.baby.ui.base.BaseActivity;
import com.abings.baby.ui.infolist.news.NewsActivity;
import com.abings.baby.utils.ProgressDialogHelper;
import com.abings.baby.widget.refreshlayout.BGARefreshLayout;
import com.abings.baby.widget.refreshlayout.BGAStickinessRefreshViewHolder;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * 作者：黄斌 on 2016/2/21 15:23
 * 说明：
 */
public class InfoListActivity extends BaseActivity implements InfoListMvpView, ABOnRVItemClickListener,
        BGARefreshLayout.BGARefreshLayoutDelegate {
    @Inject
    InfoListPresenter infoListPresenter;

    @Bind(R.id.rv_recyclerview_data)
    RecyclerView mDataRv;
    @Bind(R.id.rl_recyclerview_refresh)
    BGARefreshLayout mRefreshLayout;
    @Bind(R.id.title_center)
    TextView titleCenter;
    @Bind(R.id.btn_img)
    ImageView btn_img;
    @Bind(R.id.btn_back)
    ImageView btnBack;
    @Bind(R.id.layout_img)
    LinearLayout layoutImg;

    private boolean isFirstIn = true;
    private String[] namesTeacher = {"食谱", "拓展", "活动", "动态"};
    private String[] namesUser = {"食谱", "拓展", "活动", "动态"};
    private RecyclerViewAdapterInfoList adapter;
    private int NowPageNum = 1;

    private int mNewsTag = 1;
    private MainListItem mItem;
    private String mTag_id;

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.activity_infolist;
    }

    @Override
    protected void iniInjector() {
        activityComponent().inject(this);
        infoListPresenter.attachView(this);
    }

    @Override
    protected boolean toggleOverridePendingTransition() {
        return false;
    }

    @Override
    protected TransitionMode getOverridePendingTransitionMode() {
        return TransitionMode.RIGHT;
    }

    @Override
    protected void getBundleExtras(Bundle extras) {

    }

    String fk_grade_class_id = "";

    @Override
    protected void onStart() {
        super.onStart();
        NowPageNum = 1;
        if (isFirstIn) {
            isFirstIn = false;
            return;
        }
        infoListPresenter.loadData(mTag_id, fk_grade_class_id, NowPageNum, false);
    }

    @Override
    protected void initViewsAndEvents(Bundle savedInstanceState) {
        iniBgaRefreshLayout();
        if (getIntent().hasExtra("Tag")) {
            mTag_id = getIntent().getStringExtra("Tag");
            titleCenter.setText(getTName(mTag_id));
            if (WineApplication.getInstance().isTeacher()) {
                fk_grade_class_id = WineApplication.getInstance().getNowClass().getPk_grade_class_id();
            } else {
                fk_grade_class_id = WineApplication.getInstance().getBaby().getFk_grade_class_id();
            }
            infoListPresenter.loadData(mTag_id, fk_grade_class_id, NowPageNum, true);
        }
    }

    public String getTName(String position) {
        if (WineApplication.getInstance().isTeacher()){
            return namesTeacher[Integer.parseInt(position) - 1];
        }else{
            return namesUser[Integer.parseInt(position) - 1];
        }

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
        iniAdapter();
        if (adapter.getDatas() == null) {
            adapter.setDatas(new ArrayList<NewsListItem>());
        }

        if ((NowPageNum == 1) || (adapter.getDatas() == null)) {
            adapter.setDatas(data);
            mRefreshLayout.endRefreshing();
        } else {
            adapter.addMoreDatas(data);
            mRefreshLayout.endLoadingMore();
        }
        mRefreshLayout.setIsShowLoadingMoreView(canNext);
    }

    @Override
    public void showLoadingProgress(boolean show) {
        if (show) {
            ProgressDialogHelper.getInstance().showProgressDialog(this, "正在加载中...");
        } else {
            ProgressDialogHelper.getInstance().hideProgressDialog();
        }

    }

    @Override
    public void showMessage(String mes) {

    }

    @Override
    public void showError(String mes) {
        mRefreshLayout.endLoadingMore();
        Toast.makeText(InfoListActivity.this, mes, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onTokenError() {
        startLoginActivity();
    }


    private void iniBgaRefreshLayout() {
        mRefreshLayout.setDelegate(this);
        BGAStickinessRefreshViewHolder stickinessRefreshViewHolder = new BGAStickinessRefreshViewHolder(this, true);
        stickinessRefreshViewHolder.setStickinessColor(R.color.colorPrimary);
        stickinessRefreshViewHolder.setRotateImage(R.mipmap.bga_refresh_stickiness);
        stickinessRefreshViewHolder.setRefreshViewBackgroundColorRes(R.color.white);
        mRefreshLayout.setRefreshViewHolder(stickinessRefreshViewHolder);
    }

    private void iniAdapter() {
        if (adapter == null) {
            LinearLayoutManager layoutManager = new LinearLayoutManager(this);
            mDataRv.setLayoutManager(layoutManager);
            adapter = new RecyclerViewAdapterInfoList(mDataRv);
            adapter.setOnRVItemClickListener(this);
            mDataRv.setAdapter(adapter);
        }
    }

    @Override
    public void onBGARefreshLayoutBeginRefreshing(BGARefreshLayout refreshLayout) {
        NowPageNum = 1;
        infoListPresenter.loadData(mTag_id, fk_grade_class_id, NowPageNum, false);
    }

    @Override
    public boolean onBGARefreshLayoutBeginLoadingMore(BGARefreshLayout refreshLayout) {
        NowPageNum++;
        infoListPresenter.loadData(mTag_id, fk_grade_class_id, NowPageNum, false);
        return true;
    }

    @Override
    public void onRVItemClick(ViewGroup parent, View itemView, int position) {
        Intent inten = new Intent(InfoListActivity.this, NewsActivity.class);
        inten.putExtra("pk_image_news_id", adapter.getItem(position).getPk_image_news_id());
        inten.putExtra("tag_id", mTag_id);
        startActivity(inten);
    }

    private void showPopup() {
        InfoListPopup.Builder builder = new InfoListPopup.Builder(this);
        builder.addItem(0, R.drawable.main_launch);
        builder.addItem(1, R.drawable.main_operation);
        builder.addItem(2, R.drawable.main_activity);
        builder.addItem(3, R.drawable.main_journal);

        final InfoListPopup mListPopup = builder.build();
        mListPopup.setOnListPopupItemClickListener(new InfoListPopup.OnListPopupItemClickListener() {
            @Override
            public void onItemClick(int what) {
                mListPopup.dismiss();
                switchItem(what + 1);
            }
        });
        mListPopup.showPopupWindow(layoutImg);
    }

    public void switchItem(int tag) {
        switch (tag) {
            case 1:
                titleCenter.setText("食谱");
                btn_img.setImageResource(R.drawable.toolbar_launch);
                break;
            case 2:
                titleCenter.setText("拓展");
                btn_img.setImageResource(R.drawable.toolbar_operation);
                break;
            case 3:
                titleCenter.setText("活动");
                btn_img.setImageResource(R.drawable.toolbar_activity);
                break;
            case 4:
                titleCenter.setText("动态");
                btn_img.setImageResource(R.drawable.toolbar_journal);
                break;
        }
        String fk_grade_class_id = "";
        if (WineApplication.getInstance().isTeacher()) {
            fk_grade_class_id = WineApplication.getInstance().getNowClass().getPk_grade_class_id();
        } else {
            fk_grade_class_id = WineApplication.getInstance().getBaby().getFk_grade_class_id();
        }
        NowPageNum = 1;
        mTag_id = tag + "";
        infoListPresenter.loadData(mTag_id, fk_grade_class_id, NowPageNum, true);
    }

    @OnClick({R.id.btn_back, R.id.btn_img})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_back:
                finish();
                break;
            case R.id.btn_img:
                showPopup();
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        infoListPresenter.detachView();
    }
}
