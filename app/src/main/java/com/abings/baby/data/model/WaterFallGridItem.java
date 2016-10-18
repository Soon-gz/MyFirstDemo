package com.abings.baby.data.model;

import java.io.Serializable;

public class WaterFallGridItem implements Serializable {

    @Override
    public String toString() {
        return "WaterFallGridItem1{" +
                "pk_image_news_image_id='" + pk_image_news_image_id + '\'' +
                ", fk_image_news_id='" + fk_image_news_id + '\'' +
                ", image='" + image + '\'' +
                ", create_datetime='" + create_datetime + '\'' +
                ", fk_grade_class_id='" + fk_grade_class_id + '\'' +
                '}';
    }

    /**
     * pk_image_news_image_id : 4
     * fk_image_news_id : 7
     * image : 201602220040435367.jpg
     * create_datetime : 2016/2/22 00:40:43
     * fk_grade_class_id : 1
     */

    private String pk_image_news_image_id;
    private String fk_image_news_id;
    private String image = "";
    private String create_datetime;
    private String fk_grade_class_id;
    private String fk_user_type;
    private String fk_user_id;
    private String fk_teacher_id;
    private String video;
    private String type;

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

    public String getFk_user_type() {
        return fk_user_type;
    }

    public void setFk_user_type(String fk_user_type) {
        this.fk_user_type = fk_user_type;
    }

    public String getFk_user_id() {
        return fk_user_id;
    }

    public void setFk_user_id(String fk_user_id) {
        this.fk_user_id = fk_user_id;
    }

    public String getFk_teacher_id() {
        return fk_teacher_id;
    }

    public void setFk_teacher_id(String fk_teacher_id) {
        this.fk_teacher_id = fk_teacher_id;
    }

    public String getVideo() {
        return video;
    }

    public void setVideo(String video) {
        this.video = video;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getFk_grade_class_id() {
        return fk_grade_class_id;
    }

    public void setFk_grade_class_id(String fk_grade_class_id) {
        this.fk_grade_class_id = fk_grade_class_id;
    }
}
