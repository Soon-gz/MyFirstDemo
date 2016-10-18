package com.abings.baby.data.local;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;

import com.abings.baby.R;
import com.abings.baby.data.model.Album;
import com.abings.baby.data.model.Photos;
import com.abings.baby.injection.ApplicationContext;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.inject.Singleton;

import rx.Observable;
import rx.Subscriber;

/**
 * Created by Administrator on 2016/2/1.
 */
@Singleton
public class PhotoUpAlbumHelper {
    final String TAG = getClass().getSimpleName();
    Context context;
    ContentResolver cr;
    private String pathDir = "";
    private Album a;
    // 缩略图列表
    private List<Album> albumlist = new ArrayList<>();


    HashMap<String, String> thumbnailList = new HashMap<String, String>();
    // 专辑列表
    List<HashMap<String, String>> albumList = new ArrayList<HashMap<String, String>>();
    HashMap<String, Album> bucketList = new HashMap<String, Album>();

    @Inject
    public PhotoUpAlbumHelper(@ApplicationContext Context context) {
        cr = context.getContentResolver();
        this.context = context;
    }

    /**
     * 得到缩略图，这里主要得到的是图片的ID值
     */
    private void getThumbnail() {
/*        String[] projection = { MediaStore.Images.Thumbnails._ID, MediaStore.Images.Thumbnails.IMAGE_ID,
                MediaStore.Images.Thumbnails.DATA};
        Cursor cursor1 = MediaStore.Images.Thumbnails.queryMiniThumbnails(cr, MediaStore.Images.Thumbnails.EXTERNAL_CONTENT_URI,
                MediaStore.Images.Thumbnails.MINI_KIND, projection);*/

        final String orderBy = MediaStore.Images.Media.BUCKET_ID;
        String[] projection = new String[]{
                MediaStore.Images.Media.BUCKET_DISPLAY_NAME,
                MediaStore.Images.Media.BUCKET_ID};

        Cursor cursor1 = cr.query(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI, projection,
                null, null, orderBy);

        getThumbnailColumnData(cursor1);
        cursor1.close();
    }

    /**
     * 从数据库中得到缩略图
     * @param cur
     */
    private void getThumbnailColumnData(Cursor cur) {
        if (cur.moveToFirst()) {
            int image_id;
            String image_path;
/*            int image_idColumn = cur.getColumnIndex(MediaStore.Images.Thumbnails.IMAGE_ID);
            int dataColumn = cur.getColumnIndex(MediaStore.Images.Thumbnails.DATA);*/

            int image_idColumn = cur
                    .getColumnIndex(MediaStore.Images.Media.BUCKET_DISPLAY_NAME);
            int dataColumn = cur
                    .getColumnIndex(MediaStore.Images.Media.BUCKET_ID);
            do {
                image_id = cur.getInt(image_idColumn);
                image_path = cur.getString(dataColumn);
                thumbnailList.put("" + image_id, image_path);
            } while (cur.moveToNext());
        }
    }

    /**
     * 是否创建了图片集
     */
    boolean hasBuildImagesBucketList = false;

    /**
     * 得到图片集
     */
    private List<Album> buildImagesBucketList() {
        Album tempAlbum = null;
        final String orderBy = MediaStore.Images.Media.BUCKET_ID;
        String[] projection = new String[]{
                MediaStore.Images.Media.BUCKET_DISPLAY_NAME,
                MediaStore.Images.Media.BUCKET_ID,MediaStore.Images.Media.DATE_TAKEN};

        Cursor imagecursor = cr.query(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI, projection,
                null, null, orderBy);

        long previousid = 0;

        int bucketColumn = imagecursor
                .getColumnIndex(MediaStore.Images.Media.BUCKET_DISPLAY_NAME);

        int bucketcolumnid = imagecursor
                .getColumnIndex(MediaStore.Images.Media.BUCKET_ID);

        albumlist = new ArrayList<Album>();
        //总的相册
        Album totalAlbum = new Album();
        totalAlbum.bucketid = 0;
        totalAlbum.bucketname = context.getString(R.string.str_all_view);
        totalAlbum.counter = 0;
        albumlist.add(totalAlbum);
        int totalCounter = 0;
        long totalAlbumMaxDate = 0;
        while (imagecursor.moveToNext()) {
            totalCounter++;
            long bucketid = imagecursor.getInt(bucketcolumnid);
            long addedTime = Long.parseLong(imagecursor.getString(imagecursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATE_TAKEN)));
            if (addedTime > totalAlbumMaxDate) {
                totalAlbumMaxDate = addedTime;
            }
            if (previousid != bucketid) {
                Album album = new Album();
                album.bucketid = bucketid;
                album.bucketname = imagecursor.getString(bucketColumn);
                album.counter++;
                album.updateTime = addedTime;
                albumlist.add(album);
             //   Log.e("ab",album.updateTime+"相册->"+album.bucketname+"-->"+album.bucketid);
                previousid = bucketid;
            } else {
                if (albumlist.size() > 0)
                     tempAlbum =(Album)  albumlist.get(albumlist.size() - 1);
                     tempAlbum.counter++;
                     if (addedTime > tempAlbum.updateTime) {
                         tempAlbum.updateTime = addedTime;
                     }

            }
            if (imagecursor.isLast()) {
                albumlist.get(0).counter = totalCounter;
                albumlist.get(0).updateTime = totalAlbumMaxDate;
            }
        }
        imagecursor.close();

