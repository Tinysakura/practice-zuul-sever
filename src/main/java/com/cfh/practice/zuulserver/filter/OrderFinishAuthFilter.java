package com.cfh.practice.zuulserver.filter;

import com.cfh.practice.zuulserver.common.HttpConstants;
import com.cfh.practice.zuulserver.utils.CookieUtil;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.netflix.zuul.filters.support.FilterConstants;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

/**
 * @Author: cfh
 * @Date: 2018/9/24 19:50
 * @Description:
 */
@Slf4j
@Component
public class OrderFinishAuthFilter extends ZuulFilter {

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Override
    public String filterType() {
        return FilterConstants.PRE_TYPE;
    }

    @Override
    public int filterOrder() {
        return FilterConstants.PRE_DECORATION_FILTER_ORDER - 1;
    }

    @Override
    public boolean shouldFilter() {
        RequestContext requestContext = RequestContext.getCurrentContext();
        HttpServletRequest request = requestContext.getRequest();
        return request.getRequestURI().equals("/order/order/finish");
    }

    @Override
    public Object run() throws ZuulException {
        RequestContext requestContext = RequestContext.getCurrentContext();
        HttpServletRequest request = requestContext.getRequest();
        log.info("拦截{}请求", request.getRequestURI());

        String token = CookieUtil.readSellerLoginToken(request);

        if (token == null) {
            requestContext.setSendZuulResponse(false);
            requestContext.setResponseStatusCode(HttpConstants.UNAUTHORIZED);
            log.info("卖家鉴权不通过，cookie中没有携带token");

            return null;
        }

        String openid = redisTemplate.opsForValue().get(token);

        if (openid == null) {
            requestContext.setSendZuulResponse(false);
            requestContext.setResponseStatusCode(HttpConstants.UNAUTHORIZED);
            log.info("卖家鉴权不提供，Redis中没有对应的openid");
        }

        return null;
    }
}
