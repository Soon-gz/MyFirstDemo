package com.abings.baby.ui.exercise;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.abings.baby.R;
import com.abings.baby.WineApplication;
import com.abings.baby.ui.adapter.ABOnRVItemClickListener;
import com.abings.baby.ui.base.BaseActivity;
import com.abings.baby.ui.exercise.createnew.ExerciseNew;
import com.abings.baby.ui.exercise.exerciseDetail.ExerciseDetailActivity;
import com.abings.baby.utils.ProgressDialogHelper;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class Exercise extends BaseActivity implements ABOnRVItemClickListener,ExerciseMvpView {

    @Inject
    ExercisePrecenter mPrecenter;

    @Bind(R.id.exercise_recyclerview_data)
    RecyclerView mDataRv;
    @Bind(R.id.exercise_search_input)
    EditText mSearchInput;
    @Bind(R.id.exercise_new)
    TextView exerciseNew;

    private boolean isFirstIn = true;


    private ExerciseRVAdapter adapter;
    private List<ExerciseModel> mDatas;

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.activity_exercise;
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (isFirstIn){
            isFirstIn = false;
            return;
        }
        mPrecenter.loadData();
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
        mPrecenter.loadData();
        if (WineApplication.getInstance().isTeacher()){
            exerciseNew.setVisibility(View.VISIBLE);
        }else{
            exerciseNew.setVisibility(View.GONE);
        }
        mSearchInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (mDatas == null || mDatas.size() <= 0) {
                    return;
                }
                ArrayList<ExerciseModel> temp = new ArrayList<>(mDatas);
                for (ExerciseModel data : mDatas) {
                    if (data.getTitle().contains(s) || data.getAddress().contains(s)) {
                    } else {
                        temp.remove(data);
                    }
                }
                if (adapter != null) {
                    adapter.setDatas(temp);
                }
                if (temp.size() == 0) {
                    showMessage("搜索“"+s+"”没有结果。");
                }
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
    public boolean bindEvents() {
        return false;
    }

    @Override
    public void showData(List data, boolean canNext) {
        iniAdapter();
        mDatas = data;
        adapter.setDatas(mDatas);
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
        Toast.makeText(this, mes, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showError(String mes) {
        showMessage(mes);
    }

    @Override
    public void onTokenError() {

    }

    @Override
    public void onRVItemClick(ViewGroup parent, View itemView, int position) {
        Intent intent = new Intent(this, ExerciseDetailActivity.class);
        intent.putExtra("exercise",adapter.getDatas().get(position));
        startActivity(intent);
    }

    private void iniAdapter() {
        if (adapter == null) {
            LinearLayoutManager layoutManager = new LinearLayoutManager(this);
            mDataRv.setLayoutManager(layoutManager);
            adapter = new ExerciseRVAdapter(mDataRv,this);
            adapter.setOnRVItemClickListener(this);
            mDataRv.setAdapter(adapter);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
    }

    @OnClick(R.id.btn_back)
    public void back(){
        finish();
    }

    @OnClick(R.id.exercise_new)
    public void createNew(){
        startActivity(new Intent(this, ExerciseNew.class));
    }

}
