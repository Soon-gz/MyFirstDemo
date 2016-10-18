package com.abings.baby.data.remote.custom;

import android.util.Log;

import org.json.JSONException;

import retrofit.HttpException;

/**
 * Created by zwj on 2016/8/3.
 * 错误异常处理
 */
public class ExceptionHandle {
    //对应HTTP的状态码
    private static final int UNAUTHORIZED = 401;
    private static final int FORBIDDEN = 403;
    private static final int NOT_FOUND = 404;
    private static final int REQUEST_TIMEOUT = 408;
    private static final int INTERNAL_SERVER_ERROR = 500;
    private static final int BAD_GATEWAY = 502;
    private static final int SERVICE_UNAVAILABLE = 503;
    private static final int GATEWAY_TIMEOUT = 504;

    private static final int baseErrorCode = 1000;

    enum ERROR {
        UNKNOWN("未知错误", baseErrorCode),
        PARSE_ERROR("解析错误", baseErrorCode + 1),
        Connect_ERROR("连接服务器失败", baseErrorCode + 2),
        Connect_TimeOut("连接服务器超时，请检查网络", baseErrorCode + 3),
        HTTP_ERROR("网络异常，请稍后再试", baseErrorCode + 4),
        MaxErrCode("", baseErrorCode + 5);


        public String mes;
        public int code;

        ERROR(String mes, int code) {
            this.mes = mes;
            this.code = code;
        }
    }


    public static ApiException handleException(Throwable e) {
        e.printStackTrace();
        Le("Exception Name : " + e.getClass().getSimpleName());
        String exceptionName = e.getClass().getSimpleName();
        ApiException ex;
        if (e instanceof HttpException) {
            //HTTP错误
            HttpException httpException = (HttpException) e;
            ex = new ApiException(httpException, ERROR.HTTP_ERROR.code);
            switch (httpException.code()) {
                case UNAUTHORIZED:
                case FORBIDDEN:
                case NOT_FOUND:
                case REQUEST_TIMEOUT:
                case GATEWAY_TIMEOUT:
                case INTERNAL_SERVER_ERROR:
                case BAD_GATEWAY:
                case SERVICE_UNAVAILABLE:
                default:
                    ex.errorMessage = ERROR.HTTP_ERROR.mes;  //均视为网络错误
                    break;
            }
            return ex;
        } else if (e instanceof ServerException) {
            Le("------------->ServerException");
            //服务器返回错误
            ServerException serverException = (ServerException) e;
            ex = new ApiException(serverException, serverException.errorCode);
            ex.errorMessage = serverException.errorMessage;
            return ex;
        } else if (e instanceof JSONException) {
            Le("------------->JSONException");
            //json解析错误
            ex = new ApiException(e, ERROR.PARSE_ERROR.code);
            ex.errorMessage = ERROR.PARSE_ERROR.mes;
            return ex;
        } else if ("ConnectException".equals(exceptionName)) {
            Le("------------->ConnectException");
            //链接服务器失败
            ex = new ApiException(e, ERROR.Connect_ERROR.code);
            ex.errorMessage = ERROR.Connect_ERROR.mes;
            return ex;
        } else if ("SocketTimeoutException".equals(exceptionName)) {
            Le("------------->SocketTimeoutException");
            ex = new ApiException(e, ERROR.Connect_TimeOut.code);
            ex.errorMessage = ERROR.Connect_TimeOut.mes;
            return ex;
        } else {
            ex = new ApiException(e, ERROR.UNKNOWN.code);
            ex.errorMessage = ERROR.UNKNOWN.mes;
            return ex;
        }
    }

    private static void Li(Object object) {
        Log.i("ExceptionHandle", object.toString());
    }

    private static void Le(Object object) {
        Log.e("ExceptionHandle", object.toString());
    }

}
