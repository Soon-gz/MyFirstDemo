package com.abings.baby.data.model;

import java.io.Serializable;

public class MainListItem implements Serializable {
    private String content;
    private String fk_grade_class_id;
    private int count;
    private String fk_tag_id;
    public String typeName;
    public int imageRes;
    public boolean haveNew;

    public void Clear()
    {
        content="今日暂未添加";
        fk_grade_class_id="";
        count=0;
        haveNew=false;
    }
    public MainNewsItem getMainNewsItem() {
        return mainNewsItem;
    }

    public void setMainNewsItem(MainNewsItem mainNewsItem) {
        this.mainNewsItem = mainNewsItem;
    }

    public MainNewsItem mainNewsItem;

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public int getImageRes() {
        return imageRes;
    }

    public void setImageRes(int imageRes) {
        this.imageRes = imageRes;
    }

    public boolean isHaveNew() {
        return haveNew;
    }

    public void setHaveNew(boolean haveNew) {
        this.haveNew = haveNew;
    }

    public MainListItem(String typeName, String content, int imageRes) {
        this.typeName = typeName;
        this.imageRes = imageRes;
        this.content = content;
    }


    public MainListItem(String content, String fk_grade_class_id, int count, String fk_tag_id) {
        this.content = content;
        this.fk_grade_class_id = fk_grade_class_id;
        this.count = count;
        this.fk_tag_id = fk_tag_id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getFk_grade_class_id() {
        return fk_grade_class_id;
    }

    public void setFk_grade_class_id(String fk_grade_class_id) {
        this.fk_grade_class_id = fk_grade_class_id;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public String getFk_tag_id() {
        return fk_tag_id;
    }

    public void setFk_tag_id(String fk_tag_id) {
        this.fk_tag_id = fk_tag_id;
    }


}
