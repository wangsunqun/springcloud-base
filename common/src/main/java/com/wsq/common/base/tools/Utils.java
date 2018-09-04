package com.wsq.common.base.tools;

import org.apache.tomcat.util.codec.binary.Base64;

import java.net.URL;
import java.net.URLDecoder;
import java.security.MessageDigest;

public class Utils {

    //MD5
    public static String MD5(String s) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] bytes = md.digest(s.getBytes("utf-8"));
            return toHex(bytes);
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static String toHex(byte[] bytes) {

        final char[] HEX_DIGITS = "0123456789ABCDEF".toCharArray();
        StringBuilder ret = new StringBuilder(bytes.length * 2);
        for (int i=0; i<bytes.length; i++) {
            ret.append(HEX_DIGITS[(bytes[i] >> 4) & 0x0f]);
            ret.append(HEX_DIGITS[bytes[i] & 0x0f]);
        }
        return ret.toString();
    }

    //URLDcoder
    public static String decodeHttp(String msg) throws Exception{
        msg = URLDecoder.decode(msg, "UTF-8");
        msg = new String(Base64.decodeBase64(msg), "UTF-8");
        return msg;
    }

    //base64
    public static String base64Encoder(String msg) {
        java.util.Base64.Encoder encoder = java.util.Base64.getEncoder();
        return encoder.encodeToString(msg.getBytes());
    }
}
