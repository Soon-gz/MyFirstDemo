package com.abings.baby.data.model;

import java.io.Serializable;

/**
 * Created by Administrator on 2016/9/7.
 */
public class UnreadContact implements Serializable {

    /**
     * BABY_NAME : 湉湉
     * name : 夏晴
     * relation_desc : 爸爸
     * mobile_phone : 13276760777
     * BABY_PHOTO :
     * photo : 201605241559365816.jpeg
     */

    private String BABY_NAME;
    private String name;
    private String relation_desc;
    private String mobile_phone;
    private String BABY_PHOTO;
    private String photo;

    public String getBABY_NAME() {
        return BABY_NAME;
    }

    public void setBABY_NAME(String BABY_NAME) {
        this.BABY_NAME = BABY_NAME;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public String getBABY_PHOTO() {
        return BABY_PHOTO;
    }

    public void setBABY_PHOTO(String BABY_PHOTO) {
        this.BABY_PHOTO = BABY_PHOTO;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }
}
