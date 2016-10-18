package com.abings.baby.data.model;

import java.io.Serializable;

public class ClassTeacherItem implements Serializable {
    //    {"state":"0","result":[{"name":"苍老师","mobile_phone":"18628023869"}]}
    private String name;//姓名
    private String mobile_phone;//手机号
    private String photo;//头像
    private String pk_teacher_id;

    public String getPk_teacher_id() {
        return pk_teacher_id;
    }

    public void setPk_teacher_id(String pk_teacher_id) {
        this.pk_teacher_id = pk_teacher_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMobile_phone() {
        return mobile_phone;
    }

    public void setMobile_phone(String mobile_phone) {
        this.mobile_phone = mobile_phone;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }
}
