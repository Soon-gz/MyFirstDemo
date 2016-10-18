package com.abings.baby.ui.waterfall.photoviewpagedetail.fragment;


import android.media.MediaPlayer;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.VideoView;

import com.abings.baby.Const;
import com.abings.baby.R;
import com.abings.baby.data.model.PhotoInfo;
import com.abings.baby.data.remote.RetrofitUtils;
import com.abings.baby.ui.base.BaseActivity;
import com.abings.baby.ui.base.BaseFragment;
import com.abings.baby.utils.ProgressDialogHelper;
import com.bumptech.glide.Glide;

import java.io.File;
import java.util.List;

import javax.inject.Inject;

import butterknife.Bind;
import de.greenrobot.event.EventBus;
import uk.co.senab.photoview.PhotoView;
import uk.co.senab.photoview.PhotoViewAttacher;

/**
 * A simple {@link Fragment} subclass.
 */
public class PhotoDatailFragment extends BaseFragment implements PhotoFragmentMvpView{


    @Inject
    PhotoFragmentPresenter presenter;

    @Bind(R.id.img)
    PhotoView img;
    @Bind(R.id.text_content)
    TextView textContent;
    @Bind(R.id.framelayout)
    FrameLayout frameLayout;
    @Bind(R.id.playVideo_videoView)
    VideoView videoView;


    private PhotoInfo mInfo;

    private String videopath;
    private String type;

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.fragment_photo_datail;
    }

    @Override
    protected void iniInjector() {
        ((BaseActivity) getActivity()).activityComponent().inject(this);
        presenter.attachView(this);
    }

    @Override
    protected void onFirstUserVisible() {

    }

    @Override
    protected View getLoadingTargetView() {
        return null;
    }


    public String getType(){
        return type;
    }

    public void setStop(){
        if (videoView!= null && videoView.isPlaying()){
            videoView.pause();
        }
    }

    public void setImg(PhotoInfo info){
        mInfo = info;
        if ("video".equals(info.getType())){
            type = "video";
            frameLayout.getChildAt(0).setVisibility(View.GONE);
            frameLayout.getChildAt(1).setVisibility(View.VISIBLE);
            videopath = RetrofitUtils.BASE_VIDEO_URL + info.getVideo();
            File file = new File(Const.videoPath + info.getVideo());
            if (file.exists()) {
                videoView.setVideoPath(file.getAbsolutePath());
                videoView.start();
                setLoop();
            } else {
                presenter.downloadVideo(videopath, Const.basePath + "video", info.getVideo());
            }
            frameLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    EventBus.getDefault().post(new EventClick("img"));
                }
            });
        }else{
            type = "image";
            frameLayout.getChildAt(0).setVisibility(View.VISIBLE);
            frameLayout.getChildAt(1).setVisibility(View.GONE);
            Glide.with(getActivity()).load(RetrofitUtils.BASE_IMG_URL + info.getImage()).error(R.drawable.a).into(img);
            textContent.setText(info.getContent());
            img.setOnPhotoTapListener(new PhotoViewAttacher.OnPhotoTapListener() {
                @Override
                public void onPhotoTap(View view, float x, float y) {
                    EventBus.getDefault().post(new EventClick("img"));
                }
            });
        }


    }

    @Override
    public void onPause() {
        super.onPause();
        videoView.pause();
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



    @Override
    public void palyVideo(String filepath) {
        File file = new File(filepath);
        videoView.setVideoPath(file.getAbsolutePath());
        videoView.start();
        setLoop();
    }

    @Override
    protected void initViewsAndEvents() {

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
    public void showLoadingProgress(final boolean show) {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (show){
                    ProgressDialogHelper.getInstance().showProgressDialog(getActivity(), "加载中...");
                }else{
                    ProgressDialogHelper.getInstance().hideProgressDialog();
                }
            }
        });
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
    public void onDestroy() {
        super.onDestroy();
        presenter.detachView();
    }
}
