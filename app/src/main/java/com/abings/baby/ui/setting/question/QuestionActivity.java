package com.abings.baby.ui.setting.question;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.abings.baby.R;
import com.abings.baby.ui.adapter.ABOnRVItemClickListener;
import com.abings.baby.ui.base.BaseActivity;
import com.abings.baby.ui.infolist.RecyclerViewAdapterInfoList;
import com.abings.baby.widget.DividerItemDecoration;
import com.abings.baby.widget.refreshlayout.BGARefreshLayout;

import java.util.List;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Created by huangbin on 16/2/19.
 */
public class QuestionActivity extends BaseActivity implements QuestionMvpView, ABOnRVItemClickListener,
        BGARefreshLayout.BGARefreshLayoutDelegate {

    @Inject
    QuestionPresenter questionPresenter;

    @Bind(R.id.rv_recyclerview_data)
    RecyclerView mDataRv;
    @Bind(R.id.rl_recyclerview_refresh)
    BGARefreshLayout mRefreshLayout;
    private RecyclerViewAdapterInfoList adapter;
    private int NowPageNum = 1;

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.activity_question;
    }

    @Override
    protected void iniInjector() {
        activityComponent().inject(this);
        questionPresenter.attachView(this);
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

        iniAdapter();
        if (adapter.getDatas() == null) {
//            adapter.setDatas(new ArrayList<NewInfo>());
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

    private void iniAdapter() {
        if (adapter == null) {
            LinearLayoutManager layoutManager = new LinearLayoutManager(this);
            mDataRv.setLayoutManager(layoutManager);
            mDataRv.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));


            adapter = new RecyclerViewAdapterInfoList(mDataRv);
            adapter.setOnRVItemClickListener(this);
            mDataRv.setAdapter(adapter);
        }
    }

    @OnClick(R.id.btn_back)
    void toBack() {
        finish();
    }

    @Override
    public void showDialog() {

    }

    @Override
    public void dialogDismiss() {

    }

    @Override
    public void onBGARefreshLayoutBeginRefreshing(BGARefreshLayout refreshLayout) {
        NowPageNum = 1;
//        questionPresenter.loadRibots(NowPageNum, false);
    }

    @Override
    public boolean onBGARefreshLayoutBeginLoadingMore(BGARefreshLayout refreshLayout) {
        NowPageNum++;
//        questionPresenter.loadRibots(NowPageNum, false);
        return true;
    }

    @Override
    public void onRVItemClick(ViewGroup parent, View itemView, int position) {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        questionPresenter.detachView();
    }
}
