package com.wsq.common.utils.cache;

import java.util.List;

/**
 * @Description: 缓存处理服务
 */
public interface CacheService {

    /**
     * 方法用途: 放入缓存<br>
     * 实现步骤: <br>
     *
     * @param key
     * @param value
     */
    public void add(String key, Object value, CacheType cacheType);

    /**
     * 方法用途: 放入缓存<br>
     * 实现步骤: <br>
     *
     * @param key
     * @param value
     * @param timeout   为正数时按照毫秒为单位传入，但是由于redis接口问题会被处理为秒计算，为负数时不设置
     * @param cacheType
     */
    public void add(String key, Object value, long timeout, CacheType cacheType);

    /**
     * 方法用途: 如果缓存不存在则添加<br>
     * 实现步骤: <br>
     *
     * @param key
     * @param value
     */
    public boolean addIfAbsent(String key, Object value, CacheType cacheType);

    /**
     * 方法用途: 如果缓存不存在则添加<br>
     * 实现步骤: <br>
     *
     * @param key
     * @param value
     * @param timeout
     */
    public boolean addIfAbsent(String key, Object value, long timeout, CacheType cacheType);

    /**
     * 方法用途: 查询缓存中key对应的value<br>
     * 实现步骤: <br>
     *
     * @param key
     * @return
     */
    public <T> T get(String key, CacheType cacheType);

    /**
     * 方法用途: 查询缓存中是否存在<br>
     * 实现步骤: <br>
     *
     * @param key
     * @return
     */
    public boolean exist(String key, CacheType cacheType);

    /**
     * 方法用途: 删除缓存中对应的值<br>
     * 实现步骤: <br>
     *
     * @param key
     */
    public void delete(CacheType cacheType, String... key);

    /**
     * 方法用途: 如果一个key对应的value是整形，对值进行累加。超时时间只有在第一次执行时才会设置<br>
     *
     * @param key
     * @param value
     * @param timeout   过期时间，单位秒
     * @param cacheType
     */
    public long incr(String key, long value, long timeout, CacheType cacheType);

    /**
     * 方法用途：返回存储在键中的列表的指定元素。偏移开始和停止是基于零的索引
     *
     * @param key
     * @param start
     * @param end
     * @return
     */
    public List<String> range(String key, long start, long end, CacheType cacheType);

    /**
     * 方法用途：修剪现有列表，使其只包含指定的指定范围的元素，起始和停止都是基于0的索引
     *
     * @param key
     * @param start
     * @param end
     */
    public void trim(String key, long start, long end, CacheType cacheType);

    /**
     * 方法用途：返回存储在键中的列表的长度。如果键不存在，则将其解释为空列表，并返回0。当key存储的值不是列表时返回错误。
     *
     * @param key
     * @param cacheType
     * @return
     */
    public Long size(String key, CacheType cacheType);

    /**
     * 方法用途：将所有指定的值插入存储在键的列表的头部。如果键不存在，则在执行推送操作之前将其创建为空列表。（从左边插入）
     *
     * @param key
     * @param value
     * @param cacheType
     * @return
     */
    public Long leftPush(String key, String value, long timeout, CacheType cacheType);

    /**
     * 方法用途：弹出最左边的元素，弹出之后该值在列表中将不复存在
     *
     * @param key
     * @param cacheType
     * @return
     */
    public Object leftPop(String key, CacheType cacheType);

    /**
     * 方法用途：在set中添加值，返回添加个数
     *
     * @param key
     * @param cacheType
     * @param value
     * @return
     */
    public Long add(String key, long timeout, CacheType cacheType, String... value);

    /**
     * 方法用途：移除集合中一个或多个成员
     *
     * @param key
     * @param cacheType
     * @param value
     * @return
     */
    public Long remove(String key, CacheType cacheType, String... value);

    /**
     * 方法用途：判断 member 元素是否是集合key的成员
     *
     * @param key
     * @param value
     * @param cacheType
     * @return
     */
    public Boolean isMember(String key, String value, CacheType cacheType);

    /**
     * 方法用途: 根据key设置超时时间<br>
     * 实现步骤: <br>
     *
     * @param key
     * @param timeout 单位秒
     * @return
     */
    public Boolean expire(String key, long timeout, CacheType cacheType);

    /**
     * 获取key的过期时间，单位秒
     *
     * @param key
     * @param cacheType
     * @return
     */
    public long ttl(String key, CacheType cacheType);

    /**
     * 方法用途：当且仅当 key 不存在，将 key 的值设为 value ，并返回true；若给定的 key 已经存在，则 SETNX 不做任何动作，并返回false。
     *
     * @param key
     * @param value
     * @param cacheType
     * @return
     */
    public Boolean setnx(String key, String value, long timeout, CacheType cacheType);

    /**
     * 风控计数
     * @param key
     * @param timeout
     * @param cacheType
     * @return
     */
    public long incrForLimit(String key, int timeout, CacheType cacheType);

}