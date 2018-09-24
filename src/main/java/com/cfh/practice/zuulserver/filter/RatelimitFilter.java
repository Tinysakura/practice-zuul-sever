package com.cfh.practice.zuulserver.filter;

import com.cfh.practice.zuulserver.exception.RatelimitException;
import com.google.common.util.concurrent.RateLimiter;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.exception.ZuulException;
import org.springframework.cloud.netflix.zuul.filters.support.FilterConstants;
import org.springframework.stereotype.Component;

/**
 * @Author: cfh
 * @Date: 2018/9/24 14:08
 * @Description: 使用令牌桶算法构建请求限流过滤器
 */
@Component
public class RatelimitFilter extends ZuulFilter {
    //每秒放入1000个令牌
    private static RateLimiter rateLimiter = RateLimiter.create(1000);

    @Override
    public String filterType() {
        return FilterConstants.PRE_TYPE;
    }

    /**
     * 由于是限流过滤器所以过滤级别应该在所有过滤器之前
     * @return
     */
    @Override
    public int filterOrder() {
        return FilterConstants.SERVLET_DETECTION_FILTER_ORDER - 1;
    }

    @Override
    public boolean shouldFilter() {
        return true;
    }

    @Override
    public Object run() throws ZuulException {
        if (!rateLimiter.tryAcquire()) {
            throw new RatelimitException("请求被限流", 401, "请求被限流");
        }

        return null;
    }
}
