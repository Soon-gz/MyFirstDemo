package com.abings.baby.data.model;

/**
 * Created by Sunflower on 2016/1/11.
 */
public class Response<T> {

    public String code;
    public String message;
    public T object;


    public boolean isSuccess() {
        return code.equals(1);
    }
}