        if (totalCounter == 0) {
            albumlist.clear();
        } else {
            //获取路径
            for (int i = 0; i < albumlist.size(); i++) {
                Album album = albumlist.get(i);

                album.path = getAllMediaThumbnailsPath(album.bucketid);
              //  Log.e("ab",album.updateTime+"相册"+album.counter+"path->"+album.path);
            }
        }

        return albumlist;
/*        // 构造缩略图索引
        getThumbnail();
        // 构造相册索引
        String columns[] = new String[] { MediaStore.Images.Media._ID, MediaStore.Images.Media.BUCKET_ID,
                MediaStore.Images.Media.PICASA_ID, MediaStore.Images.Media.DATA, MediaStore.Images.Media.DISPLAY_NAME, MediaStore.Images.Media.TITLE,
                MediaStore.Images.Media.SIZE, MediaStore.Images.Media.BUCKET_DISPLAY_NAME };
        // 得到一个游标
        Cursor cur = cr.query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, columns, null, null,
                MediaStore.Images.Media.DATE_MODIFIED+" desc");
        if (cur.moveToFirst()) {
            // 获取指定列的索引
            int photoIDIndex = cur.getColumnIndexOrThrow(MediaStore.Images.Media._ID);
            int photoPathIndex = cur.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            int bucketDisplayNameIndex = cur.getColumnIndexOrThrow(MediaStore.Images.Media.BUCKET_DISPLAY_NAME);
            int bucketIdIndex = cur.getColumnIndexOrThrow(MediaStore.Images.Media.BUCKET_ID);
            *//**
             * Description:这里增加了一个判断：判断照片的名
             * 字是否合法，例如.jpg .png等没有名字的格式
             * 如果图片名字是不合法的，直接过滤掉
             *//*
            do {
                if (cur.getString(photoPathIndex).substring(
                        cur.getString(photoPathIndex).lastIndexOf("/")+1,
                        cur.getString(photoPathIndex).lastIndexOf("."))
                        .replaceAll(" ", "").length()<=0)
                {
                    Log.d(TAG, "出现了异常图片的地址：cur.getString(photoPathIndex)=" + cur.getString(photoPathIndex));
                }else {
                    String _id = cur.getString(photoIDIndex);
                    String path = cur.getString(photoPathIndex);
                    String bucketName = cur.getString(bucketDisplayNameIndex);
                    String bucketId = cur.getString(bucketIdIndex);
                    Album bucket = bucketList.get(bucketId);
                    //这里完成图片归并到响应的相册里去
                    if (bucket == null) {
                        bucket = new Album();
                        bucketList.put(bucketId, bucket);
                       // bucket.imageList = new ArrayList<Album>();
                        bucket.bucketname = bucketName;
                    }
                    bucket.counter++;
*//*                    PhotoUpImageItem imageItem = new PhotoUpImageItem();
                    imageItem.setImageId(_id);
                    imageItem.setImagePath(path);
                    bucket.imageList.add(imageItem);*//*
                }
            } while (cur.moveToNext());
        }
        cur.close();
        hasBuildImagesBucketList = true;*/
    }

    private List<Photos> getPhotosList(long id) {
        List<Photos> photos = new ArrayList<>();
        String path = "";
        String selection = MediaStore.Images.Media.BUCKET_ID + " = ?";
        String bucketid = String.valueOf(id);
        String sort = MediaStore.Images.Media._ID + " DESC";
        String[] selectionArgs = {bucketid};

        Uri images = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
        Cursor c;
        if (!bucketid.equals("0")) {
            c = cr.query(images, null,
                    selection, selectionArgs, sort);
        } else {
            c = cr.query(images, null,
                    null, null, sort);
        }


        if (c != null) {

            c.moveToFirst();

            setPathDir(c.getString(c.getColumnIndex(MediaStore.Images.Media.DATA)), c.getString(c.getColumnIndex(MediaStore.Images.Media.DISPLAY_NAME)));
            int position = 0;
            while (true) {
                path = c.getString(c.getColumnIndex(MediaStore.Images.Thumbnails.DATA));
               // Log.e("abings","图片地址:"+path);
                photos.add(new Photos(path));
                if (c.isLast()) {
                   // imageBeans[position] = new ImageBean(-1, path);
                    c.close();
                    break;
                } else {
                   // imageBeans[position++] = new ImageBean(-1, path);
                    c.moveToNext();
                }
            }
        }

        return photos;
    };

    private void setPathDir(String path, String fileName) {
        pathDir = path.replace("/" + fileName, "");
    }

    private String getPathDir() {

        if (pathDir.equals("") || a.bucketid == 0)
            pathDir = Environment.getExternalStoragePublicDirectory(
                    Environment.DIRECTORY_PICTURES).getAbsolutePath();
        return pathDir;
    }

    private String getAllMediaThumbnailsPath(long id) {
        String path = "";
        String selection = MediaStore.Images.Media.BUCKET_ID + " = ?";
        String bucketid = String.valueOf(id);
        String sort = MediaStore.Images.Thumbnails._ID + " DESC";
        String[] selectionArgs = {bucketid};

        Uri images = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
        Cursor c;
        if (!bucketid.equals("0")) {
            c = cr.query(images, null,
                    selection, selectionArgs, sort);
        } else {
            c = cr.query(images, null,
                    null, null, sort);
        }


        if (c.moveToNext()) {
            selection = MediaStore.Images.Media._ID + " = ?";
            String photoID = c.getString(c.getColumnIndex(MediaStore.Images.Media._ID));
            selectionArgs = new String[]{photoID};

            images = MediaStore.Images.Thumbnails.EXTERNAL_CONTENT_URI;
            Cursor cursor = cr.query(images, null,
                    selection, selectionArgs, sort);
            if (cursor != null && cursor.moveToNext()) {
                path = c.getString(c.getColumnIndex(MediaStore.Images.Media.DATA));
            } else
                path = c.getString(c.getColumnIndex(MediaStore.Images.Media.DATA));
            cursor.close();
        } else {
            Log.e("id", "from else");
        }

        c.close();
        return path;
    }

    /**
     * 得到图片集
     * @param refresh
     * @return
     */
    private List<Album> getImagesBucketList(boolean refresh) {
        if (refresh || (!refresh && !hasBuildImagesBucketList)) {
            buildImagesBucketList();
        }
        List<Album> tmpList = new ArrayList<Album>();
        Iterator<Map.Entry<String, Album>> itr = bucketList.entrySet().iterator();
        //将Hash转化为List
        while (itr.hasNext()) {
            Map.Entry<String, Album> entry = (Map.Entry<String, Album>) itr
                    .next();
            tmpList.add(entry.getValue());
        }
        return tmpList;
    }


    public Observable<List<Album>> getAlbmsObservable() {
        return Observable.create(new Observable.OnSubscribe<List<Album>>() {
            @Override
            public void call(Subscriber<? super List<Album>> subscriber) {
                List<Album> venues = buildImagesBucketList();
                if (venues != null) {
                    subscriber.onNext(venues);
                }
                subscriber.onCompleted();
            }
        });
    }

    public Observable<List<Photos>> getPhtosObservable(final long id) {
        return Observable.create(new Observable.OnSubscribe<List<Photos>>() {
            @Override
            public void call(Subscriber<? super List<Photos>> subscriber) {
                List<Photos> venues = getPhotosList(id);
                if (venues != null) {
                    subscriber.onNext(venues);
                }
                subscriber.onCompleted();
            }
        });
    }
}
