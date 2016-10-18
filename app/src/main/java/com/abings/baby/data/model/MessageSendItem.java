package com.abings.baby.data.model;

import java.io.Serializable;

public class MessageSendItem implements Serializable {

    /**
     * pk_message_id : 520
     * fk_school_id : 1
     * fk_send_user_id : 61
     * sender_name : 舒老师
     * sender_type : 1
     * fk_receive_user_id : 132
     * receiver_type : 2
     * subject : 测试
     * content : 我的得莫利
     * TOTAL_READED :
     * TOTAL_UNREADED : 11
     * create_datetime : 2016/9/5 17:36:04
     * checksum : 1473096964
     */

    private String pk_message_id;
    private String fk_school_id;
    private String fk_send_user_id;
    private String sender_name;
    private String sender_type;
    private String fk_receive_user_id;
    private String receiver_type;
    private String subject;
    private String content;
    private String TOTAL_READED;
    private String TOTAL_UNREADED;
    private String create_datetime;
    private String checksum;

    public String getIsSchool() {
        return isSchool;
    }

    public void setIsSchool(String isSchool) {
        this.isSchool = isSchool;
    }
//是发送给老师的就是1
    private String isSchool;

    public String getPk_message_id() {
        return pk_message_id;
    }

    public void setPk_message_id(String pk_message_id) {
        this.pk_message_id = pk_message_id;
    }

    public String getFk_school_id() {
        return fk_school_id;
    }

    public void setFk_school_id(String fk_school_id) {
        this.fk_school_id = fk_school_id;
    }

    public String getFk_send_user_id() {
        return fk_send_user_id;
    }

    public void setFk_send_user_id(String fk_send_user_id) {
        this.fk_send_user_id = fk_send_user_id;
    }

    public String getSender_name() {
        return sender_name;
    }

    public void setSender_name(String sender_name) {
        this.sender_name = sender_name;
    }

    public String getSender_type() {
        return sender_type;
    }

    public void setSender_type(String sender_type) {
        this.sender_type = sender_type;
    }

    public String getFk_receive_user_id() {
        return fk_receive_user_id;
    }

    public void setFk_receive_user_id(String fk_receive_user_id) {
        this.fk_receive_user_id = fk_receive_user_id;
    }

    public String getReceiver_type() {
        return receiver_type;
    }

    public void setReceiver_type(String receiver_type) {
        this.receiver_type = receiver_type;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getTOTAL_READED() {
        return TOTAL_READED;
    }

    public void setTOTAL_READED(String TOTAL_READED) {
        this.TOTAL_READED = TOTAL_READED;
    }

    public String getTOTAL_UNREADED() {
        return TOTAL_UNREADED;
    }

    public void setTOTAL_UNREADED(String TOTAL_UNREADED) {
        this.TOTAL_UNREADED = TOTAL_UNREADED;
    }

    public String getCreate_datetime() {
        return create_datetime;
    }

    public void setCreate_datetime(String create_datetime) {
        this.create_datetime = create_datetime;
    }

    public String getChecksum() {
        return checksum;
    }

    public void setChecksum(String checksum) {
        this.checksum = checksum;
    }

    @Override
    public String toString() {
        return "MessageSendItem{" +
                "pk_message_id='" + pk_message_id + '\'' +
                ", fk_school_id='" + fk_school_id + '\'' +
                ", fk_send_user_id='" + fk_send_user_id + '\'' +
                ", sender_name='" + sender_name + '\'' +
                ", sender_type='" + sender_type + '\'' +
                ", fk_receive_user_id='" + fk_receive_user_id + '\'' +
                ", receiver_type='" + receiver_type + '\'' +
                ", subject='" + subject + '\'' +
                ", content='" + content + '\'' +
                ", TOTAL_READED='" + TOTAL_READED + '\'' +
                ", TOTAL_UNREADED='" + TOTAL_UNREADED + '\'' +
                ", create_datetime='" + create_datetime + '\'' +
                ", checksum='" + checksum + '\'' +
                ", isSchool='" + isSchool + '\'' +
                '}';
    }
}
