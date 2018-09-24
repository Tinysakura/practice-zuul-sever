package com.cfh.practice.zuulserver.exception;

import com.netflix.zuul.exception.ZuulException;

/**
 * @Author: cfh
 * @Date: 2018/9/24 14:07
 * @Description: 请求被限流异常
 */
public class RatelimitException extends ZuulException {

    public RatelimitException(String sMessage, int nStatusCode, String errorCause) {
        super(sMessage, nStatusCode, errorCause);
    }

    public RatelimitException(Throwable throwable, String sMessage, int nStatusCode, String errorCause) {
        super(throwable, sMessage, nStatusCode, errorCause);
    }
}
