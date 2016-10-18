package com.abings.baby.ui.feedback;

import android.content.Intent;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.abings.baby.R;
import com.abings.baby.ui.base.BaseActivity;
import com.abings.baby.ui.base.BaseFragment;
import com.abings.baby.ui.login.LoginActivity;
import com.abings.baby.ui.main.MainActivity;
import com.abings.baby.utils.StringUtils;

import java.util.List;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * 作者：黄斌 on 2016/2/27 18:47
 * 说明：
 */
public class FeedBackFragment extends BaseFragment implements FeedBackMvpView {


    @Inject
    FeedBackPresenter feedBackPresenter;

    @Bind(R.id.main_lay)
    View main_lay;

    @Bind(R.id.input_text)
    EditText input_text;

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.fragment_feedback;
    }

    @Override
    protected void iniInjector() {
        ((BaseActivity) getActivity()).activityComponent().inject(this);
        feedBackPresenter.attachView(this);
    }

    @Override
    protected void onFirstUserVisible() {

    }

    @Override
    protected View getLoadingTargetView() {
        return main_lay;
    }

    @Override
    protected void initViewsAndEvents() {
    }


    @Override
    protected void onUserVisible() {

    }

    @Override
    protected void onUserInvisible() {

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
        toggleShowLoading(show, getResources().getString(R.string.ese_sendding_datas));
    }

    @Override
    public void showMessage(String mes) {

    }

    @Override
    public void showError(String mes) {

    }

    @Override
    public void onTokenError() {
        getActivity().startActivity(new Intent(getActivity(), LoginActivity.class));
    }

    @OnClick(R.id.btn_send)
    void Send() {
        //发送建议
        if (!StringUtils.isEmpty(input_text.getText().toString())) {
            feedBackPresenter.feebackAdd(input_text.getText().toString());
        }
    }

    @Override
    public void feedBackSucceed(boolean result) {
        if (result) {
            Toast.makeText(getActivity(), "意见提交成功,感谢你的支持！", Toast.LENGTH_SHORT).show();
            input_text.setText("");
            ((MainActivity) getActivity()).titleBack();
        } else {
            Toast.makeText(getActivity(), "意见提交失败！", Toast.LENGTH_SHORT).show();
        }
    }
}
