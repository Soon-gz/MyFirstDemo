package com.abings.baby.ui.aboutme.teacher;

import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import com.abings.baby.R;
import com.abings.baby.data.model.RelativePerson;
import com.abings.baby.data.model.TeacherDetail;
import com.abings.baby.data.model.UserDetail;
import com.abings.baby.data.remote.RetrofitUtils;
import com.abings.baby.ui.aboutme.BottomDialogUtils;
import com.abings.baby.ui.aboutme.user.AboutMeFragmentMvpView;
import com.abings.baby.ui.aboutme.user.AboutMeFragmentPresenter;
import com.abings.baby.ui.aboutme.user.BottomPickerDateDialog;
import com.abings.baby.ui.base.BaseActivity;
import com.abings.baby.ui.base.BaseFragment;
import com.abings.baby.utils.ImageLoaderUtil;
import com.abings.baby.utils.ProgressDialogHelper;

import java.util.List;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by zwj on 2016/7/22.
 * 老师的个人资料
 */
public class AboutMeTeacherFragment extends BaseFragment implements AboutMeFragmentMvpView {
    @Inject
    AboutMeFragmentPresenter mPresenter;
    @Bind(R.id.fmAboutMeTeacher_et_name)
    EditText etName;
    @Bind(R.id.fmAboutMeTeacher_et_sex)
    EditText etSex;//性别，选择
    @Bind(R.id.fmAboutMeTeacher_et_birthday)
    EditText etBirthday;//生日，选择
    @Bind(R.id.fmAboutMeTeacher_et_mobile_phone)
    EditText etMobilePhone;
    @Bind(R.id.fmAboutMeTeacher_et_email)
    EditText etEmail;
    private CircleImageView mHeaderImageView;
    private TextView mTextView;
    private boolean isEdit = false;
    private TeacherDetail mTeacherDetail;


    @Override
    protected int getContentViewLayoutID() {
        return R.layout.fragment_aboutme_teacher;
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
        // 初始化
        setEditDetail(isEdit);

        etName.setEnabled(false);
        etMobilePhone.setEnabled(false);

        etSex.setFocusable(false);
        etBirthday.setFocusable(false);

        mPresenter.loadTeacherDetail();
    }

    @Override
    protected void onUserVisible() {

    }

    @Override
    protected void onUserInvisible() {

    }

    @Override
    public void showUserDetail(UserDetail userDetail) {

    }

    @Override
    public void showUserRelative(List<RelativePerson.ResultBean> resultBeans) {

    }

    @Override
    public void updateDetailResult() {
        //更新用户信息成功
        setUnEdit();//不能编辑
    }

    @Override
    public void setHeaderImageView(CircleImageView headerImageView) {
        mHeaderImageView = headerImageView;
    }

    @Override
    public void deleteRelativeItem(int delIndex) {

    }

    @Override
    public void updateRelativeByDel(int delIndex) {

    }

    @Override
    public void refreshAddRelative() {

    }

    @Override
    public void setEditOrSave(TextView textView) {
        mTextView = textView;
        String editSaveName = textView.getText().toString().trim();
        if ("编辑".equals(editSaveName)) {
            textView.setText("保存");
            setEnEdit();
        } else {
            String email = etEmail.getText().toString().trim();
            String birthday = etBirthday.getText().toString().trim();
            String sex = etSex.getText().toString().trim();
            if (email.equals(mTeacherDetail.getEmail())
                    && birthday.equals(mTeacherDetail.getBirthday())
                    && sex.equals(mTeacherDetail.getSexName())) {
                showToast("个人信息未修改");
            } else {
                mTeacherDetail.setEmail(email);
                mTeacherDetail.setBirthday(birthday);
                mTeacherDetail.setSexName(sex);
                mPresenter.updateTeacherDetail(email, birthday, mTeacherDetail.getSexNumber());
            }
        }
    }

    @Override
    public void showTeacherDetail(TeacherDetail detail) {
        setTeacherDetail(detail);
    }

    @Override
    public boolean isEdit() {
        return isEdit;
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
     * 初始化元素
     *
     * @param teacherDetail
     */
    private void setTeacherDetail(TeacherDetail teacherDetail) {
        if (teacherDetail != null) {
            ImageLoaderUtil.loadHeadImage(getContext(), RetrofitUtils.BASE_TEACHER_PHOTO_URL, teacherDetail.getPhoto(), R.drawable.left_menu_header_image, R.drawable.left_menu_header_image, mHeaderImageView);
            mTeacherDetail = teacherDetail;
            etName.setText(teacherDetail.getName());
            etMobilePhone.setText(teacherDetail.getMobile_phone());
            etEmail.setText(teacherDetail.getEmail());
            etBirthday.setText(teacherDetail.getBirthday());
            etSex.setText(teacherDetail.getSexName());
        } else {
            Log.e(TAG_LOG, "TeacherDetail ==  null");
        }
    }

    @OnClick(R.id.fmAboutMeTeacher_et_sex)
    public void sexOnClick() {
        final String[] items = {"男", "女"};
        BottomDialogUtils.getBottomListDialog(getActivity(), items, new BottomDialogUtils.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                etSex.setText(items[position]);
            }
        });
    }

    @OnClick(R.id.fmAboutMeTeacher_et_birthday)
    public void birthdayOnClick() {
        String birthday = etBirthday.getText().toString().trim();
        BottomDialogUtils.getBottomDatePickerDialog(getActivity(), birthday, new BottomPickerDateDialog.BottomOnDateChangedListener() {
            @Override
            public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                etBirthday.setText(year + "-" + monthOfYear + "-" + dayOfMonth);
            }
        });
    }

    /**
     * 可以编辑
     */
    public void setEnEdit() {
        this.isEdit = true;
        setEditDetail(isEdit);
    }

    /**
     * 不可以编辑
     */
    public void setUnEdit() {
        this.isEdit = false;
        setEditDetail(isEdit);
        mTextView.setText("编辑");
    }

    private void setEditDetail(boolean enable) {
        if (enable) {
            etName.setTextColor(unAbleEditColor);
            etMobilePhone.setTextColor(unAbleEditColor);
        } else {
            etName.setTextColor(getResources().getColor(R.color.text_black));
            etMobilePhone.setTextColor(getResources().getColor(R.color.text_black));
        }
        etEmail.setEnabled(enable);
        etBirthday.setEnabled(enable);
        etBirthday.setClickable(enable);
        etSex.setEnabled(enable);
        etSex.setClickable(enable);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mPresenter.detachView();
    }
}
