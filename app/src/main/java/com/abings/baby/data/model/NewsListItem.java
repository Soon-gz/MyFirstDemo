package com.abings.baby.data.model;

import java.io.Serializable;
import java.util.List;

/**
 * 作者：黄斌 on 2016/2/27 20:16
 * 说明：
 */
public class NewsListItem implements Serializable {


    /**
     * content : 午餐一
     * images : [{"pk_image_news_image_id":"1","image":"a.jpg","fk_image_news_id":"2","create_datetime":"2016/2/17
     * 16:37:06"},{"pk_image_news_image_id":"6","image":"201603091304338903.jpg","fk_image_news_id":"2",
     * "create_datetime":"2016/3/9 13:04:33"},{"pk_image_news_image_id":"7","image":"201603091306057280.jpg",
     * "fk_image_news_id":"2","create_datetime":"2016/3/9 13:06:06"},{"pk_image_news_image_id":"8",
     * "image":"201603091309155777.jpg","fk_image_news_id":"2","create_datetime":"2016/3/9 13:09:16"},
     * {"pk_image_news_image_id":"9","image":"201603091650513424.jpg","fk_image_news_id":"2",
     * "create_datetime":"2016/3/9 16:50:52"},{"pk_image_news_image_id":"10","image":"201603091818203957.jpg",
     * "fk_image_news_id":"2","create_datetime":"2016/3/9 18:18:20"},{"pk_image_news_image_id":"11",
     * "image":"201603091936191250.jpg","fk_image_news_id":"2","create_datetime":"2016/3/9 19:36:19"},
     * {"pk_image_news_image_id":"12","image":"201603091941130038.jpg","fk_image_news_id":"2",
     * "create_datetime":"2016/3/9 19:41:13"},{"pk_image_news_image_id":"13","image":"201603091941418523.jpg",
     * "fk_image_news_id":"2","create_datetime":"2016/3/9 19:41:42"},{"pk_image_news_image_id":"14",
     * "image":"201603091942043455.jpg","fk_image_news_id":"2","create_datetime":"2016/3/9 19:42:05"},
     * {"pk_image_news_image_id":"15","image":"201603091944112306.jpg","fk_image_news_id":"2",
     * "create_datetime":"2016/3/9 19:44:12"},{"pk_image_news_image_id":"16","image":"201603092017011028.jpg",
     * "fk_image_news_id":"2","create_datetime":"2016/3/9 20:17:02"},{"pk_image_news_image_id":"17",
     * "image":"201603092028315535.jpg","fk_image_news_id":"2","create_datetime":"2016/3/9 20:28:31"},
     * {"pk_image_news_image_id":"18","image":"201603092047544624.jpg","fk_image_news_id":"2",
     * "create_datetime":"2016/3/9 20:47:54"},{"pk_image_news_image_id":"19","image":"201603092124318646.jpg",
     * "fk_image_news_id":"2","create_datetime":"2016/3/9 21:24:32"},{"pk_image_news_image_id":"20",
     * "image":"201603092152114962.jpg","fk_image_news_id":"2","create_datetime":"2016/3/9 21:52:12"},
     * {"pk_image_news_image_id":"21","image":"201603092154388584.jpg","fk_image_news_id":"2",
     * "create_datetime":"2016/3/9 21:54:39"}]
     * image_count : 17
     * create_datetime : 2016/2/17 15:39:33
     * like_count : 0
     * pk_image_news_id : 2
     */

    private String content;
    private String image_count;
    private String create_datetime;
    private String like_count;
    private String pk_image_news_id;
    /**
     * pk_image_news_image_id : 1
     * image : a.jpg
     * fk_image_news_id : 2
     * create_datetime : 2016/2/17 16:37:06
     */

    private List<ImagesBean> images;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getImage_count() {
        return image_count;
    }

    public void setImage_count(String image_count) {
        this.image_count = image_count;
    }

    public String getCreate_datetime() {
        return create_datetime;
    }

    public void setCreate_datetime(String create_datetime) {
        this.create_datetime = create_datetime;
    }

    public String getLike_count() {
        return like_count;
    }

    public void setLike_count(String like_count) {
        this.like_count = like_count;
    }

    public String getPk_image_news_id() {
        return pk_image_news_id;
    }

    public void setPk_image_news_id(String pk_image_news_id) {
        this.pk_image_news_id = pk_image_news_id;
    }

    public List<ImagesBean> getImages() {
        if (images != null){
            return images;
        }else{
            return null;
        }
    }

    public void setImages(List<ImagesBean> images) {
        this.images = images;
    }

    public static class ImagesBean implements Serializable {
        private String pk_image_news_image_id;
        private String image;
        private String fk_image_news_id;
        private String create_datetime;
        private String type;
        private String video;

        public String getPk_image_news_image_id() {
            return pk_image_news_image_id;
        }

        public void setPk_image_news_image_id(String pk_image_news_image_id) {
            this.pk_image_news_image_id = pk_image_news_image_id;
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

        public String getImage() {
            return image;
        }

        public void setImage(String image) {
            this.image = image;
        }

        public String getFk_image_news_id() {
            return fk_image_news_id;
        }

        public void setFk_image_news_id(String fk_image_news_id) {
            this.fk_image_news_id = fk_image_news_id;
        }

        public String getCreate_datetime() {
            return create_datetime;
        }

        public void setCreate_datetime(String create_datetime) {
            this.create_datetime = create_datetime;
        }
    }
}
