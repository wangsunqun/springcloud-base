package com.wsq.common.utils.cache;

import org.apache.log4j.Logger;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @Description: 使用redis作为缓存的实现方式
 */
@Component
public class RedisCacheServiceImpl implements CacheService {

    protected Logger log = Logger.getLogger(getClass());

    @Resource(name = "redisTemplate1")
    private RedisTemplate<String, Object> redisTemplate1;

    @Resource(name = "incrLuaScript")
    private DefaultRedisScript defaultRedisScript;

    private RedisTemplate getredisTemplate(CacheType cacheType) {
        switch (cacheType) {
            case PASSPORT:
                return redisTemplate1;
            default:
                throw new IllegalArgumentException("没有找到对应的cacheType。 cacheType = " + cacheType);
        }
    }

    @Override
    public void add(String key, Object value, CacheType cacheType) {
        add(key, value, -1, cacheType);
    }

    /**
     * 方法用途: 往缓存放入值<br>
     * 实现步骤: <br>
     *
     * @param key
     * @param value
     * @param timeout 为正数时按照秒为单位传入,为负数时不设置
     */
    @SuppressWarnings("unchecked")
    @Override
    public void add(String key, Object value, long timeout, CacheType cacheType) {
        RedisTemplate redisTemplate = getredisTemplate(cacheType);
        if (timeout > 0) {
            redisTemplate.opsForValue().set(key, SerializationUtil.serialize(value), timeout, TimeUnit.SECONDS);
        } else {
            redisTemplate.opsForValue().set(key, SerializationUtil.serialize(value));
        }
    }

    @Override
    public boolean addIfAbsent(String key, Object value, CacheType cacheType) {
        return addIfAbsent(key, value, -1, cacheType);
    }

    @Override
    public boolean addIfAbsent(String key, Object value, long timeout, CacheType cacheType) {
        RedisTemplate redisTemplate = getredisTemplate(cacheType);
        boolean result = redisTemplate.opsForValue().setIfAbsent(key, SerializationUtil.serialize(value));
        if (timeout > 0) {
            expire(key, timeout, cacheType);
        }
        return result;
    }

    @Override
    public <T> T get(String key, CacheType cacheType) {
        RedisTemplate redisTemplate = getredisTemplate(cacheType);
        try {
            Object result = redisTemplate.opsForValue().get(key);
            if (result instanceof String) {
                return SerializationUtil.deserialize((String) result);
            } else {
                return (T) result;
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.error("deserialize failed, key:" + key, e);
            return null;
        }
    }

    @Override
    public boolean exist(String key, CacheType cacheType) {
        RedisTemplate redisTemplate = getredisTemplate(cacheType);
        return redisTemplate.hasKey(key);
    }

    @Override
    public void delete(CacheType cacheType, String... key) {
        RedisTemplate redisTemplate = getredisTemplate(cacheType);
        if (key != null && key.length > 0) {
            if (key.length == 1) {
                redisTemplate.delete(key[0]);
            } else {
                redisTemplate.delete(CollectionUtils.arrayToList(key));
            }
        }
    }

    @Override
    public long incr(String key, long value, long timeout, CacheType cacheType) {
        RedisTemplate redisTemplate = getredisTemplate(cacheType);
        if (timeout > 0) {
            expire(key, timeout, cacheType);
        }
        return redisTemplate.opsForValue().increment(key, value);
    }

    @Override
    public List<String> range(String key, long start, long end, CacheType cacheType) {
        RedisTemplate redisTemplate = getredisTemplate(cacheType);
        return redisTemplate.opsForList().range(key, start, end);
    }

    @Override
    public void trim(String key, long start, long end, CacheType cacheType) {
        RedisTemplate redisTemplate = getredisTemplate(cacheType);
        redisTemplate.opsForList().trim(key, start, end);
    }

    @Override
    public Long size(String key, CacheType cacheType) {
        RedisTemplate redisTemplate = getredisTemplate(cacheType);
        return redisTemplate.opsForList().size(key);
    }

    @Override
    public Long leftPush(String key, String value, long timeout, CacheType cacheType) {
        RedisTemplate redisTemplate = getredisTemplate(cacheType);
        Long result = redisTemplate.opsForList().leftPush(key, value);
        if (timeout > 0) {
            expire(key, timeout, cacheType);
        }
        return result;
    }

    @Override
    public Object leftPop(String key, CacheType cacheType) {
        RedisTemplate redisTemplate = getredisTemplate(cacheType);
        return redisTemplate.opsForList().leftPop(key);
    }

    @Override
    public Long add(String key, long timeout, CacheType cacheType, String... value) {
        RedisTemplate redisTemplate = getredisTemplate(cacheType);
        Long result = 0L;
        if (value != null && value.length > 0) {
            if (value.length == 1) {
                result = redisTemplate.opsForSet().add(key, value);
            } else {
                result = redisTemplate.opsForSet().add(CollectionUtils.arrayToList(value));
            }
        }
        if (timeout > 0) {
            expire(key, timeout, cacheType);
        }
        return result;
    }

    @Override
    public Long remove(String key, CacheType cacheType, String... value) {
        RedisTemplate redisTemplate = getredisTemplate(cacheType);
        Long result = 0L;
        if (value != null && value.length > 0) {
            if (value.length == 1) {
                result = redisTemplate.opsForSet().remove(key, value);
            } else {
                result = redisTemplate.opsForSet().remove(CollectionUtils.arrayToList(value));
            }
        }
        return result;
    }

    @Override
    public Boolean isMember(String key, String value, CacheType cacheType) {
        RedisTemplate redisTemplate = getredisTemplate(cacheType);
        return redisTemplate.opsForSet().isMember(key, value);
    }

    @Override
    public Boolean expire(String key, long timeout, CacheType cacheType) {
        RedisTemplate redisTemplate = getredisTemplate(cacheType);
        return redisTemplate.expire(key, timeout, TimeUnit.SECONDS);
    }

    @Override
    public long ttl(String key, CacheType cacheType) {
        RedisTemplate redisTemplate = getredisTemplate(cacheType);
        return redisTemplate.getExpire(key, TimeUnit.SECONDS);
    }

    @Override
    public Boolean setnx(String key, String value, long timeout, CacheType cacheType) {
        RedisTemplate redisTemplate = getredisTemplate(cacheType);
        Boolean result = redisTemplate.getConnectionFactory().getConnection().setNX(key.getBytes(), value.getBytes());
        if (result && timeout > 0) {
            expire(key, timeout, cacheType);
        }
        return result;
    }

    @Override
    public long incrForLimit(String key, int timeout, CacheType cacheType) {
        RedisTemplate redisTemplate = getredisTemplate(cacheType);
        long result = (long) redisTemplate.execute(defaultRedisScript, Collections.singletonList(key), 1L, timeout);
        return result;
    }

}