package com.wsq.common.base.tools;

import com.alibaba.fastjson.JSON;
import com.ctrip.framework.apollo.model.ConfigChangeEvent;
import com.ctrip.framework.apollo.spring.annotation.ApolloConfigChangeListener;
import com.wsq.common.base.constant.Constant;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.Enumeration;
import java.util.Map;
import java.util.TreeMap;

@Component
public class SignUtil {
    @Value("${sign.info}")
    private String signInfo;

    private volatile Map<String, String> signMap;

    @ApolloConfigChangeListener
    private void someOnChange(ConfigChangeEvent changeEvent) {
        //update injected value of batch if it is changed in Apollo
        if (changeEvent.isChanged("sign.info")) {
            signMap = (Map) JSON.parse(signInfo);
        }
    }

    public boolean checkSign(HttpServletRequest request) {
        if (signMap == null) {
            signMap = (Map) JSON.parse(signInfo);
        }

        //排序
        Map<String, Object> paramMap = new TreeMap<>();
        Enumeration<String> params = request.getParameterNames();

        while (params.hasMoreElements()) {
            String key = params.nextElement().toLowerCase();
            if (key.equals(Constant.SIGN)) {
                continue;
            }
            paramMap.put(key, request.getParameter(key));
        }

        //base64
        String paramJson = JsonUtil.toJson(paramMap);
        String paramBase64 = Utils.base64Encoder(paramJson);

        //MD5
        String appId = request.getParameter(Constant.APPID);
        String secretKey = signMap.get(appId);
        String paramMd5 = Utils.MD5(paramBase64 + secretKey);

        //比较
        String sign = request.getParameter(Constant.SIGN);
        if (sign.equals(paramMd5)) {
            return true;
        }

        return false;
    }
}
