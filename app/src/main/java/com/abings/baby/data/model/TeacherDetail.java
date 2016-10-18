package com.abings.baby.data.model;

import java.io.Serializable;

public class TeacherDetail implements Serializable{

    /**
     * pk_teacher_id : 1
     * fk_school_id : 2
     * name : 苍老师
     * mobile_phone : 18628023869
     * identification_id :
     * sex : False
     * birthday :
     * email :
     * race_name :
     * photo : 201602282149353651.jpg
     */

    private String pk_teacher_id;
    private String fk_school_id;
    private String name;
    private String mobile_phone;
    private String identification_id;
    private String sex;
    private String birthday;
    private String email;
    private String race_name;
    private String photo;

    public String getPk_teacher_id() {
        return pk_teacher_id;
    }

    public void setPk_teacher_id(String pk_teacher_id) {
        this.pk_teacher_id = pk_teacher_id;
    }

    public String getFk_school_id() {
        return fk_school_id;
    }

    public void setFk_school_id(String fk_school_id) {
        this.fk_school_id = fk_school_id;
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

    public String getIdentification_id() {
        return identification_id;
    }

    public void setIdentification_id(String identification_id) {
        this.identification_id = identification_id;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }
    public String getSexName() {
        //True =男 ,False = 女
        if (sex.equalsIgnoreCase("True")) {
            return "男";
        } else if (sex.equalsIgnoreCase("False")) {
            return "女";
        } else {
            return "男";
        }
    }

    public void setSexName(String sexName) {
        if ("男".equals(sexName)) {
            this.sex = "True";
        } else if ("女".equals(sexName)) {
            this.sex = "False";
        } else {
            this.sex = "True";
        }
    }

    public int getSexNumber() {
        if (sex.equalsIgnoreCase("True")) {
            return 1;
        } else if (sex.equalsIgnoreCase("False")) {
            return 0;
        } else {
            return 1;
        }
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getRace_name() {
        return race_name;
    }

    public void setRace_name(String race_name) {
        this.race_name = race_name;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }
}
