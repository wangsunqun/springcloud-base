package com.wsq.zuul.filter;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.ctrip.framework.apollo.model.ConfigChangeEvent;
import com.ctrip.framework.apollo.spring.annotation.ApolloConfigChangeListener;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.wsq.common.base.exception.SystemException;
import com.wsq.common.base.tools.redis.CacheService;
import com.wsq.common.base.tools.redis.CacheType;
import com.wsq.zuul.constant.RateLimiteType;
import com.wsq.zuul.pojo.RateLimit;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

@Component
public class RateLimiteFilter extends ZuulFilter {

    @Autowired
    private CacheService cacheService;

    protected Logger log = Logger.getLogger(getClass());

    private Map<String, RateLimit> uriMap = new HashMap<>();
    private Map<String, RateLimit> modelMap = new HashMap<>();

    @Override
    public String filterType() {
        return "pre";
    }

    @Override
    public int filterOrder() {
        return 1;
    }

    @Override
    public boolean shouldFilter() {
        return true;
    }

    @Value("${ratelimit.type}")
    private RateLimiteType rateLimiteType;

    @Value("${ratelimit.ip.count:0}")
    private int ipCount;

    @Value("${ratelimit.ip.time:0}")
    private int ipTime;

    @Value("${ratelimit.model:}")
    private String modelRateLimit;

    @Value("${ratelimit.uri:}")
    private String uriRateLimit;

    @PostConstruct
    public void transformConfig() {
        switch (rateLimiteType) {
            case IP:
                break;
            case MODEL:
                modelMap = JSON.parseObject(modelRateLimit, new TypeReference<Map<String, RateLimit>>() {
                });
                break;
            case URI:
                uriMap = JSON.parseObject(uriRateLimit, new TypeReference<Map<String, RateLimit>>() {
                });
                break;
            default:
                throw new SystemException("限流类型配置错误");
        }
    }

    //model&uri hotUpdate by apollo
    @ApolloConfigChangeListener
    private void someOnChange(ConfigChangeEvent changeEvent) {
        if (changeEvent.isChanged("rateLimite.uri")) {
            uriMap = JSON.parseObject(uriRateLimit, new TypeReference<Map<String, RateLimit>>() {
            });
        }
        if (changeEvent.isChanged("rateLimite.model")) {
            modelMap = JSON.parseObject(modelRateLimit, new TypeReference<Map<String, RateLimit>>() {
            });
        }
    }

    //main
    @Override
    public Object run() {
        RequestContext context = RequestContext.getCurrentContext();
        HttpServletRequest request = context.getRequest();

        switch (rateLimiteType) {
            case IP:
                //ip的次数和间隔时间，配置中心配置，所有ip使用统一频率。后期考虑增加白名单。
                if (ipCount != 0 && ipTime != 0) {
                    ipRateLimite(request);
                }
                break;
            case MODEL:
                //model,如果没有配置
                modelRateLimite(request);
                break;
            case URI:
                //指定uri，如果没有配置
                uriRateLimite(request);
                break;
            default:
                throw new SystemException("限流类型配置错误");
        }

        return null;
    }

    //===============   各个模块业务处理   =================//
    private void ipRateLimite(HttpServletRequest request) {
        String ip = getIp(request);
        String key = "limit_ip_" + ip;
        Integer result = cacheService.get(key, CacheType.PASSPORT);

        exec(result, key, ipCount, ipTime);
    }

    private void modelRateLimite(HttpServletRequest request) {
        String serviceId = getServiceId(request.getRequestURI());
        String key = "limit_model_" + serviceId;
        Integer result = cacheService.get(key, CacheType.PASSPORT);
        RateLimit rateLimit = modelMap.get(serviceId);

        if (rateLimit != null) {
            exec(result, key, rateLimit.getCount(), rateLimit.getTime());
        }
    }

    private void uriRateLimite(HttpServletRequest request) {
        String routeHost = request.getRequestURI();
        String key = "limit_uri_" + routeHost;
        Integer result = cacheService.get(key, CacheType.PASSPORT);
        RateLimit rateLimit = uriMap.get(routeHost);

        if (rateLimit != null) {
            exec(result, key, rateLimit.getCount(), rateLimit.getTime());
        }
    }

    //===============   执行脚本   =================//
    private void exec(Integer result, String key, int count, int time) {
        if (result != null && result > count) {
            System.out.println("接口被限流");
            throw new RuntimeException("接口被限流");
        } else {
            try {
                cacheService.incrForLimit(key, time, CacheType.PASSPORT);
            } catch (Exception ignored) {
                log.error("接口限流统计失败", ignored);
            }
        }
    }

    //===============   信息获取工具   =================//
    private String getIp(HttpServletRequest request) {
        String Xip = request.getHeader("X-Real-IP");
        String XFor = request.getHeader("X-Forwarded-For");
        if (StringUtils.isNotEmpty(XFor) && !"unKnown".equalsIgnoreCase(XFor)) {
            //多次反向代理后会有多个ip值，第一个ip才是真实ip
            int index = XFor.indexOf(",");
            if (index != -1) {
                return XFor.substring(0, index);
            } else {
                return XFor;
            }
        }
        XFor = Xip;
        if (StringUtils.isNotEmpty(XFor) && !"unKnown".equalsIgnoreCase(XFor)) {
            return XFor;
        }
        if (StringUtils.isBlank(XFor) || "unknown".equalsIgnoreCase(XFor)) {
            XFor = request.getHeader("Proxy-Client-IP");
        }
        if (StringUtils.isBlank(XFor) || "unknown".equalsIgnoreCase(XFor)) {
            XFor = request.getHeader("WL-Proxy-Client-IP");
        }
        if (StringUtils.isBlank(XFor) || "unknown".equalsIgnoreCase(XFor)) {
            XFor = request.getHeader("HTTP_CLIENT_IP");
        }
        if (StringUtils.isBlank(XFor) || "unknown".equalsIgnoreCase(XFor)) {
            XFor = request.getHeader("HTTP_X_FORWARDED_FOR");
        }
        if (StringUtils.isBlank(XFor) || "unknown".equalsIgnoreCase(XFor)) {
            XFor = request.getRemoteAddr();
        }
        return XFor;
    }

    private String getServiceId(String uri) {
        return uri.split("/")[1];
    }
}
