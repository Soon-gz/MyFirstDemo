package com.abings.baby.data.model;

import java.io.Serializable;

/**
 * Created by Administrator on 2014-12-23.
 */
public class Album implements Serializable {
    private static final long serialVersionUID = -3891074272976155946L;

    public long bucketid;
    public String bucketname;
    public int counter;

    public String path;
    public long updateTime = 0;
}