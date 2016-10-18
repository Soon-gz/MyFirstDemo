package com.abings.baby.ui.exercise.exerciseDetail;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.abings.baby.R;
import com.abings.baby.WineApplication;
import com.abings.baby.ui.base.BaseActivity;
import com.abings.baby.ui.exercise.ExerciseModel;
import com.abings.baby.ui.home2.CustomActionSheetDialog;
import com.abings.baby.ui.home2.ShareBottomDialog;
import com.abings.baby.utils.CustomAlertDialog;
import com.flyco.dialog.listener.OnOperItemClickL;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;

import java.util.List;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ExerciseDetailActivity extends BaseActivity implements ExerciseDetailMvpView {

    private ExerciseModel exerciseModel;
    private AlertDialog myDialog = null;

    @Inject
    ExerciseDetailPrecenter mPrecenter;

    @Bind(R.id.text_title)
    TextView textTitle;
    @Bind(R.id.text_time)
    TextView textTime;
    @Bind(R.id.text_address)
    TextView textAddress;
    @Bind(R.id.text_money)
    TextView textMoney;
    @Bind(R.id.text_peoples)
    TextView textPeoples;
    @Bind(R.id.text_stoptime)
    TextView textStoptime;
    @Bind(R.id.text_contact)
    TextView textContact;
    @Bind(R.id.text_content)
    TextView textContent;
    @Bind(R.id.textwant)
    TextView textwant;
    @Bind(R.id.textrelease)
    TextView textrelease;

    EditText people;


    public void updateView(ExerciseModel exerciseModel){
        textTitle.setText(exerciseModel.getTitle());
        textAddress.setText("活动地址："+exerciseModel.getAddress());
        textContact.setText("联系人："+exerciseModel.getTeacher());
        textMoney.setText("活动费用："+exerciseModel.getMoney());
        textContent.setText("　　"+exerciseModel.getContent());
        textStoptime.setText("报名截至日期："+exerciseModel.getStoptime());
        textTime.setText("活动时间："+exerciseModel.getTime());
        textPeoples.setText("活动人数：" + exerciseModel.getPeoples());
        if (!WineApplication.getInstance().isTeacher()){
            if (exerciseModel.isRead()){
                textrelease.setVisibility(View.VISIBLE);
                textwant.setVisibility(View.GONE);
            }else{
                textrelease.setVisibility(View.GONE);
                textwant.setVisibility(View.VISIBLE);
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
    }

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.activity_exercise_detail;
    }

    @OnClick(R.id.btn_share)
    public void share(){
        showPopup();
    }

    private void showPopup() {
        final String[] stringItems = {"分享"};
        final CustomActionSheetDialog dialog = new CustomActionSheetDialog(this, stringItems, null);
        dialog.isTitleShow(false).isCancelShow(false)
                .itemTextColor(this.getResources().getColor(R.color.ese_white))
                .itemPressColor(this.getResources().getColor(R.color.ese_orange))
                .lvBgColor(this.getResources().getColor(R.color.baby_orange))
                .show();

        dialog.setOnOperItemClickL(new OnOperItemClickL() {
            @Override
            public void onOperItemClick(AdapterView<?> parent, View view, int position, long id) {
                dialog.dismiss();
                switch (position) {
                    case 0:
                        showSharePopup();
                        break;
                    default:
                        break;
                }
            }
        });
    }
    ShareBottomDialog dialog;
    void showSharePopup() {

        dialog = new ShareBottomDialog(this, null);
        dialog.setListner(new ShareBottomDialog.onItemClickLitener() {
            @Override
            public void onItemClick(int position) {
                dialog.dismiss();
                share(position);
            }
        }).show();

    }

    void share(int i) {
        UMImage image = null;

        switch (i) {
            case 0:
                dialog.shareWeixin(ExerciseDetailActivity.this, "HelloBaby", exerciseModel.getTitle(), image, umShareListener,false);
                break;
            case 1:
                dialog.shareWeixinCircle(ExerciseDetailActivity.this, "HelloBaby", exerciseModel.getTitle(), image, umShareListener,true);
                break;
            case 2:
                dialog.shareQQ(ExerciseDetailActivity.this, "HelloBaby", exerciseModel.getTitle(), image, umShareListener);
                break;
            case 3:
                dialog.shareWeibo(ExerciseDetailActivity.this, "HelloBaby", exerciseModel.getTitle(), image, umShareListener);
                break;
            default:
                break;
        }
    }

    private UMShareListener umShareListener = new UMShareListener() {
        @Override
        public void onResult(SHARE_MEDIA platform) {
            showMessage("分享成功");
        }

        @Override
        public void onError(SHARE_MEDIA platform, Throwable t) {
            showMessage("分享失败");
        }

        @Override
        public void onCancel(SHARE_MEDIA platform) {
            showMessage("分享取消");
        }
    };

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
        if (getIntent().hasExtra("exercise")){
            exerciseModel = (ExerciseModel) getIntent().getSerializableExtra("exercise");
            updateView(exerciseModel);
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

    @OnClick(R.id.btn_back)
    public void back(){
        finish();
    }
    @OnClick(R.id.textrelease)
    public void relase(){
        CustomAlertDialog.dialogExSureCancel("请确认取消报名", this);
    }
    @OnClick(R.id.textwant)
    public void want(){
        CustomAlertDialog.dialogWithInput(this,people);
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
        Toast.makeText(ExerciseDetailActivity.this, mes, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showError(String mes) {

    }

    @Override
    public void onTokenError() {

    }
}
