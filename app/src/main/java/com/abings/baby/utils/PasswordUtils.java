package com.abings.baby.utils;

import com.abings.baby.R;
import com.abings.baby.WineApplication;

import java.util.regex.Pattern;

/**
 * Created by zwj on 16/9/9.
 * 密码验证工具类
 */
public class PasswordUtils {

    /**
     * 密码长度>=8位，符合要求
     *
     * @param pwd
     * @return
     */
    public static boolean isPwdLengthRange(String pwd) {
        return pwd.length() >= 8;
    }

    /**
     * 1.判断数字或者字母为连续重复的，比如8个1或者8个a这样的
     * 2.判断数字或者字母为有序的输入，比如12345678或者abcdefgh
     *
     * @param pwd 密码
     * @return true:密码可用;false:密码不可用
     */
    public static boolean isPwdAvailable(String pwd) {
        if (pwd == null || pwd.isEmpty()) {
            return true;
        }
        String regular = WineApplication.getInstance().getResources().getString(R.string.pwdutlis_reg,String.valueOf(pwd.length() - 1));
        if (Pattern.matches(regular, pwd)) {
            //匹配重复字符
            return false;
        }
        if (isPasswordContinuous(pwd)) {
            //正序 || 反续
            return false;
        }
        return true;
    }


    /**
     * 密码是否是正序或反序连续
     *
     * @param pwd
     * @return true为连续，false为不连续。
     */
    private static boolean isPasswordContinuous(String pwd) {
        int count = 0;// 正序次数
        int reverseCount = 0;// 反序次数
        final int pwdLength = pwd.length();
        final int maxCount = pwdLength - 2;
        String[] strArr = pwd.split("");
        // 从1开始是因为划分数组时，第一个为空
        for (int i = 1; i < pwdLength - 1; i++) {
            if (isPositiveContinuous(strArr[i], strArr[i + 1])) {
                count++;
            } else {
                count = 0;
            }
            if (isReverseContinuous(strArr[i], strArr[i + 1])) {
                reverseCount++;
            } else {
                reverseCount = 0;
            }
            if (count > maxCount || reverseCount > maxCount)
                break;
        }
        if (count >= maxCount || reverseCount >= maxCount)
            return true;
        return false;
    }

    /**
     * 是否是正序连续
     *
     * @param str1
     * @param str2
     * @return
     */
    private static boolean isPositiveContinuous(String str1, String str2) {
        if (str2.hashCode() - str1.hashCode() == 1)
            return true;
        return false;
    }

    /**
     * 是否是反序连续
     *
     * @param str1
     * @param str2
     * @return
     */
    private static boolean isReverseContinuous(String str1, String str2) {
        if (str2.hashCode() - str1.hashCode() == -1)
            return true;
        return false;
    }
}
