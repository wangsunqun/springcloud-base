package com.wsq.common.utils.cache;

import org.apache.commons.lang3.StringUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * @Description: 以json序列化为基础，同时不需要外部传入类型的序列化方式
 */
public class SerializationUtil {

    public static String serialize(Object o) {
        if (o == null) {
            return null;
        }
        Map<String, String> map = new HashMap<String, String>();
        map.put("clazz", o.getClass().getName());
        map.put("obj", JsonUtil.toJson(o));
        return JsonUtil.toJson(map);
    }

    @SuppressWarnings("unchecked")
    public static <T> T deserialize(String source) throws ClassNotFoundException {
        if (StringUtils.isBlank(source)) {
            return null;
        }
        Map<String, String> map = JsonUtil.fromJson(source, Map.class);
        Class<?> clazz = Class.forName((String) map.get("clazz"));
        return (T) JsonUtil.fromJson(map.get("obj"), clazz);
    }

}
