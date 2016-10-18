package com.abings.baby.data.remote.custom;

/**
 * Created by zwj on 2016/8/3.
 * 服务器错信息
 */
public class ServerException extends RuntimeException {
    public int errorCode;
    public String errorMessage;
}
