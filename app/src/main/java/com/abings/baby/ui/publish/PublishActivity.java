package com.abings.baby.ui.publish;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.abings.baby.R;
import com.abings.baby.WineApplication;
import com.abings.baby.ui.base.BaseActivity;
import com.abings.baby.utils.CustomAlertDialog;
import com.abings.baby.utils.KeyBoardUtils;
import com.abings.baby.utils.ProgressDialogHelper;
import com.abings.baby.utils.StringUtils;
import com.abings.baby.utils.TLog;
import com.foamtrace.photopicker.PhotoPickerActivity;
import com.foamtrace.photopicker.PhotoPreviewActivity;
import com.foamtrace.photopicker.SelectModel;
import com.foamtrace.photopicker.intent.PhotoPickerIntent;
import com.foamtrace.photopicker.intent.PhotoPreviewIntent;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Created by huangbin on 16/2/19.
 * 发布消息
 */
public class PublishActivity extends BaseActivity implements PublishMvpView, RadioGroup.OnCheckedChangeListener {

    @Inject
    PublishPresenter publishPresenter;
    @Bind(R.id.btn_back)
    TextView btnBack;
    @Bind(R.id.publish)
    TextView publish;
    @Bind(R.id.title_center)
    TextView title_center;


    @Bind(R.id.input_text)
    EditText inputText;
    @Bind(R.id.radio1)
    RadioButton radio1;
    @Bind(R.id.radio2)
    RadioButton radio2;
    @Bind(R.id.radio3)
    RadioButton radio3;
    @Bind(R.id.radio4)
    RadioButton radio4;
    @Bind(R.id.radioGroup)
    RadioGroup radioGroup;

    @Bind(R.id.aligntextNum)
    TextView aligntextNum;

    @Bind(R.id.gridView)
    GridView gridView;

    private int Type = 1;

    private String fk_grade_class_id = "";

    private GridAdapter gridAdapter;
    private static final int REQUEST_CAMERA_CODE = 10;
    private static final int REQUEST_PREVIEW_CODE = 20;
    private ArrayList<String> imagePaths = new ArrayList<>();

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.activity_publish;
    }

    @Override
    protected void iniInjector() {
        activityComponent().inject(this);
        publishPresenter.attachView(this);
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

        if (getIntent().hasExtra("type")) {
            if (getIntent().getIntExtra("type", 2) == 1) {
                title_center.setText("发布照片");
                ArrayList<String> list = (ArrayList<String>) getIntent().getSerializableExtra("imgs");
                imagePaths.addAll(list);
            }else{
                gridView.setVisibility(View.GONE);
            }
        }
        if (WineApplication.getInstance().isTeacher()) {
            radioGroup.setVisibility(View.VISIBLE);
            radioGroup.setOnCheckedChangeListener(this);
            radioGroup.check(R.id.radio1);
            fk_grade_class_id = WineApplication.getInstance().getNowClass().getPk_grade_class_id();
        } else {
            radioGroup.setVisibility(View.GONE);
            fk_grade_class_id = WineApplication.getInstance().getBaby().getFk_grade_class_id();
            Type = 4;
        }

        int cols = getResources().getDisplayMetrics().widthPixels / getResources().getDisplayMetrics().densityDpi;
        cols = cols < 3 ? 3 : cols;
        gridView.setNumColumns(5);
        // preview
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String imgs = (String) parent.getItemAtPosition(position);
                ArrayList<String> images = new ArrayList<>();
                images.addAll(imagePaths);
                if (images.contains(GridAdapter.ADD)) {
                    images.remove(GridAdapter.ADD);
                }
                if (GridAdapter.ADD.equals(imgs)) {
                    PhotoPickerIntent intent = new PhotoPickerIntent(PublishActivity.this);
                    intent.setSelectModel(SelectModel.MULTI);
                    intent.setShowCarema(true); // 是否显示拍照
                    intent.setMaxTotal(9); // 最多选择照片数量，默认为6
                    intent.setSelectedPaths(images); // 已选中的照片地址， 用于回显选中状态
                    startActivityForResult(intent, REQUEST_CAMERA_CODE);
                } else {
                    PhotoPreviewIntent intent = new PhotoPreviewIntent(PublishActivity.this);
                    intent.setCurrentItem(position);
                    intent.setPhotoPaths(images);
                    startActivityForResult(intent, REQUEST_PREVIEW_CODE);
                }
            }
        });
        imagePaths.add(GridAdapter.ADD);
        gridAdapter = new GridAdapter(this, imagePaths);
        gridView.setAdapter(gridAdapter);

        inputText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                aligntextNum.setText("已输入字数："+s.toString().length());
            }

            @Override
            public void afterTextChanged(Editable s) {

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

    }

    @Override
    public void showLoadingProgress(boolean show) {

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
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                // 选择照片
                case REQUEST_CAMERA_CODE:
                    ArrayList<String> list = data.getStringArrayListExtra(PhotoPickerActivity.EXTRA_RESULT);
                    TLog.getInstance().d(TAG,"list: " + "list = [" + list.size());
                    loadAdpater(list);
                    break;
                // 预览
                case REQUEST_PREVIEW_CODE:
                    ArrayList<String> ListExtra = data.getStringArrayListExtra(PhotoPreviewActivity.EXTRA_RESULT);
                    TLog.getInstance().d(TAG, "ListExtra: " + "ListExtra = [" + ListExtra.size());
                    loadAdpater(ListExtra);
                    break;
            }
        }
    }

    private void loadAdpater(ArrayList<String> paths) {
        if (imagePaths != null && imagePaths.size() > 0) {
            imagePaths.clear();
        }
        if (paths.contains(GridAdapter.ADD)) {
            paths.remove(GridAdapter.ADD);
        }
        paths.add(GridAdapter.ADD);
        imagePaths.addAll(paths);
        gridAdapter = new GridAdapter(this, imagePaths);
        gridView.setAdapter(gridAdapter);
    }

    @OnClick({R.id.btn_back, R.id.publish})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_back:
                if (!StringUtils.isEmpty(inputText.getText().toString().trim())){
                    CustomAlertDialog.dialogWithSureCancel("确定放弃编辑页面？",this);
                }else{
                    finish();
                }
                break;
            case R.id.publish:
                KeyBoardUtils.closeKeybord(this);
                if (!StringUtils.isEmpty(inputText.getText().toString())) {
                    publishPresenter.publish(WineApplication.getInstance().getRole(), fk_grade_class_id, Type + "", inputText.getText().toString(),imagePaths);
                }else{
                    showMessage("内容不能为空！");
                }
                break;
        }
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        if (checkedId == radio1.getId()) {
            Type = 1;
        } else if (checkedId == radio2.getId()) {
            Type = 2;
        } else if (checkedId == radio3.getId()) {
            Type = 3;
        }else if (checkedId == radio4.getId()) {
            Type = 4;
        }
    }

    @Override
    public void showDialog(String msg) {
        ProgressDialogHelper.getInstance().showProgressDialog(this, msg);
    }

    @Override
    public void dialogDismiss() {
        ProgressDialogHelper.getInstance().hideProgressDialog();
    }

    @Override
    public void uploadCompleted() {
        finish();
    }

    @Override
    public void onTokenError() {
        startLoginActivity();
    }



    @Override
    protected void onDestroy() {
        super.onDestroy();
        publishPresenter.detachView();
    }
}
