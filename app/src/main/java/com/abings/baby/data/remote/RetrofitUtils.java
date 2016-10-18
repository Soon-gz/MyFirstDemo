package com.abings.baby.data.remote;

import android.util.Log;

import com.abings.baby.data.convert.JsonConverterFactory;
import com.squareup.okhttp.Interceptor;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import java.io.IOException;
import java.net.URL;

import okio.Buffer;
import retrofit.Retrofit;
import retrofit.RxJavaCallAdapterFactory;

/**
 * Created by Administrator on 2016/1/20.
 */
public class RetrofitUtils {
    public static final String Api_key = "c45a5ce7204b45f8bdbecc96255ad5aa";
    public static final String BASE_URL = "http://www.hellobaobei.com.cn:88/webservice/";//测试�?
    //    public static final String BASE_URL = "http://192.168.1.114/projectSchool/webservice/";//测试�?
    public static final String ChinaNew = "GuoNeiNews/Query"; //国内新闻

    public static final String BASE_IMG_URL = BASE_URL+"upload/msgImages/";//测试�?

    public static final String BASE_NEWS_SMALL_IMG_URL = BASE_URL+"upload/msgImages/small_";
    public static final String BASE_MESSAGE_IMG_URL = BASE_URL+"upload/messageImages/";

    public static final String BASE_USER_PHOTO_URL = BASE_URL+"upload/userPhotos/";
    public static final String BASE_BABY_PHOTO_URL = BASE_URL+"upload/babyPhotos/";
    public static final String BASE_TEACHER_PHOTO_URL = BASE_URL+"upload/teacherPhotos/";
    public static final String BASE_VIDEO_URL = BASE_URL+"upload/msgVideos/";

    private static Retrofit singleton;

    /**
     * @param clazz 自定�?
     * @param <T>
     * @return
     */
    public static <T> T createApi(Class<T> clazz, Interceptor interceptor, RxJavaCallAdapterFactory factory) {
        //if (singleton == null) {
        synchronized (RetrofitUtils.class) {
            if (singleton == null) {
                Retrofit.Builder builder = new Retrofit.Builder()
                        .baseUrl(BASE_URL)
                                //.addConverterFactory(FastJsonConverterFactory.create())
                        .addConverterFactory(JsonConverterFactory.create());
                // .addConverterFactory(FastJsonConvertFactory.create());
                if (interceptor != null) {
                    builder.client(createInterceptor(interceptor));
                }
                if (factory != null) {
                    builder.addCallAdapterFactory(factory);
                }
                singleton = builder.build();
            }
        }
        //}
        return singleton.create(clazz);
    }

    public static <T> APIService createApi() {
        return createApi(APIService.class, null, null);
    }

    public static <T> APIService createApi(Interceptor interceptor) {
        return createApi(APIService.class, interceptor, null);
    }

    public static <T> APIService createApi(RxJavaCallAdapterFactory factory) {
        return createApi(APIService.class, null, factory);
    }

    /**
     * 拦截�?
     *
     * @return
     */
    private static OkHttpClient createInterceptor(Interceptor interceptor) {
        OkHttpClient client = new OkHttpClient();
        client.networkInterceptors().add(interceptor);
        return client;
    }

    /**
     * 拦截�?
     *
     * @return
     */
    private static Interceptor createInterceptor1() {
//        OkHttpClient client = new OkHttpClient();
//        client.networkInterceptors().add(new Interceptor() {
//            @Override
//            public com.squareup.okhttp.Response intercept(Chain chain) throws IOException {
//                Request request = chain.request();
//                Logger.d("拦截�?--拦截到网络请求：" + request.urlString());
//                com.squareup.okhttp.Response response = chain.proceed(chain.request());//response读取�?��就close�?
//                Response copy = chain.proceed(chain.request());
//                Logger.json(copy.body().string());
//                return response;
//            }
//        });
//        return client;

        return new Interceptor() {
            @Override
            public com.squareup.okhttp.Response intercept(Chain chain) throws IOException {
                Request request = chain.request();
                //Log.d("拦截�?--拦截到网络请求：" + request.urlString());
                com.squareup.okhttp.Response response = chain.proceed(chain.request());//response读取�?��就close�?
                Response copy = chain.proceed(chain.request());
                // Log.json(copy.body().string());
                return response;
            }
        };
    }

