package com.wsq.zuul.filter;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.wsq.common.base.exception.ServiceException;
import com.wsq.zuul.constant.ExceptionCode;
import com.wsq.common.base.tools.SignUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;


@Component
public class SecurityFilter extends ZuulFilter {
    @Autowired
    private SignUtil signUtil;

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

    @Override
    public Object run() {
        RequestContext context = RequestContext.getCurrentContext();
        HttpServletRequest request = context.getRequest();

        //验签
        verifySign(request);

        return null;
    }

    /**
     * 参数签名验证
     *
     * @param request
     */
    private void verifySign(HttpServletRequest request) {
        if (!signUtil.checkSign(request)) {
            throw new ServiceException(ExceptionCode.REQUEST_SIGN_ERROR.getCode(), ExceptionCode.REQUEST_SIGN_ERROR.getMsg());
        }
    }
}
