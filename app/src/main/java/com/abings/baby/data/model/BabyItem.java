package com.abings.baby.data.model;

import java.io.Serializable;

/**
 * Created by Administrator on 2014-12-23.
 */
public class BabyItem implements Serializable {

    /**
     * "pk_baby_id": "3", 宝贝 ID
     "fk_shool_id": "2", 学校 ID
     "fk_master_user_id": "2",主账号 ID，若为空值，该账号为主账号
     "fk_user_id": "2", 家长账号 ID
     "fk_grade_class_id": "1", 班级 ID
     "name": "李小宝",
     "nick_name": "小宝",
     "sex": "False", True =男 ,False = 女
     "birthday": "2010-01-10",
     "create_datetime": "2016/2/14 16:30:29"
     */

    private String pk_baby_id;
    private String fk_shool_id;
    private String fk_master_user_id;
    private String pk_user_id;
    private String fk_grade_class_id;
    private String name;
    private String nick_name;
    private String sex;
    private String birthday;
    private String create_datetime;
    private String photo;

    public String getFk_user_id() {
        return pk_user_id;
    }

    public void setFk_user_id(String fk_user_id) {
        this.pk_user_id = fk_user_id;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getPk_baby_id() {
        return pk_baby_id;
    }

    public void setPk_baby_id(String pk_baby_id) {
        this.pk_baby_id = pk_baby_id;
    }

    public String getFk_shool_id() {
        return fk_shool_id;
    }

    public void setFk_shool_id(String fk_shool_id) {
        this.fk_shool_id = fk_shool_id;
    }

    public String getFk_master_user_id() {
        return fk_master_user_id;
    }

    public void setFk_master_user_id(String fk_master_user_id) {
        this.fk_master_user_id = fk_master_user_id;
    }

    public String getFk_grade_class_id() {
        return fk_grade_class_id;
    }

    public void setFk_grade_class_id(String fk_grade_class_id) {
        this.fk_grade_class_id = fk_grade_class_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNick_name() {
        return nick_name;
    }

    public void setNick_name(String nick_name) {
        this.nick_name = nick_name;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getCreate_datetime() {
        return create_datetime;
    }

    public void setCreate_datetime(String create_datetime) {
        this.create_datetime = create_datetime;
    }
}