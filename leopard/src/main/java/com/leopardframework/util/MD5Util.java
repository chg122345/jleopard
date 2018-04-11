package com.leopardframework.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * MD5  加密
 *
 * Copyright (c) 2018, Chen_9g 陈刚 (80588183@qq.com).
 * <p>
 * DateTime 2018/3/29
 * <p>
 * A novice on the road, please give me a suggestion.
 * 众里寻他千百度，蓦然回首，那人却在，灯火阑珊处。
 * Find a way for success and not make excuses for failure.
 */
public class MD5Util {

    /**
     * @param str
     * @return
     * @Description:  32位小写MD5
     */
    public static String parseStrToMd5L32(String str){
        String reStr = null;
        try {
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            byte[] bytes = md5.digest(str.getBytes());
            StringBuffer stringBuffer = new StringBuffer();
            for (byte b : bytes){
                int bt = b&0xff;
                if (bt < 16){
                    stringBuffer.append(0);
                }
                stringBuffer.append(Integer.toHexString(bt));
            }
            reStr = stringBuffer.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return reStr;
    }

    /**
     * @Description: 32位大写MD5
     */
    public static String parseStrToMd5U32(String str){
        String reStr = parseStrToMd5L32(str);
        if (reStr != null){
            reStr = reStr.toUpperCase();
        }
        return reStr;
    }

    /**
     * @Description: 16位小写MD5
     */
    public static String parseStrToMd5U16(String str){
        String reStr = parseStrToMd5L32(str);
        if (reStr != null){
            reStr = reStr.toUpperCase().substring(8, 24);
        }
        return reStr;
    }

    /**
     * @Description: 16位大写MD5
     */
    public static String parseStrToMd5L16(String str){
        String reStr = parseStrToMd5L32(str);
        if (reStr != null){
            reStr = reStr.substring(8, 24);
        }
        return reStr;
    }
}
