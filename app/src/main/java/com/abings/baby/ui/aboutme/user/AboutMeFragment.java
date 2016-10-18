package com.abings.baby.ui.aboutme.user;

import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.TextView;

import com.abings.baby.R;
import com.abings.baby.data.model.RelativePerson;
import com.abings.baby.data.model.TeacherDetail;
import com.abings.baby.data.model.UserDetail;
import com.abings.baby.data.remote.RetrofitUtils;
import com.abings.baby.ui.aboutme.BottomDialogUtils;
import com.abings.baby.ui.base.BaseActivity;
import com.abings.baby.ui.base.BaseFragment;
import com.abings.baby.utils.ImageLoaderUtil;
import com.abings.baby.utils.ProgressDialogHelper;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by zwj on 16/7/22.
 * 用户版本的个人信息
 */
public class AboutMeFragment extends BaseFragment implements AboutMeFragmentMvpView {

    @Inject
    AboutMeFragmentPresenter mPresenter;

    @Bind(R.id.fmAboutMe_et_name)
    EditText etName;
    @Bind(R.id.fmAboutMe_et_sex)
    EditText etSex;//性别，选择
    @Bind(R.id.fmAboutMe_et_relation_desc)
    EditText etRelationDesc;//关系
    @Bind(R.id.fmAboutMe_et_birthday)
    EditText etBirthday;//生日，选择
    @Bind(R.id.fmAboutMe_et_mobile_phone)
    EditText etMobilePhone;
    @Bind(R.id.fmAboutMe_et_email)
    EditText etEmail;
    @Bind(R.id.fmAboutMe_et_address)
    EditText etAddress;
    @Bind(R.id.fmAboutMe_relative)
    ExpandableListView expandableListView;
    private UserDetail mUserDetail;

