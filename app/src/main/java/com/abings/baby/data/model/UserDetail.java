package com.abings.baby.data.model;

import java.io.Serializable;

public class UserDetail implements Serializable {

    /**
     * pk_user_id : 1
     * fk_master_user_id :
     * fk_school_id : 2
     * name : 王大明
     * mobile_phone : 18628023869
     * password : 123456
     * email : 123@er.com
     * relation_desc : 孩子他爸
     * birthday : 1997-01-01
     * identification_id :
     * tel :
     * address : 尚未填写
     * open_tel : True
     * photo : 201602282144053877.jpg
     * token : e72a44611e94f6939171fcd423998eeaa8c4d3e3
     * token_expire : 2016-03-13
     * enable : True
     * create_datetime :
     * last_app_os_ver : ios9.01
     * last_device_id : 1111111
     * first_login_datetime :
     * sms_code :
     */

    private String pk_user_id;
    private String fk_master_user_id;
    private String fk_school_id;
    private String name;
    private String sex;//True =男 ,False = 女
    private String mobile_phone;
    private String password;
    private String email;
    private String relation_desc;
    private String birthday;
    private String identification_id;
    private String tel;
    private String address;
    private String open_tel;
    private String photo;
    private String token;
    private String token_expire;
    private String enable;
    private String create_datetime;
    private String last_app_os_ver;
    private String last_device_id;
    private String first_login_datetime;
    private String sms_code;

    public String getPk_user_id() {
        return pk_user_id;
    }

    public void setPk_user_id(String pk_user_id) {
        this.pk_user_id = pk_user_id;
    }

    public String getFk_master_user_id() {
        return fk_master_user_id;
    }

    public void setFk_master_user_id(String fk_master_user_id) {
        this.fk_master_user_id = fk_master_user_id;
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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getRelation_desc() {
        return relation_desc;
    }

    public void setRelation_desc(String relation_desc) {
        this.relation_desc = relation_desc;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getIdentification_id() {
        return identification_id;
    }

    public void setIdentification_id(String identification_id) {
        this.identification_id = identification_id;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getOpen_tel() {
        return open_tel;
    }

    public void setOpen_tel(String open_tel) {
        this.open_tel = open_tel;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getToken_expire() {
        return token_expire;
    }

    public void setToken_expire(String token_expire) {
        this.token_expire = token_expire;
    }

    public String getEnable() {
        return enable;
    }

    public void setEnable(String enable) {
        this.enable = enable;
    }

    public String getCreate_datetime() {
        return create_datetime;
    }

    public void setCreate_datetime(String create_datetime) {
        this.create_datetime = create_datetime;
    }

    public String getLast_app_os_ver() {
        return last_app_os_ver;
    }

    public void setLast_app_os_ver(String last_app_os_ver) {
        this.last_app_os_ver = last_app_os_ver;
    }

    public String getLast_device_id() {
        return last_device_id;
    }

    public void setLast_device_id(String last_device_id) {
        this.last_device_id = last_device_id;
    }

    public String getFirst_login_datetime() {
        return first_login_datetime;
    }

    public void setFirst_login_datetime(String first_login_datetime) {
        this.first_login_datetime = first_login_datetime;
    }

    public String getSms_code() {
        return sms_code;
    }

    public void setSms_code(String sms_code) {
        this.sms_code = sms_code;
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
}
