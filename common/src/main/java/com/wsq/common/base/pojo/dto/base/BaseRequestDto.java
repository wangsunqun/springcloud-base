package com.wsq.common.base.pojo.dto.base;

import lombok.Getter;
import lombok.Setter;

import java.util.Locale;

@Setter
@Getter
public class BaseRequestDto {
    /**
     * 平台标识
     */
    private String appId;
    /**
     * 请求时间戳(精度到秒)
     */
    private String timestamp;
    /**
     * 接口版本号
     */
    private String version;
    private Long currentUserId;
    private Locale currentLocale;
}