package com.cfh.practice.zuulserver.filter;

import com.cfh.practice.zuulserver.common.HttpConstants;
import com.cfh.practice.zuulserver.utils.CookieUtil;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.netflix.zuul.filters.support.FilterConstants;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

import static org.springframework.cloud.netflix.zuul.filters.support.FilterConstants.PRE_DECORATION_FILTER_ORDER;

/**
 * @Author: cfh
 * @Date: 2018/9/24 19:21
 * @Description:
 */
@Slf4j
@Component
public class OrderCreateAuthFilter extends ZuulFilter {

    @Override
    public String filterType() {
        return FilterConstants.PRE_TYPE;
    }

    @Override
    public int filterOrder() {
        return PRE_DECORATION_FILTER_ORDER - 1;
    }

    @Override
    public boolean shouldFilter() {
        RequestContext requestContext = RequestContext.getCurrentContext();
        HttpServletRequest request = requestContext.getRequest();
        return request.getRequestURI().equals("/order/order/create");
    }

    @Override
    public Object run() throws ZuulException {
        RequestContext requestContext = RequestContext.getCurrentContext();
        HttpServletRequest request = requestContext.getRequest();

        log.info("拦截{}请求", request.getRequestURI());

        if (CookieUtil.readBuyerLoginToken(request) == null) {
            log.info("买家鉴权不通过,cookie中没有携带openid");
            requestContext.setSendZuulResponse(false);
            requestContext.setResponseStatusCode(HttpConstants.UNAUTHORIZED);
        }

        return null;
    }
}
