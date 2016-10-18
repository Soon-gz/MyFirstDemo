package com.abings.baby.data.model;

import java.io.Serializable;

public class MainNewsItem implements Serializable {

    /**
     * content : 2016/2/23 11:47am 高学凯测试
     * checksum : 1456229362
     * create_datetime : 2016/2/23 12:09:23
     * fk_grade_class_id : 1
     * like_count : 0
     * fk_tag_id : 1
     * pk_image_news_id : 15
     */

    private String content;
    private String checksum;
    private String create_datetime;
    private String fk_grade_class_id;
    private String like_count;
    private String fk_tag_id;
    private String pk_image_news_id;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getChecksum() {
        return checksum;
    }

    public void setChecksum(String checksum) {
        this.checksum = checksum;
    }

    public String getCreate_datetime() {
        return create_datetime;
    }

    public void setCreate_datetime(String create_datetime) {
        this.create_datetime = create_datetime;
    }

    public String getFk_grade_class_id() {
        return fk_grade_class_id;
    }

    public void setFk_grade_class_id(String fk_grade_class_id) {
        this.fk_grade_class_id = fk_grade_class_id;
    }

    public String getLike_count() {
        return like_count;
    }

    public void setLike_count(String like_count) {
        this.like_count = like_count;
    }

    public String getFk_tag_id() {
        return fk_tag_id;
    }

    public void setFk_tag_id(String fk_tag_id) {
        this.fk_tag_id = fk_tag_id;
    }

    public String getPk_image_news_id() {
        return pk_image_news_id;
    }

    public void setPk_image_news_id(String pk_image_news_id) {
        this.pk_image_news_id = pk_image_news_id;
    }
}
