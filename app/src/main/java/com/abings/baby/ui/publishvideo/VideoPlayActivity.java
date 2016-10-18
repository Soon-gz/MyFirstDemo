package com.abings.baby.ui.publishvideo;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.VideoView;

import com.abings.baby.Const;
import com.abings.baby.R;
import com.abings.baby.data.remote.RetrofitUtils;
import com.abings.baby.ui.base.BaseActivity;
import com.abings.baby.utils.ProgressDialogHelper;

import java.io.File;
import java.util.List;

import javax.inject.Inject;

import butterknife.OnClick;


/**
 * Created by zwj on 2016/6/28.
 * 播放video
 */
public class VideoPlayActivity extends BaseActivity implements VideoPlayMVPView {
    public static final String kPath = "path";
    public String videopath;
    public String imagepath;
    private VideoView videoView;
    private String filerealpath;

    @Inject
    VideoPlayPrecenter mPresenter;

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.activity_play_video;
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
        videoView = (VideoView) findViewById(R.id.playVideo_videoView);
        if (getIntent().hasExtra("video")) {
            videopath = RetrofitUtils.BASE_VIDEO_URL + getIntent().getStringExtra("video");
            File file = new File(Const.videoPath + getIntent().getStringExtra("video"));
            if (file.exists()) {
                filerealpath = file.getAbsolutePath();
                videoView.setVideoPath(file.getAbsolutePath());
                videoView.start();
                setLoop();
            } else {
                mPresenter.downloadVideo(videopath, Const.basePath + "video", getIntent().getStringExtra("video"));
            }
        } else {
            videopath = getIntent().getStringExtra(kPath);
            filerealpath = videopath;
            imagepath = videopath + ".jpg";
            File file = new File(videopath);
            if (file.exists()) {
                videoView.setVideoPath(file.getAbsolutePath());
                videoView.start();
                setLoop();
            }
        }

    }

    public void setLoop(){
        videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {

            @Override
            public void onPrepared(MediaPlayer mp) {
                mp.start();
                mp.setLooping(true);

            }
        });
        videoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {

            @Override
            public void onCompletion(MediaPlayer mp) {
                videoView.setVideoPath(videopath);
                videoView.start();

            }
        });
    }


    @OnClick(R.id.playVideo_tv_cancel)
    public void onClickCancel(){
        finish();
    }

    @Override
    protected View getLoadingTargetView() {
        return null;
    }

    @Override
    protected boolean isApplyStatusBarTranslucency() {
        return false;
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
            ProgressDialogHelper.getInstance().showProgressDialog(this, "正在加载中...");
        } else {
            ProgressDialogHelper.getInstance().hideProgressDialog();
        }
    }

    @Override
    public void showMessage(String mes) {

    }

    @Override
    public void showError(String mes) {

    }

    @Override
    public void onTokenError() {

    }

    @Override
    public void palyVideo(String filepath) {
        filerealpath = filepath;
        File file = new File(filepath);
        videoView.setVideoPath(file.getAbsolutePath());
        videoView.start();
        setLoop();
    }

    @Override
    public void finishActivity() {
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mPresenter.detachView();
    }
}
