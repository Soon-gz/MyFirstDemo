package com.abings.baby.ui.finder;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.abings.baby.R;
import com.abings.baby.data.model.NewsListItem;
import com.abings.baby.ui.adapter.ABOnRVItemClickListener;
import com.abings.baby.ui.base.BaseActivity;
import com.abings.baby.ui.infolist.news.NewsActivity;
import com.abings.baby.utils.ProgressDialogHelper;
import com.abings.baby.utils.StringUtils;
import com.abings.baby.widget.DividerItemDecoration;
import com.abings.baby.widget.dialog.dialog.widget.MaterialDialog;
import com.abings.baby.widget.refreshlayout.BGARefreshLayout;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * 作者：黄斌 on 2016/2/21 15:23
 * 说明：
 */
public class FinderActivity extends BaseActivity implements FinderMvpView, ABOnRVItemClickListener, BGARefreshLayout.BGARefreshLayoutDelegate {

    @Inject
    FinderPresenter finderPresenter;

    @Bind(R.id.main_lay)
    View main_lay;

    @Bind(R.id.school_friend_member_search_input)
    EditText schoolFriendMemberSearchInput;
    @Bind(R.id.rv_recyclerview_data)
    RecyclerView rvRecyclerviewData;
    @Bind(R.id.rl_recyclerview_refresh)
    BGARefreshLayout mRefreshLayout;

    @Bind(R.id.toolbar)
    RelativeLayout toolbar;

    private MaterialDialog dialog;

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.activity_finder;
    }

    @Override
    protected void iniInjector() {
        activityComponent().inject(this);
        finderPresenter.attachView(this);
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
        schoolFriendMemberSearchInput.requestFocus();
        schoolFriendMemberSearchInput.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    toolbar.setVisibility(View.GONE);
                    goSearch();
                    return true;
                }
                return false;
            }
        });
    }


    @Override
    protected View getLoadingTargetView() {
        return main_lay;
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
        if (mAdapter.getDatas() == null) {
            mAdapter.setDatas(new ArrayList<NewsListItem>());
        }
        if ((NowPageNum == 1) || (mAdapter.getDatas() == null)) {
            mAdapter.setDatas(data);
            mRefreshLayout.endRefreshing();
        } else {
            mAdapter.addMoreDatas(data);
            mRefreshLayout.endLoadingMore();
        }
        mRefreshLayout.setIsShowLoadingMoreView(canNext);
    }

    @Override
    public void showLoadingProgress(boolean show) {
        if (show) {
            ProgressDialogHelper.getInstance().showProgressDialog(this, "正在载入...");
        } else {
            ProgressDialogHelper.getInstance().hideProgressDialog();
        }
    }

    @Override
    public void showMessage(String mes) {
        Toast.makeText(this, mes, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showError(String mes) {

    }

    @Override
    public void onTokenError() {

    }

    private void goSearch() {
        //点击隐藏软键盘
        InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
        if (!StringUtils.isEmpty(schoolFriendMemberSearchInput.getText().toString())) {
            finderPresenter.search(schoolFriendMemberSearchInput.getText().toString(), NowPageNum, true);
        } else {
            if (mAdapter != null) {
                mAdapter.clear();
                toolbar.setVisibility(View.GONE);
                mAdapter.notifyDataSetChanged();
            }
            Toast.makeText(this, "请输入搜索内容", Toast.LENGTH_SHORT).show();
        }
    }

    private int NowPageNum = 1;
    private FinderRecyclerViewAdapter mAdapter;

    private void iniAdapter() {
        if (mAdapter == null) {
            LinearLayoutManager layoutManager = new LinearLayoutManager(this);
            rvRecyclerviewData.setLayoutManager(layoutManager);
            rvRecyclerviewData.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
            mAdapter = new FinderRecyclerViewAdapter(rvRecyclerviewData);
            mAdapter.setOnRVItemClickListener(this);
            rvRecyclerviewData.setAdapter(mAdapter);
        }
    }

    @OnClick(R.id.school_friend_member_search_input)
    public void click(){
        toolbar.setVisibility(View.GONE);
    }

    @OnClick(R.id.relax)
    public void quxiao() {
        finish();
    }

    @OnClick(R.id.text_back)
    public void back() {
        finish();
    }

    @Override
    public void onRVItemClick(ViewGroup parent, View itemView, int position) {
        Intent inten = new Intent(this, NewsActivity.class);
        inten.putExtra("pk_image_news_id", mAdapter.getItem(position).getPk_image_news_id());
        inten.putExtra("from_search", true);
        inten.putExtra("title", mAdapter.getItem(position).getContent());
        inten.putExtra("model", mAdapter.getItem(position));
        startActivity(inten);
    }

    @Override
    public void onBGARefreshLayoutBeginRefreshing(BGARefreshLayout refreshLayout) {
        NowPageNum = 1;
        finderPresenter.search(schoolFriendMemberSearchInput.getText().toString(), NowPageNum, false);
    }

    @Override
    public boolean onBGARefreshLayoutBeginLoadingMore(BGARefreshLayout refreshLayout) {
        NowPageNum++;
        finderPresenter.search(schoolFriendMemberSearchInput.getText().toString(), NowPageNum, false);
        return true;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        finderPresenter.detachView();
    }
}
