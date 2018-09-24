package com.cfh.practice.zuulserver.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.cloud.netflix.zuul.filters.ZuulProperties;
import org.springframework.stereotype.Component;

/**
 * @Author: cfh
 * @Date: 2018/9/24 11:56
 * @Description: 给zuul添加动态刷新的能力
 */
@Component
public class ZuulConfig {

    @RefreshScope
    @ConfigurationProperties("/zuul")
    public ZuulProperties zuulProperties(){
        return new ZuulProperties();
    }
}
