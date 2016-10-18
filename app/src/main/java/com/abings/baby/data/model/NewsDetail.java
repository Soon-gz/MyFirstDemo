package com.abings.baby.data.model;

import java.io.Serializable;
import java.util.List;

/**
 * 作者：黄斌 on 2016/2/27 20:16
 * 说明：
 */
public class NewsDetail implements Serializable {

    /**
     * image_count : 1
     * pk_image_news_id : 2
     * content : 午餐一
     * create_datetime : 2016/2/17 15:39:33
     * like_count : 0
     * images : [{"pk_image_news_image_id":"1","fk_image_news_id":"2","image":"a.jpg","create_datetime":"2016/2/17 16:37:06"}]
     */

    private String image_count;
    private String pk_image_news_id;
    private String content;
    private String create_datetime;
    private String like_count;
    private String name;
    /**
     * pk_image_news_image_id : 1
     * fk_image_news_id : 2
     * image : a.jpg
     * create_datetime : 2016/2/17 16:37:06
     */

    private List<ImagesBean> images;

    public String getImage_count() {
        return image_count;
    }

    public void setImage_count(String image_count) {
        this.image_count = image_count;
    }

    public String getPk_image_news_id() {
        return pk_image_news_id;
    }

    public void setPk_image_news_id(String pk_image_news_id) {
        this.pk_image_news_id = pk_image_news_id;
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLike_count() {
        return like_count;
    }

    public void setLike_count(String like_count) {
        this.like_count = like_count;
    }

    public List<ImagesBean> getImages() {
        return images;
    }

    public void setImages(List<ImagesBean> images) {
        this.images = images;
    }

    public static class ImagesBean implements Serializable{
        private String pk_image_news_image_id;
        private String fk_image_news_id;
        private String image;
        private String create_datetime;
        private String type;
        private String video;

        @Override
        public String toString() {
            return "ImagesBean{" +
                    "pk_image_news_image_id='" + pk_image_news_image_id + '\'' +
                    ", fk_image_news_id='" + fk_image_news_id + '\'' +
                    ", image='" + image + '\'' +
                    ", create_datetime='" + create_datetime + '\'' +
                    '}';
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getVideo() {
            return video;
        }

        public void setVideo(String video) {
            this.video = video;
        }

        public String getPk_image_news_image_id() {
            return pk_image_news_image_id;
        }

        public void setPk_image_news_image_id(String pk_image_news_image_id) {
            this.pk_image_news_image_id = pk_image_news_image_id;
        }

        public String getFk_image_news_id() {
            return fk_image_news_id;
        }

        public void setFk_image_news_id(String fk_image_news_id) {
            this.fk_image_news_id = fk_image_news_id;
        }

        public String getImage() {
            return image;
        }

        public void setImage(String image) {
            this.image = image;
        }

        public String getCreate_datetime() {
            return create_datetime;
        }

        public void setCreate_datetime(String create_datetime) {
            this.create_datetime = create_datetime;
        }
    }
}
