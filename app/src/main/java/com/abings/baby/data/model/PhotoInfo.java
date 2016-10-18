package com.abings.baby.data.model;

import java.io.Serializable;

public class PhotoInfo implements Serializable {

    /**
     * pk_image_news_image_id : 2
     * fk_image_news_id : 6
     * image : 201602220031032465.jpg
     * create_datetime : 2016/2/22 00:31:04
     * content : !11
     * teacher_name :
     * user_name : 王大明
     * like_count : 0
     * fk_user_type : 1
     */

    private String pk_image_news_image_id;
    private String fk_image_news_id;
    private String image;
    private String create_datetime;
    private String content;
    private String teacher_name;
    private String user_name;
    private String like_count;
    private String fk_user_type;
    private String type;
    private String video;

    public String getPk_image_news_image_id() {
        return pk_image_news_image_id;
    }

    public void setPk_image_news_image_id(String pk_image_news_image_id) {
        this.pk_image_news_image_id = pk_image_news_image_id;
    }

    public String getFk_image_news_id() {
        return fk_image_news_id;
    }

    public void setFk_image_news_id(String fk_image_news_id) {
        this.fk_image_news_id = fk_image_news_id;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getCreate_datetime() {
        return create_datetime;
    }

    public void setCreate_datetime(String create_datetime) {
        this.create_datetime = create_datetime;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getTeacher_name() {
        return teacher_name;
    }

    public void setTeacher_name(String teacher_name) {
        this.teacher_name = teacher_name;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getLike_count() {
        return like_count;
    }

    public void setLike_count(String like_count) {
        this.like_count = like_count;
    }

    public String getFk_user_type() {
        return fk_user_type;
    }

    public void setFk_user_type(String fk_user_type) {
        this.fk_user_type = fk_user_type;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getVideo() {
        return video;
    }

    public void setVideo(String video) {
        this.video = video;
    }

    @Override
    public String toString() {
        return "PhotoInfo{" +
                "pk_image_news_image_id='" + pk_image_news_image_id + '\'' +
                ", fk_image_news_id='" + fk_image_news_id + '\'' +
                ", image='" + image + '\'' +
                ", create_datetime='" + create_datetime + '\'' +
                ", content='" + content + '\'' +
                ", teacher_name='" + teacher_name + '\'' +
                ", user_name='" + user_name + '\'' +
                ", like_count='" + like_count + '\'' +
                ", fk_user_type='" + fk_user_type + '\'' +
                '}';
    }
}
