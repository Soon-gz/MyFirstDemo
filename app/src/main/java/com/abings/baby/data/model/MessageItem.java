package com.abings.baby.data.model;

import java.io.Serializable;

public class MessageItem implements Serializable {

    /**
     * photo : 201607271327455527.jpg
     * pk_message_id : 11
     * fk_school_id : 1
     * fk_send_user_id : 1
     * sender_name : 蔡婷婷
     * sender_type : 1
     * receiver_type : 2
     * subject : 你好
     * content : 来昨晚
     * create_datetime : 2016/6/16 14:20:46
     * readed : True
     * checksum : 1466086846
     */

    private String photo;
    private String pk_message_id;
    private String fk_school_id;
    private String fk_send_user_id;
    private String sender_name;
    private String sender_type;
    private String receiver_type;
    private String subject;
    private String content;
    private String create_datetime;
    private String readed;
    private String checksum;

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

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

    public String getCreate_datetime() {
        return create_datetime;
    }

    public void setCreate_datetime(String create_datetime) {
        this.create_datetime = create_datetime;
    }

    public String getReaded() {
        return readed;
    }

    public void setReaded(String readed) {
        this.readed = readed;
    }

    public String getChecksum() {
        return checksum;
    }

    public void setChecksum(String checksum) {
        this.checksum = checksum;
    }

    @Override
    public String toString() {
        return "MessageItem{" +
                "photo='" + photo + '\'' +
                ", pk_message_id='" + pk_message_id + '\'' +
                ", fk_school_id='" + fk_school_id + '\'' +
                ", fk_send_user_id='" + fk_send_user_id + '\'' +
                ", sender_name='" + sender_name + '\'' +
                ", sender_type='" + sender_type + '\'' +
                ", receiver_type='" + receiver_type + '\'' +
                ", subject='" + subject + '\'' +
                ", content='" + content + '\'' +
                ", create_datetime='" + create_datetime + '\'' +
                ", readed='" + readed + '\'' +
                ", checksum='" + checksum + '\'' +
                '}';
    }
}
