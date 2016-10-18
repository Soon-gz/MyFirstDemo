package com.abings.baby.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MD5Utils {

    /**
     * md5加密方法
     *
     * @param password 明文
     * @return 密文
     */
    public static String encode(String password) {
        // 信息摘要器
        MessageDigest digest;
        try {
            digest = MessageDigest.getInstance("md5");
            byte[] result = digest.digest(password.getBytes());
            StringBuffer buffer = new StringBuffer();
            for (byte b : result) {
                // 每个byte做与运算
                int number = b & 0xff;// 不按标准加密，密码学：加盐
                // 转换成16进制
                String numberStr = Integer.toHexString(number);
                if (numberStr.length() == 1) {
                    buffer.append("0");

                }
                buffer.append(numberStr);
            }
            // 就标准的md5加密的结果
            return buffer.toString();
        } catch (NoSuchAlgorithmException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return "";
        }

    }

}