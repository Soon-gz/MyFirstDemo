package com.abings.baby.ui.baby;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.abings.baby.R;
import com.abings.baby.WineApplication;
import com.abings.baby.data.model.BabyItem;
import com.abings.baby.data.model.ClassItem;
import com.abings.baby.data.remote.RetrofitUtils;
import com.abings.baby.ui.base.BaseActivity;
import com.abings.baby.ui.main.MainActivity;
import com.abings.baby.utils.ProgressDialogHelper;
import com.bumptech.glide.Glide;
import com.soundcloud.android.crop.Crop;

import java.io.File;
import java.util.List;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;


public class BabyInfoActivity extends BaseActivity implements BabyInfoMvpView {

    @Inject
    BabyInfoPresenter mPresenter;
    @Bind(R.id.btn_back)
    TextView btnBack;
    @Bind(R.id.edit_save)
    TextView editSave;
    @Bind(R.id.circle_heads)
    CircleImageView circleHeads;
    @Bind(R.id.input_text_name)
    TextView inputTextName;
    @Bind(R.id.gender)
    TextView gender;
    @Bind(R.id.gender_layout)
    LinearLayout genderLayout;
    @Bind(R.id.input_text_nick_name)
    TextView inputTextNickName;
    @Bind(R.id.input_edit_nick_name)
    EditText inputEditNickName;
    @Bind(R.id.input_text_birthday)
    TextView inputTextBirthday;
    @Bind(R.id.birthday_layout)
    LinearLayout birthdayLayout;
    @Bind(R.id.text_school)
    TextView text_school;
    @Bind(R.id.gradeclass)
    TextView gradeclass;

    @Bind(R.id.text_title)
    TextView text_title;
    @Bind(R.id.text_name)
    TextView text_name;
    @Bind(R.id.text_gender)
    TextView text_gender;
    @Bind(R.id.text_nick_name)
    TextView text_nick_name;
    @Bind(R.id.text_birthday)
    TextView text_birthday;

    @Bind(R.id.babyinfo_linear)
    LinearLayout babyinfo_linear;



    private boolean isBaby = false;
    private String position;


    public void initEditState() {
        if (isBaby){
            inputTextNickName.setVisibility(View.GONE);
            inputEditNickName.setVisibility(View.VISIBLE);
            inputEditNickName.requestFocus();
            inputTextName.setTextColor(Color.parseColor("#e8e8e8"));
            gender.setTextColor(Color.parseColor("#e8e8e8"));
            inputTextBirthday.setTextColor(Color.parseColor("#e8e8e8"));
            text_school.setTextColor(Color.parseColor("#e8e8e8"));
            gradeclass.setTextColor(Color.parseColor("#e8e8e8"));
            editSave.setText("保存");
        }else{
            editSave.setText("保存");
        }

    }

