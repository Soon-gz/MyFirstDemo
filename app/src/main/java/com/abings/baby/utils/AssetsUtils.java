package com.abings.baby.utils;

import android.content.Context;

import org.apache.http.util.EncodingUtils;

import java.io.InputStream;

public class AssetsUtils {

    public static final String ENCODING = "UTF-8";

    public AssetsUtils() {
    }

    /**
     * 从Assents工程文件获取数据
     *
     * @return String
     */
    public static String getDataFromAssets(Context context, String s) {
        String s1;
        try {
            InputStream inputstream = context.getResources().getAssets().open(s);
            byte abyte0[] = new byte[inputstream.available()];
            inputstream.read(abyte0);
            s1 = EncodingUtils.getString(abyte0, "UTF-8");
        } catch (Exception exception) {
            exception.printStackTrace();
            return "";
        }
        return s1;
    }
}
