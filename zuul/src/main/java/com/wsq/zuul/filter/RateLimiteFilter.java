package com.wsq.zuul.filter;

import com.alibaba.fastjson.JSON;
import com.ctrip.framework.apollo.model.ConfigChangeEvent;
import com.ctrip.framework.apollo.spring.annotation.ApolloConfigChangeListener;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.wsq.common.cache.CacheService;
import com.wsq.common.cache.CacheType;
import com.wsq.zuul.constant.LimitType;
import com.wsq.zuul.pojo.RateLimite;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class RateLimiteFilter extends ZuulFilter {

    @Autowired
    private CacheService cacheService;

    private Map<String, RateLimite> uriMap = new ConcurrentHashMap<>();
    private Map<String, RateLimite> modelMap = new ConcurrentHashMap<>();

    @Override
    public String filterType() {
        return "pre";
    }

    @Override
    public int filterOrder() {
        return 0;
    }

    @Override
    public boolean shouldFilter() {
        return true;
    }

    @Value("${limit.type}")
    private LimitType limitType;

    @Value("${ratelimite.model}")
    private String modelRateLimite;

    @Value("${rateLimite.uri}")
    private String uriRateLimite;

    @PostConstruct
    public void transformConfig() {
        uriMap = (Map) JSON.parse(uriRateLimite);
        modelMap = (Map) JSON.parse(modelRateLimite);
    }

    @ApolloConfigChangeListener
    private void someOnChange(ConfigChangeEvent changeEvent) {
        //update injected value of batch if it is changed in Apollo
        if (changeEvent.isChanged("rateLimite.uri")) {
            uriMap = (Map) JSON.parse(uriRateLimite);
        }
        if (changeEvent.isChanged("rateLimite.model")) {
            modelMap = (Map) JSON.parse(modelRateLimite);
        }
    }

    @Override
    public Object run() {
        RequestContext context = RequestContext.getCurrentContext();
        HttpServletRequest request = context.getRequest();
        HttpServletResponse response = context.getResponse();

        switch (limitType) {
            case IP:
                //ip的次数和间隔时间，配置中心配置，所有ip使用统一频率。后期考虑增加白名单。
                ipRateLimite(request);
                break;
            case MODEL:
                //model,如果没有配置，默认10次/s
                modelRateLimite(request);
                break;
            case URI:
                //指定uri，如果没有配置，默认1次/s
                uriRateLimite(request);
                break;
        }

        return null;
    }

    private void ipRateLimite(HttpServletRequest request) {
        String ip = request.getRemoteUser();
        String key = "limit_ip_" + ip;
        Integer result = cacheService.get(key, CacheType.PASSPORT);

        exec(result, key, 10, 1000);
    }

    private void modelRateLimite(HttpServletRequest request) {
        String serviceId = request.getContextPath();
        String key = "limit_model_" + serviceId;
        Integer result = cacheService.get(key, CacheType.PASSPORT);
        RateLimite rateLimite = modelMap.get(serviceId);

        if (rateLimite == null) {
            rateLimite = new RateLimite();
            rateLimite.setCount(10);
            rateLimite.setTime(1000);
        }

        exec(result, key, rateLimite.getCount(), rateLimite.getTime());
    }

    private void uriRateLimite(HttpServletRequest request) {
        String routeHost = request.getRequestURI();
        String key = "limit_uri_" + routeHost;
        Integer result = cacheService.get(key, CacheType.PASSPORT);
        RateLimite rateLimite = uriMap.get(routeHost);

        if (rateLimite == null) {
            rateLimite = new RateLimite();
            rateLimite.setCount(1);
            rateLimite.setTime(1000);
        }

        exec(result, key, rateLimite.getCount(), rateLimite.getTime());
    }

    private void exec(Integer result, String key, int count, int time) {
        if (result != null && result > count) {
            System.out.println("接口被限流");
            throw new RuntimeException("接口被限流");
        } else {
            try {
                cacheService.incrForLimit(key, time, CacheType.PASSPORT);
            } catch (Exception ignored) {
            }
        }
    }
}
