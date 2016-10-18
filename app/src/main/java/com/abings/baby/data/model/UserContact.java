package com.abings.baby.data.model;

import java.io.Serializable;
import java.util.List;

public class UserContact implements Serializable {

    @Override
    public String toString() {
        return "UserContact{" +
                "nick_name='" + nick_name + '\'' +
                ", birthday='" + birthday + '\'' +
                ", sex='" + sex + '\'' +
                ", fk_grade_class_id='" + fk_grade_class_id + '\'' +
                ", name='" + name + '\'' +
                ", pk_baby_id='" + pk_baby_id + '\'' +
                ", fk_shool_id='" + fk_shool_id + '\'' +
                ", create_datetime='" + create_datetime + '\'' +
                ", photo='" + photo + '\'' +
                ", fk_master_user_id='" + fk_master_user_id + '\'' +
                ", images=" + images.toString() +
                '}';
    }

    /**
     * nick_name : 宝哥
     * birthday : 2010-01-10
     * sex : True
     * fk_grade_class_id : 2
     * name : 王小宝
     * pk_baby_id : 2
     * images : [{"relation_desc":"孩子他爸","mobile_phone":"18628023869","address":"尚未填写","email":"123@er.com",
     * "pk_user_id":"1","tel":"","name":"王大明","photo":"201602282144053877.jpg","open_tel":"True"}]
     * fk_shool_id : 2
     * create_datetime : 2016/1/1
     * photo : 201602282152516532.jpg
     * fk_master_user_id : 1
     */

    private String nick_name;
    private String birthday;
    private String sex;
    private String fk_grade_class_id;
    private String name;
    private String pk_baby_id;
    private String fk_shool_id;
    private String create_datetime;
    private String photo;
    private String fk_master_user_id;
    /**
     * relation_desc : 孩子他爸
     * mobile_phone : 18628023869
     * address : 尚未填写
     * email : 123@er.com
     * pk_user_id : 1
     * tel :
     * name : 王大明
     * photo : 201602282144053877.jpg
     * open_tel : True
     */

    private List<ImagesBean> images;

    public String getNick_name() {
        return nick_name;
    }

    public void setNick_name(String nick_name) {
        this.nick_name = nick_name;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
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

    public String getCreate_datetime() {
        return create_datetime;
    }

    public void setCreate_datetime(String create_datetime) {
        this.create_datetime = create_datetime;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getFk_master_user_id() {
        return fk_master_user_id;
    }

    public void setFk_master_user_id(String fk_master_user_id) {
        this.fk_master_user_id = fk_master_user_id;
    }

    public List<ImagesBean> getImages() {
        return images;
    }

    public void setImages(List<ImagesBean> images) {
        this.images = images;
    }

    public static class ImagesBean implements Serializable {
        private String relation_desc;
        private String mobile_phone;
        private String address;
        private String email;
        private String pk_user_id;
        private String tel;
        private String name;
        private String photo;
        private String open_tel;

        @Override
        public String toString() {
            return "ImagesBean{" +
                    "relation_desc='" + relation_desc + '\'' +
                    ", mobile_phone='" + mobile_phone + '\'' +
                    ", address='" + address + '\'' +
                    ", email='" + email + '\'' +
                    ", pk_user_id='" + pk_user_id + '\'' +
                    ", tel='" + tel + '\'' +
                    ", name='" + name + '\'' +
                    ", photo='" + photo + '\'' +
                    ", open_tel='" + open_tel + '\'' +
                    '}';
        }

        public String getRelation_desc() {
            return relation_desc;
        }

        public void setRelation_desc(String relation_desc) {
            this.relation_desc = relation_desc;
        }

        public String getMobile_phone() {
            return mobile_phone;
        }

        public void setMobile_phone(String mobile_phone) {
            this.mobile_phone = mobile_phone;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getPk_user_id() {
            return pk_user_id;
        }

        public void setPk_user_id(String pk_user_id) {
            this.pk_user_id = pk_user_id;
        }

        public String getTel() {
            return tel;
        }

        public void setTel(String tel) {
            this.tel = tel;
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

        public String getOpen_tel() {
            return open_tel;
        }

        public void setOpen_tel(String open_tel) {
            this.open_tel = open_tel;
        }
    }
}
