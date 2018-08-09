package com.wsq.zuul.filter;

import com.google.common.collect.Maps;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.wsq.common.cache.CacheService;
import com.wsq.common.cache.CacheType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletResponse;
import java.util.Map;

@Component
public class RateLimiteFilter extends ZuulFilter{
    @Autowired
    private CacheService cacheService;
    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    private Map<String, Integer> map = Maps.newConcurrentMap();

    @Override
    public String filterType() {
        return null;
    }

    @Override
    public int filterOrder() {
        return 0;
    }

    @Override
    public boolean shouldFilter() {
        return false;
    }

    @Override
    public Object run() {
        RequestContext context = RequestContext.getCurrentContext();
        HttpServletResponse response = context.getResponse();
        String key = null;

        // 针对具体接口限流
        String routeHost = context.getRequest().getRequestURI();
        if (routeHost != null) {
            map.putIfAbsent(routeHost, 2);
        }

        key = "limit_" + routeHost;
        Integer result = cacheService.get(key, CacheType.PASSPORT);
        if (result != null && result > map.get(routeHost)) {
            System.out.println("接口被限流");
            throw new RuntimeException("接口被限流");
        } else {
            try {
                cacheService.incrForLimit(key, 1000, CacheType.PASSPORT);
            } catch (Exception e) {}
        }

        return null;
    }
}
