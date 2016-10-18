package com.abings.baby.ui.publishvideo;

import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.abings.baby.Const;
import com.abings.baby.data.DataManager;
import com.abings.baby.ui.base.Presenter;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

/**
 * Created by HaomingXu on 2016/6/28.
 */
public class VideoPlayPrecenter implements Presenter<VideoPlayMVPView> {

    private final DataManager mDataManager;

    private VideoPlayMVPView mMvpMiew;

    private String fileNameobj;

    @Inject
    public VideoPlayPrecenter(DataManager mDataManager) {
        this.mDataManager = mDataManager;
    }

    @Override
    public void attachView(VideoPlayMVPView mvpView) {
        mMvpMiew = mvpView;
        if (mMvpMiew.bindEvents()) {
            mDataManager.getEventBus().register(this);
        }
    }




    Handler handler = new Handler(){
        public void handleMessage(Message msg){
            switch (msg.what){
                case 0:
                    mMvpMiew.palyVideo(Const.videoPath + fileNameobj);
                    mMvpMiew.showLoadingProgress(false);
                    break;
            }
        }
    };

    public void downloadVideo(String videopath, final String path, final String fileName) {
        fileNameobj = fileName;
        mMvpMiew.showLoadingProgress(true);
        OkHttpClient mOkHttpClient = new OkHttpClient();
        mOkHttpClient.setReadTimeout(3000, TimeUnit.MILLISECONDS);
        mOkHttpClient.setWriteTimeout(3000, TimeUnit.MILLISECONDS);
        mOkHttpClient.setConnectTimeout(3000, TimeUnit.MILLISECONDS);
        Request request = new Request.Builder().url(videopath).tag(this).build();
        mOkHttpClient.newCall(request).enqueue(new com.squareup.okhttp.Callback() {
            @Override
            public void onFailure(Request request, IOException e) {

            }

            @Override
            public void onResponse(Response response) throws IOException {
                if (response.isSuccessful()) {
                    InputStream is = response.body().byteStream();
                    byte[] buf = new byte[2048];
                    int len = 0;
                    FileOutputStream fos = null;
                    try {
                        is = response.body().byteStream();
                        final long total = response.body().contentLength();
                        long sum = 0;
                        File dir = new File(path);
                        if (!dir.exists()) {
                            dir.mkdirs();
                        }
                        File file = new File(dir, fileName);
                        fos = new FileOutputStream(file);
                        while ((len = is.read(buf)) != -1) {
                            sum += len;
                            fos.write(buf, 0, len);
                            final long finalSum = sum;
                        }
                        fos.flush();
                    } finally {
                        if (is != null) is.close();
                        if (fos != null) fos.close();
                        handler.sendEmptyMessage(0);
                    }

                } else {
                    Log.i("TAG00", "文件下载链接失败。。。");
                }
            }
        });

    }

    @Override
    public void detachView() {
        if (mMvpMiew.bindEvents()) {
            mDataManager.getEventBus().unregister(this);
        }
        mMvpMiew = null;
    }
}
