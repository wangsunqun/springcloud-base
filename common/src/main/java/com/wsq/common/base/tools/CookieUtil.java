package com.wsq.common.base.tools;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

public class CookieUtil {
    //根据name获取cookie的值
    public static String getCookieByName(Cookie[] cookies, String name) {
        String value = "";

        for (Cookie cookie : cookies) {
            if (cookie.getName().equals(name)) {
                value = cookie.getValue();
            }
        }

        return value;
    }

    //设置cookie的值
    public static void setCookie(HttpServletResponse response, String key, String value, int timeout) {
        Cookie cookie = new Cookie(key, value);
        cookie.setMaxAge(timeout);
        cookie.setPath("/");

        response.addCookie(cookie);
    }
}
