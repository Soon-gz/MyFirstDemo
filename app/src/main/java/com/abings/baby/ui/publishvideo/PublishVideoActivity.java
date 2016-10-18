package com.abings.baby.ui.publishvideo;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.abings.baby.R;
import com.abings.baby.ui.base.BaseActivity;
import com.abings.baby.utils.CustomAlertDialog;
import com.abings.baby.utils.KeyBoardUtils;
import com.abings.baby.utils.ProgressDialogHelper;
import com.abings.baby.utils.StringUtils;

import java.io.File;
import java.util.List;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Created by zwj on 2016/7/13.
 * 发布视频界面
 */
public class PublishVideoActivity extends BaseActivity implements PublishVideoMvpView {
    public final static String kVIDEO_THUMB = "video_thumb";
    public final static String kVIDEO_MP4 = "video_mp4";
    @Inject
    PublishVideoPresenter publishVideoPresenter;
    @Bind(R.id.publishvideo_btn_back)
    TextView tvBack;//返回按钮
    @Bind(R.id.publishvideo_send)
    TextView tvSend;//发送按钮
    @Bind(R.id.publishvideo_et)
    EditText etContent;//内容
    @Bind(R.id.publishvideo_image)
    ImageView imageView;//预览图片
    private String videoThumbPath;
    private String videoPath;


    @Override
    protected int getContentViewLayoutID() {
        return R.layout.activity_publishvideo;
    }

    @Override
    protected void iniInjector() {
        activityComponent().inject(this);
        publishVideoPresenter.attachView(this);
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
        videoThumbPath = getIntent().getStringExtra(kVIDEO_THUMB);
        videoPath = getIntent().getStringExtra(kVIDEO_MP4);
        File file = new File(videoThumbPath);
        if (file.exists()) {
            Bitmap bitmap = BitmapFactory.decodeFile(videoThumbPath);
            imageView.setImageBitmap(bitmap);
        } else {
            Toast.makeText(PublishVideoActivity.this, "预览图不存在", Toast.LENGTH_SHORT).show();
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
            ProgressDialogHelper.getInstance().showProgressDialog(this, "正在上传小视频");
        } else {
            ProgressDialogHelper.getInstance().hideProgressDialog();
        }
    }

    @Override
    public void showMessage(String mes) {

    }

    @Override
    public void showError(String mes) {
        Toast.makeText(PublishVideoActivity.this, mes, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onTokenError() {
    }

    @OnClick({R.id.publishvideo_btn_back, R.id.publishvideo_send})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.publishvideo_btn_back:
                CustomAlertDialog.dialogWithSureCancel("确定放弃编辑页面？", this);
                break;
            case R.id.publishvideo_send:
                KeyBoardUtils.closeKeybord(this);
                String content = StringUtils.isEmpty(etContent.getText().toString()) ? "" : etContent.getText().toString();
                publishVideoPresenter.publishVideo(content, videoThumbPath, videoPath);
                break;
        }
    }

    @OnClick(R.id.publishvideo_image)
    public void onClickImageVideo() {

        Intent intent = new Intent(PublishVideoActivity.this, VideoPlayActivity.class);
        intent.putExtra(VideoPlayActivity.kPath, videoPath);
        startActivity(intent);
    }

    @Override
    public void publishSuccess() {
        Toast.makeText(this, "上传成功", Toast.LENGTH_SHORT).show();
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        publishVideoPresenter.detachView();
    }

}
