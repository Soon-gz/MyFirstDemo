package com.abings.baby.ui.aboutme;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.abings.baby.R;
import com.abings.baby.WineApplication;
import com.abings.baby.data.remote.RetrofitUtils;
import com.abings.baby.ui.aboutme.association.AssociationActivity;
import com.abings.baby.ui.aboutme.teacher.AboutMeTeacherFragment;
import com.abings.baby.ui.aboutme.user.AboutMeFragment;
import com.abings.baby.ui.aboutme.user.AboutMeFragmentMvpView;
import com.abings.baby.ui.base.BaseActivity;
import com.abings.baby.ui.main.MainActivity;
import com.abings.baby.utils.CustomAlertDialog;
import com.abings.baby.utils.ImageLoaderUtil;
import com.abings.baby.utils.KeyBoardUtils;
import com.abings.baby.utils.ProgressDialogHelper;
import com.socks.library.KLog;
import com.soundcloud.android.crop.Crop;

import java.io.File;
import java.util.List;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;


/**
 * 关于个人资料的主界面
 */
public class AboutMeActivity extends BaseActivity implements AboutMeMvpView {

    @Inject
    AboutMePresenter aboutMePresenter;
    @Bind(R.id.btn_back)
    TextView btnBack;
    @Bind(R.id.edit_save)
    TextView editSave;
    @Bind(R.id.circle_heads)
    CircleImageView circleHeads;

    private AboutMeFragmentMvpView aboutMeFragmentMvpView;


    @Override
    protected int getContentViewLayoutID() {
        return R.layout.activity_aboutme;
    }

    @Override
    protected void iniInjector() {
        activityComponent().inject(this);
        aboutMePresenter.attachView(this);
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
        initFragment();
    }

    private void initFragment() {
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();

        if (WineApplication.getInstance().isTeacher()) {
            AboutMeTeacherFragment aboutMeTeacherFragment = new AboutMeTeacherFragment();
            aboutMeFragmentMvpView = aboutMeTeacherFragment;
            ft.replace(R.id.aboutme_content, aboutMeTeacherFragment);
        } else {
            AboutMeFragment aboutMeFragment = new AboutMeFragment();
            aboutMeFragmentMvpView = aboutMeFragment;
            ft.replace(R.id.aboutme_content, aboutMeFragment);
        }
        ft.commit();
        aboutMeFragmentMvpView.setHeaderImageView(circleHeads);
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
        showToast(mes);
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
        } else if (resultCode == AssociationActivity.kAdd) {
            //添加关系
            aboutMeFragmentMvpView.refreshAddRelative();
        }
    }

    private void beginCrop(Uri source) {
        Uri destination = Uri.fromFile(new File(Environment.getExternalStorageDirectory(), "image.jpg"));
        Crop.of(source, destination).asSquare().withMaxSize(512, 512).start(this);
    }

    private void handleCrop(int resultCode, Intent result) {
        if (resultCode == RESULT_OK) {
            KLog.e(TAG, Crop.getOutput(result).toString());
//            KLog.e(TAG, "path = " + getRealPathFromURI(Crop.getOutput(result)));
//            aboutMePresenter.uploadImage(Crop.getOutput(result).toString().substring(7), true);
            aboutMePresenter.uploadImage(Crop.getOutput(result).toString().substring(7), WineApplication.getInstance().isTeacher());
//            FileUploadManager.upload(Crop.getOutput(result).toString().substring(7), WineApplication.getInstance().getToken());
//            circleHeads.setImageURI(Crop.getOutput(result));
        } else if (resultCode == Crop.RESULT_ERROR) {
            Toast.makeText(this, Crop.getError(result).getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    public String getRealPathFromURI(Uri contentUri) {
        String res = null;
        String[] proj = {MediaStore.Images.Media.DATA};
        Cursor cursor = getContentResolver().query(contentUri, proj, null, null, null);
        if (cursor.moveToFirst()) {
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            res = cursor.getString(column_index);
        }
        cursor.close();
        return res;
    }

    @OnClick({R.id.btn_back, R.id.edit_save, R.id.circle_heads})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_back:
                KeyBoardUtils.closeKeybord(this);
                if (aboutMeFragmentMvpView.isEdit()){
                    CustomAlertDialog.dialogWithSureCancel("确定放弃编辑页面？", this);
                }else{
                    finish();
                }
                break;
            case R.id.edit_save:
                aboutMeFragmentMvpView.setEditOrSave(editSave);
                break;
            case R.id.circle_heads:
                Crop.pickImage(this);
                break;
        }
    }


    @Override
    public void updateHeader(String name) {
        if (WineApplication.getInstance().isTeacher()) {
            ImageLoaderUtil.loadHeadImage(this,RetrofitUtils.BASE_TEACHER_PHOTO_URL,name,R.drawable.left_menu_header_image,R.drawable.left_menu_header_image,circleHeads);
        } else {
            ImageLoaderUtil.loadHeadImage(this,RetrofitUtils.BASE_USER_PHOTO_URL ,name,R.drawable.left_menu_header_image,R.drawable.left_menu_header_image,circleHeads);
        }
        MainActivity.SingleInstance.updateHeader(name);
    }



    @Override
    protected void onDestroy() {
        super.onDestroy();
        aboutMePresenter.detachView();
    }


}
