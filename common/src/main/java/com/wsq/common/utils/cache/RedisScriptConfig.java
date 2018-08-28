package com.wsq.common.utils.cache;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.data.redis.core.script.DefaultRedisScript;

@Configuration
public class RedisScriptConfig {

    @Bean (name = "incrLuaScript")
    public DefaultRedisScript defaultRedisScript(){
        PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
        DefaultRedisScript defaultRedisScript = new DefaultRedisScript();
        defaultRedisScript.setResultType(Long.class);
        defaultRedisScript.setLocation(resolver.getResource("classpath:incrbyWithoutUpdateExpireTime.lua"));
        return defaultRedisScript;
    }
}