package com.abings.baby.data.model;

/**
 * 作者：黄斌 on 2016/2/21 11:40
 * 说明：
 */
public class InfoTypes {
    public String typeName;
    public int imageRes;
    public String content;
    public boolean haveNew;

    public InfoTypes(String typeName, String content, int imageRes) {
        this.typeName = typeName;
        this.imageRes = imageRes;
        this.content = content;
    }
}
