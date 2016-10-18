package com.abings.baby.data.remote.custom;

/**
 * Created by zwj on 2016/8/3.
 * API报出的错误
 */
public class ApiException extends Exception {
    public int errorCode;
    public String errorMessage;


    /**
     * 服务定义错误
     */
    enum ERROR_SERVER {
        TOKEN("登录出现异常，请重新登录", 1),
        STATE2("没有数据，请稍后再试", 2),
        STATE3("出现未知异常，请稍后再试", 3),
        UNKNOWN("解析异常", 99);

        public String mes;
        public int code;

        ERROR_SERVER(String mes, int code) {
            this.mes = mes;
            this.code = code;
        }
    }

    public ApiException(Throwable throwable, int errorCode) {
        super(throwable);
        this.errorCode = errorCode;
    }

    public boolean isTokenError() {
        return this.errorCode == ERROR_SERVER.TOKEN.code;
    }

    public boolean isState2() {
        return this.errorCode == ERROR_SERVER.STATE2.code;
    }

    public boolean isState3() {
        return this.errorCode == ERROR_SERVER.STATE3.code;
    }

    public boolean isNetError() {
        return ((this.errorCode >= ExceptionHandle.ERROR.UNKNOWN.code) && (this.errorCode < ExceptionHandle.ERROR.MaxErrCode.code));
    }
}