    public void initNormalState() {
        if (isBaby){
            inputEditNickName.setVisibility(View.GONE);
            inputTextNickName.setVisibility(View.VISIBLE);
            inputTextName.setTextColor(Color.parseColor("#000000"));
            gender.setTextColor(Color.parseColor("#000000"));
            inputTextBirthday.setTextColor(Color.parseColor("#000000"));
            text_school.setTextColor(Color.parseColor("#000000"));
            gradeclass.setTextColor(Color.parseColor("#000000"));
            editSave.setText("编辑");
        }else{
//            editSave.setText("编辑");
        }

    }

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.activity_babyinfo;
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
        return TransitionMode.RIGHT;
    }

    @Override
    protected void getBundleExtras(Bundle extras) {

    }


    @Override
    protected void initViewsAndEvents(Bundle savedInstanceState) {
        initNormalState();

        if (getIntent().hasExtra("baby")) {
            isBaby = true;
            BabyItem baby = (BabyItem) getIntent().getSerializableExtra("baby");
            updateView(baby);
        }else if (getIntent().hasExtra("class")){
            isBaby = false;
            ClassItem classItem = (ClassItem) getIntent().getSerializableExtra("class");
            updateClassView(classItem);
        }
        position = getIntent().getStringExtra("position");

    }

    private void initTextName(){
        if (isBaby){
            text_title.setText("宝贝资料");
            text_name.setText("姓名：");
            text_gender.setText("性别：");
            text_nick_name.setText("昵称：");
            text_birthday.setText("生日：");
            babyinfo_linear.setVisibility(View.VISIBLE);
        }else{
            text_title.setText("班级资料");
            text_name.setText("班级：");
            text_gender.setText("人数：");
            text_nick_name.setText("学级：");
            text_birthday.setText("昵称：");
            editSave.setVisibility(View.GONE);
            babyinfo_linear.setVisibility(View.GONE);
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
            ProgressDialogHelper.getInstance().showProgressDialog(this, "加载中...");
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

    }

    @Override
    public void onTokenError() {
        startLoginActivity();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent result) {
        if (requestCode == Crop.REQUEST_PICK && resultCode == RESULT_OK) {
            beginCrop(result.getData());
        } else if (requestCode == Crop.REQUEST_CROP) {
            handleCrop(resultCode, result);
        }
    }

    private void beginCrop(Uri source) {
        Uri destination = Uri.fromFile(new File(Environment.getExternalStorageDirectory(), "image.jpg"));
        Crop.of(source, destination).asSquare().withMaxSize(512, 512).start(this);
    }

    private void handleCrop(int resultCode, Intent result) {
        if (resultCode == RESULT_OK) {
           // KLog.e(TAG, Crop.getOutput(result).toString());
            mPresenter.uploadImage(Crop.getOutput(result).toString().substring(7));
            Bitmap bitmap = BitmapFactory.decodeFile(Crop.getOutput(result).toString().substring(7));
            circleHeads.setImageBitmap(bitmap);
        } else if (resultCode == Crop.RESULT_ERROR) {
            Toast.makeText(this, Crop.getError(result).getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private boolean editState = false;

    @OnClick({R.id.btn_back, R.id.edit_save, R.id.circle_heads})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_back:
                finish();
                break;
            case R.id.edit_save:
//                if (WineApplication.getInstance().isTeacher()) {
//                    return;
//                }
                if (isBaby){
                    if (editState) {
                        editState = false;
                        saveInfo();
                    } else {
                        if (mBabyDetail == null) {
                            return;
                        }
                        editState = true;
                        initEditState();
                    }
                }else{
                    if (editState) {
                        editState = false;
                        saveInfo();
                    } else {
                        if (itemInfo == null) {
                            return;
                        }
                        editState = true;
                        initEditState();
                    }
                }

                break;
            case R.id.circle_heads:
                if (isBaby){
                    Crop.pickImage(this);
                }else{
                    showMessage("不好意思，教师端暂不支持自定义头像~");
                }
                break;
        }
    }


    private BabyItem mBabyDetail;

    public void saveInfo() {
        if (isBaby){
            if (inputEditNickName.getText().toString().equals(mBabyDetail.getNick_name())) {
                showToast("资料未作修改！");
                initNormalState();
            } else {
                mPresenter.updateUserInfo(inputEditNickName.getText().toString());
            }
        }else{
            showToast("班级资料暂时无法修改！");
            initNormalState();
        }

    }

    public void updateView(BabyItem baby) {
        initTextName();
        mBabyDetail = baby;
        Glide.with(this).load(RetrofitUtils.BASE_BABY_PHOTO_URL + baby.getPhoto()).placeholder(R.drawable.image_onload).error(R.drawable.left_menu_header_image).dontAnimate().into(circleHeads);
        text_school.setText(baby.getFk_shool_id());
        if (baby.getSex().equals("False")) {
            gender.setText("男");
        } else {
            gender.setText("女");
        }
        inputTextNickName.setText(baby.getNick_name());
        inputTextBirthday.setText(baby.getBirthday());
        inputTextName.setText(baby.getName());
        gradeclass.setText(baby.getFk_grade_class_id());
        inputEditNickName.setText(baby.getNick_name());
    }
    private ClassItem itemInfo;

    private void updateClassView(ClassItem classItem) {
        initTextName();
        itemInfo = classItem;
        //头像设置，后台没数据
        //Glide.with(this).load(RetrofitUtils.BASE_BABY_PHOTO_URL + classItem.getPhoto()).placeholder(R.drawable.image_onload).error(R.drawable.image_empty_failed).dontAnimate().into(circleHeads);
        inputTextName.setText(classItem.getClass_name());
        gender.setText("");
        inputTextNickName.setText(classItem.getGrade_name());
        inputTextBirthday.setText("");
        text_school.setText(classItem.getFk_school_id());
    }

    /**
     *上传头像之后更新界面的头像，同时调用MainAcitvity的BabyItem设置相应的photo的字段
     *@author Shuwen
     *created at 2016/7/29 13:40
     */
    @Override
    public void updateHeader(String name) {
        Glide.with(this).load(RetrofitUtils.BASE_BABY_PHOTO_URL + name).error(R.drawable.left_menu_header_image).dontAnimate().into(circleHeads);
        MainActivity.SingleInstance.setChildHead(position,name);
    }

    @Override
    public void updateUserInfo(boolean result) {
        if (result) {
            initNormalState();
            inputTextNickName.setText(inputEditNickName.getText().toString());
            WineApplication.getInstance().getBaby().setNick_name(inputEditNickName.getText().toString());
//            aboutMePresenter.loadData(true);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mPresenter.detachView();
    }
}
