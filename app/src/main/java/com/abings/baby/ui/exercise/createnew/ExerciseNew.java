package com.abings.baby.ui.exercise.createnew;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.abings.baby.R;
import com.abings.baby.WineApplication;
import com.abings.baby.data.model.ClassItem;
import com.abings.baby.ui.base.BaseActivity;
import com.abings.baby.utils.CustomAlertDialog;

import java.util.List;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.OnClick;

public class ExerciseNew extends BaseActivity implements ExerciseNewMvpView{


    @Inject
    ExerciseNewPresenter mPresenter;

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.activity_exercise_new;
    }

    private AlertDialog myDialog = null;
    private List<ClassItem>clases;

    @Bind(R.id.exercise_new_name)
    EditText exercisenewname;

    @Bind(R.id.clear)
    ImageView clear;
    @Bind(R.id.chooseclass)
    ImageView chooseclass;


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
        clases = WineApplication.getInstance().getMyClasss();
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
        CustomAlertDialog.dialogWithSureCancel("确定放弃编辑页面？", this);
    }

    @OnClick(R.id.exercise_new)
    public void createNew(){
        mPresenter.ucrateNewExercise();
    }

    @Override
    public boolean bindEvents() {
        return false;
    }

    @Override
    public void showData(List data, boolean canNext) {

    }

    @OnClick(R.id.clear)
    public void clear(){
        exercisenewname.setText("");
        chooseclass.setVisibility(View.VISIBLE);
        clear.setVisibility(View.GONE);
    }

    @Override
    public void showLoadingProgress(boolean show) {

    }

    @Override
    public void showMessage(String mes) {
        Toast.makeText(ExerciseNew.this, mes, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showError(String mes) {

    }

    @Override
    public void onTokenError() {

    }

    @Override
    public void updateNew() {
        creatAlert("活动发布成功！");
    }

    private void creatAlert(String mes) {

        myDialog = new AlertDialog.Builder(this).create();

        myDialog.show();

        myDialog.getWindow().setContentView(R.layout.alert_dialog);
        myDialog.setCanceledOnTouchOutside(false);


        TextView textView = (TextView) myDialog.getWindow().findViewById(R.id.input0);
        textView.setText(mes);
        myDialog.getWindow()
                .findViewById(R.id.alert_btn_sure1)
                .setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        myDialog.dismiss();
                        finish();
                    }
                });
    }

    @OnClick(R.id.chooseclass)
    public void chooseClass(){
        WineApplication.getInstance().getMyClasss();
        final String[] stringItems = new String[clases.size()*2];
        for (int i = 0; i < clases.size(); i++) {
            stringItems[i] = clases.get(i).getClass_name();
        }
        for (int i = clases.size(); i < clases.size()*2; i++) {
            stringItems[i] = i+"班级";
        }
        final ChooseClassDialog dialog = new ChooseClassDialog(this, stringItems, null,exercisenewname,chooseclass,clear);
        final int[]flags = new int[stringItems.length];
        for (int i = 0; i < flags.length; i++) {
            flags[i] = 0;
        }
        dialog.isTitleShow(true).isCancelShow(false)
                .title("请选择")
                .titleBgColor(getResources().getColor(R.color.ese_orange))
                .titleTextColor(getResources().getColor(R.color.ese_white))
                .itemTextColor(getResources().getColor(R.color.ese_white))
                .setFlags(flags)
                .lvBgColor(getResources().getColor(R.color.baby_orange))
                .show();
    }

}
