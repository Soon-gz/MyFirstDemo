package com.abings.baby.ui.aboutme;

import android.util.Log;

import com.abings.baby.data.convert.JsonConverterFactory;
import com.abings.baby.data.remote.RetrofitUtils;
import com.socks.library.KLog;

import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.RequestBody;

import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;

import retrofit.Call;
import retrofit.Callback;
import retrofit.GsonConverterFactory;
import retrofit.Response;
import retrofit.Retrofit;
import retrofit.http.Multipart;
import retrofit.http.POST;
import retrofit.http.Part;

/**
 * Created by lidong on 2016/1/28.
 */
public class FileUploadManager {

    private static final String ENDPOINT = RetrofitUtils.BASE_URL;

    public interface FileUploadService {
        /**
         * 上传一张图片
         *
         * @param token
         * @param imgs
         * @return
         */
        @Multipart
        @POST("Fupload_member_photo.asp")
        Call<JSONObject> uploadImage(@Part("token") RequestBody token,
                                     @Part("file\"; filename=\"image.jpg\"") RequestBody imgs);


        /**
         * 上传6张图片
         *
         * @param token
         * @param imgs1
         * @param imgs2
         * @param imgs3
         * @param imgs4
         * @param imgs5
         * @param imgs6
         * @return
         */
        @Multipart
        @POST("/upload")
        Call<String> uploadImage(@Part("token") String token,
                                 @Part("file\"; filename=\"image.jpg\"") RequestBody imgs1,
                                 @Part("file\"; filename=\"image.jpg\"") RequestBody imgs2,
                                 @Part("file\"; filename=\"image.jpg\"") RequestBody imgs3,
                                 @Part("file\"; filename=\"image.jpg\"") RequestBody imgs4,
                                 @Part("file\"; filename=\"image.jpg\"") RequestBody imgs5,
                                 @Part("file\"; filename=\"image.jpg\"") RequestBody imgs6);

    }

    private static final Retrofit sRetrofit = new Retrofit.Builder()
            .baseUrl(ENDPOINT)
            .addConverterFactory(JsonConverterFactory.create())
//            .addCallAdapterFactory(RxJavaCallAdapterFactory.create()) // 使用RxJava作为回调适配器
            .build();

    private static final FileUploadService apiManager = sRetrofit.create(FileUploadService.class);


    /**
     * 发说说
     *
     * @param paths
     * @param token
     */
    public static void upload(ArrayList<String> paths, String token) {
        RequestBody[] requestBody = new RequestBody[6];
        if (paths.size() > 0) {
            for (int i = 0; i < paths.size(); i++) {
                requestBody[i] =
                        RequestBody.create(MediaType.parse("multipart/form-data"), new File(paths.get(i)));
            }
        }
        Call<String> call = apiManager.uploadImage(token, requestBody[0], requestBody[1], requestBody[2], requestBody[3], requestBody[4], requestBody[5]);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Response<String> response, Retrofit retrofit) {
                Log.v("Upload", response.message());
                Log.v("Upload", "success");
            }

            @Override
            public void onFailure(Throwable t) {
                Log.e("Upload", t.toString());
            }
        });

    }


    /**
     * 发说说
     *
     * @param path
     * @param token
     */
    public static void upload(String path, String token) {

        KLog.e("path = " + path);
        RequestBody requestBody =
                RequestBody.create(MediaType.parse("multipart/form-data"), new File(path));

        RequestBody requestBody1 =
                RequestBody.create(MediaType.parse("text/plain; charset=utf-8"), token);
        Call<JSONObject> call = apiManager.uploadImage(requestBody1, requestBody);
        call.enqueue(new Callback<JSONObject>() {
                         @Override
                         public void onResponse(Response<JSONObject> response, Retrofit retrofit) {
                             KLog.e(response.body().toString());
                             KLog.e(response.message());
                             KLog.e("success");
                         }

                         @Override
                         public void onFailure(Throwable t) {
                             KLog.e(t.toString());
                         }
                     }

        );

    }
}
