package com.yodoo.feikongbao.provisioning.util;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

public class Base64Util {
    /**
     * base64编码
     *
     * @param src 需要编码的源字符串
     * @return
     */
    public static String base64Encoder(String src) {
        if (src == null) {
            return null;
        }
        String des = "";
        try {
            des = new BASE64Encoder().encode(src.getBytes("utf-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return des;
    }

    /**
     * 解码base64字符串
     *
     * @param desc base64字符串
     * @return
     */
    public static String base64Decoder(String desc) {
        if (desc == null) {
            return null;
        }
        String src = "";
        try {
            byte[] bytes = new BASE64Decoder().decodeBuffer(desc);
            src = new String(bytes);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return src;
    }
}