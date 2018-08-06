package com.victor.cache;

import org.apache.commons.lang3.StringUtils;

/**
 * 
 * @Description: 缓存类型分类
 */
public enum CacheType
{
	BIZ("biz", "biz通用缓存"),

	PASSPORT("passport", "passport缓存");

	/**
	 * 业务描述
	 * 
	 */
	private String desc;

	/**
	 * 业务码
	 * 
	 */
	private String code;

	private CacheType(String code, String desc)
	{
		this.code = code;
		this.desc = desc;
	}

	public String getDesc()
	{
		return desc;
	}

	public String getCode()
	{
		return code;
	}

	/**
	 * 方法用途: 获取入参对应的枚举值<br>
	 * 实现步骤: <br>
	 * 
	 * @param param
	 * @return
	 */
	public static CacheType getEnum(String param)
	{
		if (StringUtils.isBlank(param))
		{
			return null;
		}
		for (CacheType each : values())
		{
			if (each.getCode().equalsIgnoreCase(param))
			{
				return each;
			}
		}
		throw new IllegalArgumentException("illegal cache type[" + param + "]");
	}

}