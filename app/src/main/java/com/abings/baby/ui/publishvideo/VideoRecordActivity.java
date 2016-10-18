package com.abings.baby.ui.publishvideo;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Rect;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.abings.baby.R;
import com.abings.baby.ui.base.BaseActivity;

import butterknife.Bind;
import butterknife.OnClick;


/**
 * Created by zwj on 2016/6/28.
 * 视频录制界面
 */
public class VideoRecordActivity extends BaseActivity implements VideoRecordSurface.OnRecordListener {

    @Bind(R.id.videoRecorder_tv_cancel)
    TextView tvCancel;

    @Bind(R.id.videoRecorder_btn_start)
    Button btnStart;

    @Bind(R.id.videoRecorder_btn_play)
    Button btnPlay;

    @Bind(R.id.videoRecorder_fl)
    FrameLayout frameLayout;

    @Bind(R.id.videoRecorder_progress)
    VideoProgressView videoProgressView;

    @Bind(R.id.videoRecorder_tv_tips)
    TextView tvTips;

    private int iTime;

    private VideoRecordSurface videoRecordSurface;

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.activity_video_record;
    }

    @Override
    protected void iniInjector() {

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
        videoRecordSurface = new VideoRecordSurface(this);
        frameLayout.addView(videoRecordSurface);
        btnStart.setOnTouchListener(new View.OnTouchListener() {
            private float moveY;
            private float moveX;
            Rect rect = new Rect();
            boolean isInner = true;

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN: {
                        //按住事件发生后执行代码的区域
                        tvTips.setVisibility(View.VISIBLE);
                        videoRecordSurface.record(VideoRecordActivity.this);
                        videoProgressView.startProgress(videoRecordSurface.mRecordMaxTime);
                        break;
                    }
                    case MotionEvent.ACTION_MOVE: {
                        //移动事件发生后执行代码的区域
                        if (rect.right == 0 && rect.bottom == 0) {
                            btnStart.getFocusedRect(rect);
                        }
                        moveX = event.getX();
                        moveY = event.getY();
                        if (moveY > 0 && moveX > 0 && moveX <= rect.right && moveY <= rect.bottom) {
                            //内
                            isInner = true;
                            if (!"移开取消".equals(tvTips.getText().toString().trim())) {
                                tvTips.setBackgroundColor(Color.TRANSPARENT);
                                tvTips.setTextColor(getResources().getColor(R.color.video_green));
                                tvTips.setText("移开取消");
                            }
                            btnStart.setVisibility(View.INVISIBLE);
                        } else {
                            //外
                            isInner = false;
                            if (!"松开取消".equals(tvTips.getText().toString().trim())) {
                                tvTips.setBackgroundColor(Color.RED);//getResources().getColor(android.R.color.holo_red_dark));
                                tvTips.setTextColor(Color.WHITE);
                                tvTips.setText("松开取消");
                            }
                        }
                        break;
                    }
                    case MotionEvent.ACTION_UP: {
                        //松开事件发生后执行代码的区域
                        tvTips.setVisibility(View.INVISIBLE);
                        videoProgressView.stopProgress();
                        if (iTime <= videoRecordSurface.mRecordMiniTime || (iTime < videoRecordSurface.mRecordMaxTime && !isInner)) {
                            if(isInner){
                                Toast.makeText(VideoRecordActivity.this, "录制时间太短", Toast.LENGTH_SHORT).show();
                            }else{
                                //
                            }
                            videoRecordSurface.stopRecord();
                            videoRecordSurface.repCamera();
                            btnStart.setVisibility(View.VISIBLE);
                        } else {
                            videoRecordSurface.stop();
                            String recordThumbDir = videoRecordSurface.getRecordThumbDir();
                            String recordDir = videoRecordSurface.getRecordDir();
                            Intent intent = new Intent(VideoRecordActivity.this, PublishVideoActivity.class);
                            intent.putExtra(PublishVideoActivity.kVIDEO_THUMB, recordThumbDir);
                            intent.putExtra(PublishVideoActivity.kVIDEO_MP4, recordDir);
                            startActivity(intent);
                        }
                        break;
                    }
                    default:
                        break;
                }
                return false;
            }
        });
    }

    @Override
    protected View getLoadingTargetView() {
        return null;
    }

    @Override
    protected boolean isApplyStatusBarTranslucency() {
        return false;
    }

    @OnClick(R.id.videoRecorder_btn_play)
    public void onClickPlay() {
        if (videoRecordSurface.getRecordDir().isEmpty()) {
            Toast.makeText(this, "没有选中视频", Toast.LENGTH_SHORT).show();
            return;
        }
        Intent intent = new Intent(this, VideoPlayActivity.class);
        intent.putExtra(VideoPlayActivity.kPath, videoRecordSurface.getRecordDir());
        startActivity(intent);
        finish();
    }

    @OnClick(R.id.videoRecorder_tv_cancel)
    public void onClickCancel() {
        finish();
    }

    @Override
    protected void onStop() {
        super.onStop();
        videoRecordSurface.stopRecord();
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }


    @Override
    public void onRecordFinish() {
        if (videoProgressView != null) {
            videoProgressView.stopProgress();
        }
    }

    @Override
    public void onRecordProgress(int progress) {
        iTime = progress;
    }
}