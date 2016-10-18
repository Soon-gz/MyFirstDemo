package com.abings.baby.data.model;

import java.io.Serializable;

/**
 * Created by zwj on 16/9/14.
 * 签到获取到的数据格式
 */
public class Attend implements Serializable {


    /**
     * relation_desc : Mom
     * user_name : 舒文
     * mobile_phone : 15968602936
     * fk_grade_class_id : 4
     * school_name : 台州市椒江区奕村教育幼儿园
     * pk_user_id : 173
     * name : 雯雯
     * pk_baby_id : 137
     * user_photo : 201607291114357546.jpg
     * class_name : 智塾科技班
     * photo : 201607291334097604.jpg
     * fk_school_id : 1
     * fk_master_user_id : 173
     */

    private String relation_desc;
    private String user_name;
    private String mobile_phone;
    private String fk_grade_class_id;
    private String school_name;
    private String pk_user_id;
    private String name;
    private String pk_baby_id;
    private String user_photo;
    private String class_name;
    private String photo;
    private String fk_school_id;
    private String fk_master_user_id;

    public String getRelation_desc() {
        return relation_desc;
    }

    public void setRelation_desc(String relation_desc) {
        this.relation_desc = relation_desc;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getMobile_phone() {
        return mobile_phone;
    }

    public void setMobile_phone(String mobile_phone) {
        this.mobile_phone = mobile_phone;
    }

    public String getFk_grade_class_id() {
        return fk_grade_class_id;
    }

    public void setFk_grade_class_id(String fk_grade_class_id) {
        this.fk_grade_class_id = fk_grade_class_id;
    }

    public String getSchool_name() {
        return school_name;
    }

    public void setSchool_name(String school_name) {
        this.school_name = school_name;
    }

    public String getPk_user_id() {
        return pk_user_id;
    }

    public void setPk_user_id(String pk_user_id) {
        this.pk_user_id = pk_user_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPk_baby_id() {
        return pk_baby_id;
    }

    public void setPk_baby_id(String pk_baby_id) {
        this.pk_baby_id = pk_baby_id;
    }

    public String getUser_photo() {
        return user_photo;
    }

    public void setUser_photo(String user_photo) {
        this.user_photo = user_photo;
    }

    public String getClass_name() {
        return class_name;
    }

    public void setClass_name(String class_name) {
        this.class_name = class_name;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getFk_school_id() {
        return fk_school_id;
    }

    public void setFk_school_id(String fk_school_id) {
        this.fk_school_id = fk_school_id;
    }

    public String getFk_master_user_id() {
        return fk_master_user_id;
    }

    public void setFk_master_user_id(String fk_master_user_id) {
        this.fk_master_user_id = fk_master_user_id;
    }

    @Override
    public String toString() {
        return "Attend{" +
                "relation_desc='" + relation_desc + '\'' +
                ", user_name='" + user_name + '\'' +
                ", mobile_phone='" + mobile_phone + '\'' +
                ", fk_grade_class_id='" + fk_grade_class_id + '\'' +
                ", school_name='" + school_name + '\'' +
                ", pk_user_id='" + pk_user_id + '\'' +
                ", name='" + name + '\'' +
                ", pk_baby_id='" + pk_baby_id + '\'' +
                ", user_photo='" + user_photo + '\'' +
                ", class_name='" + class_name + '\'' +
                ", photo='" + photo + '\'' +
                ", fk_school_id='" + fk_school_id + '\'' +
                ", fk_master_user_id='" + fk_master_user_id + '\'' +
                '}';
    }
}
