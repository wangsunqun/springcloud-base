package com.wsq.common.utils.cache;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.alibaba.fastjson.serializer.SerializerFeature;
import org.apache.log4j.Logger;


/**
 * @Description: json转换工具类
 */
public class JsonUtil {

    private static Logger logger = Logger.getLogger(JsonUtil.class);

    /**
     * 注意!!!, 泛型类如BaseResponseDto<T> 不要传 BaseResponseDto.class
     * 这会导致后面的如new TypeReference<BaseResponseDto<ContentDto>>解析全部不行
     *
     * @param json
     * @param clazz
     * @param <T>
     * @return
     */
    public static <T> T fromJson(String json, Class<T> clazz) {

        try {
            return JSON.parseObject(json, clazz);
        } catch (Exception e) {
            logger.warn(String.format("JsonParseException, caurse:%s", e.getMessage()));
        }
        return null;
    }


    /**
     * 注意!!!, 泛型类如BaseResponseDto<T> 不要传 BaseResponseDto.class
     * 这会导致后面的如new TypeReference<BaseResponseDto<ContentDto>>解析全部不行
     *
     * @param json
     * @param valueTypeRef
     * @param <T>
     * @return
     */
    public static <T> T fromJSON(String json, TypeReference<T> valueTypeRef) {
        try {
            return JSON.parseObject(json, valueTypeRef);
        } catch (Exception jpe) {
            logger.warn(String.format("JsonParseException, caurse:%s", jpe.getMessage()));
        }
        return null;
    }


    public static String toJson(Object object) {


        try {
            return JSON.toJSONString(object, SerializerFeature.WriteNullStringAsEmpty, SerializerFeature.WriteNullNumberAsZero, SerializerFeature.DisableCircularReferenceDetect);
        } catch (Exception jge) {
            logger.error("JSON error ", jge);
        }
        return null;
    }


}
