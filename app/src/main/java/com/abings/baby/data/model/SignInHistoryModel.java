package com.abings.baby.data.model;

/**
 * Created by HaomingXu on 2016/9/15.
 */
public class SignInHistoryModel {
    private String name;
    private String photo;
    private String school_name;
    private String class_name;
    private String user_name;
    private String relation_desc;
    private String mobile_photo;
    private String user_photo;
    private String pk_baby_id;
    private String pk_user_id;
    private String fk_school_id;
    private String fk_grade_class_id;
    private String fk_master_user_id;
    private String datetime;
    private boolean isIn;

    public String getDatetime() {
        return datetime;
    }

    public void setDatetime(String datetime) {
        this.datetime = datetime;
    }

    public boolean isIn() {
        return isIn;
    }

    public void setIsIn(boolean isIn) {
        this.isIn = isIn;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getSchool_name() {
        return school_name;
    }

    public void setSchool_name(String school_name) {
        this.school_name = school_name;
    }

    public String getClass_name() {
        return class_name;
    }

    public void setClass_name(String class_name) {
        this.class_name = class_name;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getRelation_desc() {
        return relation_desc;
    }

    public void setRelation_desc(String relation_desc) {
        this.relation_desc = relation_desc;
    }

    public String getMobile_photo() {
        return mobile_photo;
    }

    public void setMobile_photo(String mobile_photo) {
        this.mobile_photo = mobile_photo;
    }

    public String getUser_photo() {
        return user_photo;
    }

    public void setUser_photo(String user_photo) {
        this.user_photo = user_photo;
    }

    public String getPk_baby_id() {
        return pk_baby_id;
    }

    public void setPk_baby_id(String pk_baby_id) {
        this.pk_baby_id = pk_baby_id;
    }

    public String getPk_user_id() {
        return pk_user_id;
    }

    public void setPk_user_id(String pk_user_id) {
        this.pk_user_id = pk_user_id;
    }

    public String getFk_school_id() {
        return fk_school_id;
    }

    public void setFk_school_id(String fk_school_id) {
        this.fk_school_id = fk_school_id;
    }

    public String getFk_grade_class_id() {
        return fk_grade_class_id;
    }

    public void setFk_grade_class_id(String fk_grade_class_id) {
        this.fk_grade_class_id = fk_grade_class_id;
    }

    public String getFk_master_user_id() {
        return fk_master_user_id;
    }

    public void setFk_master_user_id(String fk_master_user_id) {
        this.fk_master_user_id = fk_master_user_id;
    }
}