    private RelativeExpandableListAdapter mAdapter;
    private List<RelativePerson.ResultBean> mUserList = new ArrayList<>();
    private boolean isEdit = false;

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.fragment_aboutme;
    }

    @Override
    protected void iniInjector() {
        ((BaseActivity) getActivity()).activityComponent().inject(this);
        mPresenter.attachView(this);
    }

    @Override
    protected void onFirstUserVisible() {

    }

    @Override
    protected View getLoadingTargetView() {
        return null;
    }

    @Override
    protected void initViewsAndEvents() {
        // 初始化控件
        setEditDetail(isEdit);

        etName.setEnabled(false);
        etMobilePhone.setEnabled(false);

        etSex.setFocusable(false);
        etBirthday.setFocusable(false);

        mAdapter = new RelativeExpandableListAdapter(getActivity(), mUserList);
        expandableListView.setAdapter(mAdapter);
        mAdapter.attachView(this);

        mPresenter.loadUserDetail();
    }


    private void setEditDetail(boolean enable) {
        if (enable) {
            etName.setTextColor(unAbleEditColor);
            etMobilePhone.setTextColor(unAbleEditColor);
        } else {
            etName.setTextColor(getResources().getColor(R.color.text_black));
            etMobilePhone.setTextColor(getResources().getColor(R.color.text_black));
        }
        etRelationDesc.setEnabled(enable);
        etAddress.setEnabled(enable);
        etEmail.setEnabled(enable);

        etBirthday.setEnabled(enable);
        etBirthday.setClickable(enable);

        etSex.setEnabled(enable);
        etSex.setClickable(enable);
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
        if (show) {
            ProgressDialogHelper.getInstance().showProgressDialog(getContext(), "正在载入...");
        } else {
            ProgressDialogHelper.getInstance().hideProgressDialog();
        }
    }

    @Override
    public void showMessage(String mes) {
        showToast(mes);
    }

    @Override
    public void showError(String mes) {
        showToast(mes);
    }

    @Override
    public void onTokenError() {

    }


    /**
     * 可以编辑
     */
    public void setEnEdit() {
        this.isEdit = true;
        setEditDetail(isEdit);
        mAdapter.setEditState(true);
    }

    /**
     * 不可以编辑
     */
    public void setUnEdit() {
        this.isEdit = false;
        setEditDetail(isEdit);
        mAdapter.setEditState(false);
        mTextView.setText("编辑");
    }

    @OnClick(R.id.fmAboutMe_et_sex)
    public void sexOnClick() {
        final String[] items = {"男", "女"};
        BottomDialogUtils.getBottomListDialog(getActivity(), items, new BottomDialogUtils.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                etSex.setText(items[position]);
            }
        });
    }

    @OnClick(R.id.fmAboutMe_et_birthday)
    public void birthdayOnClick() {
        String birthday = etBirthday.getText().toString().trim();
        BottomDialogUtils.getBottomDatePickerDialog(getActivity(), birthday, new BottomPickerDateDialog.BottomOnDateChangedListener() {
            @Override
            public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                etBirthday.setText(year + "-" + monthOfYear + "-" + dayOfMonth);
            }
        });
    }


    private TextView mTextView;

    @Override
    public void setEditOrSave(TextView textView) {
        this.mTextView = textView;
        String editSaveName = textView.getText().toString().trim();
        if ("编辑".equals(editSaveName)) {
            textView.setText("保存");
            setEnEdit();
        } else {
            String email = etEmail.getText().toString().trim();
            String birthday = etBirthday.getText().toString().trim();
            String address = etAddress.getText().toString().trim();
            String relation_desc = etRelationDesc.getText().toString().trim();
            String sex = etSex.getText().toString().trim();
            if (email.equals(mUserDetail.getEmail())
                    && birthday.equals(mUserDetail.getBirthday())
                    && address.equals(mUserDetail.getAddress())
                    && relation_desc.equals(mUserDetail.getRelation_desc())
                    && sex.equals(mUserDetail.getSexName())) {
                showToast("个人信息未修改");
            } else {
                mUserDetail.setEmail(email);
                mUserDetail.setBirthday(birthday);
                mUserDetail.setAddress(address);
                mUserDetail.setRelation_desc(relation_desc);
                mUserDetail.setSexName(sex);
                mPresenter.updateUserDetail(email, birthday, address, relation_desc, mUserDetail.getSexNumber());
            }
        }
    }

    @Override
    public void showTeacherDetail(TeacherDetail detail) {
        //家长端不用
    }

    @Override
    public boolean isEdit() {
        return isEdit;
    }

    /**
     * 初始化关系人员
     *
     * @param listUser
     */
    private void setUserRelative(List<RelativePerson.ResultBean> listUser) {

        if (listUser == null) {
            listUser = new ArrayList<>();
        }

        mUserList.clear();
        mUserList.addAll(listUser);
        RelativePerson.ResultBean resultBean = new RelativePerson.ResultBean();
        resultBean.setName("添加更多相关人员");
        mUserList.add(resultBean);
        mAdapter.notifyDataSetChanged();
    }

    /**
     * 初始化元素
     *
     * @param userDetail
     */
    private void setUserDetail(UserDetail userDetail) {
        if (userDetail != null) {
            ImageLoaderUtil.loadHeadImage(getContext(),RetrofitUtils.BASE_USER_PHOTO_URL,userDetail.getPhoto(),R.drawable.left_menu_header_image,R.drawable.left_menu_header_image,headerImageView);
            mUserDetail = userDetail;
            etName.setText(userDetail.getName());
            etMobilePhone.setText(userDetail.getMobile_phone());
            etAddress.setText(userDetail.getAddress());
            etEmail.setText(userDetail.getEmail());
            etBirthday.setText(userDetail.getBirthday());
            etSex.setText(userDetail.getSexName());
            etRelationDesc.setText(userDetail.getRelation_desc());
        } else {
            Log.e(TAG_LOG, "UserDetail ==  null");
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mPresenter.detachView();
    }


    @Override
    public void showUserDetail(final UserDetail userDetail) {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                setUserDetail(userDetail);
            }
        });
    }

    @Override
    public void showUserRelative(List<RelativePerson.ResultBean> resultBeans) {
        setUserRelative(resultBeans);
    }

    @Override
    public void updateDetailResult() {
        //更新用户信息成功
        setUnEdit();//不能编辑

    }

    private CircleImageView headerImageView;

    @Override
    public void setHeaderImageView(CircleImageView headerImageView) {
        this.headerImageView = headerImageView;
    }

    @Override
    public void deleteRelativeItem(int delIndex) {
        //删除以一个
        mPresenter.deleteRelativePerson(mUserList.get(delIndex).getPk_user_id(), delIndex);
    }

    @Override
    public void updateRelativeByDel(int delIndex) {
        mUserList.remove(delIndex);
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void refreshAddRelative() {
        //刷新关系人
        mPresenter.refreshAddRelative();
    }

}