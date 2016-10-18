package com.abings.baby.ui.contacts.detail;

import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.abings.baby.R;
import com.abings.baby.data.model.UserContact;
import com.abings.baby.ui.base.BaseActivity;
import com.socks.library.KLog;

import java.util.List;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Created by huangbin on 16/2/19.
 */
public class ContactDetailActivity extends BaseActivity implements ContactDetailMvpView {

    @Inject
    ContactDetailPresenter contactDetailPresenter;
    @Bind(R.id.listView)
    ListView listView;
    @Bind(R.id.title_center)
    TextView titleCenter;


    @Override
    protected int getContentViewLayoutID() {
        return R.layout.contact_detail;
    }

    @Override
    protected void iniInjector() {
        activityComponent().inject(this);
        contactDetailPresenter.attachView(this);
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
        if (getIntent().hasExtra("contact")) {
            UserContact contact = (UserContact) getIntent().getSerializableExtra("contact");
            if (contact != null) {
                KLog.e("contact = " + contact.toString());
                titleCenter.setText(contact.getName());
                ContactDetailListAdapter adapter = new ContactDetailListAdapter(ContactDetailActivity.this, contact);
                listView.setAdapter(adapter);
            }
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

    }

    @Override
    public void showMessage(String mes) {

    }

    @Override
    public void showError(String mes) {

    }

    @Override
    public void onTokenError() {
        startLoginActivity();
    }

    @OnClick({R.id.btn_back})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_back:
                finish();
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        contactDetailPresenter.detachView();
    }
}
