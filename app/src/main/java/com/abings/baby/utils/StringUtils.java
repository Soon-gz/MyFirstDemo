package com.abings.baby.utils;

import android.content.Context;
import android.text.TextUtils;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.regex.Pattern;

public class StringUtils {

    private StringUtils() {
    }

    public static String split(String params) {

        if (params != null && params.indexOf(".") != -1) {
            params = params.substring(0, params.indexOf(".") + 2);

        }

        return params;
    }

    public static String encodeStr(String s) {
        if (TextUtils.isEmpty(s)) return "";
        String s1;
        try {
            s1 = URLEncoder.encode(s, "UTF-8");
        } catch (UnsupportedEncodingException unsupportedencodingexception) {
            unsupportedencodingexception.printStackTrace();
            return s;
        }
        return s1;
    }

    public static boolean equalsNull(String s) {
        return isBlank(s) || s.equalsIgnoreCase("null");
    }

    public static String getSubStr(String s, int i, String s1) {
        int j = s.length();
        if (j >= i) {
            s = s.substring(j - i, j);
        } else {
            int k = j;
            while (k < i) {
                s = (new StringBuilder(String.valueOf(s))).append(s1).toString();
                k++;
            }
        }
        return s;
    }

    public static String getUUIDString(String s, String s1, String s2, int i, String s3) {
        return md5Helper((new StringBuilder(String.valueOf(getSubStr(s, i, s3)))).append(getSubStr(s1, i, s3)).append
                (getSubStr(s2, i, s3)).toString());
    }

    public static String replaceSpace(String str) {
        StringBuilder sb = new StringBuilder();
        char[] chars = str.toCharArray();
        for (char ch : chars) {
            if (' ' == ch) {
                sb.append("%20");
            } else {
                sb.append(ch);
            }
        }
        return sb.toString();
    }

    // �ж��ַ�
    public static boolean isBlank(String s) {
        if (s != null) {
            int i = s.length();
            if (i != 0) {
                int j = 0;
                while (j < i) {
                    if (!Character.isWhitespace(s.charAt(j))) {
                        return false;
                    }
                    j++;
                }
            }
        }
        return true;
    }

    public static boolean isEmpty(String as[]) {
        if (as != null) {
            int i = as.length;
            int j = 0;
            while (j < i) {
                String s = as[j];
                if (s != null && !s.isEmpty()) return false;
                j++;
            }
        }
        return true;
    }

    public static boolean isEmpty(String as) {
        if (as != null) {
            String s = as;
            if (s != null && !s.isEmpty()) return false;
        }
        return true;
    }

    public static boolean isDigit(String s) {
        return Pattern.compile("[0-9]{1,}").matcher(s).matches();
    }

    public static boolean isNumeric(String s) {
        if (s == null) return false;
        int i = s.length();
        int j = 0;
        while (true) {
            if (j >= i) return true;
            if (!Character.isDigit(s.charAt(j))) continue;
            j++;
        }
    }

    public static String md5Helper(String s) {
        byte abyte0[];
        StringBuffer stringbuffer;
        int i;
        int j;
        try {
            MessageDigest messagedigest = MessageDigest.getInstance("MD5");
            messagedigest.update(s.getBytes());
            abyte0 = messagedigest.digest();
            stringbuffer = new StringBuffer("");
        } catch (NoSuchAlgorithmException nosuchalgorithmexception) {
            throw new RuntimeException(nosuchalgorithmexception);
        }
        i = 0;
        while (true) {
            if (i >= abyte0.length) return stringbuffer.toString();
            j = abyte0[i];
            if (j < 0) j += 256;
            if (j < 16) {
                stringbuffer.append("0");
                stringbuffer.append(Integer.toHexString(j));
            }
            i++;
        }
    }

    public static String connString(Context context, int stringID, String text) {
        return (new StringBuilder(String.format(context.getResources().getString(stringID), new Object[]{String
                .valueOf(text)}))).toString();
    }
}
