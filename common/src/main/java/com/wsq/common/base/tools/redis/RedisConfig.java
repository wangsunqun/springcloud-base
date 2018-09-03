package com.wsq.common.base.tools.redis;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import redis.clients.jedis.JedisPoolConfig;

@Configuration
public class RedisConfig {

    @Value("${spring.redis.host:}")
    private String hostName;
    @Value("${spring.redis.port:6379}")
    private int port;
    @Value("${spring.redis.password:}")
    private String passWord;
    @Value("${spring.redis.pool.max-idle:8}")
    private int maxIdl;
    @Value("${spring.redis.pool.min-idle:1}")
    private int minIdl;
    @Value("${spring.redis.database:0}")
    private int database;
    @Value("${spring.redis.timeout:2000}")
    private int timeout;
//    @Value("${spring.redis.host2}")
//    private String hostName2;

    @Bean
    public RedisConnectionFactory redisConnectionFactory() {
        if (hostName.isEmpty()) {
            return new JedisConnectionFactory();
        }
        JedisPoolConfig poolConfig = new JedisPoolConfig();
        poolConfig.setMaxIdle(maxIdl);
        poolConfig.setMinIdle(minIdl);
        poolConfig.setTestOnBorrow(false);
        poolConfig.setTestOnReturn(false);
        poolConfig.setTestWhileIdle(true);
        poolConfig.setNumTestsPerEvictionRun(10);
        poolConfig.setTimeBetweenEvictionRunsMillis(60000);
        JedisConnectionFactory jedisConnectionFactory = new JedisConnectionFactory(poolConfig);
        jedisConnectionFactory.setHostName(hostName);
        if (!passWord.isEmpty()) {
            jedisConnectionFactory.setPassword(passWord);
        }
        jedisConnectionFactory.setPort(port);
        jedisConnectionFactory.setDatabase(database);
        jedisConnectionFactory.setTimeout(timeout);
        return jedisConnectionFactory;
    }

//    @Bean
//    public RedisConnectionFactory redisConnectionFactory2(){
//        JedisPoolConfig poolConfig=new JedisPoolConfig();
//        poolConfig.setMaxIdle(maxIdl);
//        poolConfig.setMinIdle(minIdl);
//        poolConfig.setTestOnBorrow(true);
//        poolConfig.setTestOnReturn(true);
//        poolConfig.setTestWhileIdle(true);
//        poolConfig.setNumTestsPerEvictionRun(10);
//        poolConfig.setTimeBetweenEvictionRunsMillis(60000);
//        JedisConnectionFactory jedisConnectionFactory = new JedisConnectionFactory(poolConfig);
//        jedisConnectionFactory.setHostName(hostName2);
//        if(!passWord.isEmpty()){
//            jedisConnectionFactory.setPassword(passWord);
//        }
//        jedisConnectionFactory.setPort(port);
//        jedisConnectionFactory.setDatabase(database);
//        return jedisConnectionFactory;
//    }

    @Bean(name = "redisTemplate1")
    public RedisTemplate<String, Object> redisTemplateObject() throws Exception {
        RedisTemplate<String, Object> redisTemplateObject = new RedisTemplate<String, Object>();
        redisTemplateObject.setConnectionFactory(redisConnectionFactory());
        setSerializer(redisTemplateObject);
        redisTemplateObject.afterPropertiesSet();
        return redisTemplateObject;
    }

//    @Bean(name = "redisTemplate2")
//    public RedisTemplate<String, Object> redisTemplateObject2() throws Exception {
//        RedisTemplate<String, Object> redisTemplateObject = new RedisTemplate<String, Object>();
//        redisTemplateObject.setConnectionFactory(redisConnectionFactory2());
//        setSerializer(redisTemplateObject);
//        redisTemplateObject.afterPropertiesSet();
//        return redisTemplateObject;
//    }


    private void setSerializer(RedisTemplate<String, Object> template) {
        Jackson2JsonRedisSerializer<Object> jackson2JsonRedisSerializer = new Jackson2JsonRedisSerializer<Object>(Object.class);
        ObjectMapper om = new ObjectMapper();
        om.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
        om.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);
        jackson2JsonRedisSerializer.setObjectMapper(om);
        template.setKeySerializer(template.getStringSerializer());
        template.setValueSerializer(jackson2JsonRedisSerializer);
        template.setHashValueSerializer(jackson2JsonRedisSerializer);
        //在使用String的数据结构的时候使用这个来更改序列化方式
        /*RedisSerializer<String> stringSerializer = new StringRedisSerializer();
        template.setKeySerializer(stringSerializer );
        template.setValueSerializer(stringSerializer );
        template.setHashKeySerializer(stringSerializer );
        template.setHashValueSerializer(stringSerializer );*/

    }
}