    /**
     * 拦截�?
     * 过滤请求内容或添加内�?
     *
     * @return
     */
    private static Interceptor createInterceptor2() {
//        final String SGIN = "&token=1453134ddsgd5";
//        OkHttpClient client = new OkHttpClient();
//        client.interceptors().add(new Interceptor() {
//            @Override
//            public com.squareup.okhttp.Response intercept(Chain chain) throws IOException {
//                Request request = chain.request();
//                Request newRequest;
//                Request.Builder tempBuilder = request.newBuilder();
//                if (request.method().equals("GET")) {
//                    URL url = request.url();
//                    tempBuilder.url(url.toString() + SGIN);
//                } else {
//                    RequestBody body = request.body();
//                    Buffer sink = new Buffer();
//                    body.writeTo(sink);
//                    byte[] content = sink.write(SGIN.getBytes()).readByteArray();
//                    Logger.d(new String(content, "UTF-8"));
//                    RequestBody tempbody = RequestBody.create(MediaType.parse("application/x-www-form-urlencoded"), content);
//                    if (request.method().equals("POST")) {
//                        tempBuilder.post(tempbody);
//                    } else if (request.method().equals("PUT")) {
//                        tempBuilder.put(tempbody);
//                    }
//                }
//                newRequest = tempBuilder.build();
//                Logger.d("拦截�?--拦截到网络请求：" + newRequest.urlString());
//                return chain.proceed(newRequest);
//            }
//        });
//        return client;
        final String SGIN = "&token=1453134ddsgd5";
        return new Interceptor() {
            @Override
            public com.squareup.okhttp.Response intercept(Chain chain) throws IOException {
                Request request = chain.request();
                Request newRequest;
                Request.Builder tempBuilder = request.newBuilder();
                if (request.method().equals("GET")) {
                    URL url = request.url();
                    tempBuilder.url(url.toString() + SGIN);
                } else {
                    RequestBody body = request.body();
                    Buffer sink = new Buffer();
                    body.writeTo(sink);
                    byte[] content = sink.write(SGIN.getBytes()).readByteArray();
                    // Logger.d(new String(content, "UTF-8"));
                    RequestBody tempbody = RequestBody.create(MediaType.parse("application/x-www-form-urlencoded"), content);
                    if (request.method().equals("POST")) {
                        tempBuilder.post(tempbody);
                    } else if (request.method().equals("PUT")) {
                        tempBuilder.put(tempbody);
                    }
                }
                newRequest = tempBuilder.build();
                //Log.d("拦截�?--拦截到网络请求：" + newRequest.urlString());
                return chain.proceed(newRequest);
            }
        };
    }

    /**
     * 拦截�?
     * 使用�?��Interceptor来为请求添加Headers。要发�?请求到一个authenticated API
     *
     * @return
     */
    private static Interceptor createInterceptor3() {
        return new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request newRequest = chain.request();
                newRequest.newBuilder()
                        .addHeader("appid", "hello")
                        .addHeader("deviceplatform", "android")
                        .removeHeader("User-Agent")
                        .addHeader("User-Agent", "Mozilla/5.0 (X11; Ubuntu; Linux x86_64; rv:38.0) Gecko/20100101 Firefox/38.0");
                //.method(newRequest.method(), newRequest.body());
                Log.d("", "拦截�?--拦截到网络请求：" + newRequest.urlString());
                return chain.proceed(newRequest);
            }
        };
    }


}