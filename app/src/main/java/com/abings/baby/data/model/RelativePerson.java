package com.abings.baby.data.model;

import com.google.gson.Gson;

import java.io.Serializable;
import java.util.List;

/**
 * Created by HaomingXu on 2016/5/26.
 */
public class RelativePerson implements Serializable {

    /**
     * state : 0
     * result : [{"pk_user_id":"138","name":"张三","relation_desc":"爷爷奶奶","mobile_phone":"15764226682"}]
     */

    private String state;
    /**
     * pk_user_id : 138
     * name : 张三
     * relation_desc : 爷爷奶奶
     * mobile_phone : 15764226682
     */

    private List<ResultBean> result;

    public static RelativePerson objectFromData(String str) {

        return new Gson().fromJson(str, RelativePerson.class);
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public List<ResultBean> getResult() {
        return result;
    }

    public void setResult(List<ResultBean> result) {
        this.result = result;
    }

    public static class ResultBean implements Serializable {
        public static final String kName = "ResultBean";

        private String pk_user_id;
        private String name;
        private String relation_desc;
        private String mobile_phone;

        public static ResultBean objectFromData(String str) {

            return new Gson().fromJson(str, ResultBean.class);
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
    }
}
