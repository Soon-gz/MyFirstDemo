package com.abings.baby.ui.aboutme.association;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.abings.baby.R;
import com.abings.baby.data.model.RelativePerson;
import com.abings.baby.ui.base.BaseActivity;
import com.abings.baby.utils.CustomAlertDialog;
import com.abings.baby.utils.ProgressDialogHelper;

import java.util.List;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Created by zwj on 2016/7/26.
 * 子账号的相关界面
 */
public class AssociationActivity extends BaseActivity implements AssociationMvpView {

    public static final int kAdd = 1987;//R.string.add_association;
    @Inject
    AssociationPresenter mPresenter;
    @Bind(R.id.association_btn_back)
    TextView btnBack;
    @Bind(R.id.association_btn_save)
    TextView btnSave;
    @Bind(R.id.association_circle_heads)
    ImageView circleHeads;
    @Bind(R.id.association_et_name)
    EditText etName;
    @Bind(R.id.association_et_relation_desc)
    EditText etRelationDesc;
    @Bind(R.id.association_et_mobile_phone)
    EditText etMobilePhone;

    private boolean isEdit = false;


    @Override
    protected int getContentViewLayoutID() {
        return R.layout.activity_association;
    }

    @Override
    protected void iniInjector() {
        activityComponent().inject(this);
        mPresenter.attachView(this);
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
        RelativePerson.ResultBean resultBean = (RelativePerson.ResultBean) getIntent().getSerializableExtra(RelativePerson.ResultBean.kName);
        if (resultBean == null) {
            //是添加
            btnSave.setVisibility(View.VISIBLE);
            isEdit = true;
        } else {
            //查看
            btnSave.setVisibility(View.GONE);
            etName.setEnabled(false);
            etMobilePhone.setEnabled(false);
            etRelationDesc.setEnabled(false);

            isEdit = false;
            etName.setText(resultBean.getName());
            etRelationDesc.setText(resultBean.getRelation_desc());
            etMobilePhone.setText(resultBean.getMobile_phone());
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

    }

    @Override
    public void showLoadingProgress(boolean show) {
        if (show) {
            ProgressDialogHelper.getInstance().showProgressDialog(this, "正在载入");
        } else {
            ProgressDialogHelper.getInstance().hideProgressDialog();
        }
    }

    @Override
    public void showMessage(String mes) {

    }

    @Override
    public void showError(String mes) {
        showToast(mes);
    }

    @Override
    public void onTokenError() {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mPresenter.detachView();
    }


    @OnClick(R.id.association_btn_back)
    public void backOnClick() {
        String name = getEditTextContext(etName);
        String relationDesc = getEditTextContext(etRelationDesc);
        String mobilePhone = getEditTextContext(etMobilePhone);
        if ("".equals(name) && "".equals(relationDesc) && "".equals(mobilePhone) || !isEdit) {
            finish();
        }else{
            CustomAlertDialog.dialogWithSureCancel("确定放弃编辑页面？", this);
        }
    }

    @OnClick(R.id.association_btn_save)
    public void saveOnClick() {
        String name = getEditTextContext(etName);
        String relationDesc = getEditTextContext(etRelationDesc);
        String mobilePhone = getEditTextContext(etMobilePhone);
        if ("".equals(name) && "".equals(relationDesc) && "".equals(mobilePhone)) {
            showToast("请完善所有信息！");
        } else {
            mPresenter.addRelativePerson(name, relationDesc, mobilePhone);
        }
    }


    private String getEditTextContext(EditText editText) {
        return editText.getText().toString().trim();
    }

    @Override
    public void addSuccess(RelativePerson.ResultBean resultBean) {
        Intent intent = new Intent();
        intent.putExtra(RelativePerson.ResultBean.kName, resultBean);
        setResult(kAdd, intent);
        finish();
    }
}
